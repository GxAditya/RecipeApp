package com.example.recipeapp.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.recipeapp.databinding.MealItemBinding
import com.example.recipeapp.pojo.Meal

class MealsAdapter:RecyclerView.Adapter<MealsAdapter.FavouritesMealsAdapterViewHolder>() {

   private  var onItemClickListener: ((Meal) -> Unit)? = null

    fun setOnItemClickListener(listener: (Meal) -> Unit) {
        onItemClickListener = listener
    }


    inner class FavouritesMealsAdapterViewHolder(val binding:MealItemBinding): RecyclerView.ViewHolder(binding.root)
    private val diffUtil = object : DiffUtil.ItemCallback<Meal>() {
        override fun areItemsTheSame(oldItem: Meal, newItem: Meal): Boolean {
            return oldItem.idMeal == newItem.idMeal
        }

        override fun areContentsTheSame(oldItem: Meal, newItem: Meal): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this,diffUtil)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): FavouritesMealsAdapterViewHolder {
        return FavouritesMealsAdapterViewHolder(
            MealItemBinding.inflate(
                LayoutInflater.from(parent.context),parent,false
            )
        )
    }

    override fun onBindViewHolder(holder: FavouritesMealsAdapterViewHolder, position: Int) {
        val meal = differ.currentList[position]
        Glide.with(holder.itemView).load(meal.strMealThumb).into(holder.binding.imgMeal)
        holder.binding.tvMealName.text = meal.strMeal

        holder.itemView.apply {
            // Attach click listener safely
            setOnClickListener {
                onItemClickListener?.invoke(meal)
            }
        }
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }


}