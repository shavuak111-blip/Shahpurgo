package com.example.model

import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.IgnoreExtraProperties
import com.google.firebase.firestore.ServerTimestamp

@IgnoreExtraProperties
data class FirestoreOrder(
  @DocumentId val id: String = "",
  val orderId: String = "",
  val customerId: String = "",
  val customerName: String = "",
  val customerPhone: String = "",
  val deliveryAddress: String = "",
  val landmark: String = "",
  val itemsSummary: String = "",
  val itemsCount: Int = 0,
  val totalAmount: Double = 0.0,
  val status: String = "PLACED",
  val paymentMethod: String = "COD",
  val notes: String = "",
  val shopId: String = "shahpur-central-hub",
  val shopName: String = "Shahpur Central Mart & Hub",
  val cancellationReason: String = "",
  val rating: Double = 0.0,
  val review: String = "",
  @ServerTimestamp val createdAt: Timestamp? = null,
  @ServerTimestamp val updatedAt: Timestamp? = null
)

fun FirestoreOrder.toOrder(): Order {
  val orderStatus = when (status.uppercase()) {
    "PLACED" -> OrderStatus.PLACED
    "ACCEPTED" -> OrderStatus.ACCEPTED
    "CONFIRMED" -> OrderStatus.CONFIRMED
    "PREPARING", "PACKING" -> OrderStatus.PACKING
    "PACKED" -> OrderStatus.PACKED
    "ON_THE_WAY", "DISPATCHED", "OUT_FOR_DELIVERY" -> OrderStatus.OUT_FOR_DELIVERY
    "DELIVERED" -> OrderStatus.DELIVERED
    "CANCELLED" -> OrderStatus.CANCELLED
    else -> OrderStatus.CONFIRMED
  }

  val displayId = if (orderId.isNotBlank()) orderId else if (id.isNotBlank()) id else "SPG-LIVE"

  return Order(
    id = displayId,
    items = emptyList(),
    subtotal = (totalAmount - 27.0).coerceAtLeast(0.0),
    deliveryFee = 25.0,
    tipAmount = 0.0,
    platformFee = 2.0,
    total = totalAmount,
    status = orderStatus,
    timestamp = createdAt?.toDate()?.time ?: System.currentTimeMillis(),
    deliveryLocation = DeliveryLocation(
      id = "loc_${id.ifBlank { "default" }}",
      tag = "Home",
      areaName = "Shahpur",
      fullAddress = deliveryAddress.ifBlank { "Shahpur, Kangra HP" },
      landmark = landmark.ifBlank { "Main Bazaar" },
      latitude = 32.2276,
      longitude = 76.1661
    ),
    paymentMethod = paymentMethod.ifBlank { "Cash on Delivery (COD)" },
    deliveryPartnerName = "Raman Kumar (Shahpur Local)",
    deliveryPartnerPhone = "+91 98160 54321",
    etaMinutes = when (orderStatus) {
      OrderStatus.PLACED -> 25
      OrderStatus.ACCEPTED -> 22
      OrderStatus.CONFIRMED -> 20
      OrderStatus.PACKING -> 15
      OrderStatus.PACKED -> 12
      OrderStatus.OUT_FOR_DELIVERY -> 8
      OrderStatus.DELIVERED -> 0
      OrderStatus.CANCELLED -> 0
      else -> 20
    },
    shopId = shopId.ifBlank { "shahpur-central-hub" },
    shopName = shopName.ifBlank { "Shahpur Central Mart & Hub" },
    cancellationReason = cancellationReason,
    rating = rating.toFloat(),
    review = review,
    isPaymentVerified = true
  )
}
