package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AdminPanelSettings
import androidx.compose.material.icons.filled.Block
import androidx.compose.material.icons.filled.Category
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.LocalShipping
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Percent
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.Store
import androidx.compose.material.icons.filled.ToggleOff
import androidx.compose.material.icons.filled.ToggleOn
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.PrimaryTabRow
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.PlatformCategory
import com.example.model.Shop
import com.example.model.ShopApprovalStatus
import com.example.ui.theme.BrandGreenDark
import com.example.ui.theme.BrandGreenPrimary
import com.example.ui.theme.BrandGreenSurface
import com.example.ui.theme.MountainCardSurface
import com.example.ui.theme.MountainWhite
import com.example.ui.theme.RewardGold
import com.example.ui.theme.TextMuted
import com.example.ui.theme.WarningRed
import com.example.viewmodel.GroceryUiState
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(androidx.compose.material3.ExperimentalMaterial3Api::class)
@Composable
fun SuperAdminDashboardScreen(
  uiState: GroceryUiState,
  onApproveShop: (String) -> Unit,
  onSuspendShop: (String) -> Unit,
  onToggleShopOpen: (String) -> Unit,
  onUpdateCommission: (String, Double) -> Unit,
  onToggleCategory: (String) -> Unit,
  onAddCategory: (PlatformCategory) -> Unit,
  onUpdatePlatformSettings: (Double, Double, Double) -> Unit,
  onBackToCustomer: () -> Unit
) {
  var selectedTab by remember { mutableIntStateOf(0) }
  val tabTitles = listOf("Vendors", "Categories", "Settings", "Audit Log")

  // Platform calculations
  val approvedShopsCount = uiState.platformShops.count { it.status == ShopApprovalStatus.APPROVED }
  val totalOrdersCount = uiState.orders.size + uiState.firestoreOrders.size
  val totalPlatformGmv = uiState.orders.sumOf { it.total }
  val estimatedCommission = totalPlatformGmv * 0.08 // 8% avg platform commission

  Scaffold(
    topBar = {
      Box(
        modifier = Modifier
          .fillMaxWidth()
          .background(BrandGreenDark)
          .padding(horizontal = 16.dp, vertical = 12.dp)
      ) {
        Row(
          modifier = Modifier.fillMaxWidth(),
          verticalAlignment = Alignment.CenterVertically
        ) {
          IconButton(
            onClick = onBackToCustomer,
            modifier = Modifier
              .size(40.dp)
              .clip(CircleShape)
              .background(MountainWhite.copy(alpha = 0.15f))
              .testTag("super_admin_back_btn")
          ) {
            Icon(
              imageVector = Icons.AutoMirrored.Filled.ArrowBack,
              contentDescription = "Back",
              tint = MountainWhite
            )
          }

          Spacer(modifier = Modifier.width(12.dp))

          Column(modifier = Modifier.weight(1f)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
              Icon(
                imageVector = Icons.Default.Shield,
                contentDescription = null,
                tint = RewardGold,
                modifier = Modifier.size(18.dp)
              )
              Spacer(modifier = Modifier.width(6.dp))
              Text(
                text = "Owner Super Admin",
                color = MountainWhite,
                fontWeight = FontWeight.Bold,
                fontSize = 17.sp
              )
            }
            Text(
              text = "Platform Master: ${Shop.PLATFORM_OWNER_EMAIL}",
              color = MountainWhite.copy(alpha = 0.75f),
              fontSize = 11.sp,
              maxLines = 1,
              overflow = TextOverflow.Ellipsis
            )
          }

          Box(
            modifier = Modifier
              .clip(RoundedCornerShape(8.dp))
              .background(RewardGold)
              .padding(horizontal = 8.dp, vertical = 4.dp)
          ) {
            Text(
              text = "SUPER USER",
              fontWeight = FontWeight.Bold,
              fontSize = 10.sp,
              color = Color(0xFF1E293B)
            )
          }
        }
      }
    }
  ) { padding ->
    Column(
      modifier = Modifier
        .fillMaxSize()
        .background(MountainCardSurface)
        .padding(padding)
    ) {
      // Platform KPIs
      Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MountainWhite),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = Modifier
          .fillMaxWidth()
          .padding(14.dp)
          .testTag("super_admin_kpis_card")
      ) {
        Column(modifier = Modifier.padding(14.dp)) {
          Text(
            text = "Platform Overview (Shahpur)",
            style = MaterialTheme.typography.labelMedium,
            fontWeight = FontWeight.Bold,
            color = TextMuted
          )
          Spacer(modifier = Modifier.height(10.dp))
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
          ) {
            Column {
              Text(text = "Gross Sales (GMV)", fontSize = 11.sp, color = TextMuted)
              Text(
                text = "₹${totalPlatformGmv.toInt()}",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = BrandGreenDark
              )
            }
            Column {
              Text(text = "Total Orders", fontSize = 11.sp, color = TextMuted)
              Text(
                text = "$totalOrdersCount",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF1F2937)
              )
            }
            Column {
              Text(text = "Active Shops", fontSize = 11.sp, color = TextMuted)
              Text(
                text = "$approvedShopsCount",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = BrandGreenPrimary
              )
            }
            Column {
              Text(text = "Commission", fontSize = 11.sp, color = TextMuted)
              Text(
                text = "₹${estimatedCommission.toInt()}",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = RewardGold
              )
            }
          }
        }
      }

      // Tab selector
      PrimaryTabRow(
        selectedTabIndex = selectedTab,
        containerColor = MountainWhite,
        contentColor = BrandGreenDark
      ) {
        tabTitles.forEachIndexed { index, title ->
          Tab(
            selected = selectedTab == index,
            onClick = { selectedTab = index },
            text = {
              Text(
                text = title,
                fontWeight = if (selectedTab == index) FontWeight.Bold else FontWeight.Normal,
                fontSize = 13.sp
              )
            },
            modifier = Modifier.testTag("super_admin_tab_$index")
          )
        }
      }

      // Content based on tab
      when (selectedTab) {
        0 -> VendorManagementTab(
          shops = uiState.platformShops,
          onApproveShop = onApproveShop,
          onSuspendShop = onSuspendShop,
          onToggleShopOpen = onToggleShopOpen,
          onUpdateCommission = onUpdateCommission
        )
        1 -> CategoryManagementTab(
          categories = uiState.platformCategories,
          onToggleCategory = onToggleCategory,
          onAddCategory = onAddCategory
        )
        2 -> PlatformSettingsTab(
          currentDeliveryFee = uiState.platformDeliveryFee,
          currentRadiusKm = uiState.platformServiceRadiusKm,
          currentMinOrder = uiState.platformMinOrderValue,
          onSaveSettings = onUpdatePlatformSettings
        )
        3 -> AuditLogTab(logs = uiState.platformAuditLogs)
      }
    }
  }
}

@Composable
private fun VendorManagementTab(
  shops: List<Shop>,
  onApproveShop: (String) -> Unit,
  onSuspendShop: (String) -> Unit,
  onToggleShopOpen: (String) -> Unit,
  onUpdateCommission: (String, Double) -> Unit
) {
  LazyColumn(
    modifier = Modifier.fillMaxSize(),
    contentPadding = PaddingValues(14.dp),
    verticalArrangement = Arrangement.spacedBy(10.dp)
  ) {
    items(shops, key = { it.id }) { shop ->
      Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MountainWhite),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = Modifier.fillMaxWidth().testTag("vendor_card_${shop.id}")
      ) {
        Column(modifier = Modifier.padding(14.dp)) {
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
              Text(text = shop.iconEmoji, fontSize = 28.sp)
              Spacer(modifier = Modifier.width(10.dp))
              Column {
                Text(
                  text = shop.name,
                  fontWeight = FontWeight.Bold,
                  fontSize = 15.sp,
                  color = Color(0xFF1F2937),
                  maxLines = 1,
                  overflow = TextOverflow.Ellipsis
                )
                Text(
                  text = "Admin: ${shop.assignedAdminName} (${shop.adminEmail})",
                  fontSize = 11.sp,
                  color = TextMuted,
                  maxLines = 1,
                  overflow = TextOverflow.Ellipsis
                )
              }
            }

            // Status chip
            Box(
              modifier = Modifier
                .clip(RoundedCornerShape(8.dp))
                .background(
                  when (shop.status) {
                    ShopApprovalStatus.APPROVED -> BrandGreenSurface
                    ShopApprovalStatus.PENDING -> RewardGold.copy(alpha = 0.2f)
                    ShopApprovalStatus.SUSPENDED -> WarningRed.copy(alpha = 0.15f)
                  }
                )
                .padding(horizontal = 8.dp, vertical = 4.dp)
            ) {
              Text(
                text = shop.status.title,
                fontWeight = FontWeight.Bold,
                fontSize = 11.sp,
                color = when (shop.status) {
                  ShopApprovalStatus.APPROVED -> BrandGreenDark
                  ShopApprovalStatus.PENDING -> Color(0xFFB45309)
                  ShopApprovalStatus.SUSPENDED -> WarningRed
                }
              )
            }
          }

          Spacer(modifier = Modifier.height(10.dp))
          Divider(color = Color(0xFFF3F4F6))
          Spacer(modifier = Modifier.height(10.dp))

          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Text(
              text = "Commission: ${shop.commissionRatePercent}%",
              fontSize = 12.sp,
              fontWeight = FontWeight.SemiBold,
              color = BrandGreenDark
            )

            Row(verticalAlignment = Alignment.CenterVertically) {
              Text(
                text = if (shop.isOpen) "Open" else "Closed",
                fontSize = 12.sp,
                color = if (shop.isOpen) BrandGreenDark else WarningRed,
                fontWeight = FontWeight.Medium
              )
              Spacer(modifier = Modifier.width(4.dp))
              Switch(
                checked = shop.isOpen,
                onCheckedChange = { onToggleShopOpen(shop.id) },
                colors = SwitchDefaults.colors(
                  checkedThumbColor = MountainWhite,
                  checkedTrackColor = BrandGreenPrimary
                ),
                modifier = Modifier.testTag("toggle_shop_open_${shop.id}")
              )
            }
          }

          Spacer(modifier = Modifier.height(8.dp))

          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End
          ) {
            if (shop.status != ShopApprovalStatus.APPROVED) {
              Button(
                onClick = { onApproveShop(shop.id) },
                colors = ButtonDefaults.buttonColors(containerColor = BrandGreenPrimary),
                shape = RoundedCornerShape(8.dp),
                contentPadding = PaddingValues(horizontal = 12.dp, vertical = 4.dp),
                modifier = Modifier.testTag("approve_shop_${shop.id}")
              ) {
                Text("Approve", fontSize = 12.sp, fontWeight = FontWeight.Bold)
              }
            }

            if (shop.status != ShopApprovalStatus.SUSPENDED && !shop.isHubStore) {
              Spacer(modifier = Modifier.width(8.dp))
              OutlinedButton(
                onClick = { onSuspendShop(shop.id) },
                shape = RoundedCornerShape(8.dp),
                contentPadding = PaddingValues(horizontal = 12.dp, vertical = 4.dp),
                modifier = Modifier.testTag("suspend_shop_${shop.id}")
              ) {
                Text("Suspend", fontSize = 12.sp, color = WarningRed)
              }
            }
          }
        }
      }
    }
  }
}

@Composable
private fun CategoryManagementTab(
  categories: List<PlatformCategory>,
  onToggleCategory: (String) -> Unit,
  onAddCategory: (PlatformCategory) -> Unit
) {
  var newCatName by remember { mutableStateOf("") }
  var newCatEmoji by remember { mutableStateOf("🛍️") }

  LazyColumn(
    modifier = Modifier.fillMaxSize(),
    contentPadding = PaddingValues(14.dp),
    verticalArrangement = Arrangement.spacedBy(10.dp)
  ) {
    item {
      Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MountainWhite),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = Modifier.fillMaxWidth()
      ) {
        Column(modifier = Modifier.padding(14.dp)) {
          Text(
            text = "Add Custom Category",
            fontWeight = FontWeight.Bold,
            fontSize = 14.sp,
            color = BrandGreenDark
          )
          Spacer(modifier = Modifier.height(10.dp))
          Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            OutlinedTextField(
              value = newCatEmoji,
              onValueChange = { if (it.length <= 2) newCatEmoji = it },
              label = { Text("Icon") },
              modifier = Modifier.width(70.dp),
              singleLine = true
            )
            Spacer(modifier = Modifier.width(8.dp))
            OutlinedTextField(
              value = newCatName,
              onValueChange = { newCatName = it },
              label = { Text("Category Title") },
              modifier = Modifier.weight(1f),
              singleLine = true
            )
            Spacer(modifier = Modifier.width(8.dp))
            Button(
              onClick = {
                if (newCatName.isNotBlank()) {
                  val cat = PlatformCategory(
                    id = newCatName.trim().uppercase().replace(" ", "_"),
                    displayName = newCatName.trim(),
                    hindiName = newCatName.trim(),
                    iconEmoji = newCatEmoji,
                    isEnabled = true,
                    displayOrder = categories.size
                  )
                  onAddCategory(cat)
                  newCatName = ""
                }
              },
              enabled = newCatName.isNotBlank(),
              colors = ButtonDefaults.buttonColors(containerColor = BrandGreenPrimary),
              shape = RoundedCornerShape(10.dp),
              modifier = Modifier.testTag("add_category_btn")
            ) {
              Text("Add")
            }
          }
        }
      }
    }

    items(categories, key = { it.id }) { cat ->
      Card(
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = MountainWhite),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
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
            Text(text = cat.iconEmoji, fontSize = 22.sp)
            Spacer(modifier = Modifier.width(10.dp))
            Column {
              Text(
                text = cat.displayName,
                fontWeight = FontWeight.SemiBold,
                fontSize = 14.sp,
                color = if (cat.isEnabled) Color(0xFF1F2937) else TextMuted
              )
              Text(
                text = cat.hindiName,
                fontSize = 11.sp,
                color = TextMuted
              )
            }
          }

          Switch(
            checked = cat.isEnabled,
            onCheckedChange = { onToggleCategory(cat.id) },
            colors = SwitchDefaults.colors(
              checkedThumbColor = MountainWhite,
              checkedTrackColor = BrandGreenPrimary
            ),
            modifier = Modifier.testTag("toggle_category_${cat.id}")
          )
        }
      }
    }
  }
}

@Composable
private fun PlatformSettingsTab(
  currentDeliveryFee: Double,
  currentRadiusKm: Double,
  currentMinOrder: Double,
  onSaveSettings: (Double, Double, Double) -> Unit
) {
  var feeInput by remember { mutableStateOf(currentDeliveryFee.toInt().toString()) }
  var radiusInput by remember { mutableStateOf(currentRadiusKm.toInt().toString()) }
  var minOrderInput by remember { mutableStateOf(currentMinOrder.toInt().toString()) }

  Column(
    modifier = Modifier
      .fillMaxSize()
      .padding(16.dp),
    verticalArrangement = Arrangement.spacedBy(16.dp)
  ) {
    Card(
      shape = RoundedCornerShape(16.dp),
      colors = CardDefaults.cardColors(containerColor = MountainWhite),
      elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
      modifier = Modifier.fillMaxWidth()
    ) {
      Column(modifier = Modifier.padding(16.dp)) {
        Text(
          text = "Delivery & Operational Rules",
          fontWeight = FontWeight.Bold,
          fontSize = 15.sp,
          color = BrandGreenDark
        )
        Text(
          text = "Configured settings take effect immediately across all customer checkout sessions in Shahpur.",
          fontSize = 12.sp,
          color = TextMuted
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
          value = feeInput,
          onValueChange = { feeInput = it },
          label = { Text("Base Delivery Fee (₹)") },
          singleLine = true,
          modifier = Modifier.fillMaxWidth().testTag("setting_delivery_fee_input")
        )

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextField(
          value = radiusInput,
          onValueChange = { radiusInput = it },
          label = { Text("Serviceable Radius (KM from Shahpur Hub)") },
          singleLine = true,
          modifier = Modifier.fillMaxWidth().testTag("setting_radius_input")
        )

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextField(
          value = minOrderInput,
          onValueChange = { minOrderInput = it },
          label = { Text("Minimum Order Value (₹)") },
          singleLine = true,
          modifier = Modifier.fillMaxWidth().testTag("setting_min_order_input")
        )

        Spacer(modifier = Modifier.height(20.dp))

        Button(
          onClick = {
            val fee = feeInput.toDoubleOrNull() ?: currentDeliveryFee
            val radius = radiusInput.toDoubleOrNull() ?: currentRadiusKm
            val minOrder = minOrderInput.toDoubleOrNull() ?: currentMinOrder
            onSaveSettings(fee, radius, minOrder)
          },
          colors = ButtonDefaults.buttonColors(containerColor = BrandGreenPrimary),
          shape = RoundedCornerShape(12.dp),
          modifier = Modifier
            .fillMaxWidth()
            .height(48.dp)
            .testTag("save_platform_settings_btn")
        ) {
          Text("Save Platform Settings", fontWeight = FontWeight.Bold)
        }
      }
    }
  }
}

@Composable
private fun AuditLogTab(logs: List<com.example.model.PlatformAuditLog>) {
  val sdf = remember { SimpleDateFormat("dd MMM, hh:mm a", Locale.getDefault()) }

  LazyColumn(
    modifier = Modifier.fillMaxSize(),
    contentPadding = PaddingValues(14.dp),
    verticalArrangement = Arrangement.spacedBy(8.dp)
  ) {
    if (logs.isEmpty()) {
      item {
        Box(modifier = Modifier.fillMaxWidth().padding(40.dp), contentAlignment = Alignment.Center) {
          Text("No audit log records recorded yet.", color = TextMuted)
        }
      }
    } else {
      items(logs, key = { it.id }) { log ->
        Card(
          shape = RoundedCornerShape(12.dp),
          colors = CardDefaults.cardColors(containerColor = MountainWhite),
          elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
          modifier = Modifier.fillMaxWidth()
        ) {
          Column(modifier = Modifier.padding(12.dp)) {
            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.SpaceBetween,
              verticalAlignment = Alignment.CenterVertically
            ) {
              Text(
                text = log.action,
                fontWeight = FontWeight.Bold,
                fontSize = 13.sp,
                color = BrandGreenDark
              )
              Text(
                text = sdf.format(Date(log.timestamp)),
                fontSize = 11.sp,
                color = TextMuted
              )
            }
            Spacer(modifier = Modifier.height(4.dp))
            Text(
              text = log.details,
              fontSize = 12.sp,
              color = Color(0xFF374151)
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
              text = "By: ${log.performedBy} • Target: ${log.targetEntity}",
              fontSize = 10.sp,
              color = TextMuted
            )
          }
        }
      }
    }
  }
}
