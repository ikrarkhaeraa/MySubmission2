package com.example.mysubmission1.main

import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.mysubmission1.databinding.ItemRowStoryBinding
import com.example.mysubmission1.main.activity.DetailStoryActivity
//import com.example.mysubmission1.main.activity.MapsActivity
import com.example.mysubmission1.main.response.ListStoryItem

class ListStoryAdapter : PagingDataAdapter<ListStoryItem, ListStoryAdapter.ListViewHolder>(DIFF_CALLBACK) {

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<ListStoryItem>() {
            override fun areItemsTheSame(
                oldItem: ListStoryItem,
                newItem: ListStoryItem
            ): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(
                oldItem: ListStoryItem,
                newItem: ListStoryItem
            ): Boolean {
                return oldItem.name == newItem.name
            }
        }
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): ListViewHolder {
        val binding = ItemRowStoryBinding.inflate(LayoutInflater.from(viewGroup.context), viewGroup, false)
        return ListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val userData = getItem(position)
        if (userData != null) {
            holder.bind(userData)
        }
    }


    class ListViewHolder(var binding: ItemRowStoryBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(userData:ListStoryItem){
            binding.apply{
                Glide.with(itemView)
                    .load(userData.photoUrl)
                    .into(binding.ivImage)
                binding.tvNamauser.text = userData.name
            }

            itemView.setOnClickListener{
                val intentToDetail = Intent(itemView.context, DetailStoryActivity::class.java)
                intentToDetail.putExtra("DATA", userData)
                itemView.context.startActivity(intentToDetail)
            }
        }
    }
}