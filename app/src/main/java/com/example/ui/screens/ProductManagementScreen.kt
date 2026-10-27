package com.example.ui.screens

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.PickVisualMediaRequest
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AddPhotoAlternate
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.CloudDone
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Inventory
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.PhotoLibrary
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material.icons.filled.Store
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.model.FirestoreProduct
import com.example.model.ProductCategory
import com.example.model.Shop
import com.example.ui.theme.BrandGreenDark
import com.example.ui.theme.BrandGreenPrimary
import com.example.ui.theme.BrandGreenSurface
import com.example.ui.theme.MountainBorder
import com.example.ui.theme.MountainCardSurface
import com.example.ui.theme.MountainSnowBackground
import com.example.ui.theme.MountainWhite
import com.example.ui.theme.RewardGold
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimaryDark
import com.example.ui.theme.TextSecondarySlate
import com.example.viewmodel.GroceryUiState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductManagementScreen(
  uiState: GroceryUiState,
  onSaveProduct: (product: FirestoreProduct, imageUri: Uri?, onComplete: (Boolean, String?) -> Unit) -> Unit,
  onDeleteProduct: (productId: String, onComplete: (Boolean, String?) -> Unit) -> Unit,
  onToggleAvailability: (productId: String, isAvailable: Boolean) -> Unit,
  onUpdateStock: (productId: String, newStock: Int) -> Unit,
  onSelectShop: (shopId: String) -> Unit,
  onSelectCategory: (category: ProductCategory) -> Unit,
  onSearchChange: (String) -> Unit,
  onClearMessages: () -> Unit,
  onRefresh: () -> Unit,
  modifier: Modifier = Modifier
) {
  val context = LocalContext.current
  val coroutineScope = rememberCoroutineScope()

  var productToEdit by remember { mutableStateOf<FirestoreProduct?>(null) }
  var isAddOrEditOpen by remember { mutableStateOf(false) }
  var productToDelete by remember { mutableStateOf<FirestoreProduct?>(null) }
  var showFirebaseStorageHelp by remember { mutableStateOf(false) }

  // Quick single-product image update state
  var quickImageTargetProduct by remember { mutableStateOf<FirestoreProduct?>(null) }
  var showImageSourceModal by remember { mutableStateOf(false) }

  // Photo pickers for quick change
  val quickGalleryPicker = rememberLauncherForActivityResult(
    contract = ActivityResultContracts.PickVisualMedia()
  ) { uri: Uri? ->
    val target = quickImageTargetProduct
    if (uri != null && target != null) {
      onSaveProduct(target, uri) { _, _ -> }
      quickImageTargetProduct = null
    }
  }

  val quickCameraLauncher = rememberLauncherForActivityResult(
    contract = ActivityResultContracts.TakePicturePreview()
  ) { bitmap: Bitmap? ->
    val target = quickImageTargetProduct
    if (bitmap != null && target != null) {
      coroutineScope.launch {
        val uri = saveBitmapToTempUri(context, bitmap)
        onSaveProduct(target, uri) { _, _ -> }
        quickImageTargetProduct = null
      }
    }
  }

  Column(
    modifier = modifier
      .fillMaxSize()
      .background(MountainSnowBackground)
  ) {
    // Top Multi-Vendor Authorization & Shop Scope Bar
    Surface(
      color = Color(0xFF0F172A),
      modifier = Modifier.fillMaxWidth()
    ) {
      Column(modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp)) {
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Row(verticalAlignment = Alignment.CenterVertically) {
            Surface(
              shape = CircleShape,
              color = if (uiState.isPlatformOwner) RewardGold else BrandGreenPrimary,
              modifier = Modifier.size(24.dp)
            ) {
              Box(contentAlignment = Alignment.Center) {
                Icon(
                  imageVector = if (uiState.isPlatformOwner) Icons.Default.Security else Icons.Default.Store,
                  contentDescription = null,
                  tint = MountainWhite,
                  modifier = Modifier.size(14.dp)
                )
              }
            }
            Spacer(modifier = Modifier.width(8.dp))
            Column {
              Text(
                text = if (uiState.isPlatformOwner) "Platform Super Owner Access" else "Authorized Shop Merchant",
                style = MaterialTheme.typography.labelMedium.copy(
                  color = MountainWhite,
                  fontWeight = FontWeight.Bold
                )
              )
              Text(
                text = if (uiState.isPlatformOwner)
                  "Managing all Shahpur vendor shops (Full Platform Privileges)"
                else
                  "Managing products for: ${Shop.findShopById(uiState.currentAdminShopId).name}",
                style = MaterialTheme.typography.labelSmall.copy(
                  color = Color(0xFF94A3B8),
                  fontSize = 11.sp
                )
              )
            }
          }

          // Live cloud badge
          Surface(
            shape = RoundedCornerShape(12.dp),
            color = BrandGreenPrimary.copy(alpha = 0.2f),
            border = androidx.compose.foundation.BorderStroke(1.dp, BrandGreenPrimary)
          ) {
            Row(
              verticalAlignment = Alignment.CenterVertically,
              modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
            ) {
              Icon(
                imageVector = Icons.Default.CloudDone,
                contentDescription = null,
                tint = BrandGreenPrimary,
                modifier = Modifier.size(14.dp)
              )
              Spacer(modifier = Modifier.width(4.dp))
              Text(
                text = "Firestore Live",
                style = MaterialTheme.typography.labelSmall.copy(
                  color = BrandGreenPrimary,
                  fontWeight = FontWeight.Bold,
                  fontSize = 10.sp
                )
              )
            }
          }
        }

        // Shop filter selector (Available when Super Owner is logged in)
        if (uiState.isPlatformOwner) {
          Spacer(modifier = Modifier.height(8.dp))
          Text(
            text = "FILTER OR MANAGE BY VENDOR SHOP:",
            style = MaterialTheme.typography.labelSmall.copy(
              color = RewardGold,
              fontWeight = FontWeight.Black,
              fontSize = 9.sp
            )
          )
          Spacer(modifier = Modifier.height(4.dp))
          LazyRow(
            horizontalArrangement = Arrangement.spacedBy(6.dp),
            modifier = Modifier.fillMaxWidth()
          ) {
            item {
              FilterChip(
                selected = uiState.selectedProductShopId == "ALL",
                onClick = { onSelectShop("ALL") },
                label = { Text("All Vendor Shops (${Shop.defaultShops.size})") },
                colors = FilterChipDefaults.filterChipColors(
                  selectedContainerColor = RewardGold,
                  selectedLabelColor = Color(0xFF0F172A),
                  containerColor = Color(0xFF1E293B),
                  labelColor = MountainWhite
                )
              )
            }
            items(Shop.defaultShops) { shop ->
              FilterChip(
                selected = uiState.selectedProductShopId == shop.id,
                onClick = { onSelectShop(shop.id) },
                label = { Text("${shop.iconEmoji} ${shop.name}") },
                colors = FilterChipDefaults.filterChipColors(
                  selectedContainerColor = BrandGreenPrimary,
                  selectedLabelColor = MountainWhite,
                  containerColor = Color(0xFF1E293B),
                  labelColor = MountainWhite
                )
              )
            }
          }
        }
      }
    }

    // Feedback or storage banner
    if (uiState.productSuccessMessage != null || uiState.productErrorMessage != null) {
      Surface(
        color = if (uiState.productErrorMessage != null) Color(0xFFFEE2E2) else BrandGreenSurface,
        modifier = Modifier.fillMaxWidth()
      ) {
        Row(
          modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 14.dp, vertical = 8.dp),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Row(
            modifier = Modifier.weight(1f),
            verticalAlignment = Alignment.CenterVertically
          ) {
            Icon(
              imageVector = if (uiState.productErrorMessage != null) Icons.Default.Warning else Icons.Default.CheckCircle,
              contentDescription = null,
              tint = if (uiState.productErrorMessage != null) Color(0xFFDC2626) else BrandGreenPrimary,
              modifier = Modifier.size(18.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
              text = uiState.productErrorMessage ?: uiState.productSuccessMessage ?: "",
              style = MaterialTheme.typography.bodySmall.copy(
                color = if (uiState.productErrorMessage != null) Color(0xFFDC2626) else BrandGreenDark,
                fontWeight = FontWeight.Medium
              )
            )
          }
          IconButton(
            onClick = onClearMessages,
            modifier = Modifier.size(24.dp)
          ) {
            Icon(Icons.Default.Close, contentDescription = "Dismiss", modifier = Modifier.size(16.dp))
          }
        }
      }
    }

    // Action Header Bar: Search, Category Bar & Add Product FAB Trigger
    Column(
      modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 14.dp, vertical = 10.dp)
    ) {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically
      ) {
        OutlinedTextField(
          value = uiState.productSearchQuery,
          onValueChange = onSearchChange,
          placeholder = { Text("Search product name, category, shop...") },
          leadingIcon = { Icon(Icons.Default.Search, contentDescription = null, tint = BrandGreenPrimary) },
          trailingIcon = {
            if (uiState.productSearchQuery.isNotEmpty()) {
              IconButton(onClick = { onSearchChange("") }) {
                Icon(Icons.Default.Close, contentDescription = "Clear")
              }
            }
          },
          shape = RoundedCornerShape(12.dp),
          modifier = Modifier
            .weight(1f)
            .testTag("product_mgmt_search_input"),
          singleLine = true,
          colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = BrandGreenPrimary,
            unfocusedBorderColor = MountainBorder
          )
        )

        Button(
          onClick = {
            productToEdit = null
            isAddOrEditOpen = true
          },
          shape = RoundedCornerShape(12.dp),
          colors = ButtonDefaults.buttonColors(containerColor = BrandGreenPrimary),
          modifier = Modifier.testTag("product_mgmt_add_btn")
        ) {
          Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(18.dp))
          Spacer(modifier = Modifier.width(4.dp))
          Text("Add Product")
        }
      }

      Spacer(modifier = Modifier.height(10.dp))

      // Multi-Vendor Category Filter Chips
      LazyRow(
        horizontalArrangement = Arrangement.spacedBy(6.dp),
        modifier = Modifier.fillMaxWidth()
      ) {
        items(ProductCategory.entries) { category ->
          FilterChip(
            selected = uiState.selectedProductCategory == category,
            onClick = { onSelectCategory(category) },
            label = { Text("${category.iconEmoji} ${category.displayName}") },
            shape = RoundedCornerShape(16.dp),
            colors = FilterChipDefaults.filterChipColors(
              selectedContainerColor = BrandGreenPrimary,
              selectedLabelColor = MountainWhite,
              containerColor = MountainCardSurface,
              labelColor = TextPrimaryDark
            )
          )
        }
      }

      Spacer(modifier = Modifier.height(6.dp))

      // Status info strip & Storage activation explanation link
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Text(
          text = "Showing ${uiState.filteredFirestoreProducts.size} products in catalog",
          style = MaterialTheme.typography.labelSmall.copy(
            color = TextSecondarySlate,
            fontWeight = FontWeight.SemiBold
          )
        )

        Row(
          verticalAlignment = Alignment.CenterVertically,
          modifier = Modifier.clickable { showFirebaseStorageHelp = !showFirebaseStorageHelp }
        ) {
          Icon(
            imageVector = Icons.Default.Info,
            contentDescription = null,
            tint = TextMuted,
            modifier = Modifier.size(14.dp)
          )
          Spacer(modifier = Modifier.width(4.dp))
          Text(
            text = "Storage & Setup Info",
            style = MaterialTheme.typography.labelSmall.copy(
              color = BrandGreenPrimary,
              fontWeight = FontWeight.Bold
            )
          )
        }
      }

      // Expandable Firebase Storage setup instructions card
      AnimatedVisibility(visible = showFirebaseStorageHelp) {
        Card(
          shape = RoundedCornerShape(12.dp),
          colors = CardDefaults.cardColors(containerColor = Color(0xFFF1F5F9)),
          border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFCBD5E1)),
          modifier = Modifier
            .fillMaxWidth()
            .padding(top = 8.dp)
        ) {
          Column(modifier = Modifier.padding(12.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
              Icon(Icons.Default.CloudDone, contentDescription = null, tint = BrandGreenDark, modifier = Modifier.size(16.dp))
              Spacer(modifier = Modifier.width(6.dp))
              Text(
                text = "Manual Firebase Setup Instructions",
                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold, color = BrandGreenDark)
              )
            }
            Spacer(modifier = Modifier.height(6.dp))
            Text(
              text = "1. Cloud Firestore: Connected & live. Products and real-time orders sync automatically.\n" +
                  "2. Firebase Authentication: Connected. Sign in with Google as admin or platform owner (shavuak111@gmail.com).\n" +
                  "3. Firebase Storage (Manual console step): In your Firebase Console, open Build > Storage > Get Started to activate your cloud storage bucket. Once enabled, uploaded photos sync to cloud URLs. Until activated, photos are safely persisted in app local storage.",
              style = MaterialTheme.typography.bodySmall.copy(color = TextSecondarySlate, lineHeight = 17.sp)
            )
          }
        }
      }
    }

    // Products List
    if (uiState.isProductsLoading && uiState.firestoreProducts.isEmpty()) {
      Box(
        modifier = Modifier
          .fillMaxSize()
          .padding(32.dp),
        contentAlignment = Alignment.Center
      ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
          CircularProgressIndicator(color = BrandGreenPrimary)
          Spacer(modifier = Modifier.height(12.dp))
          Text("Syncing products with Cloud Firestore...", style = MaterialTheme.typography.bodyMedium.copy(color = TextSecondarySlate))
        }
      }
    } else if (uiState.filteredFirestoreProducts.isEmpty()) {
      Box(
        modifier = Modifier
          .fillMaxSize()
          .padding(32.dp),
        contentAlignment = Alignment.Center
      ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
          Icon(Icons.Default.Inventory, contentDescription = null, tint = TextMuted, modifier = Modifier.size(54.dp))
          Spacer(modifier = Modifier.height(12.dp))
          Text(
            text = "No Products Found",
            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold, color = TextPrimaryDark)
          )
          Spacer(modifier = Modifier.height(4.dp))
          Text(
            text = if (uiState.productSearchQuery.isNotBlank())
              "No matches for \"${uiState.productSearchQuery}\". Try another search or category."
            else
              "No products listed yet for this shop. Tap '+ Add Product' to list your first SKU.",
            style = MaterialTheme.typography.bodySmall.copy(color = TextSecondarySlate),
            modifier = Modifier.padding(horizontal = 24.dp)
          )
          Spacer(modifier = Modifier.height(16.dp))
          Button(
            onClick = {
              productToEdit = null
              isAddOrEditOpen = true
            },
            colors = ButtonDefaults.buttonColors(containerColor = BrandGreenPrimary)
          ) {
            Icon(Icons.Default.Add, contentDescription = null)
            Spacer(modifier = Modifier.width(6.dp))
            Text("Add New Product")
          }
        }
      }
    } else {
      LazyColumn(
        modifier = Modifier
          .fillMaxSize()
          .padding(horizontal = 14.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
      ) {
        items(
          items = uiState.filteredFirestoreProducts,
          key = { it.productId }
        ) { product ->
          ProductCard(
            product = product,
            context = context,
            onEdit = {
              productToEdit = product
              isAddOrEditOpen = true
            },
            onDelete = { productToDelete = product },
            onToggleAvailability = { onToggleAvailability(product.productId, it) },
            onUpdateStock = { delta -> onUpdateStock(product.productId, product.stockQuantity + delta) },
            onChangeImageClick = {
              quickImageTargetProduct = product
              showImageSourceModal = true
            }
          )
        }

        item {
          Spacer(modifier = Modifier.height(80.dp))
        }
      }
    }
  }

  // Add / Edit Product Dialog
  if (isAddOrEditOpen) {
    AddOrEditProductDialog(
      product = productToEdit,
      isPlatformOwner = uiState.isPlatformOwner,
      defaultShopId = uiState.currentAdminShopId,
      isSaving = uiState.isProductSaving,
      onDismiss = { isAddOrEditOpen = false },
      onSave = { product, imageUri ->
        onSaveProduct(product, imageUri) { success, _ ->
          if (success) {
            isAddOrEditOpen = false
            productToEdit = null
          }
        }
      }
    )
  }

  // Delete Confirmation Dialog
  if (productToDelete != null) {
    val p = productToDelete!!
    AlertDialog(
      onDismissRequest = { productToDelete = null },
      title = { Text("Delete Product?") },
      text = { Text("Are you sure you want to permanently delete '${p.name}' from Cloud Firestore?") },
      confirmButton = {
        Button(
          onClick = {
            onDeleteProduct(p.productId) { _, _ -> }
            productToDelete = null
          },
          colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFDC2626))
        ) {
          Text("Delete", color = MountainWhite)
        }
      },
      dismissButton = {
        OutlinedButton(onClick = { productToDelete = null }) {
          Text("Cancel")
        }
      }
    )
  }

  // Quick image change modal (Gallery or Camera)
  if (showImageSourceModal) {
    AlertDialog(
      onDismissRequest = { showImageSourceModal = false },
      title = { Text("Update Product Image") },
      text = {
        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
          Text("Choose where you want to upload the photo from for '${quickImageTargetProduct?.name}':")
          OutlinedButton(
            onClick = {
              showImageSourceModal = false
              quickCameraLauncher.launch(null)
            },
            modifier = Modifier.fillMaxWidth()
          ) {
            Icon(Icons.Default.CameraAlt, contentDescription = null, tint = BrandGreenPrimary)
            Spacer(modifier = Modifier.width(8.dp))
            Text("Take Photo with Camera")
          }
          OutlinedButton(
            onClick = {
              showImageSourceModal = false
              quickGalleryPicker.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
            },
            modifier = Modifier.fillMaxWidth()
          ) {
            Icon(Icons.Default.PhotoLibrary, contentDescription = null, tint = BrandGreenPrimary)
            Spacer(modifier = Modifier.width(8.dp))
            Text("Choose from Photo Gallery")
          }
        }
      },
      confirmButton = {},
      dismissButton = {
        TextButton(onClick = { showImageSourceModal = false }) {
          Text("Cancel")
        }
      }
    )
  }
}

/**
 * Individual Product Card Component
 */
@Composable
fun ProductCard(
  product: FirestoreProduct,
  context: Context,
  onEdit: () -> Unit,
  onDelete: () -> Unit,
  onToggleAvailability: (Boolean) -> Unit,
  onUpdateStock: (Int) -> Unit,
  onChangeImageClick: () -> Unit
) {
  Card(
    shape = RoundedCornerShape(14.dp),
    colors = CardDefaults.cardColors(containerColor = MountainCardSurface),
    border = androidx.compose.foundation.BorderStroke(1.dp, MountainBorder),
    modifier = Modifier
      .fillMaxWidth()
      .testTag("product_item_${product.productId}")
  ) {
    Column(modifier = Modifier.padding(12.dp)) {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
      ) {
        // Thumbnail with Change Image Trigger Button
        Box(
          modifier = Modifier
            .size(90.dp)
            .clip(RoundedCornerShape(10.dp))
            .background(Color(0xFFE2E8F0))
            .border(1.dp, MountainBorder, RoundedCornerShape(10.dp)),
          contentAlignment = Alignment.Center
        ) {
          if (product.imageUrl.isNotBlank()) {
            AsyncImage(
              model = ImageRequest.Builder(context)
                .data(product.imageUrl)
                .crossfade(true)
                .build(),
              contentDescription = product.name,
              contentScale = ContentScale.Crop,
              modifier = Modifier.fillMaxSize()
            )
          } else {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
              Icon(
                imageVector = Icons.Default.Image,
                contentDescription = null,
                tint = TextMuted,
                modifier = Modifier.size(28.dp)
              )
              Text(
                text = "No Photo",
                style = MaterialTheme.typography.labelSmall.copy(fontSize = 9.sp, color = TextMuted)
              )
            }
          }

          // Camera overlay badge to change photo at any time
          Surface(
            shape = CircleShape,
            color = Color.Black.copy(alpha = 0.65f),
            modifier = Modifier
              .align(Alignment.BottomEnd)
              .padding(4.dp)
              .size(26.dp)
              .clickable { onChangeImageClick() }
          ) {
            Box(contentAlignment = Alignment.Center) {
              Icon(
                imageVector = Icons.Default.CameraAlt,
                contentDescription = "Change Image",
                tint = MountainWhite,
                modifier = Modifier.size(14.dp)
              )
            }
          }
        }

        // Details
        Column(
          modifier = Modifier.weight(1f),
          verticalArrangement = Arrangement.spacedBy(2.dp)
        ) {
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Top
          ) {
            Text(
              text = product.name,
              style = MaterialTheme.typography.titleMedium.copy(
                fontWeight = FontWeight.Bold,
                color = TextPrimaryDark
              ),
              maxLines = 1,
              overflow = TextOverflow.Ellipsis,
              modifier = Modifier.weight(1f)
            )

            // Category tag
            Surface(
              shape = RoundedCornerShape(6.dp),
              color = BrandGreenSurface
            ) {
              Text(
                text = ProductCategory.fromId(product.category).displayName,
                style = MaterialTheme.typography.labelSmall.copy(
                  color = BrandGreenDark,
                  fontWeight = FontWeight.Bold,
                  fontSize = 10.sp
                ),
                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
              )
            }
          }

          if (product.description.isNotBlank()) {
            Text(
              text = product.description,
              style = MaterialTheme.typography.bodySmall.copy(color = TextSecondarySlate),
              maxLines = 2,
              overflow = TextOverflow.Ellipsis
            )
          }

          Text(
            text = "Shop: ${product.shopName}",
            style = MaterialTheme.typography.labelSmall.copy(
              color = TextMuted,
              fontWeight = FontWeight.Medium
            )
          )

          // Price & Unit
          Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(6.dp),
            modifier = Modifier.padding(top = 2.dp)
          ) {
            Text(
              text = "₹${product.sellingPrice.toInt()}",
              style = MaterialTheme.typography.titleMedium.copy(
                fontWeight = FontWeight.ExtraBold,
                color = BrandGreenPrimary
              )
            )
            if (product.originalPrice > product.sellingPrice) {
              Text(
                text = "₹${product.originalPrice.toInt()}",
                style = MaterialTheme.typography.bodySmall.copy(
                  color = TextMuted,
                  textDecoration = TextDecoration.LineThrough
                )
              )
            }
            Text(
              text = "• ${product.unit}",
              style = MaterialTheme.typography.labelSmall.copy(
                color = TextSecondarySlate,
                fontWeight = FontWeight.SemiBold
              )
            )
          }
        }
      }

      HorizontalDivider(
        modifier = Modifier.padding(vertical = 8.dp),
        color = MountainBorder.copy(alpha = 0.6f)
      )

      // Stock Control, Availability Switch & Actions
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        // Stock Stepper
        Row(
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
          Surface(
            shape = CircleShape,
            color = Color(0xFFE2E8F0),
            modifier = Modifier
              .size(28.dp)
              .clickable { onUpdateStock(-1) }
          ) {
            Box(contentAlignment = Alignment.Center) {
              Text("-", fontWeight = FontWeight.Black, fontSize = 16.sp)
            }
          }
          Text(
            text = "${product.stockQuantity} in stock",
            style = MaterialTheme.typography.labelMedium.copy(
              fontWeight = FontWeight.Bold,
              color = if (product.stockQuantity <= 5) Color(0xFFDC2626) else TextPrimaryDark
            ),
            modifier = Modifier.padding(horizontal = 4.dp)
          )
          Surface(
            shape = CircleShape,
            color = Color(0xFFE2E8F0),
            modifier = Modifier
              .size(28.dp)
              .clickable { onUpdateStock(1) }
          ) {
            Box(contentAlignment = Alignment.Center) {
              Text("+", fontWeight = FontWeight.Black, fontSize = 14.sp)
            }
          }
        }

        // Available toggle
        Row(verticalAlignment = Alignment.CenterVertically) {
          Text(
            text = if (product.isAvailable) "Live" else "Hidden",
            style = MaterialTheme.typography.labelSmall.copy(
              color = if (product.isAvailable) BrandGreenDark else TextMuted,
              fontWeight = FontWeight.Bold
            )
          )
          Spacer(modifier = Modifier.width(4.dp))
          Switch(
            checked = product.isAvailable,
            onCheckedChange = onToggleAvailability,
            colors = SwitchDefaults.colors(
              checkedThumbColor = MountainWhite,
              checkedTrackColor = BrandGreenPrimary
            ),
            modifier = Modifier.size(36.dp)
          )
        }

        // Edit and Delete buttons
        Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
          IconButton(
            onClick = onEdit,
            modifier = Modifier.size(32.dp)
          ) {
            Icon(Icons.Default.Edit, contentDescription = "Edit Product", tint = BrandGreenPrimary, modifier = Modifier.size(18.dp))
          }
          IconButton(
            onClick = onDelete,
            modifier = Modifier.size(32.dp)
          ) {
            Icon(Icons.Default.Delete, contentDescription = "Delete Product", tint = Color(0xFFDC2626), modifier = Modifier.size(18.dp))
          }
        }
      }
    }
  }
}

/**
 * Add / Edit Product Dialog with Gallery, Camera & Form Validation
 */
@Composable
fun AddOrEditProductDialog(
  product: FirestoreProduct?,
  isPlatformOwner: Boolean,
  defaultShopId: String,
  isSaving: Boolean,
  onDismiss: () -> Unit,
  onSave: (FirestoreProduct, Uri?) -> Unit
) {
  val context = LocalContext.current
  val coroutineScope = rememberCoroutineScope()

  val isEditing = product != null

  var name by remember { mutableStateOf(product?.name ?: "") }
  var description by remember { mutableStateOf(product?.description ?: "") }
  var selectedCategory by remember { mutableStateOf(ProductCategory.fromId(product?.category ?: ProductCategory.GROCERY.id)) }
  var selectedShopId by remember { mutableStateOf(product?.shopId ?: defaultShopId) }
  var sellingPriceText by remember { mutableStateOf(product?.sellingPrice?.toInt()?.toString() ?: "") }
  var originalPriceText by remember { mutableStateOf(if (product != null && product.originalPrice > 0) product.originalPrice.toInt().toString() else "") }
  var stockText by remember { mutableStateOf(product?.stockQuantity?.toString() ?: "20") }
  var unitText by remember { mutableStateOf(product?.unit ?: "1 pc") }
  var isAvailable by remember { mutableStateOf(product?.isAvailable ?: true) }

  var selectedImageUri by remember { mutableStateOf<Uri?>(null) }
  var previewBitmap by remember { mutableStateOf<Bitmap?>(null) }
  var validationError by remember { mutableStateOf<String?>(null) }

  // Gallery Picker
  val galleryLauncher = rememberLauncherForActivityResult(
    contract = ActivityResultContracts.PickVisualMedia()
  ) { uri: Uri? ->
    if (uri != null) {
      selectedImageUri = uri
      previewBitmap = null
    }
  }

  // Camera Launcher
  val cameraLauncher = rememberLauncherForActivityResult(
    contract = ActivityResultContracts.TakePicturePreview()
  ) { bitmap: Bitmap? ->
    if (bitmap != null) {
      previewBitmap = bitmap
      coroutineScope.launch {
        selectedImageUri = saveBitmapToTempUri(context, bitmap)
      }
    }
  }

  AlertDialog(
    onDismissRequest = { if (!isSaving) onDismiss() },
    title = {
      Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(
          imageVector = if (isEditing) Icons.Default.Edit else Icons.Default.Add,
          contentDescription = null,
          tint = BrandGreenPrimary
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(if (isEditing) "Edit Product SKU" else "Add New Product")
      }
    },
    text = {
      Column(
        modifier = Modifier
          .fillMaxWidth()
          .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(10.dp)
      ) {
        if (validationError != null) {
          Surface(
            shape = RoundedCornerShape(8.dp),
            color = Color(0xFFFEE2E2),
            modifier = Modifier.fillMaxWidth()
          ) {
            Text(
              text = validationError!!,
              color = Color(0xFFDC2626),
              style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold),
              modifier = Modifier.padding(8.dp)
            )
          }
        }

        // Image Selection & Preview
        Text(
          text = "PRODUCT IMAGE (PHOTO)",
          style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold, color = TextSecondarySlate)
        )

        Box(
          modifier = Modifier
            .fillMaxWidth()
            .height(130.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(Color(0xFFE2E8F0))
            .border(1.dp, MountainBorder, RoundedCornerShape(12.dp)),
          contentAlignment = Alignment.Center
        ) {
          when {
            selectedImageUri != null -> {
              AsyncImage(
                model = ImageRequest.Builder(context).data(selectedImageUri).crossfade(true).build(),
                contentDescription = "Selected image preview",
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
              )
            }
            product?.imageUrl?.isNotBlank() == true -> {
              AsyncImage(
                model = ImageRequest.Builder(context).data(product.imageUrl).crossfade(true).build(),
                contentDescription = "Existing product image",
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
              )
            }
            else -> {
              Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Icon(Icons.Default.AddPhotoAlternate, contentDescription = null, tint = TextMuted, modifier = Modifier.size(36.dp))
                Spacer(modifier = Modifier.height(4.dp))
                Text("No image selected yet", style = MaterialTheme.typography.labelSmall.copy(color = TextMuted))
              }
            }
          }
        }

        // Camera and Gallery buttons
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
          OutlinedButton(
            onClick = { cameraLauncher.launch(null) },
            modifier = Modifier.weight(1f),
            shape = RoundedCornerShape(10.dp)
          ) {
            Icon(Icons.Default.CameraAlt, contentDescription = null, modifier = Modifier.size(16.dp))
            Spacer(modifier = Modifier.width(4.dp))
            Text("Camera", fontSize = 12.sp)
          }

          OutlinedButton(
            onClick = { galleryLauncher.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)) },
            modifier = Modifier.weight(1f),
            shape = RoundedCornerShape(10.dp)
          ) {
            Icon(Icons.Default.PhotoLibrary, contentDescription = null, modifier = Modifier.size(16.dp))
            Spacer(modifier = Modifier.width(4.dp))
            Text("Gallery", fontSize = 12.sp)
          }
        }

        // Product Name
        OutlinedTextField(
          value = name,
          onValueChange = { name = it; validationError = null },
          label = { Text("Product Name *") },
          placeholder = { Text("e.g. Kangra Organic Green Tea") },
          singleLine = true,
          modifier = Modifier.fillMaxWidth()
        )

        // Description
        OutlinedTextField(
          value = description,
          onValueChange = { description = it },
          label = { Text("Description") },
          placeholder = { Text("Quality, origin, packaging...") },
          maxLines = 3,
          modifier = Modifier.fillMaxWidth()
        )

        // Category Selection
        Text(
          text = "CATEGORY *",
          style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold, color = TextSecondarySlate)
        )
        LazyRow(
          horizontalArrangement = Arrangement.spacedBy(6.dp),
          modifier = Modifier.fillMaxWidth()
        ) {
          items(ProductCategory.entries.filter { it != ProductCategory.ALL }) { cat ->
            FilterChip(
              selected = selectedCategory == cat,
              onClick = { selectedCategory = cat },
              label = { Text("${cat.iconEmoji} ${cat.displayName}", fontSize = 11.sp) },
              colors = FilterChipDefaults.filterChipColors(
                selectedContainerColor = BrandGreenPrimary,
                selectedLabelColor = MountainWhite
              )
            )
          }
        }

        // Shop selection (Enabled for platform owner)
        if (isPlatformOwner) {
          Text(
            text = "ASSIGN TO VENDOR SHOP *",
            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold, color = TextSecondarySlate)
          )
          LazyRow(
            horizontalArrangement = Arrangement.spacedBy(6.dp),
            modifier = Modifier.fillMaxWidth()
          ) {
            items(Shop.defaultShops) { shop ->
              FilterChip(
                selected = selectedShopId == shop.id,
                onClick = { selectedShopId = shop.id },
                label = { Text("${shop.iconEmoji} ${shop.name}", fontSize = 11.sp) },
                colors = FilterChipDefaults.filterChipColors(
                  selectedContainerColor = RewardGold,
                  selectedLabelColor = Color(0xFF0F172A)
                )
              )
            }
          }
        }

        // Pricing: Selling Price & Original Price
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
          OutlinedTextField(
            value = sellingPriceText,
            onValueChange = { sellingPriceText = it; validationError = null },
            label = { Text("Selling Price (₹) *") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            singleLine = true,
            modifier = Modifier.weight(1f)
          )
          OutlinedTextField(
            value = originalPriceText,
            onValueChange = { originalPriceText = it },
            label = { Text("MRP (₹)") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            singleLine = true,
            modifier = Modifier.weight(1f)
          )
        }

        // Stock and Unit
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
          OutlinedTextField(
            value = stockText,
            onValueChange = { stockText = it; validationError = null },
            label = { Text("Stock Quantity *") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            singleLine = true,
            modifier = Modifier.weight(1f)
          )
          OutlinedTextField(
            value = unitText,
            onValueChange = { unitText = it },
            label = { Text("Unit (e.g. 1 kg) *") },
            singleLine = true,
            modifier = Modifier.weight(1f)
          )
        }

        // In Stock switch
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Text("Available for Customers", style = MaterialTheme.typography.bodyMedium)
          Switch(
            checked = isAvailable,
            onCheckedChange = { isAvailable = it },
            colors = SwitchDefaults.colors(checkedTrackColor = BrandGreenPrimary)
          )
        }
      }
    },
    confirmButton = {
      Button(
        onClick = {
          // Validation
          val cleanName = name.trim()
          if (cleanName.isBlank()) {
            validationError = "Product Name is required"
            return@Button
          }
          val selling = sellingPriceText.toDoubleOrNull()
          if (selling == null || selling < 0) {
            validationError = "Please enter a valid Selling Price"
            return@Button
          }
          val stock = stockText.toIntOrNull()
          if (stock == null || stock < 0) {
            validationError = "Please enter a valid Stock Quantity"
            return@Button
          }
          val unit = unitText.trim()
          if (unit.isBlank()) {
            validationError = "Unit is required (e.g. 1 kg, 500 g, 1 pc)"
            return@Button
          }
          val original = originalPriceText.toDoubleOrNull() ?: (selling * 1.15)
          val shop = Shop.findShopById(selectedShopId)

          val updatedProduct = (product ?: FirestoreProduct()).copy(
            name = cleanName,
            description = description.trim(),
            category = selectedCategory.id,
            sellingPrice = selling,
            originalPrice = original,
            stockQuantity = stock,
            unit = unit,
            isAvailable = isAvailable && stock > 0,
            shopId = shop.id,
            shopName = shop.name
          )

          onSave(updatedProduct, selectedImageUri)
        },
        enabled = !isSaving,
        colors = ButtonDefaults.buttonColors(containerColor = BrandGreenPrimary)
      ) {
        if (isSaving) {
          CircularProgressIndicator(color = MountainWhite, modifier = Modifier.size(18.dp), strokeWidth = 2.dp)
          Spacer(modifier = Modifier.width(6.dp))
          Text("Saving...")
        } else {
          Text(if (isEditing) "Save Changes" else "Add to Catalog")
        }
      }
    },
    dismissButton = {
      OutlinedButton(
        onClick = onDismiss,
        enabled = !isSaving
      ) {
        Text("Cancel")
      }
    }
  )
}

/**
 * Helper to save Bitmap to a local file in cache and return its URI
 */
private suspend fun saveBitmapToTempUri(context: Context, bitmap: Bitmap): Uri = withContext(Dispatchers.IO) {
  val tempFile = File(context.cacheDir, "camera_capture_${System.currentTimeMillis()}.jpg")
  FileOutputStream(tempFile).use { out ->
    bitmap.compress(Bitmap.CompressFormat.JPEG, 90, out)
  }
  Uri.fromFile(tempFile)
}
