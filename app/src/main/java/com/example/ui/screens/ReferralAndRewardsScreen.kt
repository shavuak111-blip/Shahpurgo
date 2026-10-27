package com.example.ui.screens

import android.content.Context
import android.content.Intent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CardGiftcard
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.MonetizationOn
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.TouchApp
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.components.MountainSilhouettes
import com.example.ui.theme.BrandGreenAccent
import com.example.ui.theme.BrandGreenContainer
import com.example.ui.theme.BrandGreenDark
import com.example.ui.theme.BrandGreenLight
import com.example.ui.theme.BrandGreenPrimary
import com.example.ui.theme.BrandGreenSurface
import com.example.ui.theme.MountainBorder
import com.example.ui.theme.MountainCardSurface
import com.example.ui.theme.MountainSnowBackground
import com.example.ui.theme.MountainWhite
import com.example.ui.theme.RewardGold
import com.example.ui.theme.RewardGoldContainer
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimaryDark
import com.example.ui.theme.TextSecondarySlate
import com.example.viewmodel.GroceryUiState

@Composable
fun ReferralAndRewardsScreen(
  uiState: GroceryUiState,
  onScratchCard: () -> Unit,
  modifier: Modifier = Modifier
) {
  val clipboardManager = LocalClipboardManager.current
  val context = LocalContext.current

  Column(
    modifier = modifier
      .fillMaxSize()
      .background(MountainSnowBackground)
      .verticalScroll(rememberScrollState())
      .padding(14.dp),
    verticalArrangement = Arrangement.spacedBy(14.dp)
  ) {
    // 1. Rewards Hero Header
    Card(
      shape = RoundedCornerShape(16.dp),
      colors = CardDefaults.cardColors(containerColor = BrandGreenDark),
      modifier = Modifier
        .fillMaxWidth()
        .testTag("referral_hero_card")
    ) {
      Box(modifier = Modifier.fillMaxWidth()) {
        MountainSilhouettes(
          modifier = Modifier
            .fillMaxWidth()
            .height(80.dp)
            .align(Alignment.BottomCenter),
          tint = Color.White.copy(alpha = 0.15f)
        )

        Column(
          modifier = Modifier
            .fillMaxWidth()
            .padding(18.dp)
        ) {
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Column {
              Text(
                text = "ShahpurGo Rewards",
                style = MaterialTheme.typography.headlineMedium.copy(
                  fontWeight = FontWeight.ExtraBold,
                  color = MountainWhite
                )
              )
              Text(
                text = "Shahpur Ka Apna Delivery App • Refer & Earn",
                style = MaterialTheme.typography.labelSmall.copy(
                  color = MountainWhite.copy(alpha = 0.85f)
                )
              )
            }

            Box(
              modifier = Modifier
                .size(44.dp)
                .background(RewardGold, CircleShape),
              contentAlignment = Alignment.Center
            ) {
              Icon(
                imageVector = Icons.Default.MonetizationOn,
                contentDescription = null,
                tint = BrandGreenDark,
                modifier = Modifier.size(28.dp)
              )
            }
          }

          Spacer(modifier = Modifier.height(14.dp))

          // Wallet Balance Row
          Surface(
            shape = RoundedCornerShape(12.dp),
            color = MountainWhite.copy(alpha = 0.15f),
            modifier = Modifier.fillMaxWidth()
          ) {
            Row(
              modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
              horizontalArrangement = Arrangement.SpaceBetween,
              verticalAlignment = Alignment.CenterVertically
            ) {
              Column {
                Text(
                  text = "Available Coins Balance",
                  style = MaterialTheme.typography.labelSmall.copy(color = MountainWhite.copy(alpha = 0.8f))
                )
                Text(
                  text = "${uiState.user.coinsBalance} Coins (₹${uiState.user.coinsBalance})",
                  style = MaterialTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight.ExtraBold,
                    color = RewardGold
                  )
                )
              }

              Surface(
                shape = RoundedCornerShape(8.dp),
                color = RewardGold
              ) {
                Text(
                  text = "1 Coin = ₹1",
                  style = MaterialTheme.typography.labelSmall.copy(
                    color = BrandGreenDark,
                    fontWeight = FontWeight.Bold
                  ),
                  modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                )
              }
            }
          }
        }
      }
    }

    // 2. Interactive Scratch Card Feature
    Card(
      shape = RoundedCornerShape(14.dp),
      colors = CardDefaults.cardColors(containerColor = MountainCardSurface),
      border = androidx.compose.foundation.BorderStroke(1.dp, MountainBorder),
      modifier = Modifier
        .fillMaxWidth()
        .testTag("scratch_card_container")
    ) {
      Column(
        modifier = Modifier.padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
      ) {
        Row(
          modifier = Modifier.fillMaxWidth(),
          verticalAlignment = Alignment.CenterVertically
        ) {
          Icon(
            imageVector = Icons.Default.CardGiftcard,
            contentDescription = null,
            tint = BrandGreenPrimary,
            modifier = Modifier.size(20.dp)
          )
          Spacer(modifier = Modifier.width(8.dp))
          Text(
            text = "Daily Shahpur Surprise Scratch Card",
            style = MaterialTheme.typography.titleMedium.copy(
              fontWeight = FontWeight.Bold,
              color = TextPrimaryDark
            )
          )
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Scratch card surface
        Box(
          modifier = Modifier
            .fillMaxWidth()
            .height(110.dp)
            .clip(RoundedCornerShape(12.dp))
            .border(
              2.dp,
              if (uiState.scratchCardRevealed) RewardGold else BrandGreenPrimary,
              RoundedCornerShape(12.dp)
            )
            .background(
              if (uiState.scratchCardRevealed) {
                Brush.linearGradient(listOf(RewardGoldContainer, Color(0xFFFEF08A)))
              } else {
                Brush.linearGradient(listOf(BrandGreenPrimary, BrandGreenDark))
              }
            )
            .clickable { onScratchCard() },
          contentAlignment = Alignment.Center
        ) {
          if (!uiState.scratchCardRevealed) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
              Icon(
                imageVector = Icons.Default.TouchApp,
                contentDescription = null,
                tint = MountainWhite,
                modifier = Modifier.size(28.dp)
              )
              Spacer(modifier = Modifier.height(4.dp))
              Text(
                text = "Tap to Scratch & Reveal Cashback",
                style = MaterialTheme.typography.titleMedium.copy(
                  fontWeight = FontWeight.Bold,
                  color = MountainWhite
                )
              )
              Text(
                text = "Special launch gift for Kangra residents",
                style = MaterialTheme.typography.labelSmall.copy(color = MountainWhite.copy(alpha = 0.8f))
              )
            }
          } else {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
              Text(
                text = "🎉 YOU WON ₹${uiState.scratchedRewardAmount}! 🎉",
                style = MaterialTheme.typography.titleLarge.copy(
                  fontWeight = FontWeight.ExtraBold,
                  color = BrandGreenDark
                )
              )
              Spacer(modifier = Modifier.height(4.dp))
              Text(
                text = "Added to your ShahpurGo wallet instantly!",
                style = MaterialTheme.typography.labelSmall.copy(
                  color = BrandGreenPrimary,
                  fontWeight = FontWeight.Bold
                )
              )
            }
          }
        }
      }
    }

    // 3. Referral Program Card
    Card(
      shape = RoundedCornerShape(14.dp),
      colors = CardDefaults.cardColors(containerColor = MountainCardSurface),
      border = androidx.compose.foundation.BorderStroke(1.dp, MountainBorder),
      modifier = Modifier
        .fillMaxWidth()
        .testTag("referral_code_card")
    ) {
      Column(modifier = Modifier.padding(16.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically) {
          Icon(
            imageVector = Icons.Default.People,
            contentDescription = null,
            tint = BrandGreenPrimary,
            modifier = Modifier.size(20.dp)
          )
          Spacer(modifier = Modifier.width(8.dp))
          Text(
            text = "Refer Friends in Shahpur & Kangra",
            style = MaterialTheme.typography.titleMedium.copy(
              fontWeight = FontWeight.Bold,
              color = TextPrimaryDark
            )
          )
        }

        Spacer(modifier = Modifier.height(6.dp))

        Text(
          text = "Give ₹50, Get ₹50! When a friend in Shahpur places their first order with your code, you both get ₹50 in coins.",
          style = MaterialTheme.typography.bodyMedium.copy(color = TextSecondarySlate)
        )

        Spacer(modifier = Modifier.height(14.dp))

        // Code Box
        Surface(
          shape = RoundedCornerShape(12.dp),
          color = BrandGreenSurface,
          border = androidx.compose.foundation.BorderStroke(1.dp, BrandGreenContainer),
          modifier = Modifier.fillMaxWidth()
        ) {
          Row(
            modifier = Modifier
              .fillMaxWidth()
              .padding(horizontal = 14.dp, vertical = 10.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Column {
              Text(
                text = "YOUR REFERRAL CODE",
                style = MaterialTheme.typography.labelSmall.copy(color = TextMuted)
              )
              Text(
                text = uiState.user.referralCode,
                style = MaterialTheme.typography.titleLarge.copy(
                  fontWeight = FontWeight.ExtraBold,
                  color = BrandGreenDark,
                  letterSpacing = 2.sp
                )
              )
            }

            Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
              Button(
                onClick = {
                  clipboardManager.setText(AnnotatedString(uiState.user.referralCode))
                },
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.buttonColors(containerColor = BrandGreenPrimary)
              ) {
                Icon(Icons.Default.ContentCopy, contentDescription = null, modifier = Modifier.size(14.dp))
                Spacer(modifier = Modifier.width(4.dp))
                Text("Copy", style = MaterialTheme.typography.labelSmall)
              }

              Button(
                onClick = {
                  val sendIntent: Intent = Intent().apply {
                    action = Intent.ACTION_SEND
                    putExtra(
                      Intent.EXTRA_TEXT,
                      "Hey! Use my code ${uiState.user.referralCode} on ShahpurGo - Shahpur Ka Apna Delivery App to get ₹50 off your first grocery order in Kangra!"
                    )
                    type = "text/plain"
                  }
                  val shareIntent = Intent.createChooser(sendIntent, "Share ShahpurGo Code")
                  context.startActivity(shareIntent)
                },
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.buttonColors(containerColor = RewardGold, contentColor = BrandGreenDark)
              ) {
                Icon(Icons.Default.Share, contentDescription = null, modifier = Modifier.size(14.dp))
                Spacer(modifier = Modifier.width(4.dp))
                Text("Share", style = MaterialTheme.typography.labelSmall)
              }
            }
          }
        }
      }
    }

    // 4. How Referral Works
    Card(
      shape = RoundedCornerShape(14.dp),
      colors = CardDefaults.cardColors(containerColor = MountainCardSurface),
      border = androidx.compose.foundation.BorderStroke(1.dp, MountainBorder),
      modifier = Modifier.fillMaxWidth()
    ) {
      Column(modifier = Modifier.padding(16.dp)) {
        Text(
          text = "How It Works in 3 Simple Steps",
          style = MaterialTheme.typography.titleMedium.copy(
            fontWeight = FontWeight.Bold,
            color = TextPrimaryDark
          )
        )

        Spacer(modifier = Modifier.height(10.dp))

        val steps = listOf(
          "1. Share your code" to "Send your unique code to friends, family, or college peers in Shahpur.",
          "2. Friend orders groceries" to "They receive instant ₹50 discount on their first fresh order.",
          "3. You earn ₹50 coins" to "₹50 coins are immediately credited to your ShahpurGo wallet!"
        )

        steps.forEach { (title, desc) ->
          Row(
            modifier = Modifier
              .fillMaxWidth()
              .padding(vertical = 4.dp),
            verticalAlignment = Alignment.Top
          ) {
            Box(
              modifier = Modifier
                .size(20.dp)
                .background(BrandGreenContainer, CircleShape),
              contentAlignment = Alignment.Center
            ) {
              Text(
                text = title.take(1),
                style = MaterialTheme.typography.labelSmall.copy(
                  color = BrandGreenDark,
                  fontWeight = FontWeight.Bold
                )
              )
            }
            Spacer(modifier = Modifier.width(10.dp))
            Column {
              Text(
                text = title,
                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.SemiBold)
              )
              Text(
                text = desc,
                style = MaterialTheme.typography.bodySmall.copy(color = TextMuted)
              )
            }
          }
        }
      }
    }

    Spacer(modifier = Modifier.height(10.dp))
  }
}
