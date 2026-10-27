package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
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
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.GroceryItem
import com.example.model.ProductCategory
import com.example.model.Shop
import com.example.ui.theme.BrandGreenDark
import com.example.ui.theme.BrandGreenPrimary
import com.example.ui.theme.BrandGreenSurface
import com.example.ui.theme.MountainCardSurface
import com.example.ui.theme.MountainWhite
import com.example.ui.theme.RewardGold
import com.example.ui.theme.TextMuted
import com.example.ui.theme.WarningRed
import com.example.viewmodel.GroceryUiState

@Composable
fun ShopDetailScreen(
  shop: Shop,
  uiState: GroceryUiState,
  onAddToCart: (GroceryItem) -> Unit,
  onDecrementCart: (GroceryItem) -> Unit,
  onViewCart: () -> Unit,
  onBack: () -> Unit
) {
  var selectedCategoryFilter by remember { mutableStateOf("ALL") }

  // Filter catalogue and Firestore products for this shop
  val shopCatalogueItems = remember(shop.id, selectedCategoryFilter, uiState.catalogue) {
    uiState.catalogue.filter { item ->
      // If store is Central Hub or matching category
      val matchesCategory = selectedCategoryFilter == "ALL" ||
          item.category.name.equals(selectedCategoryFilter, ignoreCase = true)
      matchesCategory
    }
  }

  Scaffold(
    bottomBar = {
      AnimatedVisibility(visible = uiState.cart.isNotEmpty()) {
        Card(
          shape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp),
          colors = CardDefaults.cardColors(containerColor = BrandGreenDark),
          elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
          modifier = Modifier
            .fillMaxWidth()
            .testTag("shop_detail_cart_bar")
        ) {
          Row(
            modifier = Modifier
              .fillMaxWidth()
              .padding(horizontal = 20.dp, vertical = 14.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Column {
              Text(
                text = "${uiState.cartItemCount} items • ₹${uiState.cartSubtotal.toInt()}",
                color = MountainWhite,
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp
              )
              Text(
                text = "Plus delivery & taxes",
                color = MountainWhite.copy(alpha = 0.8f),
                fontSize = 12.sp
              )
            }

            Button(
              onClick = onViewCart,
              colors = ButtonDefaults.buttonColors(containerColor = BrandGreenPrimary),
              shape = RoundedCornerShape(12.dp),
              modifier = Modifier.testTag("shop_view_cart_btn")
            ) {
              Text("View Cart", fontWeight = FontWeight.Bold)
              Spacer(modifier = Modifier.width(6.dp))
              Icon(Icons.Default.ShoppingBag, contentDescription = null, modifier = Modifier.size(18.dp))
            }
          }
        }
      }
    }
  ) { padding ->
    LazyColumn(
      modifier = Modifier
        .fillMaxSize()
        .background(MountainCardSurface)
        .padding(padding)
        .testTag("shop_detail_scroll")
    ) {
      // Top Hero & Shop Identity
      item {
        Box(
          modifier = Modifier
            .fillMaxWidth()
            .background(
              Brush.verticalGradient(
                colors = listOf(BrandGreenDark, BrandGreenPrimary.copy(alpha = 0.85f))
              )
            )
            .padding(16.dp)
        ) {
          Column {
            Row(
              modifier = Modifier.fillMaxWidth(),
              verticalAlignment = Alignment.CenterVertically
            ) {
              IconButton(
                onClick = onBack,
                modifier = Modifier
                  .size(44.dp)
                  .clip(CircleShape)
                  .background(MountainWhite.copy(alpha = 0.2f))
                  .testTag("shop_detail_back_button")
              ) {
                Icon(
                  imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                  contentDescription = "Back",
                  tint = MountainWhite
                )
              }

              Spacer(modifier = Modifier.width(12.dp))

              Text(
                text = shop.name,
                color = MountainWhite,
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.weight(1f)
              )

              // Open / Closed badge
              Box(
                modifier = Modifier
                  .clip(RoundedCornerShape(8.dp))
                  .background(if (shop.isOpen) BrandGreenSurface else WarningRed.copy(alpha = 0.2f))
                  .padding(horizontal = 10.dp, vertical = 4.dp)
              ) {
                Text(
                  text = if (shop.isOpen) "OPEN" else "CLOSED",
                  color = if (shop.isOpen) BrandGreenDark else WarningRed,
                  fontWeight = FontWeight.Bold,
                  fontSize = 12.sp
                )
              }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Shop Card Profile
            Card(
              shape = RoundedCornerShape(18.dp),
              colors = CardDefaults.cardColors(containerColor = MountainWhite),
              elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
              modifier = Modifier.fillMaxWidth()
            ) {
              Column(modifier = Modifier.padding(16.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                  Text(text = shop.iconEmoji, fontSize = 36.sp)
                  Spacer(modifier = Modifier.width(14.dp))
                  Column(modifier = Modifier.weight(1f)) {
                    Text(
                      text = shop.name,
                      style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                      color = BrandGreenDark
                    )
                    Text(
                      text = shop.hindiName,
                      style = MaterialTheme.typography.bodySmall,
                      color = TextMuted
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                      Box(
                        modifier = Modifier
                          .clip(RoundedCornerShape(6.dp))
                          .background(BrandGreenDark)
                          .padding(horizontal = 6.dp, vertical = 2.dp)
                      ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                          Text(
                            text = "${shop.rating}",
                            color = MountainWhite,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                          )
                          Spacer(modifier = Modifier.width(2.dp))
                          Icon(
                            imageVector = Icons.Default.Star,
                            contentDescription = null,
                            tint = RewardGold,
                            modifier = Modifier.size(12.dp)
                          )
                        }
                      }

                      Spacer(modifier = Modifier.width(8.dp))
                      Text(
                        text = "• ${shop.deliveryTimeEstimate}",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color(0xFF4B5563)
                      )
                      Spacer(modifier = Modifier.width(8.dp))
                      Text(
                        text = "• Min ₹${shop.minimumOrderValue.toInt()}",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color(0xFF4B5563)
                      )
                    }
                  }
                }

                Spacer(modifier = Modifier.height(12.dp))
                Divider(color = Color(0xFFE5E7EB), thickness = 0.8.dp)
                Spacer(modifier = Modifier.height(10.dp))

                Row(verticalAlignment = Alignment.CenterVertically) {
                  Icon(
                    imageVector = Icons.Default.LocationOn,
                    contentDescription = null,
                    tint = BrandGreenPrimary,
                    modifier = Modifier.size(16.dp)
                  )
                  Spacer(modifier = Modifier.width(6.dp))
                  Text(
                    text = shop.address,
                    style = MaterialTheme.typography.bodySmall,
                    color = Color(0xFF4B5563),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.weight(1f)
                  )
                }

                Spacer(modifier = Modifier.height(6.dp))

                Row(verticalAlignment = Alignment.CenterVertically) {
                  Icon(
                    imageVector = Icons.Default.AccessTime,
                    contentDescription = null,
                    tint = BrandGreenPrimary,
                    modifier = Modifier.size(16.dp)
                  )
                  Spacer(modifier = Modifier.width(6.dp))
                  Text(
                    text = "Hours: ${shop.openingTime} - ${shop.closingTime}",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color(0xFF4B5563)
                  )

                  Spacer(modifier = Modifier.weight(1f))

                  Icon(
                    imageVector = Icons.Default.Phone,
                    contentDescription = null,
                    tint = BrandGreenPrimary,
                    modifier = Modifier.size(16.dp)
                  )
                  Spacer(modifier = Modifier.width(4.dp))
                  Text(
                    text = shop.phone,
                    style = MaterialTheme.typography.bodySmall,
                    color = Color(0xFF4B5563)
                  )
                }

                if (!shop.isOpen) {
                  Spacer(modifier = Modifier.height(10.dp))
                  Box(
                    modifier = Modifier
                      .fillMaxWidth()
                      .clip(RoundedCornerShape(8.dp))
                      .background(WarningRed.copy(alpha = 0.12f))
                      .padding(8.dp)
                  ) {
                    Text(
                      text = "⚠️ This store is currently closed. You may browse items, but ordering is paused until opening hours.",
                      color = WarningRed,
                      fontSize = 12.sp,
                      fontWeight = FontWeight.Medium
                    )
                  }
                }
              }
            }
          }
        }
      }

      // Category filter chips
      item {
        Spacer(modifier = Modifier.height(14.dp))
        Text(
          text = "Store Menu & Catalog",
          style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
          color = BrandGreenDark,
          modifier = Modifier.padding(horizontal = 16.dp)
        )
        Spacer(modifier = Modifier.height(8.dp))

        LazyRow(
          contentPadding = PaddingValues(horizontal = 16.dp),
          horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
          item {
            FilterChip(
              selected = selectedCategoryFilter == "ALL",
              onClick = { selectedCategoryFilter = "ALL" },
              label = { Text("All Items") },
              colors = FilterChipDefaults.filterChipColors(
                selectedContainerColor = BrandGreenPrimary,
                selectedLabelColor = MountainWhite
              )
            )
          }

          items(ProductCategory.entries.filter { it != ProductCategory.ALL }.take(8)) { cat ->
            FilterChip(
              selected = selectedCategoryFilter == cat.name,
              onClick = { selectedCategoryFilter = cat.name },
              label = { Text("${cat.iconEmoji} ${cat.displayName.substringBefore(" &")}") },
              colors = FilterChipDefaults.filterChipColors(
                selectedContainerColor = BrandGreenPrimary,
                selectedLabelColor = MountainWhite
              )
            )
          }
        }
        Spacer(modifier = Modifier.height(12.dp))
      }

      // Products List
      if (shopCatalogueItems.isEmpty()) {
        item {
          Box(
            modifier = Modifier
              .fillMaxWidth()
              .padding(40.dp),
            contentAlignment = Alignment.Center
          ) {
            Text(
              text = "No items found in this section.",
              color = TextMuted,
              style = MaterialTheme.typography.bodyMedium
            )
          }
        }
      } else {
        items(shopCatalogueItems, key = { it.id }) { item ->
          val inCart = uiState.cart.find { it.item.id == item.id }
          val qty = inCart?.quantity ?: 0

          Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = MountainWhite),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
            modifier = Modifier
              .fillMaxWidth()
              .padding(horizontal = 16.dp, vertical = 6.dp)
              .testTag("shop_product_card_${item.id}")
          ) {
            Row(
              modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
              verticalAlignment = Alignment.CenterVertically
            ) {
              // Icon or emoji avatar
              Box(
                modifier = Modifier
                  .size(64.dp)
                  .clip(RoundedCornerShape(14.dp))
                  .background(BrandGreenSurface),
                contentAlignment = Alignment.Center
              ) {
                Text(
                  text = when (item.category.name) {
                    "FRESH_PRODUCE" -> "🍎"
                    "KANGRA_SPECIALS" -> "☕"
                    "DAIRY_BAKERY" -> "🥛"
                    "PANTRY_STAPLES" -> "🌾"
                    else -> "🛒"
                  },
                  fontSize = 30.sp
                )
              }

              Spacer(modifier = Modifier.width(14.dp))

              Column(modifier = Modifier.weight(1f)) {
                Text(
                  text = item.name,
                  style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold),
                  color = Color(0xFF1F2937),
                  maxLines = 1,
                  overflow = TextOverflow.Ellipsis
                )
                Text(
                  text = item.hindiName,
                  style = MaterialTheme.typography.bodySmall,
                  color = TextMuted
                )
                Text(
                  text = item.unit,
                  style = MaterialTheme.typography.labelSmall,
                  color = Color(0xFF6B7280)
                )

                Spacer(modifier = Modifier.height(4.dp))

                Row(verticalAlignment = Alignment.CenterVertically) {
                  Text(
                    text = "₹${item.price.toInt()}",
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    color = BrandGreenDark
                  )
                  if (item.originalPrice > item.price) {
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                      text = "₹${item.originalPrice.toInt()}",
                      style = MaterialTheme.typography.bodySmall.copy(
                        textDecoration = TextDecoration.LineThrough
                      ),
                      color = TextMuted
                    )
                  }
                }
              }

              // Add / Quantity controls
              if (qty == 0) {
                Button(
                  onClick = { onAddToCart(item) },
                  enabled = shop.isOpen && item.inStock,
                  colors = ButtonDefaults.buttonColors(
                    containerColor = BrandGreenPrimary,
                    disabledContainerColor = Color(0xFFE5E7EB)
                  ),
                  shape = RoundedCornerShape(10.dp),
                  contentPadding = PaddingValues(horizontal = 16.dp, vertical = 6.dp),
                  modifier = Modifier.testTag("shop_add_btn_${item.id}")
                ) {
                  Text(
                    text = if (item.inStock) "ADD" else "Out of Stock",
                    fontWeight = FontWeight.Bold,
                    fontSize = 13.sp,
                    color = if (item.inStock) MountainWhite else TextMuted
                  )
                }
              } else {
                Row(
                  verticalAlignment = Alignment.CenterVertically,
                  modifier = Modifier
                    .clip(RoundedCornerShape(10.dp))
                    .background(BrandGreenSurface)
                    .padding(horizontal = 4.dp, vertical = 2.dp)
                ) {
                  IconButton(
                    onClick = { onDecrementCart(item) },
                    modifier = Modifier.size(32.dp).testTag("shop_decrement_btn_${item.id}")
                  ) {
                    Icon(
                      imageVector = Icons.Default.Remove,
                      contentDescription = "Remove",
                      tint = BrandGreenDark,
                      modifier = Modifier.size(16.dp)
                    )
                  }

                  Text(
                    text = "$qty",
                    fontWeight = FontWeight.Bold,
                    color = BrandGreenDark,
                    fontSize = 14.sp,
                    modifier = Modifier.padding(horizontal = 8.dp)
                  )

                  IconButton(
                    onClick = { onAddToCart(item) },
                    modifier = Modifier.size(32.dp).testTag("shop_increment_btn_${item.id}")
                  ) {
                    Icon(
                      imageVector = Icons.Default.Add,
                      contentDescription = "Add",
                      tint = BrandGreenDark,
                      modifier = Modifier.size(16.dp)
                    )
                  }
                }
              }
            }
          }
        }
      }

      item {
        Spacer(modifier = Modifier.height(80.dp))
      }
    }
  }
}
