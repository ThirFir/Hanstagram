package com.dbclass.hanstagram.ui.adapter

import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Build
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.net.toUri
import androidx.core.os.bundleOf
import androidx.recyclerview.widget.RecyclerView
import com.afollestad.materialdialogs.LayoutMode
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.bottomsheets.BasicGridItem
import com.afollestad.materialdialogs.bottomsheets.BottomSheet
import com.afollestad.materialdialogs.bottomsheets.gridItems
import com.bumptech.glide.Glide
import com.dbclass.hanstagram.R
import com.dbclass.hanstagram.databinding.ItemPostBinding
import com.dbclass.hanstagram.data.db.posts.PostEntity
import com.dbclass.hanstagram.data.repository.UserRepository
import com.dbclass.hanstagram.data.utils.getImageHeightWithWidthFully
import com.dbclass.hanstagram.data.utils.startPostCommentActivity
import com.dbclass.hanstagram.ui.activity.MainActivity
import com.dbclass.hanstagram.ui.activity.PostCommentActivity
import com.dbclass.hanstagram.ui.fragment.ProfilePageFragment
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.StringTokenizer

class PostAdapter(private val posts: List<PostEntity>, private val myID: String?) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private lateinit var context: Context
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        context = parent.context
        return PostViewHolder(ItemPostBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val postBinding = (holder as PostViewHolder).binding
        val post = posts[position]

        CoroutineScope(Dispatchers.Default).launch {
            val nick = UserRepository.getNickname(post.userID)
            val profileImage = UserRepository.getProfileImage(post.userID)
            CoroutineScope(Dispatchers.Main).launch {
                postBinding.textNickname.text = nick ?: post.userID
                Glide.with(context).load(profileImage).error(R.drawable.baseline_account_circle_24)
                    .into(postBinding.imageProfile)
            }
        }

        postBinding.textContent.text = post.content
        postBinding.iconPostMenu.setOnClickListener {
            MaterialDialog(context, BottomSheet(LayoutMode.WRAP_CONTENT)).show {
                gridItems(items = listOf(
                        BasicGridItem(R.drawable.ic_siren_48, "신고"),
                        BasicGridItem(R.drawable.baseline_edit_32, "수정"),
                        BasicGridItem(R.drawable.baseline_delete_32, "삭제")
                    )
                ) { _, index, item ->
                    Toast.makeText(context, "dddddd", Toast.LENGTH_SHORT).show()

                }


            }

        }
        postBinding.iconHeart.setOnClickListener {
            Glide.with(context).load(R.drawable.ic_heart_filled_100).into(postBinding.iconHeart)
        }
        postBinding.iconDislike.setOnClickListener {
            Glide.with(context).load(R.drawable.ic_disgusting_filled_100)
                .into(postBinding.iconDislike)
        }

        postBinding.iconComment.setOnClickListener {
            /*
            val commentBottomSheet = PostCommentBottomSheet().apply {
                arguments = bundleOf("post_id" to post.postID)
            }
            commentBottomSheet.show((context as MainActivity).supportFragmentManager, commentBottomSheet.tag)*/
            myID?.let {context.startPostCommentActivity(it, post.postID) }
        }
        postBinding.iconReport.setOnClickListener {
            // TODO 신고 - 매너 온도 하락 ?
        }

        val contentImages = StringTokenizer(post.images)
        val i1 = contentImages.nextToken()
        val scaleHeight = context.getImageHeightWithWidthFully(i1)
        holder.binding.imageContent.layoutParams.height = scaleHeight
        Glide.with(context).load(i1).into(postBinding.imageContent)


        postBinding.textNickname.setOnClickListener {
            val profilePageFragment = ProfilePageFragment().apply {
                arguments = bundleOf("user_id" to post.userID)
            }
            (context as MainActivity).supportFragmentManager.beginTransaction().replace(
                R.id.fragment_content,
                profilePageFragment
            ).commit()
        }

    }

    override fun getItemCount(): Int = posts.size

    inner class PostViewHolder(val binding: ItemPostBinding) : RecyclerView.ViewHolder(binding.root)

}