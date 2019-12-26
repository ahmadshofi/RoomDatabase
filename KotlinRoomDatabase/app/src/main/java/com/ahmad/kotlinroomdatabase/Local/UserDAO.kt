package com.ahmad.kotlinroomdatabase.Local

import androidx.room.*
import com.ahmad.kotlinroomdatabase.Model.User
import io.reactivex.Flowable

@Dao
interface UserDAO {

    @get:Query("SELECT * FROM users")
    val allUsers:Flowable<List<User>>

    @Query("SELECT * FROM users WHERE id=:userId")
    fun getUserById(userId:Int):Flowable<User>

    @Insert
    fun insertUser(vararg users:User)

    @Update
    fun updateUser(vararg users:User)

    @Delete
    fun deleteUser(user:User)

    @Query("DELETE FROM users")
    fun deleteAllUsers()
}