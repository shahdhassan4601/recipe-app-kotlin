package com.example.recipeapp.data.local.entities




import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class UserEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val username: String,
    val email: String,
    val password: String,
    val createdAt: Long = System.currentTimeMillis()
)