package com.example.mysubmission1.main.model

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mysubmission1.main.response.RegisterResponse
import kotlinx.coroutines.launch

class RegisterModel(private val dataSource: DataSource) : ViewModel() {
    val regis: LiveData<RegisterResponse> = dataSource.regis

    fun postDataRegister(name: String, email: String, password: String) {
        viewModelScope.launch {
            dataSource.uploadRegisData(name, email, password)
        }
    }
}