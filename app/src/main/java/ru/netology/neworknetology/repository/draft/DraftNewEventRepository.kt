package ru.netology.neworknetology.repository.draft

interface DraftNewEventRepository {
    fun saveDraftContent(content: String)
    fun saveDraftDate(content: String)
    fun saveDraftTime(content: String)
    fun saveDraftType(content: String)
    fun getDraftContent(): String
    fun getDraftDate():String
    fun getDraftTime():String
    fun getDraftType():String
    fun clearDrafts()
}