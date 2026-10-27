package com.example

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.example.model.GroceryCategory
import com.example.model.SampleData
import com.example.model.toOrder
import com.example.viewmodel.GroceryViewModel
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseOptions
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [34])
class ExampleRobolectricTest {

  @Before
  fun setUp() {
    val context = ApplicationProvider.getApplicationContext<Context>()
    if (FirebaseApp.getApps(context).isEmpty()) {
      val options = FirebaseOptions.Builder()
        .setApplicationId("com.aistudio.shahpurgo.kxwzqp")
        .setApiKey("fakeApiKeyForTest_1234567890abcdef")
        .setProjectId("ai-studio-android-shahpurg-d6f4fa4b-f179-47aa-adb6-f0255eb969e7")
        .build()
      FirebaseApp.initializeApp(context, options)
    }
  }

  @org.junit.After
  fun tearDown() {
    val context = ApplicationProvider.getApplicationContext<Context>()
    for (app in FirebaseApp.getApps(context)) {
      app.delete()
    }
  }

  @Test
  fun `read ShahpurGo brand string from context`() {
    val context = ApplicationProvider.getApplicationContext<Context>()
    val appName = context.getString(R.string.app_name)
    assertEquals("ShahpurGo", appName)
    val tagline = context.getString(R.string.tagline)
    assertEquals("Shahpur Ka Apna Delivery App", tagline)
  }

  @Test
  fun `verify initial Shahpur catalogue contains Himachal and Kangra items`() {
    val items = SampleData.initialCatalogue
    assertTrue(items.any { it.name.contains("Kangra", ignoreCase = true) })
    assertTrue(items.any { it.name.contains("Himachal", ignoreCase = true) || it.name.contains("Kinnaur", ignoreCase = true) })
    assertTrue(items.any { it.category == GroceryCategory.KANGRA_SPECIALS })
  }

  @Test
  fun `cart and order placement flow in ViewModel`() {
    val viewModel = GroceryViewModel()
    val firstItem = viewModel.uiState.value.catalogue.first()

    // Add 2 items to cart
    viewModel.addToCart(firstItem)
    viewModel.addToCart(firstItem)

    assertEquals(1, viewModel.uiState.value.cart.size)
    assertEquals(2, viewModel.uiState.value.cartItemCount)
    assertEquals(firstItem.price * 2, viewModel.uiState.value.cartSubtotal, 0.01)

    // Set tip
    viewModel.setDeliveryTip(20.0)
    assertEquals(20.0, viewModel.uiState.value.selectedTip, 0.01)

    // Place order
    val placedOrder = viewModel.placeOrder()
    assertNotNull(placedOrder)
    assertEquals(0, viewModel.uiState.value.cart.size)
    assertTrue(placedOrder!!.paymentMethod.contains("Cash on Delivery"))
  }

  @Test
  fun `scratch card rewards credit coins`() {
    val viewModel = GroceryViewModel()
    val initialCoins = viewModel.uiState.value.user.coinsBalance

    viewModel.scratchCard()

    assertTrue(viewModel.uiState.value.scratchCardRevealed)
    assertEquals(
      initialCoins + viewModel.uiState.value.scratchedRewardAmount,
      viewModel.uiState.value.user.coinsBalance
    )
  }

  @Test
  fun `verify order tracking progress sequence from Order Placed to Delivered`() {
    val viewModel = GroceryViewModel()
    val order = viewModel.uiState.value.orders.first()

    // Test transition to Order Placed
    viewModel.updateOrderStatus(order.id, com.example.model.OrderStatus.CONFIRMED)
    assertEquals("Order Placed", viewModel.uiState.value.orders.first { it.id == order.id }.status.title)

    // Test transition to Order Packed
    viewModel.updateOrderStatus(order.id, com.example.model.OrderStatus.PACKED)
    assertEquals("Order Packed", viewModel.uiState.value.orders.first { it.id == order.id }.status.title)

    // Test transition to Out for Delivery
    viewModel.updateOrderStatus(order.id, com.example.model.OrderStatus.OUT_FOR_DELIVERY)
    assertEquals("Out for Delivery", viewModel.uiState.value.orders.first { it.id == order.id }.status.title)

    // Test transition to Delivered
    viewModel.updateOrderStatus(order.id, com.example.model.OrderStatus.DELIVERED)
    assertEquals("Delivered", viewModel.uiState.value.orders.first { it.id == order.id }.status.title)
  }

  @Test
  fun `verify past orders history and reorder functionality`() {
    val viewModel = GroceryViewModel()
    val pastOrders = viewModel.uiState.value.orders
    assertTrue(pastOrders.size >= 2)

    val targetOrder = pastOrders.first()
    val totalItemsInOrder = targetOrder.items.sumOf { it.quantity }

    // Execute re-order
    viewModel.reorder(targetOrder)

    // Verify cart now contains the items from the target order
    assertTrue(viewModel.uiState.value.cart.isNotEmpty())
    assertEquals(totalItemsInOrder, viewModel.uiState.value.cartItemCount)
    assertTrue(viewModel.uiState.value.userFeedbackMessage?.contains(targetOrder.id) == true)
  }

  @Test
  fun `verify estimated delivery time calculation varies by order size and time of day`() {
    val cal = java.util.Calendar.getInstance()

    // 1. Afternoon off-peak time (2:00 PM / 14:00)
    cal.set(java.util.Calendar.HOUR_OF_DAY, 14)
    cal.set(java.util.Calendar.MINUTE, 0)
    val afternoonMillis = cal.timeInMillis

    // Small order (1 item): Base 12 + Pack 1 + Traffic 1 = 14 mins (14-18 mins)
    val smallOrderOffPeak = com.example.model.calculateEstimatedDeliveryTime(itemCount = 1, currentTimeMillis = afternoonMillis)
    assertEquals(14, smallOrderOffPeak.minMinutes)
    assertEquals(18, smallOrderOffPeak.maxMinutes)
    assertEquals("14–18 mins", smallOrderOffPeak.rangeString)
    assertTrue(smallOrderOffPeak.orderSizeContext.contains("Quick Pack"))
    assertTrue(smallOrderOffPeak.trafficContext.contains("Clear Valley"))

    // Large order (15 items): Base 12 + Pack 9 + Traffic 1 = 22 mins (22-26 mins)
    val largeOrderOffPeak = com.example.model.calculateEstimatedDeliveryTime(itemCount = 15, currentTimeMillis = afternoonMillis)
    assertEquals(22, largeOrderOffPeak.minMinutes)
    assertEquals(26, largeOrderOffPeak.maxMinutes)
    assertEquals("22–26 mins", largeOrderOffPeak.rangeString)
    assertTrue(largeOrderOffPeak.orderSizeContext.contains("Bulky Order"))

    // 2. Evening rush hour on NH-154 (6:30 PM / 18:30)
    cal.set(java.util.Calendar.HOUR_OF_DAY, 18)
    cal.set(java.util.Calendar.MINUTE, 30)
    val eveningRushMillis = cal.timeInMillis

    // Small order (2 items) during evening rush: Base 12 + Pack 1 + Traffic 6 = 19 mins (19-23 mins)
    val rushOrder = com.example.model.calculateEstimatedDeliveryTime(itemCount = 2, currentTimeMillis = eveningRushMillis)
    assertEquals(19, rushOrder.minMinutes)
    assertEquals(23, rushOrder.maxMinutes)
    assertTrue(rushOrder.isRushHour)
    assertTrue(rushOrder.trafficContext.contains("Evening Peak Rush"))

    // 3. ViewModel dynamic ETA updates as items are added to cart
    val viewModel = GroceryViewModel()
    val initialEta = viewModel.uiState.value.estimatedDeliveryInfo
    val firstItem = viewModel.uiState.value.catalogue.first()

    viewModel.addToCart(firstItem)
    val updatedEta = viewModel.uiState.value.estimatedDeliveryInfo
    assertTrue(updatedEta.rangeString.isNotEmpty())
    assertTrue(updatedEta.timeWindowString.startsWith("Arriving by"))
  }

  @Test
  fun `verify SecurityUtils hashes PINs and rejects incorrect ones without storing plaintext`() {
    val hash = com.example.util.SecurityUtils.hashPin("7788")
    // Hash is non-empty and not equal to plaintext
    assertTrue(hash.length == 64)
    org.junit.Assert.assertNotEquals("7788", hash)

    // Verification succeeds with correct PIN
    assertTrue(com.example.util.SecurityUtils.verifyPin("7788", hash))

    // Verification fails with incorrect PIN
    org.junit.Assert.assertFalse(com.example.util.SecurityUtils.verifyPin("0000", hash))
    org.junit.Assert.assertFalse(com.example.util.SecurityUtils.verifyPin("", hash))
  }

  @Test
  fun `verify ProductCategory covers all 12 requested vendor categories`() {
    val categories = com.example.model.ProductCategory.entries.map { it.id }
    assertTrue(categories.contains("FOOD"))
    assertTrue(categories.contains("RESTAURANTS"))
    assertTrue(categories.contains("GROCERY"))
    assertTrue(categories.contains("FRUITS"))
    assertTrue(categories.contains("VEGETABLES"))
    assertTrue(categories.contains("DAIRY"))
    assertTrue(categories.contains("DRINKS"))
    assertTrue(categories.contains("HOUSEHOLD"))
    assertTrue(categories.contains("FASHION"))
    assertTrue(categories.contains("ELECTRONICS"))
    assertTrue(categories.contains("STATIONERY"))
    assertTrue(categories.contains("HARDWARE"))
  }

  @Test
  fun `verify multi-vendor shop registry contains Shahpur shops and owner`() {
    val shops = com.example.model.Shop.defaultShops
    assertTrue(shops.size >= 8)
    val centralHub = shops.first { it.id == "shahpur-central-hub" }
    assertEquals(com.example.model.Shop.PLATFORM_OWNER_EMAIL, centralHub.adminEmail)
    assertEquals("shavuak111@gmail.com", com.example.model.Shop.PLATFORM_OWNER_EMAIL)
  }

  @Test
  fun `verify FirestoreOrder to Order mapping preserves status and details`() {
    val fOrder = com.example.model.FirestoreOrder(
      orderId = "SPG-9988",
      customerId = "google_uid_12345",
      customerName = "Rajat Test",
      deliveryAddress = "Shahpur Bazaar, Kangra HP",
      totalAmount = 299.0,
      status = "ON_THE_WAY",
      shopId = "shahpur-central-hub",
      shopName = "Shahpur Central Mart & Hub"
    )
    val converted = with(fOrder) {
      toOrder()
    }
    assertEquals("SPG-9988", converted.id)
    assertEquals(com.example.model.OrderStatus.OUT_FOR_DELIVERY, converted.status)
    assertEquals(299.0, converted.total, 0.01)
    assertEquals("Shahpur Central Mart & Hub", converted.shopName)
  }

  @Test
  fun `verify UserAccount Google authentication state defaults and mutation`() {
    val initialUser = com.example.model.UserAccount()
    org.junit.Assert.assertFalse(initialUser.isGoogleSignedIn)
    org.junit.Assert.assertNull(initialUser.photoUrl)
    org.junit.Assert.assertNull(initialUser.uid)

    val googleUser = initialUser.copy(
      uid = "google_user_789",
      name = "Amit Kumar",
      email = "amit.shahpur@gmail.com",
      photoUrl = "https://example.com/avatar.jpg",
      isGoogleSignedIn = true
    )
    assertTrue(googleUser.isGoogleSignedIn)
    assertEquals("google_user_789", googleUser.uid)
    assertEquals("amit.shahpur@gmail.com", googleUser.email)
    assertEquals("https://example.com/avatar.jpg", googleUser.photoUrl)
  }
}

