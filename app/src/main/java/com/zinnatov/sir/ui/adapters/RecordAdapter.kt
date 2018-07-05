package com.zinnatov.sir.ui.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.zinnatov.sir.R
import com.zinnatov.sir.domain.Record

class RecordAdapter(context: Context, val resource: Int, private val records: List<Record>) :
    ArrayAdapter<Record>(context, resource, records) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View? {
        var recordView = convertView
        if (recordView == null) {
            recordView = LayoutInflater.from(context).inflate(R.layout.record_listview_item, parent,false)
        }

        val record = records[position]
        val recordNameWidget = recordView?.findViewById<TextView>(R.id.nameTextView)
        recordNameWidget?.text = record.name

        val roomNameWidget = recordView?.findViewById<TextView>(R.id.roomTextView)
        roomNameWidget?.text = record.room?.toString() ?: "--"

        return recordView;
    }
}