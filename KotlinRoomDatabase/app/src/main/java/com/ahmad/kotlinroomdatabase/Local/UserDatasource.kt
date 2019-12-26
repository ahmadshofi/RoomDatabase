package com.ahmad.kotlinroomdatabase.Local

import com.ahmad.kotlinroomdatabase.Database.IUserDataSource
import com.ahmad.kotlinroomdatabase.Database.UserRepository
import com.ahmad.kotlinroomdatabase.Model.User
import io.reactivex.Flowable

class UserDatasource(private val userDAO: UserDAO):IUserDataSource{
    override val allUsers: Flowable<List<User>>
        get() = userDAO.allUsers

    override fun getUserById(userId: Int): Flowable<User> {
        return userDAO.getUserById(userId)
    }

    override fun inserUser(vararg users: User) {
        userDAO.insertUser(*users)
    }

    override fun updateUser(vararg users: User) {
        userDAO.updateUser(*users)
    }

    override fun deleteUser(user: User) {
        userDAO.deleteUser(user)
    }

    override fun deleteAllUsers() {
        userDAO.deleteAllUsers()
    }

    companion object{
        private var mInstance: UserDatasource?=null
        fun getInstance(userDAO: UserDAO): UserDatasource {
            if (mInstance == null)
                mInstance = UserDatasource(userDAO)
            return mInstance!!
        }
    }
}