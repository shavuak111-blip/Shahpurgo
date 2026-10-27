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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.DeliveryDining
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Payments
import androidx.compose.material.icons.filled.QrCode
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.material.icons.filled.RadioButtonUnchecked
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.material.icons.filled.LocalOffer
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
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
import com.example.ui.theme.MountainSnowBackground
import com.example.ui.theme.MountainWhite
import com.example.ui.theme.RewardGold
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimaryDark
import com.example.ui.theme.TextSecondarySlate
import com.example.viewmodel.GroceryUiState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CartCheckoutScreen(
  uiState: GroceryUiState,
  onBack: () -> Unit,
  onAddToCart: (GroceryItem) -> Unit,
  onDecrementCart: (GroceryItem) -> Unit,
  onSelectTip: (Double) -> Unit,
  onSelectPaymentMethod: (String) -> Unit,
  onPlaceOrder: () -> Unit,
  onChangeAddressClick: () -> Unit,
  onApplyCoupon: ((String) -> Boolean)? = null,
  onRemoveCoupon: (() -> Unit)? = null,
  onVerifyOnlinePayment: ((String, (Boolean) -> Unit) -> Unit)? = null,
  modifier: Modifier = Modifier
) {
  Scaffold(
    topBar = {
      TopAppBar(
        title = {
          Column {
            Text(
              text = "ShahpurGo Cart & Checkout",
              style = MaterialTheme.typography.titleMedium.copy(
                fontWeight = FontWeight.Bold,
                color = MountainWhite
              )
            )
            Text(
              text = "Estimated Delivery in ${uiState.estimatedDeliveryInfo.rangeString} • Shahpur",
              style = MaterialTheme.typography.labelSmall.copy(
                color = MountainWhite.copy(alpha = 0.85f)
              )
            )
          }
        },
        navigationIcon = {
          IconButton(onClick = onBack, modifier = Modifier.testTag("cart_back_button")) {
            Icon(
              imageVector = Icons.Default.ArrowBack,
              contentDescription = "Back",
              tint = MountainWhite
            )
          }
        },
        colors = TopAppBarDefaults.topAppBarColors(
          containerColor = BrandGreenPrimary
        )
      )
    },
    bottomBar = {
      if (uiState.cart.isNotEmpty()) {
        Surface(
          shadowElevation = 12.dp,
          color = MountainCardSurface,
          modifier = Modifier.fillMaxWidth()
        ) {
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
              Column {
                Text(
                  text = "TOTAL AMOUNT",
                  style = MaterialTheme.typography.labelSmall.copy(color = TextMuted)
                )
                Text(
                  text = "₹${uiState.grandTotal.toInt()}",
                  style = MaterialTheme.typography.headlineMedium.copy(
                    fontWeight = FontWeight.ExtraBold,
                    color = BrandGreenDark
                  )
                )
                Row(verticalAlignment = Alignment.CenterVertically) {
                  Box(
                    modifier = Modifier
                      .size(6.dp)
                      .background(BrandGreenLight, CircleShape)
                  )
                  Spacer(modifier = Modifier.width(4.dp))
                  Text(
                    text = "ETA: ${uiState.estimatedDeliveryInfo.rangeString}",
                    style = MaterialTheme.typography.labelSmall.copy(
                      color = BrandGreenPrimary,
                      fontWeight = FontWeight.Bold,
                      fontSize = 11.sp
                    )
                  )
                }
              }

              Button(
                onClick = onPlaceOrder,
                modifier = Modifier
                  .testTag("place_order_button")
                  .height(50.dp),
                colors = ButtonDefaults.buttonColors(
                  containerColor = BrandGreenPrimary,
                  contentColor = MountainWhite
                ),
                shape = RoundedCornerShape(12.dp)
              ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                  Text(
                    text = "Place Order (${uiState.selectedPaymentMethod.substringBefore(" ")})",
                    style = MaterialTheme.typography.titleMedium.copy(
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
  ) { paddingValues ->
    if (uiState.cart.isEmpty()) {
      Box(
        modifier = modifier
          .fillMaxSize()
          .padding(paddingValues)
          .background(MountainSnowBackground),
        contentAlignment = Alignment.Center
      ) {
        Column(
          horizontalAlignment = Alignment.CenterHorizontally,
          modifier = Modifier.padding(24.dp)
        ) {
          Box(
            modifier = Modifier
              .size(80.dp)
              .background(BrandGreenSurface, CircleShape),
            contentAlignment = Alignment.Center
          ) {
            Icon(
              imageVector = Icons.Default.ShoppingBag,
              contentDescription = null,
              tint = BrandGreenPrimary,
              modifier = Modifier.size(44.dp)
            )
          }
          Spacer(modifier = Modifier.height(16.dp))
          Text(
            text = "Your ShahpurGo cart is empty",
            style = MaterialTheme.typography.titleLarge.copy(
              fontWeight = FontWeight.Bold,
              color = TextPrimaryDark
            )
          )
          Spacer(modifier = Modifier.height(8.dp))
          Text(
            text = "Add fresh Himachal fruits, organic veggies & local Kangra tea to begin!",
            style = MaterialTheme.typography.bodyMedium.copy(color = TextMuted),
            textAlign = androidx.compose.ui.text.style.TextAlign.Center
          )
          Spacer(modifier = Modifier.height(20.dp))
          Button(
            onClick = onBack,
            colors = ButtonDefaults.buttonColors(containerColor = BrandGreenPrimary)
          ) {
            Text("Browse Fresh Groceries")
          }
        }
      }
    } else {
      LazyColumn(
        modifier = modifier
          .fillMaxSize()
          .padding(paddingValues)
          .background(MountainSnowBackground)
          .padding(horizontal = 14.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
      ) {
        item { Spacer(modifier = Modifier.height(6.dp)) }

        // 1. Estimated Delivery Time Dynamic Range Card
        item {
          val eta = uiState.estimatedDeliveryInfo
          Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = BrandGreenSurface),
            border = androidx.compose.foundation.BorderStroke(1.5.dp, BrandGreenContainer),
            modifier = Modifier
              .fillMaxWidth()
              .testTag("estimated_delivery_time_card")
          ) {
            Column(modifier = Modifier.padding(14.dp)) {
              Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
              ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                  Box(
                    modifier = Modifier
                      .size(38.dp)
                      .background(BrandGreenPrimary, CircleShape),
                    contentAlignment = Alignment.Center
                  ) {
                    Icon(
                      imageVector = Icons.Default.AccessTime,
                      contentDescription = "Estimated Delivery Time",
                      tint = MountainWhite,
                      modifier = Modifier.size(20.dp)
                    )
                  }
                  Spacer(modifier = Modifier.width(10.dp))
                  Column {
                    Text(
                      text = "Estimated Delivery Time",
                      style = MaterialTheme.typography.labelSmall.copy(
                        color = BrandGreenDark,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 0.5.sp
                      )
                    )
                    Text(
                      text = eta.rangeString,
                      style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.ExtraBold,
                        color = BrandGreenDark
                      ),
                      modifier = Modifier.testTag("estimated_delivery_range_label")
                    )
                  }
                }

                Surface(
                  shape = RoundedCornerShape(8.dp),
                  color = if (eta.isRushHour) Color(0xFFFEF3C7) else BrandGreenContainer
                ) {
                  Text(
                    text = if (eta.isRushHour) "Peak Hours" else "Express Window",
                    style = MaterialTheme.typography.labelSmall.copy(
                      color = if (eta.isRushHour) Color(0xFF92400E) else BrandGreenDark,
                      fontWeight = FontWeight.Bold,
                      fontSize = 10.sp
                    ),
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                  )
                }
              }

              Spacer(modifier = Modifier.height(8.dp))

              Text(
                text = eta.timeWindowString,
                style = MaterialTheme.typography.bodyMedium.copy(
                  fontWeight = FontWeight.SemiBold,
                  color = TextPrimaryDark
                ),
                modifier = Modifier.testTag("estimated_delivery_window_text")
              )

              Spacer(modifier = Modifier.height(10.dp))

              // Dynamic Calculation Breakdown Tags based on order size and time of day
              Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
              ) {
                Surface(
                  shape = RoundedCornerShape(8.dp),
                  color = MountainWhite,
                  border = androidx.compose.foundation.BorderStroke(1.dp, BrandGreenContainer),
                  modifier = Modifier.weight(1f)
                ) {
                  Row(
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically
                  ) {
                    Icon(
                      imageVector = Icons.Default.ShoppingBag,
                      contentDescription = null,
                      tint = BrandGreenPrimary,
                      modifier = Modifier.size(13.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                      text = eta.orderSizeContext,
                      style = MaterialTheme.typography.labelSmall.copy(
                        color = TextPrimaryDark,
                        fontWeight = FontWeight.Medium,
                        fontSize = 10.sp
                      ),
                      maxLines = 1,
                      overflow = TextOverflow.Ellipsis
                    )
                  }
                }

                Surface(
                  shape = RoundedCornerShape(8.dp),
                  color = MountainWhite,
                  border = androidx.compose.foundation.BorderStroke(1.dp, BrandGreenContainer),
                  modifier = Modifier.weight(1f)
                ) {
                  Row(
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically
                  ) {
                    Icon(
                      imageVector = Icons.Default.DeliveryDining,
                      contentDescription = null,
                      tint = if (eta.isRushHour) RewardGold else BrandGreenPrimary,
                      modifier = Modifier.size(14.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                      text = eta.trafficContext,
                      style = MaterialTheme.typography.labelSmall.copy(
                        color = TextPrimaryDark,
                        fontWeight = FontWeight.Medium,
                        fontSize = 10.sp
                      ),
                      maxLines = 1,
                      overflow = TextOverflow.Ellipsis
                    )
                  }
                }
              }
            }
          }
        }

        // 2. Delivery Address Card
        item {
          Card(
            shape = RoundedCornerShape(14.dp),
            colors = CardDefaults.cardColors(containerColor = MountainCardSurface),
            border = androidx.compose.foundation.BorderStroke(1.dp, MountainBorder),
            modifier = Modifier.fillMaxWidth().testTag("cart_delivery_address_card")
          ) {
            Row(
              modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
              verticalAlignment = Alignment.CenterVertically,
              horizontalArrangement = Arrangement.SpaceBetween
            ) {
              Row(
                modifier = Modifier.weight(1f),
                verticalAlignment = Alignment.Top
              ) {
                Box(
                  modifier = Modifier
                    .size(36.dp)
                    .background(BrandGreenContainer, CircleShape),
                  contentAlignment = Alignment.Center
                ) {
                  Icon(
                    imageVector = Icons.Default.LocationOn,
                    contentDescription = null,
                    tint = BrandGreenDark,
                    modifier = Modifier.size(20.dp)
                  )
                }
                Spacer(modifier = Modifier.width(10.dp))
                Column {
                  Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                      text = "Deliver to ${uiState.currentDeliveryLocation.tag}",
                      style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = TextPrimaryDark
                      )
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Surface(
                      shape = RoundedCornerShape(6.dp),
                      color = BrandGreenSurface
                    ) {
                      Text(
                        text = uiState.estimatedDeliveryInfo.rangeString,
                        style = MaterialTheme.typography.labelSmall.copy(
                          color = BrandGreenPrimary,
                          fontWeight = FontWeight.Bold,
                          fontSize = 10.sp
                        ),
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                      )
                    }
                  }
                  Text(
                    text = uiState.currentDeliveryLocation.fullAddress,
                    style = MaterialTheme.typography.bodySmall.copy(color = TextSecondarySlate),
                    maxLines = 2
                  )
                  Text(
                    text = "Landmark: ${uiState.currentDeliveryLocation.landmark}",
                    style = MaterialTheme.typography.labelSmall.copy(color = TextMuted)
                  )
                }
              }

              Surface(
                shape = RoundedCornerShape(8.dp),
                color = BrandGreenSurface,
                modifier = Modifier
                  .clickable { onChangeAddressClick() }
                  .testTag("change_address_cart_btn")
              ) {
                Text(
                  text = "Change",
                  style = MaterialTheme.typography.labelMedium.copy(
                    color = BrandGreenPrimary,
                    fontWeight = FontWeight.Bold
                  ),
                  modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                )
              }
            }
          }
        }

        // 2. Cart Items List Card
        item {
          Card(
            shape = RoundedCornerShape(14.dp),
            colors = CardDefaults.cardColors(containerColor = MountainCardSurface),
            border = androidx.compose.foundation.BorderStroke(1.dp, MountainBorder),
            modifier = Modifier.fillMaxWidth()
          ) {
            Column(modifier = Modifier.padding(14.dp)) {
              Text(
                text = "Review Order Items (${uiState.cartItemCount})",
                style = MaterialTheme.typography.titleMedium.copy(
                  fontWeight = FontWeight.Bold,
                  color = TextPrimaryDark
                )
              )

              Spacer(modifier = Modifier.height(10.dp))

              uiState.cart.forEachIndexed { index, cartItem ->
                Row(
                  modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 6.dp),
                  horizontalArrangement = Arrangement.SpaceBetween,
                  verticalAlignment = Alignment.CenterVertically
                ) {
                  Column(modifier = Modifier.weight(1f)) {
                    Text(
                      text = cartItem.item.name,
                      style = MaterialTheme.typography.titleSmall.copy(
                        fontWeight = FontWeight.SemiBold,
                        color = TextPrimaryDark
                      )
                    )
                    Text(
                      text = "${cartItem.item.unit} • ₹${cartItem.item.price.toInt()} each",
                      style = MaterialTheme.typography.labelSmall.copy(color = TextMuted)
                    )
                  }

                  Row(verticalAlignment = Alignment.CenterVertically) {
                    // Counter
                    Surface(
                      shape = RoundedCornerShape(8.dp),
                      color = BrandGreenSurface,
                      border = androidx.compose.foundation.BorderStroke(1.dp, BrandGreenPrimary)
                    ) {
                      Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp)
                      ) {
                        Box(
                          modifier = Modifier
                            .size(24.dp)
                            .clickable { onDecrementCart(cartItem.item) },
                          contentAlignment = Alignment.Center
                        ) {
                          Icon(
                            imageVector = Icons.Default.Remove,
                            contentDescription = "Minus",
                            tint = BrandGreenPrimary,
                            modifier = Modifier.size(14.dp)
                          )
                        }

                        Text(
                          text = "${cartItem.quantity}",
                          style = MaterialTheme.typography.labelMedium.copy(
                            color = BrandGreenPrimary,
                            fontWeight = FontWeight.Bold
                          ),
                          modifier = Modifier.padding(horizontal = 6.dp)
                        )

                        Box(
                          modifier = Modifier
                            .size(24.dp)
                            .clickable { onAddToCart(cartItem.item) },
                          contentAlignment = Alignment.Center
                        ) {
                          Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = "Plus",
                            tint = BrandGreenPrimary,
                            modifier = Modifier.size(14.dp)
                          )
                        }
                      }
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    Text(
                      text = "₹${(cartItem.item.price * cartItem.quantity).toInt()}",
                      style = MaterialTheme.typography.titleSmall.copy(
                        fontWeight = FontWeight.Bold,
                        color = TextPrimaryDark
                      )
                    )
                  }
                }

                if (index < uiState.cart.size - 1) {
                  HorizontalDivider(
                    color = MountainBorder.copy(alpha = 0.6f),
                    modifier = Modifier.padding(vertical = 4.dp)
                  )
                }
              }
            }
          }
        }

        // 3. Delivery Partner Tip Selector
        item {
          Card(
            shape = RoundedCornerShape(14.dp),
            colors = CardDefaults.cardColors(containerColor = MountainCardSurface),
            border = androidx.compose.foundation.BorderStroke(1.dp, MountainBorder),
            modifier = Modifier.fillMaxWidth().testTag("delivery_tip_card")
          ) {
            Column(modifier = Modifier.padding(14.dp)) {
              Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                  imageVector = Icons.Default.Favorite,
                  contentDescription = null,
                  tint = RewardGold,
                  modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                  text = "Tip Your Shahpur Delivery Partner",
                  style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold,
                    color = TextPrimaryDark
                  )
                )
              }

              Text(
                text = "100% of your tip goes directly to the local rider navigating Kangra roads.",
                style = MaterialTheme.typography.bodySmall.copy(color = TextMuted),
                modifier = Modifier.padding(vertical = 4.dp)
              )

              Spacer(modifier = Modifier.height(8.dp))

              Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
              ) {
                val tipOptions = listOf(0.0, 10.0, 20.0, 30.0, 50.0)
                tipOptions.forEach { tip ->
                  val isSelected = uiState.selectedTip == tip
                  Surface(
                    shape = RoundedCornerShape(10.dp),
                    color = if (isSelected) BrandGreenPrimary else BrandGreenSurface,
                    border = if (isSelected) null else androidx.compose.foundation.BorderStroke(1.dp, BrandGreenContainer),
                    modifier = Modifier
                      .weight(1f)
                      .testTag("tip_option_${tip.toInt()}")
                      .clip(RoundedCornerShape(10.dp))
                      .clickable { onSelectTip(tip) }
                  ) {
                    Column(
                      modifier = Modifier.padding(vertical = 8.dp),
                      horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                      Text(
                        text = if (tip == 0.0) "None" else "₹${tip.toInt()}",
                        style = MaterialTheme.typography.labelLarge.copy(
                          fontWeight = FontWeight.Bold,
                          color = if (isSelected) MountainWhite else BrandGreenDark
                        )
                      )
                      if (tip == 20.0) {
                        Text(
                          text = "Most Popular",
                          style = MaterialTheme.typography.labelSmall.copy(
                            fontSize = 8.sp,
                            color = if (isSelected) RewardGold else BrandGreenPrimary
                          )
                        )
                      }
                    }
                  }
                }
              }
            }
          }
        }

        // 4. Payment Method Selector (Cash on Delivery + UPI)
        item {
          Card(
            shape = RoundedCornerShape(14.dp),
            colors = CardDefaults.cardColors(containerColor = MountainCardSurface),
            border = androidx.compose.foundation.BorderStroke(1.dp, MountainBorder),
            modifier = Modifier.fillMaxWidth().testTag("payment_method_card")
          ) {
            Column(modifier = Modifier.padding(14.dp)) {
              Text(
                text = "Select Payment Method",
                style = MaterialTheme.typography.titleMedium.copy(
                  fontWeight = FontWeight.Bold,
                  color = TextPrimaryDark
                )
              )

              Spacer(modifier = Modifier.height(8.dp))

              // Option 1: Cash on Delivery (COD)
              val isCod = uiState.selectedPaymentMethod.startsWith("Cash on Delivery")
              Surface(
                shape = RoundedCornerShape(10.dp),
                color = if (isCod) BrandGreenSurface else MountainWhite,
                border = androidx.compose.foundation.BorderStroke(
                  1.dp,
                  if (isCod) BrandGreenPrimary else MountainBorder
                ),
                modifier = Modifier
                  .fillMaxWidth()
                  .testTag("payment_cod_option")
                  .clickable { onSelectPaymentMethod("Cash on Delivery (COD)") }
              ) {
                Row(
                  modifier = Modifier.padding(12.dp),
                  verticalAlignment = Alignment.CenterVertically,
                  horizontalArrangement = Arrangement.SpaceBetween
                ) {
                  Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                      imageVector = Icons.Default.Payments,
                      contentDescription = null,
                      tint = BrandGreenPrimary,
                      modifier = Modifier.size(22.dp)
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                      Text(
                        text = "Cash on Delivery (COD)",
                        style = MaterialTheme.typography.titleSmall.copy(
                          fontWeight = FontWeight.Bold,
                          color = TextPrimaryDark
                        )
                      )
                      Text(
                        text = "Pay cash or scan QR when delivery partner arrives",
                        style = MaterialTheme.typography.labelSmall.copy(color = TextMuted)
                      )
                    }
                  }

                  Icon(
                    imageVector = if (isCod) Icons.Default.CheckCircle else Icons.Default.RadioButtonUnchecked,
                    contentDescription = null,
                    tint = if (isCod) BrandGreenPrimary else TextMuted
                  )
                }
              }

              Spacer(modifier = Modifier.height(8.dp))

              // Option 2: UPI on Delivery
              val isUpi = uiState.selectedPaymentMethod.startsWith("UPI")
              Surface(
                shape = RoundedCornerShape(10.dp),
                color = if (isUpi) BrandGreenSurface else MountainWhite,
                border = androidx.compose.foundation.BorderStroke(
                  1.dp,
                  if (isUpi) BrandGreenPrimary else MountainBorder
                ),
                modifier = Modifier
                  .fillMaxWidth()
                  .testTag("payment_upi_option")
                  .clickable { onSelectPaymentMethod("UPI on Delivery (GPay / PhonePe / Paytm)") }
              ) {
                Row(
                  modifier = Modifier.padding(12.dp),
                  verticalAlignment = Alignment.CenterVertically,
                  horizontalArrangement = Arrangement.SpaceBetween
                ) {
                  Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                      imageVector = Icons.Default.QrCode,
                      contentDescription = null,
                      tint = BrandGreenPrimary,
                      modifier = Modifier.size(22.dp)
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                      Text(
                        text = "UPI on Delivery",
                        style = MaterialTheme.typography.titleSmall.copy(
                          fontWeight = FontWeight.Bold,
                          color = TextPrimaryDark
                        )
                      )
                      Text(
                        text = "GPay, PhonePe, Paytm QR scanner",
                        style = MaterialTheme.typography.labelSmall.copy(color = TextMuted)
                      )
                    }
                  }

                  Icon(
                    imageVector = if (isUpi) Icons.Default.CheckCircle else Icons.Default.RadioButtonUnchecked,
                    contentDescription = null,
                    tint = if (isUpi) BrandGreenPrimary else TextMuted
                  )
                }
              }

              Spacer(modifier = Modifier.height(8.dp))

              // Option 3: Online Payment Gateway (Cards / NetBanking)
              val isOnline = uiState.selectedPaymentMethod.startsWith("Online Payment")
              Surface(
                shape = RoundedCornerShape(10.dp),
                color = if (isOnline) BrandGreenSurface else MountainWhite,
                border = androidx.compose.foundation.BorderStroke(
                  1.dp,
                  if (isOnline) BrandGreenPrimary else MountainBorder
                ),
                modifier = Modifier
                  .fillMaxWidth()
                  .testTag("payment_online_option")
                  .clickable { onSelectPaymentMethod("Online Payment (Bank Gateway)") }
              ) {
                Row(
                  modifier = Modifier.padding(12.dp),
                  verticalAlignment = Alignment.CenterVertically,
                  horizontalArrangement = Arrangement.SpaceBetween
                ) {
                  Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                      imageVector = Icons.Default.CreditCard,
                      contentDescription = null,
                      tint = BrandGreenPrimary,
                      modifier = Modifier.size(22.dp)
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                      Text(
                        text = "Online Payment (Verified Gateway)",
                        style = MaterialTheme.typography.titleSmall.copy(
                          fontWeight = FontWeight.Bold,
                          color = TextPrimaryDark
                        )
                      )
                      Text(
                        text = if (uiState.paymentVerificationSuccess) "✓ Authorized & Verified" else "Debit / Credit / NetBanking with OTP verification",
                        style = MaterialTheme.typography.labelSmall.copy(
                          color = if (uiState.paymentVerificationSuccess) BrandGreenDark else TextMuted
                        )
                      )
                    }
                  }

                  Icon(
                    imageVector = if (isOnline) Icons.Default.CheckCircle else Icons.Default.RadioButtonUnchecked,
                    contentDescription = null,
                    tint = if (isOnline) BrandGreenPrimary else TextMuted
                  )
                }
              }
            }
          }
        }

        // Promo Code & Coupons Card
        item {
          var promoInput by remember { mutableStateOf("") }

          Card(
            shape = RoundedCornerShape(14.dp),
            colors = CardDefaults.cardColors(containerColor = MountainCardSurface),
            border = androidx.compose.foundation.BorderStroke(1.dp, MountainBorder),
            modifier = Modifier.fillMaxWidth().testTag("coupon_voucher_card")
          ) {
            Column(modifier = Modifier.padding(14.dp)) {
              Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                  imageVector = Icons.Default.LocalOffer,
                  contentDescription = null,
                  tint = RewardGold,
                  modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                  text = "Coupons & Offers",
                  style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold,
                    color = TextPrimaryDark
                  )
                )
              }

              Spacer(modifier = Modifier.height(10.dp))

              if (uiState.appliedCoupon != null) {
                Surface(
                  shape = RoundedCornerShape(10.dp),
                  color = BrandGreenSurface,
                  border = androidx.compose.foundation.BorderStroke(1.dp, BrandGreenPrimary),
                  modifier = Modifier.fillMaxWidth()
                ) {
                  Row(
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                  ) {
                    Column {
                      Text(
                        text = "Voucher Applied: ${uiState.appliedCoupon.code}",
                        fontWeight = FontWeight.Bold,
                        color = BrandGreenDark,
                        fontSize = 13.sp
                      )
                      Text(
                        text = "You saved ₹${uiState.discountAmount.toInt()} on this order!",
                        fontSize = 11.sp,
                        color = BrandGreenPrimary
                      )
                    }

                    Button(
                      onClick = { onRemoveCoupon?.invoke() },
                      colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFEE2E2)),
                      contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp),
                      shape = RoundedCornerShape(8.dp)
                    ) {
                      Text("Remove", color = Color(0xFFDC2626), fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    }
                  }
                }
              } else {
                Row(
                  modifier = Modifier.fillMaxWidth(),
                  verticalAlignment = Alignment.CenterVertically
                ) {
                  OutlinedTextField(
                    value = promoInput,
                    onValueChange = { promoInput = it.uppercase() },
                    placeholder = { Text("Enter SHAHPUR50 or KANGRAVALLEY", fontSize = 12.sp) },
                    singleLine = true,
                    modifier = Modifier.weight(1f).testTag("coupon_code_input")
                  )
                  Spacer(modifier = Modifier.width(8.dp))
                  Button(
                    onClick = {
                      if (promoInput.isNotBlank()) {
                        onApplyCoupon?.invoke(promoInput.trim())
                      }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = BrandGreenPrimary),
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier.testTag("apply_coupon_btn")
                  ) {
                    Text("Apply", fontWeight = FontWeight.Bold)
                  }
                }

                if (uiState.couponErrorMessage != null) {
                  Spacer(modifier = Modifier.height(4.dp))
                  Text(
                    text = uiState.couponErrorMessage,
                    color = Color(0xFFDC2626),
                    fontSize = 11.sp
                  )
                }
              }
            }
          }
        }

        // 5. Bill Summary Card
        item {
          Card(
            shape = RoundedCornerShape(14.dp),
            colors = CardDefaults.cardColors(containerColor = MountainCardSurface),
            border = androidx.compose.foundation.BorderStroke(1.dp, MountainBorder),
            modifier = Modifier.fillMaxWidth().testTag("bill_summary_card")
          ) {
            Column(modifier = Modifier.padding(14.dp)) {
              Text(
                text = "Bill Details",
                style = MaterialTheme.typography.titleMedium.copy(
                  fontWeight = FontWeight.Bold,
                  color = TextPrimaryDark
                )
              )

              Spacer(modifier = Modifier.height(8.dp))

              Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
              ) {
                Text("Items Subtotal", style = MaterialTheme.typography.bodyMedium.copy(color = TextMuted))
                Text("₹${uiState.cartSubtotal.toInt()}", style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold))
              }

              if (uiState.discountAmount > 0) {
                Spacer(modifier = Modifier.height(6.dp))
                Row(
                  modifier = Modifier.fillMaxWidth(),
                  horizontalArrangement = Arrangement.SpaceBetween
                ) {
                  Text("Promo Discount (${uiState.appliedCoupon?.code})", style = MaterialTheme.typography.bodyMedium.copy(color = BrandGreenPrimary, fontWeight = FontWeight.Bold))
                  Text("-₹${uiState.discountAmount.toInt()}", style = MaterialTheme.typography.bodyMedium.copy(color = BrandGreenPrimary, fontWeight = FontWeight.Bold))
                }
              }

              Spacer(modifier = Modifier.height(6.dp))

              Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
              ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                  Text("Shahpur Express Delivery", style = MaterialTheme.typography.bodyMedium.copy(color = TextMuted))
                }
                if (uiState.deliveryFee == 0.0) {
                  Text("FREE", style = MaterialTheme.typography.bodyMedium.copy(color = BrandGreenLight, fontWeight = FontWeight.Bold))
                } else {
                  Text("₹${uiState.deliveryFee.toInt()}", style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold))
                }
              }

              Spacer(modifier = Modifier.height(6.dp))

              Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
              ) {
                Text("Platform Fee", style = MaterialTheme.typography.bodyMedium.copy(color = TextMuted))
                Text("₹${uiState.platformFee.toInt()}", style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold))
              }

              if (uiState.selectedTip > 0) {
                Spacer(modifier = Modifier.height(6.dp))
                Row(
                  modifier = Modifier.fillMaxWidth(),
                  horizontalArrangement = Arrangement.SpaceBetween
                ) {
                  Text("Delivery Partner Tip", style = MaterialTheme.typography.bodyMedium.copy(color = TextMuted))
                  Text("₹${uiState.selectedTip.toInt()}", style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold))
                }
              }

              HorizontalDivider(modifier = Modifier.padding(vertical = 10.dp), color = MountainBorder)

              Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
              ) {
                Text(
                  text = "Grand Total",
                  style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold,
                    color = TextPrimaryDark
                  )
                )
                Text(
                  text = "₹${uiState.grandTotal.toInt()}",
                  style = MaterialTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight.ExtraBold,
                    color = BrandGreenPrimary
                  )
                )
              }
            }
          }
        }

        // Trust note
        item {
          Row(
            modifier = Modifier
              .fillMaxWidth()
              .padding(vertical = 6.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Icon(
              imageVector = Icons.Default.Shield,
              contentDescription = null,
              tint = BrandGreenPrimary,
              modifier = Modifier.size(16.dp)
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(
              text = "ShahpurGo Guarantee • 100% Freshness Or Instant Refund",
              style = MaterialTheme.typography.labelSmall.copy(color = TextMuted)
            )
          }
        }

        item { Spacer(modifier = Modifier.height(20.dp)) }
      }
    }
  }
}
