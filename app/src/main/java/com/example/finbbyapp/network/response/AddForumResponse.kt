package com.example.finbbyapp.network.response

import com.google.gson.annotations.SerializedName

data class AddForumResponse(

	@field:SerializedName("data")
	val data: DataAddForum,

	@field:SerializedName("message")
	val message: String,

	@field:SerializedName("status")
	val status: String
)

data class DataAddForum(

	@field:SerializedName("forumdesc")
	val forumdesc: String,

	@field:SerializedName("topic")
	val topic: String,

	@field:SerializedName("admin")
	val admin: String,

	@field:SerializedName("share")
	val share: String,

	@field:SerializedName("forumname")
	val forumname: String
)
