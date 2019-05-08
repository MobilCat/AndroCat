package mustafaozhan.github.com.androcat.extensions

import android.util.Log
import com.crashlytics.android.Crashlytics
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.MobileAds
import im.delight.android.webview.AdvancedWebView
import mustafaozhan.github.com.androcat.tools.JsScrip

/**
 * Created by Mustafa Ozhan on 1/30/18 at 12:42 AM on Arch Linux wit Love <3.
 */
fun AdvancedWebView.runScript(jsScrip: JsScrip, action: (String) -> Unit = {}) =
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