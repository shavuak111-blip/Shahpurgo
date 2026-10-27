package com.example.data

import android.content.Context
import android.net.Uri
import android.util.Log
import com.example.R
import com.example.model.FirestoreProduct
import com.example.model.ProductCategory
import com.example.model.Shop
import com.example.util.OperationType
import com.example.util.handleFirestoreError
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.snapshots
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.storage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import kotlin.coroutines.cancellation.CancellationException

class FirestoreProductRepository(
  private val db: FirebaseFirestore,
  private val auth: FirebaseAuth = Firebase.auth,
  private val storage: FirebaseStorage = Firebase.storage
) {
  constructor(context: Context) : this(
    FirebaseFirestore.getInstance(
      context.applicationContext.getString(R.string.firestore_database_id)
    ),
    Firebase.auth,
    Firebase.storage
  )

  /**
   * Observes live products in real time.
   * If [shopId] is provided (and not "ALL"), filters strictly for that shop's products.
   */
  fun observeProducts(shopId: String? = null): Flow<List<FirestoreProduct>> {
    val collectionRef = db.collection(COLLECTION_PRODUCTS)
    val query: Query = if (!shopId.isNullOrBlank() && shopId != "ALL") {
      collectionRef.whereEqualTo("shopId", shopId)
    } else {
      collectionRef
    }

    return query
      .snapshots()
      .map { snapshot ->
        snapshot.toObjects(
          FirestoreProduct::class.java,
          DocumentSnapshot.ServerTimestampBehavior.ESTIMATE
        )
      }
      .catch { e ->
        if (e is CancellationException) throw e
        handleFirestoreError(e as Exception, OperationType.LIST, COLLECTION_PRODUCTS)
        throw e
      }
  }

  /**
   * Saves or updates a product in Firestore and uploads/persists its image.
   */
  suspend fun saveProduct(
    context: Context,
    product: FirestoreProduct,
    imageUri: Uri?
  ): Result<ProductSaveResult> = withContext(Dispatchers.IO) {
    val currentUser = auth.currentUser
    val adminUid = currentUser?.uid ?: product.adminUid
    val adminEmail = currentUser?.email ?: product.adminEmail

    if (adminUid.isBlank()) {
      return@withContext Result.failure(IllegalStateException("Shop Admin must be authenticated to manage products."))
    }

    val finalProductId = if (product.productId.isNotBlank()) product.productId else "PRD-${System.currentTimeMillis()}"
    var finalImageUrl = product.imageUrl
    var storageNotice: String? = null

    // Process image if a new URI was selected from gallery or camera
    if (imageUri != null) {
      try {
        // 1. Persist local copy in private storage
        val localFile = saveImageLocally(context, imageUri, finalProductId)
        finalImageUrl = Uri.fromFile(localFile).toString()

        // 2. Attempt upload to Firebase Storage
        try {
          val storageRef = storage.reference.child("products/${product.shopId}/$finalProductId.jpg")
          storageRef.putFile(Uri.fromFile(localFile)).await()
          val downloadUrl = storageRef.downloadUrl.await()
          finalImageUrl = downloadUrl.toString()
          Log.d(TAG, "Uploaded image to Firebase Storage: $finalImageUrl")
        } catch (storageEx: Exception) {
          Log.w(TAG, "Firebase Storage not yet activated in console; using local persistent image: ${storageEx.message}")
          storageNotice = "Image saved locally on device. To sync images to cloud across all devices, activate Firebase Storage in Firebase Console."
        }
      } catch (e: Exception) {
        Log.e(TAG, "Failed to persist image: ${e.message}", e)
      }
    }

    val docRef = db.collection(COLLECTION_PRODUCTS).document(finalProductId)
    try {
      val payload = mutableMapOf<String, Any>(
        "productId" to finalProductId,
        "name" to product.name.trim(),
        "description" to product.description.trim(),
        "category" to product.category.trim(),
        "sellingPrice" to product.sellingPrice,
        "originalPrice" to product.originalPrice,
        "stockQuantity" to product.stockQuantity,
        "unit" to product.unit.trim(),
        "isAvailable" to product.isAvailable,
        "shopId" to product.shopId.trim(),
        "shopName" to product.shopName.trim(),
        "imageUrl" to finalImageUrl,
        "adminUid" to adminUid,
        "adminEmail" to adminEmail,
        "updatedAt" to FieldValue.serverTimestamp()
      )

      val isNew = product.productId.isBlank()
      if (isNew) {
        payload["createdAt"] = FieldValue.serverTimestamp()
        docRef.set(payload).await()
      } else {
        docRef.update(payload).await()
      }

      val savedProduct = product.copy(
        productId = finalProductId,
        imageUrl = finalImageUrl,
        adminUid = adminUid,
        adminEmail = adminEmail
      )
      Result.success(ProductSaveResult(savedProduct, storageNotice))
    } catch (e: CancellationException) {
      throw e
    } catch (e: Exception) {
      handleFirestoreError(e, if (product.productId.isBlank()) OperationType.CREATE else OperationType.UPDATE, docRef.path)
      Result.failure(e)
    }
  }

  /**
   * Toggles product in-stock availability for customers in real time.
   */
  suspend fun toggleProductAvailability(productId: String, isAvailable: Boolean): Result<Unit> {
    require(productId.isNotBlank()) { "productId must not be blank" }
    val docRef = db.collection(COLLECTION_PRODUCTS).document(productId)
    return try {
      docRef.update(
        mapOf(
          "isAvailable" to isAvailable,
          "updatedAt" to FieldValue.serverTimestamp()
        )
      ).await()
      Result.success(Unit)
    } catch (e: CancellationException) {
      throw e
    } catch (e: Exception) {
      handleFirestoreError(e, OperationType.UPDATE, docRef.path)
      Result.failure(e)
    }
  }

  /**
   * Updates product stock quantity.
   */
  suspend fun updateStock(productId: String, newStock: Int): Result<Unit> {
    require(productId.isNotBlank()) { "productId must not be blank" }
    val docRef = db.collection(COLLECTION_PRODUCTS).document(productId)
    return try {
      docRef.update(
        mapOf(
          "stockQuantity" to maxOf(0, newStock),
          "isAvailable" to (newStock > 0),
          "updatedAt" to FieldValue.serverTimestamp()
        )
      ).await()
      Result.success(Unit)
    } catch (e: CancellationException) {
      throw e
    } catch (e: Exception) {
      handleFirestoreError(e, OperationType.UPDATE, docRef.path)
      Result.failure(e)
    }
  }

  /**
   * Deletes a product from Firestore.
   */
  suspend fun deleteProduct(productId: String): Result<Unit> {
    require(productId.isNotBlank()) { "productId must not be blank" }
    val docRef = db.collection(COLLECTION_PRODUCTS).document(productId)
    return try {
      docRef.delete().await()
      Result.success(Unit)
    } catch (e: CancellationException) {
      throw e
    } catch (e: Exception) {
      handleFirestoreError(e, OperationType.DELETE, docRef.path)
      Result.failure(e)
    }
  }

  /**
   * Populates initial starter products across multi-vendor categories for Shahpur merchants
   * if the products collection is currently empty.
   */
  suspend fun seedInitialProductsIfEmpty(adminUid: String, adminEmail: String) = withContext(Dispatchers.IO) {
    try {
      val existing = db.collection(COLLECTION_PRODUCTS).limit(1).get().await()
      if (!existing.isEmpty) return@withContext

      val starterProducts = listOf(
        FirestoreProduct(
          productId = "PRD-SHP-001",
          name = "Kangra Valley Organic Green Tea",
          description = "Handpicked tender tea leaves from Dharamsala & Shahpur slopes. Rich in natural antioxidants.",
          category = ProductCategory.GROCERY.id,
          sellingPrice = 180.0,
          originalPrice = 220.0,
          stockQuantity = 45,
          unit = "250 g",
          isAvailable = true,
          shopId = "shahpur-central-hub",
          shopName = "Shahpur Central Mart & Hub",
          imageUrl = "",
          adminUid = adminUid,
          adminEmail = adminEmail
        ),
        FirestoreProduct(
          productId = "PRD-SHP-002",
          name = "Kinnaur Crisp Royal Delicious Apples",
          description = "Grade-A orchard fresh apples delivered directly from Himachal hill groves.",
          category = ProductCategory.FRUITS.id,
          sellingPrice = 140.0,
          originalPrice = 180.0,
          stockQuantity = 80,
          unit = "1 kg",
          isAvailable = true,
          shopId = "dhauladhar-greens",
          shopName = "Dhauladhar Fresh Fruits & Veggies",
          imageUrl = "",
          adminUid = adminUid,
          adminEmail = adminEmail
        ),
        FirestoreProduct(
          productId = "PRD-SHP-003",
          name = "Himachali Dham Sepu Vadi Meal Box",
          description = "Authentic Kangri Dham thali featuring hot Sepu Vadi, Madra, Mah Dal and fragrant rice.",
          category = ProductCategory.RESTAURANTS.id,
          sellingPrice = 240.0,
          originalPrice = 280.0,
          stockQuantity = 30,
          unit = "1 full thali",
          isAvailable = true,
          shopId = "himachal-rasoi",
          shopName = "Himachal Rasoi Restaurant",
          imageUrl = "",
          adminUid = adminUid,
          adminEmail = adminEmail
        ),
        FirestoreProduct(
          productId = "PRD-SHP-004",
          name = "Pure Kangra Pahadi Cow Ghee",
          description = "Traditional bilona churned pure desi cow ghee with natural golden aroma.",
          category = ProductCategory.DAIRY.id,
          sellingPrice = 650.0,
          originalPrice = 750.0,
          stockQuantity = 22,
          unit = "500 ml",
          isAvailable = true,
          shopId = "kangra-dairy-bakers",
          shopName = "Kangra Valley Dairy & Sweets",
          imageUrl = "",
          adminUid = adminUid,
          adminEmail = adminEmail
        ),
        FirestoreProduct(
          productId = "PRD-SHP-005",
          name = "Himalayan Handwoven Woolen Shawl",
          description = "Warm pure merino wool shawl woven with geometric Kullu borders.",
          category = ProductCategory.FASHION.id,
          sellingPrice = 1250.0,
          originalPrice = 1600.0,
          stockQuantity = 15,
          unit = "1 pc",
          isAvailable = true,
          shopId = "himalayan-apparel",
          shopName = "Himalayan Fashion & Woollens",
          imageUrl = "",
          adminUid = adminUid,
          adminEmail = adminEmail
        ),
        FirestoreProduct(
          productId = "PRD-SHP-006",
          name = "Fast Charge USB-C Power Bank 20000mAh",
          description = "Heavy duty dual output power bank for mountain commutes and outdoor travel.",
          category = ProductCategory.ELECTRONICS.id,
          sellingPrice = 1499.0,
          originalPrice = 1999.0,
          stockQuantity = 18,
          unit = "1 unit",
          isAvailable = true,
          shopId = "shiva-electronics",
          shopName = "Shiva Electronics & Mobiles",
          imageUrl = "",
          adminUid = adminUid,
          adminEmail = adminEmail
        ),
        FirestoreProduct(
          productId = "PRD-SHP-007",
          name = "Heavy Duty Electric Drill Kit 650W",
          description = "Variable speed reversible hammer drill with 13mm chuck and 10 masonry bits.",
          category = ProductCategory.HARDWARE.id,
          sellingPrice = 2199.0,
          originalPrice = 2800.0,
          stockQuantity = 12,
          unit = "1 set",
          isAvailable = true,
          shopId = "pahadi-hardware",
          shopName = "Pahadi Hardware & Electricals",
          imageUrl = "",
          adminUid = adminUid,
          adminEmail = adminEmail
        ),
        FirestoreProduct(
          productId = "PRD-SHP-008",
          name = "Hardbound College Journal & Gel Pen Set",
          description = "Premium 300-page ruled notebook with waterproof cover for students and office use.",
          category = ProductCategory.STATIONERY.id,
          sellingPrice = 160.0,
          originalPrice = 210.0,
          stockQuantity = 50,
          unit = "1 pack",
          isAvailable = true,
          shopId = "vidya-stationers",
          shopName = "Vidya Pustak Bhandar & Stationery",
          imageUrl = "",
          adminUid = adminUid,
          adminEmail = adminEmail
        )
      )

      for (item in starterProducts) {
        val docRef = db.collection(COLLECTION_PRODUCTS).document(item.productId)
        val payload = mapOf(
          "productId" to item.productId,
          "name" to item.name,
          "description" to item.description,
          "category" to item.category,
          "sellingPrice" to item.sellingPrice,
          "originalPrice" to item.originalPrice,
          "stockQuantity" to item.stockQuantity,
          "unit" to item.unit,
          "isAvailable" to item.isAvailable,
          "shopId" to item.shopId,
          "shopName" to item.shopName,
          "imageUrl" to item.imageUrl,
          "adminUid" to adminUid,
          "adminEmail" to adminEmail,
          "createdAt" to FieldValue.serverTimestamp(),
          "updatedAt" to FieldValue.serverTimestamp()
        )
        docRef.set(payload).await()
      }
      Log.d(TAG, "Seeded initial starter products in Firestore for Shahpur vendors")
    } catch (e: Exception) {
      Log.w(TAG, "Seeding starter products skipped or failed: ${e.message}")
    }
  }

  private fun saveImageLocally(context: Context, uri: Uri, productId: String): File {
    val dir = File(context.filesDir, "products")
    if (!dir.exists()) dir.mkdirs()
    val destFile = File(dir, "${productId}_${System.currentTimeMillis()}.jpg")
    context.contentResolver.openInputStream(uri)?.use { input ->
      FileOutputStream(destFile).use { output ->
        input.copyTo(output)
      }
    }
    return destFile
  }

  companion object {
    private const val TAG = "FirestoreProductRepo"
    const val COLLECTION_PRODUCTS = "products"
  }
}

data class ProductSaveResult(
  val product: FirestoreProduct,
  val notice: String? = null
)
