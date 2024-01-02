package com.imahdev.myappstory.adapter


import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.app.ActivityOptionsCompat
import androidx.core.util.Pair
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.imahdev.myappstory.data.remote.response.ListStoryItem
import com.imahdev.myappstory.databinding.ItemStoryBinding
import com.imahdev.myappstory.view.detail.DetailActivity

class StoryAdapter:
    PagingDataAdapter<ListStoryItem,StoryAdapter.MyViewHolder>(DIFF_CALLBACK) {

    class MyViewHolder(private val binding: ItemStoryBinding) :
        RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SuspiciousIndentation")
        fun bind(user: ListStoryItem) {
            with(binding) {
                tvItemName.text = user.name
                ivItemPhoto.loadImage(
                    url = user.photoUrl
                )

                    itemView.setOnClickListener {
                    val intent = Intent(itemView.context, DetailActivity::class.java)
                        intent.putExtra(DetailActivity.STORY_EXTRA, user)

                        val optionsCompat: ActivityOptionsCompat =
                            ActivityOptionsCompat.makeSceneTransitionAnimation(
                                itemView.context as Activity,
                                Pair(binding.ivItemPhoto, "photo"),
                                Pair(binding.tvItemName, "name"),
                            )
                        it.context.startActivity(intent, optionsCompat.toBundle())
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = ItemStoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val listData = getItem(position)
        if (listData != null) {
            holder.bind(listData)
        }
    }
    companion object {
        val DIFF_CALLBACK = object :DiffUtil.ItemCallback<ListStoryItem>() {
            override fun areItemsTheSame(oldItem: ListStoryItem, newItem: ListStoryItem): Boolean {
                return oldItem == newItem
            }
            override fun areContentsTheSame(
                oldItem: ListStoryItem,
                newItem: ListStoryItem
            ): Boolean {
                return oldItem.id == newItem.id
            }
        }
    }
}

fun ImageView.loadImage(url: String) {
    Glide.with(this.context)
        .load(url)
        .centerCrop()
        .into(this)
}


