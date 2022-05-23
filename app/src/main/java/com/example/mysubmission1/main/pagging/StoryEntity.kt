package com.example.mysubmission1.main.pagging

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.mysubmission1.main.response.ListStoryItem
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Entity(tableName = "listStory")
data class StoryEntity(

    @field:SerializedName("photoUrl")
    val photoUrl: String,

    @field:SerializedName("createdAt")
    val createdAt: String,

    @PrimaryKey
    @field:SerializedName("name")
    val name: String,

    @field:SerializedName("description")
    val description: String,

    @field:SerializedName("lon")
    val lon: Float,

    @field:SerializedName("id")
    val id: String,

    @field:SerializedName("lat")
    val lat: Float
)