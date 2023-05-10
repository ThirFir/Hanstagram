package com.dbclass.hanstagram.ui.adapter

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat.requestPermissions
import androidx.core.app.ActivityCompat.shouldShowRequestPermissionRationale
import androidx.core.content.ContextCompat.checkSelfPermission
import androidx.core.net.toUri
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dbclass.hanstagram.R
import com.dbclass.hanstagram.data.db.guests.GuestCommentEntity
import com.dbclass.hanstagram.data.repository.GuestCommentRepository
import com.dbclass.hanstagram.data.repository.UserRepository
import com.dbclass.hanstagram.data.utils.getFormattedDate
import com.dbclass.hanstagram.databinding.ItemGuestCommentBinding
import com.dbclass.hanstagram.ui.activity.MainActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class GuestAdapter(private val guestComments: MutableList<GuestCommentEntity>, private val guestID: String?) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private lateinit var context: Context
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        context = parent.context
        return GuestViewHolder(ItemGuestCommentBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }
    override fun getItemCount(): Int = guestComments.size

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        with((holder as GuestViewHolder).binding) {
            val comment = guestComments[position]
            textGuestId.text = comment.guestUserID
            textGuestComment.text = comment.comment
            textEditDate.text = getFormattedDate(comment.createdTime)
            textButtonDelete.isVisible = false
            if (guestID == comment.guestUserID || guestID == comment.ownerUserID)
                textButtonDelete.isVisible = true
            textButtonDelete.setOnClickListener {
                deleteComment(position)
            }
            CoroutineScope(Dispatchers.Default).launch {
                val image = UserRepository.getProfileImage(comment.guestUserID)
                CoroutineScope(Dispatchers.Main).launch {
                    image?.let {
                        Glide.with(context).load(image).error(R.drawable.ic_account_96).into(imageGuestProfile)
                    }
                }
            }
        }
    }

    fun addComment(guestComment: GuestCommentEntity) {
        GuestCommentRepository.addComment(guestComment)
        guestComments.add(0, guestComment)
        notifyItemInserted(0)
    }

    fun deleteComment(position: Int) {
        GuestCommentRepository.deleteComment(guestComments[position])
        guestComments.removeAt(position)
        notifyItemRemoved(position)
        notifyItemRangeChanged(position, itemCount)
    }
    inner class GuestViewHolder(val binding: ItemGuestCommentBinding): RecyclerView.ViewHolder(binding.root)

}