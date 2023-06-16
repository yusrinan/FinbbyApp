package com.example.finbbyapp.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.view.View
import android.widget.Toast
import com.example.finbbyapp.R
import com.example.finbbyapp.databinding.ActivityRegisterBinding
import com.example.finbbyapp.network.ApiConfig
import com.example.finbbyapp.network.RegisterRequest
import com.example.finbbyapp.network.response.RegisterResponse
import com.google.gson.Gson
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegisterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding
    private lateinit var registerRequest: RegisterRequest

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()


        binding.btnRegister.setOnClickListener {
            register()
        }
    }

    private fun register() {
        val name = binding.edtName.text.toString().trim()
        val email = binding.edtEmail.text.toString().trim()
        val password = binding.edtPassword.text.toString().trim()
        val emailIsValid = !Patterns.EMAIL_ADDRESS.matcher(email).matches()

        when {
            name.isEmpty() -> binding.edtName.error = "Name can't be blank"
            email.isEmpty() -> binding.edtEmail.error = "Email can't be blank"
            password.isEmpty() -> binding.edtPassword.error = "Password must not be blank"
            emailIsValid -> binding.edtEmail.error = "Incorrect email format"
            else -> {
                binding.progressBar.visibility = View.VISIBLE
                registerRequest = RegisterRequest(name, email, password)
                val n = binding.edtName.text.toString().toRequestBody("text/plain".toMediaType())
                val e = binding.edtEmail.text.toString().toRequestBody("text/plain".toMediaType())
                val p = binding.edtPassword.text.toString().toRequestBody("text/plain".toMediaType())

                val client = ApiConfig.getApiService().register(registerRequest)
                client.enqueue(object : Callback<RegisterResponse> {
                    override fun onResponse(
                        call: Call<RegisterResponse>,
                        response: Response<RegisterResponse>
                    ) {
                        if (response.isSuccessful) {
                            binding.progressBar.visibility = View.GONE
                            startActivity(Intent(this@RegisterActivity, LoginActivity::class.java))
                        } else {
                            val responseBody = Gson().fromJson(response.errorBody()?.charStream(), RegisterResponse::class.java)
                            binding.progressBar.visibility = View.GONE
                            Toast.makeText(this@RegisterActivity, response.message().toString(), Toast.LENGTH_LONG).show()
                        }
                    }
                    override fun onFailure(call: Call<RegisterResponse>, t: Throwable) {
                        binding.progressBar.visibility = View.GONE
                        Toast.makeText(this@RegisterActivity, t.message.toString(), Toast.LENGTH_LONG).show()
                    }
                })
            }
        }
    }
}