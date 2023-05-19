package com.dbclass.hanstagram.ui.adapter

import android.content.Context
import android.os.Build
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.FragmentActivity
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
import com.dbclass.hanstagram.data.db.users.UserEntity
import com.dbclass.hanstagram.data.repository.comment.CommentRepository
import com.dbclass.hanstagram.data.repository.comment.CommentRepositoryImpl
import com.dbclass.hanstagram.data.repository.dislike.DislikeRepository
import com.dbclass.hanstagram.data.repository.dislike.DislikeRepositoryImpl
import com.dbclass.hanstagram.data.repository.like.LikeRepository
import com.dbclass.hanstagram.data.repository.like.LikeRepositoryImpl
import com.dbclass.hanstagram.data.repository.user.UserRepository
import com.dbclass.hanstagram.data.repository.user.UserRepositoryImpl
import com.dbclass.hanstagram.data.utils.getImageHeightWithWidthFully
import com.dbclass.hanstagram.data.utils.getImageList
import com.dbclass.hanstagram.data.utils.startPostCommentActivity
import com.dbclass.hanstagram.ui.activity.MainActivity
import com.dbclass.hanstagram.ui.fragment.ProfilePageFragment
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*

class PostAdapter(
    private val postsWithUsers: List<Pair<PostEntity, UserEntity>>,
    private val myID: String?,
    private val context: Context,
    private val rootWidth: Int = 0
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val commentRepository: CommentRepository = CommentRepositoryImpl
    private val dislikeRepository: DislikeRepository = DislikeRepositoryImpl
    private val likeRepository: LikeRepository = LikeRepositoryImpl
    private val userRepository: UserRepository = UserRepositoryImpl

    private val mainDispatcher: CoroutineDispatcher = Dispatchers.Main
    private val uiScope: CoroutineScope = CoroutineScope(mainDispatcher)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        return PostViewHolder(
            ItemPostBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val postBinding = (holder as PostViewHolder).binding
        val post = postsWithUsers[position].first
        val postOwner = postsWithUsers[position].second

        uiScope.launch {
            val commentsCount = commentRepository.getCommentsCount(post.postID)

            postBinding.textNickname.text = postOwner.nickname
            Glide.with(this@PostAdapter.context).load(postOwner.profileImage)
                .error(R.drawable.ic_account_96)
                .into(postBinding.imageProfile)
            postBinding.textCommentsCount.text = commentsCount.toString()
        }

        postBinding.textContent.text = post.content
        postBinding.iconPostMenu.setOnClickListener {
            MaterialDialog(this.context, BottomSheet(LayoutMode.WRAP_CONTENT)).show {
                gridItems(
                    items = listOf(
                        BasicGridItem(R.drawable.ic_siren_48, "신고"),
                        BasicGridItem(R.drawable.baseline_edit_32, "수정"),
                        BasicGridItem(R.drawable.baseline_delete_32, "삭제")
                    )
                ) { _, index, item ->
                    Toast.makeText(context, "dddddd", Toast.LENGTH_SHORT).show()

                }
            }
        }

        setDislikeListener(holder.binding, post, postOwner)
        setLikeListener(holder.binding, post, postOwner)

        postBinding.iconComment.setOnClickListener {
            myID?.let { this.context.startPostCommentActivity(it, post.postID) }
        }
        postBinding.iconReport.setOnClickListener {
            // TODO 신고 - 매너 온도 하락 ?
        }

        setContentImageViewPager(holder.binding, post)

        postBinding.textNickname.setOnClickListener {
            val profilePageFragment = ProfilePageFragment.newInstance(postOwner.id)
            (this.context as MainActivity).supportFragmentManager.beginTransaction().replace(
                R.id.fragment_content,
                profilePageFragment
            ).commit()
        }
    }

    private fun setDislikeListener(
        binding: ItemPostBinding,
        post: PostEntity,
        postOwner: UserEntity
    ) {
        uiScope.launch {
            var dislikesCount = dislikeRepository.getDislikesCount(post.postID)
            if (myID != null) {
                var dislike = dislikeRepository.getDislike(myID, post.postID)
                if (dislike == null) {
                    Glide.with(this@PostAdapter.context).load(R.drawable.ic_disgusting_100)
                        .into(binding.iconDislike)
                } else {
                    Glide.with(this@PostAdapter.context).load(R.drawable.ic_disgusting_filled_100)
                        .into(binding.iconDislike)
                }
                binding.textDislikesCount.text = dislikesCount.toString()

                binding.iconDislike.setOnClickListener {
                    uiScope.launch {
                        if (dislike == null) {
                            dislike = dislikeRepository.doDislike(DislikeEntity(myID, post.postID))
                            userRepository.updateTemperature(postOwner.id, postOwner.temperature - 1f)
                            Glide.with(this@PostAdapter.context)
                                .load(R.drawable.ic_disgusting_filled_100)
                                .into(binding.iconDislike)
                            binding.textDislikesCount.text = (++dislikesCount).toString()
                        } else {
                            dislikeRepository.cancelDislike(dislike!!.pid)
                            userRepository.updateTemperature(postOwner.id, postOwner.temperature + 1f)
                            dislike = null

                            Glide.with(this@PostAdapter.context).load(R.drawable.ic_disgusting_100)
                                .into(binding.iconDislike)
                            binding.textDislikesCount.text = (--dislikesCount).toString()
                        }
                    }
                }

            }
        }
    }

    private fun setLikeListener(binding: ItemPostBinding, post: PostEntity, postOwner: UserEntity) {
        uiScope.launch {
            var likesCount = likeRepository.getLikesCount(post.postID)
            if (myID != null) {
                var like = likeRepository.getLike(myID, post.postID)
                if (like == null) {
                    Glide.with(this@PostAdapter.context).load(R.drawable.ic_heart_100)
                        .into(binding.iconHeart)
                } else {
                    Glide.with(this@PostAdapter.context).load(R.drawable.ic_heart_filled_100)
                        .into(binding.iconHeart)
                }
                binding.textLikesCount.text = likesCount.toString()

                binding.iconHeart.setOnClickListener {
                    uiScope.launch {
                        if (like == null) {
                            like = likeRepository.doLike(LikeEntity(myID, post.postID))
                            userRepository.updateTemperature(postOwner.id, postOwner.temperature + 1f)

                            Glide.with(this@PostAdapter.context)
                                .load(R.drawable.ic_heart_filled_100)
                                .into(binding.iconHeart)
                            binding.textLikesCount.text = (++likesCount).toString()
                        } else {
                            likeRepository.cancelLike(like!!.pid)
                            userRepository.updateTemperature(postOwner.id, postOwner.temperature - 1f)
                            like = null

                            Glide.with(this@PostAdapter.context).load(R.drawable.ic_heart_100)
                                .into(binding.iconHeart)
                            binding.textLikesCount.text = (--likesCount).toString()
                        }
                    }
                }

            }
        }
    }

    private fun setContentImageViewPager(binding: ItemPostBinding, post: PostEntity) {
        val imageList = getImageList(post.images)
        binding.imageContentViewpager.adapter =
            ImagePagerAdapter(this.context as FragmentActivity).apply {
                setImages(
                    imageList,
                    this@PostAdapter.context.getImageHeightWithWidthFully(imageList[0], rootWidth)
                )
            }
    }

    override fun getItemCount(): Int = postsWithUsers.size

    inner class PostViewHolder(val binding: ItemPostBinding) : RecyclerView.ViewHolder(binding.root)

}