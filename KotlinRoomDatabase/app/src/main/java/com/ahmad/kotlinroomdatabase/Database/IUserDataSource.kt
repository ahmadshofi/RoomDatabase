package com.ahmad.kotlinroomdatabase.Database

import com.ahmad.kotlinroomdatabase.Model.User
import io.reactivex.Flowable

interface IUserDataSource {
    val allUsers:Flowable<List<User>>
    fun getUserById(userId:Int):Flowable<User>
    fun inserUser(vararg users:User)
    fun updateUser(vararg users:User)
    fun deleteUser(user:User)
    fun deleteAllUsers()
}