package com.example.finbbyapp.network.response

import com.google.gson.annotations.SerializedName

data class JoinForumResponse(

	@field:SerializedName("message")
	val message: String,

	@field:SerializedName("status")
	val status: String
)
