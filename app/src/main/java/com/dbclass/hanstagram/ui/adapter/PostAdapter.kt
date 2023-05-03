package com.dbclass.hanstagram.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dbclass.hanstagram.R
import com.dbclass.hanstagram.databinding.ItemPostBinding
import com.dbclass.hanstagram.data.db.posts.PostEntity
import com.dbclass.hanstagram.data.repository.UserRepository
import com.dbclass.hanstagram.ui.activity.MainActivity
import com.dbclass.hanstagram.ui.fragment.ProfilePageFragment
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.StringTokenizer

class PostAdapter(private val posts: List<PostEntity>, val context: Context) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder
        = PostViewHolder(ItemPostBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val postBinding = (holder as PostViewHolder).binding

        CoroutineScope(Dispatchers.Default).launch {
            val nick = UserRepository.getNickname(posts[position].userID)
            val profileImage = UserRepository.getProfileImage(posts[position].userID)
            CoroutineScope(Dispatchers.Main).launch {
                postBinding.textNickname.text = nick ?: posts[position].userID
                Glide.with(context).load(profileImage).error(R.drawable.baseline_account_circle_24).into(postBinding.imageProfile)
            }
        }


        postBinding.textContent.text = posts[position].content
        postBinding.imageHeart.setOnClickListener {
            Glide.with(context).load(R.drawable.ic_heart_filled_100).into(postBinding.imageHeart)
        }
        postBinding.imageDislike.setOnClickListener {
            Glide.with(context).load(R.drawable.ic_disgusting_filled_100).into(postBinding.imageDislike)
        }
        postBinding.imageContent.setOnClickListener {
            // TODO 댓글 창 - dialog fragment ?
        }
        postBinding.imageReport.setOnClickListener {
            // TODO 신고 - 매너 온도 하락 ?
        }

        val contentImages = StringTokenizer(posts[position].images)
        val i1 = contentImages.nextToken()
        Glide.with(context).load(i1).into(postBinding.imageContent)

        postBinding.textNickname.setOnClickListener{
            val profilePageFragment = ProfilePageFragment().apply {
                arguments = bundleOf("user_id" to posts[position].userID)
            }
            (context as MainActivity).supportFragmentManager.beginTransaction().replace(R.id.fragment_content,
                profilePageFragment).commit()
        }

    }

    override fun getItemCount(): Int = posts.size

    inner class PostViewHolder(val binding: ItemPostBinding): RecyclerView.ViewHolder(binding.root)

}