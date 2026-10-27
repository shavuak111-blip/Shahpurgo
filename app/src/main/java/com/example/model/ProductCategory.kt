package com.example.model

data class PlatformCategory(
  val id: String,
  val displayName: String,
  val hindiName: String,
  val iconEmoji: String,
  val isEnabled: Boolean = true,
  val displayOrder: Int = 0
)

enum class ProductCategory(
  val id: String,
  val displayName: String,
  val hindiName: String,
  val iconEmoji: String
) {
  ALL("ALL", "All Categories", "सभी श्रेणियां", "🛍️"),
  FOOD("FOOD", "Prepared Food & Meals", "तैयार भोजन व नाश्ता", "🍱"),
  RESTAURANTS("RESTAURANTS", "Restaurants & Cafes", "रेस्तरां व कैफे", "🍲"),
  CHICKEN_MEAT("CHICKEN_MEAT", "Fresh Chicken & Meat", "ताजा चिकन व मीट", "🍗"),
  GROCERY("GROCERY", "Grocery & Staples", "किराना व राशन", "🛒"),
  FRUITS("FRUITS", "Fresh Fruits", "ताजे फल", "🍎"),
  VEGETABLES("VEGETABLES", "Fresh Vegetables", "हरी सब्जियां", "🥦"),
  DAIRY("DAIRY", "Milk, Dairy & Bakery", "दूध, डेयरी व बेकरी", "🥛"),
  DRINKS("DRINKS", "Drinks & Snacks", "पेय व स्नैक्स", "🧃"),
  HOUSEHOLD("HOUSEHOLD", "Household Supplies", "घरेलू सामान", "🧼"),
  CLEANING("CLEANING", "Cleaning Products", "सफाई सामग्री", "🧹"),
  PERSONAL_CARE("PERSONAL_CARE", "Personal Care & Beauty", "पर्सनल केयर", "🧴"),
  FASHION("FASHION", "Clothing & Footwear", "कपड़े व जूते", "👕"),
  ELECTRONICS("ELECTRONICS", "Electronics & Mobiles", "इलेक्ट्रॉनिक्स", "🔌"),
  HOME_KITCHEN("HOME_KITCHEN", "Home & Kitchen", "गृह व रसोई", "🍳"),
  HARDWARE("HARDWARE", "Hardware & Tools", "हार्डवेयर व औजार", "🔨"),
  STATIONERY("STATIONERY", "Books & Stationery", "पुस्तकें व स्टेशनरी", "📚"),
  TOYS_GIFTS("TOYS_GIFTS", "Toys & Gifts", "खिलौने व उपहार", "🧸"),
  PET_SUPPLIES("PET_SUPPLIES", "Pet Supplies", "पालतू पशु आहार", "🐾");

  companion object {
    fun fromId(id: String): ProductCategory {
      return entries.firstOrNull { it.id.equals(id, ignoreCase = true) } ?: GROCERY
    }

    val defaultPlatformCategories = entries.filter { it != ALL }.mapIndexed { index, cat ->
      PlatformCategory(
        id = cat.id,
        displayName = cat.displayName,
        hindiName = cat.hindiName,
        iconEmoji = cat.iconEmoji,
        isEnabled = true,
        displayOrder = index
      )
    }
  }
}
