package com.example.taskbookxpert.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_table")
data class UserEntity (
    @PrimaryKey val uid: String,
    val name: String,
    val email: String,
    val photoUrl: String?
)