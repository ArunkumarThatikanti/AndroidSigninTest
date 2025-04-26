package com.example.taskbookxpert.repository

import com.example.taskbookxpert.data.local.AppDatabase
import com.example.taskbookxpert.data.local.UserEntity

class UserRepository(private val db: AppDatabase) {

    suspend fun insertUser(user: UserEntity) = db.userDao().insertUser(user)

    suspend fun getUser(): UserEntity? =db.userDao().getUser()

    suspend fun deleteUser() = db.userDao().deleteUser()
}