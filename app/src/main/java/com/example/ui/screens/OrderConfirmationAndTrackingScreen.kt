package com.example.ui.screens

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.DeliveryDining
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Receipt
import androidx.compose.material.icons.filled.Shield
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
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.Order
import com.example.model.OrderStatus
import com.example.ui.components.MountainSilhouettes
import com.example.ui.components.OrderTrackingProgressBar
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
import com.example.ui.theme.WarningRed
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimaryDark
import com.example.ui.theme.TextSecondarySlate

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OrderConfirmationAndTrackingScreen(
  order: Order?,
  onBack: () -> Unit,
  onContinueShopping: () -> Unit,
  onUpdateStatus: ((orderId: String, status: OrderStatus) -> Unit)? = null,
  onCancelOrder: ((orderId: String, reason: String) -> Unit)? = null,
  onSubmitRating: ((orderId: String, rating: Float, review: String) -> Unit)? = null,
  modifier: Modifier = Modifier
) {
  Scaffold(
    topBar = {
      TopAppBar(
        title = {
          Text(
            text = "ShahpurGo Live Tracking",
            style = MaterialTheme.typography.titleMedium.copy(
              fontWeight = FontWeight.Bold,
              color = MountainWhite
            )
          )
        },
        navigationIcon = {
          IconButton(onClick = onBack, modifier = Modifier.testTag("tracking_back_button")) {
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
    if (order == null) {
      Box(
        modifier = modifier
          .fillMaxSize()
          .padding(paddingValues),
        contentAlignment = Alignment.Center
      ) {
        Text("No active order found.")
      }
    } else {
      val infiniteTransition = rememberInfiniteTransition(label = "rider_movement")
      val riderProgress by infiniteTransition.animateFloat(
        initialValue = 0.15f,
        targetValue = 0.85f,
        animationSpec = infiniteRepeatable(
          animation = tween(4000, easing = FastOutSlowInEasing),
          repeatMode = RepeatMode.Reverse
        ),
        label = "rider_progress"
      )

      Column(
        modifier = modifier
          .fillMaxSize()
          .padding(paddingValues)
          .background(MountainSnowBackground)
          .verticalScroll(rememberScrollState())
          .padding(14.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
      ) {
        // 1. Success Hero Card
        Card(
          shape = RoundedCornerShape(16.dp),
          colors = CardDefaults.cardColors(containerColor = BrandGreenPrimary),
          modifier = Modifier
            .fillMaxWidth()
            .testTag("order_success_card")
        ) {
          Box(modifier = Modifier.fillMaxWidth()) {
            MountainSilhouettes(
              modifier = Modifier
                .fillMaxWidth()
                .height(70.dp)
                .align(Alignment.BottomCenter),
              tint = Color.White.copy(alpha = 0.15f)
            )

            Column(
              modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
            ) {
              Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
              ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                  Box(
                    modifier = Modifier
                      .size(28.dp)
                      .background(RewardGold, CircleShape),
                    contentAlignment = Alignment.Center
                  ) {
                    Icon(
                      imageVector = Icons.Default.Check,
                      contentDescription = null,
                      tint = BrandGreenDark,
                      modifier = Modifier.size(18.dp)
                    )
                  }
                  Spacer(modifier = Modifier.width(8.dp))
                  Text(
                    text = "Order Placed! (#${order.id})",
                    style = MaterialTheme.typography.titleMedium.copy(
                      fontWeight = FontWeight.Bold,
                      color = MountainWhite
                    )
                  )
                }

                Surface(
                  shape = RoundedCornerShape(12.dp),
                  color = MountainWhite.copy(alpha = 0.2f)
                ) {
                  Text(
                    text = order.paymentMethod,
                    style = MaterialTheme.typography.labelSmall.copy(
                      color = MountainWhite,
                      fontWeight = FontWeight.Bold
                    ),
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                  )
                }
              }

              Spacer(modifier = Modifier.height(10.dp))

              Text(
                text = "Arriving in ~${order.etaMinutes} mins",
                style = MaterialTheme.typography.headlineLarge.copy(
                  fontWeight = FontWeight.ExtraBold,
                  color = MountainWhite
                )
              )

              Text(
                text = "Shahpur Dark Store received your items. Prepared fresh for speedy dispatch.",
                style = MaterialTheme.typography.bodySmall.copy(
                  color = MountainWhite.copy(alpha = 0.9f)
                ),
                modifier = Modifier.padding(top = 2.dp)
              )
            }
          }
        }

        // Live Firestore Cloud Tracking Status
        Surface(
          shape = RoundedCornerShape(12.dp),
          color = BrandGreenSurface,
          border = androidx.compose.foundation.BorderStroke(1.dp, BrandGreenPrimary.copy(alpha = 0.3f)),
          modifier = Modifier.fillMaxWidth().testTag("live_tracking_cloud_status")
        ) {
          Row(
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
          ) {
            Icon(
              imageVector = Icons.Default.CheckCircle,
              contentDescription = null,
              tint = BrandGreenPrimary,
              modifier = Modifier.size(18.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Column {
              Text(
                text = "Live Firestore Cloud Tracking Active",
                style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold, color = BrandGreenDark)
              )
              Text(
                text = "Order status synchronizes in real time as ${order.shopName} prepares and dispatches.",
                style = MaterialTheme.typography.labelSmall.copy(color = TextMuted)
              )
            }
          }
        }

        // 2. Animated Rider Route Map
        Card(
          shape = RoundedCornerShape(14.dp),
          colors = CardDefaults.cardColors(containerColor = MountainCardSurface),
          border = androidx.compose.foundation.BorderStroke(1.dp, MountainBorder),
          modifier = Modifier.fillMaxWidth().testTag("live_rider_map_card")
        ) {
          Column(modifier = Modifier.padding(14.dp)) {
            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.SpaceBetween,
              verticalAlignment = Alignment.CenterVertically
            ) {
              Text(
                text = "Live Delivery Route in Shahpur",
                style = MaterialTheme.typography.titleMedium.copy(
                  fontWeight = FontWeight.Bold,
                  color = TextPrimaryDark
                )
              )

              Surface(
                shape = RoundedCornerShape(6.dp),
                color = BrandGreenSurface
              ) {
                Text(
                  text = "ON THE WAY",
                  style = MaterialTheme.typography.labelSmall.copy(
                    color = BrandGreenPrimary,
                    fontWeight = FontWeight.Bold,
                    fontSize = 10.sp
                  ),
                  modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                )
              }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Canvas Map Route
            Box(
              modifier = Modifier
                .fillMaxWidth()
                .height(150.dp)
                .clip(RoundedCornerShape(10.dp))
                .background(Color(0xFFE8F5E9))
            ) {
              Canvas(modifier = Modifier.fillMaxSize()) {
                val w = size.width
                val h = size.height

                // Road curve from Store (left) to Home (right)
                val roadPath = Path().apply {
                  moveTo(w * 0.1f, h * 0.7f)
                  quadraticBezierTo(w * 0.45f, h * 0.2f, w * 0.88f, h * 0.6f)
                }
                drawPath(
                  path = roadPath,
                  color = Color(0xFFCBD5E1),
                  style = androidx.compose.ui.graphics.drawscope.Stroke(width = 16f)
                )
                drawPath(
                  path = roadPath,
                  color = Color(0xFF16A34A).copy(alpha = 0.4f),
                  style = androidx.compose.ui.graphics.drawscope.Stroke(width = 6f)
                )

                // Store Marker
                drawCircle(
                  color = Color(0xFF0B7A48),
                  radius = 16f,
                  center = Offset(w * 0.1f, h * 0.7f)
                )

                // Destination Marker
                drawCircle(
                  color = Color(0xFFDC2626),
                  radius = 16f,
                  center = Offset(w * 0.88f, h * 0.6f)
                )

                // Dynamic Rider Position
                val t = riderProgress
                val rx = (1 - t) * (1 - t) * (w * 0.1f) + 2 * (1 - t) * t * (w * 0.45f) + t * t * (w * 0.88f)
                val ry = (1 - t) * (1 - t) * (h * 0.7f) + 2 * (1 - t) * t * (h * 0.2f) + t * t * (h * 0.6f)

                drawCircle(
                  color = Color(0xFFF59E0B),
                  radius = 18f,
                  center = Offset(rx, ry)
                )
                drawCircle(
                  color = Color.White,
                  radius = 8f,
                  center = Offset(rx, ry)
                )
              }

              // Labels on Map
              Surface(
                shape = RoundedCornerShape(4.dp),
                color = BrandGreenDark,
                modifier = Modifier
                  .align(Alignment.BottomStart)
                  .padding(8.dp)
              ) {
                Text(
                  text = "Shahpur Dark Store",
                  style = MaterialTheme.typography.labelSmall.copy(
                    color = MountainWhite,
                    fontSize = 9.sp,
                    fontWeight = FontWeight.Bold
                  ),
                  modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp)
                )
              }

              Surface(
                shape = RoundedCornerShape(4.dp),
                color = Color(0xFF991B1B),
                modifier = Modifier
                  .align(Alignment.BottomEnd)
                  .padding(8.dp)
              ) {
                Text(
                  text = order.deliveryLocation.tag,
                  style = MaterialTheme.typography.labelSmall.copy(
                    color = MountainWhite,
                    fontSize = 9.sp,
                    fontWeight = FontWeight.Bold
                  ),
                  modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp)
                )
              }
            }
          }
        }

        // 2. Visual Step-by-Step Order Tracking Progress Bar (Order Placed -> Delivered)
        OrderTrackingProgressBar(
          currentStatus = order.status,
          onStepClick = { nextSt ->
            onUpdateStatus?.invoke(order.id, nextSt)
          }
        )

        // 3. Quick Status Simulator Controller
        Card(
          shape = RoundedCornerShape(14.dp),
          colors = CardDefaults.cardColors(containerColor = MountainCardSurface),
          border = androidx.compose.foundation.BorderStroke(1.dp, MountainBorder),
          modifier = Modifier.fillMaxWidth().testTag("interactive_status_simulator_card")
        ) {
          Column(modifier = Modifier.padding(14.dp)) {
            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.SpaceBetween,
              verticalAlignment = Alignment.CenterVertically
            ) {
              Text(
                text = "Simulate Order Status (Live Demo)",
                style = MaterialTheme.typography.titleSmall.copy(
                  fontWeight = FontWeight.Bold,
                  color = TextPrimaryDark
                )
              )
              Surface(
                shape = RoundedCornerShape(6.dp),
                color = BrandGreenSurface
              ) {
                Text(
                  text = "TAP STEP",
                  style = MaterialTheme.typography.labelSmall.copy(
                    fontSize = 9.sp,
                    color = BrandGreenPrimary,
                    fontWeight = FontWeight.Bold
                  ),
                  modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                )
              }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
              val allSteps = listOf(
                OrderStatus.CONFIRMED to "1. Placed",
                OrderStatus.PACKED to "2. Packed",
                OrderStatus.OUT_FOR_DELIVERY to "3. Out",
                OrderStatus.DELIVERED to "4. Delivered"
              )

              allSteps.forEach { (st, label) ->
                val isSelected = order.status == st
                Surface(
                  shape = RoundedCornerShape(8.dp),
                  color = if (isSelected) BrandGreenPrimary else MountainSnowBackground,
                  border = androidx.compose.foundation.BorderStroke(
                    1.dp,
                    if (isSelected) BrandGreenDark else MountainBorder
                  ),
                  modifier = Modifier
                    .weight(1f)
                    .testTag("simulate_step_${st.name}")
                    .clip(RoundedCornerShape(8.dp))
                    .clickable {
                      onUpdateStatus?.invoke(order.id, st)
                    }
                ) {
                  Box(
                    modifier = Modifier.padding(vertical = 8.dp),
                    contentAlignment = Alignment.Center
                  ) {
                    Text(
                      text = label,
                      style = MaterialTheme.typography.labelSmall.copy(
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                        color = if (isSelected) MountainWhite else TextPrimaryDark,
                        fontSize = 10.sp
                      ),
                      maxLines = 1
                    )
                  }
                }
              }
            }
          }
        }

        // 4. Delivery Partner Card
        Card(
          shape = RoundedCornerShape(14.dp),
          colors = CardDefaults.cardColors(containerColor = MountainCardSurface),
          border = androidx.compose.foundation.BorderStroke(1.dp, MountainBorder),
          modifier = Modifier.fillMaxWidth().testTag("delivery_partner_card")
        ) {
          Row(
            modifier = Modifier
              .fillMaxWidth()
              .padding(14.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
              Box(
                modifier = Modifier
                  .size(46.dp)
                  .background(BrandGreenContainer, CircleShape),
                contentAlignment = Alignment.Center
              ) {
                Icon(
                  imageVector = Icons.Default.DeliveryDining,
                  contentDescription = null,
                  tint = BrandGreenDark,
                  modifier = Modifier.size(26.dp)
                )
              }
              Spacer(modifier = Modifier.width(10.dp))
              Column {
                Text(
                  text = order.deliveryPartnerName,
                  style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold,
                    color = TextPrimaryDark
                  )
                )
                Text(
                  text = "Verified Shahpur Delivery Partner • 4.9 ★",
                  style = MaterialTheme.typography.labelSmall.copy(color = BrandGreenLight)
                )
                Text(
                  text = order.deliveryPartnerPhone,
                  style = MaterialTheme.typography.labelSmall.copy(color = TextMuted)
                )
              }
            }

            Surface(
              shape = CircleShape,
              color = BrandGreenSurface,
              border = androidx.compose.foundation.BorderStroke(1.dp, BrandGreenPrimary)
            ) {
              IconButton(onClick = { /* simulated partner call */ }) {
                Icon(
                  imageVector = Icons.Default.Call,
                  contentDescription = "Call Delivery Partner",
                  tint = BrandGreenPrimary
                )
              }
            }
          }
        }

        // 5. Order Summary Card
        Card(
          shape = RoundedCornerShape(14.dp),
          colors = CardDefaults.cardColors(containerColor = MountainCardSurface),
          border = androidx.compose.foundation.BorderStroke(1.dp, MountainBorder),
          modifier = Modifier.fillMaxWidth().testTag("order_receipt_card")
        ) {
          Column(modifier = Modifier.padding(14.dp)) {
            Text(
              text = "Ordered Items (${order.items.size})",
              style = MaterialTheme.typography.titleMedium.copy(
                fontWeight = FontWeight.Bold,
                color = TextPrimaryDark
              )
            )

            Spacer(modifier = Modifier.height(8.dp))

            order.items.forEach { cartItem ->
              Row(
                modifier = Modifier
                  .fillMaxWidth()
                  .padding(vertical = 4.dp),
                horizontalArrangement = Arrangement.SpaceBetween
              ) {
                Text(
                  text = "${cartItem.quantity}x ${cartItem.item.name} (${cartItem.item.unit})",
                  style = MaterialTheme.typography.bodyMedium.copy(color = TextPrimaryDark)
                )
                Text(
                  text = "₹${(cartItem.item.price * cartItem.quantity).toInt()}",
                  style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold)
                )
              }
            }

            HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp), color = MountainBorder)

            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.SpaceBetween
            ) {
              Text("Subtotal", style = MaterialTheme.typography.bodySmall.copy(color = TextMuted))
              Text("₹${order.subtotal.toInt()}", style = MaterialTheme.typography.bodySmall)
            }
            if (order.tipAmount > 0) {
              Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
              ) {
                Text("Delivery Tip", style = MaterialTheme.typography.bodySmall.copy(color = TextMuted))
                Text("₹${order.tipAmount.toInt()}", style = MaterialTheme.typography.bodySmall)
              }
            }
            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.SpaceBetween
            ) {
              Text("Shahpur Delivery", style = MaterialTheme.typography.bodySmall.copy(color = TextMuted))
              Text("FREE", style = MaterialTheme.typography.bodySmall.copy(color = BrandGreenLight, fontWeight = FontWeight.Bold))
            }

            HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp), color = MountainBorder)

            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.SpaceBetween,
              verticalAlignment = Alignment.CenterVertically
            ) {
              Text("Total Paid / Due on Delivery", style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold))
              Text(
                "₹${order.total.toInt()}",
                style = MaterialTheme.typography.titleMedium.copy(
                  fontWeight = FontWeight.ExtraBold,
                  color = BrandGreenPrimary
                )
              )
            }
          }
        }

        var showCancelDialog by remember { mutableStateOf(false) }
        var cancelReason by remember { mutableStateOf("Changed my mind / Ordered by mistake") }
        var showRatingDialog by remember { mutableStateOf(false) }
        var reviewText by remember { mutableStateOf("") }
        var ratingStars by remember { mutableFloatStateOf(5.0f) }

        // Cancellation Dialog
        if (showCancelDialog) {
          AlertDialog(
            onDismissRequest = { showCancelDialog = false },
            title = { Text("Cancel Order #${order.id}?", fontWeight = FontWeight.Bold) },
            text = {
              Column {
                Text("Are you sure you want to cancel? Items will be restocked immediately.", fontSize = 13.sp)
                Spacer(modifier = Modifier.height(10.dp))
                OutlinedTextField(
                  value = cancelReason,
                  onValueChange = { cancelReason = it },
                  label = { Text("Reason for cancellation") },
                  singleLine = true,
                  modifier = Modifier.fillMaxWidth().testTag("cancel_reason_input")
                )
              }
            },
            confirmButton = {
              Button(
                onClick = {
                  showCancelDialog = false
                  onCancelOrder?.invoke(order.id, cancelReason)
                },
                colors = ButtonDefaults.buttonColors(containerColor = WarningRed)
              ) {
                Text("Confirm Cancel")
              }
            },
            dismissButton = {
              TextButton(onClick = { showCancelDialog = false }) {
                Text("Keep Order")
              }
            }
          )
        }

        // Rating Dialog
        if (showRatingDialog) {
          AlertDialog(
            onDismissRequest = { showRatingDialog = false },
            title = { Text("Rate & Review Your Order", fontWeight = FontWeight.Bold) },
            text = {
              Column {
                Text("How was your delivery experience with ${order.shopName}?", fontSize = 13.sp)
                Spacer(modifier = Modifier.height(12.dp))
                Row(
                  modifier = Modifier.fillMaxWidth(),
                  horizontalArrangement = Arrangement.Center
                ) {
                  (1..5).forEach { star ->
                    IconButton(onClick = { ratingStars = star.toFloat() }) {
                      Text(
                        text = if (star <= ratingStars) "⭐" else "☆",
                        fontSize = 26.sp
                      )
                    }
                  }
                }
                Spacer(modifier = Modifier.height(10.dp))
                OutlinedTextField(
                  value = reviewText,
                  onValueChange = { reviewText = it },
                  label = { Text("Add comments (optional)") },
                  placeholder = { Text("Fresh items, fast delivery!") },
                  modifier = Modifier.fillMaxWidth().testTag("rating_review_input")
                )
              }
            },
            confirmButton = {
              Button(
                onClick = {
                  showRatingDialog = false
                  onSubmitRating?.invoke(order.id, ratingStars, reviewText)
                },
                colors = ButtonDefaults.buttonColors(containerColor = BrandGreenPrimary)
              ) {
                Text("Submit Review")
              }
            },
            dismissButton = {
              TextButton(onClick = { showRatingDialog = false }) {
                Text("Later")
              }
            }
          )
        }

        // Cancel order button if cancellable
        if (order.status.isCancellable) {
          OutlinedButton(
            onClick = { showCancelDialog = true },
            modifier = Modifier
              .fillMaxWidth()
              .height(48.dp)
              .testTag("cancel_order_button"),
            colors = ButtonDefaults.outlinedButtonColors(contentColor = WarningRed),
            border = androidx.compose.foundation.BorderStroke(1.dp, WarningRed),
            shape = RoundedCornerShape(10.dp)
          ) {
            Text("Cancel Order", fontWeight = FontWeight.Bold)
          }
        }

        // Rate Order button if delivered
        if (order.status == OrderStatus.DELIVERED) {
          Button(
            onClick = { showRatingDialog = true },
            modifier = Modifier
              .fillMaxWidth()
              .height(48.dp)
              .testTag("rate_order_button"),
            colors = ButtonDefaults.buttonColors(containerColor = RewardGold),
            shape = RoundedCornerShape(10.dp)
          ) {
            Text(
              text = if (order.rating > 0) "Update Rating (⭐ ${order.rating.toInt()})" else "Rate & Review Delivery ⭐",
              color = BrandGreenDark,
              fontWeight = FontWeight.Bold
            )
          }
        }

        // Return button
        Button(
          onClick = onContinueShopping,
          modifier = Modifier
            .fillMaxWidth()
            .height(48.dp)
            .testTag("continue_shopping_btn"),
          colors = ButtonDefaults.buttonColors(containerColor = BrandGreenPrimary),
          shape = RoundedCornerShape(10.dp)
        ) {
          Text("Continue Shopping on ShahpurGo")
        }

        Spacer(modifier = Modifier.height(16.dp))
      }
    }
  }
}
