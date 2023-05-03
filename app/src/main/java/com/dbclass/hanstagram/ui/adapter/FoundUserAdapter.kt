package com.dbclass.hanstagram.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dbclass.hanstagram.data.db.users.UserEntity
import com.dbclass.hanstagram.databinding.ItemFoundUserBinding

class FoundUserAdapter(private val users: MutableList<UserEntity>, val context: Context) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder =
        FountUserViewHolder(ItemFoundUserBinding.inflate(LayoutInflater.from(parent.context), parent ,false))

    override fun getItemCount() = users.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as FountUserViewHolder).binding.textFoundId.text = users[position].id
        Glide.with(context).load(users[position].profileImage).into(holder.binding.imageProfile)
    }

    inner class FountUserViewHolder(val binding: ItemFoundUserBinding): RecyclerView.ViewHolder(binding.root)
}