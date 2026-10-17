
package com.example.s8131650assignment2.ui.details

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.s8131650assignment2.R
import com.google.gson.JsonElement
import com.google.gson.JsonParser

/**
 * Displays all information about the entity selected
 * from the Dashboard, including its description.
 */
class DetailsActivity : AppCompatActivity() {

    companion object {
        const val EXTRA_ENTITY_JSON = "extra_entity_json"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_details)

        val titleText = findViewById<TextView>(
            R.id.tvDetailsTitle
        )

        val propertiesText = findViewById<TextView>(
            R.id.tvEntityProperties
        )

        val descriptionText = findViewById<TextView>(
            R.id.tvEntityDescription
        )

        findViewById<Button>(R.id.btnBack).setOnClickListener {
            finish()
        }

        // Read the complete entity passed from the Dashboard.
        val entityJson = intent.getStringExtra(EXTRA_ENTITY_JSON)

        val entity = try {
            JsonParser.parseString(entityJson).asJsonObject
        } catch (e: Exception) {
            titleText.text = "Entity unavailable"
            propertiesText.text = "Unable to display entity details."
            descriptionText.text = "Please return to the Dashboard."
            return
        }

        val fields = entity.entrySet().toList()

        // Support API entities with either a name or title.
        val nameField = fields.firstOrNull {
            it.key.equals("name", true) ||
                    it.key.equals("title", true)
        }

        titleText.text = nameField?.value?.let {
            formatValue(it)
        } ?: "Entity Details"

        // Show all other properties, excluding the description.
        propertiesText.text = fields
            .filterNot {
                it.key.equals("description", true)
            }
            .joinToString("\n\n") { (key, value) ->
                "${formatLabel(key)}\n${formatValue(value)}"
            }
            .ifBlank { "No additional properties available." }

        // Display the full description separately.
        val description = fields.firstOrNull {
            it.key.equals("description", true)
        }?.value

        descriptionText.text = description?.let {
            formatValue(it)
        } ?: "No description available."
    }

    /**
     * Makes API field names easier to read.
     * Example: romanEquivalent -> Roman Equivalent.
     */
    private fun formatLabel(key: String): String =
        key.replace(Regex("([a-z])([A-Z])"), "$1 $2")
            .replace("_", " ")
            .replaceFirstChar { it.uppercase() }

    private fun formatValue(value: JsonElement): String =
        when {
            value.isJsonNull -> "Not available"
            value.isJsonPrimitive -> value.asString
            else -> value.toString()
        }
}
