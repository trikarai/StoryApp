package id.trisutrisno.storyapp.ui.story

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import id.trisutrisno.storyapp.databinding.FragmentStoryBinding
import id.trisutrisno.storyapp.ui.MainActivity
import id.trisutrisno.storyapp.ui.auth.LoginActivity
import id.trisutrisno.storyapp.ui.story.adapter.LoadingStateAdapter
import id.trisutrisno.storyapp.ui.story.adapter.StoryAdapter
import id.trisutrisno.storyapp.ui.upload.UploadStoryActivity
import id.trisutrisno.storyapp.utils.SharedViewModel
import id.trisutrisno.storyapp.utils.UserViewModelFactory

class StoryFragment: Fragment() {
    private var _binding: FragmentStoryBinding? = null
    private val binding get() = _binding!!

    private lateinit var storyAdapter: StoryAdapter

    private val storyViewModel: StoryViewModel by viewModels {
        UserViewModelFactory.getInstance(requireContext())
    }

    private val sharedViewModel: SharedViewModel by viewModels  {
        UserViewModelFactory.getInstance(requireContext())
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentStoryBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {

            rvStories.adapter = StoryAdapter()
            rvStories.setHasFixedSize(true)
            rvStories.layoutManager = LinearLayoutManager(requireContext())
        }
        setListener()
        setObserver()
        fetchData()
    }

    private fun setObserver() {
        sharedViewModel.fetchUser()
        sharedViewModel.user.observe(viewLifecycleOwner) { user ->
            if (!user.isLogin) {
                startActivity(Intent(activity, LoginActivity::class.java))
            }
        }
    }

    override fun onResume() {
        super.onResume()
        fetchData()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private fun fetchData() {
        storyAdapter = StoryAdapter()

        val token = sharedViewModel.user.value?.token ?: ""

        storyViewModel.fetchAllStory(token).observe(viewLifecycleOwner) {
            storyAdapter.submitData(lifecycle, it)
        }

        binding.rvStories.adapter = storyAdapter.withLoadStateFooter(
            footer = LoadingStateAdapter {
                storyAdapter.retry()
            }
        )
    }

    private fun setListener() {
        binding.fabCreateStory.setOnClickListener {
            startActivity(Intent(requireContext(), UploadStoryActivity::class.java))
        }
        binding.refreshStory.setOnRefreshListener {
            fetchData()
            storyAdapter.refresh()
            binding.refreshStory.isRefreshing = false
        }
    }
}