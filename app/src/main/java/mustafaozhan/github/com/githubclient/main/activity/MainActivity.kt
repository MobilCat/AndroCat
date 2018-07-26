package mustafaozhan.github.com.githubclient.main.activity

import android.graphics.Typeface
import android.os.Handler
import android.support.v4.content.ContextCompat
import android.view.KeyEvent
import de.mateware.snacky.Snacky
import kotlinx.android.synthetic.main.fragment_main.*
import mustafaozhan.github.com.githubclient.R
import mustafaozhan.github.com.githubclient.base.BaseFragment
import mustafaozhan.github.com.githubclient.base.BaseMvvmActivity
import mustafaozhan.github.com.githubclient.main.fragment.MainFragment
import mustafaozhan.github.com.githubclient.settings.SettingsFragment

/**
 * Created by Mustafa Ozhan on 2018-07-22.
 */
class MainActivity : BaseMvvmActivity<MainActivityViewModel>() {

    companion object {
        var uri: String? = null
    }

    private var doubleBackToExitPressedOnce = false

    override fun getDefaultFragment(): BaseFragment = MainFragment.newInstance()

    override fun getViewModelClass(): Class<MainActivityViewModel> = MainActivityViewModel::class.java

    override fun getLayoutResId(): Int = R.layout.activity_main

    override fun onResume() {
        super.onResume()
        val data = this.intent.data
        if (data != null && data.isHierarchical)
            uri = this.intent.dataString
    }

    override fun onKeyUp(keyCode: Int, event: KeyEvent?): Boolean {
        val f = supportFragmentManager.findFragmentById(containerId)

        return if (f is MainFragment) {
            if (keyCode == KeyEvent.KEYCODE_BACK && webView.canGoBack()) {
                webView.goBack()
                true
            } else
                super.onKeyUp(keyCode, event)
        } else
            super.onKeyUp(keyCode, event)
    }

    override fun onBackPressed() {

        val f = supportFragmentManager.findFragmentById(containerId)
        if (f is MainFragment) {
            if (doubleBackToExitPressedOnce) {
                super.onBackPressed()
                return
            }
            this.doubleBackToExitPressedOnce = true
            snacky("Please click BACK again to exit")
            Handler().postDelayed({ doubleBackToExitPressedOnce = false }, 2000)
        } else
            super.onBackPressed()
    }

    fun snacky(text: String, hasAction: Boolean = false, actionText: String = "") {

        val mySnacky = Snacky.builder()
                .setBackgroundColor(ContextCompat.getColor(this, R.color.colorPrimary))
                .setText(text)
                .setIcon(R.mipmap.ic_launcher)
                .setActivity(this)
                .setDuration(Snacky.LENGTH_SHORT)

        if (hasAction) {
            mySnacky.setActionText(actionText.toUpperCase())
                    .setActionTextTypefaceStyle(Typeface.BOLD)
                    .setActionClickListener { replaceFragment(SettingsFragment.newInstance(), true) }

        }
        mySnacky.build().show()
    }
}