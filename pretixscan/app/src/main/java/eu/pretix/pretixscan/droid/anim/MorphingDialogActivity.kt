package eu.pretix.pretixscan.droid.anim

import android.transition.ArcMotion
import android.view.View
import android.view.animation.AnimationUtils
import androidx.activity.OnBackPressedCallback
import androidx.annotation.ColorInt
import androidx.appcompat.app.AppCompatActivity
import eu.pretix.pretixscan.droid.R

abstract class MorphingDialogActivity : AppCompatActivity() {
    protected fun setupTransition(@ColorInt backgroundStart: Int, duration: Int = 300) {
        val arcMotion = ArcMotion()
        arcMotion.minimumHorizontalAngle = 50f
        arcMotion.minimumVerticalAngle = 50f

        val easeInOut = AnimationUtils.loadInterpolator(this, android.R.interpolator.fast_out_slow_in)

        val array = theme.obtainStyledAttributes(intArrayOf(android.R.attr.colorBackground))
        val backgroundEnd: Int = array.getColor(0, 0xFF00FF)
        val sharedEnter = MorphViewToDialog(backgroundStart, backgroundEnd, duration)
        sharedEnter.pathMotion = arcMotion
        sharedEnter.interpolator = easeInOut

        val sharedReturn = MorphDialogToView(backgroundEnd, backgroundStart, duration)
        sharedReturn.pathMotion = arcMotion
        sharedReturn.interpolator = easeInOut

        sharedEnter.addTarget(findViewById<View>(R.id.container))
        sharedReturn.addTarget(findViewById<View>(R.id.container))

        window.sharedElementEnterTransition = sharedEnter
        window.sharedElementReturnTransition = sharedReturn

        // NOTE: this doesn't support inheritance.
        // If the child activity calls `onBackPressedDispatcher.addCallback` by itself,
        // add `supportFinishAfterTransition()` to that.
        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                supportFinishAfterTransition()
            }
        })
    }
}
