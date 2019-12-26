package com.ahmad.kotlinroomdatabase.Database

import com.ahmad.kotlinroomdatabase.Model.User
import io.reactivex.Flowable

class UserRepository(private val mLocationDataSource: IUserDataSource):IUserDataSource {
    override val allUsers: Flowable<List<User>>
        get() = mLocationDataSource.allUsers

    override fun getUserById(userId: Int): Flowable<User> {
        return mLocationDataSource.getUserById(userId)
    }

    override fun inserUser(vararg users: User) {
        mLocationDataSource.inserUser(*users)
    }

    override fun updateUser(vararg users: User) {
        mLocationDataSource.updateUser(*users)
    }

    override fun deleteUser(user: User) {
        mLocationDataSource.deleteUser(user)
    }

    override fun deleteAllUsers() {
        mLocationDataSource.deleteAllUsers()
    }

    companion object{
        private var mInstance:UserRepository?=null
        fun getInstance(mLocationDataSource: IUserDataSource):UserRepository{
            if (mInstance == null)
                mInstance = UserRepository(mLocationDataSource)
            return mInstance!!
        }
    }

}