package com.example.socialapp.models

data class Post(
    val text:String="",
    val createdBy:User =User(),
    val createdAt:Long= 0L,
    val image: String="",
    val likedBy:ArrayList<String> = ArrayList(),
    val Comment: ArrayList<Map<String,Any>> = ArrayList()
)
