package com.dbclass.hanstagram.ui.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dbclass.hanstagram.R
import com.dbclass.hanstagram.data.db.users.UserEntity
import com.dbclass.hanstagram.data.repository.UserRepository
import com.dbclass.hanstagram.databinding.ItemFoundUserBinding
import com.dbclass.hanstagram.ui.activity.MainActivity
import com.dbclass.hanstagram.ui.fragment.ProfilePageFragment
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class FoundUserAdapter(private val foundUsers: MutableList<UserEntity>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private lateinit var context: Context
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        context = parent.context
        return FountUserViewHolder(ItemFoundUserBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }
    override fun getItemCount() = foundUsers.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as FountUserViewHolder).binding.textFoundId.text = foundUsers[position].id
        holder.binding.textNicknameOfId.text = foundUsers[position].nickname
        if(foundUsers[position].profileImage != null)
            Glide.with(context).load(foundUsers[position].profileImage).into(holder.binding.imageProfile)
        holder.binding.itemFoundUser.setOnClickListener {
            (context as MainActivity).supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_content, ProfilePageFragment().apply {
                    arguments = bundleOf("user_id" to foundUsers[position].id)
                }).commit()
        }
    }

    fun updateFoundUsers(users: List<UserEntity>) {
        this.foundUsers.clear()
        this.foundUsers.addAll(users)
        notifyDataSetChanged()
    }

    fun findUsers(input: String) {
        CoroutineScope(Dispatchers.Default).launch {
            val foundUsers = UserRepository.findUsers(input)
            CoroutineScope(Dispatchers.Main).launch {
                updateFoundUsers(foundUsers)
                Log.d("dddddddddd", foundUsers.toString())
            }
        }
    }
    inner class FountUserViewHolder(val binding: ItemFoundUserBinding): RecyclerView.ViewHolder(binding.root)
}