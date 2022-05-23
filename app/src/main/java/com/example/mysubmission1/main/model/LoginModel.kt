package com.example.mysubmission1.main.model

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mysubmission1.main.response.LoginResponse
import kotlinx.coroutines.launch

class LoginModel(private val dataSource: DataSource) : ViewModel() {
    val login: LiveData<LoginResponse> = dataSource.login

    fun uploadLoginData(email: String, password: String) {
        viewModelScope.launch {
            dataSource.uploadLoginData(email, password)
        }
    }

    fun saveUserSession(session: UserSession) {
        viewModelScope.launch {
            dataSource.saveSession(session)
        }
    }

    fun userLogin() {
        viewModelScope.launch {
            dataSource.userLogin()
        }
    }
}