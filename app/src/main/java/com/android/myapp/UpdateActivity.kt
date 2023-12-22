package com.android.myapp

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.android.myapp.databinding.ActivityAddBinding
import com.android.myapp.databinding.ActivityUpdateBinding
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.textfield.TextInputEditText
import java.text.SimpleDateFormat
import java.util.Calendar

class UpdateActivity : AppCompatActivity() {

    private lateinit var binding: ActivityUpdateBinding
    private lateinit var id: TextView
    private lateinit var name: TextInputEditText
    private lateinit var phone: TextInputEditText
    private lateinit var hutang: TextInputEditText
    private lateinit var date: TextInputEditText
    private lateinit var keterangan: TextInputEditText
    private val sqLite = SQLite(this)

    @SuppressLint("SimpleDateFormat")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUpdateBinding.inflate(layoutInflater)
        setContentView(binding.root)

        id = binding.id
        name = binding.name
        phone = binding.phone
        hutang = binding.hutang
        date = binding.date
        keterangan = binding.keterangan
        date.inputType = 0

        val update = binding.button
        val toolbar = binding.toolbar
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = intent.getStringExtra("name")
        val currentDate = Calendar.getInstance().time
        val dateFormat = SimpleDateFormat("dd/MM/yyyy")
        val dateString = dateFormat.format(currentDate)
        date.setText(dateString)

//        Set dropdown
        val dropdown = binding.dropdown
        dropdown.inputType = 0
        val items = arrayOf("Hutang", "Saldo")
        val dropdownAdapter = ArrayAdapter(this, R.layout.fill_dropdown, items)
        dropdown.setAdapter(dropdownAdapter)

        id.text = intent.getStringExtra("id")
        name.setText(intent.getStringExtra("name"))
        phone.setText(intent.getStringExtra("phone"))
        hutang.setText(intent.getStringExtra("hutang"))
        dropdown.setText(intent.getStringExtra("tipe"))
        keterangan.setText(intent.getStringExtra("keterangan"))

        update.setOnClickListener {
            if (name.text!!.isEmpty()) {
                name.error = "Harap isi bidang ini"
                return@setOnClickListener
            }
            if (phone.text!!.isEmpty()) {
                phone.error = "Harap isi bidang ini"
                return@setOnClickListener
            }
            if (hutang.text!!.isEmpty()) {
                hutang.error = "Harap isi bidang ini"
                return@setOnClickListener
            }
            sqLite.update(
                id.text.toString(),
                name.text.toString(),
                phone.text.toString(),
                hutang.text.toString().toInt(),
                date.text.toString(),
                dropdown.text.toString(),
                keterangan.text.toString()
            )
            finish()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.toolbar_update, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                finish()
            }

            R.id.delete_update -> {
                materialAlertDialog()
            }
        }
        return true
    }

    fun materialAlertDialog() {
        MaterialAlertDialogBuilder(this)
            .setTitle("Konfirmasi")
            .setMessage("Yakin ingin menghapus ?")
            .setNegativeButton(
                "Batal"
            ) { dialogInterface: DialogInterface, i: Int -> dialogInterface.dismiss() }
            .setPositiveButton("Ok") { dialogInterface: DialogInterface?, i: Int ->
                sqLite.delete(id.text.toString())
                finish()
            }
            .create()
            .show()
    }
}