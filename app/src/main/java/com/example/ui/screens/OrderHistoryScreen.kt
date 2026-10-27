package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.DeliveryDining
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Receipt
import androidx.compose.material.icons.filled.Replay
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.Order
import com.example.model.OrderStatus
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
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

enum class OrderHistoryFilter(val label: String) {
  ALL("All Orders"),
  DELIVERED("Delivered"),
  ACTIVE("Active / On The Way")
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OrderHistoryScreen(
  orders: List<Order>,
  onReorder: (Order) -> Unit,
  onTrackOrder: (Order) -> Unit,
  onBack: () -> Unit,
  onBrowseGroceries: () -> Unit,
  modifier: Modifier = Modifier
) {
  var selectedFilter by remember { mutableStateOf(OrderHistoryFilter.ALL) }

  val filteredOrders = orders.filter { ord ->
    when (selectedFilter) {
      OrderHistoryFilter.ALL -> true
      OrderHistoryFilter.DELIVERED -> ord.status == OrderStatus.DELIVERED
      OrderHistoryFilter.ACTIVE -> ord.status != OrderStatus.DELIVERED
    }
  }

  Scaffold(
    topBar = {
      TopAppBar(
        title = {
          Column {
            Text(
              text = "Past Orders",
              style = MaterialTheme.typography.titleMedium.copy(
                fontWeight = FontWeight.Bold,
                color = MountainWhite
              )
            )
            Text(
              text = "ShahpurGo • ${orders.size} Total Orders",
              style = MaterialTheme.typography.labelSmall.copy(
                color = MountainWhite.copy(alpha = 0.85f)
              )
            )
          }
        },
        navigationIcon = {
          IconButton(onClick = onBack, modifier = Modifier.testTag("order_history_back_button")) {
            Icon(
              imageVector = Icons.Default.ArrowBack,
              contentDescription = "Back",
              tint = MountainWhite
            )
          }
        },
        colors = TopAppBarDefaults.topAppBarColors(containerColor = BrandGreenPrimary)
      )
    }
  ) { paddingValues ->
    Column(
      modifier = modifier
        .fillMaxSize()
        .padding(paddingValues)
        .background(MountainSnowBackground)
    ) {
      // Filter Tabs Row
      Surface(
        color = MountainCardSurface,
        border = androidx.compose.foundation.BorderStroke(1.dp, MountainBorder),
        modifier = Modifier.fillMaxWidth()
      ) {
        LazyRow(
          modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp),
          horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
          items(OrderHistoryFilter.values()) { filter ->
            val isSelected = selectedFilter == filter
            Surface(
              shape = RoundedCornerShape(20.dp),
              color = if (isSelected) BrandGreenPrimary else BrandGreenSurface,
              border = if (isSelected) null else androidx.compose.foundation.BorderStroke(1.dp, BrandGreenContainer),
              modifier = Modifier
                .clip(RoundedCornerShape(20.dp))
                .clickable { selectedFilter = filter }
                .testTag("history_filter_${filter.name}")
            ) {
              Text(
                text = filter.label,
                style = MaterialTheme.typography.labelMedium.copy(
                  color = if (isSelected) MountainWhite else BrandGreenDark,
                  fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
                ),
                modifier = Modifier.padding(horizontal = 14.dp, vertical = 6.dp)
              )
            }
          }
        }
      }

      // Cloud Sync Indicator
      Surface(
        color = BrandGreenSurface,
        modifier = Modifier.fillMaxWidth().testTag("order_history_cloud_banner")
      ) {
        Row(
          modifier = Modifier.padding(horizontal = 14.dp, vertical = 6.dp),
          verticalAlignment = Alignment.CenterVertically
        ) {
          Icon(
            imageVector = Icons.Default.CheckCircle,
            contentDescription = null,
            tint = BrandGreenPrimary,
            modifier = Modifier.size(16.dp)
          )
          Spacer(modifier = Modifier.width(6.dp))
          Text(
            text = "Cloud Sync Active • Real-time order updates from Firestore",
            style = MaterialTheme.typography.labelSmall.copy(color = BrandGreenDark, fontWeight = FontWeight.Medium)
          )
        }
      }

      if (filteredOrders.isEmpty()) {
        Box(
          modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
          contentAlignment = Alignment.Center
        ) {
          Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Box(
              modifier = Modifier
                .size(70.dp)
                .background(BrandGreenSurface, CircleShape),
              contentAlignment = Alignment.Center
            ) {
              Icon(
                imageVector = Icons.Default.History,
                contentDescription = null,
                tint = BrandGreenPrimary,
                modifier = Modifier.size(36.dp)
              )
            }
            Spacer(modifier = Modifier.height(14.dp))
            Text(
              text = "No orders found in this section",
              style = MaterialTheme.typography.titleMedium.copy(
                fontWeight = FontWeight.Bold,
                color = TextPrimaryDark
              )
            )
            Spacer(modifier = Modifier.height(6.dp))
            Text(
              text = "Browse fresh Himachal groceries and place your order in Shahpur!",
              style = MaterialTheme.typography.bodySmall.copy(color = TextMuted),
              textAlign = androidx.compose.ui.text.style.TextAlign.Center
            )
            Spacer(modifier = Modifier.height(16.dp))
            Button(
              onClick = onBrowseGroceries,
              colors = ButtonDefaults.buttonColors(containerColor = BrandGreenPrimary)
            ) {
              Text("Shop Fresh Groceries")
            }
          }
        }
      } else {
        LazyColumn(
          modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 14.dp),
          contentPadding = PaddingValues(top = 12.dp, bottom = 24.dp),
          verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
          items(filteredOrders, key = { it.id }) { order ->
            PastOrderCard(
              order = order,
              onReorderClick = { onReorder(order) },
              onTrackClick = { onTrackOrder(order) }
            )
          }
        }
      }
    }
  }
}

@Composable
fun PastOrderCard(
  order: Order,
  onReorderClick: () -> Unit,
  onTrackClick: () -> Unit
) {
  var isExpanded by remember { mutableStateOf(false) }

  val dateStr = remember(order.timestamp) {
    val sdf = SimpleDateFormat("dd MMM, hh:mm a", Locale.getDefault())
    sdf.format(Date(order.timestamp))
  }

  val isDelivered = order.status == OrderStatus.DELIVERED

  Card(
    shape = RoundedCornerShape(16.dp),
    colors = CardDefaults.cardColors(containerColor = MountainCardSurface),
    border = androidx.compose.foundation.BorderStroke(1.dp, MountainBorder),
    modifier = Modifier
      .fillMaxWidth()
      .testTag("order_history_card_${order.id}")
  ) {
    Column(modifier = Modifier.padding(14.dp)) {
      // 1. Order Header: ID, Date & Status
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
          Box(
            modifier = Modifier
              .size(34.dp)
              .background(if (isDelivered) BrandGreenSurface else RewardGold.copy(alpha = 0.15f), CircleShape),
            contentAlignment = Alignment.Center
          ) {
            Icon(
              imageVector = if (isDelivered) Icons.Default.CheckCircle else Icons.Default.DeliveryDining,
              contentDescription = null,
              tint = if (isDelivered) BrandGreenPrimary else RewardGold,
              modifier = Modifier.size(18.dp)
            )
          }

          Spacer(modifier = Modifier.width(10.dp))

          Column {
            Text(
              text = "Order #${order.id}",
              style = MaterialTheme.typography.titleMedium.copy(
                fontWeight = FontWeight.Bold,
                color = TextPrimaryDark
              )
            )
            Text(
              text = dateStr,
              style = MaterialTheme.typography.labelSmall.copy(color = TextMuted)
            )
          }
        }

        Surface(
          shape = RoundedCornerShape(8.dp),
          color = if (isDelivered) BrandGreenContainer else Color(0xFFFEF08A)
        ) {
          Text(
            text = order.status.title,
            style = MaterialTheme.typography.labelSmall.copy(
              color = if (isDelivered) BrandGreenDark else Color(0xFF854D0E),
              fontWeight = FontWeight.Bold,
              fontSize = 10.sp
            ),
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
          )
        }
      }

      Spacer(modifier = Modifier.height(10.dp))

      // 2. Location & Partner Info
      Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
      ) {
        Icon(
          imageVector = Icons.Default.LocationOn,
          contentDescription = null,
          tint = BrandGreenPrimary,
          modifier = Modifier.size(15.dp)
        )
        Spacer(modifier = Modifier.width(4.dp))
        Text(
          text = "${order.deliveryLocation.tag} • ${order.deliveryLocation.areaName}",
          style = MaterialTheme.typography.bodySmall.copy(
            color = TextSecondarySlate,
            fontWeight = FontWeight.Medium
          ),
          maxLines = 1,
          overflow = TextOverflow.Ellipsis
        )
      }

      Spacer(modifier = Modifier.height(10.dp))

      // 3. Item List Preview / Expanded View
      Surface(
        shape = RoundedCornerShape(10.dp),
        color = MountainSnowBackground,
        border = androidx.compose.foundation.BorderStroke(1.dp, MountainBorder),
        modifier = Modifier.fillMaxWidth()
      ) {
        Column(modifier = Modifier.padding(10.dp)) {
          val displayedItems = if (isExpanded) order.items else order.items.take(2)

          displayedItems.forEach { cartItem ->
            Row(
              modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 3.dp),
              horizontalArrangement = Arrangement.SpaceBetween,
              verticalAlignment = Alignment.CenterVertically
            ) {
              Text(
                text = "${cartItem.quantity}x ${cartItem.item.name}",
                style = MaterialTheme.typography.bodySmall.copy(
                  fontWeight = FontWeight.Medium,
                  color = TextPrimaryDark
                ),
                modifier = Modifier.weight(1f),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
              )
              Text(
                text = "₹${(cartItem.item.price * cartItem.quantity).toInt()}",
                style = MaterialTheme.typography.bodySmall.copy(
                  fontWeight = FontWeight.SemiBold,
                  color = TextSecondarySlate
                )
              )
            }
          }

          if (order.items.size > 2) {
            Spacer(modifier = Modifier.height(4.dp))
            Row(
              modifier = Modifier
                .fillMaxWidth()
                .clickable { isExpanded = !isExpanded },
              horizontalArrangement = Arrangement.Center,
              verticalAlignment = Alignment.CenterVertically
            ) {
              Text(
                text = if (isExpanded) "Show Less" else "+${order.items.size - 2} more items",
                style = MaterialTheme.typography.labelSmall.copy(
                  color = BrandGreenPrimary,
                  fontWeight = FontWeight.Bold
                )
              )
              Icon(
                imageVector = if (isExpanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                contentDescription = null,
                tint = BrandGreenPrimary,
                modifier = Modifier.size(16.dp)
              )
            }
          }
        }
      }

      Spacer(modifier = Modifier.height(12.dp))

      // 4. Total Amount & Payment Method
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Column {
          Text(
            text = "Total Paid",
            style = MaterialTheme.typography.labelSmall.copy(color = TextMuted)
          )
          Text(
            text = "₹${order.total.toInt()}",
            style = MaterialTheme.typography.titleLarge.copy(
              fontWeight = FontWeight.ExtraBold,
              color = BrandGreenDark
            )
          )
        }

        Surface(
          shape = RoundedCornerShape(6.dp),
          color = BrandGreenSurface
        ) {
          Text(
            text = order.paymentMethod,
            style = MaterialTheme.typography.labelSmall.copy(
              color = BrandGreenPrimary,
              fontWeight = FontWeight.SemiBold,
              fontSize = 10.sp
            ),
            modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp)
          )
        }
      }

      Spacer(modifier = Modifier.height(14.dp))
      HorizontalDivider(color = MountainBorder.copy(alpha = 0.6f))
      Spacer(modifier = Modifier.height(10.dp))

      // 5. Actions: Re-order All Items & Track / Receipt
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
      ) {
        // Track / View Receipt
        OutlinedButton(
          onClick = onTrackClick,
          modifier = Modifier
            .weight(1f)
            .testTag("track_order_${order.id}_btn"),
          shape = RoundedCornerShape(10.dp),
          colors = ButtonDefaults.outlinedButtonColors(contentColor = BrandGreenDark)
        ) {
          Icon(
            imageVector = if (isDelivered) Icons.Default.Receipt else Icons.Default.DeliveryDining,
            contentDescription = null,
            modifier = Modifier.size(16.dp),
            tint = BrandGreenDark
          )
          Spacer(modifier = Modifier.width(6.dp))
          Text(
            text = if (isDelivered) "Receipt" else "Track Live",
            style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold)
          )
        }

        // Re-order All Items
        Button(
          onClick = onReorderClick,
          modifier = Modifier
            .weight(1f)
            .testTag("reorder_button_${order.id}"),
          shape = RoundedCornerShape(10.dp),
          colors = ButtonDefaults.buttonColors(
            containerColor = BrandGreenPrimary,
            contentColor = MountainWhite
          )
        ) {
          Icon(
            imageVector = Icons.Default.Replay,
            contentDescription = null,
            modifier = Modifier.size(16.dp)
          )
          Spacer(modifier = Modifier.width(6.dp))
          Text(
            text = "Re-order",
            style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold)
          )
        }
      }
    }
  }
}
