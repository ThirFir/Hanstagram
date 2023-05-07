package com.dbclass.hanstagram.ui.adapter

import android.content.Context
import android.os.Build
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.os.bundleOf
import androidx.recyclerview.widget.RecyclerView
import com.afollestad.materialdialogs.LayoutMode
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.bottomsheets.BasicGridItem
import com.afollestad.materialdialogs.bottomsheets.BottomSheet
import com.afollestad.materialdialogs.bottomsheets.gridItems
import com.bumptech.glide.Glide
import com.dbclass.hanstagram.R
import com.dbclass.hanstagram.data.db.dislikes.DislikeEntity
import com.dbclass.hanstagram.data.db.likes.LikeEntity
import com.dbclass.hanstagram.databinding.ItemPostBinding
import com.dbclass.hanstagram.data.db.posts.PostEntity
import com.dbclass.hanstagram.data.repository.CommentRepository
import com.dbclass.hanstagram.data.repository.DislikeRepository
import com.dbclass.hanstagram.data.repository.LikeRepository
import com.dbclass.hanstagram.data.repository.UserRepository
import com.dbclass.hanstagram.data.utils.getImageHeightWithWidthFully
import com.dbclass.hanstagram.data.utils.startPostCommentActivity
import com.dbclass.hanstagram.ui.activity.MainActivity
import com.dbclass.hanstagram.ui.fragment.ProfilePageFragment
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.StringTokenizer

class PostAdapter(private val posts: List<PostEntity>, private val myID: String?, private val rootWidth: Int = 0) :
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
            val commentsCount = CommentRepository.getCommentsCount(post.postID)
            CoroutineScope(Dispatchers.Main).launch {
                postBinding.textNickname.text = nick ?: post.userID
                Glide.with(context).load(profileImage).error(R.drawable.ic_account_96)
                    .into(postBinding.imageProfile)
                postBinding.textCommentsCount.text = commentsCount.toString()
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

        setDislikeListener(holder.binding, post)
        setLikeListener(holder.binding, post)

        postBinding.iconComment.setOnClickListener {
            myID?.let {context.startPostCommentActivity(it, post.postID) }
        }
        postBinding.iconReport.setOnClickListener {
            // TODO 신고 - 매너 온도 하락 ?
        }

        setContentImageSize(holder.binding, post)

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

    private fun setDislikeListener(binding: ItemPostBinding, post: PostEntity) {
        CoroutineScope(Dispatchers.Default).launch {
            var dislikesCount = DislikeRepository.getDislikesCount(post.postID)
            if (myID != null) {
                var dislike = DislikeRepository.getDislike(myID, post.postID)
                if(dislike == null) {
                    CoroutineScope(Dispatchers.Main).launch {
                        Glide.with(context).load(R.drawable.ic_disgusting_100).into(binding.iconDislike)
                    }
                } else {
                    CoroutineScope(Dispatchers.Main).launch {
                        Glide.with(context).load(R.drawable.ic_disgusting_filled_100).into(binding.iconDislike)
                    }
                }
                CoroutineScope(Dispatchers.Main).launch {
                    binding.textDislikesCount.text = dislikesCount.toString()
                    binding.iconDislike.setOnClickListener {
                        if (dislike == null) {
                            CoroutineScope(Dispatchers.Default).launch {
                                dislike = DislikeRepository.doDislike(DislikeEntity(myID, post.postID))
                            }
                            Glide.with(context).load(R.drawable.ic_disgusting_filled_100)
                                .into(binding.iconDislike)
                            binding.textDislikesCount.text = (++dislikesCount).toString()
                        } else {
                            CoroutineScope(Dispatchers.Default).launch {
                                Log.d("ddddddddd", dislike!!.pid.toString() + " ddddd ")
                                DislikeRepository.cancelDislike(dislike!!.pid)
                                dislike = null
                            }
                            Glide.with(context).load(R.drawable.ic_disgusting_100)
                                .into(binding.iconDislike)
                            binding.textDislikesCount.text = (--dislikesCount).toString()
                        }
                    }
                }
            }
        }
    }
    private fun setLikeListener(binding: ItemPostBinding, post: PostEntity) {
        CoroutineScope(Dispatchers.Default).launch {
            var likesCount = LikeRepository.getLikesCount(post.postID)
            if (myID != null) {
                var like = LikeRepository.getLike(myID, post.postID)
                if(like == null) {
                    CoroutineScope(Dispatchers.Main).launch {
                        Glide.with(context).load(R.drawable.ic_heart_100).into(binding.iconHeart)
                    }
                } else {
                    CoroutineScope(Dispatchers.Main).launch {
                        Glide.with(context).load(R.drawable.ic_heart_filled_100).into(binding.iconHeart)
                    }
                }
                CoroutineScope(Dispatchers.Main).launch {
                    binding.textLikesCount.text = likesCount.toString()
                    binding.iconHeart.setOnClickListener {
                        if (like == null) {
                            CoroutineScope(Dispatchers.Default).launch {
                                like = LikeRepository.doLike(LikeEntity(myID, post.postID))
                            }
                            Glide.with(context).load(R.drawable.ic_heart_filled_100)
                                .into(binding.iconHeart)
                            binding.textLikesCount.text = (++likesCount).toString()
                        } else {
                            CoroutineScope(Dispatchers.Default).launch {
                                LikeRepository.cancelLike(like!!.pid)
                                like = null
                            }
                            Glide.with(context).load(R.drawable.ic_heart_100)
                                .into(binding.iconHeart)
                            binding.textLikesCount.text = (--likesCount).toString()
                        }
                    }
                }
            }
        }
    }
    private fun setContentImageSize(binding: ItemPostBinding, post: PostEntity) {
        val contentImages = StringTokenizer(post.images)
        val i1 = contentImages.nextToken()
        val scaleHeight = context.getImageHeightWithWidthFully(i1, rootWidth)
        binding.imageContent.layoutParams.height = scaleHeight
        Glide.with(context).load(i1).into(binding.imageContent)
    }

    override fun getItemCount(): Int = posts.size

    inner class PostViewHolder(val binding: ItemPostBinding) : RecyclerView.ViewHolder(binding.root)

}