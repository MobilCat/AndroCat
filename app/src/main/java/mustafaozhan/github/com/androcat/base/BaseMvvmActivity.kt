package mustafaozhan.github.com.androcat.base

import androidx.lifecycle.ViewModelProviders

abstract class BaseMvvmActivity<VM : BaseViewModel> : BaseActivity() {

    protected abstract fun getViewModelClass(): Class<VM>

    protected val viewModel: VM by lazy { ViewModelProviders.of(this).get(getViewModelClass()) }
}