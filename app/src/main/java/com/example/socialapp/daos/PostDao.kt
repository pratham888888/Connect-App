package com.example.socialapp.daos

import android.content.ContentValues.TAG
import android.util.Log
import android.widget.Toast
import com.example.socialapp.models.Comments
import com.example.socialapp.models.Post
import com.example.socialapp.models.User
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.lang.reflect.TypeVariable

class PostDao {

    val db = FirebaseFirestore.getInstance()
    val postCollections = db.collection("posts")
    public lateinit var userId: String
   public lateinit var user12: User
    public var displayname:String=""

    val userCollections = db.collection("Users")
    val commentCollections = db.collection("Comments")

    val auth = Firebase.auth
    var postPhoto: String = ""

    fun addPost(text: String, picUriString: String) {
        GlobalScope.launch {
            val currentUserId = auth.currentUser!!.uid
            val userDao = UserDao()
            val user = userDao.getUserById(currentUserId).await().toObject(User::class.java)!!
            val currentTime = System.currentTimeMillis()
            val post = Post(text, user, currentTime, picUriString)
            postCollections.document().set(post)
        }
    }

    fun addComment(postId: String, text: String) {
        GlobalScope.launch {
            val currentUserId = auth.currentUser!!.uid
            val username = auth.currentUser!!.displayName.toString()
            val userImage = auth.currentUser!!.photoUrl.toString()
            //val userDao = UserDao()
            val currentTime = System.currentTimeMillis()
            val comment = Comments(text, currentUserId, currentTime, postId, username, userImage)
            commentCollections.add(comment)
            /*  val PostsRef =
                FirebaseDatabase.getInstance().getReference().child("posts").child(postId)
                    .child("Comment")
            val post = getPostById(postId).await().toObject(Post::class.java)!!
            val user = userDao.getUserById(currentUserId).await().toObject(User::class.java)!!

            val post1 = db.collection("posts").document(postId).collection("Comment")
            val currentTimeString = currentTime.toString()
            val random_key = currentUserId + currentTime
            val comment = Comments(text, user, currentTime)
            val commentArrayList: Map<String, Any> =
                mapOf("comment" to text, "uid" to currentUserId, "time" to currentTimeString)

              val hashMap:HashMap<String,Any> = HashMap<String,Any>()

             hashMap.put("comment",text)
            hashMap.put("uid",currentUserId)
            hashMap.put("time",currentTimeString)



            post?.let {
                GlobalScope.launch(Dispatchers.IO) {
                    val profileRef =db.collection("users")
                    profileRef.document(postId!!).update(
                        mapOf(
                        "comment" to hashMap))
                }
                    //post.Comment.add(commentArrayList)
*/
        }
    }


    fun deletePost(postId: String) {
        GlobalScope.launch {
            postCollections.document(postId).delete()
        }
    }

    fun getPostById(postId: String): Task<DocumentSnapshot> {
        return postCollections.document(postId).get()
    }

    fun getUserById(userId: String): Task<DocumentSnapshot> {
        return userCollections.document(userId).get()
    }




    fun updateLikes(postId: String) {
        GlobalScope.launch {
            val currentUserId = auth.currentUser!!.uid
            val post = getPostById(postId).await().toObject(Post::class.java)!!
            val isLiked = post.likedBy.contains(currentUserId)

            if (isLiked) {
                post.likedBy.remove(currentUserId)
            } else {
                post.likedBy.add(currentUserId)
            }
            postCollections.document(postId).set(post)
        }

    }


}