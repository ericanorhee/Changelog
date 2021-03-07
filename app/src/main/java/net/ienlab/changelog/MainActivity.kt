package net.ienlab.changelog

import android.content.Context
import android.graphics.*
import android.graphics.drawable.BitmapDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.content.ContextCompat
import androidx.core.graphics.scale
import androidx.databinding.DataBindingUtil
import net.ienlab.changelog.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        val option = BitmapFactory.Options().apply { inPreferredConfig = Bitmap.Config.ARGB_8888 }
        val bitmap = Bitmap.createBitmap(1000, 1000, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        val paint = Paint()

        val gmSansBold = Typeface.createFromAsset(assets, "fonts/gmsans_bold.otf")
        val gmSansMedium = Typeface.createFromAsset(assets, "fonts/gmsans_medium.otf")

        canvas.drawColor(Color.WHITE)

        paint.color = ContextCompat.getColor(this, R.color.purple_200)
        canvas.drawCircle(125f, 125f, 75f, paint)
        val logo = getBitmapFromVectorDrawable(this, R.drawable.ic_icon_sogangassist, 100, 100, Color.WHITE)
        for (i in 0..10) canvas.drawBitmap(logo, 75f, 75f, paint)
        paint.typeface = gmSansBold
        paint.textSize = 40f
        canvas.drawText("알바트로스 리마인더", 250f, 50f, paint)


        binding.iv.setImageBitmap(bitmap)
    }

    fun getScaledDrawableBitmap(context: Context, resId: Int, width: Float, height: Float): Bitmap {
        val bitmap = (ContextCompat.getDrawable(context, resId) as BitmapDrawable).bitmap.scale(width.toInt(), height.toInt(), true)

        val outputBitmap = Bitmap.createBitmap(bitmap.width, bitmap.height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(outputBitmap)
        val paint = Paint()
        val rect = Rect(0, 0, bitmap.width, bitmap.height)

        paint.isAntiAlias = true
        canvas.drawBitmap(bitmap, rect, rect, paint)

        return outputBitmap
    }

    fun getBitmapFromVectorDrawable(context: Context, drawableId: Int, width: Int, height: Int, color: Int): Bitmap {
        val drawable = ContextCompat.getDrawable(context, drawableId)
        val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        val paint = Paint().apply {
            colorFilter = PorterDuffColorFilter(color, PorterDuff.Mode.SRC_IN)
        }

        drawable?.setBounds(0, 0, canvas.width, canvas.height)
        drawable?.draw(canvas)

        val bitmapResult = Bitmap.createBitmap(bitmap.width, bitmap.height, Bitmap.Config.ARGB_8888)
        val canvasResult = Canvas(bitmapResult)
        canvasResult.drawBitmap(bitmap, 0f, 0f, paint)

        return bitmapResult
    }

}