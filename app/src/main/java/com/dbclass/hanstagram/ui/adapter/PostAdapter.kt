package com.dbclass.hanstagram.ui.adapter

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Build
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.app.ActivityCompat.requestPermissions
import androidx.core.app.ActivityCompat.startActivity
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
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
import com.dbclass.hanstagram.ui.activity.MainActivity
import com.dbclass.hanstagram.ui.activity.PostCommentActivity
import com.dbclass.hanstagram.ui.fragment.PostCommentBottomSheet
import com.dbclass.hanstagram.ui.fragment.ProfilePageFragment
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.dialog.MaterialDialogs
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.StringTokenizer

class PostAdapter(private val posts: List<PostEntity>, private val myID: String?, val context: Context) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder =
        PostViewHolder(ItemPostBinding.inflate(LayoutInflater.from(parent.context), parent, false))

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
        postBinding.imagePostMenu.setOnClickListener {
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
        postBinding.imageHeart.setOnClickListener {
            Glide.with(context).load(R.drawable.ic_heart_filled_100).into(postBinding.imageHeart)
        }
        postBinding.imageDislike.setOnClickListener {
            Glide.with(context).load(R.drawable.ic_disgusting_filled_100)
                .into(postBinding.imageDislike)
        }

        postBinding.imageComment.setOnClickListener {
            /*
            val commentBottomSheet = PostCommentBottomSheet().apply {
                arguments = bundleOf("post_id" to post.postID)
            }
            commentBottomSheet.show((context as MainActivity).supportFragmentManager, commentBottomSheet.tag)*/
            val intent = Intent(context, PostCommentActivity::class.java).apply {
                putExtra("user_id", myID)
                putExtra("post_id", post.postID)
            }
            context.startActivity(intent)
        }
        postBinding.imageReport.setOnClickListener {
            // TODO 신고 - 매너 온도 하락 ?
        }

        val contentImages = StringTokenizer(post.images)
        val i1 = contentImages.nextToken()
        val imageOptions = BitmapFactory.Options().apply {
            inJustDecodeBounds = true
            BitmapFactory.decodeStream(
                context.contentResolver.openInputStream(i1.toUri()),
                null,
                this
            )
        }


        val imageWidth = imageOptions.outWidth
        val imageHeight = imageOptions.outHeight
        val displayMetrics = context.resources.displayMetrics
        val screenWidth = displayMetrics.widthPixels
        val scaleFactor = screenWidth.toFloat() / imageWidth
        val scaleHeight = (imageHeight * scaleFactor).toInt()
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