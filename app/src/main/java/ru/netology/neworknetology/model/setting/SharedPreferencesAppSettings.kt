package ru.netology.neworknetology.model.setting

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SharedPreferencesAppSettings @Inject constructor(
    @ApplicationContext appContext: Context
) : AppSettings {

    private val sharedPreferences = appContext.getSharedPreferences("settings", Context.MODE_PRIVATE)

    override fun getCurrentToken(): String? {
        return sharedPreferences.getString(PREF_CURRENT_ACCOUNT_TOKEN, null)
    }

    override fun setCurrentToken(token: String?) {
        val editor = sharedPreferences.edit()
        if (token == null)
            editor.remove(PREF_CURRENT_ACCOUNT_TOKEN)
        else
            editor.putString(PREF_CURRENT_ACCOUNT_TOKEN, token)
        editor.apply()
    }

    override fun getCurrentIdForUser(): Int {
        return sharedPreferences.getInt(PREF_CURRENT_USER_ID, 0)
    }

    override fun setCurrentIdForUser(id: Int) {
        val editor = sharedPreferences.edit()
        if (id == 0)
            editor.remove(PREF_CURRENT_USER_ID)
        else
            editor.putInt(PREF_CURRENT_USER_ID, id)
        editor.apply()
    }

    override fun clearAllSetting() {
        val editor = sharedPreferences.edit()
        editor.remove(PREF_CURRENT_USER_ID)
        editor.remove(PREF_CURRENT_ACCOUNT_TOKEN)
        editor.apply()
    }

    companion object {
        private const val PREF_CURRENT_ACCOUNT_TOKEN = "currentToken"
        private const val PREF_CURRENT_USER_ID = "userId"
    }
}