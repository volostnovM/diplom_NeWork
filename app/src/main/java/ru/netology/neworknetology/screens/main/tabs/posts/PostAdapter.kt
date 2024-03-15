package ru.netology.neworknetology.screens.main.tabs.posts

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.PopupMenu
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import ru.netology.neworknetology.R
import ru.netology.neworknetology.databinding.PostCardBinding
import ru.netology.neworknetology.dto.Post
import ru.netology.neworknetology.model.OnInteractionListenerForPost
import ru.netology.neworknetology.model.enums.AttachmentType
import ru.netology.neworknetology.utils.Formatter
import ru.netology.neworknetology.utils.load

class PostAdapter(
    private val onInteractionListener: OnInteractionListenerForPost
) : ListAdapter<Post, PostViewHolder>(PostDiffCallback()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        val binding = PostCardBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PostViewHolder(binding, onInteractionListener)
    }

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        val post = getItem(position)
        holder.bind(post)
    }
}

class PostViewHolder(
    private val binding: PostCardBinding,
    private val onInteractionListener: OnInteractionListenerForPost
) : RecyclerView.ViewHolder(binding.root)  {

    fun bind(post: Post) {
        with(binding) {
            if(post.authorAvatar != null) {
                avatar.load(post.authorAvatar)
            }

            authorName.text = post.author
            published.text = Formatter.formatPostDate(post.published)
            content.text = post.content

            likeButton.isChecked = post.likedByMe
            likeButton.text = Formatter.formatCount(post.likeOwnerIds.size)

            if (post.attachment?.type == AttachmentType.IMAGE) {
                attachment.visibility = View.VISIBLE
                attachment.load(post.attachment.url)
            } else {
                attachment.visibility = View.GONE
            }

            likeButton.setOnClickListener {
                onInteractionListener.onPostLike(post)
            }

            attachment.setOnClickListener {
                onInteractionListener.onImageClickEvent(post)
            }

            avatar.setOnClickListener {
                onInteractionListener.onAuthorClickEvent(post)
            }

            shareButton.setOnClickListener {
                onInteractionListener.onPostShare(post)
            }

            menu.isVisible = post.ownedByMe

            menu.setOnClickListener {
                PopupMenu(it.context, it).apply {
                    inflate(R.menu.options_event)
                    setOnMenuItemClickListener { item ->
                        when (item.itemId) {
                            R.id.remove -> {
                                onInteractionListener.onPostRemove(post)
                                true
                            }

                            R.id.edit -> {
                                onInteractionListener.onPostEdit(post)
                                true
                            }

                            else -> false
                        }
                    }
                }.show()
            }
        }
    }
}

class PostDiffCallback : DiffUtil.ItemCallback<Post>() {
    override fun areItemsTheSame(oldItem: Post, newItem: Post): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Post, newItem: Post): Boolean {
        return oldItem == newItem
    }
}