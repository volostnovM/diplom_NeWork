package ru.netology.neworknetology.model

import ru.netology.neworknetology.dto.Event

interface OnInteractionListener {
    fun onEventLike(event: Event)
    fun onEventRemove(event: Event)
    fun onEventEdit(event: Event)
}