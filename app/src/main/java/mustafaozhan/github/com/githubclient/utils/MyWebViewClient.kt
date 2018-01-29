package mustafaozhan.github.com.githubclient.utils

import android.graphics.Bitmap
import android.view.View
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.LinearLayout

@Suppress("OverridingDeprecatedMember")
/**
 * Created by Mustafa Ozhan on 1/29/18 at 1:06 AM on Arch Linux wit Love <3.
 */
class MyWebViewClient(private val mGifLayout: LinearLayout) : WebViewClient() {
    override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
        view?.loadUrl(url)
        return true
    }

    override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
        mGifLayout.visibility = View.VISIBLE
        // mGifLayout.startAnimation()
    }

    override fun onPageFinished(view: WebView?, url: String?) {
        // hide element by class name
        view?.loadUrl("javascript:(function() { " +
                "document.getElementsByClassName('position-relative js-header-wrapper ')[0].style.display='none'; })()");
        mGifLayout.visibility = View.GONE
        // hide element by id
//        view?.loadUrl("javascript:(function() { " +
//                "document.getElementById('your_id').style.display='none';})()");
    }
}