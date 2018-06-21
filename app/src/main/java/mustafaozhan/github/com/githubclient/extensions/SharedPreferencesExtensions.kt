package mustafaozhan.github.com.githubclient.extensions

import android.content.Context
import android.preference.PreferenceManager

/**
 * Created by Mustafa Ozhan on 1/30/18 at 12:42 AM on Arch Linux wit Love <3.
 */
fun getStringPreferences(context: Context, key: String, defValue: String): String {
    val pref = PreferenceManager.getDefaultSharedPreferences(context)
    return pref.getString(key, defValue)
}

fun putStringPreferences(context: Context, key: String, value: String) {
    val pref = PreferenceManager.getDefaultSharedPreferences(context)
    val editor = pref.edit()
    editor.putString(key, value)
    editor.apply()
}