package mustafaozhan.github.com.androcat.splash

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import mustafaozhan.github.com.androcat.main.activity.MainActivity

/**
 * Created by Mustafa Ozhan on 2018-07-22.
 */
class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }
}