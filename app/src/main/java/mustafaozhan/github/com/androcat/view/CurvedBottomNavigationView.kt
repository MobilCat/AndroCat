package mustafaozhan.github.com.androcat.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Path
import android.graphics.Point
import android.util.AttributeSet
import androidx.core.content.ContextCompat
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx
import mustafaozhan.github.com.androcat.R
import mustafaozhan.github.com.androcat.main.activity.MainActivity

@Suppress("MagicNumber")
class CurvedBottomNavigationView : BottomNavigationViewEx {

    private var mPath = Path()
    private var mPaint = Paint()

    private val mFirstCurveStartPoint = Point()
    private val mFirstCurveEndPoint = Point()
    private val mFirstCurveControlPoint1 = Point()
    private val mFirstCurveControlPoint2 = Point()

    private var mSecondCurveStartPoint = Point()
    private val mSecondCurveEndPoint = Point()
    private val mSecondCurveControlPoint1 = Point()
    private val mSecondCurveControlPoint2 = Point()

    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init()
    }

    private fun init() {

        mPaint.style = Paint.Style.FILL_AND_STROKE
        mPaint.color = ContextCompat.getColor(context, R.color.colorPrimary)
        setBackgroundColor(ContextCompat.getColor(context, R.color.transparent))
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)

        val width = getWidth(context)
        val height = getHeight(context)
        val radius = width / 12
        val transparentNavigationCurve = 8
        val backgroundNavigationCurve = 16

        mFirstCurveStartPoint.set(width / 2 - radius * 2 - radius / transparentNavigationCurve, 0)

        mFirstCurveEndPoint.set(width / 2, radius + radius / transparentNavigationCurve)

        mSecondCurveStartPoint = mFirstCurveEndPoint
        mSecondCurveEndPoint.set(width / 2 + radius * 2 + radius / transparentNavigationCurve, 0)

        mFirstCurveControlPoint1.set(
            mFirstCurveStartPoint.x + radius + radius / backgroundNavigationCurve,
            mFirstCurveStartPoint.y
        )

        mFirstCurveControlPoint2.set(
            mFirstCurveEndPoint.x - radius * 2 + radius,
            mFirstCurveEndPoint.y
        )

        mSecondCurveControlPoint1.set(
            mSecondCurveStartPoint.x + radius * 2 - radius,
            mSecondCurveStartPoint.y
        )

        mSecondCurveControlPoint2.set(
            mSecondCurveEndPoint.x - (radius + radius / backgroundNavigationCurve),
            mSecondCurveEndPoint.y
        )

        mPath.apply {
            reset()
            moveTo(0f, 0f)
            lineTo(
                mFirstCurveStartPoint.x.toFloat(),
                mFirstCurveStartPoint.y.toFloat()
            )

            cubicTo(
                mFirstCurveControlPoint1.x.toFloat(),
                mFirstCurveControlPoint1.y.toFloat(),
                mFirstCurveControlPoint2.x.toFloat(),
                mFirstCurveControlPoint2.y.toFloat(),
                mFirstCurveEndPoint.x.toFloat(),
                mFirstCurveEndPoint.y.toFloat()
            )

            cubicTo(
                mSecondCurveControlPoint1.x.toFloat(),
                mSecondCurveControlPoint1.y.toFloat(),
                mSecondCurveControlPoint2.x.toFloat(),
                mSecondCurveControlPoint2.y.toFloat(),
                mSecondCurveEndPoint.x.toFloat(),
                mSecondCurveEndPoint.y.toFloat()
            )

            lineTo(width.toFloat(), 0f)
            lineTo(width.toFloat(), height.toFloat())
            lineTo(0f, height.toFloat())
            close()
        }
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas.drawPath(mPath, mPaint)
    }

    private fun getWidth(context: Context): Int {
        val display = (context as MainActivity).windowManager.defaultDisplay
        val size = Point()
        display.getSize(size)
        return size.x
    }

    private fun getHeight(context: Context): Int {
        val display = (context as MainActivity).windowManager.defaultDisplay
        val size = Point()
        display.getSize(size)
        return size.y
    }
}
