package com.example.ui.components

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
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AdminPanelSettings
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Store
import androidx.compose.ui.layout.ContentScale
import coil.compose.AsyncImage
import com.example.auth.UserSession
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.DeliveryLocation
import com.example.ui.theme.BrandGreenDark
import com.example.ui.theme.BrandGreenLight
import com.example.ui.theme.BrandGreenPrimary
import com.example.ui.theme.MountainWhite
import com.example.ui.theme.RewardGold

@Composable
fun ShahpurGoBrandHeader(
  currentLocation: DeliveryLocation,
  unreadNotificationsCount: Int,
  userSession: UserSession? = null,
  isAdminView: Boolean = false,
  onLocationClick: () -> Unit,
  onNotificationClick: () -> Unit,
  onOpenMerchantHub: (() -> Unit)? = null,
  onProfileClick: (() -> Unit)? = null,
  onToggleAdminView: (() -> Unit)? = null,
  modifier: Modifier = Modifier
) {
  Box(
    modifier = modifier
      .fillMaxWidth()
      .background(
        Brush.verticalGradient(
          colors = listOf(
            BrandGreenDark,
            BrandGreenPrimary
          )
        )
      )
      .statusBarsPadding()
  ) {
    // Subtle background mountain silhouette
    MountainSilhouettes(
      modifier = Modifier
        .align(Alignment.BottomCenter)
        .fillMaxWidth()
        .height(48.dp),
      tint = Color.White.copy(alpha = 0.12f)
    )

    Column(
      modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 16.dp, vertical = 12.dp)
    ) {
      // Row 1: Logo & Brand Name + Actions
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        // Logo + App Name + Tagline
        Row(
          verticalAlignment = Alignment.CenterVertically,
          modifier = Modifier.weight(1f)
        ) {
          // Custom Icon badge
          Surface(
            shape = RoundedCornerShape(10.dp),
            color = MountainWhite,
            modifier = Modifier.size(38.dp)
          ) {
            ShahpurGoBrandLogo(
              modifier = Modifier
                .padding(4.dp)
                .size(30.dp)
            )
          }

          Spacer(modifier = Modifier.width(10.dp))

          Column {
            Row(verticalAlignment = Alignment.CenterVertically) {
              Text(
                text = "Shahpur",
                style = MaterialTheme.typography.titleLarge.copy(
                  fontWeight = FontWeight.ExtraBold,
                  color = MountainWhite,
                  letterSpacing = (-0.5).sp
                )
              )
              Text(
                text = "Go",
                style = MaterialTheme.typography.titleLarge.copy(
                  fontWeight = FontWeight.ExtraBold,
                  color = RewardGold,
                  letterSpacing = (-0.5).sp
                )
              )
              Spacer(modifier = Modifier.width(6.dp))
              // 15-20 Min Delivery badge
              Surface(
                shape = RoundedCornerShape(6.dp),
                color = BrandGreenLight.copy(alpha = 0.9f)
              ) {
                Text(
                  text = "⚡ 15 MINS",
                  style = MaterialTheme.typography.labelSmall.copy(
                    color = MountainWhite,
                    fontWeight = FontWeight.Bold
                  ),
                  modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                )
              }
            }

            Text(
              text = "Shahpur Ka Apna Delivery App",
              style = MaterialTheme.typography.labelSmall.copy(
                color = MountainWhite.copy(alpha = 0.85f),
                fontWeight = FontWeight.Medium
              )
            )
          }
        }

        // Actions: Partner Hub + Notifications
        Row(
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
          // Merchant Hub button
          if (onOpenMerchantHub != null) {
            Surface(
              shape = RoundedCornerShape(8.dp),
              color = MountainWhite.copy(alpha = 0.16f),
              modifier = Modifier
                .clickable { onOpenMerchantHub() }
                .testTag("header_partner_hub_btn")
            ) {
              Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(horizontal = 8.dp, vertical = 6.dp)
              ) {
                Icon(
                  imageVector = Icons.Default.Store,
                  contentDescription = "Merchant Hub",
                  tint = RewardGold,
                  modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                  text = "Hub",
                  style = MaterialTheme.typography.labelSmall.copy(
                    color = MountainWhite,
                    fontWeight = FontWeight.Bold,
                    fontSize = 11.sp
                  )
                )
              }
            }
          }

          // Notifications icon
          IconButton(
            onClick = onNotificationClick,
            modifier = Modifier
              .testTag("notifications_header_button")
              .size(38.dp)
          ) {
            BadgedBox(
              badge = {
                if (unreadNotificationsCount > 0) {
                  Badge(
                    containerColor = RewardGold,
                    contentColor = BrandGreenDark
                  ) {
                    Text("$unreadNotificationsCount")
                  }
                }
              }
            ) {
              Icon(
                imageVector = Icons.Default.Notifications,
                contentDescription = "Notifications",
                tint = MountainWhite,
                modifier = Modifier.size(22.dp)
              )
            }
          }

          // User Profile Session Avatar
          if (onProfileClick != null) {
            Box(
              modifier = Modifier
                .size(34.dp)
                .clip(CircleShape)
                .background(if (userSession?.isGoogleSignedIn == true) RewardGold.copy(alpha = 0.25f) else MountainWhite.copy(alpha = 0.16f))
                .border(
                  1.5.dp,
                  if (userSession?.isGoogleSignedIn == true) RewardGold else MountainWhite.copy(alpha = 0.4f),
                  CircleShape
                )
                .clickable { onProfileClick() }
                .testTag("header_profile_avatar_btn"),
              contentAlignment = Alignment.Center
            ) {
              if (userSession?.photoUrl != null) {
                AsyncImage(
                  model = userSession.photoUrl,
                  contentDescription = userSession.displayName,
                  contentScale = ContentScale.Crop,
                  modifier = Modifier
                    .fillMaxSize()
                    .clip(CircleShape)
                )
              } else if (userSession?.isGoogleSignedIn == true) {
                Text(
                  text = userSession.initials,
                  fontSize = 12.sp,
                  fontWeight = FontWeight.ExtraBold,
                  color = MountainWhite
                )
              } else {
                Icon(
                  imageVector = Icons.Default.Person,
                  contentDescription = "Profile",
                  tint = MountainWhite,
                  modifier = Modifier.size(18.dp)
                )
              }
            }
          }
        }
      }

      Spacer(modifier = Modifier.height(10.dp))

      // Row 2: Delivery Location Bar
      Surface(
        shape = RoundedCornerShape(12.dp),
        color = MountainWhite.copy(alpha = 0.14f),
        modifier = Modifier
          .fillMaxWidth()
          .testTag("delivery_location_header_bar")
          .clickable { onLocationClick() }
      ) {
        Row(
          modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 10.dp, vertical = 7.dp),
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.SpaceBetween
        ) {
          Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.weight(1f)
          ) {
            Box(
              modifier = Modifier
                .size(24.dp)
                .background(RewardGold, CircleShape),
              contentAlignment = Alignment.Center
            ) {
              Icon(
                imageVector = Icons.Default.LocationOn,
                contentDescription = null,
                tint = BrandGreenDark,
                modifier = Modifier.size(15.dp)
              )
            }
            Spacer(modifier = Modifier.width(8.dp))
            Column {
              Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                  text = "Delivering to:",
                  style = MaterialTheme.typography.labelSmall.copy(
                    color = MountainWhite.copy(alpha = 0.8f)
                  )
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                  text = currentLocation.tag,
                  style = MaterialTheme.typography.labelSmall.copy(
                    color = RewardGold,
                    fontWeight = FontWeight.Bold
                  )
                )
              }
              Text(
                text = "${currentLocation.areaName}, Shahpur, Kangra HP",
                style = MaterialTheme.typography.labelMedium.copy(
                  color = MountainWhite,
                  fontWeight = FontWeight.SemiBold
                ),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
              )
            }
          }

          Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
              text = "Change",
              style = MaterialTheme.typography.labelSmall.copy(
                color = MountainWhite.copy(alpha = 0.9f),
                fontWeight = FontWeight.Bold
              )
            )
            Icon(
              imageVector = Icons.Default.KeyboardArrowDown,
              contentDescription = "Select Location",
              tint = MountainWhite,
              modifier = Modifier.size(18.dp)
            )
          }
        }
      }
    }
  }
}
