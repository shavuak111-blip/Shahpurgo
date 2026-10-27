package com.example.ui.screens

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.MyLocation
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.Store
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.DeliveryLocation
import com.example.ui.theme.BrandGreenAccent
import com.example.ui.theme.BrandGreenContainer
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
import kotlin.math.roundToInt

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LocationPickerScreen(
  currentLocation: DeliveryLocation,
  savedLocations: List<DeliveryLocation>,
  onLocationSelected: (DeliveryLocation) -> Unit,
  onSaveCustomLocation: (area: String, address: String, landmark: String, lat: Double, lng: Double) -> Unit,
  onBack: () -> Unit,
  modifier: Modifier = Modifier
) {
  var selectedArea by remember { mutableStateOf(currentLocation.areaName) }
  var streetAddress by remember { mutableStateOf(currentLocation.fullAddress) }
  var landmark by remember { mutableStateOf(currentLocation.landmark) }
  var pinOffsetRatio by remember { mutableStateOf(Offset(0.5f, 0.5f)) }

  Scaffold(
    topBar = {
      TopAppBar(
        title = {
          Column {
            Text(
              text = "Select Delivery Location",
              style = MaterialTheme.typography.titleMedium.copy(
                fontWeight = FontWeight.Bold,
                color = MountainWhite
              )
            )
            Text(
              text = "Shahpur, Kangra, Himachal Pradesh",
              style = MaterialTheme.typography.labelSmall.copy(
                color = MountainWhite.copy(alpha = 0.85f)
              )
            )
          }
        },
        navigationIcon = {
          IconButton(onClick = onBack, modifier = Modifier.testTag("location_picker_back")) {
            Icon(
              imageVector = Icons.Default.ArrowBack,
              contentDescription = "Back",
              tint = MountainWhite
            )
          }
        },
        colors = TopAppBarDefaults.topAppBarColors(containerColor = BrandGreenPrimary)
      )
    }
  ) { paddingValues ->
    Column(
      modifier = modifier
        .fillMaxSize()
        .padding(paddingValues)
        .background(MountainSnowBackground)
        .verticalScroll(rememberScrollState())
    ) {
      // 1. Interactive Map Canvas of Shahpur & Kangra Valley
      Box(
        modifier = Modifier
          .fillMaxWidth()
          .height(280.dp)
          .testTag("shahpur_interactive_map")
          .background(Color(0xFFE8F5E9))
          .pointerInput(Unit) {
            detectTapGestures { tapOffset ->
              val rx = (tapOffset.x / size.width).coerceIn(0.1f, 0.9f)
              val ry = (tapOffset.y / size.height).coerceIn(0.1f, 0.9f)
              pinOffsetRatio = Offset(rx, ry)
            }
          }
      ) {
        // Kangra Valley Map Graphics (Roads, Mountain Ridge, Rivers, Shahpur Hub)
        Canvas(modifier = Modifier.fillMaxSize()) {
          val w = size.width
          val h = size.height

          // Valley green background shades
          drawRect(color = Color(0xFFE2F1E4))

          // Dhauladhar Mountains in the top background
          val mountains = Path().apply {
            moveTo(0f, h * 0.25f)
            lineTo(w * 0.2f, h * 0.08f)
            lineTo(w * 0.45f, h * 0.18f)
            lineTo(w * 0.7f, h * 0.05f)
            lineTo(w * 0.9f, h * 0.15f)
            lineTo(w, h * 0.10f)
            lineTo(w, 0f)
            lineTo(0f, 0f)
            close()
          }
          drawPath(mountains, color = Color(0xFFC7E3CE))

          // Snow caps
          val snow = Path().apply {
            moveTo(w * 0.17f, h * 0.11f)
            lineTo(w * 0.2f, h * 0.08f)
            lineTo(w * 0.23f, h * 0.11f)
            close()
            moveTo(w * 0.67f, h * 0.08f)
            lineTo(w * 0.7f, h * 0.05f)
            lineTo(w * 0.73f, h * 0.08f)
            close()
          }
          drawPath(snow, color = Color.White)

          // Main Highway (NH 154 Pathankot - Mandi highway passing through Shahpur)
          drawLine(
            color = Color(0xFFCBD5E1),
            strokeWidth = 14f,
            start = Offset(0f, h * 0.65f),
            end = Offset(w, h * 0.45f)
          )
          drawLine(
            color = Color(0xFFFBBF24),
            strokeWidth = 2f,
            start = Offset(0f, h * 0.65f),
            end = Offset(w, h * 0.45f)
          )

          // Secondary Roads
          // Dronacharya College Road
          drawLine(
            color = Color(0xFFE2E8F0),
            strokeWidth = 8f,
            start = Offset(w * 0.4f, h * 0.58f),
            end = Offset(w * 0.35f, h * 0.95f)
          )
          // Rait Link
          drawLine(
            color = Color(0xFFE2E8F0),
            strokeWidth = 8f,
            start = Offset(w * 0.65f, h * 0.52f),
            end = Offset(w * 0.85f, h * 0.85f)
          )

          // Shahpur Dark Store Hub Marker
          drawCircle(
            color = Color(0xFF0B7A48),
            radius = 18f,
            center = Offset(w * 0.48f, h * 0.55f)
          )
          drawCircle(
            color = Color.White,
            radius = 7f,
            center = Offset(w * 0.48f, h * 0.55f)
          )
        }

        // Hub Badge on Map
        Surface(
          shape = RoundedCornerShape(8.dp),
          color = BrandGreenPrimary,
          modifier = Modifier
            .align(Alignment.Center)
            .offset(y = (-35).dp)
        ) {
          Text(
            text = "Shahpur Central Hub",
            style = MaterialTheme.typography.labelSmall.copy(
              color = MountainWhite,
              fontWeight = FontWeight.Bold,
              fontSize = 9.sp
            ),
            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
          )
        }

        // Movable / Tappable Pin
        Box(
          modifier = Modifier
            .align(Alignment.TopStart)
            .offset {
              IntOffset(
                (pinOffsetRatio.x * 1000).roundToInt().coerceIn(40, 800),
                (pinOffsetRatio.y * 500).roundToInt().coerceIn(30, 480)
              )
            }
        ) {
          Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Surface(
              shape = RoundedCornerShape(8.dp),
              color = BrandGreenDark,
              shadowElevation = 4.dp
            ) {
              Text(
                text = "Deliver Here",
                style = MaterialTheme.typography.labelSmall.copy(
                  color = MountainWhite,
                  fontWeight = FontWeight.Bold,
                  fontSize = 9.sp
                ),
                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
              )
            }
            Icon(
              imageVector = Icons.Default.LocationOn,
              contentDescription = "Pin",
              tint = RewardGold,
              modifier = Modifier.size(34.dp)
            )
          }
        }

        // Tap to position guide
        Surface(
          shape = RoundedCornerShape(20.dp),
          color = MountainWhite.copy(alpha = 0.9f),
          modifier = Modifier
            .align(Alignment.BottomEnd)
            .padding(10.dp)
        ) {
          Row(
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp),
            verticalAlignment = Alignment.CenterVertically
          ) {
            Icon(
              imageVector = Icons.Default.MyLocation,
              contentDescription = null,
              tint = BrandGreenPrimary,
              modifier = Modifier.size(14.dp)
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
              text = "Tap map to pinpoint in Shahpur",
              style = MaterialTheme.typography.labelSmall.copy(
                fontWeight = FontWeight.SemiBold,
                color = BrandGreenDark
              )
            )
          }
        }
      }

      // 2. Saved / Preset Locations in Shahpur
      Column(modifier = Modifier.padding(16.dp)) {
        Text(
          text = "Select From Shahpur Localities",
          style = MaterialTheme.typography.titleMedium.copy(
            fontWeight = FontWeight.Bold,
            color = TextPrimaryDark
          )
        )

        Spacer(modifier = Modifier.height(8.dp))

        LazyRow(
          horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
          items(savedLocations) { loc ->
            val isSelected = selectedArea == loc.areaName
            Surface(
              shape = RoundedCornerShape(12.dp),
              color = if (isSelected) BrandGreenPrimary else MountainWhite,
              border = androidx.compose.foundation.BorderStroke(
                1.dp,
                if (isSelected) BrandGreenPrimary else MountainBorder
              ),
              modifier = Modifier
                .testTag("preset_location_${loc.id}")
                .clip(RoundedCornerShape(12.dp))
                .clickable {
                  selectedArea = loc.areaName
                  streetAddress = loc.fullAddress
                  landmark = loc.landmark
                  onLocationSelected(loc)
                }
            ) {
              Row(
                modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
              ) {
                val icon = when (loc.tag) {
                  "Home" -> Icons.Default.Home
                  "Hostel / PG" -> Icons.Default.School
                  else -> Icons.Default.Store
                }
                Icon(
                  imageVector = icon,
                  contentDescription = null,
                  tint = if (isSelected) MountainWhite else BrandGreenPrimary,
                  modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Column {
                  Text(
                    text = loc.areaName,
                    style = MaterialTheme.typography.labelMedium.copy(
                      fontWeight = FontWeight.Bold,
                      color = if (isSelected) MountainWhite else TextPrimaryDark
                    )
                  )
                  Text(
                    text = loc.tag,
                    style = MaterialTheme.typography.labelSmall.copy(
                      fontSize = 9.sp,
                      color = if (isSelected) MountainWhite.copy(alpha = 0.8f) else TextMuted
                    )
                  )
                }
              }
            }
          }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // 3. Address Form
        Card(
          shape = RoundedCornerShape(14.dp),
          colors = CardDefaults.cardColors(containerColor = MountainCardSurface),
          border = androidx.compose.foundation.BorderStroke(1.dp, MountainBorder),
          modifier = Modifier.fillMaxWidth()
        ) {
          Column(modifier = Modifier.padding(14.dp)) {
            Text(
              text = "Address & Landmark Details",
              style = MaterialTheme.typography.titleMedium.copy(
                fontWeight = FontWeight.Bold,
                color = TextPrimaryDark
              )
            )

            Spacer(modifier = Modifier.height(10.dp))

            OutlinedTextField(
              value = selectedArea,
              onValueChange = { selectedArea = it },
              label = { Text("Area / Locality in Shahpur") },
              modifier = Modifier
                .fillMaxWidth()
                .testTag("input_area_name"),
              shape = RoundedCornerShape(10.dp),
              colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = BrandGreenPrimary,
                unfocusedBorderColor = MountainBorder
              )
            )

            Spacer(modifier = Modifier.height(10.dp))

            OutlinedTextField(
              value = streetAddress,
              onValueChange = { streetAddress = it },
              label = { Text("House / Flat / Hostel No. & Street") },
              modifier = Modifier
                .fillMaxWidth()
                .testTag("input_street_address"),
              shape = RoundedCornerShape(10.dp),
              colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = BrandGreenPrimary,
                unfocusedBorderColor = MountainBorder
              )
            )

            Spacer(modifier = Modifier.height(10.dp))

            OutlinedTextField(
              value = landmark,
              onValueChange = { landmark = it },
              label = { Text("Nearby Landmark (e.g. Near Bus Stand / SBI / College Gate)") },
              modifier = Modifier
                .fillMaxWidth()
                .testTag("input_landmark"),
              shape = RoundedCornerShape(10.dp),
              colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = BrandGreenPrimary,
                unfocusedBorderColor = MountainBorder
              )
            )

            Spacer(modifier = Modifier.height(16.dp))

            Button(
              onClick = {
                onSaveCustomLocation(
                  selectedArea,
                  streetAddress,
                  landmark,
                  32.2244,
                  76.1772
                )
                onBack()
              },
              modifier = Modifier
                .fillMaxWidth()
                .height(48.dp)
                .testTag("confirm_delivery_location_btn"),
              shape = RoundedCornerShape(10.dp),
              colors = ButtonDefaults.buttonColors(
                containerColor = BrandGreenPrimary,
                contentColor = MountainWhite
              )
            ) {
              Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                  imageVector = Icons.Default.Check,
                  contentDescription = null,
                  modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                  text = "Confirm & Deliver to Shahpur Pin",
                  style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                )
              }
            }
          }
        }
      }
    }
  }
}
