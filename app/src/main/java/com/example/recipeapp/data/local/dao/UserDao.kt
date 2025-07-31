package com.example.recipeapp.data.local.dao




import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.recipeapp.data.local.entities.FavoriteRecipeEntity
import com.example.recipeapp.data.local.entities.UserEntity

@Dao
interface UserDAO {

    @Insert
    suspend fun insertUser(user: UserEntity): Long

    @Query("SELECT * FROM users WHERE username = :username AND password = :password")
    suspend fun loginUser(username: String, password: String): UserEntity?

    @Query("SELECT * FROM users WHERE email = :email")
    suspend fun getUserByEmail(email: String): UserEntity?

    @Query("SELECT EXISTS(SELECT 1 FROM users WHERE email = :email)")
    suspend fun isEmailExists(email: String): Boolean
}
