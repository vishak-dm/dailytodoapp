package com.android.daily.ui

import android.content.Context
import android.os.Bundle
import androidx.core.content.ContextCompat
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import androidx.navigation.Navigation
import com.android.daily.R
import io.github.inflationx.viewpump.ViewPumpContextWrapper
import kotlinx.android.synthetic.main.activity_main.*
import androidx.navigation.ui.setupWithNavController
import dagger.android.support.DaggerAppCompatActivity


class MainActivity : DaggerAppCompatActivity() {

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

    fun showBackButton() {
        back_button.visibility = View.VISIBLE
    }

    fun hideBackButton() {
        back_button.visibility = View.GONE
    }

    fun getBackButton(): ImageView? {
        return back_button
    }

    fun showLabelEditText() {
        label_edit_text_view.visibility = View.VISIBLE
    }

    fun hideLabelEditText() {
        label_edit_text_view.visibility = View.GONE
    }

    fun getLabeleEditText(): EditText? {
        return label_edit_text_view
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

    fun getToolbar(): Toolbar? {
        return toolbar2
    }


    fun showToolbar() {
        toolbar2.visibility = View.VISIBLE
    }


}
