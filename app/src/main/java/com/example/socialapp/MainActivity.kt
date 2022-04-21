package com.example.socialapp

import android.content.ContentValues
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.socialapp.daos.PostDao
import com.example.socialapp.models.Post
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.Query
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.appbar.*
import kotlinx.android.synthetic.main.item_post.*
import android.content.DialogInterface
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import com.example.socialapp.daos.ProfileDao
import com.example.socialapp.models.User
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.getValue
import com.google.firebase.firestore.FirebaseFirestore
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.android.synthetic.main.activity_comments.*
import kotlinx.android.synthetic.main.activity_profile.*
import kotlinx.android.synthetic.main.item_comments.*


class MainActivity : AppCompatActivity(), IPostAdapter {
    private lateinit var adapter: PostAdapter
    lateinit var mGoogleSignInClient: GoogleSignInClient
    private lateinit var postDao: PostDao
     lateinit var user12: User
    val db = FirebaseFirestore.getInstance()

        val postCollections = db.collection("posts")
    private lateinit var profileDao: ProfileDao
    private lateinit var profilePic:ImageView

    val auth by lazy {
        FirebaseAuth.getInstance()
    }

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        lateinit var mGoogleSignInClient: GoogleSignInClient



        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        mGoogleSignInClient= GoogleSignIn.getClient(this,gso)


        logout.setOnClickListener {
         showPopup()


        }


        fab.setOnClickListener {
        val intent= Intent(this,CreatePostActivity::class.java)
            startActivity(intent)
        }

         MemeAct.setOnClickListener {
             val intent= Intent(this,MemeActivity::class.java)
             startActivity(intent)
         }




       // settings.setOnClickListener {
         //   postDao.deletePost()
        //}
        setUpRecyclerView()
    }


    private fun setUpRecyclerView() {
        postDao = PostDao()
        val postsCollections = postDao.postCollections
        val query = postsCollections.orderBy("createdAt", Query.Direction.DESCENDING)
        val recyclerViewOptions = FirestoreRecyclerOptions.Builder<Post>().setQuery(query, Post::class.java).build()

        adapter = PostAdapter(recyclerViewOptions, this)

        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)
    }
  //  private fun setUpRecyclerViewForComments() {
    //    postDao = PostDao()
      //  val commentCollections = postDao.commentCollections
      //  val query =commentCollections.orderBy("commentedAt", Query.Direction.DESCENDING)
        //val recyclerViewOptions = FirestoreRecyclerOptions.Builder<Comments>().setQuery(query, Comments::class.java).build()

        //adapter = PostAdapter(recyclerViewOptions, this)

        //recyclerViewComments.adapter = adapter
        //recyclerViewComments.layoutManager = LinearLayoutManager(this)
    //}

    override fun onStart() {
        super.onStart()
        adapter.startListening()
    }


    override fun onStop() {
        super.onStop()
        adapter.stopListening()
    }

    override fun onLikeClicked(postId: String) {
        postDao.updateLikes(postId)
    }


    override fun onRecyclerViewItemClicked(view: View, postId: String) {
        val alert: android.app.AlertDialog.Builder = android.app.AlertDialog.Builder(this@MainActivity)
        alert.setMessage("Are you sure?This post will be permanently deleted.")
            .setPositiveButton("Delete", DialogInterface.OnClickListener { dialog, which ->
                postDao.deletePost(postId)
            }).setNegativeButton("Cancel", null)
        val alert1: android.app.AlertDialog? = alert.create()
        alert1!!.show()

    }

    override fun onCommentClicked(postId: String) {
        val input =CommentInputs.text.toString().trim()
        CommentInputs.setText("")
        if (input.isNotEmpty()) {
            postDao.addComment(postId,input)
            val intent= Intent(this,CommentsActivity::class.java)
            intent.putExtra("postId",postId)
            startActivity(intent)
        }else
        {
            Toast.makeText(this,"Please enter something",Toast.LENGTH_SHORT).show()
        }
    }

    override fun onProfileClicked(postId: String) {
         showUserProfile(postId)

    }

    override fun onActivityCommentsClicked(postId: String) {
        val intent= Intent(this,CommentsActivity::class.java)
        intent.putExtra("postId",postId)
        startActivity(intent)
    }

    private fun showPopup() {
        val alert: android.app.AlertDialog.Builder = android.app.AlertDialog.Builder(this@MainActivity)
        alert.setMessage("Are you sure?")
            .setPositiveButton("Logout", DialogInterface.OnClickListener { dialog, which ->
               logout() // Last step. Logout function
            }).setNegativeButton("Cancel", null)
        val alert1: android.app.AlertDialog? = alert.create()
        alert1!!.show()
    }
    private fun logout(){
        val fAuth = FirebaseAuth.getInstance()
        fAuth.signOut()
        startActivity(Intent(this, SignInActivity::class.java))
        finish()
        Toast.makeText(this, "Logging Out", Toast.LENGTH_SHORT).show()
    }



    fun showUserProfile(postId: String) {

        //val post = postCollections.document(postId).get().addOnSuccessListener {
        postCollections.document(postId).get().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                // Document found in the offline cache
                val document = task.result

                Log.d(ContentValues.TAG, "Cached document data: ${document?.data}")
            } else {
                Log.d(ContentValues.TAG, "Cached get failed: ", task.exception)
            }
        }.addOnSuccessListener {
            val post12 = it.toObject(Post::class.java)
            user12 = post12!!.createdBy
           val uid = user12.uid
            val displayname= user12.username.toString()
            val profession= post12!!.createdBy.profession
            val usercity= user12.usercity.toString()
            val profileimage= user12.profileimage.toString()
            val usercountry= user12.usercountry.toString()
            val intent= Intent(this,MyProfile::class.java)
            intent.putExtra("displayname",displayname)
            intent.putExtra("userCity",usercity)
            intent.putExtra("userCountry",usercountry)
            intent.putExtra("profileImage",profileimage)
            intent.putExtra("profession",profession)
            intent.putExtra("user",postId)
            intent.putExtra("userid",uid)

            startActivity(intent)
        }
        // userCollections.document(userId).get().addOnSuccessListener {
        //           user12= it.toObject(User::class.java)!!

        //   userId= post.createdBy.uid
        //user12= getUserById(userId).await().toObject(User::class.java)!!

        //
    }
}