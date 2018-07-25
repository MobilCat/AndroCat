package mustafaozhan.github.com.githubclient.main.activity

import android.os.Handler
import android.view.KeyEvent
import kotlinx.android.synthetic.main.fragment_main.*
import mustafaozhan.github.com.githubclient.R
import mustafaozhan.github.com.githubclient.base.BaseFragment
import mustafaozhan.github.com.githubclient.base.BaseMvvmActivity
import mustafaozhan.github.com.githubclient.extensions.snacky
import mustafaozhan.github.com.githubclient.main.fragment.MainFragment

/**
 * Created by Mustafa Ozhan on 2018-07-22.
 */
class MainActivity : BaseMvvmActivity<MainActivityViewModel>() {

    private var doubleBackToExitPressedOnce = false

    override fun getDefaultFragment(): BaseFragment = MainFragment.newInstance()

    override fun getViewModelClass(): Class<MainActivityViewModel> = MainActivityViewModel::class.java

    override fun getLayoutResId(): Int = R.layout.activity_main


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
            snacky(this, "Please click BACK again to exit")
            Handler().postDelayed({ doubleBackToExitPressedOnce = false }, 2000)
        } else
            super.onBackPressed()
    }
}