package com.ahmad.kotlinroomdatabase

import android.content.DialogInterface
import android.database.Observable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.ContextMenu
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.*
import androidx.appcompat.app.AlertDialog
import com.ahmad.kotlinroomdatabase.Database.UserRepository
import com.ahmad.kotlinroomdatabase.Local.UserDatabase
import com.ahmad.kotlinroomdatabase.Local.UserDatasource
import com.ahmad.kotlinroomdatabase.Model.User
import io.reactivex.ObservableOnSubscribe
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.functions.Action
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*
import java.util.function.Consumer
import kotlin.collections.ArrayList

class MainActivity : AppCompatActivity() {

    lateinit var adapter: ArrayAdapter<*>
    var userList:MutableList<User> = ArrayList()


    //Database
    private var compositeDisposable:CompositeDisposable?=null
    private var userRepository:UserRepository?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //Init
        compositeDisposable = CompositeDisposable()

        adapter = ArrayAdapter(this,android.R.layout.simple_list_item_1,userList)

        registerForContextMenu(lst_users)
        lst_users!!.adapter = adapter

        //Database
        val userDatabase = UserDatabase.getInstance(this)
        userRepository = UserRepository.getInstance(UserDatasource.getInstance(userDatabase.userDAO()))

        //Load all data from DB
        loadData()

        //event for FAB
        fab_add.setOnClickListener({
            val disposable = io.reactivex.Observable.create(ObservableOnSubscribe<Any> {e ->
                val user = User()
                user.name = "EDMTDev"
                user.email = UUID.randomUUID().toString()+"@gmail.com"
                userRepository!!.inserUser(user)
                e.onComplete()
            })

                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(io.reactivex.functions.Consumer{
                    //Succes
                },
                    io.reactivex.functions.Consumer{
                            throwable -> Toast.makeText(this,""+throwable.message,Toast.LENGTH_SHORT).show()
                    },
                    Action { loadData() })
            compositeDisposable!!.addAll(disposable)
        })
    }

    private fun loadData() {
        val disposable = userRepository!!.allUsers
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe({users-> onGetAllUserSuccess(users)})

            {
                throwable-> Toast.makeText(this,""+throwable.message,Toast.LENGTH_SHORT).show()
            }

        compositeDisposable!!.add(disposable)
    }

    private fun onGetAllUserSuccess(users: List<User>) {
        userList.clear()
        userList.addAll(users)
        adapter.notifyDataSetChanged()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu,menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId)
        {
            R.id.clear -> deleteAllUsers()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun deleteAllUsers() {
        val disposable = io.reactivex.Observable.create(ObservableOnSubscribe<Any> {e ->
            userRepository!!.deleteAllUsers()
            e.onComplete()
        })

            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe(io.reactivex.functions.Consumer{
                //Succes
            },
                io.reactivex.functions.Consumer{
                    throwable -> Toast.makeText(this,""+throwable.message,Toast.LENGTH_SHORT).show()
                },
                Action { loadData() })
        compositeDisposable!!.addAll(disposable)
    }

    override fun onCreateContextMenu(
        menu: ContextMenu,
        v: View?,
        menuInfo: ContextMenu.ContextMenuInfo?
    ) {
        super.onCreateContextMenu(menu, v, menuInfo)
        val info = menuInfo as AdapterView.AdapterContextMenuInfo
        menu.setHeaderTitle("Select Action: ")

        menu.add(Menu.NONE,0,Menu.NONE,"UPDATE")
        menu.add(Menu.NONE,1,Menu.NONE,"DELETE")
    }

    override fun onContextItemSelected(item: MenuItem): Boolean {
        val info = item.menuInfo as AdapterView.AdapterContextMenuInfo
        val user = userList[info.position]
        when(item.itemId)
        {
            0 //update
            ->
            {
                val edtName = EditText(this)
                edtName.setText(user.name)
                edtName.hint = "Enter your name"

                //cretae dialog
                AlertDialog.Builder(this)
                    .setTitle("Edit")
                    .setMessage("Edit user name")
                    .setView(edtName)
                    .setPositiveButton(android.R.string.ok,DialogInterface.OnClickListener { dialog, which ->

                        if (TextUtils.isEmpty(edtName.text.toString()))
                            return@OnClickListener
                        else
                        {
                            user.name = edtName.text.toString()
                            updateUser(user)
                        }
                    }).setNegativeButton(android.R.string.cancel){
                        dialog, which -> dialog.dismiss()
                    }.create().show()
            }
            1//Delete
            ->
            {
                AlertDialog.Builder(this)
                    .setMessage("Do you want to delete "+user.name)
                    .setPositiveButton(android.R.string.ok,DialogInterface.OnClickListener { dialog, which ->
                        deleteUser(user)
                    }).setNegativeButton(android.R.string.cancel){
                            dialog, which -> dialog.dismiss()
                    }.create().show()
            }
        }
        return true
    }

    private fun deleteUser(user: User) {
        val disposable = io.reactivex.Observable.create(ObservableOnSubscribe<Any> {e ->
            userRepository!!.deleteUser(user)
            e.onComplete()
        })

            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe(io.reactivex.functions.Consumer{
                //Succes
            },
                io.reactivex.functions.Consumer{
                        throwable -> Toast.makeText(this,""+throwable.message,Toast.LENGTH_SHORT).show()
                },
                Action { loadData() })
        compositeDisposable!!.addAll(disposable)
    }

    private fun updateUser(user: User) {
        val disposable = io.reactivex.Observable.create(ObservableOnSubscribe<Any> {e ->
            userRepository!!.updateUser(user)
            e.onComplete()
        })

            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe(io.reactivex.functions.Consumer{
                //Succes
            },
                io.reactivex.functions.Consumer{
                        throwable -> Toast.makeText(this,""+throwable.message,Toast.LENGTH_SHORT).show()
                },
                Action { loadData() })
        compositeDisposable!!.addAll(disposable)

    }

    override fun onDestroy() {
        compositeDisposable!!.clear()
        super.onDestroy()
    }
}
