
package com.example.s8131650assignment2

import com.example.s8131650assignment2.data.model.DashboardResponse
import com.example.s8131650assignment2.data.repository.AppRepository
import com.example.s8131650assignment2.ui.login.LoginViewModel
import com.example.s8131650assignment2.ui.login.LoginUiState
import com.example.s8131650assignment2.ui.dashboard.DashboardViewModel
import com.example.s8131650assignment2.ui.dashboard.DashboardUiState

import com.google.gson.JsonPrimitive
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestWatcher
import org.junit.runner.Description
import java.io.IOException

/**
 * Replaces the Android Main dispatcher with a test dispatcher.
 * This lets ViewModels be tested without an emulator.
 */
@OptIn(ExperimentalCoroutinesApi::class)
class MainDispatcherRule : TestWatcher() {

    private val dispatcher = StandardTestDispatcher()

    override fun starting(description: Description) {
        Dispatchers.setMain(dispatcher)
    }

    override fun finished(description: Description) {
        Dispatchers.resetMain()
    }
}

/**
 * Simulates university API responses during unit testing.
 * No real network requests are made.
 */
private class FakeRepository : AppRepository {

    var loginCalls = 0
    var dashboardCalls = 0
    var receivedKeypass = ""

    var failLogin = false
    var failDashboard = false

    var dashboardData = DashboardResponse(
        entities = emptyList(),
        entityTotal = 0
    )

    override suspend fun login(
        username: String,
        password: String
    ): String {
        loginCalls++

        if (failLogin) {
            throw IOException("Simulated connection failure")
        }

        return "testTopic"
    }

    override suspend fun getDashboard(
        keypass: String
    ): DashboardResponse {
        dashboardCalls++
        receivedKeypass = keypass

        if (failDashboard) {
            throw IOException("Simulated server failure")
        }

        return dashboardData
    }
}

/**
 * Unit tests for student authentication.
 */
@OptIn(ExperimentalCoroutinesApi::class)
class LoginViewModelTest {

    @get:Rule
    val dispatcherRule = MainDispatcherRule()

    @Test
    fun emptyStudentIdShowsError() = runTest {
        val repository = FakeRepository()
        val viewModel = LoginViewModel(repository)

        viewModel.login("", "Alex")

        assertTrue(
            viewModel.uiState.value is LoginUiState.Error
        )
        assertEquals(0, repository.loginCalls)
    }

    @Test
    fun emptyPasswordShowsError() = runTest {
        val repository = FakeRepository()
        val viewModel = LoginViewModel(repository)

        viewModel.login("12345678", "")

        assertTrue(
            viewModel.uiState.value is LoginUiState.Error
        )
        assertEquals(0, repository.loginCalls)
    }

    @Test
    fun successfulLoginReturnsKeypass() = runTest {
        val repository = FakeRepository()
        val viewModel = LoginViewModel(repository)

        viewModel.login("12345678", "Alex")
        advanceUntilIdle()

        val state = viewModel.uiState.value
                as LoginUiState.Success

        assertEquals("testTopic", state.keypass)
        assertEquals(1, repository.loginCalls)
    }

    @Test
    fun loginNetworkFailureShowsError() = runTest {
        val repository = FakeRepository()
        repository.failLogin = true

        val viewModel = LoginViewModel(repository)

        viewModel.login("12345678", "Alex")
        advanceUntilIdle()

        assertTrue(
            viewModel.uiState.value is LoginUiState.Error
        )
    }
}

/**
 * Unit tests for dashboard data retrieval.
 */
@OptIn(ExperimentalCoroutinesApi::class)
class DashboardViewModelTest {

    @get:Rule
    val dispatcherRule = MainDispatcherRule()

    @Test
    fun dashboardLoadsEntities() = runTest {
        val repository = FakeRepository()

        repository.dashboardData = DashboardResponse(
            entities = listOf(
                mapOf(
                    "name" to JsonPrimitive("Test Entity"),
                    "description" to JsonPrimitive("Test description")
                )
            ),
            entityTotal = 1
        )

        val viewModel = DashboardViewModel(repository)

        viewModel.loadDashboard("testTopic")
        advanceUntilIdle()

        val state = viewModel.uiState.value
                as DashboardUiState.Success

        assertEquals(1, state.total)
        assertEquals(1, state.entities.size)
        assertEquals("testTopic", repository.receivedKeypass)
    }

    @Test
    fun missingKeypassShowsError() = runTest {
        val repository = FakeRepository()
        val viewModel = DashboardViewModel(repository)

        viewModel.loadDashboard("")

        assertTrue(
            viewModel.uiState.value is DashboardUiState.Error
        )
        assertEquals(0, repository.dashboardCalls)
    }

    @Test
    fun dashboardNetworkFailureShowsError() = runTest {
        val repository = FakeRepository()
        repository.failDashboard = true

        val viewModel = DashboardViewModel(repository)

        viewModel.loadDashboard("testTopic")
        advanceUntilIdle()

        assertTrue(
            viewModel.uiState.value is DashboardUiState.Error
        )
    }
}
