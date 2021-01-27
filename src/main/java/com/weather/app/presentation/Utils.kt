package com.weather.app.presentation

import android.view.View
import android.widget.TextView
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach

fun <T> Flow<T>.observeOn(viewLifecycleOwner: LifecycleOwner, block: (T) -> Unit) {
    viewLifecycleOwner.lifecycleScope.launchWhenCreated {
        collect {
            block(it)
        }
    }
}

inline var TextView.textOrGone: CharSequence?
    get() = this.text
    set(value) {
        if (value == null) {
            visibility = View.GONE
        } else {
            visibility = View.VISIBLE
            text = value
        }
    }

