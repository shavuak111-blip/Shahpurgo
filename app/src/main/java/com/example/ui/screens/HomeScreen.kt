package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.ElectricBolt
import androidx.compose.material.icons.filled.Landscape
import androidx.compose.material.icons.filled.LocalShipping
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.GroceryCategory
import com.example.model.GroceryItem
import com.example.model.Shop
import com.example.model.ShopApprovalStatus
import com.example.ui.components.GroceryItemCard
import com.example.ui.components.MountainSilhouettes
import com.example.ui.theme.BrandGreenAccent
import com.example.ui.theme.BrandGreenContainer
import com.example.ui.theme.BrandGreenDark
import com.example.ui.theme.BrandGreenLight
import com.example.ui.theme.BrandGreenPrimary
import com.example.ui.theme.BrandGreenSurface
import com.example.ui.theme.MountainBorder
import com.example.ui.theme.MountainWhite
import com.example.ui.theme.RewardGold
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimaryDark
import com.example.viewmodel.GroceryUiState

@Composable
fun HomeScreen(
  uiState: GroceryUiState,
  onCategorySelect: (GroceryCategory) -> Unit,
  onSearchChange: (String) -> Unit,
  onAddToCart: (GroceryItem) -> Unit,
  onDecrementCart: (GroceryItem) -> Unit,
  onViewCartClick: () -> Unit,
  onOpenLocationPicker: () -> Unit,
  onShopSelect: (Shop) -> Unit = {},
  modifier: Modifier = Modifier
) {
  val focusManager = LocalFocusManager.current

  Box(modifier = modifier.fillMaxSize()) {
    LazyVerticalGrid(
      columns = GridCells.Fixed(2),
      modifier = Modifier
        .fillMaxSize()
        .testTag("home_grocery_grid"),
      contentPadding = PaddingValues(
        start = 12.dp,
        end = 12.dp,
        top = 8.dp,
        bottom = if (uiState.cart.isNotEmpty()) 90.dp else 24.dp
      ),
      horizontalArrangement = Arrangement.spacedBy(10.dp),
      verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
      // 1. Search Bar
      item(span = { GridItemSpan(2) }) {
        OutlinedTextField(
          value = uiState.searchQuery,
          onValueChange = onSearchChange,
          modifier = Modifier
            .fillMaxWidth()
            .testTag("search_grocery_input"),
          placeholder = {
            Text(
              "Search Kinnaur apples, Kangra tea, milk...",
              style = MaterialTheme.typography.bodyMedium.copy(color = TextMuted)
            )
          },
          leadingIcon = {
            Icon(
              imageVector = Icons.Default.Search,
              contentDescription = "Search",
              tint = BrandGreenPrimary
            )
          },
          trailingIcon = {
            if (uiState.searchQuery.isNotEmpty()) {
              IconButton(onClick = { onSearchChange("") }) {
                Icon(
                  imageVector = Icons.Default.Close,
                  contentDescription = "Clear search",
                  tint = TextMuted
                )
              }
            }
          },
          shape = RoundedCornerShape(14.dp),
          colors = OutlinedTextFieldDefaults.colors(
            focusedContainerColor = MountainWhite,
            unfocusedContainerColor = MountainWhite,
            focusedBorderColor = BrandGreenPrimary,
            unfocusedBorderColor = MountainBorder
          ),
          singleLine = true,
          keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
          keyboardActions = KeyboardActions(onSearch = { focusManager.clearFocus() })
        )
      }

      if (!uiState.isStoreAcceptingOrders) {
        item(span = { GridItemSpan(2) }) {
          Card(
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFFFEF2F2)),
            border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFFCA5A5)),
            modifier = Modifier.fillMaxWidth()
          ) {
            Row(
              modifier = Modifier.padding(12.dp),
              verticalAlignment = Alignment.CenterVertically
            ) {
              Box(
                modifier = Modifier
                  .size(8.dp)
                  .background(Color(0xFFEF4444), CircleShape)
              )
              Spacer(modifier = Modifier.width(10.dp))
              Column {
                Text(
                  text = "Shahpur Hub is currently taking a short prep break for restocking.",
                  style = MaterialTheme.typography.bodySmall.copy(
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF991B1B)
                  )
                )
                Text(
                  text = "Live ordering will resume shortly. You can still browse and add to cart.",
                  style = MaterialTheme.typography.labelSmall.copy(color = Color(0xFFB91C1C))
                )
              }
            }
          }
        }
      }

      // 2. Himalayan Mountain Hero Banner
      item(span = { GridItemSpan(2) }) {
        Card(
          shape = RoundedCornerShape(16.dp),
          colors = CardDefaults.cardColors(containerColor = BrandGreenDark),
          modifier = Modifier
            .fillMaxWidth()
            .testTag("mountain_hero_banner")
        ) {
          Box(
            modifier = Modifier
              .fillMaxWidth()
              .background(
                Brush.horizontalGradient(
                  colors = listOf(BrandGreenDark, BrandGreenPrimary, Color(0xFF047857))
                )
              )
          ) {
            // Mountain backdrop
            MountainSilhouettes(
              modifier = Modifier
                .fillMaxWidth()
                .height(80.dp)
                .align(Alignment.BottomCenter),
              tint = Color.White.copy(alpha = 0.15f)
            )

            Column(
              modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
            ) {
              // Location Badge
              Row(verticalAlignment = Alignment.CenterVertically) {
                Surface(
                  shape = RoundedCornerShape(20.dp),
                  color = MountainWhite.copy(alpha = 0.2f)
                ) {
                  Row(
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically
                  ) {
                    Icon(
                      imageVector = Icons.Default.Landscape,
                      contentDescription = null,
                      tint = RewardGold,
                      modifier = Modifier.size(13.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                      text = "Shahpur • Kangra • Himachal Pradesh",
                      style = MaterialTheme.typography.labelSmall.copy(
                        color = MountainWhite,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 10.sp
                      )
                    )
                  }
                }
              }

              Spacer(modifier = Modifier.height(8.dp))

              Text(
                text = "Fresh Groceries at\nMountain Valley Speed",
                style = MaterialTheme.typography.headlineMedium.copy(
                  fontWeight = FontWeight.ExtraBold,
                  color = MountainWhite,
                  lineHeight = 24.sp
                )
              )

              Spacer(modifier = Modifier.height(6.dp))

              Text(
                text = "Local Kangra teas, farm-picked Himachal fruits & daily kitchen staples delivered in 15–20 minutes.",
                style = MaterialTheme.typography.bodyMedium.copy(
                  color = MountainWhite.copy(alpha = 0.9f),
                  fontSize = 12.sp
                )
              )

              Spacer(modifier = Modifier.height(12.dp))

              // Trust Badges
              Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
              ) {
                Surface(
                  shape = RoundedCornerShape(8.dp),
                  color = MountainWhite.copy(alpha = 0.18f)
                ) {
                  Row(
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically
                  ) {
                    Icon(
                      imageVector = Icons.Default.ElectricBolt,
                      contentDescription = null,
                      tint = RewardGold,
                      modifier = Modifier.size(14.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                      text = "15-20 Min Delivery",
                      style = MaterialTheme.typography.labelSmall.copy(
                        color = MountainWhite,
                        fontWeight = FontWeight.Bold
                      )
                    )
                  }
                }

                Surface(
                  shape = RoundedCornerShape(8.dp),
                  color = MountainWhite.copy(alpha = 0.18f)
                ) {
                  Row(
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically
                  ) {
                    Icon(
                      imageVector = Icons.Default.Verified,
                      contentDescription = null,
                      tint = BrandGreenAccent,
                      modifier = Modifier.size(14.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                      text = "Cash on Delivery",
                      style = MaterialTheme.typography.labelSmall.copy(
                        color = MountainWhite,
                        fontWeight = FontWeight.Bold
                      )
                    )
                  }
                }
              }
            }
          }
        }
      }

      // Multi-Vendor Local Shops & Kitchens Section
      item(span = { GridItemSpan(2) }) {
        Column(modifier = Modifier.padding(vertical = 4.dp)) {
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Text(
              text = "Local Shops & Kitchens",
              style = MaterialTheme.typography.titleMedium.copy(
                fontWeight = FontWeight.Bold,
                color = TextPrimaryDark
              )
            )
            Text(
              text = "${uiState.platformShops.size} nearby",
              style = MaterialTheme.typography.labelMedium.copy(
                color = BrandGreenDark,
                fontWeight = FontWeight.SemiBold
              )
            )
          }

          Spacer(modifier = Modifier.height(8.dp))

          LazyRow(
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            contentPadding = PaddingValues(horizontal = 2.dp)
          ) {
            items(uiState.platformShops) { shop ->
              Card(
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = MountainWhite),
                border = androidx.compose.foundation.BorderStroke(1.dp, MountainBorder),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                modifier = Modifier
                  .width(170.dp)
                  .clip(RoundedCornerShape(14.dp))
                  .clickable { onShopSelect(shop) }
                  .testTag("shop_card_${shop.id}")
              ) {
                Column(modifier = Modifier.padding(10.dp)) {
                  Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                  ) {
                    Text(text = shop.iconEmoji, fontSize = 24.sp)
                    Box(
                      modifier = Modifier
                        .clip(RoundedCornerShape(6.dp))
                        .background(if (shop.isOpen) BrandGreenSurface else Color(0xFFFEE2E2))
                        .padding(horizontal = 6.dp, vertical = 2.dp)
                    ) {
                      Text(
                        text = if (shop.isOpen) "OPEN" else "CLOSED",
                        fontSize = 9.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (shop.isOpen) BrandGreenDark else Color(0xFFDC2626)
                      )
                    }
                  }

                  Spacer(modifier = Modifier.height(6.dp))

                  Text(
                    text = shop.name,
                    style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold),
                    color = Color(0xFF1F2937),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                  )
                  Text(
                    text = shop.hindiName,
                    style = MaterialTheme.typography.labelSmall,
                    color = TextMuted,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                  )

                  Spacer(modifier = Modifier.height(6.dp))

                  Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                  ) {
                    Text(
                      text = "⭐ ${shop.rating}",
                      fontSize = 11.sp,
                      fontWeight = FontWeight.Bold,
                      color = Color(0xFFB45309)
                    )
                    Text(
                      text = shop.deliveryTimeEstimate,
                      fontSize = 11.sp,
                      color = TextMuted
                    )
                  }
                }
              }
            }
          }
        }
      }

      // 3. Category Horizontal Chips
      item(span = { GridItemSpan(2) }) {
        Column(modifier = Modifier.padding(vertical = 4.dp)) {
          Text(
            text = "Explore Categories",
            style = MaterialTheme.typography.titleMedium.copy(
              fontWeight = FontWeight.Bold,
              color = TextPrimaryDark
            ),
            modifier = Modifier.padding(bottom = 8.dp)
          )

          LazyRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            contentPadding = PaddingValues(horizontal = 2.dp)
          ) {
            items(GroceryCategory.values()) { cat ->
              val isSelected = uiState.selectedCategory == cat
              Surface(
                shape = RoundedCornerShape(12.dp),
                color = if (isSelected) BrandGreenPrimary else MountainWhite,
                border = if (isSelected) null else androidx.compose.foundation.BorderStroke(1.dp, MountainBorder),
                modifier = Modifier
                  .testTag("category_chip_${cat.name}")
                  .clip(RoundedCornerShape(12.dp))
                  .clickable { onCategorySelect(cat) }
              ) {
                Row(
                  modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                  verticalAlignment = Alignment.CenterVertically
                ) {
                  Text(
                    text = cat.iconEmoji,
                    fontSize = 14.sp
                  )
                  Spacer(modifier = Modifier.width(6.dp))
                  Text(
                    text = cat.displayName,
                    style = MaterialTheme.typography.labelMedium.copy(
                      color = if (isSelected) MountainWhite else TextPrimaryDark,
                      fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
                    )
                  )
                }
              }
            }
          }
        }
      }

      // 4. Section Heading
      item(span = { GridItemSpan(2) }) {
        Row(
          modifier = Modifier
            .fillMaxWidth()
            .padding(top = 8.dp, bottom = 4.dp),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Column {
            Text(
              text = if (uiState.selectedCategory == GroceryCategory.ALL) "Shahpur Store Specials" else uiState.selectedCategory.displayName,
              style = MaterialTheme.typography.titleLarge.copy(
                fontWeight = FontWeight.Bold,
                color = TextPrimaryDark
              )
            )
            Text(
              text = "${uiState.filteredCatalogue.size} items available right now",
              style = MaterialTheme.typography.labelSmall.copy(color = TextMuted)
            )
          }
        }
      }

      // 5. Grocery Products Grid
      items(
        items = uiState.filteredCatalogue,
        key = { it.id }
      ) { item ->
        val cartQty = uiState.cart.find { it.item.id == item.id }?.quantity ?: 0
        GroceryItemCard(
          item = item,
          cartQuantity = cartQty,
          onAddToCart = { onAddToCart(item) },
          onDecrement = { onDecrementCart(item) }
        )
      }
    }

    // Floating Cart Summary Pill at the Bottom
    AnimatedVisibility(
      visible = uiState.cart.isNotEmpty(),
      enter = fadeIn(),
      exit = fadeOut(),
      modifier = Modifier
        .align(Alignment.BottomCenter)
        .padding(16.dp)
    ) {
      Surface(
        shape = RoundedCornerShape(16.dp),
        color = BrandGreenPrimary,
        shadowElevation = 8.dp,
        modifier = Modifier
          .fillMaxWidth()
          .testTag("floating_cart_pill")
          .clip(RoundedCornerShape(16.dp))
          .clickable { onViewCartClick() }
      ) {
        Row(
          modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 12.dp),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
              modifier = Modifier
                .size(36.dp)
                .background(MountainWhite, CircleShape),
              contentAlignment = Alignment.Center
            ) {
              Icon(
                imageVector = Icons.Default.ShoppingBag,
                contentDescription = null,
                tint = BrandGreenPrimary,
                modifier = Modifier.size(20.dp)
              )
            }
            Spacer(modifier = Modifier.width(10.dp))
            Column {
              Text(
                text = "${uiState.cartItemCount} items • ₹${uiState.cartSubtotal.toInt()}",
                style = MaterialTheme.typography.titleMedium.copy(
                  color = MountainWhite,
                  fontWeight = FontWeight.Bold
                )
              )
              Text(
                text = if (uiState.deliveryFee == 0.0) "FREE Shahpur Delivery applied" else "Add ₹${(199 - uiState.cartSubtotal).toInt()} for FREE delivery",
                style = MaterialTheme.typography.labelSmall.copy(
                  color = RewardGold,
                  fontWeight = FontWeight.SemiBold
                )
              )
            }
          }

          Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
              text = "View Cart",
              style = MaterialTheme.typography.titleSmall.copy(
                color = MountainWhite,
                fontWeight = FontWeight.Bold
              )
            )
            Spacer(modifier = Modifier.width(4.dp))
            Icon(
              imageVector = Icons.Default.ArrowForward,
              contentDescription = "Go to cart",
              tint = MountainWhite,
              modifier = Modifier.size(18.dp)
            )
          }
        }
      }
    }
  }
}
