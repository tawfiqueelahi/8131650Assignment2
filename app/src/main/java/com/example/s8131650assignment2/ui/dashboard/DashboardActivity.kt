
package com.example.s8131650assignment2.ui.dashboard

import android.content.Intent
import com.example.s8131650assignment2.ui.details.DetailsActivity
import com.google.gson.Gson

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
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.s8131650assignment2.R
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

/**
 * Displays entities retrieved using the login keypass.
 */
@AndroidEntryPoint
class DashboardActivity : AppCompatActivity() {

    companion object {
        const val EXTRA_KEYPASS = "extra_keypass"
    }

    private val viewModel: DashboardViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)

        val keypass =
            intent.getStringExtra(EXTRA_KEYPASS).orEmpty()

        val entityCount =
            findViewById<TextView>(R.id.tvEntityCount)

        val progressBar =
            findViewById<ProgressBar>(R.id.dashboardProgress)

        val errorText =
            findViewById<TextView>(R.id.tvDashboardError)

        val retryButton =
            findViewById<Button>(R.id.btnRetry)

        val recyclerView =
            findViewById<RecyclerView>(R.id.recyclerViewEntities)



        val adapter = EntityAdapter { entity ->

            // Pass the selected entity to the Details screen.
            val intent = Intent(
                this@DashboardActivity,
                DetailsActivity::class.java
            )

            intent.putExtra(
                DetailsActivity.EXTRA_ENTITY_JSON,
                Gson().toJson(entity)
            )

            startActivity(intent)
        }



        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter

        retryButton.setOnClickListener {
            viewModel.loadDashboard(keypass)
        }

        // Update the screen whenever the ViewModel state changes.
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect { state ->

                    progressBar.visibility =
                        if (state is DashboardUiState.Loading)
                            View.VISIBLE else View.GONE

                    errorText.visibility = View.GONE
                    retryButton.visibility = View.GONE

                    when (state) {
                        is DashboardUiState.Loading -> {
                            entityCount.text = "Loading entities..."
                        }

                        is DashboardUiState.Success -> {
                            entityCount.text =
                                "Showing ${state.entities.size} of ${state.total} entities"

                            adapter.submitList(state.entities)

                            if (state.entities.isEmpty()) {
                                errorText.text = "No entities available."
                                errorText.visibility = View.VISIBLE
                            }
                        }

                        is DashboardUiState.Error -> {
                            entityCount.text = "Unable to load dashboard"
                            errorText.text = state.message
                            errorText.visibility = View.VISIBLE
                            retryButton.visibility = View.VISIBLE
                        }
                    }
                }
            }
        }

        // Fetch data using the keypass received from Login.
        if (savedInstanceState == null) {
            viewModel.loadDashboard(keypass)
        }
    }
}
