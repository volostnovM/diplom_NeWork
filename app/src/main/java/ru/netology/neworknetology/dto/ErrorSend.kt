package ru.netology.neworknetology.dto

data class ErrorSend(
    var timestamp: String,
    var status: Int,
    var error: String,
    var path: String
)