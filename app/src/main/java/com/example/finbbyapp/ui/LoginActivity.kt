package com.example.finbbyapp.ui

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.telecom.Call
import android.util.Patterns
import android.view.View
import android.widget.Toast
import com.example.finbbyapp.MainActivity
import com.example.finbbyapp.R
import com.example.finbbyapp.databinding.ActivityLoginBinding
import com.example.finbbyapp.network.ApiConfig
import com.example.finbbyapp.network.response.LoginResponse
import com.example.finbbyapp.network.response.RegisterResponse
import com.example.finbbyapp.ui.preferences.SurveyPreference
import com.example.finbbyapp.ui.preferences.UserModel
import com.example.finbbyapp.ui.preferences.UserPreference
import com.google.gson.Gson
import retrofit2.Callback
import retrofit2.Response

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private lateinit var userModel: UserModel
    private lateinit var userPreference: UserPreference
    private lateinit var surveyPreference: SurveyPreference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        userPreference = UserPreference(this@LoginActivity)

        userModel = UserModel()

        binding.toSignup.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }

        binding.btnLogin.setOnClickListener {
            login()
        }
    }

    private fun login() {
        val email = binding.edtEmail.text.toString().trim()
        val password = binding.edtPassword.text.toString().trim()
        val emailIsValid = !Patterns.EMAIL_ADDRESS.matcher(email).matches()

        when {
            email.isEmpty() -> binding.edtEmail.error = "Email can't be blank"
            password.isEmpty() -> binding.edtPassword.error = "Password must not be blank"
            emailIsValid -> binding.edtEmail.error = "Incorrect email format"
            else -> {
                binding.progressBar.visibility = View.VISIBLE

                val client = ApiConfig.getApiService().login(email, password)
                client.enqueue(object : Callback<LoginResponse> {
                    @SuppressLint("SuspiciousIndentation")
                    override fun onResponse(
                        call: retrofit2.Call<LoginResponse>,
                        response: Response<LoginResponse>
                    ) {
                        if (response.isSuccessful) {
                            binding.progressBar.visibility = View.GONE

                            userModel.name = response.body()?.data?.username
                            userModel.email = response.body()?.data?.email
                            userModel.id = response.body()?.data?.idUser

                            userPreference.setUser(userModel)
                            surveyPreference = SurveyPreference(this@LoginActivity, userPreference.getUser().name as String)

                            if(surveyPreference.getSurvey().isSurvey) {
                                startActivity(Intent(this@LoginActivity, MainActivity::class.java))
                                finish()
                            }
                            else {
                                startActivity(Intent(this@LoginActivity, SurveyActivity::class.java))
                                finish()
                            }

                        } else {
                            val responseBody = Gson().fromJson(response.errorBody()?.charStream(), RegisterResponse::class.java)
                            binding.progressBar.visibility = View.GONE
                            Toast.makeText(this@LoginActivity, responseBody.message.toString(), Toast.LENGTH_LONG).show()
                        }
                    }
                    override fun onFailure(call: retrofit2.Call<LoginResponse>, t: Throwable) {
                        binding.progressBar.visibility = View.GONE
                        Toast.makeText(this@LoginActivity, t.message.toString(), Toast.LENGTH_LONG).show()
                    }
                })
            }
        }
    }
}