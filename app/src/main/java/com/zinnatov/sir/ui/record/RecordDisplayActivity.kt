package com.zinnatov.sir.ui.record

import android.app.SearchManager
import android.content.Intent
import android.os.Bundle
import android.os.Parcelable
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.widget.TextView
import android.widget.Toast
import com.zinnatov.sir.R
import com.zinnatov.sir.domain.Record
import com.zinnatov.sir.services.RecordsService
import com.zinnatov.sir.ui.main.MainActivity


class RecordDisplayActivity : AppCompatActivity() {
    private var record: Record? = null
    private var recordsFilter: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_record_display)
        setSupportActionBar(findViewById(R.id.recordDisplayToolbar))
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        recordsFilter = intent.getStringExtra(SearchManager.QUERY)
        record = intent.getParcelableExtra<Record?>("record")
        setupView(record)
    }
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.record_display, menu)

        return true
    }
    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
        android.R.id.home -> onHomeOptionsItemSelected()
        R.id.action_edit -> onEditOptionsItemSelected()
        R.id.action_remove -> onRemoveOptionsItemSelected()
        else -> super.onOptionsItemSelected(item)
    }

    private fun setupView(record: Record?) {
        findViewById<TextView>(R.id.idTextView).text = record?.id
        findViewById<TextView>(R.id.nameTextView).text = record?.name
        findViewById<TextView>(R.id.dateTextView).text = record?.date ?: "нет даты"
        findViewById<TextView>(R.id.inventoryIdTextView).text = record?.inventoryId ?: "Нет инв. номера"

        val room = if (record?.room == null) "нет ауд." else "${record.room} ауд."
        findViewById<TextView>(R.id.roomTextView).text = room

        val amount = if (record?.amount == null) "нет кол-ва" else "${record.amount} шт."
        findViewById<TextView>(R.id.amountTextView).text = amount

        val price = if (record?.price == null || record.price.isNaN()) "нет цены" else "${record.price} р."
        findViewById<TextView>(R.id.priceTextView).text = price

        findViewById<TextView>(R.id.commentTextView).text = record?.comment ?: "Нет комментария"
    }
    private fun onHomeOptionsItemSelected(): Boolean {
        val recordsIntent = Intent(this, MainActivity::class.java).apply {
            putExtra(SearchManager.QUERY, recordsFilter)
        }
        startActivity(recordsIntent)
        finish()
        return true
    }
    private fun onEditOptionsItemSelected(): Boolean {
        val recordUpsertIntent = Intent(this, RecordUpsertActivity::class.java).apply {
            putExtra("record", record as Parcelable)
            putExtra(SearchManager.QUERY, recordsFilter)
        }
        startActivity(recordUpsertIntent)
        finish()
        return true
    }
    private fun onRemoveOptionsItemSelected(): Boolean {
        val recordsService = RecordsService(this)
        if (record == null) {
            Toast.makeText(this, getString(R.string.message_error_unknown), Toast.LENGTH_LONG).show()
        } else {
            recordsService.remove(record!!.id) {
                onHomeOptionsItemSelected()
                Toast.makeText(this, getString(R.string.message_record_removed), Toast.LENGTH_LONG).show()
            }
        }
        return true
    }
}
