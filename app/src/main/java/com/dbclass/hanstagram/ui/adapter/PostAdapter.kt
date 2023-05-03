package com.dbclass.hanstagram.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dbclass.hanstagram.R
import com.dbclass.hanstagram.databinding.ItemPostBinding
import com.dbclass.hanstagram.data.db.posts.PostEntity
import java.util.StringTokenizer

class PostAdapter(private val posts: List<PostEntity>, val context: Context) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder
        = PostViewHolder(ItemPostBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val postBinding = (holder as PostViewHolder).binding
        postBinding.textNickname.text = posts[position].userID
        postBinding.imageHeart.setOnClickListener {
            Glide.with(context).load(R.drawable.ic_heart_filled_100).into(postBinding.imageHeart)
        }
        postBinding.imageDislike.setOnClickListener {
            Glide.with(context).load(R.drawable.ic_disgusting_filled_100).into(postBinding.imageDislike)
        }
        val images = StringTokenizer(posts[position].images)
        Glide.with(context).load(images.nextToken()).into(postBinding.imageContent)
    }

    override fun getItemCount(): Int = posts.size

    inner class PostViewHolder(val binding: ItemPostBinding): RecyclerView.ViewHolder(binding.root)

}