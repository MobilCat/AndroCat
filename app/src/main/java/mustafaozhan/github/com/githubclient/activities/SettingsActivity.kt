package mustafaozhan.github.com.githubclient.activities

import android.app.AlertDialog
import android.content.Context
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_settings.*
import mustafaozhan.github.com.githubclient.R
import android.content.DialogInterface
import android.graphics.Color
import android.text.Editable
import android.widget.EditText
import mustafaozhan.github.com.githubclient.extensions.getStringPreferences
import mustafaozhan.github.com.githubclient.extensions.putStringPreferences


class SettingsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        txtUsernameInput.text = getStringPreferences(applicationContext, "username", resources.getString(R.string.please_enter_your_githup_username))

        constraintLayoutUsername.setOnClickListener {
            val alertDialog = AlertDialog.Builder(this)
            val editText = EditText(applicationContext)
            alertDialog.setTitle(resources.getString(R.string.please_enter_your_githup_username))

            alertDialog.setView(editText)
            editText.setTextColor(Color.WHITE)
            editText.setText(getStringPreferences(applicationContext, "username", resources.getString(R.string.please_enter_your_githup_username)))
            alertDialog.setPositiveButton("SAVE", { dialog, whichButton ->
                putStringPreferences(applicationContext, "username", editText.text.toString())
                txtUsernameInput.text = editText.text.toString()
            })

            alertDialog.setNegativeButton("CANCEL", { dialog, whichButton ->
                // what ever you want to do with No option.
            })

            alertDialog.show()
        }
    }
}
