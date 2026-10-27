package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AdminPanelSettings
import androidx.compose.material.icons.filled.CardGiftcard
import androidx.compose.material.icons.filled.DeliveryDining
import androidx.compose.material.icons.filled.LocalShipping
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material.icons.filled.Storefront
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.ui.components.ShahpurGoBrandHeader
import com.example.ui.screens.AdminDashboardScreen
import com.example.ui.screens.CartCheckoutScreen
import com.example.ui.screens.CustomerOtpLoginDialog
import com.example.ui.screens.HomeScreen
import com.example.ui.screens.LocationPickerScreen
import com.example.ui.screens.LoginScreen
import com.example.ui.screens.MerchantLoginScreen
import com.example.ui.screens.NotificationsScreen
import com.example.ui.screens.OrderConfirmationAndTrackingScreen
import com.example.ui.screens.OrderHistoryScreen
import com.example.ui.screens.ProfileAndAccountScreen
import com.example.ui.screens.ReferralAndRewardsScreen
import com.example.ui.screens.ShopDetailScreen
import com.example.ui.screens.SuperAdminDashboardScreen
import com.example.ui.theme.BrandGreenDark
import com.example.ui.theme.BrandGreenPrimary
import com.example.ui.theme.BrandGreenSurface
import com.example.ui.theme.MountainCardSurface
import com.example.ui.theme.MountainWhite
import com.example.ui.theme.RewardGold
import com.example.ui.theme.ShahpurGoTheme
import com.example.ui.theme.TextMuted
import com.example.viewmodel.AuthViewModel
import com.example.viewmodel.GroceryViewModel

enum class MainNavigationTab {
  SHOP,
  TRACK,
  REWARDS,
  PROFILE
}

enum class ActiveSubScreen {
  NONE,
  CART,
  LOCATION_PICKER,
  NOTIFICATIONS,
  ADMIN,
  MERCHANT_LOGIN,
  ORDER_HISTORY,
  SHOP_DETAIL,
  SUPER_ADMIN,
  CUSTOMER_LOGIN,
  LOGIN
}

class MainActivity : ComponentActivity() {
  private val viewModel: GroceryViewModel by viewModels()
  private val authViewModel: AuthViewModel by viewModels()

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    enableEdgeToEdge()
    viewModel.initFirestore(this)

    setContent {
      ShahpurGoTheme {
        val uiState by viewModel.uiState.collectAsStateWithLifecycle()
        val snackbarHostState = remember { SnackbarHostState() }

        var currentTab by remember { mutableStateOf(MainNavigationTab.SHOP) }
        var activeSubScreen by remember { mutableStateOf(ActiveSubScreen.NONE) }

        // Trigger snackbar on feedback
        LaunchedEffect(uiState.userFeedbackMessage) {
          uiState.userFeedbackMessage?.let { msg ->
            snackbarHostState.showSnackbar(msg)
            viewModel.clearFeedbackMessage()
          }
        }

        // Handle system back button gracefully
        if (activeSubScreen != ActiveSubScreen.NONE) {
          BackHandler {
            if (activeSubScreen == ActiveSubScreen.ADMIN) {
              viewModel.logoutAdmin()
            }
            activeSubScreen = ActiveSubScreen.NONE
          }
        } else if (currentTab != MainNavigationTab.SHOP) {
          BackHandler {
            currentTab = MainNavigationTab.SHOP
          }
        }

        // When admin view is activated directly from authenticated state
        LaunchedEffect(uiState.isAdminView, uiState.isAdminAuthenticated) {
          if (uiState.isAdminView && uiState.isAdminAuthenticated) {
            activeSubScreen = ActiveSubScreen.ADMIN
          }
        }

        Scaffold(
          modifier = Modifier.fillMaxSize(),
          snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
          topBar = {
            if (activeSubScreen == ActiveSubScreen.NONE) {
              ShahpurGoBrandHeader(
                currentLocation = uiState.currentDeliveryLocation,
                unreadNotificationsCount = uiState.notifications.count { !it.isRead },
                userSession = uiState.userSession,
                onLocationClick = { activeSubScreen = ActiveSubScreen.LOCATION_PICKER },
                onNotificationClick = { activeSubScreen = ActiveSubScreen.NOTIFICATIONS },
                onOpenMerchantHub = {
                  if (uiState.isAdminAuthenticated) {
                    activeSubScreen = ActiveSubScreen.ADMIN
                  } else {
                    activeSubScreen = ActiveSubScreen.MERCHANT_LOGIN
                  }
                },
                onProfileClick = {
                  currentTab = MainNavigationTab.PROFILE
                }
              )
            }
          },
          bottomBar = {
            if (activeSubScreen == ActiveSubScreen.NONE) {
              NavigationBar(
                containerColor = MountainCardSurface,
                tonalElevation = 6.dp,
                modifier = Modifier.testTag("main_bottom_nav_bar")
              ) {
                NavigationBarItem(
                  selected = currentTab == MainNavigationTab.SHOP,
                  onClick = { currentTab = MainNavigationTab.SHOP },
                  icon = { Icon(Icons.Default.Storefront, contentDescription = "Shop") },
                  label = { Text("Shop") },
                  modifier = Modifier.testTag("nav_tab_shop"),
                  colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = BrandGreenPrimary,
                    selectedTextColor = BrandGreenPrimary,
                    indicatorColor = BrandGreenSurface,
                    unselectedIconColor = TextMuted,
                    unselectedTextColor = TextMuted
                  )
                )

                NavigationBarItem(
                  selected = currentTab == MainNavigationTab.TRACK,
                  onClick = { currentTab = MainNavigationTab.TRACK },
                  icon = { Icon(Icons.Default.DeliveryDining, contentDescription = "Track") },
                  label = { Text("Track") },
                  modifier = Modifier.testTag("nav_tab_track"),
                  colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = BrandGreenPrimary,
                    selectedTextColor = BrandGreenPrimary,
                    indicatorColor = BrandGreenSurface,
                    unselectedIconColor = TextMuted,
                    unselectedTextColor = TextMuted
                  )
                )

                NavigationBarItem(
                  selected = currentTab == MainNavigationTab.REWARDS,
                  onClick = { currentTab = MainNavigationTab.REWARDS },
                  icon = { Icon(Icons.Default.CardGiftcard, contentDescription = "Rewards") },
                  label = { Text("Rewards") },
                  modifier = Modifier.testTag("nav_tab_rewards"),
                  colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = BrandGreenPrimary,
                    selectedTextColor = BrandGreenPrimary,
                    indicatorColor = BrandGreenSurface,
                    unselectedIconColor = TextMuted,
                    unselectedTextColor = TextMuted
                  )
                )

                NavigationBarItem(
                  selected = currentTab == MainNavigationTab.PROFILE,
                  onClick = { currentTab = MainNavigationTab.PROFILE },
                  icon = { Icon(Icons.Default.Person, contentDescription = "Account") },
                  label = { Text("Account") },
                  modifier = Modifier.testTag("nav_tab_profile"),
                  colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = BrandGreenPrimary,
                    selectedTextColor = BrandGreenPrimary,
                    indicatorColor = BrandGreenSurface,
                    unselectedIconColor = TextMuted,
                    unselectedTextColor = TextMuted
                  )
                )
              }
            }
          }
        ) { innerPadding ->
          Box(
            modifier = Modifier
              .fillMaxSize()
              .padding(innerPadding)
          ) {
            when (activeSubScreen) {
              ActiveSubScreen.CART -> {
                CartCheckoutScreen(
                  uiState = uiState,
                  onBack = { activeSubScreen = ActiveSubScreen.NONE },
                  onAddToCart = { viewModel.addToCart(it) },
                  onDecrementCart = { viewModel.decrementCart(it) },
                  onSelectTip = { viewModel.setDeliveryTip(it) },
                  onSelectPaymentMethod = { viewModel.setPaymentMethod(it) },
                  onApplyCoupon = { viewModel.applyCoupon(it) },
                  onRemoveCoupon = { viewModel.removeCoupon() },
                  onVerifyOnlinePayment = { method, cb -> viewModel.startOnlinePaymentVerification(method, cb) },
                  onPlaceOrder = {
                    val placed = viewModel.placeOrder()
                    if (placed != null) {
                      currentTab = MainNavigationTab.TRACK
                      activeSubScreen = ActiveSubScreen.NONE
                    }
                  },
                  onChangeAddressClick = { activeSubScreen = ActiveSubScreen.LOCATION_PICKER }
                )
              }

              ActiveSubScreen.LOCATION_PICKER -> {
                LocationPickerScreen(
                  currentLocation = uiState.currentDeliveryLocation,
                  savedLocations = uiState.deliveryLocations,
                  onLocationSelected = {
                    viewModel.selectDeliveryLocation(it)
                    activeSubScreen = ActiveSubScreen.NONE
                  },
                  onSaveCustomLocation = { area, addr, landmark, lat, lng ->
                    viewModel.addOrUpdateCustomLocation(area, addr, landmark, lat, lng)
                  },
                  onBack = { activeSubScreen = ActiveSubScreen.NONE }
                )
              }

              ActiveSubScreen.NOTIFICATIONS -> {
                NotificationsScreen(
                  notifications = uiState.notifications,
                  onMarkAllRead = { viewModel.markAllNotificationsRead() },
                  onBack = { activeSubScreen = ActiveSubScreen.NONE }
                )
              }

              ActiveSubScreen.MERCHANT_LOGIN -> {
                MerchantLoginScreen(
                  storeHubId = uiState.storeHubId,
                  isGoogleSigningIn = uiState.isGoogleSigningIn,
                  isGoogleAdminSignedIn = uiState.isGoogleAdminSignedIn,
                  adminUserEmail = uiState.adminUserEmail,
                  onSignInWithGoogle = {
                    viewModel.signInAdminWithGoogle(
                      context = this@MainActivity,
                      onResult = { success, _ ->
                        if (success) {
                          activeSubScreen = ActiveSubScreen.ADMIN
                        }
                      }
                    )
                  },
                  onAuthenticate = { pin ->
                    val ok = if (pin.isBlank() && uiState.isGoogleAdminSignedIn) {
                      true
                    } else {
                      viewModel.authenticateAdmin(pin)
                    }
                    if (ok) {
                      activeSubScreen = ActiveSubScreen.ADMIN
                    }
                    ok
                  },
                  onBackToCustomerStore = { activeSubScreen = ActiveSubScreen.NONE }
                )
              }

              ActiveSubScreen.ADMIN -> {
                AdminDashboardScreen(
                  uiState = uiState,
                  onBackToCustomer = {
                    viewModel.logoutAdmin()
                    activeSubScreen = ActiveSubScreen.NONE
                  },
                  onSignOutAdmin = {
                    viewModel.signOutAdmin()
                    activeSubScreen = ActiveSubScreen.NONE
                  },
                  onUpdateStock = { id, delta -> viewModel.updateInventoryStock(id, delta) },
                  onToggleStockAvailability = { id -> viewModel.toggleItemStockAvailability(id) },
                  onAddNewItem = { name, hindi, cat, price, cost, unit, stock ->
                    viewModel.addNewInventoryItem(name, hindi, cat, price, cost, unit, stock)
                  },
                  onUpdateOrderStatus = { id, status -> viewModel.updateOrderStatus(id, status) },
                  onUpdateFirestoreOrderStatus = { id, status -> viewModel.updateFirestoreOrderStatus(id, status) },
                  onCreateLiveFirestoreOrder = { viewModel.createLiveTestOrder() },
                  onRestockAllLow = { viewModel.restockLowInventory() },
                  onSimulateNewOrder = { viewModel.simulateStoreTestOrder() },
                  onToggleStoreAcceptingOrders = { viewModel.toggleStoreAcceptingOrders() },
                  onUpdateDeliveryRadius = { viewModel.updateDeliveryRadius(it) },
                  onUpdateAdminPasscode = { oldPin, newPin -> viewModel.updateAdminPasscode(oldPin, newPin) },
                  onUpdateStoreManagerName = { viewModel.updateStoreManagerName(it) },
                  onSaveProduct = { product, uri, cb -> viewModel.saveProduct(this@MainActivity, product, uri, cb) },
                  onDeleteProduct = { id, cb -> viewModel.deleteProduct(id, cb) },
                  onToggleProductAvailability = { id, isAvail -> viewModel.toggleProductAvailability(id, isAvail) },
                  onUpdateProductStock = { id, newStock -> viewModel.updateProductStock(id, newStock) },
                  onSelectProductShop = { viewModel.setSelectedProductShop(it) },
                  onSelectProductCategory = { viewModel.setSelectedProductCategory(it) },
                  onSearchProductQuery = { viewModel.setProductSearchQuery(it) },
                  onClearProductMessages = { viewModel.clearProductMessages() },
                  onRefreshProducts = { viewModel.startListeningToFirestoreProducts() }
                )
              }

              ActiveSubScreen.ORDER_HISTORY -> {
                OrderHistoryScreen(
                  orders = uiState.orders,
                  onReorder = { ord ->
                    viewModel.reorder(ord)
                    activeSubScreen = ActiveSubScreen.CART
                  },
                  onTrackOrder = { ord ->
                    viewModel.setActiveTrackingOrder(ord)
                    currentTab = MainNavigationTab.TRACK
                    activeSubScreen = ActiveSubScreen.NONE
                  },
                  onBack = { activeSubScreen = ActiveSubScreen.NONE },
                  onBrowseGroceries = {
                    currentTab = MainNavigationTab.SHOP
                    activeSubScreen = ActiveSubScreen.NONE
                  }
                )
              }

              ActiveSubScreen.SHOP_DETAIL -> {
                ShopDetailScreen(
                  shop = uiState.selectedShopForBrowsing ?: uiState.platformShops.first(),
                  uiState = uiState,
                  onAddToCart = { viewModel.addToCart(it) },
                  onDecrementCart = { viewModel.decrementCart(it) },
                  onViewCart = { activeSubScreen = ActiveSubScreen.CART },
                  onBack = { activeSubScreen = ActiveSubScreen.NONE }
                )
              }

              ActiveSubScreen.SUPER_ADMIN -> {
                SuperAdminDashboardScreen(
                  uiState = uiState,
                  onApproveShop = { viewModel.approveShop(it) },
                  onSuspendShop = { viewModel.suspendShop(it) },
                  onToggleShopOpen = { viewModel.toggleShopOpenStatus(it) },
                  onUpdateCommission = { id, comm -> viewModel.updateShopCommission(id, comm) },
                  onToggleCategory = { viewModel.togglePlatformCategory(it) },
                  onAddCategory = { viewModel.addOrUpdatePlatformCategory(it) },
                  onUpdatePlatformSettings = { fee, rad, minOrd -> viewModel.updatePlatformSettings(fee, rad, minOrd) },
                  onBackToCustomer = { activeSubScreen = ActiveSubScreen.NONE }
                )
              }

              ActiveSubScreen.CUSTOMER_LOGIN -> {
                CustomerOtpLoginDialog(
                  isOtpSent = uiState.isCustomerOtpSent,
                  phoneInput = uiState.customerPhoneInput,
                  otpInput = uiState.customerOtpInput,
                  loginError = uiState.customerLoginError,
                  onPhoneChange = { viewModel.setCustomerPhoneInput(it) },
                  onOtpChange = { viewModel.setCustomerOtpInput(it) },
                  onSendOtp = { viewModel.sendCustomerOtp() },
                  onVerifyOtp = { ok ->
                    if (viewModel.verifyCustomerOtp(ok)) {
                      activeSubScreen = ActiveSubScreen.NONE
                    }
                  },
                  onGoogleSignIn = {
                    viewModel.signInCustomerWithGoogle(this@MainActivity) { ok, _ ->
                      if (ok) {
                        activeSubScreen = ActiveSubScreen.NONE
                      }
                    }
                  },
                  isGoogleSigningIn = uiState.isGoogleSigningIn,
                  onDismiss = { activeSubScreen = ActiveSubScreen.NONE }
                )
              }

              ActiveSubScreen.LOGIN -> {
                LoginScreen(
                  authViewModel = authViewModel,
                  onBack = { activeSubScreen = ActiveSubScreen.NONE },
                  onLoginSuccess = {
                    activeSubScreen = ActiveSubScreen.NONE
                  },
                  onOpenPhoneLogin = {
                    activeSubScreen = ActiveSubScreen.CUSTOMER_LOGIN
                  },
                  onContinueAsGuest = {
                    activeSubScreen = ActiveSubScreen.NONE
                  }
                )
              }

              ActiveSubScreen.NONE -> {
                AnimatedContent(
                  targetState = currentTab,
                  transitionSpec = { fadeIn() togetherWith fadeOut() },
                  label = "tab_navigation"
                ) { targetTab ->
                  when (targetTab) {
                    MainNavigationTab.SHOP -> {
                      HomeScreen(
                        uiState = uiState,
                        onCategorySelect = { viewModel.selectCategory(it) },
                        onSearchChange = { viewModel.updateSearchQuery(it) },
                        onAddToCart = { viewModel.addToCart(it) },
                        onDecrementCart = { viewModel.decrementCart(it) },
                        onViewCartClick = { activeSubScreen = ActiveSubScreen.CART },
                        onOpenLocationPicker = { activeSubScreen = ActiveSubScreen.LOCATION_PICKER },
                        onShopSelect = { shop ->
                          viewModel.selectShopForBrowsing(shop)
                          activeSubScreen = ActiveSubScreen.SHOP_DETAIL
                        }
                      )
                    }

                    MainNavigationTab.TRACK -> {
                      OrderConfirmationAndTrackingScreen(
                        order = uiState.activeTrackingOrder ?: uiState.orders.firstOrNull(),
                        onBack = { currentTab = MainNavigationTab.SHOP },
                        onContinueShopping = { currentTab = MainNavigationTab.SHOP },
                        onUpdateStatus = { id, st -> viewModel.updateOrderStatus(id, st) },
                        onCancelOrder = { id, reason -> viewModel.cancelOrder(id, reason) },
                        onSubmitRating = { id, rating, review -> viewModel.submitOrderRating(id, rating, review) }
                      )
                    }

                    MainNavigationTab.REWARDS -> {
                      ReferralAndRewardsScreen(
                        uiState = uiState,
                        onScratchCard = { viewModel.scratchCard() }
                      )
                    }

                    MainNavigationTab.PROFILE -> {
                      ProfileAndAccountScreen(
                        uiState = uiState,
                        onOpenMerchantPortal = {
                          if (uiState.isAdminAuthenticated) {
                            activeSubScreen = ActiveSubScreen.ADMIN
                          } else {
                            activeSubScreen = ActiveSubScreen.MERCHANT_LOGIN
                          }
                        },
                        onOpenLocationPicker = { activeSubScreen = ActiveSubScreen.LOCATION_PICKER },
                        onOpenOrderHistory = { activeSubScreen = ActiveSubScreen.ORDER_HISTORY },
                        onSelectOrderToTrack = { ord ->
                          viewModel.setActiveTrackingOrder(ord)
                          currentTab = MainNavigationTab.TRACK
                        },
                        onReorder = { ord ->
                          viewModel.reorder(ord)
                          activeSubScreen = ActiveSubScreen.CART
                        },
                        onOpenSuperAdmin = { activeSubScreen = ActiveSubScreen.SUPER_ADMIN },
                        onOpenCustomerLogin = { activeSubScreen = ActiveSubScreen.LOGIN },
                        onLogoutCustomer = { viewModel.logoutCustomer() },
                        onSignInWithGoogle = {
                          activeSubScreen = ActiveSubScreen.LOGIN
                        },
                        onSignOutGoogle = {
                          authViewModel.signOut {
                            viewModel.signOutCustomer(this@MainActivity)
                          }
                        },
                        onRefreshProfile = {
                          authViewModel.refreshProfile()
                          viewModel.refreshUserSession()
                        }
                      )
                    }
                  }
                }
              }
            }
          }
        }
      }
    }
  }
}
