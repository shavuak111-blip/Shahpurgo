package com.example.model

import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.IgnoreExtraProperties
import com.google.firebase.firestore.ServerTimestamp

@IgnoreExtraProperties
data class FirestoreProduct(
  @DocumentId val id: String = "",
  val productId: String = "",
  val name: String = "",
  val description: String = "",
  val category: String = "GROCERY",
  val sellingPrice: Double = 0.0,
  val originalPrice: Double = 0.0,
  val stockQuantity: Int = 0,
  val unit: String = "1 pc",
  val isAvailable: Boolean = true,
  val shopId: String = "shahpur-central-hub",
  val shopName: String = "Shahpur Central Mart & Hub",
  val imageUrl: String = "",
  val adminUid: String = "",
  val adminEmail: String = "",
  @ServerTimestamp val createdAt: Timestamp? = null,
  @ServerTimestamp val updatedAt: Timestamp? = null
)
