package com.dbclass.hanstagram.ui.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dbclass.hanstagram.R
import com.dbclass.hanstagram.data.db.users.UserEntity
import com.dbclass.hanstagram.data.repository.FollowRepository
import com.dbclass.hanstagram.data.repository.UserRepository
import com.dbclass.hanstagram.databinding.ItemFoundUserBinding
import com.dbclass.hanstagram.ui.activity.MainActivity
import com.dbclass.hanstagram.ui.fragment.ProfilePageFragment
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class FoundUserAdapter(private val foundUsers: MutableList<UserEntity>, private val isButtonFollowVisible: Boolean = false, private val myID: String? = null) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private lateinit var context: Context
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        context = parent.context
        return FoundUserViewHolder(ItemFoundUserBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }
    override fun getItemCount() = foundUsers.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        with((holder as FoundUserViewHolder).binding) {
            buttonToggleFollow.run {
                if(foundUsers[position].id == myID) {
                    isVisible = false
                    return@run
                }
                isVisible = isButtonFollowVisible
                val defaultScope = CoroutineScope(Dispatchers.Default)
                val mainScope = CoroutineScope(Dispatchers.Main)
                defaultScope.launch {
                    if (myID != null) {
                        var followPID: Long? = FollowRepository.getFollowPID(myID, foundUsers[position].id)
                        mainScope.launch {
                            text = if (followPID != null) {
                                context.getString(R.string.text_following_now)
                            } else {
                                context.getString(R.string.text_follow)
                            }

                            setOnClickListener {
                                defaultScope.launch {
                                    if (followPID == null) {
                                        followPID = FollowRepository.doFollow(myID, foundUsers[position].id)
                                        mainScope.launch {
                                            text = context.getString(R.string.text_following_now)
                                        }
                                    } else {
                                        FollowRepository.doUnFollow(followPID!!)
                                        followPID = null
                                        mainScope.launch {
                                            text = context.getString(R.string.text_follow)
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }

            textFoundId.text = foundUsers[position].id
            textNicknameOfId.text = foundUsers[position].nickname
            Glide.with(context).load(foundUsers[position].profileImage)
                .error(R.drawable.ic_account_96).into(imageProfile)
            itemFoundUser.setOnClickListener {
                (context as MainActivity).supportFragmentManager.beginTransaction()
                    .replace(R.id.fragment_content, ProfilePageFragment.newInstance(null).apply {
                        arguments = bundleOf("user_id" to foundUsers[position].id)
                    }).commit()
            }
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
            }
        }
    }
    inner class FoundUserViewHolder(val binding: ItemFoundUserBinding): RecyclerView.ViewHolder(binding.root)
}