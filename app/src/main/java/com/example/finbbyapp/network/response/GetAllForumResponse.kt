package com.example.finbbyapp.network.response

import com.google.gson.annotations.SerializedName

data class GetAllForumResponse(

	@field:SerializedName("data")
	val data: List<DataAllForumItem>,

	@field:SerializedName("message")
	val message: String,

	@field:SerializedName("status")
	val status: String
)

data class DataAllForumItem(

	@field:SerializedName("forumdesc")
	val forumdesc: String,

	@field:SerializedName("id_forum")
	val idForum: Int,

	@field:SerializedName("members")
	val members: List<String>,

	@field:SerializedName("topic")
	val topic: String,

	@field:SerializedName("admin")
	val admin: String,

	@field:SerializedName("share")
	val share: String,

	@field:SerializedName("forumname")
	val forumname: String
)
