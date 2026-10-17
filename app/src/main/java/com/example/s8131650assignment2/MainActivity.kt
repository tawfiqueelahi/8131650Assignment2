package com.example.s8131650assignment2


import android.content.Intent
import com.example.s8131650assignment2.ui.dashboard.DashboardActivity

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.s8131650assignment2.ui.login.LoginUiState
import com.example.s8131650assignment2.ui.login.LoginViewModel
import com.google.android.material.textfield.TextInputEditText
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

/**
 * Login screen of the student application.
 * Observes the ViewModel and updates the UI based on login state.
 */
@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    // Hilt provides the ViewModel and its repository dependencies.
    private val loginViewModel: LoginViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val studentIdInput =
            findViewById<TextInputEditText>(R.id.etStudentId)

        val passwordInput =
            findViewById<TextInputEditText>(R.id.etPassword)

        val loginButton =
            findViewById<Button>(R.id.btnLogin)

        val progressBar =
            findViewById<ProgressBar>(R.id.progressBar)

        val statusText =
            findViewById<TextView>(R.id.tvStatus)

        // Send credentials to the ViewModel when Login is pressed.
        loginButton.setOnClickListener {
            val studentId = studentIdInput.text.toString()
            val password = passwordInput.text.toString()

            loginViewModel.login(studentId, password)
        }

        // Observe UI state only while the Activity is active.
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                loginViewModel.uiState.collect { state ->

                    progressBar.visibility =
                        if (state is LoginUiState.Loading)
                            View.VISIBLE else View.GONE

                    loginButton.isEnabled =
                        state !is LoginUiState.Loading

                    when (state) {

                        is LoginUiState.Idle -> {
                            statusText.visibility = View.GONE
                        }

                        is LoginUiState.Loading -> {
                            statusText.visibility = View.GONE
                        }


                        is LoginUiState.Success -> {

                            // Pass the API keypass to the Dashboard.
                            val intent = Intent(
                                this@MainActivity,
                                DashboardActivity::class.java
                            )

                            intent.putExtra(
                                DashboardActivity.EXTRA_KEYPASS,
                                state.keypass
                            )

                            startActivity(intent)
                            finish()
                        }


                        is LoginUiState.Error -> {
                            statusText.text = state.message
                            statusText.setTextColor(
                                getColor(android.R.color.holo_red_dark)
                            )
                            statusText.visibility = View.VISIBLE
                        }
                    }
                }
            }
        }
    }
}
