package com.android.myapp

import android.Manifest
import android.R.attr
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.ContactsContract
import android.view.Menu
import android.view.MenuItem
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.android.myapp.databinding.ActivityAddBinding
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.textfield.MaterialAutoCompleteTextView
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import java.text.SimpleDateFormat
import java.util.Calendar


class AddActivity : AppCompatActivity() {

    lateinit var binding: ActivityAddBinding;
    private var sqLite = SQLite(this)
    lateinit var name: TextInputEditText
    private lateinit var phone: TextInputEditText
    private lateinit var hutang: TextInputEditText
    private lateinit var tanggal: TextInputEditText
    private lateinit var keterangan: TextInputEditText
    private val PICK_CONTACT_REQUEST = 1
    private var READ_CONTACT_REQUEST_CODE = 100
    private lateinit var dropdown: MaterialAutoCompleteTextView

    @SuppressLint("SimpleDateFormat")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        name = binding.name
        phone = binding.phone
        hutang = binding.hutang
        tanggal = binding.date
        keterangan = binding.keterangan
        tanggal.inputType = 0
        val dateNow = Calendar.getInstance().time
        val dateFormat = SimpleDateFormat("dd/MM/yyyy")
        val dateString = dateFormat.format(dateNow)
        requestContactPermissions()
        val contactButton = findViewById<ImageView>(R.id.image_contact)

//        Set dropdown
        dropdown = binding.dropdown
        dropdown.inputType = 0
        val items = listOf("Hutang", "Saldo")
        val dropdownAdapter = ArrayAdapter(this, R.layout.fill_dropdown, items)
        dropdown.setAdapter(dropdownAdapter)

        tanggal.setText(dateString)

        contactButton.setOnClickListener {
            pickContact()
        }
    }

    private fun requestContactPermissions() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS)
            != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this, arrayOf(Manifest.permission.READ_CONTACTS),
                READ_CONTACT_REQUEST_CODE
            )
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.add_activiy_menu, menu)

        return true
    }

    fun checkInput() {
        if (name.length() == 0) {
            name.setError("Harap isi bidang ini")
            return
        }

        if (phone.length() == 0) {
            phone.setError("Harap isi bidang ini")
            return
        }

        if (hutang.length() == 0) {
            hutang.setError("Harap isi bidang ini")
            return
        }
        sqLite.insert(
            name.text.toString(),
            phone.text.toString(),
            hutang.text.toString().toInt(),
            tanggal.text.toString(),
            dropdown.text.toString(),
            keterangan.text.toString()
        )
        finish()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                finish()
            }

            R.id.tambahData -> {
                checkInput()
            }
        }
        return true
    }

    private fun pickContact() {
        val contactPickerIntent = Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI)
        startActivityForResult(contactPickerIntent, PICK_CONTACT_REQUEST)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == PICK_CONTACT_REQUEST && resultCode == RESULT_OK) {
            val contactUri = data?.data
            val contactDetails = getContactDetails(contactUri)
            name.setText(contactDetails.contactName)
            phone.setText(contactDetails.contactNumber)
        }
    }

    @SuppressLint("Range")
    private fun getContactDetails(contactUri: Uri?): ContactData {
        val projection = arrayOf(
            ContactsContract.Contacts.DISPLAY_NAME,
            ContactsContract.Contacts.HAS_PHONE_NUMBER
        )
        val cursor = contentResolver.query(contactUri!!, projection, null, null, null)

        cursor?.use {
            if (it.moveToFirst()) {
                val contactName =
                    it.getString(it.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME))
                val hasPhoneNumber =
                    it.getInt(it.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER))
                val uri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI

                if (hasPhoneNumber > 0) {
                    val phoneCursor = contentResolver.query(
                        uri,
                        null,
                        ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?",
                        arrayOf(contactUri.lastPathSegment),
                        null
                    )

                    phoneCursor?.use {
                        if (it.moveToFirst()) {
                            val phoneNumber =
                                it.getString(it.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER))
                            return ContactData(contactName, phoneNumber)
                            cursor.close()
                        }
                    }
                }
            }
        }
        return ContactData("", "")
    }
}