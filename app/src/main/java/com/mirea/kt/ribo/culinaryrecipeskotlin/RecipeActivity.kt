package com.mirea.kt.ribo.culinaryrecipeskotlin

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.navigation.NavigationView
import com.mirea.kt.ribo.culinaryrecipeskotlin.DBManager
import com.mirea.kt.ribo.culinaryrecipeskotlin.MyAppSQLiteHelper
import com.mirea.kt.ribo.culinaryrecipeskotlin.R

class RecipeActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: RecipeAdapter
    private lateinit var dbManager: DBManager
    private lateinit var btnAdd: Button
    private lateinit var searchView: SearchView
    private lateinit var fab: FloatingActionButton
    private lateinit var bottomNavigationView: BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recipe)

        dbManager = DBManager(MyAppSQLiteHelper(this, "my_database.db", null, 1))

        bottomNavigationView = findViewById(R.id.bottomNavigation)
        fab = findViewById(R.id.fab)

        val tb: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(tb)
        supportActionBar?.apply {
            setHomeButtonEnabled(true)
            setDisplayHomeAsUpEnabled(true)
        }

        val recipes = dbManager.loadAllRecipesFromDatabase()
        recyclerView = findViewById(R.id.recyclerView)
        adapter = RecipeAdapter(recipes, this)
        recyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        recyclerView.adapter = adapter

        searchView = findViewById(R.id.search)
        searchView.clearFocus()
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return true
            }
        })

        fab.setOnClickListener {
            showBottomDialog()
        }

        bottomNavigationView.setOnNavigationItemSelectedListener(this::onNavigationItemSelected)
    }

    override fun onNavigationItemSelected(menuItem: MenuItem): Boolean {
        return when (menuItem.itemId) {
            R.id.exit -> {
                finishAffinity()
                System.exit(0)
                true
            }
            else -> false
        }
    }

    private fun showBottomDialog() {
        val dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.add)

        val dRname: EditText = dialog.findViewById(R.id.rname)
        val dIngredients: EditText = dialog.findViewById(R.id.ingredients)
        val dProcess: EditText = dialog.findViewById(R.id.process)
        val cancelButton: Button = dialog.findViewById(R.id.cancelButton)

        cancelButton.setOnClickListener {
            dialog.dismiss()
        }

        val btnAdd: Button = dialog.findViewById(R.id.btnAdd)
        btnAdd.setOnClickListener {
            val rname = dRname.text.toString()
            val ingredients = dIngredients.text.toString()
            val process = dProcess.text.toString()
            Log.d("CHECK", "$rname $ingredients $process")
            if (rname.isNotEmpty() && ingredients.isNotEmpty() && process.isNotEmpty()) {
                val result = dbManager.saveRecipeToDatabase(Recipe(rname, ingredients, process))
                val recipes = dbManager.loadAllRecipesFromDatabase()
                recyclerView.adapter = RecipeAdapter(recipes, this)
                dialog.dismiss()
                Toast.makeText(this, if (result) R.string.insert_success else R.string.insert_error, Toast.LENGTH_LONG).show()
            } else {
                Toast.makeText(this, R.string.incorrect_value, Toast.LENGTH_LONG).show()
            }
        }

        dialog.show()
        dialog.window?.apply {
            setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
            setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            attributes.windowAnimations = R.style.DialogAnimation
            setGravity(Gravity.BOTTOM)
        }
    }

    override fun onResume() {
        super.onResume()
        val recipes = dbManager.loadAllRecipesFromDatabase()
        recyclerView.adapter = RecipeAdapter(recipes, this)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}