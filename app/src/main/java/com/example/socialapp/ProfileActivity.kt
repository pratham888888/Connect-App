package com.example.socialapp

import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Bitmap
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.bumptech.glide.Glide
import com.example.socialapp.daos.ProfileDao
import com.example.socialapp.daos.UserDao
import com.example.socialapp.models.Profile
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.theartofdev.edmodo.cropper.CropImage
import com.theartofdev.edmodo.cropper.CropImageView
import kotlinx.android.synthetic.main.activity_create_post.*
import kotlinx.android.synthetic.main.activity_profile.*
import kotlinx.android.synthetic.main.item_post.*


class ProfileActivity : AppCompatActivity() {


    lateinit var textView: TextView
    var username:String=""
  //  var picUriString:String=""
    private val REQUEST_PICK_IMAGE_2 = 12345
    var usercity:String=""
    private lateinit var picUri:Uri
    private val TAG ="AppDebug"
    var userCountry:String=""
    var userProfession:String=""
    var age:String=""
    var about:String=""
    var userImage: String=""
    val auth = Firebase.auth
    val user1= auth.currentUser!!.uid
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)
        var preferences:SharedPreferences= getSharedPreferences("Preference", MODE_PRIVATE)
     /*   var FirstTime= preferences.getString("FirstTimeInstall","")
       if(FirstTime.equals("Yes")){
            var intent= Intent(this,MainActivity::class.java)
            startActivity(intent)
            finish()
        }else{
               var editor:SharedPreferences.Editor=preferences.edit()
            editor.putString("FirstTimeInstall","Yes")
            editor.apply()
        }*/

        var profile: ProfileDao
        var user:UserDao

        circleImageView.setOnClickListener{
           openGallery()
        }

        btnUpdate.setOnClickListener {
            //  textView = findViewById<EditText>(R.id.input)
            //textView.text= inputUsername.text

            username = inputUsername.text.toString()
            usercity = inputCity.text.toString()
            userCountry = inputCountry.text.toString()
            userProfession = inputProfession.text.toString()
            about = inputAbout.text.toString()
            age = inputAge.text.toString()
            if (username.isEmpty() || usercity.isEmpty() || userCountry.isEmpty() || userProfession.isEmpty() || age.isEmpty()) {
                Toast.makeText(this, "Please provide all the details", Toast.LENGTH_SHORT).show()
            } else if (userImage.isEmpty()) {
                Toast.makeText(this, "Please select a picture", Toast.LENGTH_SHORT).show()
            } else {
                profile = ProfileDao()
                user = UserDao()
                val profile1 =
                    Profile(about, userProfession, userImage, usercity, userCountry, age, username)
                profile.addProfile(profile1)
                user.updateUser(user1, profile1)

                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                finish()
            }
        }

    }
    private fun openGallery() {
        val intent= Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        intent.type="image/*"
        val mimeTypes = arrayOf("image/jpeg","image/png","image/jpg")
        intent.putExtra(Intent.EXTRA_MIME_TYPES,mimeTypes)
        intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        startActivityForResult(intent,REQUEST_PICK_IMAGE_2)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)


        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_PICK_IMAGE_2) {
                data?.data?.let { uri ->
                    launchImageCrop(uri)

                }



            }else if(requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE){
                val result= CropImage.getActivityResult(data)
                result.uri?.let{ uri ->
                    userImage=uri.toString()
                    setImage(uri)

                }
            }

        }else{
            Log.e(TAG,"couldn't select image")
        }
    }
    private fun setImage(uri: Uri) {
        Glide.with(this).load(uri).into(CircleImageView)

    }

    private fun launchImageCrop(uri: Uri){
        CropImage.activity(uri).setGuidelines(CropImageView.Guidelines.ON).setAspectRatio(2240,2240)
            .setCropShape(CropImageView.CropShape.RECTANGLE).start(this)
    }

}