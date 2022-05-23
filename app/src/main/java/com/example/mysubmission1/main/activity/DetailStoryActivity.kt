package com.example.mysubmission1.main.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.bumptech.glide.Glide
import com.example.mysubmission1.databinding.ActivityDetailStoryBinding
import com.example.mysubmission1.main.response.ListStoryItem

class DetailStoryActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailStoryBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailStoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val data = intent.getParcelableExtra<ListStoryItem>("DATA")
        val photo = data?.photoUrl
        val name = data?.name
        val description = data?.description

        Log.e("name", "onResponse: $name")

        Glide.with(this@DetailStoryActivity)
            .load(photo)
            .into(binding.rvImage)
        binding.tvName.text = name
        binding.tvDescription.text = description
    }

}