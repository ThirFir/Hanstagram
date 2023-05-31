package com.dbclass.hanstagram.ui.adapter

import android.app.Activity
import android.app.Activity.RESULT_OK
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.annotation.RequiresApi
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
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
import com.dbclass.hanstagram.data.repository.post.PostRepository
import com.dbclass.hanstagram.data.repository.post.PostRepositoryImpl
import com.dbclass.hanstagram.data.repository.user.UserRepository
import com.dbclass.hanstagram.data.repository.user.UserRepositoryImpl
import com.dbclass.hanstagram.data.utils.IntegerConstants.DELETE
import com.dbclass.hanstagram.data.utils.IntegerConstants.EDIT
import com.dbclass.hanstagram.data.utils.IntegerConstants.REPORT
import com.dbclass.hanstagram.data.utils.closeKeyboard
import com.dbclass.hanstagram.data.utils.getImageHeightWithWidthFully
import com.dbclass.hanstagram.data.utils.getImageList
import com.dbclass.hanstagram.data.utils.startPostCommentActivity
import com.dbclass.hanstagram.ui.activity.MainActivity
import com.dbclass.hanstagram.ui.activity.PostAddActivity
import com.dbclass.hanstagram.ui.dialog.PostMenuDialogFragment
import com.dbclass.hanstagram.ui.fragment.ProfilePageFragment
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*

class PostAdapter(
    private val postsWithUsers: MutableList<Pair<PostEntity, UserEntity>>,
    private val myID: String?,
    private val context: Context,
    private val rootWidth: Int = 0,
    private val postEditActivityResult: ActivityResultLauncher<Intent>
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val postRepository: PostRepository = PostRepositoryImpl
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
            PostMenuDialogFragment.newInstance(post.postID, postOwner.id, myID!!, position)
                .apply {
                    setOnItemClickListener { selectedItem ->
                        when (selectedItem) {
                            EDIT -> {
                                dismiss()
                                val postEditActivity = PostAddActivity().apply {
                                    setOnEditCompleteListener { _, images, content ->
                                        updatePost(position, post.postID, images, content)
                                        Toast.makeText(
                                            this@PostAdapter.context,
                                            "게시물 수정이 완료되었습니다.",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }
                                }

                                postEditActivityResult.launch(
                                    Intent(context, postEditActivity::class.java).apply {
                                        putExtra("user_id", postOwner.id)
                                        putExtra("images", post.images)
                                    }
                                )
                            }

                            DELETE -> {
                                uiScope.launch {
                                    parentFragmentManager.fragments.forEach { fragment ->
                                        if (fragment is DialogFragment) {
                                            fragment.dismiss()
                                        }
                                    }
                                    postRepository.deletePost(post.postID)
                                    postsWithUsers.removeAt(position)
                                    notifyItemRemoved(position)
                                    notifyItemRangeChanged(position, itemCount)
                                }
                            }

                            REPORT -> {
                                uiScope.launch {
                                    dismiss()
                                    userRepository.updateTemperature(
                                        postOwner.id,
                                        postOwner.temperature - 5f
                                    )
                                    Toast.makeText(this@PostAdapter.context, "신고가 접수되었습니다", Toast.LENGTH_SHORT)
                                        .show()
                                }
                            }
                        }
                    }
                }.show((context as MainActivity).supportFragmentManager, PostMenuDialogFragment.TAG)

        }

        setDislikeListener(holder.binding, post, postOwner)
        setLikeListener(holder.binding, post, postOwner)

        postBinding.iconComment.setOnClickListener {
            myID?.let { this.context.startPostCommentActivity(it, post.postID) }
        }

        setContentImageViewPager(holder.binding, post.images)

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
                            if (postOwner.id != myID)   // Not self dislike
                                userRepository.updateTemperature(
                                    postOwner.id,
                                    postOwner.temperature - 1f
                                )
                            Glide.with(this@PostAdapter.context)
                                .load(R.drawable.ic_disgusting_filled_100)
                                .into(binding.iconDislike)
                            binding.textDislikesCount.text = (++dislikesCount).toString()
                        } else {
                            dislikeRepository.cancelDislike(dislike!!.pid)
                            if (postOwner.id != myID)
                                userRepository.updateTemperature(
                                    postOwner.id,
                                    postOwner.temperature + 1f
                                )
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
                            if (postOwner.id != myID)    // Not self like
                                userRepository.updateTemperature(
                                    postOwner.id,
                                    postOwner.temperature + 1f
                                )

                            Glide.with(this@PostAdapter.context)
                                .load(R.drawable.ic_heart_filled_100)
                                .into(binding.iconHeart)
                            binding.textLikesCount.text = (++likesCount).toString()
                        } else {
                            likeRepository.cancelLike(like!!.pid)
                            if (postOwner.id != myID)
                                userRepository.updateTemperature(
                                    postOwner.id,
                                    postOwner.temperature - 1f
                                )
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

    private fun setContentImageViewPager(binding: ItemPostBinding, images: String) {
        val imageList = getImageList(images)
        binding.imageContentViewpager.adapter =
            ImagePagerAdapter(this.context as FragmentActivity).apply {
                setImages(
                    imageList,
                    this@PostAdapter.context.getImageHeightWithWidthFully(imageList[0], rootWidth)
                )
            }

        if (imageList.size != 1) {
            binding.textImagePosition.text = "1/${imageList.size}"
            binding.imageContentViewpager.registerOnPageChangeCallback(object :
                ViewPager2.OnPageChangeCallback() {

                override fun onPageSelected(position: Int) {
                    super.onPageSelected(position)
                    Log.d(this@PostAdapter.javaClass.simpleName, position.toString())
                    binding.textImagePosition.text = "${position + 1}/${imageList.size}"
                }
            }
            )
        }
    }

    private fun updatePost(position: Int, postID: Long, images: String, content: String) {
        uiScope.launch {
            postRepository.updatePost(
                postID = postID,
                images = images,
                content = content
            )
            val p = postsWithUsers[position].first
            p.images = images
            p.content = content
            val u = postsWithUsers[position].second
            postsWithUsers[position] = Pair(p, u)
            notifyItemChanged(position)
        }
    }

    override fun getItemCount(): Int = postsWithUsers.size

    inner class PostViewHolder(val binding: ItemPostBinding) : RecyclerView.ViewHolder(binding.root)

}