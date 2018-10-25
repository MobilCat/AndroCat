package mustafaozhan.github.com.androcat.extensions

import android.graphics.BitmapFactory
import android.view.View
import android.view.animation.AnimationUtils
import com.mrtyvz.archedimageprogress.ArchedImageProgressBar
import mustafaozhan.github.com.androcat.R
import mustafaozhan.github.com.androcat.main.activity.MainActivity
import mustafaozhan.github.com.androcat.tools.State


/**
 * Created by Mustafa Ozhan on 1/30/18 at 12:42 AM on Arch Linux wit Love <3.
 */


fun ArchedImageProgressBar.fadeIO(isIn: Boolean) {
    if (isIn)
        this.startAnimation(AnimationUtils.loadAnimation(context, R.anim.fade_in))
    else
        this.startAnimation(AnimationUtils.loadAnimation(context, R.anim.fade_out))
}

fun ArchedImageProgressBar.setState(state: State) {
    try {
        when (state) {
            State.SUCCESS -> {
                this.setProgressImage(BitmapFactory.decodeResource(resources, R.drawable.androcat), 120f)
                this.setArchSpeed(10)
                this.visibility = View.GONE
            }
            State.FAILED -> {
                this.visibility = View.VISIBLE
                this.setProgressImage(BitmapFactory.decodeResource(resources, R.drawable.warning), 120f)
                this.setArchSpeed(1)
                (this.context as MainActivity).snacky("No internet connection", isLong = true)
            }
        }
    } catch (outOfMemoryError: OutOfMemoryError) {
        (this.context as MainActivity).snacky("Your device do not have enough memory", isLong = true)
    }
}