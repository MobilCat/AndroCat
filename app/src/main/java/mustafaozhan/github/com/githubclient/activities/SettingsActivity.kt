package mustafaozhan.github.com.githubclient.activities

import android.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_settings.*
import mustafaozhan.github.com.githubclient.R
import android.graphics.Color
import android.widget.EditText
import mustafaozhan.github.com.githubclient.extensions.getStringPreferences
import mustafaozhan.github.com.githubclient.extensions.putStringPreferences


class SettingsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        txtUsernameInput.text = getStringPreferences(applicationContext, "username", resources.getString(R.string.missUsername))

        constraintLayoutUsername.setOnClickListener {
            val alertDialog = AlertDialog.Builder(this)
            val editText = EditText(applicationContext)
            alertDialog.setTitle(resources.getString(R.string.missUsername))

            alertDialog.setView(editText)
            editText.setTextColor(Color.WHITE)
            editText.setText(getStringPreferences(applicationContext, "username", resources.getString(R.string.missUsername)))
            editText.setSelection(editText.text.length)
            alertDialog.setPositiveButton("SAVE", { _, _ ->
                putStringPreferences(applicationContext, "username", editText.text.toString())
                txtUsernameInput.text = editText.text.toString()
            })

            alertDialog.setNegativeButton("CANCEL", { _, _ ->
                // what ever you want to do with No option.
            })

            alertDialog.show()
        }
    }
}
