package mustafaozhan.github.com.androcat.slider.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewpager.widget.PagerAdapter
import mustafaozhan.github.com.androcat.R

class ViewPagerAdapter(var context: Context) : PagerAdapter() {

    private var layouts = intArrayOf(
        R.layout.slide_1,
        R.layout.slide_2,
        R.layout.slide_3,
        R.layout.slide_4
    )

    override fun instantiateItem(container: ViewGroup, position: Int): View {
        val layoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view = layoutInflater.inflate(layouts[position], container, false)
        container.addView(view)
        return view
    }

    override fun getCount(): Int {
        return layouts.size
    }

    override fun isViewFromObject(view: View, obj: Any): Boolean {
        return view === obj
    }

    override fun destroyItem(container: ViewGroup, position: Int, obj: Any) {
        val view = obj as? View
        container.removeView(view)
    }
}