package com.example.finbbyapp.ui.preferences

import android.content.Context

class UserPreference(context: Context) {
    private val preferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    fun setTopic(value: TopicModel) {
        val editor = preferences.edit()
        editor.putString(TOPIC1, value.topic1)
        editor.putString(TOPIC2, value.topic2)
        editor.putString(TOPIC3, value.topic3)
        editor.apply()
    }

    fun setUser(value: UserModel) {
        val editor = preferences.edit()
        editor.putString(NAME, value.name)
        editor.putString(EMAIL, value.email)
        editor.putInt(ID_USER, value.id as Int)
        editor.putBoolean(IS_SURVEY, value.isSurvey as Boolean)
        editor.apply()
    }

    fun dataAddContent1(): AddContent1Model {
        val model = AddContent1Model()
        model.title = preferences.getString(TITLE, "")
        model.content = preferences.getString(CONTENT, "")
        model.topic = preferences.getString(TOPIC, "")

        return model
    }

    fun setDataAddContent1(value: AddContent1Model) {
        val editor = preferences.edit()
        editor.putString(TITLE, value.title)
        editor.putString(EMAIL, value.content)
        editor.putString(TOPIC, value.topic)
        editor.apply()
    }

    fun deleteDataPreference() {
        val editor = preferences.edit()
        editor.remove(NAME)
        editor.remove(EMAIL)
        editor.remove(ID_USER)
        editor.apply()
    }

    fun getUser(): UserModel {
        val model = UserModel()
        model.name = preferences.getString(NAME, "")
        model.email = preferences.getString(EMAIL, "")
        model.id = preferences.getInt(ID_USER, 0)
        model.isSurvey = preferences.getBoolean(IS_SURVEY, false)

        return model
    }

    fun getTopic(): TopicModel {
        val model = TopicModel()
        model.topic1 = preferences.getString(TOPIC1, "")

        return model
    }

    fun deleteTopic() {
        val editor = preferences.edit()
        editor.remove(TOPIC1)
        editor.apply()
    }

    companion object {
        private const val PREFS_NAME = "user_pref"
        private const val TOPIC1 = "topic1"
        private const val TOPIC2 = "topic2"
        private const val TOPIC3 = "topic3"
        private const val NAME = "name"
        private const val EMAIL = "email"
        private const val ID_USER = "id_user"
        private const val IS_SURVEY = "is_survey"
        private const val TITLE = "title"
        private const val CONTENT = "content"
        private const val TOPIC = "topic"
    }
}