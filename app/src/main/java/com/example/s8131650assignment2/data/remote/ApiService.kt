
package com.example.s8131650assignment2.data.remote

import com.example.s8131650assignment2.data.model.LoginRequest
import com.example.s8131650assignment2.data.model.LoginResponse
import com.example.s8131650assignment2.data.model.DashboardResponse

import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

/**
 * Defines API requests used for authentication
 * and retrieving dashboard entities.
 */
interface ApiService {

    // Sydney students authenticate using this endpoint.
    @POST("sydney/auth")
    suspend fun login(
        @Body request: LoginRequest
    ): LoginResponse

    // Uses the keypass received from successful login.
    @GET("dashboard/{keypass}")
    suspend fun getDashboard(
        @Path("keypass") keypass: String
    ): DashboardResponse
}
