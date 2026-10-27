package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.AdminPanelSettings
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Store
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
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
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.components.MountainSilhouettes
import com.example.ui.theme.BrandGreenDark
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

@Composable
fun MerchantLoginScreen(
  storeHubId: String,
  isGoogleSigningIn: Boolean = false,
  isGoogleAdminSignedIn: Boolean = false,
  adminUserEmail: String? = null,
  onSignInWithGoogle: () -> Unit = {},
  onAuthenticate: (String) -> Boolean,
  onBackToCustomerStore: () -> Unit,
  modifier: Modifier = Modifier
) {
  var hubIdInput by remember { mutableStateOf(storeHubId) }
  var passcodeInput by remember { mutableStateOf("") }
  var isPasswordVisible by remember { mutableStateOf(false) }
  var errorMessage by remember { mutableStateOf<String?>(null) }

  Box(
    modifier = modifier
      .fillMaxSize()
      .background(MountainSnowBackground)
  ) {
    Column(
      modifier = Modifier
        .fillMaxSize()
        .verticalScroll(rememberScrollState())
    ) {
      // Top Hero Header
      Box(
        modifier = Modifier
          .fillMaxWidth()
          .background(
            Brush.verticalGradient(
              colors = listOf(
                Color(0xFF042013),
                BrandGreenDark
              )
            )
          )
          .statusBarsPadding()
          .padding(top = 16.dp, bottom = 28.dp, start = 20.dp, end = 20.dp)
      ) {
        MountainSilhouettes(
          modifier = Modifier
            .fillMaxWidth()
            .height(56.dp)
            .align(Alignment.BottomCenter),
          tint = Color.White.copy(alpha = 0.08f)
        )

        Column(
          modifier = Modifier.fillMaxWidth()
        ) {
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            IconButton(
              onClick = onBackToCustomerStore,
              modifier = Modifier.testTag("merchant_back_to_customer_btn")
            ) {
              Icon(
                imageVector = Icons.Default.ArrowBack,
                contentDescription = "Return to Customer Store",
                tint = MountainWhite
              )
            }

            Surface(
              shape = RoundedCornerShape(20.dp),
              color = RewardGold.copy(alpha = 0.2f),
              border = androidx.compose.foundation.BorderStroke(1.dp, RewardGold)
            ) {
              Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
              ) {
                Box(
                  modifier = Modifier
                    .size(6.dp)
                    .background(RewardGold, CircleShape)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                  text = "STORE TERMINAL #01",
                  style = MaterialTheme.typography.labelSmall.copy(
                    color = RewardGold,
                    fontWeight = FontWeight.Bold,
                    fontSize = 10.sp
                  )
                )
              }
            }
          }

          Spacer(modifier = Modifier.height(16.dp))

          Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
              modifier = Modifier
                .size(52.dp)
                .background(RewardGold, CircleShape),
              contentAlignment = Alignment.Center
            ) {
              Icon(
                imageVector = Icons.Default.Store,
                contentDescription = null,
                tint = BrandGreenDark,
                modifier = Modifier.size(28.dp)
              )
            }
            Spacer(modifier = Modifier.width(14.dp))
            Column {
              Text(
                text = "ShahpurGo Merchant Hub",
                style = MaterialTheme.typography.headlineSmall.copy(
                  fontWeight = FontWeight.ExtraBold,
                  color = MountainWhite
                )
              )
              Text(
                text = "Kangra Valley Store Operations & Dispatch Desk",
                style = MaterialTheme.typography.bodySmall.copy(
                  color = Color(0xFFA7F3D0)
                )
              )
            }
          }
        }
      }

      // Login Card
      Column(
        modifier = Modifier
          .fillMaxWidth()
          .padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
      ) {
        Card(
          shape = RoundedCornerShape(16.dp),
          colors = CardDefaults.cardColors(containerColor = MountainCardSurface),
          border = androidx.compose.foundation.BorderStroke(1.dp, MountainBorder),
          modifier = Modifier.fillMaxWidth()
        ) {
          Column(
            modifier = Modifier.padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
          ) {
            Column {
              Text(
                text = "Store Admin Authentication",
                style = MaterialTheme.typography.titleLarge.copy(
                  fontWeight = FontWeight.Bold,
                  color = TextPrimaryDark
                )
              )
              Text(
                text = "Live Google Admin Sign-In & real-time dispatch dashboard access.",
                style = MaterialTheme.typography.bodySmall.copy(color = TextMuted)
              )
            }

            // PRIMARY: Live Google Sign-In for Admin
            if (isGoogleAdminSignedIn && adminUserEmail != null) {
              // Already signed in via Google
              Surface(
                shape = RoundedCornerShape(12.dp),
                color = BrandGreenSurface,
                border = androidx.compose.foundation.BorderStroke(1.dp, BrandGreenPrimary.copy(alpha = 0.3f)),
                modifier = Modifier.fillMaxWidth()
              ) {
                Column(modifier = Modifier.padding(14.dp)) {
                  Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                      imageVector = Icons.Default.CheckCircle,
                      contentDescription = null,
                      tint = BrandGreenPrimary,
                      modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                      text = "Google Admin Verified",
                      style = MaterialTheme.typography.titleSmall.copy(
                        fontWeight = FontWeight.Bold,
                        color = BrandGreenDark
                      )
                    )
                  }
                  Spacer(modifier = Modifier.height(4.dp))
                  Text(
                    text = adminUserEmail,
                    style = MaterialTheme.typography.bodySmall.copy(
                      fontWeight = FontWeight.Medium,
                      color = TextSecondarySlate
                    )
                  )
                  Spacer(modifier = Modifier.height(10.dp))
                  Button(
                    onClick = { onAuthenticate("") },
                    modifier = Modifier
                      .fillMaxWidth()
                      .height(44.dp)
                      .testTag("admin_enter_terminal_verified_btn"),
                    colors = ButtonDefaults.buttonColors(
                      containerColor = BrandGreenPrimary,
                      contentColor = MountainWhite
                    ),
                    shape = RoundedCornerShape(10.dp)
                  ) {
                    Text(
                      text = "Open Live Order Dashboard →",
                      style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold)
                    )
                  }
                }
              }
            } else {
              // Google Sign-In Button
              Button(
                onClick = onSignInWithGoogle,
                enabled = !isGoogleSigningIn,
                modifier = Modifier
                  .fillMaxWidth()
                  .height(52.dp)
                  .testTag("admin_google_sign_in_btn"),
                colors = ButtonDefaults.buttonColors(
                  containerColor = Color(0xFF1E293B),
                  contentColor = MountainWhite
                ),
                shape = RoundedCornerShape(12.dp)
              ) {
                if (isGoogleSigningIn) {
                  CircularProgressIndicator(
                    color = MountainWhite,
                    modifier = Modifier.size(20.dp),
                    strokeWidth = 2.dp
                  )
                  Spacer(modifier = Modifier.width(10.dp))
                  Text(
                    text = "Authenticating Admin...",
                    style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
                  )
                } else {
                  // Google "G" representation
                  Surface(
                    shape = CircleShape,
                    color = MountainWhite,
                    modifier = Modifier.size(24.dp)
                  ) {
                    Box(contentAlignment = Alignment.Center) {
                      Text(
                        text = "G",
                        style = MaterialTheme.typography.titleSmall.copy(
                          fontWeight = FontWeight.Black,
                          color = Color(0xFF4285F4),
                          fontSize = 15.sp
                        )
                      )
                    }
                  }
                  Spacer(modifier = Modifier.width(12.dp))
                  Text(
                    text = "Sign in with Google (Live Admin)",
                    style = MaterialTheme.typography.titleSmall.copy(
                      fontWeight = FontWeight.Bold,
                      color = MountainWhite
                    )
                  )
                }
              }
            }

            // Divider
            Row(
              modifier = Modifier.fillMaxWidth(),
              verticalAlignment = Alignment.CenterVertically
            ) {
              HorizontalDivider(modifier = Modifier.weight(1f), color = MountainBorder)
              Text(
                text = "  OR PIN FALLBACK  ",
                style = MaterialTheme.typography.labelSmall.copy(
                  color = TextMuted,
                  fontWeight = FontWeight.Bold
                )
              )
              HorizontalDivider(modifier = Modifier.weight(1f), color = MountainBorder)
            }

            // Store Terminal ID Field
            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
              Text(
                text = "Store Hub Identifier",
                style = MaterialTheme.typography.labelMedium.copy(
                  fontWeight = FontWeight.SemiBold,
                  color = TextSecondarySlate
                )
              )
              OutlinedTextField(
                value = hubIdInput,
                onValueChange = { hubIdInput = it },
                leadingIcon = {
                  Icon(Icons.Default.Store, contentDescription = null, tint = BrandGreenPrimary)
                },
                singleLine = true,
                modifier = Modifier
                  .fillMaxWidth()
                  .testTag("merchant_hub_id_input"),
                colors = OutlinedTextFieldDefaults.colors(
                  focusedBorderColor = BrandGreenPrimary,
                  unfocusedBorderColor = MountainBorder
                )
              )
            }

            // Security Passcode Field
            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
              Text(
                text = "Store Security Passcode",
                style = MaterialTheme.typography.labelMedium.copy(
                  fontWeight = FontWeight.SemiBold,
                  color = TextSecondarySlate
                )
              )
              OutlinedTextField(
                value = passcodeInput,
                onValueChange = {
                  if (it.length <= 10) {
                    passcodeInput = it
                    errorMessage = null
                  }
                },
                leadingIcon = {
                  Icon(Icons.Default.Lock, contentDescription = null, tint = BrandGreenPrimary)
                },
                trailingIcon = {
                  IconButton(
                    onClick = { isPasswordVisible = !isPasswordVisible },
                    modifier = Modifier.testTag("merchant_password_toggle")
                  ) {
                    Icon(
                      imageVector = if (isPasswordVisible) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                      contentDescription = if (isPasswordVisible) "Hide Passcode" else "Show Passcode",
                      tint = TextMuted
                    )
                  }
                },
                placeholder = { Text("••••") },
                singleLine = true,
                isError = errorMessage != null,
                supportingText = {
                  if (errorMessage != null) {
                    Text(errorMessage!!, color = Color(0xFFDC2626))
                  }
                },
                visualTransformation = if (isPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.NumberPassword),
                modifier = Modifier
                  .fillMaxWidth()
                  .testTag("merchant_passcode_input"),
                colors = OutlinedTextFieldDefaults.colors(
                  focusedBorderColor = BrandGreenPrimary,
                  unfocusedBorderColor = MountainBorder
                )
              )
            }

            Spacer(modifier = Modifier.height(4.dp))

            // Sign In with Passcode Button
            Button(
              onClick = {
                val success = onAuthenticate(passcodeInput.trim())
                if (!success) {
                  errorMessage = "Invalid Store Security Passcode. Access restricted to authorized personnel."
                }
              },
              enabled = passcodeInput.isNotBlank(),
              modifier = Modifier
                .fillMaxWidth()
                .height(48.dp)
                .testTag("merchant_sign_in_btn"),
              colors = ButtonDefaults.buttonColors(
                containerColor = BrandGreenPrimary,
                contentColor = MountainWhite
              ),
              shape = RoundedCornerShape(12.dp)
            ) {
              Icon(Icons.Default.Security, contentDescription = null)
              Spacer(modifier = Modifier.width(8.dp))
              Text(
                text = "Unlock with Passcode",
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
              )
            }

            // Return to Store Link
            OutlinedButton(
              onClick = onBackToCustomerStore,
              modifier = Modifier
                .fillMaxWidth()
                .height(48.dp)
                .testTag("merchant_return_customer_btn"),
              shape = RoundedCornerShape(12.dp),
              border = androidx.compose.foundation.BorderStroke(1.dp, MountainBorder)
            ) {
              Text(
                text = "← Return to ShahpurGo Shopping App",
                style = MaterialTheme.typography.bodyMedium.copy(
                  color = TextSecondarySlate,
                  fontWeight = FontWeight.Medium
                )
              )
            }
          }
        }

        // Terminal Info & Security Details Card
        Card(
          shape = RoundedCornerShape(14.dp),
          colors = CardDefaults.cardColors(containerColor = BrandGreenSurface),
          border = androidx.compose.foundation.BorderStroke(1.dp, BrandGreenPrimary.copy(alpha = 0.2f)),
          modifier = Modifier.fillMaxWidth()
        ) {
          Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
              Icon(
                imageVector = Icons.Default.CheckCircle,
                contentDescription = null,
                tint = BrandGreenPrimary,
                modifier = Modifier.size(20.dp)
              )
              Spacer(modifier = Modifier.width(8.dp))
              Text(
                text = "Shahpur Central Fulfilment Hub",
                style = MaterialTheme.typography.titleSmall.copy(
                  fontWeight = FontWeight.Bold,
                  color = BrandGreenDark
                )
              )
            }
            Spacer(modifier = Modifier.height(6.dp))
            Text(
              text = "• Location: Main Bazar, Shahpur, Kangra HP 176206\n• Live Cloud Database order stream active\n• Dispatch team & real-time inventory tracking connected",
              style = MaterialTheme.typography.bodySmall.copy(
                color = TextSecondarySlate,
                lineHeight = 18.sp
              )
            )
          }
        }

        // Support Contact
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.Center,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Icon(Icons.Default.Info, contentDescription = null, tint = TextMuted, modifier = Modifier.size(14.dp))
          Spacer(modifier = Modifier.width(6.dp))
          Text(
            text = "Store Ops Support: +91 98160 54321 • hub@shahpurgo.in",
            style = MaterialTheme.typography.labelSmall.copy(color = TextMuted)
          )
        }
      }
    }
  }
}
