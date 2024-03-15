package ru.netology.neworknetology.screens.main.tabs.events

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.PopupMenu
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import ru.netology.neworknetology.R
import ru.netology.neworknetology.databinding.EventCardBinding
import ru.netology.neworknetology.dto.Event
import ru.netology.neworknetology.model.OnInteractionListener
import ru.netology.neworknetology.model.enums.AttachmentType
import ru.netology.neworknetology.model.enums.EventType
import ru.netology.neworknetology.utils.Formatter
import ru.netology.neworknetology.utils.load


class EventAdapter(
    private val onInteractionListener: OnInteractionListener
) : ListAdapter<Event, EventViewHolder>(EventDiffCallback()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventViewHolder {
        val binding = EventCardBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return EventViewHolder(binding, onInteractionListener)
    }

    override fun onBindViewHolder(holder: EventViewHolder, position: Int) {
        val event = getItem(position)
        holder.bind(event)
    }
}

class EventViewHolder(
    private val binding: EventCardBinding,
    private val onInteractionListener: OnInteractionListener
) : RecyclerView.ViewHolder(binding.root)  {

    fun bind(event: Event) {
        with(binding) {
            if(event.authorAvatar != null) {
                avatar.load(event.authorAvatar)
            }

            authorName.text = event.author
            published.text = Formatter.formatPostDate(event.published)
            content.text = event.content

            likeButton.isChecked = event.likedByMe
            likeButton.text = Formatter.formatCount(event.likeOwnerIds.size)

            subscribeButton.isChecked = event.participatedByMe
            subscribeButton.text = Formatter.formatCount(event.participantsIds.size)

            dateOfEvent.text = Formatter.formatEventDate(event.datetime)

            when (event.type) {
                EventType.ONLINE -> {
                    status.text = status.context.getString(R.string.online)
                }

                EventType.OFFLINE -> {
                    status.text = status.context.getString(R.string.offline)
                }
            }

            if (event.attachment?.type == AttachmentType.IMAGE) {
                imageAttachment.visibility = View.VISIBLE
                imageAttachment.load(event.attachment.url)
            } else {
                imageAttachment.visibility = View.GONE
            }

            if (event.attachment?.type == AttachmentType.AUDIO) {
                audioAttachment.visibility = View.VISIBLE
            } else {
                audioAttachment.visibility = View.GONE
            }

            likeButton.setOnClickListener {
                onInteractionListener.onEventLike(event)
            }

            imageAttachment.setOnClickListener {
                onInteractionListener.onImageClickEvent(event)
            }

            shareButton.setOnClickListener {
                onInteractionListener.onEventShare(event)
            }

            subscribeButton.setOnClickListener {
                onInteractionListener.onEventTakePart(event)
            }

            menu.isVisible = event.ownedByMe

            menu.setOnClickListener {
                PopupMenu(it.context, it).apply {
                    inflate(R.menu.options_event)
                    setOnMenuItemClickListener { item ->
                        when (item.itemId) {
                            R.id.remove -> {
                                onInteractionListener.onEventRemove(event)
                                true
                            }

                            R.id.edit -> {
                                onInteractionListener.onEventEdit(event)
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

class EventDiffCallback : DiffUtil.ItemCallback<Event>() {
    override fun areItemsTheSame(oldItem: Event, newItem: Event): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Event, newItem: Event): Boolean {
        return oldItem == newItem
    }
}