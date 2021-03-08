package net.ienlab.changelog

import android.R.attr.name
import android.content.ContentResolver
import android.content.ContentValues
import android.content.Context
import android.content.SharedPreferences
import android.graphics.*
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.graphics.scale
import androidx.databinding.DataBindingUtil
import com.google.android.material.snackbar.Snackbar
import net.ienlab.changelog.databinding.ActivityMainBinding
import java.io.OutputStream


class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding

    lateinit var gmSansBold: Typeface
    lateinit var gmSansMedium: Typeface

    var color = 0
    var icon = 0
    var appName = ""

    lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        sharedPreferences = getSharedPreferences("${packageName}_preferences", Context.MODE_PRIVATE)

        gmSansBold = Typeface.createFromAsset(assets, "fonts/gmsans_bold.otf")
        gmSansMedium = Typeface.createFromAsset(assets, "fonts/gmsans_medium.otf")

        color = ContextCompat.getColor(this, R.color.blogPlannerColor)
        icon = R.drawable.ic_icon_blogplanner
        appName = "블로그 플래너"

        binding.iv.setImageBitmap(getBitmapResult())
        binding.appSelect.setOnCheckedChangeListener { group, checkedId ->
            when (checkedId) {
                R.id.select_bp -> {
                    binding.etVersion.editText?.setText(sharedPreferences.getString(SharedGroup.BP_VERSION, "1.0.0"))
                    binding.etChangelog.editText?.setText(sharedPreferences.getString(SharedGroup.BP_CHANGELOG, ""))
                    color = ContextCompat.getColor(this, R.color.blogPlannerColor)
                    icon = R.drawable.ic_icon_blogplanner
                    appName = "블로그 플래너"
                }
                R.id.select_alba -> {
                    binding.etVersion.editText?.setText(sharedPreferences.getString(SharedGroup.AR_VERSION, "1.0.0"))
                    binding.etChangelog.editText?.setText(sharedPreferences.getString(SharedGroup.AR_CHANGELOG, ""))
                    color = ContextCompat.getColor(this, R.color.albatrossColor)
                    icon = R.drawable.ic_icon_sogangassist
                    appName = "알바트로스 리마인더"

                }
                R.id.select_no -> {
                    binding.etVersion.editText?.setText(sharedPreferences.getString(SharedGroup.NO_VERSION, "1.0.0"))
                    binding.etChangelog.editText?.setText(sharedPreferences.getString(SharedGroup.NO_CHANGELOG, ""))
                    color = ContextCompat.getColor(this, R.color.noMakerColor)
                    icon = R.drawable.ic_icon_nomaker
                    appName = "보이콧 짤 생성기"
                }
                R.id.select_recro -> {
                    binding.etVersion.editText?.setText(sharedPreferences.getString(SharedGroup.RR_VERSION, "1.0.0"))
                    binding.etChangelog.editText?.setText(sharedPreferences.getString(SharedGroup.RR_CHANGELOG, ""))
                    color = ContextCompat.getColor(this, R.color.blogPlannerColor)
                    appName = "레코드 루틴"
                }
            }
        }

        binding.btnRefresh.setOnClickListener {
            when (binding.appSelect.checkedRadioButtonId) {
                R.id.select_bp -> {
                    sharedPreferences.edit().putString(SharedGroup.BP_VERSION, binding.etVersion.editText?.text.toString()).apply()
                    sharedPreferences.edit().putString(SharedGroup.BP_CHANGELOG, binding.etChangelog.editText?.text.toString()).apply()
                }
                R.id.select_alba -> {
                    sharedPreferences.edit().putString(SharedGroup.AR_VERSION, binding.etVersion.editText?.text.toString()).apply()
                    sharedPreferences.edit().putString(SharedGroup.AR_CHANGELOG, binding.etChangelog.editText?.text.toString()).apply()
                }
                R.id.select_no -> {
                    sharedPreferences.edit().putString(SharedGroup.NO_VERSION, binding.etVersion.editText?.text.toString()).apply()
                    sharedPreferences.edit().putString(SharedGroup.NO_CHANGELOG, binding.etChangelog.editText?.text.toString()).apply()
                }
                R.id.select_recro -> {
                    sharedPreferences.edit().putString(SharedGroup.RR_VERSION, binding.etVersion.editText?.text.toString()).apply()
                    sharedPreferences.edit().putString(SharedGroup.RR_CHANGELOG, binding.etChangelog.editText?.text.toString()).apply()
                }
            }
            binding.iv.setImageBitmap(getBitmapResult())
        }

        binding.btnShare.setOnClickListener {
            val outputBitmap = getBitmapResult()

            val resolver: ContentResolver = contentResolver
            val contentValues = ContentValues()
            contentValues.put(MediaStore.MediaColumns.DISPLAY_NAME, name)
            contentValues.put(MediaStore.MediaColumns.MIME_TYPE, "image/png")
            contentValues.put(MediaStore.MediaColumns.RELATIVE_PATH, "DCIM/CHANGELOG")
            val imageUri = resolver.insert(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                contentValues
            ) ?: Uri.parse("")
            val fos: OutputStream? = resolver.openOutputStream(imageUri)

            outputBitmap.compress(Bitmap.CompressFormat.PNG, 100, fos)
            fos?.flush()
            fos?.close()

            Snackbar.make(window.decorView.rootView, "저장 완료", Snackbar.LENGTH_SHORT).show()
        }
    }

    fun getBitmapResult(): Bitmap {
        val option = BitmapFactory.Options().apply { inPreferredConfig = Bitmap.Config.ARGB_8888 }
        val bitmap = Bitmap.createBitmap(1000, 1000, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        val paint = Paint(Paint.LINEAR_TEXT_FLAG or Paint.ANTI_ALIAS_FLAG)

        val color = color
        canvas.drawColor(Color.WHITE)

        paint.color = color
        canvas.drawCircle(125f, 125f, 75f, paint)
        val logo = getBitmapFromVectorDrawable(this, icon, 100, 100, Color.WHITE)
        val logoColor = getBitmapFromVectorDrawable(this, icon, 900, 900, color)
        for (i in 0..10) canvas.drawBitmap(logo, 75f, 75f, paint)
        paint.typeface = gmSansBold
        paint.textSize = 72f
        paint.color = ContextCompat.getColor(this, R.color.dark_blue)
        canvas.drawText(appName, 250f, 50f + 64f, paint)
        paint.textSize = 52f
        paint.color = color
        canvas.drawText(binding.etVersion.editText?.text?.toString() ?: "", 250f, 200f, paint)

        paint.alpha = 128
        canvas.drawBitmap(logoColor, 350f, 320f, paint)
        for (i in 0..10) canvas.drawBitmap(
            getScaledDrawableBitmap(
                this,
                R.drawable.img_dev,
                255f,
                105f
            ), 377f, 845f, paint
        )

        paint.textSize = (binding.etTextsize.editText?.text.toString() ?: "46").toInt().toFloat()
        paint.color = Color.BLACK
        paint.typeface = gmSansMedium

        var y = 350f
        val text = binding.etChangelog.editText?.text?.toString() ?: ""
        for (line in text.split("\n")) {
            canvas.drawText(line, 50f, y, paint)
            y += paint.descent() - paint.ascent() + 30f
        }

        paint.alpha = 255

        return bitmap
    }

    fun getScaledDrawableBitmap(context: Context, resId: Int, width: Float, height: Float): Bitmap {
        val bitmap = (ContextCompat.getDrawable(context, resId) as BitmapDrawable).bitmap.scale(
            width.toInt(),
            height.toInt(),
            true
        )

        val outputBitmap = Bitmap.createBitmap(bitmap.width, bitmap.height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(outputBitmap)
        val paint = Paint()
        val rect = Rect(0, 0, bitmap.width, bitmap.height)

        paint.isAntiAlias = true
        canvas.drawBitmap(bitmap, rect, rect, paint)

        return outputBitmap
    }

    fun getBitmapFromVectorDrawable(
        context: Context,
        drawableId: Int,
        width: Int,
        height: Int,
        color: Int
    ): Bitmap {
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