package com.example.recipeapp.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.recipeapp.R
import com.example.recipeapp.activities.MainActivity
import com.example.recipeapp.adapters.MealsAdapter
import com.example.recipeapp.databinding.FragmentSearchBinding
import com.example.recipeapp.viewModel.HomeViewModel


class SearchFragment : Fragment() {
    private lateinit var binding: FragmentSearchBinding
    private lateinit var viewModel: HomeViewModel
    private lateinit var  searchRecyclerViewAdapter : MealsAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = (activity as MainActivity).viewModel

        }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSearchBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        prepareRecyclerView()
        binding.imgSearchArrow.setOnClickListener{
            searchMeals()
        }

        observeSearchedMealsLiveData()
    }

    private fun observeSearchedMealsLiveData() {
        viewModel.observeSearchMealsLiveData().observe(viewLifecycleOwner,Observer{mealsList->

            searchRecyclerViewAdapter.differ.submitList(mealsList)
        })
    }

    private fun searchMeals() {
        val searchQuery = binding.edSearchBox.text.toString()
        if (searchQuery.isNotEmpty()){
            viewModel.searchMeals(searchQuery)
        }
    }

    private fun prepareRecyclerView() {
        searchRecyclerViewAdapter = MealsAdapter()
        binding.rvSearchedMeals.apply {
            layoutManager = GridLayoutManager(context,2,GridLayoutManager.VERTICAL,false)
            adapter= searchRecyclerViewAdapter
        }
    }

}