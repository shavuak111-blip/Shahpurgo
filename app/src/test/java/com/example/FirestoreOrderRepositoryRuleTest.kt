package com.example

import com.example.base.FirestoreEmulatorTestBase
import com.example.data.FirestoreOrderRepository
import com.example.model.FirestoreOrder
import com.google.firebase.firestore.FirebaseFirestoreException
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Assert.fail
import org.junit.Test

class FirestoreOrderRepositoryRuleTest : FirestoreEmulatorTestBase() {

  @Test
  fun unauthenticated_cannot_create_order() = runBlocking {
    auth.signOut()
    val repo = FirestoreOrderRepository(firestore, auth)

    val order = FirestoreOrder(
      orderId = "TEST-ORD-001",
      customerId = "fake_cust",
      customerName = "Test User",
      customerPhone = "+919816012345",
      deliveryAddress = "Shahpur Bazar",
      totalAmount = 250.0,
      status = "PLACED"
    )

    try {
      repo.createOrder(order)
      fail("Expected PERMISSION_DENIED when unauthenticated")
    } catch (e: Exception) {
      val firestoreEx = e.unwrapFirestoreException()
      assertNotNull("Expected FirebaseFirestoreException but got $e", firestoreEx)
      assertEquals(FirebaseFirestoreException.Code.PERMISSION_DENIED, firestoreEx?.code)
    }
  }

  @Test
  fun authenticated_customer_can_create_order() = runBlocking {
    val uid = signInTestUser("customer@shahpurgo.com")
    val repo = FirestoreOrderRepository(firestore, auth)

    val order = FirestoreOrder(
      orderId = "TEST-ORD-002",
      customerId = uid,
      customerName = "Aman Sharma",
      customerPhone = "+919816087654",
      deliveryAddress = "Ward No 3, Near Bus Stand, Shahpur",
      landmark = "State Bank",
      itemsSummary = "Kangra Tea, Fresh Milk",
      itemsCount = 2,
      totalAmount = 320.0,
      status = "CONFIRMED",
      paymentMethod = "COD",
      notes = "Ring bell"
    )

    repo.createOrder(order)

    val snapshot = firestore.collection("orders").document("TEST-ORD-002").get().result
    assertTrue(snapshot.exists())
    assertEquals("Aman Sharma", snapshot.getString("customerName"))
    assertEquals("CONFIRMED", snapshot.getString("status"))
  }

  @Test
  fun admin_can_update_order_status() = runBlocking {
    val adminUid = signInTestUser("admin@shahpurgo.com")
    val repo = FirestoreOrderRepository(firestore, auth)

    val order = FirestoreOrder(
      orderId = "TEST-ORD-003",
      customerId = adminUid,
      customerName = "Pooja Devi",
      customerPhone = "+919816099887",
      deliveryAddress = "Chambi Road, Shahpur",
      totalAmount = 180.0,
      status = "CONFIRMED"
    )
    repo.createOrder(order)

    // Admin updates status to PACKED
    repo.updateOrderStatus("TEST-ORD-003", "PACKED")

    val updatedSnapshot = firestore.collection("orders").document("TEST-ORD-003").get().result
    assertEquals("PACKED", updatedSnapshot.getString("status"))
  }
}
