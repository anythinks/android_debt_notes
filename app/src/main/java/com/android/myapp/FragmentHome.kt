package com.android.myapp

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.appcompat.widget.SearchView.OnQueryTextListener
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton

class FragmentHome : Fragment() {

    private val data = ArrayList<DataContainer>()
    private lateinit var recyclerView: RecyclerView
    private lateinit var sqLite: SQLite
    private lateinit var jumlahHutang: TextView
    
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_home, container, false)

        sqLite =  SQLite(context)
        recyclerView = view.findViewById(R.id.recyclerView)
        jumlahHutang = view.findViewById(R.id.hutang)
        val fab: ExtendedFloatingActionButton = view.findViewById(R.id.floatingActionButton)
        val refreshLayout = view.findViewById<SwipeRefreshLayout>(R.id.swipeRefresh)
        val searchview = view.findViewById<SearchView>(R.id.searchView)
        read()
        readTotalHutang()

        refreshLayout.setOnRefreshListener {
            read()
            readTotalHutang()
            refreshLayout.isRefreshing = false
        }

        fab.setOnClickListener {
            startActivity(Intent(requireContext(),AddActivity::class.java))
        }
        
        searchview.setOnQueryTextListener(object : OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                filteredQuery(newText)
                return true
            }
        })

        return view
    }

    fun filteredQuery(query: String?) {
        if (query!=null){
            val filteredData = data.filter { dataFilter -> query.let {
                dataFilter.name.lowercase().contains(query, true)
            } }

            recyclerView.adapter = Adapter(filteredData, context)
            if (filteredData.isEmpty()){
                Toast.makeText(context,"Tidak ada", Toast.LENGTH_SHORT).show()
            }
        }
    }

    fun read() {
        data.clear()
        val cursor = sqLite.read()
        if (cursor.moveToFirst()) {
            do {
                data.add(DataContainer(
                        cursor.getString(0),
                        cursor.getString(1),
                        cursor.getString(2),
                        cursor.getInt(3).toString(),
                        cursor.getString(4),
                        cursor.getString(5)))
            } while (cursor.moveToNext())
        }
        recyclerView.adapter = Adapter(data, context)
        cursor.close()
    }

    private fun readTotalHutang() {
        val cursor = sqLite.readJumlahHutang()
        var nilai = 0
        jumlahHutang.text = "Rp $nilai"
        if (cursor.moveToFirst()) {
            val jumlah = cursor.getInt(0)
            nilai = (nilai + jumlah)
        }
        jumlahHutang.text = "Rp $nilai"
        cursor.close()
    }

    override fun onResume() {
        super.onResume()
        
        read()
        readTotalHutang()
    }
}