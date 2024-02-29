package ru.netology.neworknetology.model.setting

interface AppSettings {

    fun getCurrentToken(): String?
    fun setCurrentToken(token: String?)
}