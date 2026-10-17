
package com.example.s8131650assignment2.data.model

import com.google.gson.JsonElement

/**
 * Represents the student credentials sent to the login API.
 */
data class LoginRequest(
    val username: String,
    val password: String
)

/**
 * Stores the keypass returned after successful authentication.
 */
data class LoginResponse(
    val keypass: String
)

/**
 * Represents dashboard data received from the university API.
 * A flexible property map supports different entity types.
 */
data class DashboardResponse(
    val entities: List<Map<String, JsonElement>>,
    val entityTotal: Int
)
