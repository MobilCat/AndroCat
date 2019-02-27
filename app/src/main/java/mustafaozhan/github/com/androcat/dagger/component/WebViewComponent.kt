package mustafaozhan.github.com.androcat.dagger.component

import dagger.Subcomponent
import mustafaozhan.github.com.androcat.annontation.PerWebView
import mustafaozhan.github.com.androcat.dagger.module.WebViewModule
import mustafaozhan.github.com.androcat.webview.AndroCatWebViewClient

@PerWebView
@Subcomponent(modules = [(WebViewModule::class)])
interface WebViewComponent {
    fun inject(androCatWebViewClient: AndroCatWebViewClient)
}