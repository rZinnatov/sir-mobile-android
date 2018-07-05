package com.zinnatov.sir.ui.record

import android.app.SearchManager
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.EditText
import android.widget.Toast
import com.zinnatov.sir.R
import com.zinnatov.sir.domain.Record
import com.zinnatov.sir.services.RecordsService
import android.content.Intent
import android.os.Parcelable
import android.view.View
import android.widget.Button
import com.zinnatov.sir.ui.main.MainActivity


class RecordUpsertActivity : AppCompatActivity() {
    private var record: Record? = null
    private var isInsert: Boolean = true
    private var recordsService: RecordsService? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_record_upsert)
        setSupportActionBar(findViewById(R.id.recordUpsertToolbar))
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        recordsService = RecordsService(this)

        record = intent.getParcelableExtra<Record?>("record")
        isInsert = record == null
        if (!isInsert) {
            setupView(record)
        }
        findViewById<Button>(R.id.applyButton).text = if (isInsert) getString(R.string.action_add) else getString(R.string.action_edit)
    }
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.record_upsert, menu)
        return true
    }
    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
        android.R.id.home -> {
            if (isInsert) {
                goToRecordsActivity()
            } else {
                goToRecordDisplayActivity(record)
            }
            finish()
            true
        }
        R.id.action_apply -> {
            upsertRecord()
            true
        }
        else -> {
            super.onOptionsItemSelected(item)
        }
    }

    fun onApplyButtonClick(view: View) {
        upsertRecord()
    }
    private fun setupView(record: Record?) {
        findViewById<EditText>(R.id.nameEditText).setText(record?.name)
        findViewById<EditText>(R.id.dateEditText).setText(record?.date)
        findViewById<EditText>(R.id.inventoryIdEditText).setText(record?.inventoryId)
        if (record?.room != null) {
            findViewById<EditText>(R.id.roomEditText).setText(record.room.toString())
        }
        if (record?.amount != null) {
            findViewById<EditText>(R.id.amountEditText).setText(record.amount.toString())
        }
        if (record?.price != null && !record.price.isNaN()) {
            findViewById<EditText>(R.id.priceEditText).setText(record.price.toString())
        }
        findViewById<EditText>(R.id.commentEditText).setText(record?.comment)
    }
    private fun upsertRecord() {
        val name = findViewById<EditText>(R.id.nameEditText).text.toString()
        if (name.isEmpty()) {
            Toast.makeText(this, getString(R.string.message_record_name_required), Toast.LENGTH_LONG).show()
            return
        }
        val dateString = findViewById<EditText>(R.id.dateEditText).text.toString()
        val inventoryId = findViewById<EditText>(R.id.inventoryIdEditText).text.toString()
        val roomString = findViewById<EditText>(R.id.roomEditText).text.toString()
        val amountString = findViewById<EditText>(R.id.amountEditText).text.toString()
        val priceString = findViewById<EditText>(R.id.priceEditText).text.toString()
        val commentString = findViewById<EditText>(R.id.commentEditText).text.toString()
        val newRecord = Record(
                record?.id ?: "",
                name,
                if (dateString.isEmpty()) null else dateString,
                if (inventoryId.isEmpty()) null else inventoryId,
                if (roomString.isEmpty()) null else roomString.toInt(),
                if (amountString.isEmpty()) null else amountString.toInt(),
                if (priceString.isEmpty()) null else priceString.toDouble(),
                if (commentString.isEmpty()) null else commentString
        )

        if (isInsert) {
            recordsService?.add(newRecord) { id ->
                Toast.makeText(this, getString(R.string.message_record_added), Toast.LENGTH_LONG).show()
                newRecord.id = id
                goToRecordDisplayActivity(newRecord)
            }
            return
        }

        recordsService?.update(newRecord) {
            Toast.makeText(this, getString(R.string.message_record_updated), Toast.LENGTH_LONG).show()
            goToRecordDisplayActivity(newRecord)
        }
    }
    private fun goToRecordsActivity() {
        startActivity(Intent(this, MainActivity::class.java).apply {
            putExtra(SearchManager.QUERY, intent.getStringExtra(SearchManager.QUERY))
        })
    }
    private fun goToRecordDisplayActivity(record: Record?) {
        startActivity(Intent(this, RecordDisplayActivity::class.java).apply {
            putExtra("record", record as Parcelable)
            putExtra(SearchManager.QUERY, intent.getStringExtra(SearchManager.QUERY))
        })
    }
}
