package com.android.myapp

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import java.text.SimpleDateFormat
import java.util.Calendar

class UpdateActivity : AppCompatActivity() {

    lateinit var id: TextView
    lateinit var name: EditText
    private lateinit var phone: EditText
    lateinit var hutang: EditText
    private lateinit var date: EditText
    private var sqLite = SQLite(this)
    
    @SuppressLint("SimpleDateFormat")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_update)

        id = findViewById(R.id.id)
        name = findViewById(R.id.name)
        phone = findViewById(R.id.phone)
        hutang = findViewById(R.id.hutang)
        date = findViewById(R.id.date)
        date.inputType = 0

        val update = findViewById<Button>(R.id.button)
        val toolbar = findViewById<MaterialToolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = intent.getStringExtra("name")
        val currentDate = Calendar.getInstance().time
        val dateFormat = SimpleDateFormat("dd/MM/yyyy")
        val dateString = dateFormat.format(currentDate)
        date.setText(dateString)
        id.text = intent.getStringExtra("id")
        name.setText(intent.getStringExtra("name"))
        phone.setText(intent.getStringExtra("phone"))
        hutang.setText(intent.getStringExtra("hutang"))

        update.setOnClickListener {
            if (name.length() == 0) {
                name.setError("Harap isi bidang ini")
            } else if (phone.length() == 0) {
                phone.setError("Harap isi bidang ini")
            } else if (hutang.length() == 0) {
                hutang.setError("Harap isi bidang ini")
            } else {
                sqLite.update(
                    id.text.toString(),
                    name.text.toString(),
                    phone.text.toString(),
                    hutang.text.toString().toInt(),
                    date.text.toString())
                finish()
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.toolbar_delete_data, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId){
            android.R.id.home -> {
                finish()
            }
            R.id.delete -> {
                materialAlertDialog(this)
            }
        }
        return true
    }

    fun materialAlertDialog(context: Context){
        val builder = MaterialAlertDialogBuilder(context)
        builder.setTitle("Konfirmasi")
            .setMessage("Yakin ingin menghapus ?")
            .setNegativeButton("Batal") { dialog, which ->
                dialog.dismiss()
            }
            .setPositiveButton("Oke") { dialog, which ->
                sqLite.delete(id.text.toString())
                finish()
            }.create().show()
    }
}