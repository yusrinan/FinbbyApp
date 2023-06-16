package com.example.finbbyapp.ui.preferences

import android.content.Context

class SurveyPreference(context: Context, var name: String) {
    private val preferences = context.getSharedPreferences(name, Context.MODE_PRIVATE)

    fun setSurvey(value: SurveyModel) {
        val editor = preferences.edit()
        editor.putBoolean(IS_SURVEY, value.isSurvey)
        editor.apply()
    }


    fun getSurvey(): SurveyModel {
        val model = SurveyModel()
        model.isSurvey = preferences.getBoolean(IS_SURVEY, false)

        return model
    }


    companion object {
        private const val IS_SURVEY = "is_survey"
    }
}