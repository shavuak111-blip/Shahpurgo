package com.example.ui.components

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
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Eco
import androidx.compose.material.icons.filled.LocalDrink
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material.icons.filled.Spa
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.GroceryItem
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

@Composable
fun GroceryItemCard(
  item: GroceryItem,
  cartQuantity: Int,
  onAddToCart: () -> Unit,
  onDecrement: () -> Unit,
  modifier: Modifier = Modifier
) {
  Card(
    modifier = modifier
      .testTag("grocery_item_${item.id}")
      .border(1.dp, MountainBorder, RoundedCornerShape(14.dp)),
    colors = CardDefaults.cardColors(containerColor = MountainCardSurface),
    shape = RoundedCornerShape(14.dp),
    elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
  ) {
    Column(
      modifier = Modifier
        .fillMaxWidth()
        .padding(10.dp)
    ) {
      // Product Visual Box with Badge
      Box(
        modifier = Modifier
          .fillMaxWidth()
          .height(100.dp)
          .clip(RoundedCornerShape(10.dp))
          .background(
            Brush.linearGradient(
              colors = listOf(BrandGreenSurface, Color(0xFFF1F5F9))
            )
          ),
        contentAlignment = Alignment.Center
      ) {
        // Subtle background mountain silhouette hint
        MountainSilhouettes(
          modifier = Modifier
            .fillMaxWidth()
            .height(30.dp)
            .align(Alignment.BottomCenter),
          tint = BrandGreenPrimary.copy(alpha = 0.08f)
        )

        // Item Icon Visual Representation
        val iconVector: ImageVector = when (item.iconSymbol) {
          "tea" -> Icons.Default.Spa
          "milk", "juice" -> Icons.Default.LocalDrink
          "veggie", "herb", "apple" -> Icons.Default.Eco
          else -> Icons.Default.ShoppingBag
        }

        Box(
          modifier = Modifier
            .size(54.dp)
            .background(MountainWhite, CircleShape)
            .border(1.dp, BrandGreenContainer, CircleShape),
          contentAlignment = Alignment.Center
        ) {
          Icon(
            imageVector = iconVector,
            contentDescription = item.name,
            tint = BrandGreenPrimary,
            modifier = Modifier.size(30.dp)
          )
        }

        // Top Badge (e.g. "Kangra Fresh", "GI Tagged", "Himachal Crisp")
        item.badge?.let { badgeText ->
          Surface(
            shape = RoundedCornerShape(bottomEnd = 8.dp),
            color = BrandGreenPrimary,
            modifier = Modifier.align(Alignment.TopStart)
          ) {
            Text(
              text = badgeText,
              style = MaterialTheme.typography.labelSmall.copy(
                fontSize = 9.sp,
                fontWeight = FontWeight.Bold,
                color = MountainWhite
              ),
              modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
            )
          }
        }

        // Rating Star
        Surface(
          shape = RoundedCornerShape(bottomStart = 8.dp),
          color = MountainWhite.copy(alpha = 0.95f),
          modifier = Modifier.align(Alignment.TopEnd)
        ) {
          Row(
            modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp),
            verticalAlignment = Alignment.CenterVertically
          ) {
            Icon(
              imageVector = Icons.Default.Star,
              contentDescription = null,
              tint = RewardGold,
              modifier = Modifier.size(11.dp)
            )
            Spacer(modifier = Modifier.width(2.dp))
            Text(
              text = "${item.rating}",
              style = MaterialTheme.typography.labelSmall.copy(
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold
              )
            )
          }
        }
      }

      Spacer(modifier = Modifier.height(8.dp))

      // Unit
      Text(
        text = item.unit,
        style = MaterialTheme.typography.labelSmall.copy(
          color = TextMuted,
          fontWeight = FontWeight.Medium
        )
      )

      // Name
      Text(
        text = item.name,
        style = MaterialTheme.typography.titleMedium.copy(
          fontWeight = FontWeight.SemiBold,
          fontSize = 13.sp,
          lineHeight = 16.sp
        ),
        maxLines = 2,
        overflow = TextOverflow.Ellipsis,
        modifier = Modifier.height(34.dp)
      )

      // Hindi Name
      Text(
        text = item.hindiName,
        style = MaterialTheme.typography.labelSmall.copy(
          color = BrandGreenLight,
          fontSize = 10.sp
        ),
        maxLines = 1,
        overflow = TextOverflow.Ellipsis
      )

      Spacer(modifier = Modifier.height(6.dp))

      // Price & Add to Cart action row
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Column {
          Text(
            text = "₹${item.price.toInt()}",
            style = MaterialTheme.typography.titleMedium.copy(
              fontWeight = FontWeight.Bold,
              color = TextPrimaryDark,
              fontSize = 15.sp
            )
          )
          if (item.originalPrice > item.price) {
            Text(
              text = "₹${item.originalPrice.toInt()}",
              style = MaterialTheme.typography.labelSmall.copy(
                color = TextMuted,
                textDecoration = TextDecoration.LineThrough,
                fontSize = 10.sp
              )
            )
          }
        }

        if (!item.inStock || item.stockQuantity == 0) {
          Surface(
            shape = RoundedCornerShape(8.dp),
            color = Color(0xFFFEE2E2),
            modifier = Modifier.padding(vertical = 4.dp)
          ) {
            Text(
              text = "Out of Stock",
              style = MaterialTheme.typography.labelSmall.copy(
                color = Color(0xFFDC2626),
                fontWeight = FontWeight.Bold,
                fontSize = 10.sp
              ),
              modifier = Modifier.padding(horizontal = 6.dp, vertical = 4.dp)
            )
          }
        } else if (cartQuantity == 0) {
          // Clean M3 "+ ADD" button with green styling
          Surface(
            shape = RoundedCornerShape(8.dp),
            color = BrandGreenSurface,
            modifier = Modifier
              .testTag("add_item_${item.id}_button")
              .border(1.dp, BrandGreenPrimary, RoundedCornerShape(8.dp))
              .clip(RoundedCornerShape(8.dp))
              .clickable { onAddToCart() }
          ) {
            Row(
              modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
              verticalAlignment = Alignment.CenterVertically
            ) {
              Text(
                text = "ADD",
                style = MaterialTheme.typography.labelSmall.copy(
                  color = BrandGreenPrimary,
                  fontWeight = FontWeight.ExtraBold,
                  letterSpacing = 0.5.sp
                )
              )
            }
          }
        } else {
          // Counter stepper
          Surface(
            shape = RoundedCornerShape(8.dp),
            color = BrandGreenPrimary,
            modifier = Modifier.testTag("counter_${item.id}")
          ) {
            Row(
              verticalAlignment = Alignment.CenterVertically,
              modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp)
            ) {
              Box(
                modifier = Modifier
                  .size(24.dp)
                  .clickable { onDecrement() },
                contentAlignment = Alignment.Center
              ) {
                Icon(
                  imageVector = Icons.Default.Remove,
                  contentDescription = "Decrease quantity",
                  tint = MountainWhite,
                  modifier = Modifier.size(14.dp)
                )
              }

              Text(
                text = "$cartQuantity",
                style = MaterialTheme.typography.labelMedium.copy(
                  color = MountainWhite,
                  fontWeight = FontWeight.Bold
                ),
                modifier = Modifier.padding(horizontal = 6.dp)
              )

              Box(
                modifier = Modifier
                  .size(24.dp)
                  .clickable { onAddToCart() },
                contentAlignment = Alignment.Center
              ) {
                Icon(
                  imageVector = Icons.Default.Add,
                  contentDescription = "Increase quantity",
                  tint = MountainWhite,
                  modifier = Modifier.size(14.dp)
                )
              }
            }
          }
        }
      }
    }
  }
}
