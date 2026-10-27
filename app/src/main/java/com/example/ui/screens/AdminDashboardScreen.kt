package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
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
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import android.content.Intent
import android.net.Uri
import androidx.compose.material.icons.filled.CloudDone
import androidx.compose.material.icons.filled.CloudSync
import androidx.compose.ui.platform.LocalContext
import com.example.model.FirestoreOrder
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AdminPanelSettings
import androidx.compose.material.icons.filled.AttachMoney
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.DeliveryDining
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.FlashOn
import androidx.compose.material.icons.filled.Inventory
import androidx.compose.material.icons.filled.LocalShipping
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.NotificationsActive
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.PowerSettingsNew
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material.icons.filled.Store
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.FirestoreProduct
import com.example.model.GroceryCategory
import com.example.model.GroceryItem
import com.example.model.Order
import com.example.model.OrderStatus
import com.example.model.ProductCategory
import com.example.ui.theme.BrandGreenAccent
import com.example.ui.theme.BrandGreenContainer
import com.example.ui.theme.BrandGreenDark
import com.example.ui.theme.BrandGreenLight
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminDashboardScreen(
  uiState: GroceryUiState,
  onBackToCustomer: () -> Unit,
  onUpdateStock: (itemId: String, delta: Int) -> Unit,
  onToggleStockAvailability: (itemId: String) -> Unit,
  onAddNewItem: (name: String, hindiName: String, category: GroceryCategory, price: Double, costPrice: Double, unit: String, stock: Int) -> Unit,
  onUpdateOrderStatus: (orderId: String, status: OrderStatus) -> Unit,
  onRestockAllLow: () -> Unit = {},
  onSimulateNewOrder: () -> Unit = {},
  onToggleStoreAcceptingOrders: () -> Unit = {},
  onUpdateDeliveryRadius: (Int) -> Unit = {},
  onUpdateAdminPasscode: (oldPin: String, newPin: String) -> Boolean = { _, _ -> true },
  onUpdateStoreManagerName: (String) -> Unit = {},
  onUpdateFirestoreOrderStatus: (orderId: String, status: String) -> Unit = { _, _ -> },
  onCreateLiveFirestoreOrder: () -> Unit = {},
  onSignOutAdmin: () -> Unit = {},
  onSaveProduct: (product: FirestoreProduct, imageUri: Uri?, onComplete: (Boolean, String?) -> Unit) -> Unit = { _, _, _ -> },
  onDeleteProduct: (productId: String, onComplete: (Boolean, String?) -> Unit) -> Unit = { _, _ -> },
  onToggleProductAvailability: (productId: String, isAvailable: Boolean) -> Unit = { _, _ -> },
  onUpdateProductStock: (productId: String, newStock: Int) -> Unit = { _, _ -> },
  onSelectProductShop: (shopId: String) -> Unit = {},
  onSelectProductCategory: (category: ProductCategory) -> Unit = {},
  onSearchProductQuery: (String) -> Unit = {},
  onClearProductMessages: () -> Unit = {},
  onRefreshProducts: () -> Unit = {},
  modifier: Modifier = Modifier
) {
  var selectedTab by remember { mutableIntStateOf(0) }
  var showAddItemDialog by remember { mutableStateOf(false) }

  // Search & Filter state for Inventory
  var inventorySearch by remember { mutableStateOf("") }
  var selectedInventoryCategory by remember { mutableStateOf<GroceryCategory?>(null) }
  var showLowStockOnly by remember { mutableStateOf(false) }

  // Filter state for Orders
  var selectedOrderStatusFilter by remember { mutableStateOf<OrderStatus?>(null) }

  // Calculation counts
  val lowStockCount = remember(uiState.catalogue) {
    uiState.catalogue.count { it.stockQuantity <= 15 || !it.inStock }
  }
  val pendingOrdersCount = remember(uiState.orders) {
    uiState.orders.count { it.status != OrderStatus.DELIVERED }
  }

  Scaffold(
    topBar = {
      TopAppBar(
        title = {
          Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(vertical = 4.dp)
          ) {
            Box(
              modifier = Modifier
                .size(38.dp)
                .background(RewardGold, CircleShape),
              contentAlignment = Alignment.Center
            ) {
              Icon(
                imageVector = Icons.Default.AdminPanelSettings,
                contentDescription = null,
                tint = BrandGreenDark,
                modifier = Modifier.size(22.dp)
              )
            }
            Spacer(modifier = Modifier.width(10.dp))
            Column {
              Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                  text = "ShahpurGo Partner",
                  style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold,
                    color = MountainWhite
                  )
                )
                Spacer(modifier = Modifier.width(6.dp))
                Surface(
                  shape = RoundedCornerShape(4.dp),
                  color = RewardGold
                ) {
                  Text(
                    text = "STORE STAFF",
                    style = MaterialTheme.typography.labelSmall.copy(
                      color = BrandGreenDark,
                      fontWeight = FontWeight.Black,
                      fontSize = 9.sp
                    ),
                    modifier = Modifier.padding(horizontal = 4.dp, vertical = 1.dp)
                  )
                }
              }
              Text(
                text = if (uiState.isGoogleAdminSignedIn && uiState.adminUserEmail != null)
                  "Admin: ${uiState.adminUserEmail}"
                else
                  "Dark Store #01 • Main Bazar, Shahpur HP",
                style = MaterialTheme.typography.labelSmall.copy(
                  color = if (uiState.isGoogleAdminSignedIn) RewardGold else MountainWhite.copy(alpha = 0.85f),
                  fontWeight = if (uiState.isGoogleAdminSignedIn) FontWeight.Bold else FontWeight.Normal
                ),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
              )
            }
          }
        },
        actions = {
          if (uiState.isGoogleAdminSignedIn) {
            Button(
              onClick = onSignOutAdmin,
              modifier = Modifier
                .padding(end = 4.dp)
                .testTag("admin_google_sign_out_btn"),
              colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFFEF4444).copy(alpha = 0.25f),
                contentColor = Color(0xFFFCA5A5)
              ),
              shape = RoundedCornerShape(10.dp)
            ) {
              Text(
                text = "Sign Out",
                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold)
              )
            }
          }
          Button(
            onClick = onBackToCustomer,
            modifier = Modifier
              .padding(end = 8.dp)
              .testTag("admin_switch_to_customer_btn"),
            colors = ButtonDefaults.buttonColors(
              containerColor = MountainWhite.copy(alpha = 0.18f),
              contentColor = MountainWhite
            ),
            shape = RoundedCornerShape(12.dp)
          ) {
            Icon(
              imageVector = Icons.Default.Lock,
              contentDescription = "Lock Portal",
              modifier = Modifier.size(16.dp),
              tint = MountainWhite
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(
              text = "Lock & Exit",
              style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold)
            )
          }
        },
        colors = TopAppBarDefaults.topAppBarColors(containerColor = BrandGreenDark)
      )
    },
    floatingActionButton = {
      if (selectedTab == 1) {
        FloatingActionButton(
          onClick = { showAddItemDialog = true },
          containerColor = BrandGreenPrimary,
          contentColor = MountainWhite,
          modifier = Modifier.testTag("fab_admin_add_item")
        ) {
          Icon(Icons.Default.Add, contentDescription = "Add New SKU")
        }
      }
    }
  ) { paddingValues ->
    Column(
      modifier = modifier
        .fillMaxSize()
        .padding(paddingValues)
        .background(MountainSnowBackground)
    ) {
      // Store Status & Quick Metric Bar
      Surface(
        color = Color(0xFF072B1C),
        modifier = Modifier.fillMaxWidth()
      ) {
        Row(
          modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 14.dp, vertical = 8.dp),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
              modifier = Modifier
                .size(8.dp)
                .background(if (uiState.isStoreAcceptingOrders) Color(0xFF22C55E) else Color(0xFFEF4444), CircleShape)
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(
              text = if (uiState.isStoreAcceptingOrders)
                "HUB OPERATIONAL • 15 MIN DELIVERY ACTIVE"
              else
                "HUB PAUSED • NOT ACCEPTING NEW ORDERS",
              style = MaterialTheme.typography.labelSmall.copy(
                color = if (uiState.isStoreAcceptingOrders) Color(0xFF86EFAC) else Color(0xFFFCA5A5),
                fontWeight = FontWeight.Bold,
                fontSize = 10.sp
              )
            )
          }

          Text(
            text = "Today: ₹${uiState.salesReport.todayRevenue.toInt()}",
            style = MaterialTheme.typography.labelSmall.copy(
              color = RewardGold,
              fontWeight = FontWeight.ExtraBold,
              fontSize = 11.sp
            )
          )
        }
      }

      // Three Main Partner Tabs
      TabRow(
        selectedTabIndex = selectedTab,
        containerColor = MountainCardSurface,
        contentColor = BrandGreenPrimary,
        indicator = { tabPositions ->
          TabRowDefaults.SecondaryIndicator(
            modifier = Modifier.tabIndicatorOffset(tabPositions[selectedTab]),
            color = BrandGreenPrimary
          )
        }
      ) {
        // Tab 0: Orders & Dispatch
        Tab(
          selected = selectedTab == 0,
          onClick = { selectedTab = 0 },
          text = {
            Row(verticalAlignment = Alignment.CenterVertically) {
              Text(
                "Live Orders",
                style = MaterialTheme.typography.labelLarge.copy(
                  fontWeight = if (selectedTab == 0) FontWeight.Bold else FontWeight.Medium
                )
              )
              if (pendingOrdersCount > 0) {
                Spacer(modifier = Modifier.width(6.dp))
                Surface(
                  shape = CircleShape,
                  color = if (selectedTab == 0) BrandGreenPrimary else Color(0xFFE2E8F0)
                ) {
                  Text(
                    text = "$pendingOrdersCount",
                    style = MaterialTheme.typography.labelSmall.copy(
                      color = if (selectedTab == 0) MountainWhite else TextPrimaryDark,
                      fontWeight = FontWeight.Bold,
                      fontSize = 10.sp
                    ),
                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 1.dp)
                  )
                }
              }
            }
          }
        )

        // Tab 1: Multi-Vendor Product Management
        Tab(
          selected = selectedTab == 1,
          onClick = { selectedTab = 1 },
          text = {
            Row(verticalAlignment = Alignment.CenterVertically) {
              Text(
                "Products (${uiState.firestoreProducts.size})",
                style = MaterialTheme.typography.labelLarge.copy(
                  fontWeight = if (selectedTab == 1) FontWeight.Bold else FontWeight.Medium
                )
              )
              val alerts = uiState.firestoreProducts.count { !it.isAvailable || it.stockQuantity <= 5 }
              if (alerts > 0) {
                Spacer(modifier = Modifier.width(4.dp))
                Surface(
                  shape = RoundedCornerShape(4.dp),
                  color = Color(0xFFFEE2E2)
                ) {
                  Text(
                    text = "$alerts Alert",
                    style = MaterialTheme.typography.labelSmall.copy(
                      color = Color(0xFFDC2626),
                      fontWeight = FontWeight.Bold,
                      fontSize = 9.sp
                    ),
                    modifier = Modifier.padding(horizontal = 4.dp, vertical = 1.dp)
                  )
                }
              }
            }
          }
        )

        // Tab 2: Analytics & Reports
        Tab(
          selected = selectedTab == 2,
          onClick = { selectedTab = 2 },
          text = {
            Text(
              "Analytics",
              style = MaterialTheme.typography.labelLarge.copy(
                fontWeight = if (selectedTab == 2) FontWeight.Bold else FontWeight.Medium
              )
            )
          }
        )

        // Tab 3: Hub Operations & Settings
        Tab(
          selected = selectedTab == 3,
          onClick = { selectedTab = 3 },
          text = {
            Text(
              "Hub Ops",
              style = MaterialTheme.typography.labelLarge.copy(
                fontWeight = if (selectedTab == 3) FontWeight.Bold else FontWeight.Medium
              )
            )
          }
        )
      }

      // Tab Content
      when (selectedTab) {
        0 -> LiveOrdersDispatchTab(
          orders = uiState.orders,
          firestoreOrders = uiState.firestoreOrders,
          isFirestoreListening = uiState.isFirestoreListening,
          isGoogleAdminSignedIn = uiState.isGoogleAdminSignedIn,
          adminEmail = uiState.adminUserEmail,
          selectedStatusFilter = selectedOrderStatusFilter,
          onSelectFilter = { selectedOrderStatusFilter = it },
          onUpdateStatus = onUpdateOrderStatus,
          onUpdateFirestoreOrderStatus = onUpdateFirestoreOrderStatus,
          onSimulateNewOrder = onSimulateNewOrder,
          onCreateLiveFirestoreOrder = onCreateLiveFirestoreOrder
        )

        1 -> ProductManagementScreen(
          uiState = uiState,
          onSaveProduct = onSaveProduct,
          onDeleteProduct = onDeleteProduct,
          onToggleAvailability = onToggleProductAvailability,
          onUpdateStock = onUpdateProductStock,
          onSelectShop = onSelectProductShop,
          onSelectCategory = onSelectProductCategory,
          onSearchChange = onSearchProductQuery,
          onClearMessages = onClearProductMessages,
          onRefresh = onRefreshProducts
        )

        2 -> SalesAndAnalyticsTab(
          uiState = uiState
        )

        3 -> StoreOpsSettingsTab(
          uiState = uiState,
          onToggleStoreAcceptingOrders = onToggleStoreAcceptingOrders,
          onUpdateDeliveryRadius = onUpdateDeliveryRadius,
          onUpdateAdminPasscode = onUpdateAdminPasscode,
          onUpdateStoreManagerName = onUpdateStoreManagerName,
          onLogout = onBackToCustomer
        )
      }
    }
  }

  // Add Item Dialog
  if (showAddItemDialog) {
    AddNewItemDialog(
      onDismiss = { showAddItemDialog = false },
      onConfirm = { name, hindiName, cat, price, cost, unit, stock ->
        onAddNewItem(name, hindiName, cat, price, cost, unit, stock)
        showAddItemDialog = false
      }
    )
  }
}

/**
 * TAB 0: Live Orders & Dispatch Desk
 */
@Composable
fun LiveOrdersDispatchTab(
  orders: List<Order>,
  firestoreOrders: List<FirestoreOrder> = emptyList(),
  isFirestoreListening: Boolean = false,
  isGoogleAdminSignedIn: Boolean = false,
  adminEmail: String? = null,
  selectedStatusFilter: OrderStatus?,
  onSelectFilter: (OrderStatus?) -> Unit,
  onUpdateStatus: (orderId: String, status: OrderStatus) -> Unit,
  onUpdateFirestoreOrderStatus: (orderId: String, status: String) -> Unit,
  onSimulateNewOrder: () -> Unit,
  onCreateLiveFirestoreOrder: () -> Unit
) {
  val hasFirestoreOrders = firestoreOrders.isNotEmpty()

  val filteredFirestoreOrders = remember(firestoreOrders, selectedStatusFilter) {
    if (selectedStatusFilter == null) {
      firestoreOrders
    } else {
      val targetStatus = when (selectedStatusFilter) {
        OrderStatus.CONFIRMED, OrderStatus.PLACED, OrderStatus.ACCEPTED -> listOf("PLACED", "CONFIRMED", "ACCEPTED")
        OrderStatus.PACKED, OrderStatus.PACKING -> listOf("PACKING", "PACKED")
        OrderStatus.OUT_FOR_DELIVERY -> listOf("OUT_FOR_DELIVERY")
        OrderStatus.DELIVERED -> listOf("DELIVERED")
        OrderStatus.CANCELLED -> listOf("CANCELLED")
        else -> listOf("PLACED", "CONFIRMED")
      }
      firestoreOrders.filter { it.status in targetStatus }
    }
  }

  val filteredLocalOrders = remember(orders, selectedStatusFilter) {
    if (selectedStatusFilter == null) {
      orders
    } else {
      orders.filter { it.status == selectedStatusFilter }
    }
  }

  LazyColumn(
    modifier = Modifier
      .fillMaxSize()
      .padding(horizontal = 14.dp, vertical = 10.dp),
    verticalArrangement = Arrangement.spacedBy(10.dp)
  ) {
    // 1. Live Real-Time Firestore Sync Indicator Banner
    item {
      Card(
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF06291C)),
        border = androidx.compose.foundation.BorderStroke(1.dp, BrandGreenPrimary.copy(alpha = 0.4f)),
        modifier = Modifier.fillMaxWidth()
      ) {
        Row(
          modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 14.dp, vertical = 10.dp),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
              modifier = Modifier
                .size(10.dp)
                .background(if (isFirestoreListening) Color(0xFF22C55E) else RewardGold, CircleShape)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Column {
              Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                  text = "FIRESTORE REAL-TIME DISPATCH",
                  style = MaterialTheme.typography.labelSmall.copy(
                    fontWeight = FontWeight.Black,
                    color = Color(0xFFA7F3D0),
                    fontSize = 11.sp
                  )
                )
                if (isGoogleAdminSignedIn) {
                  Spacer(modifier = Modifier.width(6.dp))
                  Surface(
                    shape = RoundedCornerShape(4.dp),
                    color = RewardGold
                  ) {
                    Text(
                      text = "GOOGLE ADMIN",
                      style = MaterialTheme.typography.labelSmall.copy(
                        color = BrandGreenDark,
                        fontWeight = FontWeight.Bold,
                        fontSize = 8.sp
                      ),
                      modifier = Modifier.padding(horizontal = 4.dp, vertical = 1.dp)
                    )
                  }
                }
              }
              Text(
                text = if (isFirestoreListening)
                  "Live database listener active (${firestoreOrders.size} cloud orders)"
                else
                  "Connecting to cloud database...",
                style = MaterialTheme.typography.bodySmall.copy(
                  color = MountainWhite.copy(alpha = 0.8f),
                  fontSize = 11.sp
                )
              )
            }
          }

          // Action: Quick Live Order Creation
          Button(
            onClick = onCreateLiveFirestoreOrder,
            colors = ButtonDefaults.buttonColors(
              containerColor = BrandGreenPrimary,
              contentColor = MountainWhite
            ),
            shape = RoundedCornerShape(8.dp),
            modifier = Modifier.testTag("admin_create_live_firestore_order_btn")
          ) {
            Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(14.dp))
            Spacer(modifier = Modifier.width(4.dp))
            Text(
              text = "+ Live Order",
              style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold)
            )
          }
        }
      }
    }

    // 2. Dispatch Header Row & Filter Chips
    item {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Column {
          Text(
            text = "Live Incoming Orders",
            style = MaterialTheme.typography.titleMedium.copy(
              fontWeight = FontWeight.Bold,
              color = TextPrimaryDark
            )
          )
          Text(
            text = "Real-time updates, customer contacts & dispatch controls",
            style = MaterialTheme.typography.bodySmall.copy(color = TextMuted)
          )
        }

        OutlinedButton(
          onClick = onSimulateNewOrder,
          shape = RoundedCornerShape(10.dp),
          modifier = Modifier.testTag("admin_simulate_local_order_btn")
        ) {
          Icon(Icons.Default.FlashOn, contentDescription = null, modifier = Modifier.size(14.dp), tint = BrandGreenPrimary)
          Spacer(modifier = Modifier.width(4.dp))
          Text(
            text = "Demo Order",
            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold, color = BrandGreenDark)
          )
        }
      }
    }

    // Status Filter Chips
    item {
      val totalCount = if (hasFirestoreOrders) firestoreOrders.size else orders.size
      val placedCount = if (hasFirestoreOrders)
        firestoreOrders.count { it.status in listOf("PLACED", "CONFIRMED") }
      else
        orders.count { it.status == OrderStatus.CONFIRMED }

      val packedCount = if (hasFirestoreOrders)
        firestoreOrders.count { it.status in listOf("PACKING", "PACKED") }
      else
        orders.count { it.status == OrderStatus.PACKED }

      val outCount = if (hasFirestoreOrders)
        firestoreOrders.count { it.status == "OUT_FOR_DELIVERY" }
      else
        orders.count { it.status == OrderStatus.OUT_FOR_DELIVERY }

      val deliveredCount = if (hasFirestoreOrders)
        firestoreOrders.count { it.status == "DELIVERED" }
      else
        orders.count { it.status == OrderStatus.DELIVERED }

      LazyRow(
        horizontalArrangement = Arrangement.spacedBy(6.dp),
        modifier = Modifier.fillMaxWidth()
      ) {
        item {
          FilterChip(
            selected = selectedStatusFilter == null,
            onClick = { onSelectFilter(null) },
            label = { Text("All ($totalCount)") },
            colors = FilterChipDefaults.filterChipColors(
              selectedContainerColor = BrandGreenPrimary,
              selectedLabelColor = MountainWhite
            )
          )
        }
        item {
          FilterChip(
            selected = selectedStatusFilter == OrderStatus.CONFIRMED,
            onClick = { onSelectFilter(OrderStatus.CONFIRMED) },
            label = { Text("Placed ($placedCount)") },
            colors = FilterChipDefaults.filterChipColors(
              selectedContainerColor = Color(0xFFD97706),
              selectedLabelColor = MountainWhite
            )
          )
        }
        item {
          FilterChip(
            selected = selectedStatusFilter == OrderStatus.PACKED,
            onClick = { onSelectFilter(OrderStatus.PACKED) },
            label = { Text("Packed ($packedCount)") },
            colors = FilterChipDefaults.filterChipColors(
              selectedContainerColor = BrandGreenPrimary,
              selectedLabelColor = MountainWhite
            )
          )
        }
        item {
          FilterChip(
            selected = selectedStatusFilter == OrderStatus.OUT_FOR_DELIVERY,
            onClick = { onSelectFilter(OrderStatus.OUT_FOR_DELIVERY) },
            label = { Text("Out for Delivery ($outCount)") },
            colors = FilterChipDefaults.filterChipColors(
              selectedContainerColor = Color(0xFF8B5CF6),
              selectedLabelColor = MountainWhite
            )
          )
        }
        item {
          FilterChip(
            selected = selectedStatusFilter == OrderStatus.DELIVERED,
            onClick = { onSelectFilter(OrderStatus.DELIVERED) },
            label = { Text("Delivered ($deliveredCount)") },
            colors = FilterChipDefaults.filterChipColors(
              selectedContainerColor = Color(0xFF10B981),
              selectedLabelColor = MountainWhite
            )
          )
        }
      }
    }

    // Orders Rendering: If Firestore orders available, render them with full customer contact & live actions
    if (hasFirestoreOrders) {
      if (filteredFirestoreOrders.isEmpty()) {
        item {
          EmptyOrdersCard(onSimulate = onCreateLiveFirestoreOrder)
        }
      } else {
        items(filteredFirestoreOrders, key = { it.orderId }) { fOrder ->
          FirestoreAdminOrderCard(
            order = fOrder,
            onUpdateStatus = onUpdateFirestoreOrderStatus
          )
        }
      }
    } else {
      // Fallback local list
      if (filteredLocalOrders.isEmpty()) {
        item {
          EmptyOrdersCard(onSimulate = onSimulateNewOrder)
        }
      } else {
        items(filteredLocalOrders, key = { it.id }) { ord ->
          AdminOrderCard(
            order = ord,
            onUpdateStatus = onUpdateStatus
          )
        }
      }
    }

    item {
      Spacer(modifier = Modifier.height(24.dp))
    }
  }
}

@Composable
fun FirestoreAdminOrderCard(
  order: FirestoreOrder,
  onUpdateStatus: (orderId: String, status: String) -> Unit
) {
  val context = LocalContext.current

  val statusBgColor = when (order.status) {
    "PLACED", "CONFIRMED" -> Color(0xFFFEF3C7)
    "PACKING", "PACKED" -> BrandGreenContainer
    "OUT_FOR_DELIVERY" -> Color(0xFFEDE9FE)
    "DELIVERED" -> Color(0xFFD1FAE5)
    else -> Color(0xFFF1F5F9)
  }

  val statusTextColor = when (order.status) {
    "PLACED", "CONFIRMED" -> Color(0xFF92400E)
    "PACKING", "PACKED" -> BrandGreenDark
    "OUT_FOR_DELIVERY" -> Color(0xFF5B21B6)
    "DELIVERED" -> Color(0xFF065F46)
    else -> TextSecondarySlate
  }

  val statusDisplayName = when (order.status) {
    "PLACED", "CONFIRMED" -> "Order Placed"
    "PACKING", "PACKED" -> "Packing in Progress"
    "OUT_FOR_DELIVERY" -> "Out for Delivery"
    "DELIVERED" -> "Delivered"
    "CANCELLED" -> "Cancelled"
    else -> order.status
  }

  Card(
    shape = RoundedCornerShape(14.dp),
    colors = CardDefaults.cardColors(containerColor = MountainCardSurface),
    border = androidx.compose.foundation.BorderStroke(1.dp, MountainBorder),
    modifier = Modifier
      .fillMaxWidth()
      .testTag("admin_firestore_order_${order.orderId}")
      .animateContentSize()
  ) {
    Column(modifier = Modifier.padding(14.dp)) {
      // Header: Order ID + Cloud Badge + Status
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
          Surface(
            shape = CircleShape,
            color = BrandGreenSurface,
            modifier = Modifier.size(36.dp)
          ) {
            Box(contentAlignment = Alignment.Center) {
              Icon(
                imageVector = Icons.Default.ShoppingBag,
                contentDescription = null,
                tint = BrandGreenDark,
                modifier = Modifier.size(18.dp)
              )
            }
          }
          Spacer(modifier = Modifier.width(10.dp))
          Column {
            Row(verticalAlignment = Alignment.CenterVertically) {
              Text(
                text = "Order #${order.orderId}",
                style = MaterialTheme.typography.titleMedium.copy(
                  fontWeight = FontWeight.ExtraBold,
                  color = TextPrimaryDark
                )
              )
              Spacer(modifier = Modifier.width(6.dp))
              Surface(
                shape = RoundedCornerShape(4.dp),
                color = Color(0xFFE0F2FE)
              ) {
                Text(
                  text = "CLOUD LIVE",
                  style = MaterialTheme.typography.labelSmall.copy(
                    color = Color(0xFF0369A1),
                    fontWeight = FontWeight.Bold,
                    fontSize = 9.sp
                  ),
                  modifier = Modifier.padding(horizontal = 4.dp, vertical = 1.dp)
                )
              }
            }
            Text(
              text = "${order.itemsCount} items • ₹${order.totalAmount.toInt()} • ${order.paymentMethod}",
              style = MaterialTheme.typography.bodySmall.copy(color = TextMuted)
            )
          }
        }

        Surface(
          shape = RoundedCornerShape(8.dp),
          color = statusBgColor
        ) {
          Text(
            text = statusDisplayName,
            style = MaterialTheme.typography.labelSmall.copy(
              fontWeight = FontWeight.Bold,
              color = statusTextColor
            ),
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
          )
        }
      }

      Spacer(modifier = Modifier.height(12.dp))

      // CUSTOMER CONTACT INFORMATION CARD (MANDATORY REQUIREMENT)
      Surface(
        shape = RoundedCornerShape(10.dp),
        color = BrandGreenSurface.copy(alpha = 0.5f),
        border = androidx.compose.foundation.BorderStroke(1.dp, BrandGreenPrimary.copy(alpha = 0.25f)),
        modifier = Modifier.fillMaxWidth()
      ) {
        Column(modifier = Modifier.padding(12.dp)) {
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
              Icon(
                imageVector = Icons.Default.Person,
                contentDescription = null,
                tint = BrandGreenDark,
                modifier = Modifier.size(18.dp)
              )
              Spacer(modifier = Modifier.width(6.dp))
              Text(
                text = order.customerName.ifBlank { "Customer" },
                style = MaterialTheme.typography.titleSmall.copy(
                  fontWeight = FontWeight.Bold,
                  color = TextPrimaryDark
                )
              )
            }

            // Interactive Call Customer Button
            Button(
              onClick = {
                if (order.customerPhone.isNotBlank()) {
                  val dialIntent = Intent(Intent.ACTION_DIAL).apply {
                    data = Uri.parse("tel:${order.customerPhone}")
                  }
                  context.startActivity(dialIntent)
                }
              },
              colors = ButtonDefaults.buttonColors(
                containerColor = BrandGreenPrimary,
                contentColor = MountainWhite
              ),
              shape = RoundedCornerShape(8.dp),
              modifier = Modifier.testTag("admin_call_customer_btn_${order.orderId}")
            ) {
              Icon(Icons.Default.Phone, contentDescription = "Call Customer", modifier = Modifier.size(14.dp))
              Spacer(modifier = Modifier.width(4.dp))
              Text(
                text = order.customerPhone.ifBlank { "Call" },
                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold)
              )
            }
          }

          Spacer(modifier = Modifier.height(6.dp))

          // Delivery Address & Landmark
          Row(verticalAlignment = Alignment.Top) {
            Icon(
              imageVector = Icons.Default.LocationOn,
              contentDescription = null,
              tint = BrandGreenPrimary,
              modifier = Modifier.size(16.dp).padding(top = 2.dp)
            )
            Spacer(modifier = Modifier.width(6.dp))
            Column {
              Text(
                text = order.deliveryAddress,
                style = MaterialTheme.typography.bodySmall.copy(
                  color = TextSecondarySlate,
                  fontWeight = FontWeight.Medium
                )
              )
              if (order.landmark.isNotBlank()) {
                Text(
                  text = "Landmark: ${order.landmark}",
                  style = MaterialTheme.typography.labelSmall.copy(color = TextMuted)
                )
              }
              if (order.notes.isNotBlank()) {
                Text(
                  text = "Note: ${order.notes}",
                  style = MaterialTheme.typography.labelSmall.copy(
                    color = Color(0xFFD97706),
                    fontWeight = FontWeight.SemiBold
                  )
                )
              }
            }
          }
        }
      }

      Spacer(modifier = Modifier.height(10.dp))

      // Items Ordered Summary
      Surface(
        shape = RoundedCornerShape(8.dp),
        color = MountainSnowBackground,
        border = androidx.compose.foundation.BorderStroke(1.dp, MountainBorder),
        modifier = Modifier.fillMaxWidth()
      ) {
        Column(modifier = Modifier.padding(10.dp)) {
          Text(
            text = "🛒 Items Summary:",
            style = MaterialTheme.typography.labelSmall.copy(
              fontWeight = FontWeight.Bold,
              color = TextPrimaryDark
            )
          )
          Spacer(modifier = Modifier.height(2.dp))
          Text(
            text = order.itemsSummary.ifBlank { "Grocery items package" },
            style = MaterialTheme.typography.bodySmall.copy(color = TextSecondarySlate)
          )
        }
      }

      Spacer(modifier = Modifier.height(12.dp))

      // Real-time Order Status Actions
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
      ) {
        when (order.status) {
          "PLACED", "CONFIRMED" -> {
            Button(
              onClick = { onUpdateStatus(order.orderId, "PACKED") },
              colors = ButtonDefaults.buttonColors(
                containerColor = BrandGreenPrimary,
                contentColor = MountainWhite
              ),
              shape = RoundedCornerShape(10.dp),
              modifier = Modifier
                .weight(1f)
                .testTag("admin_accept_pack_btn_${order.orderId}")
            ) {
              Icon(Icons.Default.Inventory, contentDescription = null, modifier = Modifier.size(16.dp))
              Spacer(modifier = Modifier.width(6.dp))
              Text("Accept & Start Packing", style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold))
            }
          }

          "PACKING", "PACKED" -> {
            Button(
              onClick = { onUpdateStatus(order.orderId, "OUT_FOR_DELIVERY") },
              colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF7C3AED),
                contentColor = MountainWhite
              ),
              shape = RoundedCornerShape(10.dp),
              modifier = Modifier
                .weight(1f)
                .testTag("admin_dispatch_btn_${order.orderId}")
            ) {
              Icon(Icons.Default.LocalShipping, contentDescription = null, modifier = Modifier.size(16.dp))
              Spacer(modifier = Modifier.width(6.dp))
              Text("Dispatch / Out for Delivery", style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold))
            }
          }

          "OUT_FOR_DELIVERY" -> {
            Button(
              onClick = { onUpdateStatus(order.orderId, "DELIVERED") },
              colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF059669),
                contentColor = MountainWhite
              ),
              shape = RoundedCornerShape(10.dp),
              modifier = Modifier
                .weight(1f)
                .testTag("admin_delivered_btn_${order.orderId}")
            ) {
              Icon(Icons.Default.CheckCircle, contentDescription = null, modifier = Modifier.size(16.dp))
              Spacer(modifier = Modifier.width(6.dp))
              Text("Mark as Delivered", style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold))
            }
          }

          "DELIVERED" -> {
            Surface(
              shape = RoundedCornerShape(8.dp),
              color = Color(0xFFECFDF5),
              border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFA7F3D0)),
              modifier = Modifier.weight(1f)
            ) {
              Row(
                modifier = Modifier.padding(vertical = 8.dp, horizontal = 12.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
              ) {
                Icon(Icons.Default.CheckCircle, contentDescription = null, tint = Color(0xFF059669), modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                  text = "Order Fulfilled Safely",
                  style = MaterialTheme.typography.labelMedium.copy(
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF065F46)
                  )
                )
              }
            }
          }

          else -> {
            Text(
              text = "Status: ${order.status}",
              style = MaterialTheme.typography.labelMedium.copy(color = TextMuted)
            )
          }
        }

        // Cancel order option if not delivered
        if (order.status != "DELIVERED" && order.status != "CANCELLED") {
          OutlinedButton(
            onClick = { onUpdateStatus(order.orderId, "CANCELLED") },
            shape = RoundedCornerShape(10.dp),
            colors = ButtonDefaults.outlinedButtonColors(contentColor = Color(0xFFDC2626)),
            border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFFCA5A5)),
            modifier = Modifier.testTag("admin_cancel_btn_${order.orderId}")
          ) {
            Text("Cancel", style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold))
          }
        }
      }
    }
  }
}

@Composable
fun EmptyOrdersCard(onSimulate: () -> Unit) {
  Card(
    shape = RoundedCornerShape(12.dp),
    colors = CardDefaults.cardColors(containerColor = MountainCardSurface),
    border = androidx.compose.foundation.BorderStroke(1.dp, MountainBorder),
    modifier = Modifier.fillMaxWidth()
  ) {
    Column(
      modifier = Modifier
        .fillMaxWidth()
        .padding(32.dp),
      horizontalAlignment = Alignment.CenterHorizontally
    ) {
      Icon(
        imageVector = Icons.Default.ShoppingBag,
        contentDescription = null,
        tint = TextMuted,
        modifier = Modifier.size(40.dp)
      )
      Spacer(modifier = Modifier.height(10.dp))
      Text(
        text = "No orders found in this category",
        style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
      )
      Text(
        text = "Tap '+ Live Order' to create an incoming order at Shahpur Hub.",
        style = MaterialTheme.typography.bodySmall.copy(color = TextMuted)
      )
      Spacer(modifier = Modifier.height(14.dp))
      Button(
        onClick = onSimulate,
        colors = ButtonDefaults.buttonColors(
          containerColor = BrandGreenPrimary,
          contentColor = MountainWhite
        ),
        shape = RoundedCornerShape(10.dp)
      ) {
        Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(16.dp))
        Spacer(modifier = Modifier.width(6.dp))
        Text("Create Sample Incoming Order", style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold))
      }
    }
  }
}


@Composable
fun AdminOrderCard(
  order: Order,
  onUpdateStatus: (orderId: String, status: OrderStatus) -> Unit
) {
  var isExpanded by remember { mutableStateOf(order.status != OrderStatus.DELIVERED) }
  val checkedItems = remember { mutableStateMapOf<String, Boolean>() }

  val statusBgColor = when (order.status) {
    OrderStatus.CONFIRMED, OrderStatus.PLACED, OrderStatus.ACCEPTED -> Color(0xFFFEF3C7)
    OrderStatus.PACKING, OrderStatus.PACKED -> BrandGreenContainer
    OrderStatus.OUT_FOR_DELIVERY -> Color(0xFFEDE9FE)
    OrderStatus.DELIVERED -> Color(0xFFD1FAE5)
    OrderStatus.CANCELLED -> Color(0xFFFEE2E2)
    else -> Color(0xFFFEF3C7)
  }

  val statusTextColor = when (order.status) {
    OrderStatus.CONFIRMED, OrderStatus.PLACED, OrderStatus.ACCEPTED -> Color(0xFF92400E)
    OrderStatus.PACKING, OrderStatus.PACKED -> BrandGreenDark
    OrderStatus.OUT_FOR_DELIVERY -> Color(0xFF5B21B6)
    OrderStatus.DELIVERED -> Color(0xFF065F46)
    OrderStatus.CANCELLED -> Color(0xFFDC2626)
    else -> Color(0xFF92400E)
  }

  Card(
    shape = RoundedCornerShape(14.dp),
    colors = CardDefaults.cardColors(containerColor = MountainCardSurface),
    border = androidx.compose.foundation.BorderStroke(1.dp, MountainBorder),
    modifier = Modifier
      .fillMaxWidth()
      .testTag("admin_order_card_${order.id}")
      .animateContentSize()
  ) {
    Column(modifier = Modifier.padding(14.dp)) {
      // Header: Order ID + Status
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
          Surface(
            shape = CircleShape,
            color = BrandGreenSurface,
            modifier = Modifier.size(32.dp)
          ) {
            Box(contentAlignment = Alignment.Center) {
              Icon(
                imageVector = Icons.Default.ShoppingBag,
                contentDescription = null,
                tint = BrandGreenDark,
                modifier = Modifier.size(16.dp)
              )
            }
          }
          Spacer(modifier = Modifier.width(10.dp))
          Column {
            Text(
              text = "Order #${order.id}",
              style = MaterialTheme.typography.titleMedium.copy(
                fontWeight = FontWeight.ExtraBold,
                color = TextPrimaryDark
              )
            )
            Text(
              text = "${order.items.size} item(s) • ₹${order.total.toInt()} • ${order.paymentMethod}",
              style = MaterialTheme.typography.bodySmall.copy(color = TextMuted)
            )
          }
        }

        Surface(
          shape = RoundedCornerShape(8.dp),
          color = statusBgColor
        ) {
          Text(
            text = order.status.title,
            style = MaterialTheme.typography.labelSmall.copy(
              fontWeight = FontWeight.Bold,
              color = statusTextColor
            ),
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
          )
        }
      }

      Spacer(modifier = Modifier.height(10.dp))

      // Delivery Destination Details
      Surface(
        shape = RoundedCornerShape(10.dp),
        color = MountainSnowBackground,
        border = androidx.compose.foundation.BorderStroke(1.dp, MountainBorder),
        modifier = Modifier.fillMaxWidth()
      ) {
        Column(modifier = Modifier.padding(10.dp)) {
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
          ) {
            Text(
              text = "📍 ${order.deliveryLocation.tag} (${order.deliveryLocation.areaName})",
              style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold)
            )
            Text(
              text = "ETA: ~${order.etaMinutes}m",
              style = MaterialTheme.typography.labelSmall.copy(
                fontWeight = FontWeight.Bold,
                color = BrandGreenPrimary
              )
            )
          }
          Text(
            text = order.deliveryLocation.fullAddress,
            style = MaterialTheme.typography.bodySmall.copy(color = TextSecondarySlate),
            maxLines = 2,
            overflow = TextOverflow.Ellipsis
          )
          if (order.deliveryLocation.landmark.isNotBlank()) {
            Text(
              text = "Landmark: ${order.deliveryLocation.landmark}",
              style = MaterialTheme.typography.labelSmall.copy(color = TextMuted)
            )
          }
        }
      }

      Spacer(modifier = Modifier.height(8.dp))

      // Toggle item checklist visibility
      Row(
        modifier = Modifier
          .fillMaxWidth()
          .clickable { isExpanded = !isExpanded }
          .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Text(
          text = if (isExpanded) "Hide Picking Checklist" else "Show Packing Checklist (${order.items.size} items)",
          style = MaterialTheme.typography.labelMedium.copy(
            color = BrandGreenPrimary,
            fontWeight = FontWeight.Bold
          )
        )
        Icon(
          imageVector = if (isExpanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
          contentDescription = null,
          tint = BrandGreenPrimary
        )
      }

      // Expandable Items Checklist for store staff picking
      AnimatedVisibility(visible = isExpanded) {
        Column(
          modifier = Modifier
            .fillMaxWidth()
            .padding(top = 6.dp),
          verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
          order.items.forEach { cartItem ->
            val isChecked = checkedItems[cartItem.item.id] ?: (order.status == OrderStatus.PACKED || order.status == OrderStatus.DELIVERED)
            Surface(
              shape = RoundedCornerShape(8.dp),
              color = if (isChecked) BrandGreenSurface.copy(alpha = 0.5f) else MountainSnowBackground,
              modifier = Modifier.fillMaxWidth()
            ) {
              Row(
                modifier = Modifier
                  .fillMaxWidth()
                  .clickable { checkedItems[cartItem.item.id] = !isChecked }
                  .padding(horizontal = 8.dp, vertical = 6.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
              ) {
                Row(
                  verticalAlignment = Alignment.CenterVertically,
                  modifier = Modifier.weight(1f)
                ) {
                  Checkbox(
                    checked = isChecked,
                    onCheckedChange = { checkedItems[cartItem.item.id] = it },
                    colors = CheckboxDefaults.colors(
                      checkedColor = BrandGreenPrimary,
                      checkmarkColor = MountainWhite
                    ),
                    modifier = Modifier.size(28.dp)
                  )
                  Spacer(modifier = Modifier.width(6.dp))
                  Column {
                    Text(
                      text = "${cartItem.quantity}x ${cartItem.item.name}",
                      style = MaterialTheme.typography.bodyMedium.copy(
                        fontWeight = FontWeight.SemiBold,
                        color = TextPrimaryDark
                      )
                    )
                    Text(
                      text = "${cartItem.item.hindiName} • ${cartItem.item.unit}",
                      style = MaterialTheme.typography.labelSmall.copy(color = TextMuted)
                    )
                  }
                }
                Text(
                  text = "₹${(cartItem.item.price * cartItem.quantity).toInt()}",
                  style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold)
                )
              }
            }
          }

          Spacer(modifier = Modifier.height(4.dp))

          // Assigned Delivery Rider
          Row(
            modifier = Modifier
              .fillMaxWidth()
              .background(BrandGreenSurface, RoundedCornerShape(8.dp))
              .padding(horizontal = 10.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
              Icon(
                imageVector = Icons.Default.DeliveryDining,
                contentDescription = null,
                tint = BrandGreenDark,
                modifier = Modifier.size(20.dp)
              )
              Spacer(modifier = Modifier.width(8.dp))
              Column {
                Text(
                  text = "Assigned Rider: ${order.deliveryPartnerName}",
                  style = MaterialTheme.typography.labelMedium.copy(
                    fontWeight = FontWeight.Bold,
                    color = BrandGreenDark
                  )
                )
                Text(
                  text = "Contact: ${order.deliveryPartnerPhone}",
                  style = MaterialTheme.typography.labelSmall.copy(color = TextMuted)
                )
              }
            }
          }
        }
      }

      Spacer(modifier = Modifier.height(10.dp))

      // Status Advancement Progression Actions
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
      ) {
        when (order.status) {
          OrderStatus.CONFIRMED -> {
            Button(
              onClick = { onUpdateStatus(order.id, OrderStatus.PACKED) },
              modifier = Modifier
                .fillMaxWidth()
                .testTag("admin_action_pack_${order.id}"),
              colors = ButtonDefaults.buttonColors(containerColor = BrandGreenPrimary),
              shape = RoundedCornerShape(10.dp)
            ) {
              Icon(Icons.Default.Check, contentDescription = null, modifier = Modifier.size(18.dp))
              Spacer(modifier = Modifier.width(6.dp))
              Text("Accept & Mark Packed", style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold))
            }
          }

          OrderStatus.PACKED -> {
            Button(
              onClick = { onUpdateStatus(order.id, OrderStatus.OUT_FOR_DELIVERY) },
              modifier = Modifier
                .fillMaxWidth()
                .testTag("admin_action_dispatch_${order.id}"),
              colors = ButtonDefaults.buttonColors(
                containerColor = RewardGold,
                contentColor = BrandGreenDark
              ),
              shape = RoundedCornerShape(10.dp)
            ) {
              Icon(Icons.Default.LocalShipping, contentDescription = null, modifier = Modifier.size(18.dp))
              Spacer(modifier = Modifier.width(6.dp))
              Text("Dispatch with Rider Raman", style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold))
            }
          }

          OrderStatus.OUT_FOR_DELIVERY -> {
            Button(
              onClick = { onUpdateStatus(order.id, OrderStatus.DELIVERED) },
              modifier = Modifier
                .fillMaxWidth()
                .testTag("admin_action_deliver_${order.id}"),
              colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF10B981)),
              shape = RoundedCornerShape(10.dp)
            ) {
              Icon(Icons.Default.CheckCircle, contentDescription = null, modifier = Modifier.size(18.dp))
              Spacer(modifier = Modifier.width(6.dp))
              Text("Confirm Customer Delivery", style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold))
            }
          }

          OrderStatus.DELIVERED -> {
            Surface(
              shape = RoundedCornerShape(10.dp),
              color = Color(0xFFECFDF5),
              modifier = Modifier.fillMaxWidth()
            ) {
              Row(
                modifier = Modifier.padding(vertical = 10.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
              ) {
                Icon(Icons.Default.CheckCircle, contentDescription = null, tint = Color(0xFF059669), modifier = Modifier.size(18.dp))
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                  text = "Order Successfully Delivered to Customer",
                  style = MaterialTheme.typography.labelMedium.copy(
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF059669)
                  )
                )
              }
            }
          }

          OrderStatus.CANCELLED -> {
            Surface(
              shape = RoundedCornerShape(10.dp),
              color = Color(0xFFFEF2F2),
              modifier = Modifier.fillMaxWidth()
            ) {
              Row(
                modifier = Modifier.padding(vertical = 10.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
              ) {
                Text(
                  text = "Order Cancelled (${order.cancellationReason.ifBlank { "By Customer/Store" }})",
                  style = MaterialTheme.typography.labelMedium.copy(
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFFDC2626)
                  )
                )
              }
            }
          }

          else -> {
            Button(
              onClick = { onUpdateStatus(order.id, OrderStatus.PACKED) },
              modifier = Modifier.fillMaxWidth(),
              colors = ButtonDefaults.buttonColors(containerColor = BrandGreenPrimary),
              shape = RoundedCornerShape(10.dp)
            ) {
              Text("Accept & Pack Order")
            }
          }
        }
      }
    }
  }
}

/**
 * TAB 1: Inventory & Stock Control
 */
@Composable
fun InventoryStockManagerTab(
  catalogue: List<GroceryItem>,
  searchQuery: String,
  onSearchChange: (String) -> Unit,
  selectedCategory: GroceryCategory?,
  onSelectCategory: (GroceryCategory?) -> Unit,
  showLowStockOnly: Boolean,
  onToggleLowStockOnly: (Boolean) -> Unit,
  onUpdateStock: (itemId: String, delta: Int) -> Unit,
  onToggleAvailability: (itemId: String) -> Unit,
  onOpenAddDialog: () -> Unit,
  onRestockAllLow: () -> Unit
) {
  val filteredList = remember(catalogue, searchQuery, selectedCategory, showLowStockOnly) {
    catalogue.filter { item ->
      val matchesSearch = searchQuery.isBlank() ||
          item.name.contains(searchQuery, ignoreCase = true) ||
          item.hindiName.contains(searchQuery, ignoreCase = true)

      val matchesCategory = selectedCategory == null || item.category == selectedCategory
      val matchesLowStock = !showLowStockOnly || (item.stockQuantity <= 15 || !item.inStock)

      matchesSearch && matchesCategory && matchesLowStock
    }
  }

  val lowStockTotal = remember(catalogue) {
    catalogue.count { it.stockQuantity <= 15 || !it.inStock }
  }

  Column(
    modifier = Modifier
      .fillMaxSize()
      .padding(horizontal = 14.dp, vertical = 10.dp)
  ) {
    // Search & Add SKU Bar
    Row(
      modifier = Modifier.fillMaxWidth(),
      horizontalArrangement = Arrangement.spacedBy(8.dp),
      verticalAlignment = Alignment.CenterVertically
    ) {
      OutlinedTextField(
        value = searchQuery,
        onValueChange = onSearchChange,
        placeholder = { Text("Search product name, Hindi...") },
        leadingIcon = { Icon(Icons.Default.Search, contentDescription = null, tint = BrandGreenPrimary) },
        trailingIcon = {
          if (searchQuery.isNotEmpty()) {
            IconButton(onClick = { onSearchChange("") }) {
              Icon(Icons.Default.Close, contentDescription = "Clear")
            }
          }
        },
        shape = RoundedCornerShape(12.dp),
        modifier = Modifier
          .weight(1f)
          .testTag("admin_inventory_search"),
        singleLine = true
      )

      Button(
        onClick = onOpenAddDialog,
        shape = RoundedCornerShape(12.dp),
        colors = ButtonDefaults.buttonColors(containerColor = BrandGreenPrimary),
        modifier = Modifier.testTag("admin_add_item_btn")
      ) {
        Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(18.dp))
        Spacer(modifier = Modifier.width(4.dp))
        Text("New SKU")
      }
    }

    Spacer(modifier = Modifier.height(8.dp))

    // Category Filter Chips
    LazyRow(
      horizontalArrangement = Arrangement.spacedBy(6.dp),
      modifier = Modifier.fillMaxWidth()
    ) {
      item {
        FilterChip(
          selected = selectedCategory == null,
          onClick = { onSelectCategory(null) },
          label = { Text("All Categories") },
          colors = FilterChipDefaults.filterChipColors(
            selectedContainerColor = BrandGreenPrimary,
            selectedLabelColor = MountainWhite
          )
        )
      }
      items(GroceryCategory.values().filter { it != GroceryCategory.ALL }) { cat ->
        FilterChip(
          selected = selectedCategory == cat,
          onClick = { onSelectCategory(if (selectedCategory == cat) null else cat) },
          label = { Text("${cat.iconEmoji} ${cat.displayName}") },
          colors = FilterChipDefaults.filterChipColors(
            selectedContainerColor = BrandGreenPrimary,
            selectedLabelColor = MountainWhite
          )
        )
      }
    }

    Spacer(modifier = Modifier.height(8.dp))

    // Quick Alert / Restock Bar
    Row(
      modifier = Modifier.fillMaxWidth(),
      horizontalArrangement = Arrangement.SpaceBetween,
      verticalAlignment = Alignment.CenterVertically
    ) {
      Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.clickable { onToggleLowStockOnly(!showLowStockOnly) }
      ) {
        Switch(
          checked = showLowStockOnly,
          onCheckedChange = onToggleLowStockOnly,
          colors = SwitchDefaults.colors(
            checkedThumbColor = MountainWhite,
            checkedTrackColor = Color(0xFFDC2626)
          ),
          modifier = Modifier.size(34.dp)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
          text = "Low Stock Only ($lowStockTotal items)",
          style = MaterialTheme.typography.labelMedium.copy(
            color = if (showLowStockOnly) Color(0xFFDC2626) else TextPrimaryDark,
            fontWeight = FontWeight.Bold
          )
        )
      }

      OutlinedButton(
        onClick = onRestockAllLow,
        colors = ButtonDefaults.outlinedButtonColors(contentColor = BrandGreenDark),
        shape = RoundedCornerShape(8.dp),
        modifier = Modifier.testTag("admin_bulk_restock_btn")
      ) {
        Icon(Icons.Default.Refresh, contentDescription = null, modifier = Modifier.size(14.dp))
        Spacer(modifier = Modifier.width(4.dp))
        Text("Restock All Low", style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold))
      }
    }

    Spacer(modifier = Modifier.height(10.dp))

    // Product Catalogue List
    LazyColumn(
      verticalArrangement = Arrangement.spacedBy(8.dp),
      modifier = Modifier.fillMaxSize()
    ) {
      items(filteredList, key = { it.id }) { item ->
        InventoryItemCard(
          item = item,
          onUpdateStock = onUpdateStock,
          onToggleAvailability = onToggleAvailability
        )
      }

      item {
        Spacer(modifier = Modifier.height(70.dp))
      }
    }
  }
}

@Composable
fun InventoryItemCard(
  item: GroceryItem,
  onUpdateStock: (itemId: String, delta: Int) -> Unit,
  onToggleAvailability: (itemId: String) -> Unit
) {
  val margin = item.price - item.costPrice
  val marginPercent = if (item.price > 0) ((margin / item.price) * 100).toInt() else 0

  val isCritical = item.stockQuantity <= 5 || !item.inStock
  val isLow = item.stockQuantity in 6..15

  Card(
    shape = RoundedCornerShape(12.dp),
    colors = CardDefaults.cardColors(containerColor = MountainCardSurface),
    border = androidx.compose.foundation.BorderStroke(
      1.dp,
      if (!item.inStock || item.stockQuantity == 0) Color(0xFFFCA5A5) else MountainBorder
    ),
    modifier = Modifier
      .fillMaxWidth()
      .testTag("admin_inventory_row_${item.id}")
  ) {
    Column(modifier = Modifier.padding(12.dp)) {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.Top
      ) {
        Column(modifier = Modifier.weight(1f)) {
          Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
              text = item.name,
              style = MaterialTheme.typography.titleSmall.copy(
                fontWeight = FontWeight.Bold,
                color = TextPrimaryDark
              )
            )
            Spacer(modifier = Modifier.width(6.dp))
            Surface(
              shape = RoundedCornerShape(4.dp),
              color = when {
                !item.inStock || item.stockQuantity == 0 -> Color(0xFFFEE2E2)
                isCritical -> Color(0xFFFEE2E2)
                isLow -> Color(0xFFFEF3C7)
                else -> BrandGreenSurface
              }
            ) {
              Text(
                text = when {
                  !item.inStock || item.stockQuantity == 0 -> "OUT OF STOCK"
                  isCritical -> "CRITICAL (${item.stockQuantity})"
                  isLow -> "LOW STOCK (${item.stockQuantity})"
                  else -> "${item.stockQuantity} IN STOCK"
                },
                style = MaterialTheme.typography.labelSmall.copy(
                  fontSize = 9.sp,
                  fontWeight = FontWeight.Black,
                  color = when {
                    !item.inStock || item.stockQuantity == 0 -> Color(0xFFDC2626)
                    isCritical -> Color(0xFFDC2626)
                    isLow -> Color(0xFFD97706)
                    else -> BrandGreenDark
                  }
                ),
                modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp)
              )
            }
          }

          Text(
            text = "${item.hindiName} • ${item.unit} • ${item.category.displayName}",
            style = MaterialTheme.typography.labelSmall.copy(color = TextMuted)
          )

          Spacer(modifier = Modifier.height(4.dp))

          // Financial breakdown
          Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
              text = "Sell: ₹${item.price.toInt()} | Cost: ₹${item.costPrice.toInt()}",
              style = MaterialTheme.typography.labelSmall.copy(
                fontWeight = FontWeight.SemiBold,
                color = TextPrimaryDark
              )
            )
            Spacer(modifier = Modifier.width(8.dp))
            Surface(
              shape = RoundedCornerShape(4.dp),
              color = BrandGreenSurface
            ) {
              Text(
                text = "+₹${margin.toInt()} ($marginPercent% margin)",
                style = MaterialTheme.typography.labelSmall.copy(
                  color = BrandGreenPrimary,
                  fontWeight = FontWeight.Bold,
                  fontSize = 9.sp
                ),
                modifier = Modifier.padding(horizontal = 4.dp, vertical = 1.dp)
              )
            }
          }
        }

        // In-Stock Switch
        Column(horizontalAlignment = Alignment.End) {
          Switch(
            checked = item.inStock,
            onCheckedChange = { onToggleAvailability(item.id) },
            colors = SwitchDefaults.colors(
              checkedThumbColor = MountainWhite,
              checkedTrackColor = BrandGreenPrimary
            ),
            modifier = Modifier.size(36.dp)
          )
          Text(
            text = if (item.inStock) "Active" else "Disabled",
            style = MaterialTheme.typography.labelSmall.copy(
              fontSize = 9.sp,
              color = if (item.inStock) BrandGreenPrimary else Color(0xFFDC2626),
              fontWeight = FontWeight.Bold
            )
          )
        }
      }

      HorizontalDivider(
        color = MountainBorder.copy(alpha = 0.5f),
        modifier = Modifier.padding(vertical = 8.dp)
      )

      // Stepper Controls
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Text(
          text = "Inventory Count:",
          style = MaterialTheme.typography.labelSmall.copy(color = TextMuted)
        )

        Row(
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
          // -5
          Surface(
            shape = RoundedCornerShape(6.dp),
            color = MountainSnowBackground,
            border = androidx.compose.foundation.BorderStroke(1.dp, MountainBorder),
            modifier = Modifier
              .clickable { onUpdateStock(item.id, -5) }
              .padding(2.dp)
          ) {
            Text(
              "-5",
              style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
              modifier = Modifier.padding(horizontal = 6.dp, vertical = 4.dp)
            )
          }

          // -1
          Surface(
            shape = RoundedCornerShape(6.dp),
            color = MountainSnowBackground,
            border = androidx.compose.foundation.BorderStroke(1.dp, MountainBorder),
            modifier = Modifier
              .clickable { onUpdateStock(item.id, -1) }
              .padding(2.dp)
          ) {
            Icon(Icons.Default.Remove, contentDescription = "-1", modifier = Modifier.size(16.dp))
          }

          // Count Display
          Surface(
            shape = RoundedCornerShape(6.dp),
            color = BrandGreenSurface,
            modifier = Modifier.padding(horizontal = 4.dp)
          ) {
            Text(
              text = "${item.stockQuantity}",
              style = MaterialTheme.typography.titleSmall.copy(
                fontWeight = FontWeight.ExtraBold,
                color = BrandGreenDark
              ),
              modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
            )
          }

          // +1
          Surface(
            shape = RoundedCornerShape(6.dp),
            color = MountainSnowBackground,
            border = androidx.compose.foundation.BorderStroke(1.dp, MountainBorder),
            modifier = Modifier
              .clickable { onUpdateStock(item.id, 1) }
              .padding(2.dp)
          ) {
            Icon(Icons.Default.Add, contentDescription = "+1", modifier = Modifier.size(16.dp))
          }

          // +5
          Surface(
            shape = RoundedCornerShape(6.dp),
            color = MountainSnowBackground,
            border = androidx.compose.foundation.BorderStroke(1.dp, MountainBorder),
            modifier = Modifier
              .clickable { onUpdateStock(item.id, 5) }
              .padding(2.dp)
          ) {
            Text(
              "+5",
              style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
              modifier = Modifier.padding(horizontal = 6.dp, vertical = 4.dp)
            )
          }
        }
      }
    }
  }
}

/**
 * TAB 2: Sales & Profit Analytics
 */
@Composable
fun SalesAndAnalyticsTab(uiState: GroceryUiState) {
  val report = uiState.salesReport

  LazyColumn(
    modifier = Modifier
      .fillMaxSize()
      .padding(14.dp),
    verticalArrangement = Arrangement.spacedBy(14.dp)
  ) {
    // 1. KPI Cards Grid
    item {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(10.dp)
      ) {
        // Today's Revenue
        Card(
          modifier = Modifier.weight(1f),
          colors = CardDefaults.cardColors(containerColor = MountainCardSurface),
          shape = RoundedCornerShape(12.dp),
          border = androidx.compose.foundation.BorderStroke(1.dp, MountainBorder)
        ) {
          Column(modifier = Modifier.padding(12.dp)) {
            Row(
              verticalAlignment = Alignment.CenterVertically,
              horizontalArrangement = Arrangement.SpaceBetween,
              modifier = Modifier.fillMaxWidth()
            ) {
              Text("Today's Revenue", style = MaterialTheme.typography.labelSmall.copy(color = TextMuted))
              Icon(Icons.Default.TrendingUp, contentDescription = null, tint = BrandGreenPrimary, modifier = Modifier.size(16.dp))
            }
            Spacer(modifier = Modifier.height(4.dp))
            Text(
              text = "₹${report.todayRevenue.toInt()}",
              style = MaterialTheme.typography.titleLarge.copy(
                fontWeight = FontWeight.ExtraBold,
                color = BrandGreenDark
              )
            )
            Text("+18.4% vs yesterday", style = MaterialTheme.typography.labelSmall.copy(color = BrandGreenLight, fontSize = 9.sp))
          }
        }

        // Net Profit
        Card(
          modifier = Modifier.weight(1f),
          colors = CardDefaults.cardColors(containerColor = MountainCardSurface),
          shape = RoundedCornerShape(12.dp),
          border = androidx.compose.foundation.BorderStroke(1.dp, MountainBorder)
        ) {
          Column(modifier = Modifier.padding(12.dp)) {
            Row(
              verticalAlignment = Alignment.CenterVertically,
              horizontalArrangement = Arrangement.SpaceBetween,
              modifier = Modifier.fillMaxWidth()
            ) {
              Text("Gross Profit", style = MaterialTheme.typography.labelSmall.copy(color = TextMuted))
              Icon(Icons.Default.AttachMoney, contentDescription = null, tint = RewardGold, modifier = Modifier.size(16.dp))
            }
            Spacer(modifier = Modifier.height(4.dp))
            Text(
              text = "₹${report.todayProfit.toInt()}",
              style = MaterialTheme.typography.titleLarge.copy(
                fontWeight = FontWeight.ExtraBold,
                color = BrandGreenPrimary
              )
            )
            Text("${report.profitMarginPercent}% store margin", style = MaterialTheme.typography.labelSmall.copy(color = BrandGreenLight, fontSize = 9.sp))
          }
        }
      }
    }

    item {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(10.dp)
      ) {
        // Orders Completed
        Card(
          modifier = Modifier.weight(1f),
          colors = CardDefaults.cardColors(containerColor = MountainCardSurface),
          shape = RoundedCornerShape(12.dp),
          border = androidx.compose.foundation.BorderStroke(1.dp, MountainBorder)
        ) {
          Column(modifier = Modifier.padding(12.dp)) {
            Text("Orders Completed", style = MaterialTheme.typography.labelSmall.copy(color = TextMuted))
            Spacer(modifier = Modifier.height(4.dp))
            Text(
              text = "${report.totalOrders}",
              style = MaterialTheme.typography.titleLarge.copy(
                fontWeight = FontWeight.Bold,
                color = TextPrimaryDark
              )
            )
            Text("Avg Delivery: 16 mins", style = MaterialTheme.typography.labelSmall.copy(color = TextMuted, fontSize = 9.sp))
          }
        }

        // Average Order Value
        Card(
          modifier = Modifier.weight(1f),
          colors = CardDefaults.cardColors(containerColor = MountainCardSurface),
          shape = RoundedCornerShape(12.dp),
          border = androidx.compose.foundation.BorderStroke(1.dp, MountainBorder)
        ) {
          Column(modifier = Modifier.padding(12.dp)) {
            Text("Average Order Value", style = MaterialTheme.typography.labelSmall.copy(color = TextMuted))
            Spacer(modifier = Modifier.height(4.dp))
            Text(
              text = "₹${report.averageOrderValue.toInt()}",
              style = MaterialTheme.typography.titleLarge.copy(
                fontWeight = FontWeight.Bold,
                color = TextPrimaryDark
              )
            )
            Text("Kangra Valley Hub", style = MaterialTheme.typography.labelSmall.copy(color = TextMuted, fontSize = 9.sp))
          }
        }
      }
    }

    // 2. Sales Hourly Bar Chart (Canvas)
    item {
      Card(
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = MountainCardSurface),
        border = androidx.compose.foundation.BorderStroke(1.dp, MountainBorder),
        modifier = Modifier
          .fillMaxWidth()
          .testTag("hourly_sales_chart_card")
      ) {
        Column(modifier = Modifier.padding(14.dp)) {
          Text(
            text = "Today's Hourly Revenue Trend (Shahpur)",
            style = MaterialTheme.typography.titleMedium.copy(
              fontWeight = FontWeight.Bold,
              color = TextPrimaryDark
            )
          )
          Text(
            text = "Peak ordering times: 12 PM - 2 PM & 6 PM - 8 PM",
            style = MaterialTheme.typography.labelSmall.copy(color = TextMuted)
          )

          Spacer(modifier = Modifier.height(14.dp))

          // Bar Chart Canvas
          val maxVal = report.hourlyRevenue.maxOfOrNull { it.second } ?: 5000.0
          Box(
            modifier = Modifier
              .fillMaxWidth()
              .height(140.dp)
          ) {
            Canvas(modifier = Modifier.fillMaxSize()) {
              val w = size.width
              val h = size.height
              val count = report.hourlyRevenue.size
              val barWidth = (w / count) * 0.55f
              val step = w / count

              report.hourlyRevenue.forEachIndexed { i, pair ->
                val barHeight = ((pair.second / maxVal) * (h * 0.8f)).toFloat()
                val left = i * step + (step - barWidth) / 2
                val top = h - barHeight

                drawRoundRect(
                  color = if (i == 5) Color(0xFF0B7A48) else Color(0xFF86EFAC),
                  topLeft = Offset(left, top),
                  size = Size(barWidth, barHeight),
                  cornerRadius = CornerRadius(6f, 6f)
                )
              }
            }
          }

          // Labels
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
          ) {
            report.hourlyRevenue.forEach {
              Text(
                text = it.first,
                style = MaterialTheme.typography.labelSmall.copy(
                  fontSize = 9.sp,
                  color = TextMuted
                )
              )
            }
          }
        }
      }
    }

    // 3. Top Selling Products in Shahpur
    item {
      Card(
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = MountainCardSurface),
        border = androidx.compose.foundation.BorderStroke(1.dp, MountainBorder),
        modifier = Modifier.fillMaxWidth()
      ) {
        Column(modifier = Modifier.padding(14.dp)) {
          Text(
            text = "Top Selling Items in Shahpur",
            style = MaterialTheme.typography.titleMedium.copy(
              fontWeight = FontWeight.Bold,
              color = TextPrimaryDark
            )
          )

          Spacer(modifier = Modifier.height(10.dp))

          report.topSellingItemNames.forEachIndexed { index, pair ->
            Row(
              modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 5.dp),
              horizontalArrangement = Arrangement.SpaceBetween,
              verticalAlignment = Alignment.CenterVertically
            ) {
              Row(verticalAlignment = Alignment.CenterVertically) {
                Surface(
                  shape = CircleShape,
                  color = BrandGreenSurface,
                  modifier = Modifier.size(24.dp)
                ) {
                  Box(contentAlignment = Alignment.Center) {
                    Text(
                      text = "${index + 1}",
                      style = MaterialTheme.typography.labelSmall.copy(
                        fontWeight = FontWeight.Bold,
                        color = BrandGreenPrimary
                      )
                    )
                  }
                }
                Spacer(modifier = Modifier.width(10.dp))
                Text(
                  text = pair.first,
                  style = MaterialTheme.typography.bodyMedium.copy(
                    fontWeight = FontWeight.SemiBold,
                    color = TextPrimaryDark
                  )
                )
              }

              Text(
                text = "${pair.second} units sold",
                style = MaterialTheme.typography.labelMedium.copy(
                  color = BrandGreenPrimary,
                  fontWeight = FontWeight.Bold
                )
              )
            }

            if (index < report.topSellingItemNames.size - 1) {
              HorizontalDivider(color = MountainBorder.copy(alpha = 0.5f))
            }
          }
        }
      }
    }

    // 4. Dark Store Operational Speed
    item {
      Card(
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = MountainCardSurface),
        border = androidx.compose.foundation.BorderStroke(1.dp, MountainBorder),
        modifier = Modifier.fillMaxWidth()
      ) {
        Column(modifier = Modifier.padding(14.dp)) {
          Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(Icons.Default.Speed, contentDescription = null, tint = BrandGreenPrimary, modifier = Modifier.size(20.dp))
            Spacer(modifier = Modifier.width(8.dp))
            Text(
              text = "Shahpur Hub Fulfillment Speed",
              style = MaterialTheme.typography.titleMedium.copy(
                fontWeight = FontWeight.Bold,
                color = TextPrimaryDark
              )
            )
          }

          Spacer(modifier = Modifier.height(8.dp))

          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
          ) {
            Surface(
              shape = RoundedCornerShape(8.dp),
              color = BrandGreenSurface,
              modifier = Modifier.weight(1f)
            ) {
              Column(modifier = Modifier.padding(10.dp)) {
                Text("Avg Packing", style = MaterialTheme.typography.labelSmall.copy(color = TextMuted))
                Text("3.2 mins", style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold, color = BrandGreenDark))
              }
            }

            Surface(
              shape = RoundedCornerShape(8.dp),
              color = BrandGreenSurface,
              modifier = Modifier.weight(1f)
            ) {
              Column(modifier = Modifier.padding(10.dp)) {
                Text("Avg Delivery", style = MaterialTheme.typography.labelSmall.copy(color = TextMuted))
                Text("12.8 mins", style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold, color = BrandGreenPrimary))
              }
            }

            Surface(
              shape = RoundedCornerShape(8.dp),
              color = BrandGreenSurface,
              modifier = Modifier.weight(1f)
            ) {
              Column(modifier = Modifier.padding(10.dp)) {
                Text("On-Time Rate", style = MaterialTheme.typography.labelSmall.copy(color = TextMuted))
                Text("98.6%", style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold, color = BrandGreenDark))
              }
            }
          }
        }
      }
    }

    item {
      Spacer(modifier = Modifier.height(24.dp))
    }
  }
}

/**
 * Add New Item Dialog
 */
@Composable
fun AddNewItemDialog(
  onDismiss: () -> Unit,
  onConfirm: (name: String, hindiName: String, cat: GroceryCategory, price: Double, cost: Double, unit: String, stock: Int) -> Unit
) {
  var name by remember { mutableStateOf("") }
  var hindiName by remember { mutableStateOf("") }
  var priceStr by remember { mutableStateOf("") }
  var costStr by remember { mutableStateOf("") }
  var unitStr by remember { mutableStateOf("1 kg") }
  var stockStr by remember { mutableStateOf("30") }
  var selectedCategory by remember { mutableStateOf(GroceryCategory.FRESH_PRODUCE) }

  val sellingPrice = priceStr.toDoubleOrNull() ?: 0.0
  val costPrice = costStr.toDoubleOrNull() ?: 0.0
  val calculatedMargin = sellingPrice - costPrice
  val calculatedMarginPercent = if (sellingPrice > 0) ((calculatedMargin / sellingPrice) * 100).toInt() else 0

  AlertDialog(
    onDismissRequest = onDismiss,
    title = {
      Row(verticalAlignment = Alignment.CenterVertically) {
        Box(
          modifier = Modifier
            .size(34.dp)
            .background(BrandGreenSurface, CircleShape),
          contentAlignment = Alignment.Center
        ) {
          Icon(Icons.Default.Add, contentDescription = null, tint = BrandGreenDark)
        }
        Spacer(modifier = Modifier.width(10.dp))
        Text("Add New Grocery SKU", style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold))
      }
    },
    text = {
      Column(
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier.verticalScroll(rememberScrollState())
      ) {
        OutlinedTextField(
          value = name,
          onValueChange = { name = it },
          label = { Text("Product Name (English)") },
          placeholder = { Text("e.g. Kangra Organic Garlic") },
          singleLine = true,
          modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
          value = hindiName,
          onValueChange = { hindiName = it },
          label = { Text("Hindi / Pahadi Name") },
          placeholder = { Text("e.g. कांगड़ा देसी लहसुन") },
          singleLine = true,
          modifier = Modifier.fillMaxWidth()
        )

        Text("Select Category:", style = MaterialTheme.typography.labelSmall.copy(color = TextMuted))
        LazyRow(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
          items(GroceryCategory.values().filter { it != GroceryCategory.ALL }) { cat ->
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

        Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
          OutlinedTextField(
            value = priceStr,
            onValueChange = { priceStr = it },
            label = { Text("Selling ₹") },
            placeholder = { Text("50") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.weight(1f),
            singleLine = true
          )
          OutlinedTextField(
            value = costStr,
            onValueChange = { costStr = it },
            label = { Text("Cost ₹") },
            placeholder = { Text("35") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.weight(1f),
            singleLine = true
          )
        }

        // Live Margin Preview
        if (sellingPrice > 0 && costPrice > 0) {
          Surface(
            shape = RoundedCornerShape(8.dp),
            color = BrandGreenSurface,
            modifier = Modifier.fillMaxWidth()
          ) {
            Row(
              modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
              horizontalArrangement = Arrangement.SpaceBetween,
              verticalAlignment = Alignment.CenterVertically
            ) {
              Text(
                text = "Gross Margin: ₹${calculatedMargin.toInt()} / unit",
                style = MaterialTheme.typography.labelSmall.copy(color = BrandGreenDark, fontWeight = FontWeight.Bold)
              )
              Text(
                text = "$calculatedMarginPercent% margin",
                style = MaterialTheme.typography.labelSmall.copy(color = BrandGreenPrimary, fontWeight = FontWeight.Black)
              )
            }
          }
        }

        Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
          OutlinedTextField(
            value = unitStr,
            onValueChange = { unitStr = it },
            label = { Text("Unit / Pack") },
            placeholder = { Text("1 kg, 500g") },
            modifier = Modifier.weight(1f),
            singleLine = true
          )
          OutlinedTextField(
            value = stockStr,
            onValueChange = { stockStr = it },
            label = { Text("Initial Stock") },
            placeholder = { Text("30") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.weight(1f),
            singleLine = true
          )
        }
      }
    },
    confirmButton = {
      Button(
        onClick = {
          val p = priceStr.toDoubleOrNull() ?: 50.0
          val c = costStr.toDoubleOrNull() ?: (p * 0.75)
          val s = stockStr.toIntOrNull() ?: 25
          if (name.isNotBlank()) {
            onConfirm(name, hindiName.ifBlank { name }, selectedCategory, p, c, unitStr, s)
          }
        },
        colors = ButtonDefaults.buttonColors(containerColor = BrandGreenPrimary),
        enabled = name.isNotBlank() && priceStr.isNotBlank()
      ) {
        Text("Add to Catalogue")
      }
    },
    dismissButton = {
      OutlinedButton(onClick = onDismiss) { Text("Cancel") }
    }
  )
}

/**
 * TAB 3: Store Operations & Terminal Settings
 */
@Composable
fun StoreOpsSettingsTab(
  uiState: GroceryUiState,
  onToggleStoreAcceptingOrders: () -> Unit,
  onUpdateDeliveryRadius: (Int) -> Unit,
  onUpdateAdminPasscode: (oldPin: String, newPin: String) -> Boolean,
  onUpdateStoreManagerName: (String) -> Unit,
  onLogout: () -> Unit
) {
  var showChangePinDialog by remember { mutableStateOf(false) }

  LazyColumn(
    modifier = Modifier
      .fillMaxSize()
      .padding(14.dp),
    verticalArrangement = Arrangement.spacedBy(14.dp)
  ) {
    // 1. Live Order Acceptance Switch
    item {
      Card(
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = MountainCardSurface),
        border = androidx.compose.foundation.BorderStroke(1.dp, MountainBorder),
        modifier = Modifier.fillMaxWidth()
      ) {
        Row(
          modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Column(modifier = Modifier.weight(1f)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
              Box(
                modifier = Modifier
                  .size(10.dp)
                  .background(
                    if (uiState.isStoreAcceptingOrders) Color(0xFF22C55E) else Color(0xFFEF4444),
                    CircleShape
                  )
              )
              Spacer(modifier = Modifier.width(8.dp))
              Text(
                text = if (uiState.isStoreAcceptingOrders) "Store Online (Accepting Orders)" else "Store Offline (Paused)",
                style = MaterialTheme.typography.titleMedium.copy(
                  fontWeight = FontWeight.Bold,
                  color = TextPrimaryDark
                )
              )
            }
            Spacer(modifier = Modifier.height(4.dp))
            Text(
              text = if (uiState.isStoreAcceptingOrders)
                "Customers in Shahpur can place live orders for 15-min delivery."
              else
                "Order intake paused. Customers see 'Store temporarily offline' notice.",
              style = MaterialTheme.typography.bodySmall.copy(color = TextMuted)
            )
          }

          Switch(
            checked = uiState.isStoreAcceptingOrders,
            onCheckedChange = { onToggleStoreAcceptingOrders() },
            colors = SwitchDefaults.colors(
              checkedThumbColor = MountainWhite,
              checkedTrackColor = BrandGreenPrimary,
              uncheckedThumbColor = MountainWhite,
              uncheckedTrackColor = Color(0xFFCBD5E1)
            ),
            modifier = Modifier.testTag("admin_store_online_toggle")
          )
        }
      }
    }

    // 2. Delivery Coverage Radius
    item {
      Card(
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = MountainCardSurface),
        border = androidx.compose.foundation.BorderStroke(1.dp, MountainBorder),
        modifier = Modifier.fillMaxWidth()
      ) {
        Column(modifier = Modifier.padding(16.dp)) {
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
              Icon(Icons.Default.LocationOn, contentDescription = null, tint = BrandGreenPrimary)
              Spacer(modifier = Modifier.width(8.dp))
              Text(
                text = "Kangra Valley Coverage Radius",
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
              )
            }
            Surface(
              shape = RoundedCornerShape(6.dp),
              color = BrandGreenSurface
            ) {
              Text(
                text = "${uiState.storeDeliveryRadiusKm} KM",
                style = MaterialTheme.typography.labelMedium.copy(
                  color = BrandGreenPrimary,
                  fontWeight = FontWeight.ExtraBold
                ),
                modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
              )
            }
          }

          Spacer(modifier = Modifier.height(10.dp))

          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
          ) {
            listOf(
              5 to "5 KM (Shahpur Town)",
              8 to "8 KM (Core Valley)",
              12 to "12 KM (Extended Hub)"
            ).forEach { (km, label) ->
              val isSelected = uiState.storeDeliveryRadiusKm == km
              Surface(
                shape = RoundedCornerShape(8.dp),
                color = if (isSelected) BrandGreenPrimary else BrandGreenSurface,
                border = androidx.compose.foundation.BorderStroke(
                  1.dp,
                  if (isSelected) BrandGreenPrimary else MountainBorder
                ),
                modifier = Modifier
                  .weight(1f)
                  .clickable { onUpdateDeliveryRadius(km) }
              ) {
                Column(
                  modifier = Modifier.padding(vertical = 10.dp, horizontal = 4.dp),
                  horizontalAlignment = Alignment.CenterHorizontally
                ) {
                  Text(
                    text = "${km}km",
                    style = MaterialTheme.typography.titleSmall.copy(
                      fontWeight = FontWeight.Bold,
                      color = if (isSelected) MountainWhite else TextPrimaryDark
                    )
                  )
                  Text(
                    text = label.substringAfter("(").removeSuffix(")"),
                    style = MaterialTheme.typography.labelSmall.copy(
                      fontSize = 9.sp,
                      color = if (isSelected) MountainWhite.copy(alpha = 0.9f) else TextMuted
                    ),
                    maxLines = 1
                  )
                }
              }
            }
          }
        }
      }
    }

    // 3. Store Security & Passcode Setting
    item {
      Card(
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = MountainCardSurface),
        border = androidx.compose.foundation.BorderStroke(1.dp, MountainBorder),
        modifier = Modifier.fillMaxWidth()
      ) {
        Column(modifier = Modifier.padding(16.dp)) {
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
              Icon(Icons.Default.Lock, contentDescription = null, tint = BrandGreenPrimary)
              Spacer(modifier = Modifier.width(8.dp))
              Text(
                text = "Terminal Security & Passcode",
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
              )
            }
            Surface(
              shape = RoundedCornerShape(4.dp),
              color = RewardGold.copy(alpha = 0.2f)
            ) {
              Text(
                text = "PROTECTED",
                style = MaterialTheme.typography.labelSmall.copy(
                  color = BrandGreenDark,
                  fontWeight = FontWeight.Bold,
                  fontSize = 9.sp
                ),
                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
              )
            }
          }

          Spacer(modifier = Modifier.height(6.dp))
          Text(
            text = "Store managers can update their 4-digit security PIN anytime to prevent unauthorized access.",
            style = MaterialTheme.typography.bodySmall.copy(color = TextMuted)
          )

          Spacer(modifier = Modifier.height(12.dp))

          Button(
            onClick = { showChangePinDialog = true },
            colors = ButtonDefaults.buttonColors(containerColor = BrandGreenPrimary),
            shape = RoundedCornerShape(10.dp),
            modifier = Modifier
              .fillMaxWidth()
              .testTag("admin_change_pin_btn")
          ) {
            Icon(Icons.Default.Settings, contentDescription = null, modifier = Modifier.size(16.dp))
            Spacer(modifier = Modifier.width(8.dp))
            Text("Change Store Manager PIN", style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold))
          }
        }
      }
    }

    // 4. Active Dispatch Riders Fleet
    item {
      Card(
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = MountainCardSurface),
        border = androidx.compose.foundation.BorderStroke(1.dp, MountainBorder),
        modifier = Modifier.fillMaxWidth()
      ) {
        Column(modifier = Modifier.padding(16.dp)) {
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
              Icon(Icons.Default.DeliveryDining, contentDescription = null, tint = BrandGreenPrimary)
              Spacer(modifier = Modifier.width(8.dp))
              Text(
                text = "Shahpur Dispatch Fleet",
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
              )
            }
            Text(
              text = "${uiState.activeRidersCount} Riders Active",
              style = MaterialTheme.typography.labelSmall.copy(
                color = BrandGreenPrimary,
                fontWeight = FontWeight.Bold
              )
            )
          }

          Spacer(modifier = Modifier.height(10.dp))

          listOf(
            Triple("Raman Kumar", "+91 98160 54321", "Zone: Shahpur Bazar"),
            Triple("Vijay Thakur", "+91 98160 88214", "Zone: ITI & College Road"),
            Triple("Sunil Kumar", "+91 98160 99432", "Zone: Dadh & Rait"),
            Triple("Rohit Dogra", "+91 98160 33109", "Zone: NH-154 Chambi")
          ).forEach { (name, phone, zone) ->
            Row(
              modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 6.dp),
              horizontalArrangement = Arrangement.SpaceBetween,
              verticalAlignment = Alignment.CenterVertically
            ) {
              Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                  modifier = Modifier
                    .size(8.dp)
                    .background(Color(0xFF22C55E), CircleShape)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Column {
                  Text(name, style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold))
                  Text("$zone • $phone", style = MaterialTheme.typography.bodySmall.copy(color = TextMuted, fontSize = 11.sp))
                }
              }
              Surface(
                shape = RoundedCornerShape(4.dp),
                color = Color(0xFFDCFCE7)
              ) {
                Text(
                  text = "ON DUTY",
                  style = MaterialTheme.typography.labelSmall.copy(
                    color = Color(0xFF166534),
                    fontWeight = FontWeight.Bold,
                    fontSize = 9.sp
                  ),
                  modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                )
              }
            }
            HorizontalDivider(color = MountainBorder.copy(alpha = 0.5f))
          }
        }
      }
    }

    // 5. Terminal Info & Logout
    item {
      Card(
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = BrandGreenSurface),
        border = androidx.compose.foundation.BorderStroke(1.dp, BrandGreenPrimary.copy(alpha = 0.2f)),
        modifier = Modifier.fillMaxWidth()
      ) {
        Column(modifier = Modifier.padding(16.dp)) {
          Text(
            text = "Terminal Details",
            style = MaterialTheme.typography.titleSmall.copy(
              fontWeight = FontWeight.Bold,
              color = BrandGreenDark
            )
          )
          Spacer(modifier = Modifier.height(6.dp))
          Text(
            text = "• Terminal Identifier: ${uiState.storeHubId}\n• Store Manager: ${uiState.storeManagerName}\n• Physical Address: Main Bazar, Near Bus Stand, Shahpur HP\n• System Version: v2.4 (Enterprise Merchant Dispatch Desk)",
            style = MaterialTheme.typography.bodySmall.copy(color = TextSecondarySlate, lineHeight = 18.sp)
          )

          Spacer(modifier = Modifier.height(14.dp))

          OutlinedButton(
            onClick = onLogout,
            colors = ButtonDefaults.outlinedButtonColors(contentColor = Color(0xFFDC2626)),
            border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFDC2626)),
            shape = RoundedCornerShape(10.dp),
            modifier = Modifier
              .fillMaxWidth()
              .testTag("admin_terminal_logout_btn")
          ) {
            Icon(Icons.Default.PowerSettingsNew, contentDescription = null, modifier = Modifier.size(18.dp))
            Spacer(modifier = Modifier.width(8.dp))
            Text("Lock Terminal & Return to Customer App", style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold))
          }
        }
      }
    }

    item {
      Spacer(modifier = Modifier.height(24.dp))
    }
  }

  // Change PIN Dialog
  if (showChangePinDialog) {
    ChangePinDialog(
      onDismiss = { showChangePinDialog = false },
      onConfirm = { oldPin, newPin ->
        val success = onUpdateAdminPasscode(oldPin, newPin)
        if (success) {
          showChangePinDialog = false
        }
        success
      }
    )
  }
}

@Composable
fun ChangePinDialog(
  onDismiss: () -> Unit,
  onConfirm: (String, String) -> Boolean
) {
  var oldPin by remember { mutableStateOf("") }
  var newPin by remember { mutableStateOf("") }
  var confirmPin by remember { mutableStateOf("") }
  var errorText by remember { mutableStateOf<String?>(null) }
  var isPinVisible by remember { mutableStateOf(false) }

  AlertDialog(
    onDismissRequest = onDismiss,
    title = {
      Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(Icons.Default.Lock, contentDescription = null, tint = BrandGreenPrimary)
        Spacer(modifier = Modifier.width(8.dp))
        Text("Change Store Manager PIN", style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold))
      }
    },
    text = {
      Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        Text(
          "Enter your current PIN and choose a new 4-digit security PIN for this terminal.",
          style = MaterialTheme.typography.bodySmall.copy(color = TextMuted)
        )

        OutlinedTextField(
          value = oldPin,
          onValueChange = { oldPin = it; errorText = null },
          label = { Text("Current PIN") },
          singleLine = true,
          keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.NumberPassword),
          visualTransformation = if (isPinVisible) VisualTransformation.None else PasswordVisualTransformation(),
          modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
          value = newPin,
          onValueChange = { newPin = it; errorText = null },
          label = { Text("New PIN (min 4 digits)") },
          singleLine = true,
          keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.NumberPassword),
          visualTransformation = if (isPinVisible) VisualTransformation.None else PasswordVisualTransformation(),
          modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
          value = confirmPin,
          onValueChange = { confirmPin = it; errorText = null },
          label = { Text("Confirm New PIN") },
          singleLine = true,
          keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.NumberPassword),
          visualTransformation = if (isPinVisible) VisualTransformation.None else PasswordVisualTransformation(),
          modifier = Modifier.fillMaxWidth()
        )

        if (errorText != null) {
          Text(errorText!!, color = Color(0xFFDC2626), style = MaterialTheme.typography.labelSmall)
        }
      }
    },
    confirmButton = {
      Button(
        onClick = {
          if (newPin.trim().length < 4) {
            errorText = "New PIN must be at least 4 digits"
          } else if (newPin.trim() != confirmPin.trim()) {
            errorText = "New PIN and confirmation do not match"
          } else {
            val ok = onConfirm(oldPin.trim(), newPin.trim())
            if (!ok) {
              errorText = "Current PIN is incorrect"
            }
          }
        },
        colors = ButtonDefaults.buttonColors(containerColor = BrandGreenPrimary)
      ) {
        Text("Save New PIN")
      }
    },
    dismissButton = {
      OutlinedButton(onClick = onDismiss) { Text("Cancel") }
    }
  )
}

