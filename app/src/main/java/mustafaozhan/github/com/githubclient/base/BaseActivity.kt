package mustafaozhan.github.com.githubclient.base

import android.os.Bundle
import android.os.Handler
import android.support.annotation.IdRes
import android.support.annotation.LayoutRes
import android.support.v4.app.FragmentManager
import android.support.v7.app.AppCompatActivity
import android.view.KeyEvent
import android.view.Window
import android.view.WindowManager
import android.widget.Toast
import kotlinx.android.synthetic.main.fragment_main.*
import mustafaozhan.github.com.githubclient.R
import mustafaozhan.github.com.githubclient.main.fragment.MainFragment

/**
 * Created by Mustafa Ozhan on 2018-07-22.
 */
abstract class BaseActivity : AppCompatActivity() {

    @LayoutRes
    protected abstract fun getLayoutResId(): Int

    @IdRes
    open var containerId: Int = R.id.content
    private var doubleBackToExitPressedOnce = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN)
        setContentView(getLayoutResId())
        getDefaultFragment()?.let {
            replaceFragment(it, false)
        }
    }

    open fun getDefaultFragment(): BaseFragment? = null


    protected fun addFragment(containerViewId: Int, fragment: BaseFragment) {
        val ft = supportFragmentManager.beginTransaction()
        ft.add(containerViewId, fragment, fragment.fragmentTag)
        ft.commit()
    }

    protected fun addFragment(fragment: BaseFragment) {
        addFragment(containerId, fragment)
    }

    fun replaceFragment(fragment: BaseFragment, withBackStack: Boolean) {
        if (withBackStack) {
            replaceFragmentWithBackStack(containerId, fragment)
        } else {
            replaceFragment(containerId, fragment)
        }
    }

    protected fun replaceFragmentWithBackStack(containerViewId: Int, fragment: BaseFragment) {
        val ft = supportFragmentManager.beginTransaction()
        ft.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left, R.anim.enter_from_left, R.anim.exit_to_right)
        ft.replace(containerViewId, fragment, fragment.fragmentTag)
        ft.addToBackStack(null)
        ft.commit()
    }

    protected fun replaceFragment(containerViewId: Int, fragment: BaseFragment) {
        val ft = supportFragmentManager.beginTransaction()
        ft.setCustomAnimations(R.anim.enter_from_left, R.anim.exit_to_right)
        ft.replace(containerViewId, fragment, fragment.fragmentTag)
        ft.commit()
    }

    fun clearBackStack() {
        if (supportFragmentManager.backStackEntryCount > 0)
            supportFragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE)
    }

    override fun onKeyUp(keyCode: Int, event: KeyEvent?): Boolean {
        return if (keyCode == KeyEvent.KEYCODE_BACK && webView.canGoBack()) {
            val f = supportFragmentManager.findFragmentById(containerId)
            if (f is MainFragment) {
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
            Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show()
            Handler().postDelayed({ doubleBackToExitPressedOnce = false }, 2000)
        } else
            super.onBackPressed()
    }
}