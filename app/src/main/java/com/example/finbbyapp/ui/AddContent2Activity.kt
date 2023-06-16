package com.example.finbbyapp.ui

import android.Manifest
import android.content.Intent
import android.content.Intent.ACTION_GET_CONTENT
import android.content.pm.PackageManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.finbbyapp.MainActivity
import com.example.finbbyapp.R
import com.example.finbbyapp.databinding.ActivityAddContent2Binding
import com.example.finbbyapp.helper.reduceFileImage
import com.example.finbbyapp.helper.uriToFile
import com.example.finbbyapp.network.ApiConfig
import com.example.finbbyapp.network.response.AddPostingResponse
import com.example.finbbyapp.network.response.ErrorResponse
import com.example.finbbyapp.network.response.RegisterResponse
import com.example.finbbyapp.ui.preferences.UserPreference
import com.google.gson.Gson
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File

class AddContent2Activity : AppCompatActivity() {
    private lateinit var binding: ActivityAddContent2Binding
    private var getFile: File? = null
    private lateinit var userPreference: UserPreference

    companion object {
        private val REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.CAMERA)
        private const val REQUEST_CODE_PERMISSIONS = 10
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (!allPermissionsGranted()) {
                Toast.makeText(
                    this,
                    "Tidak mendapatkan permission.",
                    Toast.LENGTH_SHORT
                ).show()
                finish()
            }
        }
    }

    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(baseContext, it) == PackageManager.PERMISSION_GRANTED
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddContent2Binding.inflate(layoutInflater)
        setContentView(binding.root)
        if (!allPermissionsGranted()) {
            ActivityCompat.requestPermissions(
                this,
                REQUIRED_PERMISSIONS,
                REQUEST_CODE_PERMISSIONS
            )
        }
        supportActionBar?.title = "Add Content"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        userPreference = UserPreference(this)
        binding.imageView2.setOnClickListener { startGallery() }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.send_content_menu, menu)

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                return true
            }
            R.id.send -> {
                when {
                    binding.deskripsi.text.isEmpty() -> {
                        Toast.makeText(this, "Terdapat inputan yg masih kosong", Toast.LENGTH_SHORT).show()
                    }
                    else -> {
                        binding.progressBar.visibility = View.VISIBLE
                        if(getFile != null) {
                            var file: File?
                            file = reduceFileImage(getFile as File)
                            val title = userPreference.dataAddContent1().title.toString().toRequestBody("text/plain".toMediaType())
                            val content = userPreference.dataAddContent1().content.toString().toRequestBody("text/plain".toMediaType())
                            val topic = userPreference.dataAddContent1().topic.toString().toRequestBody("text/plain".toMediaType())
                            val deskripsi = binding.deskripsi.text.toString().toRequestBody("text/plain".toMediaType())
                            val requestImageFile = file.asRequestBody("image/jpeg".toMediaTypeOrNull())
                            val imageMultipart: MultipartBody.Part = MultipartBody.Part.createFormData(
                                "photo",
                                file.name,
                                requestImageFile
                            )
                            val service = ApiConfig.getApiService().addPosting(imageMultipart, deskripsi, title, topic, content)

                            service.enqueue(object : Callback<AddPostingResponse> {
                                override fun onResponse(
                                    call: Call<AddPostingResponse>,
                                    response: Response<AddPostingResponse>
                                ) {
                                    if (response.isSuccessful) {
                                        val responseBody = response.body()
                                        if (responseBody != null) {
                                            binding.progressBar.visibility = View.GONE

                                            val intent = Intent(this@AddContent2Activity, MainActivity::class.java)
                                            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                                            startActivity(intent)
                                        }
                                    } else {
                                        binding.progressBar.visibility = View.GONE
//                                        val responseBody = Gson().fromJson(response.errorBody()?.charStream(), AddPostingResponse::class.java)
//                                        Toast.makeText(this@AddContent2Activity, responseBody.message.toString(), Toast.LENGTH_LONG).show()
                                        val errorBody = response.errorBody()?.string()
                                        val contentType = response.errorBody()?.contentType()?.toString()

                                        if (contentType?.contains("text/html") == true) {
                                            // Penanganan ketika pesan error berupa HTML
                                            Toast.makeText(this@AddContent2Activity, errorBody, Toast.LENGTH_LONG).show()
                                        } else {
                                            // Penanganan ketika pesan error dalam format lain atau tidak diketahui
                                            val responseBody = Gson().fromJson(errorBody, ErrorResponse::class.java)
                                            Toast.makeText(this@AddContent2Activity, responseBody?.message ?: "Unknown error", Toast.LENGTH_LONG).show()
                                        }
                                    }
                                }
                                override fun onFailure(call: Call<AddPostingResponse>, t: Throwable) {
                                    Toast.makeText(this@AddContent2Activity, "Gagal instance Retrofit", Toast.LENGTH_LONG).show()
                                    binding.progressBar.visibility = View.GONE
                                }
                            })
                        } else {
                            Toast.makeText(this@AddContent2Activity, "Silakan masukkan berkas gambar terlebih dahulu.", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
        }

        return super.onOptionsItemSelected(item)
    }


    private fun startGallery() {
        val intent = Intent()
        intent.action = ACTION_GET_CONTENT
        intent.type = "image/*"
        val chooser = Intent.createChooser(intent, "Choose a Picture")
        launcherIntentGallery.launch(chooser)
    }

    private val launcherIntentGallery = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            val selectedImg = result.data?.data as Uri

            selectedImg.let { uri ->
                val myFile = uriToFile(uri, this@AddContent2Activity)
                getFile = myFile
                binding.imageView2.setImageURI(uri)
            }
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
    }
}