package com.android.myapp

data class DataContainer(
    val id: String,
    val name: String,
    val phone: String,
    val hutang: Int,
    val tanggal: String,
    val tipe: String,
    val keterangan: String
)

data class ContactData(
    val contactName: String,
    val contactNumber: String
)