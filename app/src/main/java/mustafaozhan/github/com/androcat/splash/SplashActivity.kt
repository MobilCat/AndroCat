package mustafaozhan.github.com.androcat.splash

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import mustafaozhan.github.com.androcat.main.activity.MainActivity
import mustafaozhan.github.com.androcat.slider.SliderActivity
import mustafaozhan.github.com.androcat.tools.GeneralSharedPreferences

/**
 * Created by Mustafa Ozhan on 2018-07-22.
 */
class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (GeneralSharedPreferences().loadSettings().sliderShown == true) {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        } else {
            GeneralSharedPreferences().updateSettings(sliderShown = true)
            startActivity(Intent(this, SliderActivity::class.java))
            finish()
        }
    }
}