package com.example.recipeapp.activities

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.recipeapp.R
import com.example.recipeapp.databinding.ActivityMealBinding
import com.example.recipeapp.db.MealDatabase
import com.example.recipeapp.fragments.HomeFragment
import com.example.recipeapp.pojo.Meal
import com.example.recipeapp.viewModel.MealViewModel
import com.example.recipeapp.viewModel.MealViewModelFactory

class MealActivity : AppCompatActivity() {
    private lateinit var mealId:String
    private lateinit var mealName:String
    private lateinit var mealThumb:String
    private lateinit var mealMvvm:MealViewModel
    private lateinit var youtubeLink:String

    private lateinit var binding: ActivityMealBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMealBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val mealDatabase = MealDatabase.getInstance(this)
        val viewModelFactory = MealViewModelFactory(mealDatabase)

       mealMvvm = ViewModelProvider(this,viewModelFactory)[MealViewModel::class.java]


        getMealInfoFromIntent()
        setInfoInViews()
        loadingCase()
        mealMvvm.getMealDetail(mealId)
        observeMealDetailsLiveData()
        onYoutubeImageClick()

        onFavouriteClick()


    }

    private fun onFavouriteClick() {
        binding.btnAddToFav.setOnClickListener{
            mealToSave?.let {
                mealMvvm.insertMeal(it)
                Toast.makeText(this, "Meal Saved", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun onYoutubeImageClick() {
        binding.imgYoutube.setOnClickListener{
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(youtubeLink))
            startActivity(intent)
        }
    }

    private var mealToSave:Meal?=null
    private fun observeMealDetailsLiveData() {
        mealMvvm.observerMealDetailsLiveData().observe(this,object:Observer<Meal>{
            override fun onChanged(value: Meal) {
                onResponceCase()
                val meal = value
                mealToSave = meal

                binding.tvCategory.text = "Category : ${meal!!.strCategory}"
                binding.tvArea.text = "Area : ${meal!!.strArea}"
                binding.tvInstructionSteps.text = meal.strInstructions
                youtubeLink = meal.strYoutube!!
            }
        })
    }

    private fun setInfoInViews() {
        Glide.with(applicationContext)
            .load(mealThumb)
            .into(binding.imgMealDetail)
        binding.collapsingToolbar.title = mealName
        binding.collapsingToolbar.setCollapsedTitleTextColor(resources.getColor(R.color.white))
        binding.collapsingToolbar.setExpandedTitleColor(resources.getColor(R.color.white))
    }

    private fun getMealInfoFromIntent() {
        val intent = intent
        mealId = intent.getStringExtra(HomeFragment.MEAL_ID)!!
        mealName = intent.getStringExtra(HomeFragment.MEAL_NAME)!!
        mealThumb = intent.getStringExtra(HomeFragment.MEAL_THUMB)!!
    }

   private fun loadingCase(){
       binding.btnAddToFav.visibility = View.INVISIBLE
       binding.tvArea.visibility = View.INVISIBLE
       binding.tvCategory.visibility = View.INVISIBLE
       binding.imgYoutube.visibility = View.INVISIBLE
       binding.progressBar.visibility = View.VISIBLE



   }

   private fun onResponceCase(){
       binding.btnAddToFav.visibility = View.VISIBLE
       binding.tvArea.visibility = View.VISIBLE
       binding.tvCategory.visibility = View.VISIBLE
       binding.imgYoutube.visibility = View.VISIBLE
       binding.progressBar.visibility = View.INVISIBLE
   }
}