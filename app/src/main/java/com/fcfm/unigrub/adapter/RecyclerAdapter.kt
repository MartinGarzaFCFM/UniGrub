package com.fcfm.unigrub.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.fcfm.unigrub.R
import com.fcfm.unigrub.data.Item
import com.squareup.picasso.Picasso

class RecyclerAdapter : RecyclerView.Adapter<RecyclerAdapter.ViewHolder>() {

    var items: MutableList<Item>  = ArrayList()
    lateinit var context:Context

    @SuppressLint("NotConstructor")
    fun RecyclerAdapter(items : MutableList<Item>, context: Context){
        this.items = items
        this.context = context
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        holder.bind(item, context)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return ViewHolder(layoutInflater.inflate(R.layout.item_list, parent, false))
    }

    override fun getItemCount(): Int {
        return items.size
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val superheroName = view.findViewById(R.id.itemName) as TextView
        val realName = view.findViewById(R.id.itemDesc) as TextView
        val publisher = view.findViewById(R.id.itemSeller) as TextView
        val avatar = view.findViewById(R.id.itemPic) as ImageView

        fun bind(item:Item, context: Context){
            superheroName.text = item.Name
            realName.text = item.Description
            publisher.text = item.Seller
            avatar.loadUrl(item.Image)
            itemView.setOnClickListener(View.OnClickListener { Toast.makeText(context, item.Name, Toast.LENGTH_SHORT).show() })
        }
        fun ImageView.loadUrl(url: String) {
            Picasso.with(context).load(url).into(this)
        }
    }
}