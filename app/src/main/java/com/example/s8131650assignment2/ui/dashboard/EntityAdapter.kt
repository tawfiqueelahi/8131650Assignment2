
package com.example.s8131650assignment2.ui.dashboard

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.s8131650assignment2.R
import com.google.gson.JsonElement

/**
 * Binds API entities to RecyclerView items.
 * Descriptions are excluded from dashboard summaries.
 */
class EntityAdapter(
    private val onEntityClick: (Map<String, JsonElement>) -> Unit
) : ListAdapter<
        Map<String, JsonElement>,
        EntityAdapter.EntityViewHolder
        >(EntityDiff) {

    class EntityViewHolder(view: View) :
        RecyclerView.ViewHolder(view) {

        val title: TextView =
            view.findViewById(R.id.tvEntityTitle)

        val summary: TextView =
            view.findViewById(R.id.tvEntitySummary)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): EntityViewHolder {

        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_entity, parent, false)

        return EntityViewHolder(view)
    }

    override fun onBindViewHolder(
        holder: EntityViewHolder,
        position: Int
    ) {
        val entity = getItem(position)

        // Display all available fields except description.
        val summaryFields = entity.filterKeys {
            !it.equals("description", ignoreCase = true)
        }

        // Use a meaningful name when the API provides one.
        val titleField = summaryFields.entries.firstOrNull {
            it.key.equals("name", true) ||
                    it.key.equals("title", true)
        }

        holder.title.text = titleField?.value
            ?.let { formatValue(it) }
            ?.take(80)
            ?: "Entity ${position + 1}"

        holder.summary.text = summaryFields.entries
            .joinToString("\n") { (key, value) ->
                "${formatLabel(key)}: ${formatValue(value)}"
            }
            .ifBlank { "Tap to view details" }

        holder.itemView.setOnClickListener {
            onEntityClick(entity)
        }
    }

    private fun formatLabel(key: String): String =
        key.replace("_", " ")
            .replaceFirstChar { it.uppercase() }

    private fun formatValue(value: JsonElement): String =
        when {
            value.isJsonNull -> "Not available"
            value.isJsonPrimitive -> value.asString
            else -> value.toString()
        }

    companion object {
        private val EntityDiff =
            object : DiffUtil.ItemCallback<Map<String, JsonElement>>() {

                override fun areItemsTheSame(
                    oldItem: Map<String, JsonElement>,
                    newItem: Map<String, JsonElement>
                ): Boolean = oldItem === newItem

                override fun areContentsTheSame(
                    oldItem: Map<String, JsonElement>,
                    newItem: Map<String, JsonElement>
                ): Boolean = oldItem == newItem
            }
    }
}
