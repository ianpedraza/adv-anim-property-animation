/*
 * Copyright (C) 2019 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.samples.propertyanimation

import android.animation.*
import android.graphics.Color
import android.os.Bundle
import android.os.CountDownTimer
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateInterpolator
import android.view.animation.LinearInterpolator
import android.widget.Button
import android.widget.FrameLayout
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatImageView

class MainActivity : AppCompatActivity() {

    lateinit var star: ImageView
    lateinit var rotateButton: Button
    lateinit var translateButton: Button
    lateinit var scaleButton: Button
    lateinit var fadeButton: Button
    lateinit var colorizeButton: Button
    lateinit var showerButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        star = findViewById(R.id.star)
        rotateButton = findViewById<Button>(R.id.rotateButton)
        translateButton = findViewById<Button>(R.id.translateButton)
        scaleButton = findViewById<Button>(R.id.scaleButton)
        fadeButton = findViewById<Button>(R.id.fadeButton)
        colorizeButton = findViewById<Button>(R.id.colorizeButton)
        showerButton = findViewById<Button>(R.id.showerButton)

        rotateButton.setOnClickListener {
            rotater()
        }

        translateButton.setOnClickListener {
            translater()
        }

        scaleButton.setOnClickListener {
            scaler()
        }

        fadeButton.setOnClickListener {
            fader()
        }

        colorizeButton.setOnClickListener {
            colorizer()
        }

        showerButton.setOnClickListener {
            shower()
        }
    }

    companion object {
        private const val HALF_SECOND = 500L
        private const val ONE_SECOND = 1000L
        private const val COUNTDOWN_TIME = 60000L
    }

    private fun makeShower() {
        val timer: CountDownTimer = object : CountDownTimer(COUNTDOWN_TIME, HALF_SECOND) {

            override fun onTick(millisUntilFinished: Long) {
                shower()
            }

            override fun onFinish() {
            }
        }

        timer.start()
    }

    private fun rotater() {
        val animator = ObjectAnimator.ofFloat(
            star,
            View.ROTATION,
            -360f,
            0f
        ).apply {
            duration = 1_000
            disableViewDuringAnimation(rotateButton)
        }

        animator.start()
    }

    private fun translater() {
        val animator = ObjectAnimator.ofFloat(
            star,
            View.TRANSLATION_X,
            200f
        ).apply {
            repeatCount = 1
            repeatMode = ObjectAnimator.REVERSE
            disableViewDuringAnimation(translateButton)
        }

        animator.start()
    }

    private fun scaler() {
        val scaleX = PropertyValuesHolder.ofFloat(View.SCALE_X, 4f)
        val scaleY = PropertyValuesHolder.ofFloat(View.SCALE_Y, 4f)

        val animator: ObjectAnimator =
            ObjectAnimator.ofPropertyValuesHolder(star, scaleX, scaleY).apply {
                repeatCount = 1
                repeatMode = ObjectAnimator.REVERSE
                disableViewDuringAnimation(scaleButton)
            }

        animator.start()
    }

    private fun fader() {
        val animator = ObjectAnimator.ofFloat(star, View.ALPHA, 0f).apply {
            repeatCount = 1
            repeatMode = ObjectAnimator.REVERSE
            disableViewDuringAnimation(fadeButton)
        }

        animator.start()
    }

    private fun colorizer() {
        val animator =
            ObjectAnimator.ofArgb(
                star.parent,
                "backgroundColor",
                Color.BLACK,
                Color.BLUE
            ).apply {
                repeatCount = 1
                repeatMode = ObjectAnimator.REVERSE
                disableViewDuringAnimation(colorizeButton)
            }

        animator.start()
    }

    private fun shower() {
        val container = star.parent as ViewGroup
        val containerW = container.width
        val containerH = container.height
        var starW: Float = star.width.toFloat()
        var starH: Float = star.height.toFloat()

        val newStar = AppCompatImageView(this).apply {
            setImageResource(R.drawable.ic_star)
            layoutParams = FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.WRAP_CONTENT,
                FrameLayout.LayoutParams.WRAP_CONTENT
            )
        }

        container.addView(newStar)

        newStar.apply {
            scaleX = Math.random().toFloat() * 1.5f + 0.1f
            scaleY = scaleX

            starW *= scaleX
            starH *= scaleY

            translationX = Math.random().toFloat() * containerW - starW / 2
        }

        val mover =
            ObjectAnimator.ofFloat(newStar, View.TRANSLATION_Y, -starH, containerH + starH).apply {
                interpolator = AccelerateInterpolator(1f)
            }

        val rotator =
            ObjectAnimator.ofFloat(newStar, View.ROTATION, (Math.random() * 1080).toFloat()).apply {
                interpolator = LinearInterpolator()
            }

        val set = AnimatorSet().apply {
            playTogether(mover, rotator)
            duration = (Math.random() * 1_500 + 500).toLong()
        }

        set.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator) {
                container.removeView(newStar)
            }
        })

        set.start()
    }
}
