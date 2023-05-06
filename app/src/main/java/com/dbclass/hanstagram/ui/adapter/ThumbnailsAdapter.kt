package com.dbclass.hanstagram.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.dbclass.hanstagram.R
import com.dbclass.hanstagram.data.db.posts.PostEntity
import com.dbclass.hanstagram.databinding.ItemThumbnailsBinding
import com.dbclass.hanstagram.ui.activity.MainActivity
import com.dbclass.hanstagram.ui.dialog.PostFragmentDialog
import jp.wasabeef.glide.transformations.CropCircleTransformation
import jp.wasabeef.glide.transformations.CropSquareTransformation

class ThumbnailsAdapter(private val posts: List<PostEntity>) :  RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private lateinit var context: Context
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        context = parent.context
        return ThumbnailsViewHolder(ItemThumbnailsBinding.inflate(LayoutInflater.from(context), parent, false))
    }

    override fun getItemCount(): Int = posts.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val post = posts[position]

        Glide.with(context).load(post.images).error(R.drawable.ic_error_96).centerCrop()
            .override((holder as ThumbnailsViewHolder).binding.root.width).into(holder.binding.imageThumbnail)
        holder.binding.imageThumbnail.setOnClickListener {
            PostFragmentDialog().apply {
                arguments = bundleOf("post_id" to post.postID)
            }.show(
                (context as MainActivity).supportFragmentManager, "PostDialog"
            )
        }
    }

    inner class ThumbnailsViewHolder(val binding: ItemThumbnailsBinding): RecyclerView.ViewHolder(binding.root)
}