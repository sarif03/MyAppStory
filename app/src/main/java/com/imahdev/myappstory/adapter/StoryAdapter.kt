package com.imahdev.myappstory.adapter


import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.app.ActivityOptionsCompat
import androidx.core.util.Pair
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.imahdev.myappstory.data.remote.response.ListStoryItem
import com.imahdev.myappstory.databinding.ItemStoryBinding
import com.imahdev.myappstory.view.detail.DetailActivity

class StoryAdapter(private var listStory: List<ListStoryItem>): RecyclerView.Adapter<StoryAdapter.ViewHolder>() {

    class ViewHolder(private val binding: ItemStoryBinding) :
        RecyclerView.ViewHolder(binding.root) {
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

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemStoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int = listStory.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(listStory[position])
    }
}

fun ImageView.loadImage(url: String) {
    Glide.with(this.context)
        .load(url)
        .centerCrop()
        .into(this)
}


