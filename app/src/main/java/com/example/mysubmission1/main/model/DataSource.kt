package com.example.mysubmission1.main.model

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asLiveData
import com.example.mysubmission1.main.API.ApiConfig
import com.example.mysubmission1.main.UserPreferences
import com.example.mysubmission1.main.response.AddNewStoryResponse
import com.example.mysubmission1.main.response.GetAllStoryResponse
import com.example.mysubmission1.main.response.LoginResponse
import com.example.mysubmission1.main.response.RegisterResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DataSource private constructor(
    private val pref: UserPreferences,
) {

    companion object {
        @Volatile
        private var instance: DataSource? = null
        fun getInstance(
            preferences: UserPreferences,
        ): DataSource =
            instance ?: synchronized(this) {
                instance ?: DataSource(preferences)
            }.also { instance = it }
    }

    private val _regis = MutableLiveData<RegisterResponse>()
    val regis: LiveData<RegisterResponse> = _regis

    private val _login = MutableLiveData<LoginResponse>()
    val login: LiveData<LoginResponse> = _login

    private val _add = MutableLiveData<AddNewStoryResponse>()
    val add: LiveData<AddNewStoryResponse> = _add

    private val _listStory = MutableLiveData<GetAllStoryResponse>()
    val listStory: LiveData<GetAllStoryResponse> = _listStory

    private val _listStoryMaps = MutableLiveData<GetAllStoryResponse>()
    val listStoryMaps: LiveData<GetAllStoryResponse> = _listStoryMaps

    fun uploadRegisData(name: String, email:String, password:String) {
        val client = ApiConfig.getApiService().uploadDataRegis(name, email, password)
        client.enqueue(object : Callback<RegisterResponse> {
            override fun onResponse(
                call: Call<RegisterResponse>,
                response: Response<RegisterResponse>
            ) {
                val responseBody = response.body()
                if (response.isSuccessful) {
                    Log.e("regisResponse", "onResponse: ${response.message()}")
                    _regis.value = response.body()
                } else {
                    Log.e("regis", "onResponse: ${response.message()} / akun sudah ada")

                }
            }
            override fun onFailure(call: Call<RegisterResponse>, t: Throwable) {
                Log.e("regisFailure", "onFailure: ${t.message}")
            }
        })
    }

    fun uploadLoginData(email:String, password:String) {
        val client = ApiConfig.getApiService().uploadDataLogin(email, password)
        client.enqueue(object : Callback<LoginResponse> {
            override fun onResponse(
                call: Call<LoginResponse>,
                response: Response<LoginResponse>
            ) {
                val responseBody = response.body()
                if (response.isSuccessful) {
                    Log.e("loginResponse", "onResponse: ${response.message()}")
                    _login.value = response.body()
                } else {
                    Log.e("login", "onFailure: ${response.message()}")
                }
            }
            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                Log.e("loginFailure", "onFailure: ${t.message}")
            }
        })
    }

    fun getStory(token: String) {
        val client = ApiConfig.getApiService().getStory(token, size = 15, location = 1)
        client.enqueue(object : Callback<GetAllStoryResponse> {
            override fun onResponse(
                call: Call<GetAllStoryResponse>,
                response: Response<GetAllStoryResponse>
            ) {
                val responseBody = response.body()
                if (response.isSuccessful) {
                    Log.e("storyResponse", "onResponse: ${response.message()}")
                    _listStory.value = response.body()
                } else {
                    Log.e("story", "onFailure: ${response.message()}")
                }
            }
            override fun onFailure(call: Call<GetAllStoryResponse>, t: Throwable) {
                Log.e("storyFailure", "onFailure: ${t.message}")
            }
        })
    }

//    fun getStoryMaps(token: String) {
//        val client = ApiConfig.getApiService().getStoryMaps(token, 1)
//        client.enqueue(object : Callback<GetAllStoryResponse> {
//            override fun onResponse(
//                call: Call<GetAllStoryResponse>,
//                response: Response<GetAllStoryResponse>
//            ) {
//                val responseBody = response.body()
//                if (response.isSuccessful) {
//                    Log.e("storyResponse", "onResponse: ${response.message()}")
//                    _listStoryMaps.value = response.body()
//                } else {
//                    Log.e("story", "onFailure: ${response.message()}")
//                }
//            }
//            override fun onFailure(call: Call<GetAllStoryResponse>, t: Throwable) {
//                Log.e("storyFailure", "onFailure: ${t.message}")
//            }
//        })
//    }

    fun uploadStory (token: String, file: MultipartBody.Part, description: RequestBody, lat: RequestBody? = null, lon: RequestBody? = null) {
        val client = ApiConfig.getApiService().uploadStory(token, file, description, lat, lon)
        client.enqueue(object : Callback<AddNewStoryResponse> {
            override fun onResponse(
                call: Call<AddNewStoryResponse>,
                response: Response<AddNewStoryResponse>
            ) {
                val responseBody = response.body()
                if (response.isSuccessful) {
                    Log.e("addResponse", "onResponse: ${response.message()}")
                    _add.value = response.body()
                } else {
                    Log.e("add", "onFailure: ${response.message()}")
                }
            }
            override fun onFailure(call: Call<AddNewStoryResponse>, t: Throwable) {
                Log.e("addFailure", "onFailure: ${t.message}")
            }
        })
    }

    fun getUserSession(): LiveData<UserSession> {
        return pref.getUserSession().asLiveData()
    }

    suspend fun saveSession(session: UserSession) {
        pref.saveUserSession(session)
    }

    suspend fun userLogin() {
        pref.login()
    }

    suspend fun userLogout() {
        pref.logout()
    }

}