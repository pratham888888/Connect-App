package com.example.socialapp.daos

import com.example.socialapp.models.Post
import com.example.socialapp.models.Profile
import com.example.socialapp.models.User
import com.google.android.gms.tasks.Task
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class UserDao {
    private val db=FirebaseFirestore.getInstance()
    val data= FirebaseDatabase.getInstance()
    val database =data.getReference("users")
    private val usersCollection=db.collection("users")
    fun addUser(user: User?){
        user?.let {
            GlobalScope.launch(Dispatchers.IO) {
                usersCollection.document(user.uid).set(it)
            }
        }
    }
    fun updateUser(user: String,profile: Profile?){
        profile?.let {
            GlobalScope.launch(Dispatchers.IO) {
                database.child(user).updateChildren(mapOf(
                    "about" to profile.about,
                    "profession" to profile.profession,
                    "profileimage" to profile.profileimage,
                    "usercity" to profile.usercity,
                    "usercountry" to profile.usercountry,
                    "age" to profile.age,
                    "username" to profile.username)).addOnSuccessListener {
                        //lets see what can be added here
                }
            }
        }
    }
    fun getUserById(uId:String):Task<DocumentSnapshot>{
        return usersCollection.document(uId).get()

    }
}