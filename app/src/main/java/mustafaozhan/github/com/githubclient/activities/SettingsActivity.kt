package mustafaozhan.github.com.githubclient.activities

import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.EditText
import kotlinx.android.synthetic.main.activity_settings.*
import mustafaozhan.github.com.githubclient.R
import mustafaozhan.github.com.githubclient.extensions.getStringPreferences
import mustafaozhan.github.com.githubclient.extensions.putStringPreferences


class SettingsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        init()
        setListeners()

    }

    private fun setListeners() {
        layoutUsername.setOnClickListener { showUsernameDialog() }
        layoutSupport.setOnClickListener { showRateDialog() }
        layoutFeedback.setOnClickListener { sendFeedBack() }
    }

    private fun init() {
        txtUsernameInput.text = getStringPreferences(applicationContext, "username", resources.getString(R.string.missUsername))
    }

    private fun showRateDialog() {
        val builder = AlertDialog.Builder(this, R.style.AlertDialogCustom)
                .setTitle("Support us !")
                .setMessage("Please, rate and commend to the app at Google Play Store")
                .setPositiveButton("RATE") { _, _ ->
                    var link = "market://details?id="
                    try {
                        packageManager.getPackageInfo(MainActivity@ this.packageName + ":GitHub Client", 0)
                    } catch (e: PackageManager.NameNotFoundException) {
                        link = "https://play.google.com/store/apps/details?id="
                    }
                    startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(link + packageName)))
                }
                .setNegativeButton("CANCEL", null)
        builder.show()
    }

    private fun showUsernameDialog() {
        val alertDialog = AlertDialog.Builder(this)
        val editText = EditText(applicationContext)
        alertDialog.setTitle(resources.getString(R.string.missUsername))

        alertDialog.setView(editText)
        editText.setTextColor(Color.WHITE)
        editText.setText(getStringPreferences(applicationContext, "username", resources.getString(R.string.missUsername)))
        editText.setSelection(editText.text.length)
        alertDialog.setPositiveButton("SAVE") { _, _ ->
            putStringPreferences(applicationContext, "username", editText.text.toString())
            txtUsernameInput.text = editText.text.toString()
        }

        alertDialog.setNegativeButton("CANCEL") { _, _ ->
            // what ever you want to do with No option.
        }

        alertDialog.show()
    }

    private fun sendFeedBack() {
        val email = Intent(Intent.ACTION_SEND)
        email.type = "text/email"
        email.putExtra(Intent.EXTRA_EMAIL, arrayOf("mr.mustafa.ozhan@gmail.com"))
        email.putExtra(Intent.EXTRA_SUBJECT, "Feedback for GitHub Client")
        email.putExtra(Intent.EXTRA_TEXT, "Dear Developer," + "")
        startActivity(Intent.createChooser(email, "Send Feedback:"))
    }
}
