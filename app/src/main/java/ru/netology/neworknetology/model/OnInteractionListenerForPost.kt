package ru.netology.neworknetology.model

import ru.netology.neworknetology.dto.Post

interface OnInteractionListenerForPost {
    fun onPostLike(post: Post)
    fun onPostRemove(post: Post)
    fun onPostEdit(post: Post)
    fun onPostShare(post: Post)
    fun onImageClickEvent(post: Post)
    fun onAuthorClickEvent(post: Post)
}