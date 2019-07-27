package mustafaozhan.github.com.androcat.extensions

import android.content.Context
import android.util.Log
import android.view.View
import android.view.animation.AnimationUtils
import android.view.inputmethod.InputMethodManager
import com.crashlytics.android.Crashlytics
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.MobileAds
import im.delight.android.webview.AdvancedWebView
import mustafaozhan.github.com.androcat.R
import mustafaozhan.github.com.androcat.tools.JsScrip

/**
 * Created by Mustafa Ozhan on 1/30/18 at 12:42 AM on Arch Linux wit Love <3.
 */
fun AdvancedWebView.runScript(jsScrip: JsScrip, action: (String?) -> Unit = {}) =
    try {
        evaluateJavascript(
            context
                .assets
                .open(jsScrip.value)
                .bufferedReader()
                .use { it.readText() },
            action
        )
    } catch (exception: NoSuchMethodError) {
        Crashlytics.logException(exception)
        Crashlytics.log(
            Log.ERROR,
            "NoSuchMethodError at AdvancedWebView.runScript",
            "jsScript:${jsScrip.value}"
        )
    }

fun AdView.loadAd(adId: Int) {
    MobileAds.initialize(context, resources.getString(adId))
    val adRequest = AdRequest.Builder().build()
    loadAd(adRequest)
}

fun View.setVisibleWithAnimation(isVisible: Boolean) {
    visibility = if (isVisible) {
        startAnimation(AnimationUtils.loadAnimation(context, R.anim.fade_in))
        View.VISIBLE
    } else {
        startAnimation(AnimationUtils.loadAnimation(context, R.anim.fade_out))
        View.GONE
    }
}

@Suppress("TooGenericExceptionCaught")
fun View.hideKeyboard(): Boolean {
    try {
        val inputMethodManager = context.getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
        return inputMethodManager?.hideSoftInputFromWindow(windowToken, 0) ?: false
    } catch (exception: RuntimeException) {
        Crashlytics.logException(exception)
        Crashlytics.log(
            Log.ERROR,
            "View.hideKeyboard()",
            "exception:$exception"
        )
    }
    return false
}

fun View.showKeyboard() {
    val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
    this.requestFocus()
    imm?.showSoftInput(this, 0)
}