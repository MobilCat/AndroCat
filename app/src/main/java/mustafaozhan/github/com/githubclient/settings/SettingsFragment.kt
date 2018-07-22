package mustafaozhan.github.com.githubclient.settings

import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.widget.EditText
import kotlinx.android.synthetic.main.fragment_settings.*
import mustafaozhan.github.com.githubclient.R
import mustafaozhan.github.com.githubclient.base.BaseMvvmFragment

/**
 * Created by Mustafa Ozhan on 2018-07-22.
 */
class SettingsFragment : BaseMvvmFragment<SettingsFragmentViewModel>() {

    companion object {
        fun newInstance(): SettingsFragment = SettingsFragment()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
        setListeners()
    }

    private fun setListeners() {
        layoutUsername.setOnClickListener { showUsernameDialog() }
        layoutSupport.setOnClickListener { showRateDialog() }
        layoutFeedback.setOnClickListener { sendFeedBack() }
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
                    var link = "market://details?id="
                    try {
                        activity?.packageManager?.getPackageInfo(MainActivity@ activity?.packageName + ":GitHub Client", 0)
                    } catch (e: PackageManager.NameNotFoundException) {
                        link = "https://play.google.com/store/apps/details?id="
                    }
                    startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(link + activity?.packageName)))
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
        val email = Intent(Intent.ACTION_SEND)
        email.type = "text/email"
        email.putExtra(Intent.EXTRA_EMAIL, arrayOf("mr.mustafa.ozhan@gmail.com"))
        email.putExtra(Intent.EXTRA_SUBJECT, "Feedback for GitHub Client")
        email.putExtra(Intent.EXTRA_TEXT, "Dear Developer," + "")
        startActivity(Intent.createChooser(email, "Send Feedback:"))
    }


    override fun getViewModelClass(): Class<SettingsFragmentViewModel> = SettingsFragmentViewModel::class.java

    override fun getLayoutResId(): Int = R.layout.fragment_settings


}