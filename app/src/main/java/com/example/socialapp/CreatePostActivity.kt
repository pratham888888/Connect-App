package com.example.socialapp

import android.content.ActivityNotFoundException
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.example.socialapp.daos.PostDao
import com.theartofdev.edmodo.cropper.CropImage
import com.theartofdev.edmodo.cropper.CropImageView
import kotlinx.android.synthetic.main.activity_create_post.*


class CreatePostActivity : AppCompatActivity() {
    private lateinit var postDao: PostDao
    private val REQUEST_PERMISSION = 100
    private val REQUEST_IMAGE_CAPTURE = 1
    val CAMERA_CAPTURE = 1
    val PIC_CROP = 2
    private val MY_BUCKET_PATH = "gs://socialapp-d7e7c.appspot.com"
    private lateinit var picUri: Uri
    private lateinit var picUriString: String
    private val TAG = "AppDebug"
    private val REQUEST_PICK_IMAGE = 1234


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_post)
        postDao = PostDao()
    //    getSupportActionBar()!!.setDisplayHomeAsUpEnabled(true)
        postButton.setOnClickListener {
            // uploadImage()
            val input = postInput.text.toString().trim()

            if (picUri.toString().isNotEmpty()) {
                picUriString = picUri.toString()
                postDao.addPost(input, picUriString)

                finish()
            }else{
                Toast.makeText(this,"Please select a photo",Toast.LENGTH_LONG).show()
            }
        }

        imagePreview.setOnClickListener {
            val options = arrayOf<CharSequence>( "Choose from Gallery", "Cancel")
            val builder: android.app.AlertDialog.Builder = android.app.AlertDialog.Builder(this)
            builder.setTitle("Add Photo!")
            builder.setItems(options, DialogInterface.OnClickListener { dialog, item ->
              if (options[item] == "Choose from Gallery") {
                    openGallery()
                } else if (options[item] == "Cancel") {
                    dialog.dismiss()
                }
            })
            builder.show()
        }
    }


    // this event will enable the back
    // function to the button on press

    override fun onResume() {
        super.onResume()
        checkCameraPermission()
    }

    private fun checkCameraPermission() {
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA)
            != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(android.Manifest.permission.CAMERA),
                REQUEST_PERMISSION
            )
        }
    }



    private fun openGallery() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        intent.type = "image/*"
        val mimeTypes = arrayOf("image/jpeg", "image/png", "image/jpg")
        intent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes)
        intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        startActivityForResult(intent, REQUEST_PICK_IMAGE)
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)


        if (resultCode == RESULT_OK) {
          if (requestCode == REQUEST_PICK_IMAGE) {
                data?.data?.let { uri ->
                    launchImageCrop(uri)
                }
            } else if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
                val result = CropImage.getActivityResult(data)
                result.uri?.let { uri ->
                    picUri = uri
                    setImage(uri)
                }
            }

        } else {
            Log.e(TAG, "couldn't select image")
        }
    }

    private fun setImage(uri: Uri) {
        Glide.with(this).load(uri).into(imagePreview)

    }
    // private fun setImageCamera(bitmap:Bitmap) {
    //   Glide.with(this).load(bitmap).into(imagePreview)

    //}
    private fun launchImageCrop(uri: Uri) {
        CropImage.activity(uri).setGuidelines(CropImageView.Guidelines.ON)
            .setAspectRatio(2240, 2240)
            .setCropShape(CropImageView.CropShape.RECTANGLE).start(this)
    }


}
