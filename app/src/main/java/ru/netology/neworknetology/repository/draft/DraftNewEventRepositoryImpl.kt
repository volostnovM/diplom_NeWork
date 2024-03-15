package ru.netology.neworknetology.repository.draft

import android.content.Context
import androidx.lifecycle.MutableLiveData
import com.squareup.moshi.Moshi
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class DraftNewEventRepositoryImpl @Inject constructor(
    @ApplicationContext
    context: Context,
    moshi: Moshi
) : DraftNewEventRepository {

    private val prefs = context.getSharedPreferences("draftEvent", Context.MODE_PRIVATE)
    private val type = String::class.java
    private val adapter = moshi.adapter(type)

    private val keyContent = "draftContent"
    private val keyDate = "draftDate"
    private val keyTime = "draftTime"
    private val keyType = "draftType"

    private var draftContent = ""
    private var draftDate = ""
    private var draftTime = ""
    private var draftType = ""

    private val dataContent = MutableLiveData(draftContent)
    private val dataDate = MutableLiveData(draftDate)
    private val dataTime = MutableLiveData(draftTime)
    private val dataFormat = MutableLiveData(draftType)

    init {
        prefs.getString(keyContent, null)?.let {
            draftContent = adapter.fromJson(it).toString()
            dataContent.value = draftContent
        }
        prefs.getString(keyDate, null)?.let {
            draftDate = adapter.fromJson(it).toString()
            dataDate.value = draftDate
        }
        prefs.getString(keyTime, null)?.let {
            draftTime = adapter.fromJson(it).toString()
            dataTime.value = draftTime
        }
        prefs.getString(keyType, null)?.let {
            draftType = adapter.fromJson(it).toString()
            dataFormat.value = draftType
        }
    }

    override fun getDraftContent(): String {
        return draftContent
    }


    override fun getDraftDate(): String {
        return draftDate
    }

    override fun getDraftTime(): String {
        return draftTime
    }


    override fun getDraftType(): String {
        return draftType
    }

    override fun saveDraftContent(content: String) {
        draftContent = content
        dataContent.value = draftContent
        sync()
    }

    override fun saveDraftDate(content: String) {
        draftDate = content
        dataDate.value = draftDate
        sync()
    }

    override fun saveDraftTime(content: String) {
        draftTime = content
        dataTime.value = draftTime
        sync()
    }

    override fun saveDraftType(content: String) {
        draftType = content
        dataFormat.value = draftType
        sync()
    }

    override fun clearDrafts() {
        draftContent = ""
        draftDate = ""
        draftTime = ""
        draftType = ""
    }

    private fun sync() {
        with(prefs.edit()) {
            putString(keyContent, adapter.toJson(draftContent))
            putString(keyDate, adapter.toJson(draftDate))
            putString(keyTime, adapter.toJson(draftTime))
            putString(keyType, adapter.toJson(draftType))
            apply()
        }
    }
}