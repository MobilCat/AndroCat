package mustafaozhan.github.com.androcat.splash

import android.content.Intent
import android.os.Bundle
import mustafaozhan.github.com.androcat.base.BaseMvvmActivity
import mustafaozhan.github.com.androcat.main.activity.MainActivity
import mustafaozhan.github.com.androcat.slider.SliderActivity

/**
 * Created by Mustafa Ozhan on 2018-07-22.
 */
class SplashActivity : BaseMvvmActivity<SplashActivityViewModel>() {
    override fun getViewModelClass(): Class<SplashActivityViewModel> = SplashActivityViewModel::class.java

    override fun getLayoutResId(): Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (viewModel.getSettings().sliderShown == true) {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        } else {
            startActivity(Intent(this, SliderActivity::class.java))
            finish()
        }
    }
}