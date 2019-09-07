package mustafaozhan.github.com.androcat.base

import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.annotation.MenuRes
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import mustafaozhan.github.com.androcat.main.activity.MainActivity

/**
 * Created by Mustafa Ozhan on 2018-07-22.
 */
abstract class BaseFragment : Fragment() {

    val fragmentTag: String = this.javaClass.simpleName

    @LayoutRes
    protected abstract fun getLayoutResId(): Int

    @MenuRes
    open var menuResID: Int? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(getLayoutResId(), container, false)
    }

    protected fun replaceFragment(fragment: BaseFragment, withBackStack: Boolean) {
        getBaseActivity()?.replaceFragment(fragment, withBackStack)
    }

    protected fun clearBackStack() = getBaseActivity()?.clearBackStack()

    protected fun getBaseActivity(): BaseActivity? = activity as? BaseActivity

    protected fun snacky(text: String, actionText: String = "", action: () -> Unit = {}) =
        getBaseActivity()?.snacky(text, actionText, action)

    fun hasPermissions(context: Context?, vararg permissions: String): Boolean {
        if (!(Build.VERSION.SDK_INT < Build.VERSION_CODES.M || context == null)) {
            for (permission in permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false
                }
            }
        }
        return true
    }

    protected fun showDialog(
        title: String,
        description: String,
        positiveButton: String,
        cancelable: Boolean = true,
        function: () -> Unit = {}
    ) = getBaseActivity()?.showDialog(title, description, positiveButton, cancelable, function)

    protected fun showRewardedAdDialog() = (getBaseActivity() as? MainActivity)?.showRewardedAdDialog()
}
