package com.zinnatov.sir.util

import org.json.JSONObject


fun JSONObject.getStringOrNull(key: String): String? {
    return if (this.isNull(key))
        null
    else
        this.optString(key, null)
}