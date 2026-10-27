package com.example.model

object SampleData {

  val defaultLocations = listOf(
    DeliveryLocation(
      id = "loc_1",
      tag = "Home",
      areaName = "Shahpur Main Bazar",
      fullAddress = "Ward No. 3, Near Bus Stand & Post Office, Shahpur, Kangra HP 176206",
      landmark = "Opposite State Bank of India",
      latitude = 32.2244,
      longitude = 76.1772
    ),
    DeliveryLocation(
      id = "loc_2",
      tag = "Hostel / PG",
      areaName = "Dronacharya PG College Road",
      fullAddress = "Near Dronacharya PG College of Education, Rait-Shahpur Road, Kangra HP 176206",
      landmark = "Green Valley Residency, Gate 2",
      latitude = 32.2198,
      longitude = 76.1685
    ),
    DeliveryLocation(
      id = "loc_3",
      tag = "Shop / Work",
      areaName = "Rait Tehsil Chowk",
      fullAddress = "Tehsil Complex Road, Rait, Shahpur Tehsil, Kangra HP",
      landmark = "Near Sub-Tehsil Office",
      latitude = 32.2150,
      longitude = 76.1620
    ),
    DeliveryLocation(
      id = "loc_4",
      tag = "Market",
      areaName = "Chambi Junction",
      fullAddress = "National Highway 154, Chambi Market, Kangra District HP",
      landmark = "Next to Fuel Station",
      latitude = 32.2080,
      longitude = 76.2100
    )
  )

  val initialCatalogue = listOf(
    // Himachal Fresh Produce
    GroceryItem(
      id = "item_1",
      name = "Kinnaur Royal Himachal Apples",
      hindiName = "किन्नौर रॉयल हिमाचली सेब",
      category = GroceryCategory.FRESH_PRODUCE,
      price = 149.0,
      originalPrice = 180.0,
      costPrice = 110.0,
      unit = "1 kg",
      stockQuantity = 45,
      inStock = true,
      isFeatured = true,
      rating = 4.9f,
      badge = "Himachal Crisp",
      iconSymbol = "apple"
    ),
    GroceryItem(
      id = "item_2",
      name = "Kangra Valley Fresh Cauliflower",
      hindiName = "कांगड़ा ताज़ा फूलगोभी",
      category = GroceryCategory.FRESH_PRODUCE,
      price = 38.0,
      originalPrice = 50.0,
      costPrice = 25.0,
      unit = "1 pc (~800g)",
      stockQuantity = 30,
      inStock = true,
      isFeatured = false,
      rating = 4.7f,
      badge = "Farm Harvest",
      iconSymbol = "veggie"
    ),
    GroceryItem(
      id = "item_3",
      name = "Organic Pahadi Desi Potatoes",
      hindiName = "पहाड़ी जैविक आलू",
      category = GroceryCategory.FRESH_PRODUCE,
      price = 35.0,
      originalPrice = 45.0,
      costPrice = 22.0,
      unit = "1 kg",
      stockQuantity = 80,
      inStock = true,
      isFeatured = false,
      rating = 4.8f,
      badge = "Pahadi Taste",
      iconSymbol = "potato"
    ),
    GroceryItem(
      id = "item_4",
      name = "Fresh Red Vine Tomatoes",
      hindiName = "देसी लाल टमाटर",
      category = GroceryCategory.FRESH_PRODUCE,
      price = 42.0,
      originalPrice = 55.0,
      costPrice = 28.0,
      unit = "1 kg",
      stockQuantity = 60,
      inStock = true,
      isFeatured = false,
      rating = 4.6f,
      iconSymbol = "tomato"
    ),
    GroceryItem(
      id = "item_5",
      name = "Fresh Mountain Mint & Coriander Bunch",
      hindiName = "पहाड़ी पुदीना व हरा धनिया",
      category = GroceryCategory.FRESH_PRODUCE,
      price = 20.0,
      originalPrice = 25.0,
      costPrice = 10.0,
      unit = "1 Combo Pack",
      stockQuantity = 25,
      inStock = true,
      rating = 4.9f,
      badge = "Aromatic",
      iconSymbol = "herb"
    ),

    // Kangra Valley Specials
    GroceryItem(
      id = "item_6",
      name = "Kangra Orthodox Whole Leaf Green Tea",
      hindiName = "कांगड़ा ऑर्थोडॉक्स ग्रीन टी",
      category = GroceryCategory.KANGRA_SPECIALS,
      price = 220.0,
      originalPrice = 270.0,
      costPrice = 160.0,
      unit = "250 g Box",
      stockQuantity = 35,
      inStock = true,
      isFeatured = true,
      rating = 5.0f,
      badge = "GI Tagged",
      iconSymbol = "tea"
    ),
    GroceryItem(
      id = "item_7",
      name = "Himachal Pure Forest Wild Honey",
      hindiName = "हिमाचली शुद्ध जंगली शहद",
      category = GroceryCategory.KANGRA_SPECIALS,
      price = 295.0,
      originalPrice = 350.0,
      costPrice = 210.0,
      unit = "500 g Glass Jar",
      stockQuantity = 20,
      inStock = true,
      isFeatured = true,
      rating = 4.9f,
      badge = "100% Raw",
      iconSymbol = "honey"
    ),
    GroceryItem(
      id = "item_8",
      name = "Chamba/Kangra Red Pahadi Rajma",
      hindiName = "चंबा लाल पहाड़ी राजमा",
      category = GroceryCategory.KANGRA_SPECIALS,
      price = 185.0,
      originalPrice = 220.0,
      costPrice = 135.0,
      unit = "1 kg Pack",
      stockQuantity = 50,
      inStock = true,
      isFeatured = true,
      rating = 4.9f,
      badge = "Specialty",
      iconSymbol = "rajma"
    ),
    GroceryItem(
      id = "item_9",
      name = "Himachal Traditional Siddu Wheat Flour Mix",
      hindiName = "हिमाचली सिड्डू आटा मिश्रण",
      category = GroceryCategory.KANGRA_SPECIALS,
      price = 110.0,
      originalPrice = 130.0,
      costPrice = 80.0,
      unit = "1 kg",
      stockQuantity = 18,
      inStock = true,
      rating = 4.8f,
      badge = "Local Favorite",
      iconSymbol = "wheat"
    ),

    // Dairy & Bakery
    GroceryItem(
      id = "item_10",
      name = "Verka Standardized Fresh Milk",
      hindiName = "वेरका ताज़ा दूध",
      category = GroceryCategory.DAIRY_BAKERY,
      price = 33.0,
      originalPrice = 33.0,
      costPrice = 30.0,
      unit = "500 ml Pouch",
      stockQuantity = 90,
      inStock = true,
      isFeatured = false,
      rating = 4.9f,
      badge = "Daily Essential",
      iconSymbol = "milk"
    ),
    GroceryItem(
      id = "item_11",
      name = "Amul Pasteurized Table Butter",
      hindiName = "अमूल मक्खन",
      category = GroceryCategory.DAIRY_BAKERY,
      price = 56.0,
      originalPrice = 60.0,
      costPrice = 50.0,
      unit = "100 g",
      stockQuantity = 40,
      inStock = true,
      rating = 4.8f,
      iconSymbol = "butter"
    ),
    GroceryItem(
      id = "item_12",
      name = "Fresh Malai Paneer Block",
      hindiName = "ताज़ा मलाई पनीर",
      category = GroceryCategory.DAIRY_BAKERY,
      price = 95.0,
      originalPrice = 110.0,
      costPrice = 75.0,
      unit = "200 g",
      stockQuantity = 22,
      inStock = true,
      rating = 4.9f,
      badge = "Ultra Fresh",
      iconSymbol = "paneer"
    ),
    GroceryItem(
      id = "item_13",
      name = "Whole Wheat Artisan Brown Bread",
      hindiName = "ब्राउन ब्रेड",
      category = GroceryCategory.DAIRY_BAKERY,
      price = 45.0,
      originalPrice = 50.0,
      costPrice = 35.0,
      unit = "400 g Loaf",
      stockQuantity = 15,
      inStock = true,
      rating = 4.7f,
      iconSymbol = "bread"
    ),

    // Pantry Staples
    GroceryItem(
      id = "item_14",
      name = "Aashirvaad Shudh Chakki Atta",
      hindiName = "आशीर्वाद शुद्ध चक्की आटा",
      category = GroceryCategory.PANTRY_STAPLES,
      price = 245.0,
      originalPrice = 280.0,
      costPrice = 210.0,
      unit = "5 kg Bag",
      stockQuantity = 35,
      inStock = true,
      isFeatured = true,
      rating = 4.9f,
      badge = "Top Choice",
      iconSymbol = "flour"
    ),
    GroceryItem(
      id = "item_15",
      name = "Daawat Rozana Super Basmati Rice",
      hindiName = "दावत रोज़ाना बासमती चावल",
      category = GroceryCategory.PANTRY_STAPLES,
      price = 99.0,
      originalPrice = 125.0,
      costPrice = 78.0,
      unit = "1 kg Pack",
      stockQuantity = 40,
      inStock = true,
      rating = 4.8f,
      iconSymbol = "rice"
    ),
    GroceryItem(
      id = "item_16",
      name = "Fortune Kachi Ghani Mustard Oil",
      hindiName = "फार्च्यून सरसों का तेल",
      category = GroceryCategory.PANTRY_STAPLES,
      price = 145.0,
      originalPrice = 165.0,
      costPrice = 125.0,
      unit = "1 Litre Bottle",
      stockQuantity = 30,
      inStock = true,
      rating = 4.8f,
      iconSymbol = "oil"
    ),
    GroceryItem(
      id = "item_17",
      name = "Tata Salt Vacuum Evaporated Iodized",
      hindiName = "टाटा नमक देश का नमक",
      category = GroceryCategory.PANTRY_STAPLES,
      price = 28.0,
      originalPrice = 30.0,
      costPrice = 23.0,
      unit = "1 kg Pouch",
      stockQuantity = 75,
      inStock = true,
      rating = 4.9f,
      iconSymbol = "salt"
    ),

    // Snacks & Drinks
    GroceryItem(
      id = "item_18",
      name = "Bikanervala Aloo Bhujia Namkeen",
      hindiName = "बीकानेरवाला आलू भुजिया",
      category = GroceryCategory.SNACKS_BEVERAGES,
      price = 55.0,
      originalPrice = 65.0,
      costPrice = 42.0,
      unit = "200 g",
      stockQuantity = 35,
      inStock = true,
      rating = 4.7f,
      iconSymbol = "snack"
    ),
    GroceryItem(
      id = "item_19",
      name = "Real Himalayan Mixed Fruit Juice",
      hindiName = "रियल मिक्स्ड फ्रूट जूस",
      category = GroceryCategory.SNACKS_BEVERAGES,
      price = 110.0,
      originalPrice = 130.0,
      costPrice = 90.0,
      unit = "1 Litre Tetra",
      stockQuantity = 24,
      inStock = true,
      rating = 4.8f,
      badge = "Chilled",
      iconSymbol = "juice"
    ),
    GroceryItem(
      id = "item_20",
      name = "Maggi 2-Minute Masala Noodles",
      hindiName = "मैगी मसाला नूडल्स",
      category = GroceryCategory.SNACKS_BEVERAGES,
      price = 56.0,
      originalPrice = 60.0,
      costPrice = 48.0,
      unit = "Pack of 4 (280g)",
      stockQuantity = 55,
      inStock = true,
      rating = 4.9f,
      badge = "Fast Delivery",
      iconSymbol = "noodles"
    )
  )

  val initialNotifications = listOf(
    AppNotification(
      id = "notif_1",
      title = "Welcome to ShahpurGo!",
      message = "Shahpur Ka Apna Delivery App is now live in Shahpur, Kangra! Enjoy ₹50 off on your first order with code SHAHPUR50.",
      timeAgo = "10m ago",
      type = NotificationType.PROMO,
      isRead = false
    ),
    AppNotification(
      id = "notif_2",
      title = "Kangra Tea Harvest Special",
      message = "Freshly plucked Kangra orthodox green tea has arrived directly from nearby tea estates.",
      timeAgo = "1h ago",
      type = NotificationType.PROMO,
      isRead = false
    ),
    AppNotification(
      id = "notif_3",
      title = "Referral Bonus Credited!",
      message = "Your friend Amit in Rait joined using your code. 50 ShahpurGo Coins credited to your wallet!",
      timeAgo = "Yesterday",
      type = NotificationType.REWARD,
      isRead = true
    ),
    AppNotification(
      id = "notif_4",
      title = "Shahpur Dark Hub Operational",
      message = "Superfast 15-20 min delivery now covers Dronacharya College, Chambi, and Main Bazar.",
      timeAgo = "2 days ago",
      type = NotificationType.SYSTEM,
      isRead = true
    )
  )

  val initialRecentOrder = Order(
    id = "SPG-8921",
    items = listOf(
      CartItem(initialCatalogue[0], 2), // Kinnaur Apples
      CartItem(initialCatalogue[5], 1), // Kangra Tea
      CartItem(initialCatalogue[9], 2)  // Verka Milk
    ),
    subtotal = 584.0,
    deliveryFee = 0.0, // Free above 199
    tipAmount = 20.0,
    platformFee = 2.0,
    total = 606.0,
    status = OrderStatus.OUT_FOR_DELIVERY,
    timestamp = System.currentTimeMillis() - (8 * 60 * 1000), // 8 mins ago
    deliveryLocation = defaultLocations[0],
    paymentMethod = "Cash on Delivery (COD)",
    deliveryPartnerName = "Raman Kumar (Shahpur Local)",
    deliveryPartnerPhone = "+91 98160 54321",
    etaMinutes = 11
  )

  val initialPastOrders = listOf(
    initialRecentOrder,
    Order(
      id = "SPG-7640",
      items = listOf(
        CartItem(initialCatalogue[13], 1), // Aashirvaad Atta 5kg (245)
        CartItem(initialCatalogue[2], 1),  // Desi Potatoes 1kg (35)
        CartItem(initialCatalogue[11], 1)  // Fresh Malai Paneer (95)
      ),
      subtotal = 375.0,
      deliveryFee = 0.0,
      tipAmount = 20.0,
      platformFee = 2.0,
      total = 397.0,
      status = OrderStatus.DELIVERED,
      timestamp = System.currentTimeMillis() - (26 * 60 * 60 * 1000), // Yesterday
      deliveryLocation = defaultLocations[0], // Main Bazar
      paymentMethod = "Cash on Delivery (COD)",
      deliveryPartnerName = "Suresh Verma (Shahpur)",
      deliveryPartnerPhone = "+91 98160 88219",
      etaMinutes = 0
    ),
    Order(
      id = "SPG-6192",
      items = listOf(
        CartItem(initialCatalogue[19], 1), // Maggi Noodles 4-Pack (56)
        CartItem(initialCatalogue[18], 1), // Real Fruit Juice (110)
        CartItem(initialCatalogue[17], 1)  // Bikanervala Bhujia (55)
      ),
      subtotal = 221.0,
      deliveryFee = 0.0,
      tipAmount = 10.0,
      platformFee = 2.0,
      total = 233.0,
      status = OrderStatus.DELIVERED,
      timestamp = System.currentTimeMillis() - (3 * 24 * 60 * 60 * 1000), // 3 days ago
      deliveryLocation = defaultLocations[1], // Dronacharya PG College
      paymentMethod = "UPI on Delivery",
      deliveryPartnerName = "Raman Kumar (Shahpur Local)",
      deliveryPartnerPhone = "+91 98160 54321",
      etaMinutes = 0
    ),
    Order(
      id = "SPG-5510",
      items = listOf(
        CartItem(initialCatalogue[5], 1), // Kangra Tea (220)
        CartItem(initialCatalogue[6], 1), // Wild Honey (295)
        CartItem(initialCatalogue[7], 1)  // Pahadi Rajma (185)
      ),
      subtotal = 700.0,
      deliveryFee = 0.0,
      tipAmount = 30.0,
      platformFee = 2.0,
      total = 732.0,
      status = OrderStatus.DELIVERED,
      timestamp = System.currentTimeMillis() - (6 * 24 * 60 * 60 * 1000), // 6 days ago
      deliveryLocation = defaultLocations[2], // Rait Tehsil Chowk
      paymentMethod = "Cash on Delivery (COD)",
      deliveryPartnerName = "Ajay Sharma (Rait)",
      deliveryPartnerPhone = "+91 98160 33190",
      etaMinutes = 0
    )
  )

  val initialSalesReport = SalesReport(
    todayRevenue = 18450.0,
    todayProfit = 4620.0,
    totalOrders = 38,
    averageOrderValue = 485.5,
    profitMarginPercent = 25.04,
    hourlyRevenue = listOf(
      "8 AM" to 1200.0,
      "10 AM" to 2800.0,
      "12 PM" to 3450.0,
      "2 PM" to 2100.0,
      "4 PM" to 3100.0,
      "6 PM" to 4200.0,
      "8 PM" to 1600.0
    ),
    topSellingItemNames = listOf(
      "Kinnaur Royal Apples" to 48,
      "Verka Fresh Milk" to 82,
      "Kangra Green Tea" to 31,
      "Chamba Red Rajma" to 29,
      "Aashirvaad Atta" to 25
    )
  )
}
