package id.trisutrisno.storyapp.ui.story

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import id.trisutrisno.storyapp.R
import id.trisutrisno.storyapp.databinding.ActivityStoryBinding
import id.trisutrisno.storyapp.ui.auth.LoginActivity
import id.trisutrisno.storyapp.ui.story.adapter.StoryAdapter
import id.trisutrisno.storyapp.ui.upload.UploadStoryActivity
import id.trisutrisno.storyapp.utils.UserViewModelFactory
import android.provider.Settings

class StoryActivity: AppCompatActivity() {
    private lateinit var binding: ActivityStoryBinding
    private lateinit var storyAdapter: StoryAdapter

    private val storyViewModel: StoryViewModel by viewModels {
        UserViewModelFactory.getInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.rvStories.layoutManager = LinearLayoutManager(this)

        fetchData()
        setListener()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId){
            R.id.action_sign_out -> {
                storyViewModel.deleteUser()
                startActivity(Intent(this, LoginActivity::class.java))
                finish()
                true
            }
            R.id.menu_language -> {
                startActivity(Intent(Settings.ACTION_LOCALE_SETTINGS))
                true
            }
            else -> {
                return super.onOptionsItemSelected(item)
            }
        }
    }

    private fun setListener() {
        binding.fabCreateStory.setOnClickListener {
            startActivity(Intent(this, UploadStoryActivity::class.java))
        }
        binding.refreshStory.setOnRefreshListener {
            fetchData()
            storyAdapter.refresh()
            binding.refreshStory.isRefreshing = false
        }
    }

    private fun fetchData() {
        val token = intent.getStringExtra(EXTRA_TOKEN).toString()
        storyAdapter = StoryAdapter()

        storyViewModel.fetchAllStory(token).observe(this) {
            storyAdapter.submitData(lifecycle, it)
        }
        binding.rvStories.adapter = storyAdapter
    }

    companion object {
        const val EXTRA_TOKEN = "extra_token"
    }
}