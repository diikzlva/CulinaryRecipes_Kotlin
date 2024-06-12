package com.mirea.kt.ribo.culinaryrecipeskotlin

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.mirea.kt.ribo.culinaryrecipeskotlin.R

class RecipeAdapter(private val recipes: ArrayList<Recipe>, private val context: Context) :
    RecyclerView.Adapter<RecipeAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val logo: ImageView = itemView.findViewById(R.id.recipeIcon)
        val rname: TextView = itemView.findViewById(R.id.rnameRecipe)
        val rparent: RelativeLayout = itemView.findViewById(R.id.RecipesLayout)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_recipe, parent, false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val recipe = recipes[position]
        holder.rname.text = recipe.rname
        holder.rparent.setOnClickListener {
            val intent = Intent(context, RecipeDetailActivity::class.java).apply {
                putExtra("id", recipe.rname)
            }
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return recipes.size
    }
}