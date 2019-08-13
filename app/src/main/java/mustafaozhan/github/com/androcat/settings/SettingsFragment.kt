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
import kotlinx.android.synthetic.main.fragment_settings.ad_view
import kotlinx.android.synthetic.main.fragment_settings.layout_dark_mode
import kotlinx.android.synthetic.main.fragment_settings.layout_feedback
import kotlinx.android.synthetic.main.fragment_settings.layout_on_github
import kotlinx.android.synthetic.main.fragment_settings.layout_report_issue
import kotlinx.android.synthetic.main.fragment_settings.layout_support
import kotlinx.android.synthetic.main.fragment_settings.layout_username
import kotlinx.android.synthetic.main.fragment_settings.switch_dark_mode
import kotlinx.android.synthetic.main.fragment_settings.tv_username_output
import mustafaozhan.github.com.androcat.R
import mustafaozhan.github.com.androcat.base.BaseMvvmFragment
import mustafaozhan.github.com.androcat.extensions.checkAd
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
        switch_dark_mode.setOnCheckedChangeListener { _, isChecked ->
            viewModel.updateSettings(darkMode = isChecked)
        }
        layout_dark_mode.setOnClickListener {
            switch_dark_mode.isChecked = !switch_dark_mode.isChecked
        }
        layout_username.setOnClickListener { showUsernameDialog() }
        layout_support.setOnClickListener {
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
        layout_feedback.setOnClickListener { sendFeedBack() }
        layout_on_github.setOnClickListener {
            clearBackStack()
            replaceFragment(MainFragment.newInstance(getString(R.string.url_project_repository)), false)
        }
        layout_report_issue.setOnClickListener {
            clearBackStack()
            replaceFragment(MainFragment.newInstance(getString(R.string.url_report_issue)), false)
        }
    }

    private fun init() {
        viewModel.getUserName()?.let {
            tv_username_output?.text = it
        } ?: run { tv_username_output?.text = getString(R.string.missUsername) }

        viewModel.getSettings().darkMode?.let {
            switch_dark_mode.isChecked = it
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
                tv_username_output?.text = editText.text.toString()
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
            logException(activityNotFoundException)
            snacky("You do not have any mail application.")
        }
    }

    override fun onResume() {
        ad_view.checkAd(R.string.banner_ad_id, viewModel.isRewardExpired())
        super.onResume()
    }
}