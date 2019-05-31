package com.arun.pokemonsample.utility

import android.animation.FloatEvaluator
import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.app.Activity
import android.content.Context
import android.text.SpannableString
import android.text.format.DateUtils
import android.util.TypedValue
import android.view.animation.LinearInterpolator
import android.widget.TextView

object UiUtility {
    fun calculateNoOfColumns(context: Context?, columnWidthDp: Float): Int {
        var noOfColumns = 1
        context?.let {
            val displayMetrics = context.resources.displayMetrics
            val screenWidthDp = displayMetrics.widthPixels / displayMetrics.density
            noOfColumns = (screenWidthDp / columnWidthDp + 0.5).toInt()
        }
       return noOfColumns
    }

    fun setRainbowAnimation(textView: TextView, name: String?) {
        val text = name?.let { it } ?: ""
        val span = AnimatedColorSpan(textView.context)

        val spannableString = SpannableString(text)
        val start = text.indexOf(text)
        val end = start + text.length
        spannableString.setSpan(span, start, end, 0)

        val objectAnimator = ObjectAnimator.ofFloat<AnimatedColorSpan>(
            span, AnimatedColorSpan.ANIMATED_COLOR_SPAN_FLOAT_PROPERTY, 0f, 100f
        )
        objectAnimator.setEvaluator(FloatEvaluator())
        objectAnimator.addUpdateListener { textView.text = spannableString }
        objectAnimator.interpolator = LinearInterpolator()
        objectAnimator.duration = DateUtils.MINUTE_IN_MILLIS * 3
        objectAnimator.repeatCount = ValueAnimator.INFINITE
        objectAnimator.start()
    }

    fun dpToPx(activity: Activity?, dps: Int): Int {
        val r = activity?.resources
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP, dps.toFloat(), r?.displayMetrics).toInt()
    }
}