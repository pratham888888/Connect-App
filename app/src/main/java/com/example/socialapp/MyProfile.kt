package com.example.socialapp

import android.content.ContentValues
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.example.socialapp.models.Post
import com.example.socialapp.models.User
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.android.synthetic.main.activity_create_post.*
import kotlinx.android.synthetic.main.activity_my_profile.*

class MyProfile : AppCompatActivity() {
    val db = FirebaseFirestore.getInstance()
    val profileCollections = db.collection("profiles")
    val userCollections = db.collection("users")
    val auth = Firebase.auth
    val user = auth.uid
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_profile)
        val textView1 = findViewById<TextView>(R.id.myUsername)
        val textView2 = findViewById<TextView>(R.id.myCity)
      //  val textView3 = findViewById<TextView>(R.id.myCountry)
        val textView4 = findViewById<TextView>(R.id.myProfession)
        val textView5 = findViewById<TextView>(R.id.about)
        val textView6= findViewById<TextView>(R.id.myAge)

        val textView7= findViewById<TextView>(R.id.email)

        var userid:String?= intent.getStringExtra("userid")
        userCollections.document(userid!!).get().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                // Document found in the offline cache
                val document = task.result

                Log.d(ContentValues.TAG, "Cached document data: ${document?.data}")
            } else {
                Log.d(ContentValues.TAG, "Cached get failed: ", task.exception)
            }
        }.addOnSuccessListener {
            val user = it.toObject(User::class.java)
            val displayname = user!!.username.toString()
            val profession = user.profession
            val usercity = user.usercity.toString()
            val profileimage = Uri.parse(user.profileimage.toString())
            val usercountry = user.usercountry.toString()
            val userAge= user.age.toString()
            val userAbout= user.about.toString()
            val useremail= user.email.toString()
            /*    db.collection("users").get().addOnCompleteListener {
             if(it.isSuccessful){
                 for(document in it.result){
                    usernamevalue =document.data.getValue("username").toString()
                     usercityvalue =document.data.getValue("usercity").toString()
                     usercountryvalue =document.data.getValue("usercountry").toString()
                     userprofessionvalue =document.data.getValue("profession").toString()
                     userimagevalue =document.data.getValue("profileimage").toString()
                 }*/
            textView1.setText(displayname)
            textView2.setText(usercity+","+usercountry)
            textView4.setText(profession)
            textView5.setText(userAbout)
            textView6.setText(userAge)
            textView7.setText(useremail)
            setImage(profileimage)
        }
    }


    private fun setImage(uri: Uri) {
        Glide.with(this).load(uri).circleCrop().into(myImageView)

    }
}

