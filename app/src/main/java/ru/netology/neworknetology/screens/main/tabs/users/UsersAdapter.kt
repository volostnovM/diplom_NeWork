package ru.netology.neworknetology.screens.main.tabs.users

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import ru.netology.neworknetology.databinding.UsersCardBinding
import ru.netology.neworknetology.dto.User
import ru.netology.neworknetology.utils.load

class UsersAdapter() : ListAdapter<User, UsersViewHolder>(UserDiffCallback())  {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UsersViewHolder {
        val binding = UsersCardBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return UsersViewHolder(binding)
    }

    override fun onBindViewHolder(holder: UsersViewHolder, position: Int) {
        val user = getItem(position)
        holder.bind(user)
    }
}


class UsersViewHolder(
    private val binding: UsersCardBinding
) : RecyclerView.ViewHolder(binding.root)  {

    fun bind(user: User) {
        with(binding) {
            if(user.avatar != null) {
                avatar.load(user.avatar)
            }

            userName.text = user.login
            description.text = user.name
        }
    }
}


class UserDiffCallback : DiffUtil.ItemCallback<User>() {
    override fun areItemsTheSame(oldItem: User, newItem: User): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: User, newItem: User): Boolean {
        return oldItem == newItem
    }
}
