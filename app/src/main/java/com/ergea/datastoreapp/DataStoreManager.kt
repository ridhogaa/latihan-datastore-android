package com.ergea.datastoreapp

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class DataStoreManager(private val context: Context) {

    suspend fun setUsername(username: String) {
        context.dataStore.edit {
            it[USERNAME_KEY] = username
        }
    }

    suspend fun setPassword(password: String) {
        context.dataStore.edit {
            it[PASSWORD_KEY] = password
        }
    }

    suspend fun setIsLogin(isLogin: Boolean) {
        context.dataStore.edit {
            it[IS_LOGIN_KEY] = isLogin
        }
    }

    suspend fun isClear(){
        context.dataStore.edit {
            it.clear()
        }
    }

    fun getUsername(): Flow<String> {
        return context.dataStore.data.map {
            it[USERNAME_KEY] ?: ""
        }
    }

    fun getPassword(): Flow<String> {
        return context.dataStore.data.map {
            it[PASSWORD_KEY] ?: ""
        }
    }

    fun getLogin(): Flow<Boolean> {
        return context.dataStore.data.map {
            it[IS_LOGIN_KEY] ?: false
        }
    }

    companion object {
        private const val DATASTORE_NAME = "user_preferences"
        private val USERNAME_KEY = stringPreferencesKey("username_key")
        private val PASSWORD_KEY = stringPreferencesKey("password_key")
        private val IS_LOGIN_KEY = booleanPreferencesKey("is_login_key")
        private val Context.dataStore by preferencesDataStore(
            name = DATASTORE_NAME
        )
    }

}