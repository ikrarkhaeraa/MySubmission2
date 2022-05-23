package com.example.mysubmission1.main.response

import com.google.gson.annotations.SerializedName

data class AddNewStoryGuestResponse(

	@field:SerializedName("error")
	val error: Boolean,

	@field:SerializedName("message")
	val message: String
)
