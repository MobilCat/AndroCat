package mustafaozhan.github.com.androcat.main.activity

import mustafaozhan.github.com.androcat.base.BaseViewModel
import org.joda.time.Instant

/**
 * Created by Mustafa Ozhan on 2018-07-22.
 */
class MainActivityViewModel : BaseViewModel() {
    override fun inject() {
        viewModelComponent.inject(this)
    }

    fun updateAdFreeActivation() = dataManager.updateSettings(adFreeActivatedDate = Instant.now())
}
