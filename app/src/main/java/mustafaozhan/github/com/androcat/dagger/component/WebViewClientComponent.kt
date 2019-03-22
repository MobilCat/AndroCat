package mustafaozhan.github.com.androcat.dagger.component

import dagger.Subcomponent
import mustafaozhan.github.com.androcat.annontation.PerWebViewClient
import mustafaozhan.github.com.androcat.dagger.module.WebViewClientModule
import mustafaozhan.github.com.androcat.webview.AndroCatWebViewClient

@PerWebViewClient
@Subcomponent(modules = [(WebViewClientModule::class)])
interface WebViewClientComponent {
    fun inject(androCatWebViewClient: AndroCatWebViewClient)
}