package mustafaozhan.github.com.githubclient.tools

import mustafaozhan.github.com.githubclient.base.BaseSharedPreferences
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Created by Mustafa Ozhan on 2018-07-22.
 */
@Singleton
class GeneralSharedPreferences @Inject
constructor() : BaseSharedPreferences() {

    companion object {
        const val GENERAL_SHARED_PREFS = "GENERAL_SHARED_PREFS"
    }

    override val preferencesName: String
        get() = GENERAL_SHARED_PREFS


}