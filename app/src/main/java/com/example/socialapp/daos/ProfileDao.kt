package com.example.socialapp.daos

import com.example.socialapp.models.Post
import com.example.socialapp.models.Profile
import com.example.socialapp.models.User
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class ProfileDao {
    val db = FirebaseFirestore.getInstance()
    val auth = Firebase.auth
    val user = auth.uid


    val userRef = FirebaseDatabase.getInstance().getReference().child("profiles")


    fun addProfile(profile: Profile?) {

        profile?.let {
            GlobalScope.launch(Dispatchers.IO) {
                val profileRef =db.collection("users")
                profileRef.document(user!!).update(mapOf(
                    "about" to profile.about,
                    "profession" to profile.profession,
                    "profileimage" to profile.profileimage,
                    "usercity" to profile.usercity,
                    "usercountry" to profile.usercountry,
                    "age" to profile.age,
                    "username" to profile.username))



            }
        }

    }
}