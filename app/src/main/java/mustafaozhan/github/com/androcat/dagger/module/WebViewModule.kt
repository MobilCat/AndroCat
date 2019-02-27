package mustafaozhan.github.com.androcat.dagger.module

import android.webkit.WebViewClient
import dagger.Module
import dagger.Provides

@Module
class WebViewModule(private val webViewClient: WebViewClient) {

    @Provides
    internal fun providesWebViewClient(): WebViewClient {
        return webViewClient
    }
}