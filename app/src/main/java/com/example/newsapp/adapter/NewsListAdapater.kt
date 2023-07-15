package com.example.newsapp.adapter

import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bumptech.glide.Glide
import com.example.newsapp.R

data class NewsData(
    val title: String,
    val author: String,
    val url: String,
    val imageUrl: String
)

class NewsListAdapater(private val listener: NewsItemClicked) : RecyclerView.Adapter<NewsViewHolder>() {
    var listOfNew: ArrayList<NewsData> = ArrayList()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.row_news_store, parent, false)
        val viewHolder = NewsViewHolder(view)
        view.setOnClickListener {
            listener.onItemClicked(listOfNew[viewHolder.adapterPosition])
        }
        return viewHolder
    }

    override fun getItemCount(): Int = listOfNew.size

    override fun onBindViewHolder(holder: NewsViewHolder, position: Int) {
        holder.description.text = listOfNew[position].title
        Glide.with(holder.itemView.context).load(listOfNew[position].imageUrl).placeholder(R.drawable.img_place_holder).into(holder.imageView)
        holder.author.text = listOfNew[position].author
    }

    fun updateNews(updateNews: ArrayList<NewsData>) {
        listOfNew.clear()
        listOfNew.addAll(updateNews)
        notifyDataSetChanged()
    }
}

class NewsViewHolder(itemView: View) : ViewHolder(itemView) {
    val description: TextView = itemView.findViewById(R.id.tv_description)
    val imageView : ImageView = itemView.findViewById(R.id.img_banner)
    val author : TextView = itemView.findViewById(R.id.tv_author)
}

interface NewsItemClicked {
    fun onItemClicked(item: NewsData)
}