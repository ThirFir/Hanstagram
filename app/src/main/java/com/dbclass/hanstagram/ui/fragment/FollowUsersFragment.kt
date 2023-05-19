package com.dbclass.hanstagram.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.dbclass.hanstagram.data.db.users.UserEntity
import com.dbclass.hanstagram.data.repository.follow.FollowRepository
import com.dbclass.hanstagram.data.repository.follow.FollowRepositoryImpl
import com.dbclass.hanstagram.data.viewmodel.UserViewModel
import com.dbclass.hanstagram.databinding.FragmentFollowUsersBinding
import com.dbclass.hanstagram.ui.adapter.FoundUserAdapter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class FollowUsersFragment : Fragment() {

    private lateinit var binding: FragmentFollowUsersBinding
    private val userViewModel: UserViewModel by activityViewModels()
    private val followRepository: FollowRepository = FollowRepositoryImpl
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFollowUsersBinding.inflate(inflater, container, false)

        val userID = arguments?.getString("user_id")

        /** state : "followers" or "followings" */
        val state = arguments?.getString("state")
        binding.recyclerviewFollowUsers.layoutManager = LinearLayoutManager(requireContext())

        CoroutineScope(Dispatchers.Default).launch {
            var followUsers = listOf<UserEntity>()
            if (state == "followers")
                followUsers = followRepository.getFollowers(userID)
            else if(state == "followings")
                followUsers = followRepository.getFollowings(userID)
            CoroutineScope(Dispatchers.Main).launch {
                binding.recyclerviewFollowUsers.adapter =
                    FoundUserAdapter(followUsers as MutableList, true, userViewModel.user.value?.id)
            }
        }

        return binding.root
    }


}