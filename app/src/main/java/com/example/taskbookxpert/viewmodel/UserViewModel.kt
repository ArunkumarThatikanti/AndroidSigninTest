package com.example.taskbookxpert.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.taskbookxpert.data.local.AppDatabase
import com.example.taskbookxpert.data.local.UserEntity
import com.example.taskbookxpert.repository.UserRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.Dispatcher

class UserViewModel(application: Application) : AndroidViewModel(application) {
    private val repository : UserRepository

    init {
        val db = AppDatabase.getDatabase(application)
        repository = UserRepository(db)
    }

    fun insertUser(user: UserEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.insertUser(user)
        }
    }

    fun deleteUSer(){
        viewModelScope.launch(Dispatchers.IO){
            repository.deleteUser()
        }

    }

    suspend fun getUserBlocking(): UserEntity? {
        return repository.getUser()
    }
}