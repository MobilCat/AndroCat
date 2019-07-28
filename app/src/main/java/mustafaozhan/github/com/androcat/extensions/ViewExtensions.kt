package mustafaozhan.github.com.androcat.extensions

import android.content.Context
import android.util.Log
import android.view.View
import android.view.animation.AnimationUtils
import android.view.inputmethod.InputMethodManager
import androidx.core.content.ContextCompat
import com.crashlytics.android.Crashlytics
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.MobileAds
import im.delight.android.webview.AdvancedWebView
import me.piruin.quickaction.ActionItem
import me.piruin.quickaction.QuickAction
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

@Suppress("MagicNumber")
fun Context.initExplorerActions() =
    QuickAction(this, QuickAction.VERTICAL).apply {
        setColorRes(R.color.colorPrimary)
        setTextColorRes(R.color.white)
        setEnabledDivider(false)
        addActionItem(
            ActionItem(6, getString(R.string.trends), R.drawable.ic_trends),
            ActionItem(4, getString(R.string.search_in_github), R.drawable.ic_search),
            ActionItem(5, getString(R.string.find_in_page), R.drawable.ic_find_in_page),
            ActionItem(3, getString(R.string.dark_mode), R.drawable.ic_dark_mode),
            ActionItem(2, getString(R.string.forward), R.drawable.ic_forward),
            ActionItem(1, getString(R.string.back), R.drawable.ic_back)
        )
    }

@Suppress("MagicNumber")
fun Context.initStackActions() =
    QuickAction(this, QuickAction.VERTICAL).apply {
        setColorRes(R.color.colorPrimary)
        setTextColorRes(R.color.white)
        setEnabledDivider(false)
        addActionItem(
            ActionItem(4, getString(R.string.gists), R.drawable.ic_gist),
            ActionItem(3, getString(R.string.repositories), R.drawable.ic_repository),
            ActionItem(2, getString(R.string.stars), R.drawable.ic_stars),
            ActionItem(1, getString(R.string.notifications), R.drawable.ic_notifications)
        )
    }

@Suppress("MagicNumber")
fun Context.initProductionActions() =
    QuickAction(this, QuickAction.VERTICAL).apply {
        setColorRes(R.color.colorPrimary)
        setTextColorRes(R.color.white)
        setEnabledDivider(false)
        addActionItem(
            ActionItem(5, getString(R.string.new_gist), R.drawable.ic_gist),
            ActionItem(4, getString(R.string.new_repository), R.drawable.ic_repository),
            ActionItem(3, getString(R.string.projects), R.drawable.ic_projects),
            ActionItem(2, getString(R.string.pull_requests), R.drawable.ic_pull_request),
            ActionItem(1, getString(R.string.issues), R.drawable.ic_issue)
        )
    }

@Suppress("MagicNumber")
fun Context.initProfileActions(isLogin: Boolean) =
    QuickAction(this, QuickAction.VERTICAL).apply {
        setColorRes(R.color.colorPrimary)
        setTextColorRes(R.color.white)
        setEnabledDivider(false)
        addActionItem(
            ActionItem(5, getString(R.string.app_settings), R.drawable.ic_settings),
            ActionItem(4, getString(R.string.user_settings), R.drawable.ic_user_settings),
            if (isLogin) {
                ActionItem(3, getString(R.string.log_out), R.drawable.ic_logout)
            } else {
                ActionItem(2, getString(R.string.log_in), R.drawable.ic_login)
            },
            ActionItem(1, getString(R.string.profile), R.drawable.ic_user)
        )
    }

fun View.setBGColor(context: Context, color: Int) {
    setBackgroundColor(ContextCompat.getColor(context, color))
}