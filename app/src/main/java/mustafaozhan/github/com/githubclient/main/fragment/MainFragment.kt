package mustafaozhan.github.com.githubclient.main.fragment

import android.os.Bundle
import android.view.View
import mustafaozhan.github.com.githubclient.R
import mustafaozhan.github.com.githubclient.base.BaseMvvmFragment

/**
 * Created by Mustafa Ozhan on 2018-07-22.
 */
class MainFragment : BaseMvvmFragment<MainFragmentViewModel>() {

    companion object {
        fun newInstance(): MainFragment = MainFragment()
    }




    override fun getViewModelClass(): Class<MainFragmentViewModel> = MainFragmentViewModel::class.java

    override fun getLayoutResId(): Int = R.layout.fragment_main


}