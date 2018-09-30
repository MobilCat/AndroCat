package mustafaozhan.github.com.githubclient.extensions

import android.support.v4.content.ContextCompat
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.LinearLayout
import kotlinx.android.synthetic.main.fragment_main.view.*
import mustafaozhan.github.com.githubclient.R
import mustafaozhan.github.com.githubclient.main.activity.MainActivity
import mustafaozhan.github.com.githubclient.tools.State

/**
 * Created by Mustafa Ozhan on 1/30/18 at 12:42 AM on Arch Linux wit Love <3.
 */


fun LinearLayout.fadeIO(isIn: Boolean) {
    if (isIn)
        this.startAnimation(AnimationUtils.loadAnimation(context, R.anim.fade_in))
    else
        this.startAnimation(AnimationUtils.loadAnimation(context, R.anim.fade_out))
}

fun LinearLayout.setState(state: State) {
    try {
        when (state) {
            State.SUCCESS -> {
                this.mImgViewOctocat.setImageResource(R.drawable.octocat_walking_animation)
                this.setBackgroundColor(ContextCompat.getColor(context, R.color.white))
                this.visibility = View.GONE
            }
            State.FAILED -> {
                this.mImgViewOctocat.setImageResource(R.drawable.mummytocat)
                this.setBackgroundColor(ContextCompat.getColor(context, R.color.failed))
                (this.context as MainActivity).snacky("No internet connection", isLong = true)
            }
        }
    } catch (outOfMemoryError: OutOfMemoryError) {
        (this.context as MainActivity).snacky("Your device do not have enough memory", isLong = true)
    }
}