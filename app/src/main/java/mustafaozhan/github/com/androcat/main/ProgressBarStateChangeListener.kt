package mustafaozhan.github.com.androcat.main

import mustafaozhan.github.com.androcat.tools.State

interface ProgressBarStateChangeListener {
    fun animateProgressBar(isFade: Boolean)
    fun setProgressBarState(state: State, inversion: Boolean)
}