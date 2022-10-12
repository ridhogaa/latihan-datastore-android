package com.ergea.datastoreapp

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class UserViewModel(private val pref: DataStoreManager): ViewModel() {
    fun saveUser(username: String, password: String){
        viewModelScope.launch {
            pref.setUsername(username)
            pref.setPassword(password)
        }
    }

    fun setIsLogin(isLogin:Boolean){
        viewModelScope.launch {
            pref.setIsLogin(isLogin)
        }
    }

    fun isClear(){
        viewModelScope.launch {
            pref.isClear()
        }
    }

    fun getDataStoreUsername(): LiveData<String> {
        return pref.getUsername().asLiveData()
    }

    fun getDataStorePassword(): LiveData<String>{
        return pref.getPassword().asLiveData()
    }

    fun getDataStoreIsLogin(): LiveData<Boolean> {
        return pref.getLogin().asLiveData()
    }
}