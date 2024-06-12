package com.mirea.kt.ribo.culinaryrecipeskotlin

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.mirea.kt.ribo.culinaryrecipeskotlin.DBManager
import com.mirea.kt.ribo.culinaryrecipeskotlin.MyAppSQLiteHelper
import com.mirea.kt.ribo.culinaryrecipeskotlin.R

class RecipeDetailActivity : AppCompatActivity() {

    private lateinit var r_rname: TextView
    private lateinit var r_ingredients: TextView
    private lateinit var r_process: TextView
    private lateinit var btn_delete: Button
    private lateinit var btn_share: Button
    private lateinit var r_photo: ImageView
    private lateinit var dbManager: DBManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recipe_detail)

        val tb: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(tb)
        supportActionBar?.apply {
            setHomeButtonEnabled(true)
            setDisplayHomeAsUpEnabled(true)
        }

        r_photo = findViewById(R.id.r_photo)
        r_rname = findViewById(R.id.r_rname)
        r_ingredients = findViewById(R.id.r_ingredients)
        r_process = findViewById(R.id.r_process)
        btn_delete = findViewById(R.id.btn_delete)
        btn_share = findViewById(R.id.btn_share)

        val intent = intent
        val name = intent.getStringExtra("id")

        dbManager = DBManager(MyAppSQLiteHelper(this, "my_database.db", null, 1))
        val recipes = dbManager.loadAllRecipesFromDatabase()

        val recipe = recipes.find { it.rname == name }

        recipe?.let {
            r_rname.text = it.rname
            r_ingredients.text = it.ingredients
            r_process.text = it.process
        }

        btn_share.setOnClickListener {
            val shareIntent = Intent(Intent.ACTION_SEND).apply {
                type = "text/plain"
                putExtra(Intent.EXTRA_TEXT, getString(R.string.naming) + recipe?.rname + '\n' + getString(R.string.ingredients) + recipe?.ingredients + '\n' + getString(R.string.process) + recipe?.process)
            }
            startActivity(Intent.createChooser(shareIntent, null))
        }

        btn_delete.setOnClickListener {
            dbManager.deleteRecipeFromDatabase(name)
            onBackPressed()
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}