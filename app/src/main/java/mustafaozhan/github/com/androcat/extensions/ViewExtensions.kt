package mustafaozhan.github.com.androcat.extensions

import android.graphics.BitmapFactory
import android.support.v4.content.ContextCompat
import android.view.View
import android.view.animation.AnimationUtils
import android.webkit.WebView
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.MobileAds
import com.mrtyvz.archedimageprogress.ArchedImageProgressBar
import mustafaozhan.github.com.androcat.R
import mustafaozhan.github.com.androcat.main.activity.MainActivity
import mustafaozhan.github.com.androcat.tools.GeneralSharedPreferences
import mustafaozhan.github.com.androcat.tools.State

/**
 * Created by Mustafa Ozhan on 1/30/18 at 12:42 AM on Arch Linux wit Love <3.
 */

const val RADIUS = 120f
const val ARC_SPEED_SUCCESS = 12
const val ARC_SPEED_FAILED = 1

fun ArchedImageProgressBar.fadeIO(isIn: Boolean) =
    if (isIn)
        startAnimation(AnimationUtils.loadAnimation(context, R.anim.fade_in))
    else
        startAnimation(AnimationUtils.loadAnimation(context, R.anim.fade_out))

fun ArchedImageProgressBar.setState(state: State) =
    when (state) {
        State.SUCCESS -> {
            if (GeneralSharedPreferences().loadSettings().isInvert) {
                setProgressImage(BitmapFactory.decodeResource(resources, R.drawable.androcat_ciycle_inverted), RADIUS)
            } else {
                setProgressImage(BitmapFactory.decodeResource(resources, R.drawable.androcat_ciycle), RADIUS)
            }
            setArchSpeed(ARC_SPEED_SUCCESS)
            visibility = View.GONE
        }
        State.FAILED -> {
            visibility = View.VISIBLE
            if (GeneralSharedPreferences().loadSettings().isInvert) {
                setProgressImage(BitmapFactory.decodeResource(resources, R.drawable.warning_inverted), RADIUS)
            } else {
                setProgressImage(BitmapFactory.decodeResource(resources, R.drawable.warning), RADIUS)
            }
            setArchSpeed(ARC_SPEED_FAILED)
            (context as MainActivity).snacky("No internet connection")
        }
    }

fun WebView.runScript(source: String, action: (String) -> Unit = {}) =
    evaluateJavascript(
        context
            .assets
            .open(source)
            .bufferedReader()
            .use {
                it.readText()
            },
        action
    )

fun AdView.loadAd(adId: Int) {
    MobileAds.initialize(context, resources.getString(adId))
    val adRequest = AdRequest.Builder().build()
    loadAd(adRequest)
}

fun ArchedImageProgressBar.setInversion(invert: Boolean) {
    if (invert) {
        setProgressImage(BitmapFactory.decodeResource(resources, R.drawable.androcat_ciycle_inverted), RADIUS)
        setBackgroundColor(ContextCompat.getColor(context, R.color.colorPrimaryDark))
        setCircleColor(ContextCompat.getColor(context, R.color.colorPrimaryDark))
        setArchColor(ContextCompat.getColor(context, R.color.white))
    } else {
        setProgressImage(BitmapFactory.decodeResource(resources, R.drawable.androcat_ciycle), RADIUS)
        setBackgroundColor(ContextCompat.getColor(context, R.color.white))
        setCircleColor(ContextCompat.getColor(context, R.color.white))
        setArchColor(ContextCompat.getColor(context, R.color.colorGitHubDash))
    }
}