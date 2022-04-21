package com.example.socialapp

import android.content.Intent
import android.provider.ContactsContract
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.socialapp.models.Post
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.item_post.view.*

class PostAdapter(options: FirestoreRecyclerOptions<Post>, val listener: IPostAdapter) :RecyclerViewClickListener, FirestoreRecyclerAdapter<Post, PostAdapter.PostViewHolder>(
    options
) {

    class PostViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val postText: TextView = itemView.findViewById(R.id.postTitle)
        val photo :ImageView =itemView.findViewById(R.id.photo)
        val userText: TextView = itemView.findViewById(R.id.userName)
        val createdAt: TextView = itemView.findViewById(R.id.createdAt)
        val likeCount: TextView = itemView.findViewById(R.id.likeCount)
        val userImage: ImageView = itemView.findViewById(R.id.userImage)
        val editButton:ImageView= itemView.findViewById(R.id.settings)
         val profilePhoto:ImageView= itemView.findViewById(R.id.userImage)
        val likeButton: ImageView = itemView.findViewById(R.id.likeButton)
        val commentButton: ImageView = itemView.findViewById(R.id.sendComments)
        val comments:ImageView =itemView.findViewById(R.id.commentButton)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        val viewHolder =  PostViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_post, parent, false))
        viewHolder.likeButton.setOnClickListener{
            listener.onLikeClicked(snapshots.getSnapshot(viewHolder.adapterPosition).id)
        }
        viewHolder.editButton.setOnClickListener {
            listener.onRecyclerViewItemClicked(it,snapshots.getSnapshot(viewHolder.adapterPosition).id)
        }
        viewHolder.commentButton.setOnClickListener {
            listener.onCommentClicked(snapshots.getSnapshot(viewHolder.adapterPosition).id)
        }
        viewHolder.userImage.setOnClickListener {
            listener.onProfileClicked(snapshots.getSnapshot(viewHolder.adapterPosition).id)
        }
        viewHolder.comments.setOnClickListener {
            listener.onActivityCommentsClicked(snapshots.getSnapshot(viewHolder.adapterPosition).id)
        }


        return viewHolder
    }


    override fun onBindViewHolder(holder: PostViewHolder, position: Int, model: Post) {
        holder.postText.text= model.text
        holder.userText.text = model.createdBy.displayName
     Glide.with(holder.userImage.context).load(model.image).into(holder.photo)
        Glide.with(holder.userImage.context).load(model.createdBy.imageUrl).circleCrop().into(holder.userImage)
        holder.likeCount.text = model.likedBy.size.toString()
        holder.createdAt.text = Utils.getTimeAgo(model.createdAt)
        val auth = Firebase.auth
        val currentUserId = auth.currentUser!!.uid

        val isLiked = model.likedBy.contains(currentUserId)
        if(isLiked) {
            holder.likeButton.setImageDrawable(ContextCompat.getDrawable(holder.likeButton.context, R.drawable.final_liked))
        } else {
            holder.likeButton.setImageDrawable(ContextCompat.getDrawable(holder.likeButton.context, R.drawable.final_unliked))
        }

    }
}

interface IPostAdapter {
    fun onLikeClicked(postId: String)
    fun onRecyclerViewItemClicked(view: View, postId: String)
    fun onCommentClicked(postId: String)
    fun onProfileClicked(postId: String)
    fun onActivityCommentsClicked(postId: String)


}