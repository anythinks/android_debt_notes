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
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.appcompat.widget.SearchView.OnQueryTextListener
import androidx.core.widget.NestedScrollView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.android.myapp.AddActivity
import com.android.myapp.DataContainer
import com.android.myapp.R
import com.android.myapp.RecyclerViewAdapter
import com.android.myapp.SQLite
import com.android.myapp.databinding.FragmentHomeBinding
import com.android.myapp.viewmodel.DrawerViewModel
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton
import com.google.android.material.snackbar.Snackbar

class FragmentHome : Fragment() {

    lateinit var binding: FragmentHomeBinding
    private val data = ArrayList<DataContainer>()
    private lateinit var recyclerView: RecyclerView
    private lateinit var sqLite: SQLite
    private lateinit var toolbar: MaterialToolbar
    private lateinit var drawerViewModel: DrawerViewModel
    private lateinit var refreshLayout: SwipeRefreshLayout

    @SuppressLint("MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentHomeBinding.inflate(inflater, container, false)
        val view = binding.root

        sqLite = SQLite(requireContext())
        toolbar = binding.toolbar
        recyclerView = binding.recyclerView
        refreshLayout = binding.swipeRefresh
        val fab = binding.floatingActionButton
        val nestedScroll = binding.nestedScroll
        (requireActivity() as AppCompatActivity).setSupportActionBar(toolbar)
        setHasOptionsMenu(true)
        read()

        refreshLayout.setOnRefreshListener {
            read()
            refreshLayout.isRefreshing = false
        }

        fab.setOnClickListener {
            startActivity(Intent(requireActivity(), AddActivity::class.java))
        }

        nestedScroll.setOnScrollChangeListener { _, _, newY, _, oldY ->
            if (newY > (oldY + 10)) {
                fab.shrink()

            }

            if (newY < (oldY - 10)) {
                fab.extend()
            }
        }
        return view
    }

    @Deprecated("Deprecated in Java")
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        @Suppress("DEPRECATION")
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.toolbar_menu, menu)

        val item = menu.findItem(R.id.search)
        val searchView = item.actionView as SearchView
        searchView.queryHint = "Cari"

        searchView.setOnQueryTextListener(object : OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                filteredQuery(newText)
                return true
            }
        })

        searchView.setOnCloseListener {
            read()
            false
        }
    }

    @Deprecated("Deprecated in Java", ReplaceWith("true"))
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.add -> {
                startActivity(Intent(requireActivity(), AddActivity::class.java))
                true
            }

            R.id.exit -> {
                requireActivity().finish()
                true
            }

            else -> {
                false
            }
        }
    }

    fun filteredQuery(query: String?) {
        if (!query?.isEmpty()!!) {
            val filteredData = data.filter { dataFilter ->
                dataFilter.name.lowercase().contains(query.lowercase(), true)
            }
            recyclerView.adapter = RecyclerViewAdapter(filteredData)

            if (filteredData.isEmpty()) {
                Snackbar.make(requireView(), "Tidak ada hasil", Snackbar.LENGTH_SHORT).show()
            }
        }
    }

    private fun read() {
        data.clear()
        val cursor = sqLite.read()

        cursor.use {
            while (it.moveToNext()) {
                data.add(
                    DataContainer(
                        it.getString(0),
                        it.getString(1),
                        it.getString(2),
                        it.getInt(3),
                        it.getString(4),
                        it.getString(5),
                        it.getString(6)
                    )
                )
            }
        }

        recyclerView.adapter = RecyclerViewAdapter(data)
    }

    override fun onResume() {
        super.onResume()
        read()
    }
}