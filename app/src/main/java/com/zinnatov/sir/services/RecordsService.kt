package com.zinnatov.sir.services

import android.content.Context
import android.util.Log
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.zinnatov.sir.R
import com.zinnatov.sir.domain.Record
import com.zinnatov.sir.util.getStringOrNull
import org.json.JSONArray
import org.json.JSONObject

class RecordsService(context: Context) {
    private val url = context.getString(R.string.url_registry_prod)
    private val requestQueue = Volley.newRequestQueue(context)

    fun getAll(callback: (records: ArrayList<Record>) -> Unit) {
        try {
            // https://androidclarified.wordpress.com/2017/07/23/android-volley-custom-request-java-object-as-response/
            val request = JsonObjectRequest(
                    Request.Method.GET,
                    url,
                    null,
                    Response.Listener<JSONObject> { response ->
                        callback(
                                deserializeRecords(
                                        response.getJSONArray("records")
                                )
                        )
                    },
                    Response.ErrorListener { response -> /*Log.e("RecordsService#getAll", response.toString())*/ }
            )
            requestQueue.add(request)
        } catch (ex:Exception) {
            /*Log.e("RecordsService#getAll", ex.message)*/
            callback(ArrayList())
        }
    }
    fun add(record: Record, callback: (id: String) -> Unit) {
        try {
            // https://androidclarified.wordpress.com/2017/07/23/android-volley-custom-request-java-object-as-response/
            val request = JsonObjectRequest(
                    Request.Method.PUT,
                    url,
                    serialize(record),
                    Response.Listener<JSONObject> { response ->
                        if (response.getBoolean("isOk")) {
                            callback(response.getString("id"))
                        }
                    },
                    Response.ErrorListener { response -> /*Log.e("RecordsService#add", response.toString())*/ }
            )
            requestQueue.add(request)
        } catch (ex:Exception) {
            /*Log.e("RecordsService#add", ex.message)*/
        }
    }
    fun update(record: Record, callback: () -> Unit) {
        try {
            // https://androidclarified.wordpress.com/2017/07/23/android-volley-custom-request-java-object-as-response/
            val request = JsonObjectRequest(
                    Request.Method.POST,
                    url,
                    serialize(record),
                    Response.Listener<JSONObject> { response ->
                        if (response.getBoolean("isOk")) {
                            callback()
                        }
                    },
                    Response.ErrorListener { response -> /*Log.e("RecordsService#update", response.toString())*/ }
            )
            requestQueue.add(request)
        } catch (ex:Exception) {
            /*Log.e("RecordsService#update", ex.message)*/
        }
    }
    fun remove(recordId: String, callback: () -> Unit) {
        try {
            // https://androidclarified.wordpress.com/2017/07/23/android-volley-custom-request-java-object-as-response/
            val request = JsonObjectRequest(
                    Request.Method.DELETE,
                    "$url/$recordId",
                    null,
                    Response.Listener<JSONObject> { response ->
                        if (response.getBoolean("isOk")) {
                            callback()
                        }
                    },
                    Response.ErrorListener { response -> /*Log.e("RecordsService#remove", response.message)*/ }
            )
            requestQueue.add(request)
        } catch (ex:Exception) {
            /*Log.e("RecordsService#remove", ex.message)*/
        }
    }

    // TODO: Need refactoring
    private fun serialize(record: Record): JSONObject {
        val jsonObject = JSONObject()
        jsonObject.put("id", record.id)
        jsonObject.put("name", record.name)
        jsonObject.putOpt("date", record.date)
        jsonObject.putOpt("inventoryId", record.inventoryId)
        jsonObject.putOpt("room", record.room)
        jsonObject.putOpt("amount", record.amount)
        jsonObject.putOpt("price", record.price)
        jsonObject.putOpt("comment", record.comment)
        return jsonObject
    }
    private fun deserializeRecords(recordsJson: JSONArray): ArrayList<Record> {
        val records = ArrayList<Record>()
        for (i in 0 until recordsJson.length()) {
            val recordJson = recordsJson.getJSONObject(i)
            val room = recordJson.optInt("room")
            val amount = recordJson.optInt("amount")
            val price = recordJson.optDouble("price")
            records.add(Record(
                    recordJson.getString("_id"),
                    recordJson.getString("name"),
                    recordJson.getStringOrNull("date"),
                    recordJson.getStringOrNull("inventoryId"),
                    if (room < 1) null else room,
                    if (amount < 1) null else amount,
                    if (price < 1) null else price,
                    recordJson.getStringOrNull("comment")
            ))
        }
        return records
    }
}