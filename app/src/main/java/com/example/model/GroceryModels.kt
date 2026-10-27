package com.example.model

enum class GroceryCategory(val displayName: String, val iconEmoji: String) {
  ALL("All Items", "🛒"),
  FRESH_PRODUCE("Himachal Fresh", "🍎"),
  KANGRA_SPECIALS("Kangra Specials", "☕"),
  DAIRY_BAKERY("Dairy & Bakery", "🥛"),
  PANTRY_STAPLES("Atta, Rice & Dal", "🌾"),
  SNACKS_BEVERAGES("Snacks & Drinks", "🍪")
}

data class GroceryItem(
  val id: String,
  val name: String,
  val hindiName: String,
  val category: GroceryCategory,
  val price: Double,
  val originalPrice: Double,
  val costPrice: Double, // wholesale store cost for admin profit calculations
  val unit: String,
  val stockQuantity: Int,
  val inStock: Boolean = true,
  val isFeatured: Boolean = false,
  val rating: Float = 4.8f,
  val badge: String? = null,
  val iconSymbol: String = "cart"
)

data class CartItem(
  val item: GroceryItem,
  val quantity: Int
)

data class DeliveryLocation(
  val id: String,
  val tag: String, // "Home", "Hostel / PG", "Shop / Work", "Bazar"
  val areaName: String,
  val fullAddress: String,
  val landmark: String,
  val latitude: Double,
  val longitude: Double
)

enum class OrderStatus(val title: String, val subtitle: String, val stepIndex: Int) {
  PLACED("Order Placed", "Store received your order", 0),
  ACCEPTED("Order Accepted", "Store confirmed and preparing", 1),
  CONFIRMED("Order Placed", "Shahpur store received your order", 0),
  PACKING("Packing Items", "Store partner is packing your goods", 2),
  PACKED("Order Packed", "Freshly packed and ready for pickup", 2),
  OUT_FOR_DELIVERY("Out for Delivery", "Rider is speeding through Kangra Valley", 3),
  DELIVERED("Delivered", "Handed over safely at your doorstep", 4),
  CANCELLED("Cancelled", "Order was cancelled", -1);

  val isCancellable: Boolean
    get() = this == PLACED || this == ACCEPTED || this == CONFIRMED
}

data class Coupon(
  val code: String,
  val title: String,
  val description: String,
  val discountPercent: Double = 0.0,
  val flatDiscount: Double = 0.0,
  val minOrderAmount: Double = 0.0,
  val maxDiscount: Double = 200.0
) {
  fun calculateDiscount(subtotal: Double): Double {
    if (subtotal < minOrderAmount) return 0.0
    val discount = if (flatDiscount > 0.0) {
      flatDiscount
    } else {
      (subtotal * discountPercent / 100.0)
    }
    return discount.coerceAtMost(maxDiscount)
  }

  companion object {
    val defaultCoupons = listOf(
      Coupon(
        code = "SHAHPUR50",
        title = "Flat ₹50 OFF",
        description = "Get flat ₹50 off on orders above ₹249",
        flatDiscount = 50.0,
        minOrderAmount = 249.0
      ),
      Coupon(
        code = "KANGRAVALLEY",
        title = "15% Kangra Special",
        description = "15% off up to ₹150 for Kangra residents",
        discountPercent = 15.0,
        minOrderAmount = 199.0,
        maxDiscount = 150.0
      ),
      Coupon(
        code = "FIRSTFREE",
        title = "Free Delivery + ₹30 OFF",
        description = "Welcome voucher for your first ShahpurGo order",
        flatDiscount = 30.0,
        minOrderAmount = 149.0
      )
    )
  }
}

data class PlatformAuditLog(
  val id: String = java.util.UUID.randomUUID().toString(),
  val action: String,
  val performedBy: String,
  val timestamp: Long = System.currentTimeMillis(),
  val details: String,
  val targetEntity: String = "Platform"
)

data class Order(
  val id: String,
  val items: List<CartItem>,
  val subtotal: Double,
  val deliveryFee: Double,
  val tipAmount: Double,
  val platformFee: Double = 2.0,
  val total: Double,
  val status: OrderStatus = OrderStatus.CONFIRMED,
  val timestamp: Long = System.currentTimeMillis(),
  val deliveryLocation: DeliveryLocation,
  val paymentMethod: String = "Cash on Delivery (COD)",
  val deliveryPartnerName: String = "Raman Kumar (Shahpur Local)",
  val deliveryPartnerPhone: String = "+91 98160 54321",
  val etaMinutes: Int = 18,
  val shopId: String = "shahpur-central-hub",
  val shopName: String = "Shahpur Central Mart & Hub",
  val cancellationReason: String = "",
  val couponCode: String? = null,
  val discountAmount: Double = 0.0,
  val isPaymentVerified: Boolean = false,
  val rating: Float = 0.0f,
  val review: String = ""
)

data class UserAccount(
  val name: String = "Rajat Sharma",
  val phone: String = "+91 98160 12345",
  val email: String = "rajat.kangra@shahpurgo.in",
  val photoUrl: String? = null,
  val uid: String? = null,
  val isGoogleSignedIn: Boolean = false,
  val referralCode: String = "SHAHPUR50",
  val coinsBalance: Int = 150,
  val totalSavings: Double = 340.0,
  val referralsCount: Int = 3,
  val currentAddress: DeliveryLocation = SampleData.defaultLocations[0],
  val isAdmin: Boolean = false
)

enum class NotificationType {
  ORDER, REWARD, PROMO, SYSTEM
}

data class AppNotification(
  val id: String,
  val title: String,
  val message: String,
  val timeAgo: String,
  val type: NotificationType,
  val isRead: Boolean = false
)

data class SalesReport(
  val todayRevenue: Double,
  val todayProfit: Double,
  val totalOrders: Int,
  val averageOrderValue: Double,
  val profitMarginPercent: Double,
  val hourlyRevenue: List<Pair<String, Double>>,
  val topSellingItemNames: List<Pair<String, Int>>
)

data class EstimatedDeliveryInfo(
  val minMinutes: Int,
  val maxMinutes: Int,
  val rangeString: String,
  val timeWindowString: String,
  val trafficContext: String,
  val orderSizeContext: String,
  val isRushHour: Boolean
)

fun calculateEstimatedDeliveryTime(
  itemCount: Int,
  currentTimeMillis: Long = System.currentTimeMillis()
): EstimatedDeliveryInfo {
  val calendar = java.util.Calendar.getInstance().apply {
    timeInMillis = currentTimeMillis
  }
  val hour = calendar.get(java.util.Calendar.HOUR_OF_DAY)
  val minute = calendar.get(java.util.Calendar.MINUTE)

  // Base transit time in Shahpur local delivery zone
  val baseMin = 12

  // Order size buffer
  val (sizeMinAdd, sizeLabel) = when {
    itemCount <= 2 -> 1 to "Quick Pack ($itemCount item${if (itemCount > 1) "s" else ""})"
    itemCount in 3..6 -> 3 to "Standard Pack ($itemCount items)"
    itemCount in 7..11 -> 6 to "Multi-Category Pack ($itemCount items)"
    else -> 9 to "Bulky Order Prep ($itemCount items)"
  }

  // Time of day traffic / hub volume buffer
  val isMorningRush = (hour in 8..9) || (hour == 10 && minute <= 30)
  val isEveningRush = (hour in 17..20)
  val isNight = (hour >= 21 || hour < 7)

  val (timeMinAdd, trafficLabel, isRush) = when {
    isMorningRush -> Triple(5, "Morning Market Rush • Shahpur Hub", true)
    isEveningRush -> Triple(6, "Evening Peak Rush • NH-154 Bazar", true)
    isNight -> Triple(4, "Night Valley Transit", false)
    else -> Triple(1, "Clear Valley Roads • Fast Flow", false)
  }

  val minMinutes = baseMin + sizeMinAdd + timeMinAdd
  val maxMinutes = minMinutes + 4

  val sdf = java.text.SimpleDateFormat("hh:mm a", java.util.Locale.getDefault())
  val startEta = sdf.format(java.util.Date(currentTimeMillis + (minMinutes * 60 * 1000L)))
  val endEta = sdf.format(java.util.Date(currentTimeMillis + (maxMinutes * 60 * 1000L)))

  return EstimatedDeliveryInfo(
    minMinutes = minMinutes,
    maxMinutes = maxMinutes,
    rangeString = "$minMinutes–$maxMinutes mins",
    timeWindowString = "Arriving by $startEta – $endEta",
    trafficContext = trafficLabel,
    orderSizeContext = sizeLabel,
    isRushHour = isRush
  )
}
