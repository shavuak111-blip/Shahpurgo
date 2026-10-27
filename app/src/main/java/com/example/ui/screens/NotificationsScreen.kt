package com.example.ui.screens

import androidx.compose.foundation.background
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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.DoneAll
import androidx.compose.material.icons.filled.LocalOffer
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.AppNotification
import com.example.model.NotificationType
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotificationsScreen(
  notifications: List<AppNotification>,
  onMarkAllRead: () -> Unit,
  onBack: () -> Unit,
  modifier: Modifier = Modifier
) {
  Scaffold(
    topBar = {
      TopAppBar(
        title = {
          Column {
            Text(
              text = "ShahpurGo Notifications",
              style = MaterialTheme.typography.titleMedium.copy(
                fontWeight = FontWeight.Bold,
                color = MountainWhite
              )
            )
            Text(
              text = "Updates from Shahpur Store",
              style = MaterialTheme.typography.labelSmall.copy(
                color = MountainWhite.copy(alpha = 0.85f)
              )
            )
          }
        },
        navigationIcon = {
          IconButton(onClick = onBack, modifier = Modifier.testTag("notifs_back_button")) {
            Icon(
              imageVector = Icons.Default.ArrowBack,
              contentDescription = "Back",
              tint = MountainWhite
            )
          }
        },
        actions = {
          IconButton(onClick = onMarkAllRead, modifier = Modifier.testTag("mark_all_read_btn")) {
            Icon(
              imageVector = Icons.Default.DoneAll,
              contentDescription = "Mark All Read",
              tint = MountainWhite
            )
          }
        },
        colors = TopAppBarDefaults.topAppBarColors(containerColor = BrandGreenPrimary)
      )
    }
  ) { paddingValues ->
    if (notifications.isEmpty()) {
      Box(
        modifier = modifier
          .fillMaxSize()
          .padding(paddingValues),
        contentAlignment = Alignment.Center
      ) {
        Text("No notifications yet", style = MaterialTheme.typography.bodyMedium.copy(color = TextMuted))
      }
    } else {
      LazyColumn(
        modifier = modifier
          .fillMaxSize()
          .padding(paddingValues)
          .background(MountainSnowBackground)
          .padding(14.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
      ) {
        items(notifications, key = { it.id }) { notif ->
          Card(
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(
              containerColor = if (notif.isRead) MountainCardSurface else BrandGreenSurface
            ),
            border = androidx.compose.foundation.BorderStroke(
              1.dp,
              if (notif.isRead) MountainBorder else BrandGreenLight.copy(alpha = 0.5f)
            ),
            modifier = Modifier.fillMaxWidth().testTag("notification_item_${notif.id}")
          ) {
            Row(
              modifier = Modifier.padding(12.dp),
              verticalAlignment = Alignment.Top
            ) {
              val (icon, tintColor) = when (notif.type) {
                NotificationType.ORDER -> Icons.Default.ShoppingBag to BrandGreenPrimary
                NotificationType.REWARD -> Icons.Default.Star to RewardGold
                NotificationType.PROMO -> Icons.Default.LocalOffer to BrandGreenLight
                NotificationType.SYSTEM -> Icons.Default.Notifications to TextMuted
              }

              Box(
                modifier = Modifier
                  .size(36.dp)
                  .background(BrandGreenContainer, CircleShape),
                contentAlignment = Alignment.Center
              ) {
                Icon(
                  imageVector = icon,
                  contentDescription = null,
                  tint = tintColor,
                  modifier = Modifier.size(18.dp)
                )
              }

              Spacer(modifier = Modifier.width(10.dp))

              Column(modifier = Modifier.weight(1f)) {
                Row(
                  modifier = Modifier.fillMaxWidth(),
                  horizontalArrangement = Arrangement.SpaceBetween,
                  verticalAlignment = Alignment.CenterVertically
                ) {
                  Text(
                    text = notif.title,
                    style = MaterialTheme.typography.titleSmall.copy(
                      fontWeight = if (notif.isRead) FontWeight.SemiBold else FontWeight.Bold,
                      color = TextPrimaryDark
                    )
                  )
                  Text(
                    text = notif.timeAgo,
                    style = MaterialTheme.typography.labelSmall.copy(
                      color = TextMuted,
                      fontSize = 10.sp
                    )
                  )
                }

                Spacer(modifier = Modifier.height(3.dp))

                Text(
                  text = notif.message,
                  style = MaterialTheme.typography.bodySmall.copy(color = TextSecondarySlate)
                )
              }
            }
          }
        }
      }
    }
  }
}
