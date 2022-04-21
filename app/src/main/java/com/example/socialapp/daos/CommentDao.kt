package com.example.socialapp.daos

import android.content.Intent
import android.widget.Toast
import com.example.socialapp.CommentsActivity
import com.example.socialapp.models.Comments
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.item_post.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class CommentDao {
    val db = FirebaseFirestore.getInstance()
    val postCollections = db.collection("posts")

    val commentCollections = db.collection("Comments")

    val auth = Firebase.auth

    fun addComment(postId: String, text: String) {
        GlobalScope.launch {
            val currentUserId = auth.currentUser!!.uid
            val username = auth.currentUser!!.displayName.toString()
            val userImage = auth.currentUser!!.photoUrl.toString()
            //val userDao = UserDao()
            val currentTime = System.currentTimeMillis()
            val comment = Comments(text, currentUserId, currentTime, postId, username, userImage)
            commentCollections.add(comment)
        }
    }

    fun deleteComment(commentId: String) {
        GlobalScope.launch {
            postCollections.document(commentId).delete()
        }
    }
}