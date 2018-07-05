package com.zinnatov.sir.util

import android.util.Log
import com.zinnatov.sir.BuildConfig

class Log {
    companion object {
        fun info(message: String) {
            if (!BuildConfig.DEBUG) {
                return
            }

            Log.i("SIR", message)
        }

        fun error(message: String) {
            if (!BuildConfig.DEBUG) {
                return
            }

            Log.e("SIR", message)
        }
    }
}