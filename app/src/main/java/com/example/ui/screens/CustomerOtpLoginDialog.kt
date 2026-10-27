package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.VerifiedUser
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
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.ui.theme.BrandGreenDark
import com.example.ui.theme.BrandGreenPrimary
import com.example.ui.theme.BrandGreenSurface
import com.example.ui.theme.MountainCardSurface
import com.example.ui.theme.MountainWhite
import com.example.ui.theme.TextMuted
import com.example.ui.theme.WarningRed

@Composable
fun CustomerOtpLoginDialog(
  isOtpSent: Boolean,
  phoneInput: String,
  otpInput: String,
  loginError: String?,
  onPhoneChange: (String) -> Unit,
  onOtpChange: (String) -> Unit,
  onSendOtp: () -> Unit,
  onVerifyOtp: (String) -> Unit,
  onGoogleSignIn: () -> Unit = {},
  isGoogleSigningIn: Boolean = false,
  onDismiss: () -> Unit
) {
  Dialog(onDismissRequest = onDismiss) {
    Card(
      shape = RoundedCornerShape(24.dp),
      colors = CardDefaults.cardColors(containerColor = MountainWhite),
      elevation = CardDefaults.cardElevation(defaultElevation = 10.dp),
      modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 8.dp)
        .testTag("customer_otp_dialog")
    ) {
      Column(
        modifier = Modifier
          .fillMaxWidth()
          .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
      ) {
        // Header
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
              modifier = Modifier
                .size(40.dp)
                .clip(CircleShape)
                .background(BrandGreenSurface),
              contentAlignment = Alignment.Center
            ) {
              Icon(
                imageVector = if (isOtpSent) Icons.Default.Lock else Icons.Default.Phone,
                contentDescription = null,
                tint = BrandGreenDark,
                modifier = Modifier.size(20.dp)
              )
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column {
              Text(
                text = if (isOtpSent) "Enter Verification Code" else "Mobile Login",
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                color = BrandGreenDark
              )
              Text(
                text = "ShahpurGo Customer Account",
                style = MaterialTheme.typography.bodySmall,
                color = TextMuted
              )
            }
          }

          IconButton(
            onClick = onDismiss,
            modifier = Modifier.size(36.dp).testTag("close_otp_dialog_button")
          ) {
            Icon(Icons.Default.Close, contentDescription = "Close", tint = TextMuted)
          }
        }

        Spacer(modifier = Modifier.height(20.dp))

        if (!isOtpSent) {
          Text(
            text = "Enter your 10-digit mobile number to receive a secure SMS OTP verification code.",
            style = MaterialTheme.typography.bodyMedium,
            color = Color(0xFF374151),
            textAlign = TextAlign.Start,
            modifier = Modifier.fillMaxWidth()
          )

          Spacer(modifier = Modifier.height(16.dp))

          OutlinedTextField(
            value = phoneInput,
            onValueChange = { if (it.length <= 10) onPhoneChange(it.filter { char -> char.isDigit() }) },
            label = { Text("Mobile Number") },
            placeholder = { Text("98160 12345") },
            prefix = { Text("+91 ", fontWeight = FontWeight.Bold, color = BrandGreenDark) },
            leadingIcon = { Icon(Icons.Default.Phone, contentDescription = null, tint = BrandGreenPrimary) },
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
            shape = RoundedCornerShape(14.dp),
            modifier = Modifier
              .fillMaxWidth()
              .testTag("customer_phone_input")
          )

          Spacer(modifier = Modifier.height(20.dp))

          Button(
            onClick = onSendOtp,
            enabled = phoneInput.length == 10,
            colors = ButtonDefaults.buttonColors(
              containerColor = BrandGreenPrimary,
              disabledContainerColor = BrandGreenPrimary.copy(alpha = 0.5f)
            ),
            shape = RoundedCornerShape(14.dp),
            modifier = Modifier
              .fillMaxWidth()
              .height(50.dp)
              .testTag("send_otp_button")
          ) {
            Text("Send OTP Verification", fontSize = 15.sp, fontWeight = FontWeight.SemiBold)
          }

          Spacer(modifier = Modifier.height(16.dp))

          Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
          ) {
            HorizontalDivider(modifier = Modifier.weight(1f), color = Color(0xFFE5E7EB))
            Text(
              text = "  OR  ",
              style = MaterialTheme.typography.labelSmall,
              color = TextMuted,
              fontWeight = FontWeight.Bold
            )
            HorizontalDivider(modifier = Modifier.weight(1f), color = Color(0xFFE5E7EB))
          }

          Spacer(modifier = Modifier.height(16.dp))

          OutlinedButton(
            onClick = onGoogleSignIn,
            shape = RoundedCornerShape(14.dp),
            modifier = Modifier
              .fillMaxWidth()
              .height(50.dp)
              .testTag("dialog_google_sign_in_btn"),
            colors = ButtonDefaults.outlinedButtonColors(containerColor = MountainCardSurface)
          ) {
            if (isGoogleSigningIn) {
              CircularProgressIndicator(modifier = Modifier.size(20.dp), strokeWidth = 2.dp, color = BrandGreenDark)
            } else {
              Icon(
                imageVector = Icons.Default.CheckCircle,
                contentDescription = null,
                tint = BrandGreenPrimary,
                modifier = Modifier.size(20.dp)
              )
              Spacer(modifier = Modifier.width(8.dp))
              Text("Continue with Google", fontSize = 15.sp, fontWeight = FontWeight.SemiBold, color = BrandGreenDark)
            }
          }
        } else {
          Text(
            text = "Enter the 6-digit code sent to +91 $phoneInput",
            style = MaterialTheme.typography.bodyMedium,
            color = Color(0xFF374151),
            textAlign = TextAlign.Start,
            modifier = Modifier.fillMaxWidth()
          )

          Spacer(modifier = Modifier.height(16.dp))

          OutlinedTextField(
            value = otpInput,
            onValueChange = { if (it.length <= 6) onOtpChange(it.filter { char -> char.isDigit() }) },
            label = { Text("6-Digit OTP") },
            placeholder = { Text("••••••") },
            leadingIcon = { Icon(Icons.Default.VerifiedUser, contentDescription = null, tint = BrandGreenPrimary) },
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.NumberPassword),
            shape = RoundedCornerShape(14.dp),
            modifier = Modifier
              .fillMaxWidth()
              .testTag("customer_otp_input")
          )

          Spacer(modifier = Modifier.height(20.dp))

          Button(
            onClick = { onVerifyOtp(otpInput) },
            enabled = otpInput.length == 6,
            colors = ButtonDefaults.buttonColors(
              containerColor = BrandGreenPrimary,
              disabledContainerColor = BrandGreenPrimary.copy(alpha = 0.5f)
            ),
            shape = RoundedCornerShape(14.dp),
            modifier = Modifier
              .fillMaxWidth()
              .height(50.dp)
              .testTag("verify_otp_button")
          ) {
            Text("Verify & Continue", fontSize = 15.sp, fontWeight = FontWeight.SemiBold)
          }

          Spacer(modifier = Modifier.height(8.dp))

          TextButton(
            onClick = onSendOtp,
            modifier = Modifier.testTag("resend_otp_button")
          ) {
            Text("Resend Code", color = BrandGreenDark, fontWeight = FontWeight.Medium)
          }
        }

        // Error message banner
        if (loginError != null) {
          Spacer(modifier = Modifier.height(12.dp))
          Text(
            text = loginError,
            color = WarningRed,
            style = MaterialTheme.typography.bodySmall,
            fontWeight = FontWeight.Medium,
            textAlign = TextAlign.Center
          )
        }
      }
    }
  }
}
