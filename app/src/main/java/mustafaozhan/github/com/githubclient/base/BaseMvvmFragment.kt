package mustafaozhan.github.com.githubclient.base

import android.arch.lifecycle.ViewModelProviders

/**
 * Created by Mustafa Ozhan on 2018-07-22.
 */
abstract class BaseMvvmFragment<VM : BaseViewModel> : BaseFragment() {

    protected abstract fun getViewModelClass(): Class<VM>

    protected val viewModel: VM by lazy { ViewModelProviders.of(this).get(getViewModelClass()) }
}