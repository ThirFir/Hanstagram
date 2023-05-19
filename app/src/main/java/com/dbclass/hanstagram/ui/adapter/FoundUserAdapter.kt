package com.dbclass.hanstagram.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dbclass.hanstagram.R
import com.dbclass.hanstagram.data.db.users.UserEntity
import com.dbclass.hanstagram.data.repository.follow.FollowRepository
import com.dbclass.hanstagram.data.repository.follow.FollowRepositoryImpl
import com.dbclass.hanstagram.data.repository.user.UserRepository
import com.dbclass.hanstagram.data.repository.user.UserRepositoryImpl
import com.dbclass.hanstagram.databinding.ItemFoundUserBinding
import com.dbclass.hanstagram.ui.activity.MainActivity
import com.dbclass.hanstagram.ui.fragment.ProfilePageFragment
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class FoundUserAdapter(
    private val foundUsers: MutableList<UserEntity>,
    private val isButtonFollowVisible: Boolean = false,
    private val myID: String? = null
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private lateinit var context: Context
    private val followRepository: FollowRepository = FollowRepositoryImpl
    private val userRepository: UserRepository = UserRepositoryImpl
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        context = parent.context
        return FoundUserViewHolder(
            ItemFoundUserBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun getItemCount() = foundUsers.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        with((holder as FoundUserViewHolder).binding) {
            buttonToggleFollow.run {
                if (foundUsers[position].id == myID) {
                    isVisible = false
                    return@run
                }
                isVisible = isButtonFollowVisible
                CoroutineScope(Dispatchers.Main).launch {
                    if (myID != null) {
                        var followPID: Long? =
                            followRepository.getFollowPID(myID, foundUsers[position].id)
                        text = if (followPID != null) {
                            context.getString(R.string.text_following_now)
                        } else {
                            context.getString(R.string.text_follow)
                        }

                        setOnClickListener {
                            CoroutineScope(Dispatchers.Main).launch {
                                if (followPID == null) {
                                    followPID = followRepository.doFollow(myID, foundUsers[position].id)
                                    text = context.getString(R.string.text_following_now)
                                } else {
                                    followRepository.doUnFollow(followPID!!)
                                    followPID = null
                                    text = context.getString(R.string.text_follow)
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
        CoroutineScope(Dispatchers.Main).launch {
            val foundUsers = userRepository.findUsers(input)
            updateFoundUsers(foundUsers)
        }
    }

    inner class FoundUserViewHolder(val binding: ItemFoundUserBinding) :
        RecyclerView.ViewHolder(binding.root)
}