package com.example.util

import java.security.MessageDigest

object SecurityUtils {
  private const val SALT = "shahpurgo_pin_salt_v1"

  /**
   * Computes salted SHA-256 hash for secure PIN storage and comparison.
   * Plain text passwords or PINs are NEVER stored.
   */
  fun hashPin(pin: String): String {
    val inputWithSalt = "$SALT:$pin"
    val bytes = MessageDigest.getInstance("SHA-256").digest(inputWithSalt.toByteArray(Charsets.UTF_8))
    return bytes.joinToString("") { "%02x".format(it) }
  }

  /**
   * Verifies a plain text PIN against a stored SHA-256 hash.
   */
  fun verifyPin(inputPin: String, storedHash: String): Boolean {
    if (inputPin.isBlank() || storedHash.isBlank()) return false
    val inputHash = hashPin(inputPin.trim())
    return inputHash.equals(storedHash.trim(), ignoreCase = true)
  }

  // Precomputed SHA-256 hash for default initial PIN "7788" (salted)
  val DEFAULT_PIN_HASH: String by lazy { hashPin("7788") }
}
