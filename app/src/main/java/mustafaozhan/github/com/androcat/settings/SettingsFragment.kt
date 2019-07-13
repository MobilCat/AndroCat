package mustafaozhan.github.com.androcat.settings

import android.app.AlertDialog
import android.content.ActivityNotFoundException
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.text.InputType
import android.view.View
import android.widget.EditText
import com.crashlytics.android.Crashlytics
import kotlinx.android.synthetic.main.fragment_settings.adView
import kotlinx.android.synthetic.main.fragment_settings.darkModeSwitch
import kotlinx.android.synthetic.main.fragment_settings.layoutDarkMode
import kotlinx.android.synthetic.main.fragment_settings.layoutFeedback
import kotlinx.android.synthetic.main.fragment_settings.layoutOnGitHub
import kotlinx.android.synthetic.main.fragment_settings.layoutReportIssue
import kotlinx.android.synthetic.main.fragment_settings.layoutSupport
import kotlinx.android.synthetic.main.fragment_settings.layoutUsername
import kotlinx.android.synthetic.main.fragment_settings.txtUsernameInput
import mustafaozhan.github.com.androcat.R
import mustafaozhan.github.com.androcat.base.BaseMvvmFragment
import mustafaozhan.github.com.androcat.extensions.loadAd
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
        darkModeSwitch.setOnCheckedChangeListener { _, isChecked ->
            viewModel.updateSetting(darkMode = isChecked)
        }
        layoutDarkMode.setOnClickListener {
            darkModeSwitch.isChecked = !darkModeSwitch.isChecked
        }
        layoutUsername.setOnClickListener { showUsernameDialog() }
        layoutSupport.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.url_androcat)))
            getBaseActivity()?.packageManager?.let { packageManager ->
                intent.resolveActivity(packageManager)?.let {
                    showDialog(
                        getString(R.string.support_us),
                        getString(R.string.rate_and_support),
                        getString(R.string.rate)
                    ) {
                        startActivity(intent)
                    }
                }
            }
        }
        layoutFeedback.setOnClickListener { sendFeedBack() }
        layoutOnGitHub.setOnClickListener {
            clearBackStack()
            replaceFragment(MainFragment.newInstance(getString(R.string.url_project_repository)), false)
        }
        layoutReportIssue.setOnClickListener {
            clearBackStack()
            replaceFragment(MainFragment.newInstance(getString(R.string.url_report_issue)), false)
        }
    }

    private fun init() {
        viewModel.getUserName()?.let {
            txtUsernameInput?.text = it
        } ?: run { txtUsernameInput?.text = getString(R.string.missUsername) }

        viewModel.loadSettings().darkMode?.let {
            darkModeSwitch.isChecked = it
        }
    }

    private fun showUsernameDialog() {
        val editText = EditText(context).apply {
            setTextColor(Color.WHITE)
            setText(viewModel.getUserName())
            setSelection(this.text.length)
            inputType = InputType.TYPE_CLASS_TEXT
        }

        AlertDialog
            .Builder(context)
            .setTitle(getString(R.string.username))
            .setView(editText)
            .setPositiveButton("SAVE") { _, _ ->
                viewModel.updateUserName(editText.text.toString())
                txtUsernameInput?.text = editText.text.toString()
            }
            .setNegativeButton("CANCEL") { _, _ -> }
            .show()
    }

    private fun sendFeedBack() {
        try {
            Intent(Intent.ACTION_SEND).apply {
                type = "text/email"
                putExtra(Intent.EXTRA_EMAIL, arrayOf("mr.mustafa.ozhan@gmail.com"))
                putExtra(Intent.EXTRA_SUBJECT, "Feedback for AndroCat")
                putExtra(Intent.EXTRA_TEXT, "Dear Developer," + "")
                startActivity(Intent.createChooser(this, "Send Feedback:"))
            }
        } catch (activityNotFoundException: ActivityNotFoundException) {
            Crashlytics.logException(activityNotFoundException)
            snacky("You do not have any mail application.")
        }
    }

    override fun onResume() {
        adView.loadAd(R.string.banner_ad_id)
        super.onResume()
    }
}