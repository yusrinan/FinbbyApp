package com.example.finbbyapp.network.response

import com.google.gson.annotations.SerializedName

data class LoginResponse(

	@field:SerializedName("data")
	val data: DataLogin,

	@field:SerializedName("status")
	val status: String,

	@field:SerializedName("token")
	val token: String,
)

data class DataLogin(

	@field:SerializedName("password")
	val password: String,

	@field:SerializedName("created_at")
	val createdAt: String,

	@field:SerializedName("id_user")
	val idUser: Int,

	@field:SerializedName("email")
	val email: String,

	@field:SerializedName("username")
	val username: String
)
