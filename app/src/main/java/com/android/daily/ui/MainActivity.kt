package com.android.daily.ui

import android.content.Context
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import androidx.navigation.Navigation
import com.android.daily.R
import io.github.inflationx.viewpump.ViewPumpContextWrapper
import kotlinx.android.synthetic.main.activity_main.*
import androidx.navigation.ui.setupWithNavController


class MainActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val navController = Navigation.findNavController(this, R.id.my_nav_host_fragment)
        // Set up navigation menu
        bottom_navigation.setupWithNavController(navController)
        setSupportActionBar(toolbar2)
        supportActionBar?.title = ""
    }

    override fun attachBaseContext(newBase: Context?) {
        super.attachBaseContext(ViewPumpContextWrapper.wrap(newBase!!))
    }

    fun hideBottomNavigationView() {
        bottom_navigation.visibility = View.GONE
    }

    fun showBottomNavigationView() {
        bottom_navigation.visibility = View.VISIBLE
    }

    fun setToolBarTitle(title: String) {
        toolbar_title.text = title
    }

    fun hideCompletedText() {
        completed_text_view_toolbar.visibility = View.GONE
    }

    fun showCompletedTextView() {
        completed_text_view_toolbar.visibility = View.VISIBLE
    }


    override fun onBackPressed() {
        val currentDestination = Navigation.findNavController(this, R.id.my_nav_host_fragment).currentDestination
        when (currentDestination?.id) {
            R.id.todayTaskFragment -> finish()
            R.id.myProjectsFragment -> finish()
            R.id.thoughtsFragment -> finish()
        }
        super.onBackPressed()
    }

    fun hideToolbar() {
        toolbar2.visibility = View.GONE
    }

    fun showToolbar() {
        toolbar2.visibility = View.VISIBLE
    }


}
