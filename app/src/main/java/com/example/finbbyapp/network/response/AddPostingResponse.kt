package com.example.finbbyapp.network.response

import com.google.gson.annotations.SerializedName

data class AddPostingResponse(

	@field:SerializedName("message")
	val message: String
)
