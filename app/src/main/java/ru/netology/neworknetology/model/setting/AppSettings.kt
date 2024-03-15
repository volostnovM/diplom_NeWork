package ru.netology.neworknetology.model.setting

interface AppSettings {

    fun getCurrentToken(): String?
    fun setCurrentToken(token: String?)

    fun getCurrentIdForUser(): Int
    fun setCurrentIdForUser(id: Int)

    fun clearAllSetting()


}