package com.fcfm.unigrub

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView

import com.fcfm.unigrub.placeholder.PlaceholderContent.PlaceholderItem
import com.fcfm.unigrub.databinding.FragmentChatmessageMeBinding

/**
 * [RecyclerView.Adapter] that can display a [PlaceholderItem].
 * TODO: Replace the implementation with code for your data type.
 */
class ChatMessageRecyclerViewAdapter(
    private val values: List<PlaceholderItem>,
) : RecyclerView.Adapter<ChatMessageRecyclerViewAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        return ViewHolder(
            FragmentChatmessageMeBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = values[position]
        holder.idView.text = item.id
        holder.contentView.text = item.content
    }

    override fun getItemCount(): Int = values.size

    inner class ViewHolder(binding: FragmentChatmessageMeBinding) :
        RecyclerView.ViewHolder(binding.root) {
        val idView: TextView = binding.textGchatMessageMe
        val contentView: TextView = binding.textGchatMessageMe

        override fun toString(): String {
            return super.toString() + " '" + contentView.text + "'"
        }
    }

}