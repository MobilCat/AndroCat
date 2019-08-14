package mustafaozhan.github.com.androcat.base

import androidx.lifecycle.ViewModelProviders
import com.crashlytics.android.Crashlytics
import io.reactivex.disposables.CompositeDisposable

/**
 * Created by Mustafa Ozhan on 2018-07-22.
 */
abstract class BaseMvvmFragment<VM : BaseViewModel> : BaseFragment() {

    protected abstract fun getViewModelClass(): Class<VM>

    protected val viewModel: VM by lazy {
        ViewModelProviders.of(this).get(getViewModelClass())
    }

    protected val compositeDisposable = CompositeDisposable()

    override fun onDestroy() {
        super.onDestroy()
        compositeDisposable.clear()
    }

    protected fun logException(t: Throwable) = Crashlytics.logException(t)
}
