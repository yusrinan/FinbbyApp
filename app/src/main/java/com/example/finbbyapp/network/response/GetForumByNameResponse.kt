package com.example.finbbyapp.network.response

import com.google.gson.annotations.SerializedName

data class GetForumByNameResponse(

	@field:SerializedName("data")
	val data: Data,

	@field:SerializedName("message")
	val message: String,

	@field:SerializedName("status")
	val status: String
)

data class Data(

	@field:SerializedName("forumdesc")
	val forumdesc: String,

	@field:SerializedName("id_forum")
	val idForum: Int,

	@field:SerializedName("topic")
	val topic: String,

	@field:SerializedName("admin")
	val admin: String,

	@field:SerializedName("share")
	val share: String,

	@field:SerializedName("forumname")
	val forumname: String
)
