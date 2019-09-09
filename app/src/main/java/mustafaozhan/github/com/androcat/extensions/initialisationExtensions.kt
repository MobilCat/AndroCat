package mustafaozhan.github.com.androcat.extensions

import android.content.Context
import me.piruin.quickaction.ActionItem
import me.piruin.quickaction.QuickAction
import mustafaozhan.github.com.androcat.R

@Suppress("MagicNumber")
fun Context.initExplorerActions() =
    QuickAction(this, QuickAction.VERTICAL).apply {
        setColorRes(R.color.colorPrimary)
        setTextColorRes(R.color.white)
        setEnabledDivider(false)
        addActionItem(
            ActionItem(4, getString(R.string.trends), R.drawable.ic_trends),
            ActionItem(3, getString(R.string.search_in_github), R.drawable.ic_search),
            ActionItem(2, getString(R.string.dialog_remove_ads_title), R.drawable.ic_disable_ads),
            ActionItem(1, getString(R.string.dark_mode), R.drawable.ic_dark_mode)
        )
    }

@Suppress("MagicNumber")
fun Context.initFeedActions() =
    QuickAction(this, QuickAction.VERTICAL).apply {
        setColorRes(R.color.colorPrimary)
        setTextColorRes(R.color.white)
        setEnabledDivider(false)
        addActionItem(
            ActionItem(3, getString(R.string.find_in_page), R.drawable.ic_find_in_page),
            ActionItem(2, getString(R.string.forward), R.drawable.ic_forward),
            ActionItem(1, getString(R.string.back), R.drawable.ic_back)
        )
    }

@Suppress("MagicNumber")
fun Context.initStackActions() =
    QuickAction(this, QuickAction.VERTICAL).apply {
        setColorRes(R.color.colorPrimary)
        setTextColorRes(R.color.white)
        setEnabledDivider(false)
        addActionItem(
            ActionItem(4, getString(R.string.gists), R.drawable.ic_gist),
            ActionItem(3, getString(R.string.repositories), R.drawable.ic_repository),
            ActionItem(2, getString(R.string.stars), R.drawable.ic_stars),
            ActionItem(1, getString(R.string.notifications), R.drawable.ic_notifications)
        )
    }

@Suppress("MagicNumber")
fun Context.initProductionActions() =
    QuickAction(this, QuickAction.VERTICAL).apply {
        setColorRes(R.color.colorPrimary)
        setTextColorRes(R.color.white)
        setEnabledDivider(false)
        addActionItem(
            ActionItem(5, getString(R.string.new_gist), R.drawable.ic_gist),
            ActionItem(4, getString(R.string.new_repository), R.drawable.ic_repository),
            ActionItem(3, getString(R.string.projects), R.drawable.ic_projects),
            ActionItem(2, getString(R.string.pull_requests), R.drawable.ic_pull_request),
            ActionItem(1, getString(R.string.issues), R.drawable.ic_issue)
        )
    }

@Suppress("MagicNumber")
fun Context.initProfileActions(isLogin: Boolean) =
    QuickAction(this, QuickAction.VERTICAL).apply {
        setColorRes(R.color.colorPrimary)
        setTextColorRes(R.color.white)
        setEnabledDivider(false)
        addActionItem(
            ActionItem(5, getString(R.string.app_settings), R.drawable.ic_settings),
            ActionItem(4, getString(R.string.user_settings), R.drawable.ic_user_settings),
            if (isLogin) {
                ActionItem(3, getString(R.string.log_out), R.drawable.ic_logout)
            } else {
                ActionItem(2, getString(R.string.log_in), R.drawable.ic_login)
            },
            ActionItem(1, getString(R.string.profile), R.drawable.ic_user)
        )
    }
