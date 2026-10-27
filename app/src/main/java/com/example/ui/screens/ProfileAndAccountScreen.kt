package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AdminPanelSettings
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.CloudDone
import androidx.compose.material.icons.filled.HeadsetMic
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Landscape
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.MonetizationOn
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Sync
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.ui.layout.ContentScale
import coil.compose.AsyncImage
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.Order
import com.example.ui.components.MountainSilhouettes
import com.example.ui.components.ShahpurGoBrandLogo
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
import com.example.ui.theme.WarningRed
import com.example.ui.theme.TextPrimaryDark
import com.example.ui.theme.TextSecondarySlate
import com.example.viewmodel.GroceryUiState

@Composable
fun ProfileAndAccountScreen(
  uiState: GroceryUiState,
  onOpenMerchantPortal: () -> Unit,
  onOpenLocationPicker: () -> Unit,
  onOpenOrderHistory: () -> Unit,
  onSelectOrderToTrack: (Order) -> Unit,
  onReorder: (Order) -> Unit,
  onOpenSuperAdmin: () -> Unit = {},
  onOpenCustomerLogin: () -> Unit = {},
  onLogoutCustomer: () -> Unit = {},
  onSignInWithGoogle: () -> Unit = {},
  onSignOutGoogle: () -> Unit = {},
  onRefreshProfile: () -> Unit = {},
  modifier: Modifier = Modifier
) {
  var showLoginDialog by remember { mutableStateOf(false) }

  Column(
    modifier = modifier
      .fillMaxSize()
      .background(MountainSnowBackground)
      .verticalScroll(rememberScrollState())
      .padding(14.dp),
    verticalArrangement = Arrangement.spacedBy(14.dp)
  ) {
    // 1. User Account & Google Cloud Sync Card
    Card(
      shape = RoundedCornerShape(16.dp),
      colors = CardDefaults.cardColors(containerColor = MountainCardSurface),
      border = androidx.compose.foundation.BorderStroke(1.dp, MountainBorder),
      modifier = Modifier
        .fillMaxWidth()
        .testTag("user_profile_card")
    ) {
      Column(modifier = Modifier.padding(16.dp)) {
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.weight(1f)
          ) {
            Box(
              modifier = Modifier
                .size(56.dp)
                .clip(CircleShape)
                .background(BrandGreenContainer)
                .border(2.dp, if (uiState.user.isGoogleSignedIn) BrandGreenPrimary else MountainBorder, CircleShape),
              contentAlignment = Alignment.Center
            ) {
              if (uiState.user.photoUrl != null) {
                AsyncImage(
                  model = uiState.user.photoUrl,
                  contentDescription = "Google Account Avatar",
                  contentScale = ContentScale.Crop,
                  modifier = Modifier
                    .fillMaxSize()
                    .clip(CircleShape)
                )
              } else {
                Icon(
                  imageVector = Icons.Default.Person,
                  contentDescription = null,
                  tint = BrandGreenDark,
                  modifier = Modifier.size(32.dp)
                )
              }
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
              Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                  text = uiState.user.name,
                  style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold,
                    color = TextPrimaryDark
                  ),
                  maxLines = 1
                )
                if (uiState.user.isGoogleSignedIn) {
                  Spacer(modifier = Modifier.width(4.dp))
                  Icon(
                    imageVector = Icons.Default.CheckCircle,
                    contentDescription = "Google Verified",
                    tint = BrandGreenPrimary,
                    modifier = Modifier.size(16.dp)
                  )
                }
              }

              Text(
                text = if (uiState.user.isGoogleSignedIn) uiState.user.email else uiState.user.phone,
                style = MaterialTheme.typography.bodySmall.copy(color = TextMuted),
                maxLines = 1
              )

              if (uiState.isPlatformOwner) {
                Spacer(modifier = Modifier.height(2.dp))
                Surface(
                  shape = RoundedCornerShape(4.dp),
                  color = BrandGreenPrimary.copy(alpha = 0.15f)
                ) {
                  Text(
                    text = "👑 Platform Owner",
                    style = MaterialTheme.typography.labelSmall.copy(
                      color = BrandGreenDark,
                      fontWeight = FontWeight.Bold
                    ),
                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                  )
                }
              } else {
                Text(
                  text = "Shahpur, Kangra HP",
                  style = MaterialTheme.typography.labelSmall.copy(color = BrandGreenLight)
                )
              }
            }
          }

          if (uiState.user.isGoogleSignedIn || uiState.isCustomerLoggedIn) {
            OutlinedButton(
              onClick = {
                if (uiState.user.isGoogleSignedIn) onSignOutGoogle() else onLogoutCustomer()
              },
              shape = RoundedCornerShape(10.dp),
              contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp),
              modifier = Modifier.testTag("account_customer_logout_btn")
            ) {
              Text("Sign Out", fontSize = 12.sp, color = WarningRed, fontWeight = FontWeight.Bold)
            }
          }
        }

        // Google Cloud Sync Status or Sign-In Affordance
        Spacer(modifier = Modifier.height(14.dp))
        HorizontalDivider(color = MountainBorder)
        Spacer(modifier = Modifier.height(10.dp))

        if (uiState.user.isGoogleSignedIn) {
          Row(
            modifier = Modifier
              .fillMaxWidth()
              .background(BrandGreenSurface, RoundedCornerShape(10.dp))
              .padding(horizontal = 12.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
          ) {
            Icon(
              imageVector = Icons.Default.CloudDone,
              contentDescription = null,
              tint = BrandGreenPrimary,
              modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Column(modifier = Modifier.weight(1f)) {
              Text(
                text = "Google Account Linked • Live Cloud Sync",
                style = MaterialTheme.typography.labelMedium.copy(
                  fontWeight = FontWeight.Bold,
                  color = BrandGreenDark
                )
              )
              Text(
                text = "Orders and live tracking synchronize across devices with Firebase Firestore.",
                style = MaterialTheme.typography.labelSmall.copy(color = TextMuted)
              )
            }
            IconButton(
              onClick = onRefreshProfile,
              modifier = Modifier
                .size(32.dp)
                .testTag("refresh_user_session_btn")
            ) {
              Icon(
                imageVector = Icons.Default.Refresh,
                contentDescription = "Refresh Profile",
                tint = BrandGreenDark,
                modifier = Modifier.size(18.dp)
              )
            }
          }
        } else {
          Column(
            modifier = Modifier
              .fillMaxWidth()
              .background(BrandGreenSurface, RoundedCornerShape(12.dp))
              .padding(12.dp)
          ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
              Icon(
                imageVector = Icons.Default.CloudDone,
                contentDescription = null,
                tint = BrandGreenDark,
                modifier = Modifier.size(18.dp)
              )
              Spacer(modifier = Modifier.width(6.dp))
              Text(
                text = "Sign in to Sync Orders & Tracking",
                style = MaterialTheme.typography.titleSmall.copy(
                  fontWeight = FontWeight.Bold,
                  color = BrandGreenDark
                )
              )
            }
            Spacer(modifier = Modifier.height(4.dp))
            Text(
              text = "Connect your Google account to access order tracking from any phone and preserve purchase history.",
              style = MaterialTheme.typography.bodySmall.copy(color = TextSecondarySlate)
            )
            Spacer(modifier = Modifier.height(10.dp))
            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
              Button(
                onClick = onSignInWithGoogle,
                colors = ButtonDefaults.buttonColors(containerColor = BrandGreenDark),
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier
                  .weight(1.3f)
                  .testTag("google_sign_in_button")
              ) {
                if (uiState.isGoogleSigningIn) {
                  CircularProgressIndicator(
                    modifier = Modifier.size(18.dp),
                    color = MountainWhite,
                    strokeWidth = 2.dp
                  )
                  Spacer(modifier = Modifier.width(6.dp))
                } else {
                  Icon(
                    imageVector = Icons.Default.CheckCircle,
                    contentDescription = null,
                    tint = RewardGold,
                    modifier = Modifier.size(18.dp)
                  )
                  Spacer(modifier = Modifier.width(6.dp))
                }
                Text("Sign in with Google", fontSize = 13.sp, fontWeight = FontWeight.Bold)
              }

              OutlinedButton(
                onClick = onOpenCustomerLogin,
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier
                  .weight(0.9f)
                  .testTag("account_login_switch_btn")
              ) {
                Text("Phone OTP", fontSize = 12.sp, fontWeight = FontWeight.SemiBold, color = BrandGreenDark)
              }
            }
          }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Mini Stats row
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
          Surface(
            shape = RoundedCornerShape(10.dp),
            color = BrandGreenSurface,
            modifier = Modifier.weight(1f)
          ) {
            Column(
              modifier = Modifier.padding(10.dp),
              horizontalAlignment = Alignment.CenterHorizontally
            ) {
              Text("Wallet Coins", style = MaterialTheme.typography.labelSmall.copy(color = TextMuted))
              Text(
                "${uiState.user.coinsBalance}",
                style = MaterialTheme.typography.titleMedium.copy(
                  fontWeight = FontWeight.Bold,
                  color = BrandGreenDark
                )
              )
            }
          }

          Surface(
            shape = RoundedCornerShape(10.dp),
            color = BrandGreenSurface,
            modifier = Modifier.weight(1f)
          ) {
            Column(
              modifier = Modifier.padding(10.dp),
              horizontalAlignment = Alignment.CenterHorizontally
            ) {
              Text("Total Saved", style = MaterialTheme.typography.labelSmall.copy(color = TextMuted))
              Text(
                "₹${uiState.user.totalSavings.toInt()}",
                style = MaterialTheme.typography.titleMedium.copy(
                  fontWeight = FontWeight.Bold,
                  color = BrandGreenPrimary
                )
              )
            }
          }

          Surface(
            shape = RoundedCornerShape(10.dp),
            color = BrandGreenSurface,
            modifier = Modifier.weight(1f)
          ) {
            Column(
              modifier = Modifier.padding(10.dp),
              horizontalAlignment = Alignment.CenterHorizontally
            ) {
              Text("Total Orders", style = MaterialTheme.typography.labelSmall.copy(color = TextMuted))
              Text(
                "${uiState.orders.size}",
                style = MaterialTheme.typography.titleMedium.copy(
                  fontWeight = FontWeight.Bold,
                  color = TextPrimaryDark
                )
              )
            }
          }
        }
      }
    }

    // 2. ShahpurGo Store Partner & Merchant Desk Card (Fixed layout & responsive button)
    Card(
      shape = RoundedCornerShape(16.dp),
      colors = CardDefaults.cardColors(containerColor = BrandGreenDark),
      elevation = CardDefaults.cardElevation(defaultElevation = 3.dp),
      modifier = Modifier
        .fillMaxWidth()
        .testTag("admin_mode_entry_card")
    ) {
      Column(modifier = Modifier.padding(16.dp)) {
        Row(
          modifier = Modifier.fillMaxWidth(),
          verticalAlignment = Alignment.CenterVertically
        ) {
          Box(
            modifier = Modifier
              .size(46.dp)
              .background(RewardGold, CircleShape),
            contentAlignment = Alignment.Center
          ) {
            Icon(
              imageVector = Icons.Default.AdminPanelSettings,
              contentDescription = null,
              tint = BrandGreenDark,
              modifier = Modifier.size(28.dp)
            )
          }

          Spacer(modifier = Modifier.width(12.dp))

          Column(modifier = Modifier.weight(1f)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
              Text(
                text = "ShahpurGo Merchant Hub",
                style = MaterialTheme.typography.titleMedium.copy(
                  fontWeight = FontWeight.Bold,
                  color = MountainWhite
                )
              )
              Spacer(modifier = Modifier.width(6.dp))
              Surface(
                shape = RoundedCornerShape(4.dp),
                color = if (uiState.isAdminAuthenticated) BrandGreenLight else RewardGold
              ) {
                Text(
                  text = if (uiState.isAdminAuthenticated) "ACTIVE" else "STORE PARTNER",
                  style = MaterialTheme.typography.labelSmall.copy(
                    color = BrandGreenDark,
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 9.sp
                  ),
                  modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                )
              }
            }

            Spacer(modifier = Modifier.height(2.dp))
            Text(
              text = if (uiState.isAdminAuthenticated)
                "Manage live orders, inventory stock & product catalog"
              else
                "Authorized shop admin portal for Shahpur vendors & restaurants",
              style = MaterialTheme.typography.bodySmall.copy(
                color = MountainWhite.copy(alpha = 0.85f),
                fontSize = 12.sp
              )
            )
          }
        }

        Spacer(modifier = Modifier.height(14.dp))

        Button(
          onClick = onOpenMerchantPortal,
          colors = ButtonDefaults.buttonColors(containerColor = RewardGold),
          shape = RoundedCornerShape(12.dp),
          modifier = Modifier
            .fillMaxWidth()
            .height(48.dp)
            .testTag("partner_login_button")
        ) {
          Text(
            text = if (uiState.isAdminAuthenticated) "Open Merchant Dashboard" else "Partner Login & Terminal",
            style = MaterialTheme.typography.labelLarge.copy(
              color = BrandGreenDark,
              fontWeight = FontWeight.ExtraBold
            )
          )
        }
      }
    }

    // Dedicated Super Admin / Owner Dashboard Card
    Card(
      shape = RoundedCornerShape(16.dp),
      colors = CardDefaults.cardColors(containerColor = MountainWhite),
      border = androidx.compose.foundation.BorderStroke(1.5.dp, RewardGold),
      elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
      modifier = Modifier
        .fillMaxWidth()
        .testTag("super_admin_entry_card")
    ) {
      Column(modifier = Modifier.padding(16.dp)) {
        Row(
          modifier = Modifier.fillMaxWidth(),
          verticalAlignment = Alignment.CenterVertically
        ) {
          Box(
            modifier = Modifier
              .size(44.dp)
              .background(BrandGreenSurface, CircleShape),
            contentAlignment = Alignment.Center
          ) {
            Icon(
              imageVector = Icons.Default.Security,
              contentDescription = null,
              tint = BrandGreenDark,
              modifier = Modifier.size(24.dp)
            )
          }

          Spacer(modifier = Modifier.width(12.dp))

          Column(modifier = Modifier.weight(1f)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
              Text(
                text = "Owner Super Admin Panel",
                style = MaterialTheme.typography.titleMedium.copy(
                  fontWeight = FontWeight.Bold,
                  color = BrandGreenDark
                )
              )
              Spacer(modifier = Modifier.width(6.dp))
              Surface(
                shape = RoundedCornerShape(4.dp),
                color = RewardGold
              ) {
                Text(
                  text = "OWNER",
                  style = MaterialTheme.typography.labelSmall.copy(
                    color = BrandGreenDark,
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 9.sp
                  ),
                  modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                )
              }
            }

            Text(
              text = "Platform GMV, vendor approvals, commissions & category control",
              style = MaterialTheme.typography.bodySmall.copy(
                color = TextMuted,
                fontSize = 12.sp
              )
            )
          }
        }

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedButton(
          onClick = onOpenSuperAdmin,
          shape = RoundedCornerShape(12.dp),
          border = androidx.compose.foundation.BorderStroke(1.dp, BrandGreenPrimary),
          modifier = Modifier
            .fillMaxWidth()
            .height(46.dp)
            .testTag("open_super_admin_btn")
        ) {
          Icon(
            imageVector = Icons.Default.AdminPanelSettings,
            contentDescription = null,
            tint = BrandGreenDark,
            modifier = Modifier.size(18.dp)
          )
          Spacer(modifier = Modifier.width(8.dp))
          Text(
            text = "Access Super Admin Controls",
            color = BrandGreenDark,
            fontWeight = FontWeight.Bold,
            fontSize = 13.sp
          )
        }
      }
    }

    // 3. Saved Addresses Section
    Card(
      shape = RoundedCornerShape(14.dp),
      colors = CardDefaults.cardColors(containerColor = MountainCardSurface),
      border = androidx.compose.foundation.BorderStroke(1.dp, MountainBorder),
      modifier = Modifier.fillMaxWidth()
    ) {
      Column(modifier = Modifier.padding(14.dp)) {
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Text(
            text = "Saved Delivery Locations",
            style = MaterialTheme.typography.titleMedium.copy(
              fontWeight = FontWeight.Bold,
              color = TextPrimaryDark
            )
          )
          Surface(
            shape = RoundedCornerShape(6.dp),
            color = BrandGreenSurface,
            modifier = Modifier.clickable { onOpenLocationPicker() }
          ) {
            Text(
              text = "+ Add New",
              style = MaterialTheme.typography.labelSmall.copy(
                color = BrandGreenPrimary,
                fontWeight = FontWeight.Bold
              ),
              modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
            )
          }
        }

        Spacer(modifier = Modifier.height(8.dp))

        uiState.deliveryLocations.forEachIndexed { idx, loc ->
          Row(
            modifier = Modifier
              .fillMaxWidth()
              .padding(vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically
          ) {
            Icon(
              imageVector = Icons.Default.LocationOn,
              contentDescription = null,
              tint = BrandGreenPrimary,
              modifier = Modifier.size(18.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Column(modifier = Modifier.weight(1f)) {
              Text(
                text = "${loc.tag} - ${loc.areaName}",
                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
              )
              Text(
                text = loc.fullAddress,
                style = MaterialTheme.typography.bodySmall.copy(color = TextMuted),
                maxLines = 1
              )
            }
          }
          if (idx < uiState.deliveryLocations.size - 1) {
            HorizontalDivider(color = MountainBorder.copy(alpha = 0.5f))
          }
        }
      }
    }

    // 4. Recent Order History & Reorder
    Card(
      shape = RoundedCornerShape(14.dp),
      colors = CardDefaults.cardColors(containerColor = MountainCardSurface),
      border = androidx.compose.foundation.BorderStroke(1.dp, MountainBorder),
      modifier = Modifier.fillMaxWidth()
    ) {
      Column(modifier = Modifier.padding(14.dp)) {
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(Icons.Default.History, contentDescription = null, tint = BrandGreenPrimary, modifier = Modifier.size(20.dp))
            Spacer(modifier = Modifier.width(8.dp))
            Text(
              text = "Recent ShahpurGo Orders",
              style = MaterialTheme.typography.titleMedium.copy(
                fontWeight = FontWeight.Bold,
                color = TextPrimaryDark
              )
            )
          }

          Surface(
            shape = RoundedCornerShape(6.dp),
            color = BrandGreenSurface,
            modifier = Modifier
              .clickable { onOpenOrderHistory() }
              .testTag("view_all_order_history_btn")
          ) {
            Text(
              text = "View All (${uiState.orders.size}) >",
              style = MaterialTheme.typography.labelSmall.copy(
                color = BrandGreenPrimary,
                fontWeight = FontWeight.Bold
              ),
              modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
            )
          }
        }

        Spacer(modifier = Modifier.height(10.dp))

        uiState.orders.take(3).forEach { ord ->
          Surface(
            shape = RoundedCornerShape(10.dp),
            color = MountainSnowBackground,
            border = androidx.compose.foundation.BorderStroke(1.dp, MountainBorder),
            modifier = Modifier
              .fillMaxWidth()
              .padding(vertical = 4.dp)
          ) {
            Row(
              modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp),
              horizontalArrangement = Arrangement.SpaceBetween,
              verticalAlignment = Alignment.CenterVertically
            ) {
              Column(
                modifier = Modifier
                  .weight(1f)
                  .clickable { onSelectOrderToTrack(ord) }
              ) {
                Text(
                  text = "Order #${ord.id}",
                  style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
                )
                Text(
                  text = "${ord.items.size} items • ₹${ord.total.toInt()} • ${ord.status.title}",
                  style = MaterialTheme.typography.labelSmall.copy(color = TextMuted)
                )
              }

              Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                Surface(
                  shape = RoundedCornerShape(6.dp),
                  color = MountainWhite,
                  border = androidx.compose.foundation.BorderStroke(1.dp, BrandGreenPrimary),
                  modifier = Modifier.clickable { onReorder(ord) }
                ) {
                  Text(
                    text = "Re-order",
                    style = MaterialTheme.typography.labelSmall.copy(
                      color = BrandGreenPrimary,
                      fontWeight = FontWeight.Bold
                    ),
                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 4.dp)
                  )
                }

                Surface(
                  shape = RoundedCornerShape(6.dp),
                  color = BrandGreenPrimary,
                  modifier = Modifier.clickable { onSelectOrderToTrack(ord) }
                ) {
                  Text(
                    text = "Track",
                    style = MaterialTheme.typography.labelSmall.copy(
                      color = MountainWhite,
                      fontWeight = FontWeight.Bold
                    ),
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                  )
                }
              }
            }
          }
        }
      }
    }

    // 5. ShahpurGo Care Helpline & Brand Information
    Card(
      shape = RoundedCornerShape(14.dp),
      colors = CardDefaults.cardColors(containerColor = MountainCardSurface),
      border = androidx.compose.foundation.BorderStroke(1.dp, MountainBorder),
      modifier = Modifier.fillMaxWidth()
    ) {
      Column(modifier = Modifier.padding(14.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically) {
          Icon(Icons.Default.HeadsetMic, contentDescription = null, tint = BrandGreenPrimary, modifier = Modifier.size(20.dp))
          Spacer(modifier = Modifier.width(8.dp))
          Text(
            text = "Shahpur Local Help & Support",
            style = MaterialTheme.typography.titleMedium.copy(
              fontWeight = FontWeight.Bold,
              color = TextPrimaryDark
            )
          )
        }

        Spacer(modifier = Modifier.height(6.dp))

        Text(
          text = "Local Shahpur Care Helpline: +91 1892-234567\nSupport hours: 6:00 AM - 11:00 PM daily",
          style = MaterialTheme.typography.bodySmall.copy(color = TextSecondarySlate)
        )

        HorizontalDivider(modifier = Modifier.padding(vertical = 10.dp), color = MountainBorder)

        Row(verticalAlignment = Alignment.CenterVertically) {
          Icon(Icons.Default.Landscape, contentDescription = null, tint = BrandGreenPrimary, modifier = Modifier.size(18.dp))
          Spacer(modifier = Modifier.width(8.dp))
          Text(
            text = "About ShahpurGo",
            style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
          )
        }
        Spacer(modifier = Modifier.height(4.dp))
        Text(
          text = "Brand: ShahpurGo\nTagline: Shahpur Ka Apna Delivery App\nLaunch Location: Shahpur, Kangra, Himachal Pradesh, India\nVersion: 1.0.0 (Made with pride in Kangra Valley)",
          style = MaterialTheme.typography.bodySmall.copy(color = TextMuted, lineHeight = 18.sp)
        )
      }
    }

    Spacer(modifier = Modifier.height(10.dp))
  }



  // Dialog: Login / Signup Simulation
  if (showLoginDialog) {
    LoginSignupDialog(
      onDismiss = { showLoginDialog = false }
    )
  }
}

@Composable
fun LoginSignupDialog(onDismiss: () -> Unit) {
  var phone by remember { mutableStateOf("98160 12345") }
  var otp by remember { mutableStateOf("4821") }
  var isOtpStep by remember { mutableStateOf(false) }

  AlertDialog(
    onDismissRequest = onDismiss,
    title = {
      Row(verticalAlignment = Alignment.CenterVertically) {
        Surface(
          shape = RoundedCornerShape(8.dp),
          color = BrandGreenPrimary,
          modifier = Modifier.size(28.dp)
        ) {
          ShahpurGoBrandLogo(modifier = Modifier.padding(3.dp))
        }
        Spacer(modifier = Modifier.width(8.dp))
        Column {
          Text("ShahpurGo", style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.ExtraBold))
          Text("Shahpur Ka Apna Delivery App", style = MaterialTheme.typography.labelSmall.copy(color = BrandGreenPrimary))
        }
      }
    },
    text = {
      Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        if (!isOtpStep) {
          Text(
            text = "Enter your mobile number to sign in or create an account for Shahpur & Kangra delivery.",
            style = MaterialTheme.typography.bodyMedium.copy(color = TextSecondarySlate)
          )
          OutlinedTextField(
            value = phone,
            onValueChange = { phone = it },
            label = { Text("Mobile Number (+91)") },
            singleLine = true
          )
        } else {
          Text(
            text = "Enter the 4-digit verification code sent to +91 $phone",
            style = MaterialTheme.typography.bodyMedium.copy(color = TextSecondarySlate)
          )
          OutlinedTextField(
            value = otp,
            onValueChange = { otp = it },
            label = { Text("4-digit OTP") },
            singleLine = true
          )
        }
      }
    },
    confirmButton = {
      Button(
        onClick = {
          if (!isOtpStep) {
            isOtpStep = true
          } else {
            onDismiss()
          }
        },
        colors = ButtonDefaults.buttonColors(containerColor = BrandGreenPrimary)
      ) {
        Text(if (!isOtpStep) "Send OTP" else "Verify & Login")
      }
    },
    dismissButton = {
      OutlinedButton(onClick = onDismiss) { Text("Cancel") }
    }
  )
}
