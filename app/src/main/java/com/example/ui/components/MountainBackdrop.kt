package com.example.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.ui.theme.BrandGreenDark
import com.example.ui.theme.BrandGreenPrimary

/**
 * Subtle mountain silhouette decorative backdrop inspired by the
 * Dhauladhar Himalayan mountain range of Kangra Valley, Himachal Pradesh.
 */
@Composable
fun MountainSilhouettes(
  modifier: Modifier = Modifier,
  height: Dp = 60.dp,
  tint: Color = Color.White.copy(alpha = 0.15f)
) {
  Canvas(
    modifier = modifier
      .fillMaxWidth()
      .height(height)
  ) {
    val w = size.width
    val h = size.height

    // Distant mountain ridge
    val farRidge = Path().apply {
      moveTo(0f, h * 0.7f)
      lineTo(w * 0.15f, h * 0.35f)
      lineTo(w * 0.32f, h * 0.55f)
      lineTo(w * 0.5f, h * 0.2f)
      lineTo(w * 0.68f, h * 0.45f)
      lineTo(w * 0.85f, h * 0.25f)
      lineTo(w, h * 0.6f)
      lineTo(w, h)
      lineTo(0f, h)
      close()
    }
    drawPath(farRidge, color = tint.copy(alpha = 0.08f))

    // Fore mountain peaks
    val nearRidge = Path().apply {
      moveTo(0f, h * 0.85f)
      lineTo(w * 0.2f, h * 0.5f)
      lineTo(w * 0.4f, h * 0.7f)
      lineTo(w * 0.62f, h * 0.4f)
      lineTo(w * 0.8f, h * 0.65f)
      lineTo(w, h * 0.5f)
      lineTo(w, h)
      lineTo(0f, h)
      close()
    }
    drawPath(nearRidge, color = tint.copy(alpha = 0.14f))

    // Snow cap highlights
    val snowCap1 = Path().apply {
      moveTo(w * 0.47f, h * 0.28f)
      lineTo(w * 0.5f, h * 0.2f)
      lineTo(w * 0.53f, h * 0.28f)
      close()
    }
    drawPath(snowCap1, color = Color.White.copy(alpha = 0.25f))

    val snowCap2 = Path().apply {
      moveTo(w * 0.59f, h * 0.47f)
      lineTo(w * 0.62f, h * 0.4f)
      lineTo(w * 0.65f, h * 0.47f)
      close()
    }
    drawPath(snowCap2, color = Color.White.copy(alpha = 0.22f))
  }
}

@Composable
fun ShahpurGoBrandLogo(
  modifier: Modifier = Modifier
) {
  Canvas(modifier = modifier) {
    val w = size.width
    val h = size.height

    // Mountain peaks behind bag
    val peakPath = Path().apply {
      moveTo(w * 0.15f, h * 0.75f)
      lineTo(w * 0.45f, h * 0.25f)
      lineTo(w * 0.65f, h * 0.55f)
      lineTo(w * 0.85f, h * 0.28f)
      lineTo(w * 0.98f, h * 0.75f)
      close()
    }
    drawPath(
      path = peakPath,
      brush = Brush.verticalGradient(
        colors = listOf(Color(0xFF34D399), Color(0xFF059669))
      )
    )

    // Bag base
    val bagPath = Path().apply {
      moveTo(w * 0.25f, h * 0.52f)
      lineTo(w * 0.75f, h * 0.52f)
      lineTo(w * 0.70f, h * 0.88f)
      lineTo(w * 0.30f, h * 0.88f)
      close()
    }
    drawPath(bagPath, color = Color.White)

    // Speed delivery mark
    val arrowPath = Path().apply {
      moveTo(w * 0.40f, h * 0.65f)
      lineTo(w * 0.58f, h * 0.70f)
      lineTo(w * 0.40f, h * 0.75f)
      close()
    }
    drawPath(arrowPath, color = Color(0xFFF59E0B))
  }
}
