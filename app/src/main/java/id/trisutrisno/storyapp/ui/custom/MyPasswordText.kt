package id.trisutrisno.storyapp.ui.custom

import android.content.Context
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.text.Editable
import android.text.TextWatcher
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.content.ContextCompat
import id.trisutrisno.storyapp.R

class MyPasswordText: AppCompatEditText, View.OnTouchListener {
    private lateinit var eyeButtonImage: Drawable

    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(context, attrs, defStyle) {
        init()
    }

    private fun init() {
        eyeButtonImage = ContextCompat.getDrawable(context, R.drawable.ic_baseline_remove_red_eye_24) as Drawable
        setOnTouchListener(this)

        addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
             }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(s: Editable?) {
                if (s.toString().length < 6) showError()
            }

        })
    }

    private fun showError() {
        error = context.getString(R.string.error_password_not_valid)
    }

    private fun showEyeButton() {
        setButtonDrawables(endOfTheText = eyeButtonImage)
    }

    private fun hideEyeButton() {
        setButtonDrawables()
    }

    private fun setButtonDrawables(startOfTheText: Drawable? = null, topOfTheText:Drawable? = null, endOfTheText:Drawable? = null, bottomOfTheText: Drawable? = null) {
        setCompoundDrawablesWithIntrinsicBounds(startOfTheText, topOfTheText, endOfTheText, bottomOfTheText)
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        showEyeButton()
        textSize = 15f
        hint = context.getString(R.string.insert_password)
        textAlignment = View.TEXT_ALIGNMENT_VIEW_START
    }

    override fun onTouch(v: View?, event: MotionEvent?): Boolean {
        if (compoundDrawables[2] != null) {
            val eyeButtonStart: Float
            val eyeButtonEnd: Float
            var isEyeButtonClicked = false

            if (layoutDirection == View.LAYOUT_DIRECTION_RTL) {
                eyeButtonEnd = (eyeButtonImage.intrinsicWidth + paddingStart).toFloat()
                if (event!!.x < eyeButtonEnd) isEyeButtonClicked = true
            } else {
                eyeButtonStart = (width - paddingEnd - eyeButtonImage.intrinsicWidth).toFloat()
                if (event!!.x > eyeButtonStart) isEyeButtonClicked = true
            }

            if (isEyeButtonClicked) {
                return when (event.action) {
                    MotionEvent.ACTION_DOWN -> {
                        hideEyeButton()
                        if (transformationMethod.equals(HideReturnsTransformationMethod.getInstance())) {
                            transformationMethod = PasswordTransformationMethod.getInstance() // hide password
                            eyeButtonImage = ContextCompat.getDrawable(context, R.drawable.ic_eye) as Drawable
                            showEyeButton()
                        } else {
                            transformationMethod = HideReturnsTransformationMethod.getInstance() // show password
                            eyeButtonImage = ContextCompat.getDrawable(context, R.drawable.ic_eye_off) as Drawable
                            showEyeButton()
                        }
                        true
                    }
                    else -> false
                }
            } else return false
        }
        return false
    }

}