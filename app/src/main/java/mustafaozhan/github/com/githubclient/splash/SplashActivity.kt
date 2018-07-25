package mustafaozhan.github.com.githubclient.splash

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_splash.*
import mustafaozhan.github.com.githubclient.R
import mustafaozhan.github.com.githubclient.extensions.fadeIO
import mustafaozhan.github.com.githubclient.main.activity.MainActivity

/**
 * Created by Mustafa Ozhan on 2018-07-22.
 */
class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        linearSplash.fadeIO(true)
        Handler().postDelayed({
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }, 1500)
    }
}