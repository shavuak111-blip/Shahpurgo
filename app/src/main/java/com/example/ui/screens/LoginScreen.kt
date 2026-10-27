package com.example.ui.screens

import android.app.Activity
import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.CloudDone
import androidx.compose.material.icons.filled.ErrorOutline
import androidx.compose.material.icons.filled.LocalShipping
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.example.R
import com.example.ui.theme.BrandGreenAccent
import com.example.ui.theme.BrandGreenContainer
import com.example.ui.theme.BrandGreenDark
import com.example.ui.theme.BrandGreenPrimary
import com.example.ui.theme.BrandGreenSurface
import com.example.ui.theme.ErrorContainer
import com.example.ui.theme.ErrorRed
import com.example.ui.theme.MountainBorder
import com.example.ui.theme.MountainCardSurface
import com.example.ui.theme.MountainSnowBackground
import com.example.ui.theme.MountainWhite
import com.example.ui.theme.RewardGold
import com.example.ui.theme.RewardGoldContainer
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimaryDark
import com.example.ui.theme.TextSecondarySlate
import com.example.viewmodel.AuthUiState
import com.example.viewmodel.AuthViewModel

/**
 * Brand-compliant, modern Login Screen for ShahpurGo.
 * Utilizes [AuthViewModel] to drive Google Sign-In with CredentialManager,
 * offering instant session synchronization, phone OTP fallback, and guest browsing.
 */
@Composable
fun LoginScreen(
  authViewModel: AuthViewModel,
  onBack: () -> Unit,
  onLoginSuccess: () -> Unit = {},
  onOpenPhoneLogin: () -> Unit = {},
  onContinueAsGuest: () -> Unit = {}
) {
  val context = LocalContext.current
  val uiState by authViewModel.uiState.collectAsStateWithLifecycle()

  BackHandler {
    onBack()
  }

  Scaffold(
    containerColor = MountainSnowBackground,
    topBar = {
      Row(
        modifier = Modifier
          .fillMaxWidth()
          .background(BrandGreenDark)
          .padding(horizontal = 8.dp, vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically
      ) {
        IconButton(
          onClick = onBack,
          modifier = Modifier
            .size(48.dp)
            .testTag("login_back_button")
        ) {
          Icon(
            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
            contentDescription = "Back to store",
            tint = MountainWhite
          )
        }

        Text(
          text = "Sign In",
          style = MaterialTheme.typography.titleMedium,
          fontWeight = FontWeight.Bold,
          color = MountainWhite,
          modifier = Modifier.padding(start = 4.dp)
        )
      }
    }
  ) { paddingValues ->
    Column(
      modifier = Modifier
        .fillMaxSize()
        .padding(paddingValues)
        .verticalScroll(rememberScrollState()),
      horizontalAlignment = Alignment.CenterHorizontally
    ) {
      // Himalayan Mountain Hero Banner
      Box(
        modifier = Modifier
          .fillMaxWidth()
          .background(
            Brush.verticalGradient(
              colors = listOf(BrandGreenDark, BrandGreenPrimary)
            )
          )
          .padding(top = 16.dp, bottom = 28.dp, start = 20.dp, end = 20.dp),
        contentAlignment = Alignment.Center
      ) {
        Column(
          horizontalAlignment = Alignment.CenterHorizontally
        ) {
          // Brand Icon Badge
          Surface(
            shape = CircleShape,
            color = MountainWhite.copy(alpha = 0.15f),
            modifier = Modifier.size(76.dp)
          ) {
            Box(contentAlignment = Alignment.Center) {
              Icon(
                imageVector = Icons.Default.ShoppingBag,
                contentDescription = "ShahpurGo Shopping",
                tint = MountainWhite,
                modifier = Modifier.size(40.dp)
              )
            }
          }

          Spacer(modifier = Modifier.height(12.dp))

          Text(
            text = "ShahpurGo",
            fontSize = 30.sp,
            fontWeight = FontWeight.ExtraBold,
            color = MountainWhite,
            letterSpacing = (-0.5).sp
          )

          Spacer(modifier = Modifier.height(4.dp))

          Surface(
            shape = RoundedCornerShape(12.dp),
            color = BrandGreenAccent.copy(alpha = 0.25f),
            modifier = Modifier.padding(top = 4.dp)
          ) {
            Row(
              modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
              verticalAlignment = Alignment.CenterVertically
            ) {
              Text(
                text = "🏔️ Kangra Valley Express",
                color = MountainWhite,
                fontSize = 12.sp,
                fontWeight = FontWeight.SemiBold
              )
              Text(
                text = " • 10-15 Min Delivery",
                color = MountainWhite.copy(alpha = 0.9f),
                fontSize = 12.sp
              )
            }
          }

          Spacer(modifier = Modifier.height(10.dp))

          Text(
            text = "Shahpur • Gaggal • Dharamshala Road",
            style = MaterialTheme.typography.bodySmall,
            color = MountainWhite.copy(alpha = 0.85f),
            textAlign = TextAlign.Center
          )
        }
      }

      // Main Card Content
      Column(
        modifier = Modifier
          .fillMaxWidth()
          .padding(horizontal = 16.dp, vertical = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
      ) {
        // Active Session State vs Unauthenticated State
        if (uiState.session.isGoogleSignedIn) {
          ActiveSessionCard(
            uiState = uiState,
            onContinueShopping = {
              onLoginSuccess()
              onBack()
            },
            onSignOut = {
              authViewModel.signOut()
            }
          )
        } else {
          UnauthenticatedLoginContent(
            uiState = uiState,
            onGoogleSignIn = {
              authViewModel.signInWithGoogle(context) {
                onLoginSuccess()
              }
            },
            onClearError = { authViewModel.clearError() },
            onOpenPhoneLogin = onOpenPhoneLogin,
            onContinueAsGuest = {
              onContinueAsGuest()
              onBack()
            }
          )
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Security Assurance & Local Origin Note
        Row(
          verticalAlignment = Alignment.CenterVertically,
          modifier = Modifier.padding(horizontal = 16.dp)
        ) {
          Icon(
            imageVector = Icons.Default.Lock,
            contentDescription = null,
            tint = TextMuted,
            modifier = Modifier.size(14.dp)
          )
          Spacer(modifier = Modifier.width(6.dp))
          Text(
            text = "Secured by Google Identity • End-to-End Encrypted",
            style = MaterialTheme.typography.labelSmall,
            color = TextMuted,
            textAlign = TextAlign.Center
          )
        }

        Spacer(modifier = Modifier.height(6.dp))

        Text(
          text = "By signing in, you agree to ShahpurGo's Terms & Privacy Policy.\nDedicated to the residents of Kangra Valley, HP.",
          style = MaterialTheme.typography.labelSmall,
          color = TextMuted.copy(alpha = 0.8f),
          textAlign = TextAlign.Center,
          lineHeight = 16.sp
        )

        Spacer(modifier = Modifier.height(16.dp))
      }
    }
  }
}

/**
 * Displayed when user is already signed in with a Google account.
 */
@Composable
private fun ActiveSessionCard(
  uiState: AuthUiState,
  onContinueShopping: () -> Unit,
  onSignOut: () -> Unit
) {
  Card(
    shape = RoundedCornerShape(20.dp),
    colors = CardDefaults.cardColors(containerColor = MountainCardSurface),
    border = androidx.compose.foundation.BorderStroke(1.dp, MountainBorder),
    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
    modifier = Modifier.fillMaxWidth()
  ) {
    Column(
      modifier = Modifier.padding(20.dp),
      horizontalAlignment = Alignment.CenterHorizontally
    ) {
      Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
      ) {
        // User Avatar
        if (!uiState.session.photoUrl.isNullOrBlank()) {
          AsyncImage(
            model = uiState.session.photoUrl,
            contentDescription = "User profile picture",
            modifier = Modifier
              .size(54.dp)
              .clip(CircleShape)
              .border(2.dp, BrandGreenPrimary, CircleShape),
            contentScale = ContentScale.Crop
          )
        } else {
          Box(
            modifier = Modifier
              .size(54.dp)
              .clip(CircleShape)
              .background(BrandGreenContainer),
            contentAlignment = Alignment.Center
          ) {
            Text(
              text = uiState.session.initials,
              fontWeight = FontWeight.Bold,
              fontSize = 20.sp,
              color = BrandGreenDark
            )
          }
        }

        Spacer(modifier = Modifier.width(14.dp))

        Column(modifier = Modifier.weight(1f)) {
          Text(
            text = uiState.session.displayName,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = TextPrimaryDark
          )
          Text(
            text = uiState.session.email ?: "Signed in via Google",
            style = MaterialTheme.typography.bodySmall,
            color = TextSecondarySlate
          )
          Surface(
            shape = RoundedCornerShape(8.dp),
            color = BrandGreenSurface,
            modifier = Modifier.padding(top = 4.dp)
          ) {
            Row(
              modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
              verticalAlignment = Alignment.CenterVertically
            ) {
              Icon(
                imageVector = Icons.Default.Verified,
                contentDescription = null,
                tint = BrandGreenPrimary,
                modifier = Modifier.size(12.dp)
              )
              Spacer(modifier = Modifier.width(4.dp))
              Text(
                text = uiState.session.displayRole,
                fontSize = 11.sp,
                fontWeight = FontWeight.SemiBold,
                color = BrandGreenDark
              )
            }
          }
        }
      }

      Spacer(modifier = Modifier.height(20.dp))
      HorizontalDivider(color = MountainBorder)
      Spacer(modifier = Modifier.height(16.dp))

      Button(
        onClick = onContinueShopping,
        modifier = Modifier
          .fillMaxWidth()
          .height(48.dp)
          .testTag("login_continue_shopping_btn"),
        shape = RoundedCornerShape(12.dp),
        colors = ButtonDefaults.buttonColors(containerColor = BrandGreenPrimary)
      ) {
        Icon(
          imageVector = Icons.Default.ShoppingBag,
          contentDescription = null,
          modifier = Modifier.size(18.dp)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text("Continue Shopping", fontWeight = FontWeight.Bold, fontSize = 15.sp)
      }

      Spacer(modifier = Modifier.height(10.dp))

      OutlinedButton(
        onClick = onSignOut,
        modifier = Modifier
          .fillMaxWidth()
          .height(48.dp)
          .testTag("login_sign_out_btn"),
        shape = RoundedCornerShape(12.dp),
        colors = ButtonDefaults.outlinedButtonColors(contentColor = TextSecondarySlate),
        border = androidx.compose.foundation.BorderStroke(1.dp, MountainBorder)
      ) {
        Text("Sign Out / Switch Account", fontWeight = FontWeight.Medium)
      }
    }
  }
}

/**
 * Standard unauthenticated view with brand propositions and Google Sign-In button.
 */
@Composable
private fun UnauthenticatedLoginContent(
  uiState: AuthUiState,
  onGoogleSignIn: () -> Unit,
  onClearError: () -> Unit,
  onOpenPhoneLogin: () -> Unit,
  onContinueAsGuest: () -> Unit
) {
  Card(
    shape = RoundedCornerShape(20.dp),
    colors = CardDefaults.cardColors(containerColor = MountainCardSurface),
    border = androidx.compose.foundation.BorderStroke(1.dp, MountainBorder),
    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
    modifier = Modifier.fillMaxWidth()
  ) {
    Column(
      modifier = Modifier.padding(20.dp),
      horizontalAlignment = Alignment.CenterHorizontally
    ) {
      Text(
        text = "Welcome to ShahpurGo",
        style = MaterialTheme.typography.titleLarge,
        fontWeight = FontWeight.ExtraBold,
        color = TextPrimaryDark,
        textAlign = TextAlign.Center
      )

      Spacer(modifier = Modifier.height(4.dp))

      Text(
        text = "Sign in to track orders, earn Kangra rewards, and save delivery addresses.",
        style = MaterialTheme.typography.bodyMedium,
        color = TextSecondarySlate,
        textAlign = TextAlign.Center
      )

      Spacer(modifier = Modifier.height(18.dp))

      // Value Propositions
      BenefitRow(
        icon = Icons.Default.LocalShipping,
        iconTint = BrandGreenPrimary,
        containerColor = BrandGreenSurface,
        title = "Live Order Tracking",
        description = "Watch your groceries delivered in real time from Shahpur shops."
      )

      Spacer(modifier = Modifier.height(10.dp))

      BenefitRow(
        icon = Icons.Default.Star,
        iconTint = RewardGold,
        containerColor = RewardGoldContainer,
        title = "Kangra Specials & Rewards",
        description = "Enjoy authentic Pahari produce, milk, and local store deals."
      )

      Spacer(modifier = Modifier.height(10.dp))

      BenefitRow(
        icon = Icons.Default.CloudDone,
        iconTint = Color(0xFF2563EB),
        containerColor = Color(0xFFEFF6FF),
        title = "Cloud Synced Account",
        description = "Your saved addresses, cart & orders sync across all devices."
      )

      Spacer(modifier = Modifier.height(20.dp))

      // Error Banner
      AnimatedVisibility(
        visible = uiState.errorMessage != null,
        enter = fadeIn(),
        exit = fadeOut()
      ) {
        uiState.errorMessage?.let { errorText ->
          Surface(
            shape = RoundedCornerShape(12.dp),
            color = ErrorContainer,
            modifier = Modifier
              .fillMaxWidth()
              .padding(bottom = 14.dp)
          ) {
            Row(
              modifier = Modifier.padding(12.dp),
              verticalAlignment = Alignment.CenterVertically
            ) {
              Icon(
                imageVector = Icons.Default.ErrorOutline,
                contentDescription = null,
                tint = ErrorRed,
                modifier = Modifier.size(20.dp)
              )
              Spacer(modifier = Modifier.width(10.dp))
              Text(
                text = errorText,
                style = MaterialTheme.typography.bodySmall,
                color = ErrorRed,
                modifier = Modifier.weight(1f)
              )
              IconButton(
                onClick = onClearError,
                modifier = Modifier.size(28.dp)
              ) {
                Text("✕", color = ErrorRed, fontWeight = FontWeight.Bold)
              }
            }
          }
        }
      }

      // Brand-Compliant Google Sign-In Button
      Surface(
        onClick = onGoogleSignIn,
        enabled = !uiState.isLoading,
        shape = RoundedCornerShape(14.dp),
        color = MountainWhite,
        shadowElevation = 2.dp,
        border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFDADCE0)),
        modifier = Modifier
          .fillMaxWidth()
          .height(52.dp)
          .testTag("google_sign_in_button")
      ) {
        Row(
          modifier = Modifier.fillMaxSize(),
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.Center
        ) {
          if (uiState.isLoading) {
            CircularProgressIndicator(
              modifier = Modifier.size(22.dp),
              strokeWidth = 2.5.dp,
              color = BrandGreenPrimary
            )
            Spacer(modifier = Modifier.width(12.dp))
            Text(
              text = "Signing in with Google...",
              style = MaterialTheme.typography.labelLarge,
              fontWeight = FontWeight.SemiBold,
              color = TextSecondarySlate
            )
          } else {
            Image(
              painter = painterResource(id = R.drawable.ic_google_logo),
              contentDescription = "Google logo",
              modifier = Modifier.size(22.dp)
            )
            Spacer(modifier = Modifier.width(12.dp))
            Text(
              text = "Continue with Google",
              style = MaterialTheme.typography.titleMedium,
              fontWeight = FontWeight.SemiBold,
              color = Color(0xFF1F1F1F),
              fontSize = 15.sp
            )
          }
        }
      }

      Spacer(modifier = Modifier.height(16.dp))

      // Alternative options separator
      Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
      ) {
        HorizontalDivider(modifier = Modifier.weight(1f), color = MountainBorder)
        Text(
          text = "  OR  ",
          style = MaterialTheme.typography.labelSmall,
          color = TextMuted,
          fontWeight = FontWeight.Bold
        )
        HorizontalDivider(modifier = Modifier.weight(1f), color = MountainBorder)
      }

      Spacer(modifier = Modifier.height(16.dp))

      // Phone OTP Alternative Button
      OutlinedButton(
        onClick = onOpenPhoneLogin,
        modifier = Modifier
          .fillMaxWidth()
          .height(48.dp)
          .testTag("login_phone_otp_btn"),
        shape = RoundedCornerShape(12.dp),
        colors = ButtonDefaults.outlinedButtonColors(contentColor = TextPrimaryDark),
        border = androidx.compose.foundation.BorderStroke(1.dp, MountainBorder)
      ) {
        Icon(
          imageVector = Icons.Default.Phone,
          contentDescription = null,
          tint = BrandGreenPrimary,
          modifier = Modifier.size(18.dp)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
          text = "Login with Mobile Number OTP",
          fontWeight = FontWeight.SemiBold,
          fontSize = 14.sp
        )
      }

      Spacer(modifier = Modifier.height(8.dp))

      // Guest explore button
      TextButton(
        onClick = onContinueAsGuest,
        modifier = Modifier
          .fillMaxWidth()
          .height(48.dp)
          .testTag("login_guest_skip_btn")
      ) {
        Text(
          text = "Skip and Browse as Guest",
          color = TextMuted,
          fontSize = 13.sp,
          fontWeight = FontWeight.Medium
        )
      }
    }
  }
}

/**
 * Clean informative highlight row with badge icon.
 */
@Composable
private fun BenefitRow(
  icon: ImageVector,
  iconTint: Color,
  containerColor: Color,
  title: String,
  description: String
) {
  Row(
    modifier = Modifier.fillMaxWidth(),
    verticalAlignment = Alignment.Top
  ) {
    Surface(
      shape = CircleShape,
      color = containerColor,
      modifier = Modifier.size(36.dp)
    ) {
      Box(contentAlignment = Alignment.Center) {
        Icon(
          imageVector = icon,
          contentDescription = null,
          tint = iconTint,
          modifier = Modifier.size(18.dp)
        )
      }
    }

    Spacer(modifier = Modifier.width(12.dp))

    Column(modifier = Modifier.weight(1f)) {
      Text(
        text = title,
        style = MaterialTheme.typography.labelLarge,
        fontWeight = FontWeight.Bold,
        color = TextPrimaryDark
      )
      Text(
        text = description,
        style = MaterialTheme.typography.bodySmall,
        color = TextSecondarySlate,
        lineHeight = 16.sp
      )
    }
  }
}
