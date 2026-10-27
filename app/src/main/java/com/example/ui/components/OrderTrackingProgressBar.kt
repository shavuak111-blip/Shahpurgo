package com.example.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
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
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.DeliveryDining
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Inventory2
import androidx.compose.material.icons.filled.ReceiptLong
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.OrderStatus
import com.example.ui.theme.BrandGreenAccent
import com.example.ui.theme.BrandGreenContainer
import com.example.ui.theme.BrandGreenDark
import com.example.ui.theme.BrandGreenLight
import com.example.ui.theme.BrandGreenPrimary
import com.example.ui.theme.BrandGreenSurface
import com.example.ui.theme.MountainBorder
import com.example.ui.theme.MountainCardSurface
import com.example.ui.theme.MountainWhite
import com.example.ui.theme.RewardGold
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimaryDark
import com.example.ui.theme.TextSecondarySlate

data class TrackingStep(
  val status: OrderStatus,
  val title: String,
  val subtitle: String,
  val timeEstimate: String,
  val icon: ImageVector
)

val trackingSteps = listOf(
  TrackingStep(
    status = OrderStatus.CONFIRMED,
    title = "Order Placed",
    subtitle = "Confirmed by Shahpur Hub",
    timeEstimate = "10:30 AM",
    icon = Icons.Default.ReceiptLong
  ),
  TrackingStep(
    status = OrderStatus.PACKED,
    title = "Order Packed",
    subtitle = "Fresh items bagged",
    timeEstimate = "10:36 AM",
    icon = Icons.Default.Inventory2
  ),
  TrackingStep(
    status = OrderStatus.OUT_FOR_DELIVERY,
    title = "Out for Delivery",
    subtitle = "Rider on Kangra road",
    timeEstimate = "10:44 AM",
    icon = Icons.Default.DeliveryDining
  ),
  TrackingStep(
    status = OrderStatus.DELIVERED,
    title = "Delivered",
    subtitle = "At your doorstep",
    timeEstimate = "10:55 AM (Est.)",
    icon = Icons.Default.Home
  )
)

/**
 * Visual step-by-step order tracking progress bar
 * Showing status from 'Order Placed' to 'Delivered'
 * with connecting animated lines, pulsing active step indicator,
 * and comprehensive stage descriptions.
 */
@Composable
fun OrderTrackingProgressBar(
  currentStatus: OrderStatus,
  onStepClick: ((OrderStatus) -> Unit)? = null,
  modifier: Modifier = Modifier
) {
  val currentStepIndex = currentStatus.stepIndex

  // Infinite pulsing transition for current active step
  val infiniteTransition = rememberInfiniteTransition(label = "pulse_active_step")
  val pulseScale by infiniteTransition.animateFloat(
    initialValue = 1f,
    targetValue = 1.18f,
    animationSpec = infiniteRepeatable(
      animation = tween(1200, easing = FastOutSlowInEasing),
      repeatMode = RepeatMode.Reverse
    ),
    label = "pulse_scale"
  )
  val pulseAlpha by infiniteTransition.animateFloat(
    initialValue = 0.5f,
    targetValue = 0.05f,
    animationSpec = infiniteRepeatable(
      animation = tween(1200, easing = FastOutSlowInEasing),
      repeatMode = RepeatMode.Reverse
    ),
    label = "pulse_alpha"
  )

  // Overall completion ratio (0.0 to 1.0)
  val targetRatio = (currentStepIndex.toFloat() / (trackingSteps.size - 1).toFloat()).coerceIn(0f, 1f)
  val animatedRatio by animateFloatAsState(
    targetValue = targetRatio,
    animationSpec = tween(durationMillis = 600, easing = FastOutSlowInEasing),
    label = "total_progress"
  )

  Card(
    shape = RoundedCornerShape(16.dp),
    colors = CardDefaults.cardColors(containerColor = MountainCardSurface),
    border = androidx.compose.foundation.BorderStroke(1.dp, MountainBorder),
    modifier = modifier
      .fillMaxWidth()
      .testTag("order_tracking_progress_bar")
  ) {
    Column(
      modifier = Modifier
        .fillMaxWidth()
        .padding(16.dp)
    ) {
      // Header: Title + Overall Percentage & Status Badge
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Column {
          Text(
            text = "Delivery Status",
            style = MaterialTheme.typography.titleMedium.copy(
              fontWeight = FontWeight.Bold,
              color = TextPrimaryDark
            )
          )
          Text(
            text = "Tracking: Order Placed → Delivered",
            style = MaterialTheme.typography.labelSmall.copy(color = TextMuted)
          )
        }

        Surface(
          shape = RoundedCornerShape(20.dp),
          color = if (currentStatus == OrderStatus.DELIVERED) BrandGreenContainer else BrandGreenSurface,
          border = androidx.compose.foundation.BorderStroke(
            1.dp,
            if (currentStatus == OrderStatus.DELIVERED) BrandGreenPrimary else BrandGreenLight
          )
        ) {
          Row(
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
            verticalAlignment = Alignment.CenterVertically
          ) {
            Box(
              modifier = Modifier
                .size(7.dp)
                .background(
                  color = if (currentStatus == OrderStatus.DELIVERED) BrandGreenDark else RewardGold,
                  shape = CircleShape
                )
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(
              text = currentStatus.title.uppercase(),
              style = MaterialTheme.typography.labelSmall.copy(
                fontWeight = FontWeight.Bold,
                fontSize = 10.sp,
                color = BrandGreenDark
              )
            )
          }
        }
      }

      Spacer(modifier = Modifier.height(16.dp))

      // Overall Linear Progress Indicator Bar
      Column(modifier = Modifier.fillMaxWidth()) {
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween
        ) {
          Text(
            text = "Progress",
            style = MaterialTheme.typography.labelSmall.copy(color = TextMuted, fontSize = 10.sp)
          )
          Text(
            text = "${(animatedRatio * 100).toInt()}% Complete",
            style = MaterialTheme.typography.labelSmall.copy(
              color = BrandGreenPrimary,
              fontWeight = FontWeight.Bold,
              fontSize = 10.sp
            )
          )
        }
        Spacer(modifier = Modifier.height(4.dp))
        LinearProgressIndicator(
          progress = { animatedRatio },
          modifier = Modifier
            .fillMaxWidth()
            .height(6.dp)
            .clip(RoundedCornerShape(3.dp)),
          color = BrandGreenPrimary,
          trackColor = Color(0xFFE2E8F0)
        )
      }

      Spacer(modifier = Modifier.height(20.dp))

      // Horizontal Step-by-Step Stepper Nodes with Connected Bar
      Box(modifier = Modifier.fillMaxWidth()) {
        // Connecting Horizontal Lines in Background
        Canvas(
          modifier = Modifier
            .fillMaxWidth()
            .height(40.dp)
            .padding(horizontal = 24.dp)
        ) {
          val w = size.width
          val y = size.height / 2f
          val stepCount = trackingSteps.size
          val segmentWidth = w / (stepCount - 1)

          for (i in 0 until stepCount - 1) {
            val startX = i * segmentWidth
            val endX = (i + 1) * segmentWidth

            // Inactive base line
            drawLine(
              color = Color(0xFFE2E8F0),
              start = Offset(startX, y),
              end = Offset(endX, y),
              strokeWidth = 6f,
              cap = StrokeCap.Round
            )

            // Active / Completed line fill
            if (currentStepIndex > i) {
              drawLine(
                brush = Brush.horizontalGradient(
                  colors = listOf(BrandGreenPrimary, BrandGreenLight)
                ),
                start = Offset(startX, y),
                end = Offset(endX, y),
                strokeWidth = 6f,
                cap = StrokeCap.Round
              )
            } else if (currentStepIndex == i) {
              // Partial progress toward next step
              drawLine(
                brush = Brush.horizontalGradient(
                  colors = listOf(BrandGreenPrimary, BrandGreenAccent)
                ),
                start = Offset(startX, y),
                end = Offset(startX + (segmentWidth * 0.5f), y),
                strokeWidth = 6f,
                cap = StrokeCap.Round
              )
            }
          }
        }

        // Stepper Nodes
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          trackingSteps.forEachIndexed { index, step ->
            val isCompleted = currentStepIndex > index
            val isCurrent = currentStepIndex == index
            val isPending = currentStepIndex < index

            val nodeBgColor by animateColorAsState(
              targetValue = when {
                isCompleted -> BrandGreenPrimary
                isCurrent -> BrandGreenPrimary
                else -> Color(0xFFF1F5F9)
              },
              label = "node_bg"
            )

            val iconColor by animateColorAsState(
              targetValue = when {
                isCompleted || isCurrent -> MountainWhite
                else -> Color(0xFF94A3B8)
              },
              label = "icon_color"
            )

            Box(
              contentAlignment = Alignment.Center,
              modifier = Modifier
                .testTag("step_node_${step.status.name}")
                .clickable(enabled = onStepClick != null) {
                  onStepClick?.invoke(step.status)
                }
            ) {
              // Pulsing outer ripple for currently active node
              if (isCurrent && currentStatus != OrderStatus.DELIVERED) {
                Box(
                  modifier = Modifier
                    .size(46.dp)
                    .scale(pulseScale)
                    .background(
                      color = BrandGreenPrimary.copy(alpha = pulseAlpha),
                      shape = CircleShape
                    )
                )
              }

              // Node circle
              Box(
                modifier = Modifier
                  .size(38.dp)
                  .background(color = nodeBgColor, shape = CircleShape)
                  .border(
                    width = if (isCurrent) 2.5.dp else 1.dp,
                    color = if (isCurrent) RewardGold else if (isCompleted) BrandGreenDark else MountainBorder,
                    shape = CircleShape
                  ),
                contentAlignment = Alignment.Center
              ) {
                if (isCompleted) {
                  Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = "Completed",
                    tint = MountainWhite,
                    modifier = Modifier.size(20.dp)
                  )
                } else {
                  Icon(
                    imageVector = step.icon,
                    contentDescription = step.title,
                    tint = iconColor,
                    modifier = Modifier.size(19.dp)
                  )
                }
              }
            }
          }
        }
      }

      Spacer(modifier = Modifier.height(8.dp))

      // Step Labels Row Underneath
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
      ) {
        trackingSteps.forEachIndexed { index, step ->
          val isCurrent = currentStepIndex == index
          val isCompleted = currentStepIndex > index

          Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.width(76.dp)
          ) {
            Text(
              text = step.title,
              style = MaterialTheme.typography.labelSmall.copy(
                fontWeight = if (isCurrent) FontWeight.ExtraBold else if (isCompleted) FontWeight.Bold else FontWeight.Medium,
                fontSize = 10.sp,
                color = if (isCurrent) BrandGreenDark else if (isCompleted) TextPrimaryDark else TextMuted
              ),
              textAlign = TextAlign.Center,
              maxLines = 2,
              lineHeight = 12.sp
            )

            Spacer(modifier = Modifier.height(2.dp))

            Text(
              text = step.timeEstimate,
              style = MaterialTheme.typography.labelSmall.copy(
                fontSize = 8.5.sp,
                fontWeight = if (isCurrent) FontWeight.Bold else FontWeight.Normal,
                color = if (isCurrent) BrandGreenLight else TextMuted
              ),
              textAlign = TextAlign.Center
            )
          }
        }
      }

      Spacer(modifier = Modifier.height(14.dp))

      // Current Active Stage Description Box
      val currentStepData = trackingSteps[currentStepIndex.coerceIn(0, trackingSteps.size - 1)]
      Surface(
        shape = RoundedCornerShape(12.dp),
        color = BrandGreenSurface,
        border = androidx.compose.foundation.BorderStroke(1.dp, BrandGreenContainer),
        modifier = Modifier.fillMaxWidth()
      ) {
        Row(
          modifier = Modifier
            .fillMaxWidth()
            .padding(12.dp),
          verticalAlignment = Alignment.CenterVertically
        ) {
          Box(
            modifier = Modifier
              .size(34.dp)
              .background(BrandGreenContainer, CircleShape),
            contentAlignment = Alignment.Center
          ) {
            Icon(
              imageVector = currentStepData.icon,
              contentDescription = null,
              tint = BrandGreenDark,
              modifier = Modifier.size(20.dp)
            )
          }

          Spacer(modifier = Modifier.width(10.dp))

          Column(modifier = Modifier.weight(1f)) {
            Text(
              text = "Current Status: ${currentStepData.title}",
              style = MaterialTheme.typography.titleSmall.copy(
                fontWeight = FontWeight.Bold,
                color = BrandGreenDark
              )
            )
            Text(
              text = when (currentStatus) {
                OrderStatus.PLACED, OrderStatus.CONFIRMED, OrderStatus.ACCEPTED -> "Shahpur store has received and queued your order."
                OrderStatus.PACKING, OrderStatus.PACKED -> "Fresh items have been carefully packed into your eco-friendly delivery bag."
                OrderStatus.OUT_FOR_DELIVERY -> "Rider is on the way through Shahpur Valley, arriving soon."
                OrderStatus.DELIVERED -> "Delivered safely! Thank you for ordering with ShahpurGo."
                OrderStatus.CANCELLED -> "Order was cancelled. Any pre-authorized charges will be refunded."
                else -> "Order received and being processed."
              },
              style = MaterialTheme.typography.bodySmall.copy(
                color = TextSecondarySlate,
                fontSize = 11.5.sp
              )
            )
          }
        }
      }
    }
  }
}
