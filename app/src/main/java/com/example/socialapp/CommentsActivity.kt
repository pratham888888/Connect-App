package com.example.socialapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.socialapp.daos.CommentDao
import com.example.socialapp.daos.PostDao
import com.example.socialapp.models.Comments
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_comments.*
import kotlinx.android.synthetic.main.item_comments.*
import kotlinx.android.synthetic.main.item_post.*

class CommentsActivity : AppCompatActivity() {
    var postId: String? = null
    private lateinit var postDao: PostDao
    private lateinit var commentDao: CommentDao
    val db = FirebaseFirestore.getInstance()
    private lateinit var commentRecyclerView:RecyclerView
    private lateinit var commentArrayList:ArrayList<Comments>
    val comments1 = FirebaseDatabase.getInstance().getReference().child("Comments")
    val commentCollections= db.collection("Comments")
    val auth = Firebase.auth
    var currentUser = auth.currentUser

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_comments)
            commentDao=CommentDao()
            postDao = PostDao()
        postId= intent.getStringExtra("postId")
        val postsCollections = postDao.postCollections
        /* var manager = LinearLayoutManager(this)
       manager.reverseLayout
       manager.stackFromEnd
       recyclerViewComments.layoutManager = manager
       postKey = intent.getStringExtra("postId")
*/
        commentRecyclerView =findViewById(R.id.recyclerViewComments)
        recyclerViewComments.layoutManager= LinearLayoutManager(this)
        recyclerViewComments.setHasFixedSize(true)
        commentArrayList = arrayListOf<Comments>()

        getComments(postId!!)
    }

    private fun getComments(postId: String) {
        commentCollections.whereEqualTo("postid",postId
            ).get().addOnSuccessListener {

            if (it.isEmpty) {

                return@addOnSuccessListener
            }
            for (i in it) {
                    val comment = i.toObject(Comments::class.java)
                    commentArrayList.add(comment)
            }
            commentRecyclerView.adapter = commentsAdapter(commentArrayList)


            //   }

        }
    }
    fun onSendCommentClicked(postId: String) {
        val input =CommentInputs.text.toString().trim()
        CommentInputs.setText("")
        if (input.isNotEmpty()) {
            commentDao.addComment(postId,input)
            val intent= Intent(this,CommentsActivity::class.java)
            intent.putExtra("postId","postId")
            startActivity(intent)
        }else
        {
            Toast.makeText(this,"Please enter something",Toast.LENGTH_SHORT).show()
        }
    }



    /*comments1.addValueEventListener(object:ValueEventListener{
        override fun onDataChange(snapshot: DataSnapshot) {
            if(snapshot.exists()){
                for(commentSnapshot in snapshot.children){
                    val comment =commentSnapshot.getValue(Comments::class.java)
                  //  if(comment!!.postid.equals(postId)){

                  // val postComments= db.collection("Comments").whereEqualTo("postid",comment!!.postid).get()
                        commentArrayList.add(comment!!)
                 //   }
*/









        }


//comments1.equals(postId)