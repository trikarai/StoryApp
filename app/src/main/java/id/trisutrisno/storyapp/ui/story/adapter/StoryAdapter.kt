package id.trisutrisno.storyapp.ui.story.adapter

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.app.ActivityOptionsCompat
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import id.trisutrisno.storyapp.data.local.entity.StoryEntity
import id.trisutrisno.storyapp.databinding.ItemStoryBinding
import id.trisutrisno.storyapp.domain.model.Story
import id.trisutrisno.storyapp.domain.model.toStoryEntity
import id.trisutrisno.storyapp.ui.story.DetailActivity
import id.trisutrisno.storyapp.utils.Utils.formatDate

class StoryAdapter : PagingDataAdapter<Story, StoryAdapter.MyViewHolder>(DiffCallback){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = ItemStoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }


    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val story = getItem(position)
        if (story != null){
            holder.bind(story)
        }
    }


    inner class MyViewHolder(private val binding: ItemStoryBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(data: Story) {
            binding.apply {
                Glide.with(itemView).load(data.photoUrl).into(imgStories)
                tvName.text = data.name
                tvDesc.text = data.description
                tvCreateDate.text =   data.createdAt.formatDate()

                imgStories.setOnClickListener {
                    val intent = Intent(it.context, DetailActivity::class.java)
                    intent.putExtra(DetailActivity.EXTRA_USER, data.toStoryEntity())

                    val optionsCompat: ActivityOptionsCompat =
                        ActivityOptionsCompat.makeSceneTransitionAnimation(
                            it.context as Activity
                        )

                    it.context.startActivity(intent, optionsCompat.toBundle())
                }
            }
        }
    }

    companion object DiffCallback : DiffUtil.ItemCallback<Story>() {
        override fun areItemsTheSame(oldItem: Story, newItem: Story): Boolean {
            return oldItem == newItem
        }
        override fun areContentsTheSame(oldItem: Story, newItem: Story): Boolean {
            return oldItem.id == newItem.id
        }
    }
}

