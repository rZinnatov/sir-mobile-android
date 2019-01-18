package com.zinnatov.sir.ui.record

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Parcelable
import android.view.Menu
import android.view.MenuItem
import android.widget.AdapterView
import android.widget.ListView
import android.widget.SearchView
import com.zinnatov.sir.R
import com.zinnatov.sir.domain.Record
import com.zinnatov.sir.services.RecordsService
import com.zinnatov.sir.ui.adapters.RecordAdapter

class RecordListActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_record_list)
        setSupportActionBar(findViewById(R.id.recordListToolbar))

        val searchString = intent.getStringExtra(SearchManager.QUERY) ?: ""
        if (!searchString.isEmpty()) {
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
        }
        setupView(searchString)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.record_list, menu)

        // Associate searchable configuration with the SearchView
        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        val searchView = menu.findItem(R.id.action_search).actionView as SearchView
        searchView.setSearchableInfo(
                searchManager.getSearchableInfo(componentName)
        )

        return true
    }
    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
        android.R.id.home -> {
            setupView("")
            supportActionBar?.setDisplayHomeAsUpEnabled(false)
            true
        }

        R.id.action_add -> {
            startActivity(Intent(this, RecordUpsertActivity::class.java))
            true
        }

        else -> {
            super.onOptionsItemSelected(item)
        }
    }

    private fun setupView(searchString: String) {
        val recordsService = RecordsService(this)
        recordsService.getAll { records -> showRecords(
                records.filter { record ->
                    record.name.contains(searchString, true) ||
                            record.date?.contains(searchString, true) ?: false ||
                            record.inventoryId?.contains(searchString, true) ?: false ||
                            record.room?.toString()?.contains(searchString, true) ?: false ||
                            record.amount?.toString()?.contains(searchString, true) ?: false ||
                            record.price?.toString()?.contains(searchString, true) ?: false ||
                            record.comment?.contains(searchString, true) ?: false
                }
        )}
    }
    private fun showRecords(records: List<Record>) {
        val adapter = RecordAdapter(
                this,
                R.layout.record_listview_item,
                records
        )
        val listView = findViewById<ListView>(R.id.recordsListView)
        listView.onItemClickListener = AdapterView.OnItemClickListener { parent, view, position, id ->
            startActivity(Intent(this, RecordDisplayActivity::class.java).apply {
                putExtra("record", records[position] as Parcelable)
                putExtra(SearchManager.QUERY, intent.getStringExtra(SearchManager.QUERY))
            })
        }
        listView.adapter = adapter
    }
}
