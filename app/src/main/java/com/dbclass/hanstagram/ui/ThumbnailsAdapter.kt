package com.dbclass.hanstagram.ui

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dbclass.hanstagram.data.db.posts.PostEntity
import com.dbclass.hanstagram.databinding.ItemThumbnailsRowBinding
import kotlin.math.roundToInt

class ThumbnailsAdapter(private val posts: List<PostEntity>, private val context: Context) :  RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder
        = ThumbnailsViewHolder(ItemThumbnailsRowBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun getItemCount(): Int = kotlin.math.ceil((posts.size / 3.0)).roundToInt()

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val pos1 = position * 3
        val pos2 = position * 3 + 1
        val pos3 = position * 3 + 2
        val thumbnailsBinding = (holder as ThumbnailsViewHolder).binding

        Glide.with(context).load(posts[pos1].images).into(thumbnailsBinding.imageFirstColumn)
        if(pos2 < posts.size)
            Glide.with(context).load(posts[pos2].images).into(thumbnailsBinding.imageSecondColumn)
        if(pos3 < posts.size)
            Glide.with(context).load(posts[pos3].images).into(thumbnailsBinding.imageThirdColumn)
    }

    inner class ThumbnailsViewHolder(val binding: ItemThumbnailsRowBinding): RecyclerView.ViewHolder(binding.root)
}