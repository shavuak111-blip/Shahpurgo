package com.example.data

import android.content.Context
import android.util.Log
import com.example.R
import com.example.model.FirestoreOrder
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
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.tasks.await
import kotlin.coroutines.cancellation.CancellationException

class FirestoreOrderRepository(
  private val db: FirebaseFirestore,
  private val auth: FirebaseAuth = Firebase.auth
) {
  // Convenience constructor dynamically resolving the named database ID from string resources
  constructor(context: Context) : this(
    FirebaseFirestore.getInstance(
      context.applicationContext.getString(R.string.firestore_database_id)
    ),
    Firebase.auth
  )

  /**
   * Observes live incoming orders in real time.
   * If [customerId] is provided, filters strictly for that customer.
   * If [shopId] is provided (and != "ALL"), filters strictly for that vendor shop.
   */
  fun observeLiveOrders(shopId: String? = null, customerId: String? = null): Flow<List<FirestoreOrder>> {
    var query: Query = db.collection(COLLECTION_ORDERS)
    if (!customerId.isNullOrBlank()) {
      query = query.whereEqualTo("customerId", customerId)
    } else if (!shopId.isNullOrBlank() && shopId != "ALL") {
      query = query.whereEqualTo("shopId", shopId)
    }

    return query
      .orderBy("createdAt", Query.Direction.DESCENDING)
      .snapshots()
      .map { snapshot ->
        snapshot.toObjects(
          FirestoreOrder::class.java,
          DocumentSnapshot.ServerTimestampBehavior.ESTIMATE
        )
      }
      .catch { e ->
        if (e is CancellationException) throw e
        Log.w(TAG, "Orders query notice or auth required: ${e.message}")
        emit(emptyList())
      }
  }

  /**
   * Updates an order's status in Firestore with updated server timestamp and optional details.
   */
  suspend fun updateOrderStatus(
    orderId: String,
    newStatus: String,
    cancellationReason: String? = null,
    rating: Double? = null,
    review: String? = null
  ) {
    require(orderId.isNotBlank()) { "orderId must not be blank" }
    val docRef = db.collection(COLLECTION_ORDERS).document(orderId)
    try {
      val updates = mutableMapOf<String, Any>(
        "status" to newStatus,
        "updatedAt" to FieldValue.serverTimestamp()
      )
      if (!cancellationReason.isNullOrBlank()) {
        updates["cancellationReason"] = cancellationReason
      }
      if (rating != null && rating > 0) {
        updates["rating"] = rating
      }
      if (!review.isNullOrBlank()) {
        updates["review"] = review
      }
      docRef.update(updates).await()
      Log.d(TAG, "Order $orderId status updated to $newStatus successfully")
    } catch (e: CancellationException) {
      throw e
    } catch (e: Exception) {
      handleFirestoreError(e, OperationType.UPDATE, docRef.path)
      throw e
    }
  }

  /**
   * Creates a new customer order in Firestore.
   */
  suspend fun createOrder(order: FirestoreOrder) {
    require(order.orderId.isNotBlank()) { "orderId must not be blank" }
    val docRef = db.collection(COLLECTION_ORDERS).document(order.orderId)
    try {
      val payload = mutableMapOf<String, Any>(
        "orderId" to order.orderId,
        "customerId" to (auth.currentUser?.uid ?: order.customerId),
        "customerName" to order.customerName,
        "customerPhone" to order.customerPhone,
        "deliveryAddress" to order.deliveryAddress,
        "landmark" to order.landmark,
        "itemsSummary" to order.itemsSummary,
        "itemsCount" to order.itemsCount,
        "totalAmount" to order.totalAmount,
        "status" to order.status,
        "paymentMethod" to order.paymentMethod,
        "notes" to order.notes,
        "shopId" to order.shopId,
        "shopName" to order.shopName,
        "createdAt" to FieldValue.serverTimestamp(),
        "updatedAt" to FieldValue.serverTimestamp()
      )
      docRef.set(payload).await()
      Log.d(TAG, "Order ${order.orderId} created successfully in Firestore")
    } catch (e: CancellationException) {
      throw e
    } catch (e: Exception) {
      handleFirestoreError(e, OperationType.CREATE, docRef.path)
      throw e
    }
  }

  /**
   * Populates starter orders if the collection is empty for an authenticated admin or demo.
   */
  suspend fun seedInitialOrdersIfEmpty() {
    val uid = auth.currentUser?.uid ?: return
    try {
      val existing = db.collection(COLLECTION_ORDERS).limit(1).get().await()
      if (!existing.isEmpty) return

      val sampleOrders = listOf(
        FirestoreOrder(
          orderId = "ORD-SP-101",
          customerId = uid,
          customerName = "Aman Sharma",
          customerPhone = "+91 98160 87654",
          deliveryAddress = "Ward No. 3, Near Old Bus Stand, Shahpur",
          landmark = "Opposite State Bank",
          itemsSummary = "Kangra Valley Organic Green Tea (250g), Amul Milk (1L), Aashirvaad Atta (5kg)",
          itemsCount = 3,
          totalAmount = 445.0,
          status = "ACCEPTED",
          paymentMethod = "Cash on Delivery",
          notes = "Please call on arrival, gate code 102",
          shopId = "shahpur-central-hub",
          shopName = "Shahpur Central Mart & Hub"
        ),
        FirestoreOrder(
          orderId = "ORD-SP-102",
          customerId = uid,
          customerName = "Neha Katoch",
          customerPhone = "+91 94180 33441",
          deliveryAddress = "Chambi Chowk, Opp. PNB Bank",
          landmark = "Near NH-154 Petrol Pump",
          itemsSummary = "Kinnaur Crisp Apples (1kg), Maggi Noodles 4-Pack, Amul Butter (500g)",
          itemsCount = 3,
          totalAmount = 370.0,
          status = "PACKED",
          paymentMethod = "UPI (Prepaid)",
          notes = "Leave with security if not answering",
          shopId = "dhauladhar-greens",
          shopName = "Dhauladhar Fresh Fruits & Veggies"
        ),
        FirestoreOrder(
          orderId = "ORD-SP-103",
          customerId = uid,
          customerName = "Col. R.S. Jamwal (Retd)",
          customerPhone = "+91 98055 12908",
          deliveryAddress = "Dharamsala Road, Near ITI Shahpur",
          landmark = "Green Enclave, Villa #4",
          itemsSummary = "Pure Cow Ghee (1L), Basmati Rice (5kg), Kangra CTC Tea",
          itemsCount = 3,
          totalAmount = 980.0,
          status = "OUT_FOR_DELIVERY",
          paymentMethod = "Cash on Delivery",
          notes = "Rider contact requested",
          shopId = "kangra-dairy-bakers",
          shopName = "Kangra Valley Dairy & Sweets"
        )
      )

      for (sample in sampleOrders) {
        val docRef = db.collection(COLLECTION_ORDERS).document(sample.orderId)
        val payload = mapOf(
          "orderId" to sample.orderId,
          "customerId" to uid,
          "customerName" to sample.customerName,
          "customerPhone" to sample.customerPhone,
          "deliveryAddress" to sample.deliveryAddress,
          "landmark" to sample.landmark,
          "itemsSummary" to sample.itemsSummary,
          "itemsCount" to sample.itemsCount,
          "totalAmount" to sample.totalAmount,
          "status" to sample.status,
          "paymentMethod" to sample.paymentMethod,
          "notes" to sample.notes,
          "shopId" to sample.shopId,
          "shopName" to sample.shopName,
          "createdAt" to FieldValue.serverTimestamp(),
          "updatedAt" to FieldValue.serverTimestamp()
        )
        docRef.set(payload).await()
      }
      Log.d(TAG, "Seeded initial starter orders in Firestore")
    } catch (e: Exception) {
      Log.w(TAG, "Failed seeding starter orders: ${e.message}")
    }
  }

  companion object {
    private const val TAG = "FirestoreOrderRepo"
    const val COLLECTION_ORDERS = "orders"
  }
}
