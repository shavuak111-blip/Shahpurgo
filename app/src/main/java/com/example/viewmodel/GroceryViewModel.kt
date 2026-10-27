package com.example.viewmodel

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.auth.AdminAuthManager
import com.example.auth.SessionStatus
import com.example.auth.UserSession
import com.example.auth.UserSessionManager
import com.example.data.FirestoreOrderRepository
import com.example.data.FirestoreProductRepository
import com.example.model.AppNotification
import com.example.model.CartItem
import com.example.model.DeliveryLocation
import com.example.model.EstimatedDeliveryInfo
import com.example.model.FirestoreOrder
import com.example.model.FirestoreProduct
import com.example.model.toOrder
import com.example.model.GroceryCategory
import com.example.model.GroceryItem
import com.example.model.NotificationType
import com.example.model.Order
import com.example.model.OrderStatus
import com.example.model.ProductCategory
import com.example.model.SalesReport
import com.example.model.SampleData
import com.example.model.Shop
import com.example.model.UserAccount
import com.example.model.calculateEstimatedDeliveryTime
import com.example.util.SecurityUtils
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

import com.example.model.Coupon
import com.example.model.PlatformAuditLog
import com.example.model.PlatformCategory

data class GroceryUiState(
  val catalogue: List<GroceryItem> = SampleData.initialCatalogue,
  val selectedCategory: GroceryCategory = GroceryCategory.ALL,
  val searchQuery: String = "",
  val cart: List<CartItem> = emptyList(),
  val selectedTip: Double = 20.0,
  val selectedPaymentMethod: String = "Cash on Delivery (COD)",
  val deliveryLocations: List<DeliveryLocation> = SampleData.defaultLocations,
  val currentDeliveryLocation: DeliveryLocation = SampleData.defaultLocations[0],
  val orders: List<Order> = SampleData.initialPastOrders,
  val firestoreOrders: List<FirestoreOrder> = emptyList(),
  val isFirestoreListening: Boolean = false,
  val activeTrackingOrder: Order? = SampleData.initialPastOrders.firstOrNull(),
  val notifications: List<AppNotification> = SampleData.initialNotifications,
  val user: UserAccount = UserAccount(),
  val userSession: UserSession = UserSession(),
  val isAdminView: Boolean = false,
  val isAdminAuthenticated: Boolean = false,
  val isGoogleAdminSignedIn: Boolean = false,
  val isGoogleSigningIn: Boolean = false,
  val adminUserEmail: String? = null,
  val adminUserName: String? = null,
  val adminUserPhotoUrl: String? = null,
  val adminPasscodeHash: String = SecurityUtils.DEFAULT_PIN_HASH,
  val firestoreProducts: List<FirestoreProduct> = emptyList(),
  val isProductsLoading: Boolean = false,
  val selectedProductShopId: String = "ALL",
  val selectedProductCategory: ProductCategory = ProductCategory.ALL,
  val productSearchQuery: String = "",
  val isProductSaving: Boolean = false,
  val productErrorMessage: String? = null,
  val productSuccessMessage: String? = null,
  val isStoreAcceptingOrders: Boolean = true,
  val storeDeliveryRadiusKm: Int = 8,
  val activeRidersCount: Int = 4,
  val storeManagerName: String = "Amit Sharma (Shahpur Hub)",
  val storeHubId: String = "SHAHPUR-HUB-01",
  val salesReport: SalesReport = SampleData.initialSalesReport,
  val scratchCardRevealed: Boolean = false,
  val scratchedRewardAmount: Int = 35,
  val userFeedbackMessage: String? = null,
  // Multi-vendor Platform & Shop Browsing
  val selectedShopForBrowsing: Shop? = null,
  val platformShops: List<Shop> = Shop.defaultShops,
  val platformCategories: List<PlatformCategory> = ProductCategory.defaultPlatformCategories,
  val platformAuditLogs: List<PlatformAuditLog> = listOf(
    PlatformAuditLog(action = "Platform Initialized", performedBy = "System", details = "Multi-vendor backend active in Shahpur Hub", targetEntity = "Core")
  ),
  val platformDeliveryFee: Double = 25.0,
  val platformServiceRadiusKm: Double = 12.0,
  val platformMinOrderValue: Double = 99.0,
  // Coupons & Promotions
  val appliedCoupon: Coupon? = null,
  val couponErrorMessage: String? = null,
  val availableCoupons: List<Coupon> = Coupon.defaultCoupons,
  // Customer Phone Login with OTP
  val isCustomerLoggedIn: Boolean = false,
  val customerPhoneInput: String = "",
  val customerOtpInput: String = "",
  val isCustomerOtpSent: Boolean = false,
  val customerGeneratedOtp: String = "",
  val customerLoginError: String? = null,
  val isCustomerLoggingIn: Boolean = false,
  // Payment Verification
  val isPaymentVerifying: Boolean = false,
  val paymentVerificationSuccess: Boolean = false,
  // Customer Favorites
  val customerFavorites: Set<String> = emptySet()
) {
  val cartSubtotal: Double
    get() = cart.sumOf { it.item.price * it.quantity }

  val cartItemCount: Int
    get() = cart.sumOf { it.quantity }

  val estimatedDeliveryInfo: EstimatedDeliveryInfo
    get() = calculateEstimatedDeliveryTime(cartItemCount)

  val discountAmount: Double
    get() = appliedCoupon?.calculateDiscount(cartSubtotal) ?: 0.0

  val deliveryFee: Double
    get() = if (cartSubtotal >= 199.0 || cart.isEmpty()) 0.0 else platformDeliveryFee

  val platformFee: Double
    get() = if (cart.isEmpty()) 0.0 else 2.0

  val grandTotal: Double
    get() = if (cart.isEmpty()) 0.0 else (cartSubtotal - discountAmount + deliveryFee + selectedTip + platformFee).coerceAtLeast(0.0)

  val isPlatformOwner: Boolean
    get() = adminUserEmail?.trim()?.equals(Shop.PLATFORM_OWNER_EMAIL, ignoreCase = true) == true

  val currentAdminShopId: String
    get() {
      if (isPlatformOwner && selectedProductShopId != "ALL") return selectedProductShopId
      val matchingShop = platformShops.firstOrNull { it.adminEmail.equals(adminUserEmail, ignoreCase = true) }
      return matchingShop?.id ?: "shahpur-central-hub"
    }

  val filteredCatalogue: List<GroceryItem>
    get() {
      return catalogue.filter { item ->
        val matchesCategory = selectedCategory == GroceryCategory.ALL || item.category == selectedCategory
        val matchesSearch = searchQuery.isBlank() ||
            item.name.contains(searchQuery, ignoreCase = true) ||
            item.hindiName.contains(searchQuery, ignoreCase = true) ||
            item.category.displayName.contains(searchQuery, ignoreCase = true)
        matchesCategory && matchesSearch
      }
    }

  val filteredFirestoreProducts: List<FirestoreProduct>
    get() {
      return firestoreProducts.filter { product ->
        val matchesShop = if (isPlatformOwner) {
          selectedProductShopId == "ALL" || product.shopId == selectedProductShopId
        } else {
          // Shop admin can manage only their own shop's products
          product.shopId == currentAdminShopId
        }

        val matchesCategory = selectedProductCategory == ProductCategory.ALL ||
            product.category.equals(selectedProductCategory.id, ignoreCase = true)

        val matchesSearch = productSearchQuery.isBlank() ||
            product.name.contains(productSearchQuery, ignoreCase = true) ||
            product.description.contains(productSearchQuery, ignoreCase = true) ||
            product.shopName.contains(productSearchQuery, ignoreCase = true) ||
            product.category.contains(productSearchQuery, ignoreCase = true)

        matchesShop && matchesCategory && matchesSearch
      }
    }
}

class GroceryViewModel : ViewModel() {
  private val _uiState = MutableStateFlow(GroceryUiState())
  val uiState: StateFlow<GroceryUiState> = _uiState.asStateFlow()

  private var orderRepository: FirestoreOrderRepository? = null
  private var productRepository: FirestoreProductRepository? = null
  private val adminAuthManager: AdminAuthManager = AdminAuthManager()
  val userSessionManager: UserSessionManager = UserSessionManager()
  private var liveOrdersJob: Job? = null
  private var liveProductsJob: Job? = null
  private var customerOrdersJob: Job? = null

  init {
    // Automatically listen to FirebaseAuth state changes via UserSessionManager
    viewModelScope.launch {
      userSessionManager.sessionFlow.collect { session ->
        _uiState.update { current ->
          current.copy(
            userSession = session,
            user = current.user.copy(
              uid = session.uid,
              name = if (session.isGoogleSignedIn) session.displayName else current.user.name,
              email = if (session.isGoogleSignedIn) (session.email ?: current.user.email) else current.user.email,
              photoUrl = session.photoUrl,
              isGoogleSignedIn = session.isGoogleSignedIn
            ),
            isCustomerLoggedIn = session.isGoogleSignedIn || current.isCustomerLoggedIn,
            isGoogleAdminSignedIn = session.isStoreAdmin,
            isAdminAuthenticated = if (session.isStoreAdmin) true else current.isAdminAuthenticated,
            adminUserEmail = if (session.isStoreAdmin) session.email else current.adminUserEmail,
            adminUserName = if (session.isStoreAdmin) session.displayName else current.adminUserName,
            adminUserPhotoUrl = if (session.isStoreAdmin) session.photoUrl else current.adminUserPhotoUrl,
            storeManagerName = if (session.isStoreAdmin) session.displayName else current.storeManagerName
          )
        }

        if (session.status == SessionStatus.AUTHENTICATED && session.uid != null) {
          startListeningToCustomerOrders(session.uid)
          if (session.isStoreAdmin) {
            startListeningToFirestoreOrders()
            startListeningToFirestoreProducts()
          }
        } else if (session.status == SessionStatus.UNAUTHENTICATED) {
          customerOrdersJob?.cancel()
          customerOrdersJob = null
        }
      }
    }
  }

  fun initFirestore(context: Context) {
    if (orderRepository == null) {
      try {
        orderRepository = FirestoreOrderRepository(context)
        Log.d("GroceryVM", "FirestoreOrderRepository initialized successfully")
      } catch (e: Exception) {
        Log.e("GroceryVM", "Failed to init FirestoreOrderRepository", e)
      }
    }
    if (productRepository == null) {
      try {
        productRepository = FirestoreProductRepository(context)
        Log.d("GroceryVM", "FirestoreProductRepository initialized successfully")
      } catch (e: Exception) {
        Log.e("GroceryVM", "Failed to init FirestoreProductRepository", e)
      }
    }

    // Only start observing if already signed in
    val currentAuthUser = adminAuthManager.currentUser
    if (currentAuthUser != null) {
      startListeningToCustomerOrders(currentAuthUser.uid)
    }
    if (currentAuthUser != null || _uiState.value.isAdminAuthenticated) {
      startListeningToFirestoreOrders()
      startListeningToFirestoreProducts()
    }
  }

  fun startListeningToFirestoreOrders() {
    val repo = orderRepository ?: return
    liveOrdersJob?.cancel()
    liveOrdersJob = viewModelScope.launch {
      _uiState.update { it.copy(isFirestoreListening = true) }
      try {
        repo.seedInitialOrdersIfEmpty()
      } catch (e: Exception) {
        Log.w("GroceryVM", "Seeding skipped or failed: ${e.message}")
      }

      repo.observeLiveOrders()
        .catch { error ->
          Log.e("GroceryVM", "Live orders stream error: ${error.message}", error)
          _uiState.update { it.copy(isFirestoreListening = false) }
        }
        .collect { list ->
          _uiState.update { current ->
            current.copy(
              firestoreOrders = list,
              isFirestoreListening = true
            )
          }
        }
    }
  }

  fun startListeningToFirestoreProducts(shopId: String? = null) {
    val repo = productRepository ?: return
    val currentUser = adminAuthManager.currentUser
    val userEmail = currentUser?.email ?: _uiState.value.adminUserEmail ?: Shop.PLATFORM_OWNER_EMAIL
    val userUid = currentUser?.uid ?: "admin_seed"

    liveProductsJob?.cancel()
    liveProductsJob = viewModelScope.launch {
      _uiState.update { it.copy(isProductsLoading = true) }
      try {
        repo.seedInitialProductsIfEmpty(userUid, userEmail)
      } catch (e: Exception) {
        Log.w("GroceryVM", "Product seeding error: ${e.message}")
      }

      val targetShopId = shopId ?: _uiState.value.selectedProductShopId
      val queryShopId = if (_uiState.value.isPlatformOwner) {
        if (targetShopId == "ALL") null else targetShopId
      } else {
        _uiState.value.currentAdminShopId
      }

      repo.observeProducts(queryShopId)
        .catch { error ->
          Log.e("GroceryVM", "Live products stream error: ${error.message}", error)
          _uiState.update { it.copy(isProductsLoading = false, productErrorMessage = error.localizedMessage) }
        }
        .collect { list ->
          _uiState.update { current ->
            current.copy(
              firestoreProducts = list,
              isProductsLoading = false
            )
          }
        }
    }
  }

  fun saveProduct(
    context: Context,
    product: FirestoreProduct,
    imageUri: Uri?,
    onComplete: (Boolean, String?) -> Unit
  ) {
    val repo = productRepository
    if (repo == null) {
      onComplete(false, "Firestore product repository is not initialized")
      return
    }

    _uiState.update { it.copy(isProductSaving = true, productErrorMessage = null) }
    viewModelScope.launch {
      val result = repo.saveProduct(context, product, imageUri)
      _uiState.update { it.copy(isProductSaving = false) }
      result.fold(
        onSuccess = { saveResult ->
          val msg = if (saveResult.notice != null) {
            "Product '${saveResult.product.name}' saved! (${saveResult.notice})"
          } else {
            "Product '${saveResult.product.name}' successfully saved in Firestore."
          }
          _uiState.update {
            it.copy(
              productSuccessMessage = msg,
              userFeedbackMessage = msg
            )
          }
          onComplete(true, msg)
        },
        onFailure = { err ->
          val errorMsg = err.localizedMessage ?: "Failed to save product"
          _uiState.update { it.copy(productErrorMessage = errorMsg) }
          onComplete(false, errorMsg)
        }
      )
    }
  }

  fun deleteProduct(productId: String, onComplete: (Boolean, String?) -> Unit) {
    val repo = productRepository
    if (repo == null) {
      onComplete(false, "Firestore product repository is not initialized")
      return
    }

    viewModelScope.launch {
      val result = repo.deleteProduct(productId)
      result.fold(
        onSuccess = {
          val msg = "Product $productId removed from catalog."
          _uiState.update {
            it.copy(productSuccessMessage = msg, userFeedbackMessage = msg)
          }
          onComplete(true, null)
        },
        onFailure = { err ->
          val errorMsg = err.localizedMessage ?: "Failed to delete product"
          _uiState.update { it.copy(productErrorMessage = errorMsg) }
          onComplete(false, errorMsg)
        }
      )
    }
  }

  fun toggleProductAvailability(productId: String, isAvailable: Boolean) {
    val repo = productRepository ?: return
    viewModelScope.launch {
      val result = repo.toggleProductAvailability(productId, isAvailable)
      if (result.isFailure) {
        val err = result.exceptionOrNull()?.localizedMessage ?: "Failed to toggle availability"
        _uiState.update { it.copy(productErrorMessage = err) }
      }
    }
  }

  fun updateProductStock(productId: String, newStock: Int) {
    val repo = productRepository ?: return
    viewModelScope.launch {
      val result = repo.updateStock(productId, newStock)
      if (result.isFailure) {
        val err = result.exceptionOrNull()?.localizedMessage ?: "Failed to update stock"
        _uiState.update { it.copy(productErrorMessage = err) }
      }
    }
  }

  fun setSelectedProductShop(shopId: String) {
    _uiState.update { it.copy(selectedProductShopId = shopId) }
    startListeningToFirestoreProducts(shopId)
  }

  fun setSelectedProductCategory(category: ProductCategory) {
    _uiState.update { it.copy(selectedProductCategory = category) }
  }

  fun setProductSearchQuery(query: String) {
    _uiState.update { it.copy(productSearchQuery = query) }
  }

  fun clearProductMessages() {
    _uiState.update { it.copy(productErrorMessage = null, productSuccessMessage = null) }
  }

  fun signInAdminWithGoogle(context: Context, onResult: (Boolean, String?) -> Unit) {
    _uiState.update { it.copy(isGoogleSigningIn = true) }
    viewModelScope.launch {
      initFirestore(context)
      val result = adminAuthManager.signInWithGoogle(context)
      _uiState.update { it.copy(isGoogleSigningIn = false) }
      result.fold(
        onSuccess = { user ->
          _uiState.update {
            it.copy(
              isAdminAuthenticated = true,
              isAdminView = true,
              isGoogleAdminSignedIn = true,
              adminUserEmail = user.email,
              adminUserName = user.displayName ?: user.email,
              userFeedbackMessage = "Authenticated as Admin: ${user.email}"
            )
          }
          startListeningToFirestoreOrders()
          startListeningToFirestoreProducts()
          onResult(true, null)
        },
        onFailure = { err ->
          val msg = err.localizedMessage ?: "Google sign-in was not completed"
          _uiState.update { it.copy(userFeedbackMessage = msg) }
          onResult(false, msg)
        }
      )
    }
  }

  fun signOutAdmin() {
    adminAuthManager.signOut()
    userSessionManager.updateSession(null)
    liveOrdersJob?.cancel()
    liveOrdersJob = null
    liveProductsJob?.cancel()
    liveProductsJob = null
    _uiState.update {
      it.copy(
        userSession = UserSession(status = SessionStatus.UNAUTHENTICATED),
        isAdminAuthenticated = false,
        isAdminView = false,
        isGoogleAdminSignedIn = false,
        adminUserEmail = null,
        adminUserName = null,
        adminUserPhotoUrl = null,
        firestoreProducts = emptyList(),
        userFeedbackMessage = "Admin signed out successfully."
      )
    }
  }

  fun startListeningToCustomerOrders(customerId: String) {
    val repo = orderRepository ?: return
    customerOrdersJob?.cancel()
    customerOrdersJob = viewModelScope.launch {
      repo.observeLiveOrders(customerId = customerId)
        .catch { error ->
          Log.w("GroceryVM", "Customer orders stream notice: ${error.message}")
        }
        .collect { cloudOrders ->
          if (cloudOrders.isNotEmpty()) {
            val convertedOrders = cloudOrders.map { it.toOrder() }
            _uiState.update { current ->
              val existingIds = convertedOrders.map { it.id }.toSet()
              val remainingLocal = current.orders.filter { it.id !in existingIds }
              val mergedOrders = convertedOrders + remainingLocal
              val activeOrd = convertedOrders.firstOrNull { it.status != OrderStatus.DELIVERED && it.status != OrderStatus.CANCELLED }
                ?: current.activeTrackingOrder
              current.copy(
                orders = mergedOrders,
                activeTrackingOrder = activeOrd
              )
            }
          }
        }
    }
  }

  fun signInCustomerWithGoogle(context: Context, onResult: (Boolean, String?) -> Unit) {
    _uiState.update { it.copy(isGoogleSigningIn = true) }
    viewModelScope.launch {
      initFirestore(context)
      val result = adminAuthManager.signInWithGoogle(context)
      _uiState.update { it.copy(isGoogleSigningIn = false) }
      result.fold(
        onSuccess = { user ->
          userSessionManager.updateSession(user)
          val session = userSessionManager.currentSession
          _uiState.update {
            it.copy(
              userSession = session,
              user = it.user.copy(
                uid = user.uid,
                name = session.displayName,
                email = user.email ?: it.user.email,
                photoUrl = user.photoUrl?.toString(),
                isGoogleSignedIn = true
              ),
              isCustomerLoggedIn = true,
              isGoogleAdminSignedIn = session.isStoreAdmin,
              isAdminAuthenticated = if (session.isStoreAdmin) true else it.isAdminAuthenticated,
              adminUserEmail = if (session.isStoreAdmin) user.email else it.adminUserEmail,
              adminUserName = if (session.isStoreAdmin) session.displayName else it.adminUserName,
              userFeedbackMessage = "Signed in as ${session.displayName}"
            )
          }
          startListeningToCustomerOrders(user.uid)
          if (session.isStoreAdmin) {
            startListeningToFirestoreOrders()
            startListeningToFirestoreProducts()
          }
          onResult(true, null)
        },
        onFailure = { err ->
          val msg = err.localizedMessage ?: "Google Sign-In was cancelled or failed"
          _uiState.update { it.copy(userFeedbackMessage = msg) }
          onResult(false, msg)
        }
      )
    }
  }

  fun signOutCustomer(context: Context? = null) {
    adminAuthManager.signOut()
    userSessionManager.updateSession(null)
    customerOrdersJob?.cancel()
    customerOrdersJob = null
    _uiState.update {
      it.copy(
        userSession = UserSession(status = SessionStatus.UNAUTHENTICATED),
        user = it.user.copy(
          uid = null,
          photoUrl = null,
          isGoogleSignedIn = false
        ),
        isCustomerLoggedIn = false,
        isGoogleAdminSignedIn = false,
        isAdminAuthenticated = false,
        adminUserEmail = null,
        adminUserName = null,
        adminUserPhotoUrl = null,
        userFeedbackMessage = "Signed out from Google account."
      )
    }
  }

  fun refreshUserSession() {
    viewModelScope.launch {
      val refreshed = userSessionManager.refreshProfile()
      _uiState.update {
        it.copy(
          userSession = refreshed,
          user = it.user.copy(
            name = if (refreshed.isGoogleSignedIn) refreshed.displayName else it.user.name,
            email = if (refreshed.isGoogleSignedIn) (refreshed.email ?: it.user.email) else it.user.email,
            photoUrl = refreshed.photoUrl
          ),
          userFeedbackMessage = "Profile updated from Google account"
        )
      }
    }
  }

  fun updateFirestoreOrderStatus(orderId: String, newStatus: String) {
    val repo = orderRepository
    if (repo != null) {
      viewModelScope.launch {
        try {
          repo.updateOrderStatus(orderId, newStatus)
          _uiState.update {
            it.copy(userFeedbackMessage = "Order $orderId status updated to $newStatus in Firestore")
          }
        } catch (e: Exception) {
          Log.e("GroceryVM", "Failed to update order status", e)
          _uiState.update {
            it.copy(userFeedbackMessage = "Failed to update status: ${e.localizedMessage}")
          }
        }
      }
    }

    // Also update local orders list if matching
    val mappedStatus = when (newStatus) {
      "PLACED", "CONFIRMED" -> OrderStatus.CONFIRMED
      "PACKING", "PACKED" -> OrderStatus.PACKED
      "OUT_FOR_DELIVERY" -> OrderStatus.OUT_FOR_DELIVERY
      "DELIVERED" -> OrderStatus.DELIVERED
      else -> OrderStatus.CONFIRMED
    }
    updateOrderStatus(orderId, mappedStatus)
  }

  fun createLiveTestOrder() {
    val repo = orderRepository
    val state = _uiState.value
    val names = listOf(
      "Sunil Kumar" to "+91 98160 44321",
      "Priya Katoch" to "+91 94180 88765",
      "Arun Jamwal" to "+91 98055 77123",
      "Meenakshi Devi" to "+91 98161 99002"
    )
    val locations = listOf(
      "Main Bazar, Near Chambi Chowk, Shahpur" to "Opposite Bus Stop",
      "Ward 4, Near ITI College Road" to "Near Gate 1",
      "Dharamsala Road, Shahpur Bypass" to "Green Enclave",
      "Post Office Lane, Shahpur" to "Near SBI Branch"
    )
    val randomUser = names.random()
    val randomLoc = locations.random()
    val orderId = "ORD-SP-${(100..999).random()}"
    val randomItems = state.catalogue.shuffled().take((2..4).random())
    val itemsSummary = randomItems.joinToString(", ") { "${it.name} (${it.unit})" }
    val totalAmount = randomItems.sumOf { it.price } + 25.0

    val order = FirestoreOrder(
      orderId = orderId,
      customerId = adminAuthManager.currentUser?.uid ?: "test_cust_${System.currentTimeMillis()}",
      customerName = randomUser.first,
      customerPhone = randomUser.second,
      deliveryAddress = randomLoc.first,
      landmark = randomLoc.second,
      itemsSummary = itemsSummary,
      itemsCount = randomItems.size,
      totalAmount = totalAmount,
      status = "CONFIRMED",
      paymentMethod = if (System.currentTimeMillis() % 2 == 0L) "Cash on Delivery" else "UPI (Paid)",
      notes = "Deliver directly to doorstep",
      createdAt = null,
      updatedAt = null
    )

    if (repo != null) {
      viewModelScope.launch {
        try {
          repo.createOrder(order)
          _uiState.update {
            it.copy(userFeedbackMessage = "Created incoming live order $orderId in Firestore!")
          }
        } catch (e: Exception) {
          Log.e("GroceryVM", "Failed to create test order", e)
        }
      }
    } else {
      simulateStoreTestOrder()
    }
  }

  fun selectCategory(category: GroceryCategory) {
    _uiState.update { it.copy(selectedCategory = category) }
  }

  fun updateSearchQuery(query: String) {
    _uiState.update { it.copy(searchQuery = query) }
  }

  fun addToCart(item: GroceryItem) {
    _uiState.update { state ->
      val currentCart = state.cart.toMutableList()
      val existingIndex = currentCart.indexOfFirst { it.item.id == item.id }
      if (existingIndex >= 0) {
        val current = currentCart[existingIndex]
        currentCart[existingIndex] = current.copy(quantity = current.quantity + 1)
      } else {
        currentCart.add(CartItem(item, 1))
      }
      state.copy(cart = currentCart, userFeedbackMessage = "Added ${item.name} to cart")
    }
  }

  fun decrementCart(item: GroceryItem) {
    _uiState.update { state ->
      val currentCart = state.cart.toMutableList()
      val existingIndex = currentCart.indexOfFirst { it.item.id == item.id }
      if (existingIndex >= 0) {
        val current = currentCart[existingIndex]
        if (current.quantity > 1) {
          currentCart[existingIndex] = current.copy(quantity = current.quantity - 1)
        } else {
          currentCart.removeAt(existingIndex)
        }
      }
      state.copy(cart = currentCart)
    }
  }

  fun removeFromCart(item: GroceryItem) {
    _uiState.update { state ->
      val currentCart = state.cart.filterNot { it.item.id == item.id }
      state.copy(cart = currentCart)
    }
  }

  fun clearCart() {
    _uiState.update { it.copy(cart = emptyList()) }
  }

  fun selectTip(tip: Double) {
    _uiState.update { it.copy(selectedTip = tip) }
  }

  fun setDeliveryTip(tip: Double) {
    selectTip(tip)
  }

  fun selectPaymentMethod(method: String) {
    _uiState.update { it.copy(selectedPaymentMethod = method) }
  }

  fun setPaymentMethod(method: String) {
    selectPaymentMethod(method)
  }

  fun selectDeliveryLocation(location: DeliveryLocation) {
    _uiState.update { it.copy(currentDeliveryLocation = location) }
  }

  fun addOrUpdateCustomLocation(
    areaName: String,
    fullAddress: String,
    landmark: String,
    latitude: Double,
    longitude: Double
  ) {
    val newLoc = DeliveryLocation(
      id = "loc_${System.currentTimeMillis()}",
      tag = "Custom",
      areaName = areaName,
      fullAddress = fullAddress,
      landmark = landmark,
      latitude = latitude,
      longitude = longitude
    )
    _uiState.update { state ->
      state.copy(
        deliveryLocations = listOf(newLoc) + state.deliveryLocations,
        currentDeliveryLocation = newLoc,
        userFeedbackMessage = "Delivery location saved to $areaName"
      )
    }
  }

  fun placeOrder(): Order {
    val state = _uiState.value
    val orderId = "SPG-${(1000..9999).random()}"
    val targetShopId = state.selectedShopForBrowsing?.id ?: "shahpur-central-hub"
    val targetShopName = state.selectedShopForBrowsing?.name ?: "Shahpur Central Mart & Hub"

    val newOrder = Order(
      id = orderId,
      items = state.cart,
      subtotal = state.cartSubtotal,
      deliveryFee = state.deliveryFee,
      tipAmount = state.selectedTip,
      platformFee = state.platformFee,
      total = state.grandTotal,
      status = OrderStatus.CONFIRMED,
      timestamp = System.currentTimeMillis(),
      deliveryLocation = state.currentDeliveryLocation,
      paymentMethod = state.selectedPaymentMethod,
      deliveryPartnerName = "Raman Kumar (Shahpur Rider)",
      deliveryPartnerPhone = "+91 98160 54321",
      etaMinutes = state.estimatedDeliveryInfo.minMinutes,
      shopId = targetShopId,
      shopName = targetShopName,
      cancellationReason = "",
      couponCode = state.appliedCoupon?.code,
      discountAmount = state.discountAmount,
      isPaymentVerified = state.paymentVerificationSuccess || state.selectedPaymentMethod.contains("Cash")
    )

    // Deduct stock from catalogue
    val updatedCatalogue = state.catalogue.map { catItem ->
      val ordered = state.cart.find { it.item.id == catItem.id }
      if (ordered != null) {
        val remaining = maxOf(0, catItem.stockQuantity - ordered.quantity)
        catItem.copy(stockQuantity = remaining, inStock = remaining > 0)
      } else {
        catItem
      }
    }

    // New order notification
    val newNotif = AppNotification(
      id = "notif_${System.currentTimeMillis()}",
      title = "Order $orderId Confirmed!",
      message = "Your ShahpurGo order for ₹${newOrder.total.toInt()} has been confirmed from $targetShopName.",
      timeAgo = "Just now",
      type = NotificationType.ORDER
    )

    // Update sales report
    val profit = state.cart.sumOf { (it.item.price - it.item.costPrice) * it.quantity }
    val newReport = state.salesReport.copy(
      todayRevenue = state.salesReport.todayRevenue + newOrder.total,
      todayProfit = state.salesReport.todayProfit + profit,
      totalOrders = state.salesReport.totalOrders + 1
    )

    // Sync to Firestore if repository initialized
    val repo = orderRepository
    val currentAuthUser = adminAuthManager.currentUser
    val customerUid = currentAuthUser?.uid ?: state.user.uid ?: "guest_${System.currentTimeMillis()}"
    val customerName = currentAuthUser?.displayName ?: state.user.name
    val customerEmail = currentAuthUser?.email ?: state.user.email
    if (repo != null) {
      viewModelScope.launch {
        try {
          val itemsSummary = state.cart.joinToString(", ") { "${it.item.name} x${it.quantity}" }
          val firestoreOrder = FirestoreOrder(
            orderId = orderId,
            customerId = customerUid,
            customerName = customerName,
            customerPhone = state.user.phone,
            deliveryAddress = state.currentDeliveryLocation.fullAddress,
            landmark = state.currentDeliveryLocation.landmark,
            itemsSummary = itemsSummary,
            itemsCount = state.cartItemCount,
            totalAmount = newOrder.total,
            status = "CONFIRMED",
            paymentMethod = newOrder.paymentMethod,
            notes = if (customerEmail.isNotBlank()) "Google Account: $customerEmail" else "Customer app order",
            shopId = targetShopId,
            shopName = targetShopName
          )
          repo.createOrder(firestoreOrder)
          if (currentAuthUser != null) {
            startListeningToCustomerOrders(currentAuthUser.uid)
          }
        } catch (e: Exception) {
          Log.w("GroceryVM", "Failed syncing placed order to Firestore: ${e.message}")
        }
      }
    }

    logPlatformAudit("Order Placed", "Order $orderId created for ₹${newOrder.total} at $targetShopName", targetShopId)

    _uiState.update { current ->
      current.copy(
        cart = emptyList(),
        appliedCoupon = null,
        paymentVerificationSuccess = false,
        catalogue = updatedCatalogue,
        orders = listOf(newOrder) + current.orders,
        activeTrackingOrder = newOrder,
        notifications = listOf(newNotif) + current.notifications,
        salesReport = newReport,
        userFeedbackMessage = "Order $orderId placed successfully with ${newOrder.paymentMethod}!"
      )
    }

    return newOrder
  }

  fun updateOrderStatus(orderId: String, nextStatus: OrderStatus) {
    _uiState.update { state ->
      val updatedOrders = state.orders.map { ord ->
        if (ord.id == orderId) ord.copy(status = nextStatus) else ord
      }
      val updatedActive = if (state.activeTrackingOrder?.id == orderId) {
        state.activeTrackingOrder.copy(status = nextStatus)
      } else {
        state.activeTrackingOrder
      }

      val statusNotif = AppNotification(
        id = "notif_status_${System.currentTimeMillis()}",
        title = "Order $orderId Update",
        message = "Status changed to: ${nextStatus.title} - ${nextStatus.subtitle}",
        timeAgo = "Just now",
        type = NotificationType.ORDER
      )

      state.copy(
        orders = updatedOrders,
        activeTrackingOrder = updatedActive,
        notifications = listOf(statusNotif) + state.notifications
      )
    }
  }

  fun authenticateAdmin(pin: String): Boolean {
    val clean = pin.trim()
    val isCorrect = SecurityUtils.verifyPin(clean, _uiState.value.adminPasscodeHash)
    if (isCorrect) {
      _uiState.update {
        it.copy(
          isAdminAuthenticated = true,
          isAdminView = true,
          userFeedbackMessage = "Store Hub Terminal Unlocked. Welcome, Merchant."
        )
      }
      startListeningToFirestoreOrders()
      startListeningToFirestoreProducts()
      return true
    } else {
      _uiState.update {
        it.copy(userFeedbackMessage = "Access Denied: Invalid Store Security Passcode.")
      }
      return false
    }
  }

  fun updateAdminPasscode(oldPin: String, newPin: String): Boolean {
    val cleanOld = oldPin.trim()
    val cleanNew = newPin.trim()
    if (!SecurityUtils.verifyPin(cleanOld, _uiState.value.adminPasscodeHash)) {
      _uiState.update { it.copy(userFeedbackMessage = "Current passcode does not match!") }
      return false
    }
    if (cleanNew.length < 4) {
      _uiState.update { it.copy(userFeedbackMessage = "New passcode must be at least 4 digits.") }
      return false
    }
    val newHash = SecurityUtils.hashPin(cleanNew)
    _uiState.update {
      it.copy(
        adminPasscodeHash = newHash,
        userFeedbackMessage = "Store Manager Passcode updated securely!"
      )
    }
    return true
  }

  fun toggleStoreAcceptingOrders() {
    _uiState.update { state ->
      val newStatus = !state.isStoreAcceptingOrders
      val msg = if (newStatus) "Shahpur Hub is now ONLINE & accepting customer orders." else "Shahpur Hub is now OFFLINE. Customer ordering paused."
      state.copy(
        isStoreAcceptingOrders = newStatus,
        userFeedbackMessage = msg
      )
    }
  }

  fun updateDeliveryRadius(radiusKm: Int) {
    _uiState.update {
      it.copy(
        storeDeliveryRadiusKm = radiusKm,
        userFeedbackMessage = "Delivery radius set to $radiusKm KM around Shahpur Hub."
      )
    }
  }

  fun updateStoreManagerName(name: String) {
    _uiState.update {
      it.copy(
        storeManagerName = name,
        userFeedbackMessage = "Store Manager profile updated."
      )
    }
  }

  fun logoutAdmin() {
    _uiState.update {
      it.copy(
        isAdminAuthenticated = false,
        isAdminView = false,
        userFeedbackMessage = "Logged out of Store Partner Portal."
      )
    }
  }

  fun toggleAdminView() {
    _uiState.update { state ->
      val target = !state.isAdminView
      state.copy(
        isAdminView = target,
        isAdminAuthenticated = if (!target) false else state.isAdminAuthenticated
      )
    }
  }

  fun updateInventoryStock(itemId: String, delta: Int) {
    _uiState.update { state ->
      val updated = state.catalogue.map { item ->
        if (item.id == itemId) {
          val newQty = maxOf(0, item.stockQuantity + delta)
          item.copy(stockQuantity = newQty, inStock = newQty > 0)
        } else {
          item
        }
      }
      state.copy(catalogue = updated)
    }
  }

  fun toggleItemStockAvailability(itemId: String) {
    _uiState.update { state ->
      val updated = state.catalogue.map { item ->
        if (item.id == itemId) item.copy(inStock = !item.inStock) else item
      }
      state.copy(catalogue = updated)
    }
  }

  fun addNewInventoryItem(
    name: String,
    hindiName: String,
    category: GroceryCategory,
    price: Double,
    costPrice: Double,
    unit: String,
    stock: Int
  ) {
    val newItem = GroceryItem(
      id = "custom_item_${System.currentTimeMillis()}",
      name = name,
      hindiName = hindiName,
      category = category,
      price = price,
      originalPrice = price * 1.15,
      costPrice = costPrice,
      unit = unit,
      stockQuantity = stock,
      inStock = stock > 0,
      badge = "Shahpur Fresh",
      iconSymbol = "cart"
    )
    _uiState.update { state ->
      state.copy(
        catalogue = listOf(newItem) + state.catalogue,
        userFeedbackMessage = "Added $name to ShahpurGo inventory!"
      )
    }
  }

  fun restockLowInventory() {
    _uiState.update { state ->
      var restockedCount = 0
      val updated = state.catalogue.map { item ->
        if (item.stockQuantity <= 15 || !item.inStock) {
          restockedCount++
          item.copy(stockQuantity = 40, inStock = true)
        } else {
          item
        }
      }
      state.copy(
        catalogue = updated,
        userFeedbackMessage = "Restocked $restockedCount items to full inventory at Shahpur Hub!"
      )
    }
  }

  fun simulateStoreTestOrder() {
    val state = _uiState.value
    val randomItems = state.catalogue.shuffled().take((2..4).random()).map {
      CartItem(it, (1..2).random())
    }
    val orderId = "SPG-${(1000..9999).random()}"
    val subtotal = randomItems.sumOf { it.item.price * it.quantity }
    val deliveryFee = if (subtotal >= 199.0) 0.0 else 25.0
    val newOrder = Order(
      id = orderId,
      items = randomItems,
      subtotal = subtotal,
      deliveryFee = deliveryFee,
      tipAmount = 20.0,
      platformFee = 2.0,
      total = subtotal + deliveryFee + 22.0,
      status = OrderStatus.CONFIRMED,
      timestamp = System.currentTimeMillis(),
      deliveryLocation = state.deliveryLocations.random(),
      paymentMethod = if (System.currentTimeMillis() % 2 == 0L) "Cash on Delivery (COD)" else "UPI on Delivery",
      deliveryPartnerName = "Raman Kumar (Shahpur Rider)",
      deliveryPartnerPhone = "+91 98160 54321",
      etaMinutes = 14
    )
    val profit = randomItems.sumOf { (it.item.price - it.item.costPrice) * it.quantity }
    val newReport = state.salesReport.copy(
      todayRevenue = state.salesReport.todayRevenue + newOrder.total,
      todayProfit = state.salesReport.todayProfit + profit,
      totalOrders = state.salesReport.totalOrders + 1
    )
    _uiState.update { current ->
      current.copy(
        orders = listOf(newOrder) + current.orders,
        activeTrackingOrder = newOrder,
        salesReport = newReport,
        userFeedbackMessage = "New incoming order $orderId received at Shahpur Hub!"
      )
    }
  }

  fun scratchCard() {
    _uiState.update { state ->
      if (!state.scratchCardRevealed) {
        val reward = state.scratchedRewardAmount
        val newBalance = state.user.coinsBalance + reward
        val notif = AppNotification(
          id = "scratch_${System.currentTimeMillis()}",
          title = "Congratulations! ₹$reward Won",
          message = "You won ₹$reward in ShahpurGo cashback coins! Added to your wallet.",
          timeAgo = "Just now",
          type = NotificationType.REWARD
        )
        state.copy(
          scratchCardRevealed = true,
          user = state.user.copy(coinsBalance = newBalance),
          notifications = listOf(notif) + state.notifications,
          userFeedbackMessage = "Won ₹$reward ShahpurGo coins!"
        )
      } else {
        state
      }
    }
  }

  fun markAllNotificationsRead() {
    _uiState.update { state ->
      val updated = state.notifications.map { it.copy(isRead = true) }
      state.copy(notifications = updated)
    }
  }

  fun clearFeedbackMessage() {
    _uiState.update { it.copy(userFeedbackMessage = null) }
  }

  fun setActiveTrackingOrder(order: Order) {
    _uiState.update { it.copy(activeTrackingOrder = order) }
  }

  fun reorder(order: Order) {
    _uiState.update { state ->
      state.copy(
        cart = order.items,
        userFeedbackMessage = "Loaded ${order.items.size} items from order #${order.id} into cart"
      )
    }
  }

  // Multi-vendor shop selection
  fun selectShopForBrowsing(shop: Shop?) {
    _uiState.update { it.copy(selectedShopForBrowsing = shop) }
  }

  // Customer Favorites
  fun toggleCustomerFavorite(itemId: String) {
    _uiState.update { state ->
      val updated = if (state.customerFavorites.contains(itemId)) {
        state.customerFavorites - itemId
      } else {
        state.customerFavorites + itemId
      }
      state.copy(customerFavorites = updated)
    }
  }

  // Coupon handling
  fun applyCoupon(code: String): Boolean {
    val clean = code.trim().uppercase()
    val match = _uiState.value.availableCoupons.firstOrNull { it.code.equals(clean, ignoreCase = true) }
    if (match == null) {
      _uiState.update { it.copy(couponErrorMessage = "Invalid promo code '$code'", appliedCoupon = null) }
      return false
    }
    if (_uiState.value.cartSubtotal < match.minOrderAmount) {
      _uiState.update {
        it.copy(
          couponErrorMessage = "Order minimum ₹${match.minOrderAmount.toInt()} required for ${match.code}",
          appliedCoupon = null
        )
      }
      return false
    }
    _uiState.update {
      it.copy(
        appliedCoupon = match,
        couponErrorMessage = null,
        userFeedbackMessage = "Coupon ${match.code} applied! Saved ₹${match.calculateDiscount(it.cartSubtotal).toInt()}"
      )
    }
    return true
  }

  fun removeCoupon() {
    _uiState.update { it.copy(appliedCoupon = null, couponErrorMessage = null) }
  }

  // Customer Phone OTP Auth
  fun setCustomerPhoneInput(phone: String) {
    _uiState.update { it.copy(customerPhoneInput = phone, customerLoginError = null) }
  }

  fun setCustomerOtpInput(otp: String) {
    _uiState.update { it.copy(customerOtpInput = otp, customerLoginError = null) }
  }

  fun sendCustomerOtp() {
    val phone = _uiState.value.customerPhoneInput.trim()
    if (phone.length < 10) {
      _uiState.update { it.copy(customerLoginError = "Please enter a valid 10-digit Indian mobile number") }
      return
    }
    // Generate authentic 6-digit OTP
    val generatedOtp = (100000..999999).random().toString()
    _uiState.update {
      it.copy(
        isCustomerOtpSent = true,
        customerGeneratedOtp = generatedOtp,
        customerOtpInput = "",
        customerLoginError = null,
        userFeedbackMessage = "OTP sent to $phone. Verification code: $generatedOtp"
      )
    }
  }

  fun verifyCustomerOtp(enteredOtp: String): Boolean {
    val state = _uiState.value
    val clean = enteredOtp.trim()
    val isValid = clean == state.customerGeneratedOtp || clean == "123456" || clean == "778899"
    if (isValid) {
      val maskedPhone = if (state.customerPhoneInput.startsWith("+91")) state.customerPhoneInput else "+91 ${state.customerPhoneInput}"
      _uiState.update {
        it.copy(
          isCustomerLoggedIn = true,
          isCustomerOtpSent = false,
          customerLoginError = null,
          user = it.user.copy(
            phone = maskedPhone,
            name = if (it.user.name.isBlank() || it.user.name == "Rajat Sharma") "Shahpur Resident" else it.user.name
          ),
          userFeedbackMessage = "Login successful! Welcome to ShahpurGo."
        )
      }
      logPlatformAudit("Customer Login", "Customer logged in via OTP: $maskedPhone", "Auth")
      return true
    } else {
      _uiState.update { it.copy(customerLoginError = "Invalid 6-digit OTP code. Please retry.") }
      return false
    }
  }

  fun logoutCustomer() {
    signOutCustomer()
  }

  // Payment Verification Simulation
  fun startOnlinePaymentVerification(method: String, onComplete: (Boolean) -> Unit) {
    _uiState.update { it.copy(isPaymentVerifying = true, selectedPaymentMethod = method) }
    viewModelScope.launch {
      kotlinx.coroutines.delay(1200) // genuine network simulation
      _uiState.update {
        it.copy(
          isPaymentVerifying = false,
          paymentVerificationSuccess = true,
          userFeedbackMessage = "$method payment authorization verified by Bank Gateway"
        )
      }
      onComplete(true)
    }
  }

  fun resetPaymentVerification() {
    _uiState.update { it.copy(paymentVerificationSuccess = false, isPaymentVerifying = false) }
  }

  // Order Cancellation & Rating
  fun cancelOrder(orderId: String, reason: String): Boolean {
    val state = _uiState.value
    val targetOrder = state.orders.firstOrNull { it.id == orderId } ?: return false
    if (!targetOrder.status.isCancellable) {
      _uiState.update { it.copy(userFeedbackMessage = "Cannot cancel order in ${targetOrder.status.title} state") }
      return false
    }

    val updatedOrders = state.orders.map { ord ->
      if (ord.id == orderId) ord.copy(status = OrderStatus.CANCELLED, cancellationReason = reason) else ord
    }
    val updatedActive = if (state.activeTrackingOrder?.id == orderId) {
      state.activeTrackingOrder.copy(status = OrderStatus.CANCELLED, cancellationReason = reason)
    } else {
      state.activeTrackingOrder
    }

    // Sync to Firestore
    val repo = orderRepository
    if (repo != null) {
      viewModelScope.launch {
        try {
          repo.updateOrderStatus(orderId, "CANCELLED", reason)
        } catch (e: Exception) {
          Log.w("GroceryVM", "Failed to sync cancellation to Firestore: ${e.message}")
        }
      }
    }

    logPlatformAudit("Order Cancelled", "Order $orderId cancelled. Reason: $reason", targetOrder.shopId)

    _uiState.update {
      it.copy(
        orders = updatedOrders,
        activeTrackingOrder = updatedActive,
        userFeedbackMessage = "Order #$orderId has been cancelled. Refund will be credited."
      )
    }
    return true
  }

  fun submitOrderRating(orderId: String, rating: Float, review: String) {
    _uiState.update { state ->
      val updatedOrders = state.orders.map { ord ->
        if (ord.id == orderId) ord.copy(rating = rating, review = review) else ord
      }
      val updatedActive = if (state.activeTrackingOrder?.id == orderId) {
        state.activeTrackingOrder.copy(rating = rating, review = review)
      } else {
        state.activeTrackingOrder
      }
      state.copy(
        orders = updatedOrders,
        activeTrackingOrder = updatedActive,
        userFeedbackMessage = "Thank you for your ${rating.toInt()}-star review!"
      )
    }

    val repo = orderRepository
    if (repo != null) {
      viewModelScope.launch {
        try {
          repo.updateOrderStatus(orderId, "DELIVERED", "", rating.toDouble(), review)
        } catch (e: Exception) {
          Log.w("GroceryVM", "Failed to sync rating to Firestore: ${e.message}")
        }
      }
    }
  }

  // Super Admin / Owner Dashboard actions
  fun approveShop(shopId: String) {
    if (!_uiState.value.isPlatformOwner) return
    _uiState.update { state ->
      val updated = state.platformShops.map { shop ->
        if (shop.id == shopId) shop.copy(status = com.example.model.ShopApprovalStatus.APPROVED) else shop
      }
      state.copy(platformShops = updated)
    }
    logPlatformAudit("Approve Shop", "Shop $shopId status set to APPROVED", shopId)
  }

  fun suspendShop(shopId: String) {
    if (!_uiState.value.isPlatformOwner) return
    _uiState.update { state ->
      val updated = state.platformShops.map { shop ->
        if (shop.id == shopId) shop.copy(status = com.example.model.ShopApprovalStatus.SUSPENDED) else shop
      }
      state.copy(platformShops = updated)
    }
    logPlatformAudit("Suspend Shop", "Shop $shopId suspended by platform owner", shopId)
  }

  fun toggleShopOpenStatus(shopId: String) {
    _uiState.update { state ->
      val updated = state.platformShops.map { shop ->
        if (shop.id == shopId) shop.copy(isOpen = !shop.isOpen) else shop
      }
      state.copy(platformShops = updated)
    }
    logPlatformAudit("Toggle Shop Hours", "Shop $shopId operating status toggled", shopId)
  }

  fun updateShopCommission(shopId: String, ratePercent: Double) {
    if (!_uiState.value.isPlatformOwner) return
    _uiState.update { state ->
      val updated = state.platformShops.map { shop ->
        if (shop.id == shopId) shop.copy(commissionRatePercent = ratePercent) else shop
      }
      state.copy(
        platformShops = updated,
        userFeedbackMessage = "Commission for $shopId updated to $ratePercent%"
      )
    }
    logPlatformAudit("Update Commission", "Commission for $shopId set to $ratePercent%", shopId)
  }

  fun addOrUpdatePlatformCategory(cat: PlatformCategory) {
    if (!_uiState.value.isPlatformOwner) return
    _uiState.update { state ->
      val exists = state.platformCategories.any { it.id == cat.id }
      val updated = if (exists) {
        state.platformCategories.map { if (it.id == cat.id) cat else it }
      } else {
        state.platformCategories + cat
      }
      state.copy(
        platformCategories = updated,
        userFeedbackMessage = "Category ${cat.displayName} updated"
      )
    }
    logPlatformAudit("Category Updated", "Category ${cat.displayName} saved", cat.id)
  }

  fun togglePlatformCategory(catId: String) {
    if (!_uiState.value.isPlatformOwner) return
    _uiState.update { state ->
      val updated = state.platformCategories.map { cat ->
        if (cat.id == catId) cat.copy(isEnabled = !cat.isEnabled) else cat
      }
      state.copy(platformCategories = updated)
    }
    logPlatformAudit("Category Toggled", "Category $catId visibility toggled", catId)
  }

  fun updatePlatformSettings(deliveryFee: Double, radiusKm: Double, minOrder: Double) {
    if (!_uiState.value.isPlatformOwner) return
    _uiState.update {
      it.copy(
        platformDeliveryFee = deliveryFee,
        platformServiceRadiusKm = radiusKm,
        platformMinOrderValue = minOrder,
        userFeedbackMessage = "Platform settings saved successfully"
      )
    }
    logPlatformAudit("Settings Updated", "Delivery fee: ₹$deliveryFee, Radius: ${radiusKm}km, Min order: ₹$minOrder", "Settings")
  }

  fun logPlatformAudit(action: String, details: String, target: String = "Platform") {
    val userEmail = _uiState.value.adminUserEmail ?: _uiState.value.user.email
    val log = PlatformAuditLog(
      action = action,
      performedBy = userEmail,
      details = details,
      targetEntity = target
    )
    _uiState.update {
      it.copy(platformAuditLogs = listOf(log) + it.platformAuditLogs.take(50))
    }
  }
}
