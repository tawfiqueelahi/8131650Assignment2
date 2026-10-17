
package com.example.s8131650assignment2.data.repository

import com.example.s8131650assignment2.data.model.LoginRequest
import com.example.s8131650assignment2.data.model.DashboardResponse
import com.example.s8131650assignment2.data.remote.ApiService
import javax.inject.Inject

/**
 * Defines the data operations available to ViewModels.
 * The interface allows us to use fake repositories in unit tests.
 */
interface AppRepository {

    suspend fun login(
        username: String,
        password: String
    ): String

    suspend fun getDashboard(
        keypass: String
    ): DashboardResponse
}

/**
 * Connects the repository to the university API.
 * Retrofit performs the actual network requests.
 */
class AppRepositoryImpl @Inject constructor(
    private val apiService: ApiService
) : AppRepository {

    override suspend fun login(
        username: String,
        password: String
    ): String {

        val response = apiService.login(
            LoginRequest(username, password)
        )

        return response.keypass
    }

    override suspend fun getDashboard(
        keypass: String
    ): DashboardResponse {

        return apiService.getDashboard(keypass)
    }
}
