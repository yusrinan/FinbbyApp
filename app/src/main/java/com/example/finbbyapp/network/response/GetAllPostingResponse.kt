package com.example.finbbyapp.network.response

import com.google.gson.annotations.SerializedName

data class GetAllPostingResponse(

	@field:SerializedName("data")
	val data: List<DataItem>,

	@field:SerializedName("message")
	val message: String,

	@field:SerializedName("status")
	val status: String
)

data class DataItem(

	@field:SerializedName("attachment")
	val attachment: String,

	@field:SerializedName("sender")
	val sender: String,

	@field:SerializedName("id_post")
	val idPost: Int,

	@field:SerializedName("description")
	val description: String,

	@field:SerializedName("topic")
	val topic: String,

	@field:SerializedName("title")
	val title: String,

	@field:SerializedName("content")
	val content: String
)
