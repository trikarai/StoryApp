package id.trisutrisno.storyapp.ui.story

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import id.trisutrisno.storyapp.data.local.room.StoryModel
import id.trisutrisno.storyapp.databinding.ActivityDetailBinding
import id.trisutrisno.storyapp.utils.Utils.formatDate

class DetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setDetail()
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    private fun setDetail() {
        val user = intent.getParcelableExtra<StoryModel>(EXTRA_USER)
        if (user != null) {
            binding.apply {
                Glide.with(this@DetailActivity).load(user.photoUrl).into(imgStories)
                tvName.text = user.name
                tvDesc.text = user.description
                tvCreateDate.text = user.createdAt.formatDate()

                supportActionBar!!.title = user.name
            }
        }
    }

    companion object {
        const val EXTRA_USER = "extra_user"
    }

}