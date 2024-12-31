package com.example.recipeapp.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.recipeapp.activities.MainActivity
import com.example.recipeapp.activities.MealActivity
import com.example.recipeapp.adapters.MealsAdapter
import com.example.recipeapp.databinding.FragmentFavouritesBinding
import com.example.recipeapp.fragments.HomeFragment.Companion.MEAL_ID
import com.example.recipeapp.fragments.HomeFragment.Companion.MEAL_NAME
import com.example.recipeapp.fragments.HomeFragment.Companion.MEAL_THUMB
import com.example.recipeapp.viewModel.HomeViewModel
import com.google.android.material.snackbar.Snackbar


class FavouritesFragment : Fragment() {
    private lateinit var binding: FragmentFavouritesBinding
    private lateinit var viewModel: HomeViewModel
    private lateinit var favouritesAdapter: MealsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = (activity as MainActivity).viewModel
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentFavouritesBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        prepareRecyclerView()
        observeFavourites()




        val itemTouchHelper = object : ItemTouchHelper.SimpleCallback(
            ItemTouchHelper.UP or ItemTouchHelper.DOWN,
            ItemTouchHelper.RIGHT or ItemTouchHelper.LEFT
        ) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ) = true

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                val deletedMeal = favouritesAdapter.differ.currentList.getOrNull(position)

                if (deletedMeal != null) {
                    viewModel.deleteMeal(deletedMeal)
                    Snackbar.make(requireView(), "Meal Deleted", Snackbar.LENGTH_LONG).setAction(
                        "Undo"
                    ) {
                        viewModel.insertMeal(deletedMeal)
                    }.show()
                } else {
                    Toast.makeText(requireContext(), "Meal not Found", Toast.LENGTH_SHORT).show()
                }
            }

        }
        ItemTouchHelper(itemTouchHelper).attachToRecyclerView(binding.rvFavourites)
    }


        private fun prepareRecyclerView() {
            favouritesAdapter = MealsAdapter()
            binding.rvFavourites.apply {
                layoutManager = GridLayoutManager(context, 2, GridLayoutManager.VERTICAL, false)
                adapter = favouritesAdapter
            }

            favouritesAdapter.setOnItemClickListener { meal ->
                val intent = Intent(activity, MealActivity::class.java).apply {
                    putExtra(MEAL_ID, meal.idMeal)
                    putExtra(MEAL_NAME, meal.strMeal)
                    putExtra(MEAL_THUMB, meal.strMealThumb)
                }
                startActivity(intent)
            }
        }

        private fun observeFavourites() {
            viewModel.observeFavouritesMealsLiveData()
                .observe(requireActivity(), Observer { meals ->
                    favouritesAdapter.differ.submitList(meals)
                })
        }

    }



