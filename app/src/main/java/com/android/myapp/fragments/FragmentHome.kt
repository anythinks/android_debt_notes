package com.android.myapp.fragments

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.appcompat.widget.SearchView.OnQueryTextListener
import androidx.core.view.ViewCompat
import androidx.core.widget.NestedScrollView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.android.myapp.RecyclerViewAdapter
import com.android.myapp.AddActivity
import com.android.myapp.DataContainer
import com.android.myapp.R
import com.android.myapp.SQLite
import com.android.myapp.viewmodel.DrawerViewModel
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton

class FragmentHome : Fragment() {

    private val data = ArrayList<DataContainer>()
    private lateinit var recyclerView: RecyclerView
    private lateinit var sqLite: SQLite
    private lateinit var jumlahHutang: TextView
//    private lateinit var imageEmpty: ImageView
//    private lateinit var textEmpty: MaterialTextView
    private lateinit var toolbar: MaterialToolbar
    lateinit var drawerViewModel: DrawerViewModel
    @SuppressLint("MissingInflatedId")
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_home, container, false)

        sqLite =  SQLite(context)
        drawerViewModel = ViewModelProvider(requireActivity())[DrawerViewModel::class.java]
        toolbar = view.findViewById(R.id.toolbar)
        (activity as AppCompatActivity).setSupportActionBar(toolbar)
        setHasOptionsMenu(true)
        recyclerView = view.findViewById(R.id.recyclerView)
        jumlahHutang = view.findViewById(R.id.hutang)
        val nestedScrollView = view.findViewById<NestedScrollView>(R.id.nestedScrollView)
        val fab: ExtendedFloatingActionButton = view.findViewById(R.id.floatingActionButton)
//        val refreshLayout = view.findViewById<SwipeRefreshLayout>(R.id.swipeRefresh)
//        val searchview = view.findViewById<SearchView>(R.id.searchView)
//        imageEmpty = view.findViewById(R.id.imageEmpty)
//        textEmpty = view.findViewById(R.id.textViewEmpty)
        read()
        readTotalHutang()

        nestedScrollView.setOnScrollChangeListener { v, scrollX, scrollY, oldScrollX, oldScrollY ->
            if (scrollY > oldScrollY){
                fab.shrink()
            }else {
                fab.extend()
            }
        }

        toolbar.setNavigationOnClickListener {
            openDrawerInMainActivity()
        }

//        refreshLayout.setOnRefreshListener {
//            read()
//            readTotalHutang()
//            refreshLayout.isRefreshing = false
//        }

        fab.setOnClickListener {
            startActivity(Intent(requireContext(), AddActivity::class.java))
        }



        return view
    }

    @Deprecated("Deprecated in Java")
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.toolbar_menu, menu)

        val searchItem = menu.findItem(R.id.search)
        val searchView = searchItem.actionView as SearchView
        searchView.queryHint = "Cari..."

        searchView.setOnQueryTextListener(object : OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                filteredQuery(newText)
                return true
            }
        })
    }

    @Deprecated("Deprecated in Java")
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.add -> {
                startActivity(Intent(context, AddActivity::class.java))
                return true
            }
            R.id.exit -> {
                activity?.finish()
                return true
            }
        }
        return true
    }

    fun openDrawerInMainActivity(){
        drawerViewModel.setOpendrawer(true)
    }

    fun filteredQuery(query: String?) {
        if (query!=null){
            val filteredData = data.filter { dataFilter -> query.let {
                dataFilter.name.lowercase().contains(it.lowercase(), false) } }

            recyclerView.adapter =
                RecyclerViewAdapter(filteredData)
            if (filteredData.isEmpty()){
                Toast.makeText(context,"Tidak ada", Toast.LENGTH_SHORT).show()
            }
        }
    }

    fun read() {
        data.clear()
        val cursor = sqLite.read()
        if (cursor.moveToNext()) {
            do {
                data.add(
                    DataContainer(
                        cursor.getString(0),
                        cursor.getString(1),
                        cursor.getString(2),
                        cursor.getInt(3).toString(),
                        cursor.getString(4),
                        cursor.getString(5),
                        cursor.getString(6))
                )
            } while (cursor.moveToNext())
//            imageEmpty.isVisible = false
//            textEmpty.isVisible = false
//        } else {
//            imageEmpty.isVisible = true
//            textEmpty.isVisible = true
        }
        recyclerView.adapter = RecyclerViewAdapter(data)
        ViewCompat.setNestedScrollingEnabled(recyclerView, false)
        cursor.close()
    }

    fun readTotalHutang() {
        val cursor = sqLite.readJumlahHutang()
        var nilai = 0
        jumlahHutang.text = "Rp $nilai"
        if (cursor.moveToNext()) {
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