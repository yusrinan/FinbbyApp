package com.example.finbbyapp.ui

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class DetailContent(val title: String,
                      val deskripsi: String,
                         val name: String,
                      val photo: String): Parcelable
