package com.example.model

enum class ShopApprovalStatus(val title: String) {
  APPROVED("Approved"),
  PENDING("Pending Verification"),
  SUSPENDED("Suspended")
}

data class Shop(
  val id: String,
  val name: String,
  val hindiName: String,
  val primaryCategory: ProductCategory,
  val address: String,
  val phone: String,
  val adminEmail: String,
  val iconEmoji: String,
  val isHubStore: Boolean = false,
  val status: ShopApprovalStatus = ShopApprovalStatus.APPROVED,
  val isOpen: Boolean = true,
  val openingTime: String = "08:00 AM",
  val closingTime: String = "09:30 PM",
  val rating: Float = 4.8f,
  val deliveryTimeEstimate: String = "15-25 mins",
  val minimumOrderValue: Double = 99.0,
  val commissionRatePercent: Double = 5.0,
  val assignedAdminName: String = "Store Partner"
) {
  companion object {
    const val PLATFORM_OWNER_EMAIL = "shavuak111@gmail.com"

    val defaultShops = listOf(
      Shop(
        id = "shahpur-central-hub",
        name = "Shahpur Central Mart & Hub",
        hindiName = "शाहपुर सेंट्रल मार्ट",
        primaryCategory = ProductCategory.GROCERY,
        address = "Main Bazar, Near Chambi Chowk, Shahpur HP 176206",
        phone = "+91 98160 54321",
        adminEmail = PLATFORM_OWNER_EMAIL,
        iconEmoji = "🏪",
        isHubStore = true,
        status = ShopApprovalStatus.APPROVED,
        isOpen = true,
        rating = 4.9f,
        deliveryTimeEstimate = "12-18 mins",
        assignedAdminName = "Central Hub Ops"
      ),
      Shop(
        id = "dhauladhar-greens",
        name = "Dhauladhar Fresh Fruits & Veggies",
        hindiName = "धौलाधार फ्रेश सब्जियां व फल",
        primaryCategory = ProductCategory.FRUITS,
        address = "Old Bus Stand Road, Shahpur",
        phone = "+91 94180 23456",
        adminEmail = "dhauladhar.greens@shahpurgo.in",
        iconEmoji = "🍎",
        status = ShopApprovalStatus.APPROVED,
        isOpen = true,
        rating = 4.8f,
        deliveryTimeEstimate = "15-20 mins",
        assignedAdminName = "Surinder Dogra"
      ),
      Shop(
        id = "himachal-rasoi",
        name = "Himachal Rasoi Restaurant",
        hindiName = "हिमाचल रसोई रेस्टोरेंट",
        primaryCategory = ProductCategory.RESTAURANTS,
        address = "Dharamsala Road, Near ITI Shahpur",
        phone = "+91 98055 98765",
        adminEmail = "rasoi@shahpurgo.in",
        iconEmoji = "🍲",
        status = ShopApprovalStatus.APPROVED,
        isOpen = true,
        rating = 4.7f,
        deliveryTimeEstimate = "25-35 mins",
        assignedAdminName = "Chef Manoj Rana"
      ),
      Shop(
        id = "kangra-dairy-bakers",
        name = "Kangra Valley Dairy & Sweets",
        hindiName = "कांगड़ा वैली डेयरी व मिष्ठान",
        primaryCategory = ProductCategory.DAIRY,
        address = "Chambi Bazar, Shahpur",
        phone = "+91 98161 33445",
        adminEmail = "kangradairy@shahpurgo.in",
        iconEmoji = "🥛",
        status = ShopApprovalStatus.APPROVED,
        isOpen = true,
        rating = 4.9f,
        deliveryTimeEstimate = "15-20 mins",
        assignedAdminName = "Pankaj Sood"
      ),
      Shop(
        id = "shiva-electronics",
        name = "Shiva Electronics & Mobiles",
        hindiName = "शिवा इलेक्ट्रॉनिक्स",
        primaryCategory = ProductCategory.ELECTRONICS,
        address = "Hospital Road, Shahpur",
        phone = "+91 98162 44556",
        adminEmail = "shiva.electro@shahpurgo.in",
        iconEmoji = "🔌",
        status = ShopApprovalStatus.APPROVED,
        isOpen = true,
        rating = 4.6f,
        deliveryTimeEstimate = "20-30 mins",
        assignedAdminName = "Ashok Kumar"
      ),
      Shop(
        id = "pahadi-hardware",
        name = "Pahadi Hardware & Electricals",
        hindiName = "पहाड़ी हार्डवेयर स्टोर",
        primaryCategory = ProductCategory.HARDWARE,
        address = "National Highway-154, Shahpur",
        phone = "+91 98163 55667",
        adminEmail = "hardware@shahpurgo.in",
        iconEmoji = "🔨",
        status = ShopApprovalStatus.APPROVED,
        isOpen = true,
        rating = 4.7f,
        deliveryTimeEstimate = "20-30 mins",
        assignedAdminName = "Rajesh Verma"
      ),
      Shop(
        id = "vidya-stationers",
        name = "Vidya Pustak Bhandar & Stationery",
        hindiName = "विद्या पुस्तक भण्डार",
        primaryCategory = ProductCategory.STATIONERY,
        address = "College Gate, Govt College Shahpur",
        phone = "+91 94182 66778",
        adminEmail = "vidya.books@shahpurgo.in",
        iconEmoji = "📚",
        status = ShopApprovalStatus.APPROVED,
        isOpen = true,
        rating = 4.8f,
        deliveryTimeEstimate = "15-25 mins",
        assignedAdminName = "Vipin Sharma"
      ),
      Shop(
        id = "himalayan-apparel",
        name = "Himalayan Fashion & Woollens",
        hindiName = "हिमालयन फैशन वूलन",
        primaryCategory = ProductCategory.FASHION,
        address = "Upper Bazar, Shahpur",
        phone = "+91 98164 77889",
        adminEmail = "fashion@shahpurgo.in",
        iconEmoji = "👕",
        status = ShopApprovalStatus.APPROVED,
        isOpen = true,
        rating = 4.6f,
        deliveryTimeEstimate = "25-35 mins",
        assignedAdminName = "Rohit Katoch"
      )
    )

    fun findShopById(id: String): Shop {
      return defaultShops.firstOrNull { it.id == id } ?: defaultShops.first()
    }
  }
}
