package com.mirea.kt.ribo.culinaryrecipeskotlin

import android.content.ContentValues
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import com.mirea.kt.ribo.culinaryrecipeskotlin.Recipe

class DBManager(private val dbHelper: MyAppSQLiteHelper) {

    private val tableName = "recipes"
    private val columnRecipeName = "rname"
    private val columnIngredients = "ingredients"
    private val columnProcess = "process"

    fun loadAllRecipesFromDatabase(): ArrayList<Recipe> {
        val db: SQLiteDatabase = dbHelper.readableDatabase
        val cursor: Cursor = db.query(tableName, null, null, null, null, null, null)
        val recipes = ArrayList<Recipe>()
        while (cursor.moveToNext()) {
            val recipeName = cursor.getString(cursor.getColumnIndexOrThrow(columnRecipeName))
            val ingredients = cursor.getString(cursor.getColumnIndexOrThrow(columnIngredients))
            val process = cursor.getString(cursor.getColumnIndexOrThrow(columnProcess))
            recipes.add(Recipe(recipeName, ingredients, process))
        }
        cursor.close()
        db.close()
        return recipes
    }

    fun saveRecipeToDatabase(recipe: Recipe): Boolean {
        val db: SQLiteDatabase = dbHelper.writableDatabase
        val values = ContentValues().apply {
            put(columnRecipeName, recipe.rname)
            put(columnIngredients, recipe.ingredients)
            put(columnProcess, recipe.process)
        }
        val result = db.insert(tableName, null, values)
        db.close()
        return result != -1L
    }

    fun deleteRecipeFromDatabase(recipeName: String?) {
        val db: SQLiteDatabase = dbHelper.writableDatabase
        db.delete(tableName, "$columnRecipeName = ?", arrayOf(recipeName))
        db.close()
    }
}