package mustafaozhan.github.com.githubclient.extensions

import android.content.Context
import android.preference.PreferenceManager
import android.support.v4.content.ContextCompat
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.LinearLayout
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.view.*
import mustafaozhan.github.com.githubclient.R
import mustafaozhan.github.com.githubclient.utils.State

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

fun LinearLayout.fadeIO(boolean: Boolean) {
    if (boolean)
        this.startAnimation(AnimationUtils.loadAnimation(context, R.anim.fade_in))
    else
        this.startAnimation(AnimationUtils.loadAnimation(context, R.anim.fade_out))
}

fun LinearLayout.setState(state: State) {
    when (state) {
        State.SUCCESS -> {
            this.mImgViewOctocat.setImageResource(R.drawable.octocat_walking_animation)
            this.setBackgroundColor(ContextCompat.getColor(context, R.color.white))
            this.visibility = View.GONE
        }
        State.FAILED -> {
            this.mImgViewOctocat.setImageResource(R.drawable.mummytocat)
            this.setBackgroundColor(ContextCompat.getColor(context, R.color.failed))
            Toast.makeText(context, "No internet connection", Toast.LENGTH_SHORT).show()
        }
    }
}