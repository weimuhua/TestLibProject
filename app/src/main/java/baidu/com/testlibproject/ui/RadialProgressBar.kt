package baidu.com.testlibproject.ui

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.widget.ProgressBar


class RadialProgressBar : ProgressBar {

    private val thickness = 6f
    private val startAngle = 270f
    private val startColor = Color.parseColor("#ff008f")
    private val endColor = Color.parseColor("#ff90c4")

    private val halfThickness = thickness / 2
    private var boundsF: RectF? = null
    private var shader: SweepGradient? = null
    private lateinit var paint: Paint
    private lateinit var endPointPaint: Paint

    constructor(context: Context?) : super(context) {
        init()
    }

    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs) {
        init()
    }

    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) :
            super(context, attrs, defStyleAttr) {
        init()
    }

    private fun init() {
        paint = Paint()
        paint.isAntiAlias = true
        paint.style = Paint.Style.STROKE
        paint.strokeWidth = thickness
        paint.strokeCap = Paint.Cap.ROUND
        progressDrawable = null

        endPointPaint = Paint()
        endPointPaint.isAntiAlias = true
        endPointPaint.style = Paint.Style.STROKE
        endPointPaint.strokeWidth = thickness / 2
        endPointPaint.color = startColor
    }

    override fun draw(canvas: Canvas) {
        super.draw(canvas)

        if (boundsF == null) {
            boundsF = RectF(background.bounds)
            boundsF?.inset(halfThickness, halfThickness)
        }

        if (shader == null) {
            val gradient = SweepGradient(width / 2.toFloat(), height / 2.toFloat(),
                    intArrayOf(startColor, endColor), null)

            val matrix = Matrix()
            matrix.setRotate(startAngle, width / 2.toFloat(), height / 2.toFloat())
            gradient.setLocalMatrix(matrix)
            shader = gradient

            paint.shader = shader
        }
        canvas.drawArc(boundsF!!, startAngle, progress * 3.60f, false, paint)
        if (progress > 0) {
            canvas.drawCircle(width / 2.toFloat(), halfThickness, halfThickness / 2, endPointPaint)
        }
    }
}