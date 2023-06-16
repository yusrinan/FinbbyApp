package com.example.finbbyapp

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.appcompat.widget.Toolbar
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.finbbyapp.databinding.ActivityMainBinding
import com.example.finbbyapp.ui.AddContent1Fragment
import com.example.finbbyapp.ui.JoinForumActivity
import com.example.finbbyapp.ui.LoginActivity
import com.example.finbbyapp.ui.preferences.UserModel
import com.example.finbbyapp.ui.preferences.UserPreference

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private var nav: Number = 0
    private lateinit var userPreference: UserPreference
    private lateinit var userModel: UserModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        userPreference = UserPreference(this)
        checkLoginStatus()

        val navView: BottomNavigationView = binding.navView

        val navController = findNavController(R.id.nav_host_fragment_activity_main)

        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_home, R.id.navigation_forum, R.id.navigation_add_content, R.id.navigation_profile
            )
        )
        var fragment = AddContent1Fragment()
        val currentFragment = getSupportFragmentManager()?.findFragmentByTag(AddContent1Fragment::class.java.simpleName)
        if(currentFragment !is AddContent1Fragment) {
            getSupportActionBar()?.title="tes"
        }
        val onDestinationChangedListener = NavController.OnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.navigation_forum -> {
                    nav = 1
                    invalidateOptionsMenu()
                }
                R.id.navigation_home -> {
                    nav = 0
                    invalidateOptionsMenu()
                }
              R.id.navigation_add_content -> {
                  nav = -1
                  invalidateOptionsMenu()
              }
            }
        }

        navController.addOnDestinationChangedListener(onDestinationChangedListener)

        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        if(nav == 0) {
            inflater.inflate(R.menu.search_menu, menu)

            val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
            val searchView = menu.findItem(R.id.search).actionView as SearchView

            searchView.setSearchableInfo(searchManager.getSearchableInfo(componentName))
            searchView.queryHint = "search content..."
            searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {

                override fun onQueryTextSubmit(query: String): Boolean {
//                mainViewModel.findUser(query)
                    searchView.clearFocus()
                    return true
                }

                override fun onQueryTextChange(newText: String): Boolean {
                    return false
                }
            })
        } else if(nav == 1) {
            inflater.inflate(R.menu.forum_menu, menu)
            val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
            val searchView = menu.findItem(R.id.search).actionView as SearchView

            searchView.setSearchableInfo(searchManager.getSearchableInfo(componentName))
            searchView.queryHint = "search forum..."
            searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {

                override fun onQueryTextSubmit(query: String): Boolean {
                    val intent = Intent(this@MainActivity, JoinForumActivity::class.java)
                    intent.putExtra("nama_forum", query)
                    startActivity(intent)
//                mainViewModel.findUser(query)
                    searchView.clearFocus()
                    return true
                }

                override fun onQueryTextChange(newText: String): Boolean {
                    return false
                }
            })
        }

        return true
    }

    fun checkLoginStatus() {
        userModel = userPreference.getUser()

        val sesi = userModel.name

        if(sesi?.isEmpty() as Boolean) {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

}