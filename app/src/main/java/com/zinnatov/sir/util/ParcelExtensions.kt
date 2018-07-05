package com.zinnatov.sir.util

import android.os.Parcel


fun Parcel.optInt(): Int? {
    return this.readValue(Int::class.java.classLoader) as Int?
}
fun Parcel.optDouble(): Double? {
    return this.readValue(Double::class.java.classLoader) as Double?
}