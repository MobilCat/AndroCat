package mustafaozhan.github.com.androcat.settings

import android.app.AlertDialog
import android.content.ActivityNotFoundException
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.EditText
import com.crashlytics.android.Crashlytics
import kotlinx.android.synthetic.main.fragment_settings.layoutFeedback
import kotlinx.android.synthetic.main.fragment_settings.layoutOnGitHub
import kotlinx.android.synthetic.main.fragment_settings.layoutSupport
import kotlinx.android.synthetic.main.fragment_settings.layoutUsername
import kotlinx.android.synthetic.main.fragment_settings.txtUsernameInput
import mustafaozhan.github.com.androcat.R
import mustafaozhan.github.com.androcat.base.BaseMvvmFragment
import mustafaozhan.github.com.androcat.main.fragment.MainFragment

/**
 * Created by Mustafa Ozhan on 2018-07-22.
 */
class SettingsFragment : BaseMvvmFragment<SettingsFragmentViewModel>() {

    companion object {
        fun newInstance(): SettingsFragment = SettingsFragment()
    }

    override fun getViewModelClass(): Class<SettingsFragmentViewModel> = SettingsFragmentViewModel::class.java

    override fun getLayoutResId(): Int = R.layout.fragment_settings

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
        setListeners()
    }

    private fun setListeners() {
        layoutUsername.setOnClickListener { showUsernameDialog() }
        layoutSupport.setOnClickListener { showRateDialog() }
        layoutFeedback.setOnClickListener { sendFeedBack() }
        layoutOnGitHub.setOnClickListener {
            getBaseActivity().clearBackStack()
            replaceFragment(MainFragment.newInstance(true), false)
        }
    }

    private fun init() {
        viewModel.initUsername()
        txtUsernameInput.text = viewModel.userName
    }

    private fun showRateDialog() {
        val builder = AlertDialog.Builder(context, R.style.AlertDialogCustom)
            .setTitle("Support us !")
            .setMessage("Please, rate and commend to the app at Google Play Store")
            .setPositiveButton("RATE") { _, _ ->
                startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.url_androcat))))
            }
            .setNegativeButton("CANCEL", null)
        builder.show()
    }

    private fun showUsernameDialog() {
        val alertDialog = AlertDialog.Builder(context)
        val editText = EditText(context)
        alertDialog.setTitle(resources.getString(R.string.missUsername))

        alertDialog.setView(editText)
        editText.setTextColor(Color.WHITE)
        editText.setText(viewModel.userName)
        editText.setSelection(editText.text.length)
        alertDialog.setPositiveButton("SAVE") { _, _ ->
            viewModel.userName = editText.text.toString()
            viewModel.saveNewUserName(editText.text.toString())
            txtUsernameInput.text = editText.text.toString()
        }

        alertDialog.setNegativeButton("CANCEL") { _, _ ->
            // what ever you want to do with No option.
        }

        alertDialog.show()
    }

    private fun sendFeedBack() {
        try {
            val email = Intent(Intent.ACTION_SEND)
            email.type = "text/email"
            email.putExtra(Intent.EXTRA_EMAIL, arrayOf("mr.mustafa.ozhan@gmail.com"))
            email.putExtra(Intent.EXTRA_SUBJECT, "Feedback for AndroCat")
            email.putExtra(Intent.EXTRA_TEXT, "Dear Developer," + "")
            startActivity(Intent.createChooser(email, "Send Feedback:"))
        } catch (activityNotFoundException: ActivityNotFoundException) {
            Crashlytics.logException(activityNotFoundException)
            snacky("You do not have any mail application.")
        }
    }
}