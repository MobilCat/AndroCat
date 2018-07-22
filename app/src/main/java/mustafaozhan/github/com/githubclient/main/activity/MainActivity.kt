package mustafaozhan.github.com.githubclient.main.activity

import mustafaozhan.github.com.githubclient.R
import mustafaozhan.github.com.githubclient.base.BaseFragment
import mustafaozhan.github.com.githubclient.base.BaseMvvmActivity
import mustafaozhan.github.com.githubclient.main.fragment.MainFragment

/**
 * Created by Mustafa Ozhan on 2018-07-22.
 */
class MainActivity : BaseMvvmActivity<MainActivityViewModel>() {

    override fun getDefaultFragment(): BaseFragment = MainFragment.newInstance()

    override fun getViewModelClass(): Class<MainActivityViewModel> = MainActivityViewModel::class.java

    override fun getLayoutResId(): Int = R.layout.activity_main

}