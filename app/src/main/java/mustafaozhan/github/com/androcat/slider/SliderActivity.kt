package mustafaozhan.github.com.androcat.slider

import mustafaozhan.github.com.androcat.R
import mustafaozhan.github.com.androcat.base.BaseMvvmActivity

class SliderActivity : BaseMvvmActivity<SliderActivityViewModel>() {
    override fun getLayoutResId() = R.layout.activity_slider

    override fun getViewModelClass(): Class<SliderActivityViewModel> = SliderActivityViewModel::class.java
}