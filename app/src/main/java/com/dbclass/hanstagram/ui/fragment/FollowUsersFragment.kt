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
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class FollowUsersFragment private constructor(): Fragment() {

    private lateinit var binding: FragmentFollowUsersBinding
    private val userViewModel: UserViewModel by activityViewModels()
    private val followRepository: FollowRepository = FollowRepositoryImpl
    private val mainDispatcher: CoroutineDispatcher = Dispatchers.Main
    private val uiScope: CoroutineScope = CoroutineScope(mainDispatcher)

    private var userID: String? = null
    private var state: String? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFollowUsersBinding.inflate(inflater, container, false)

        /** state : "followers" or "followings" */
        state = arguments?.getString("state")
        userID = arguments?.getString("user_id")

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.recyclerviewFollowUsers.layoutManager = LinearLayoutManager(requireContext())

        uiScope.launch {
            var followUsers = listOf<UserEntity>()
            if (state == "followers")
                followUsers = followRepository.getFollowers(userID)
            else if(state == "followings")
                followUsers = followRepository.getFollowings(userID)
            binding.recyclerviewFollowUsers.adapter =
                FoundUserAdapter(followUsers as MutableList, true, userViewModel.user.value?.id)

        }
    }

    companion object {

        fun newInstance(state: String, userID: String): FollowUsersFragment {
            val args = Bundle().apply {
                putString("state", state)
                putString("user_id", userID)
            }

            val fragment = FollowUsersFragment()
            fragment.arguments = args
            return fragment
        }
    }
}