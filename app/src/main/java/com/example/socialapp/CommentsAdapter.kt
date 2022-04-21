package com.example.socialapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.socialapp.daos.UserDao
import com.example.socialapp.models.Comments
import com.example.socialapp.models.User
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.item_comments.view.*
import kotlinx.coroutines.tasks.await

class commentsAdapter( val commentList:ArrayList<Comments>,): RecyclerView.Adapter<commentsAdapter.MyViewHolder>(){
    val auth = Firebase.auth
    val db = FirebaseFirestore.getInstance()
    val postCollections = db.collection("posts")


    val userCollections= db.collection("Users")


    class MyViewHolder(itemView:View):RecyclerView.ViewHolder(itemView){
        val commentProfile:ImageView= itemView.findViewById(R.id.imageView_comment_item)
        val username:TextView= itemView.findViewById(R.id.tv_name_comment_item)
        val commentTime:TextView= itemView.findViewById(R.id.tv_time_comment_item)
        val comment:TextView= itemView.findViewById(R.id.tv_comment_item)
        val deleteComment:TextView =itemView.findViewById(R.id.del_comment)



       // val commentDelete:ImageView= itemView.findViewById(R.id.del_comment)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
    val itemView=LayoutInflater.from(parent.context).inflate(R.layout.item_comments,parent,false)
       /* itemView.del_comment.setOnClickListener {
            listener.onDeleteButtonClicked(snapshots.getSnapshot(viewHolder.adapterPosition).id)
        }*/
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentItem = commentList[position]
        holder.username.setText(currentItem.username)
        Glide.with(holder.commentProfile.context).load(currentItem.userImage).circleCrop().into(holder.commentProfile)
        holder.comment.setText(currentItem.text)
        holder.commentTime.text = Utils.getTimeAgo(
            currentItem.commentedAt)
    }

    override fun getItemCount(): Int {
        return commentList.size
    }
  /*  interface ICommentsAdapter {

       fun onDeleteButtonClicked(commentid:String)
    }*/
}