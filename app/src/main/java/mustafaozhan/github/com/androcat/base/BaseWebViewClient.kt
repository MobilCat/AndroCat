package mustafaozhan.github.com.androcat.base

import android.webkit.WebViewClient
import mustafaozhan.github.com.androcat.application.Application
import mustafaozhan.github.com.androcat.dagger.component.WebViewClientComponent
import mustafaozhan.github.com.androcat.tools.DataManager
import javax.inject.Inject

abstract class BaseWebViewClient : WebViewClient() {

    protected val webViewClientComponent: WebViewClientComponent by lazy {
        Application.instance.component.webViewClientComponent()
    }

    @Inject
    lateinit var dataManager: DataManager

    init {
        inject()
    }

    abstract fun inject()

}