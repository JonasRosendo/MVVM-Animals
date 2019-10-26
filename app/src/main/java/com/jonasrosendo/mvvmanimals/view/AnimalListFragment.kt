package com.jonasrosendo.mvvmanimals.view


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager

import com.jonasrosendo.mvvmanimals.R
import com.jonasrosendo.mvvmanimals.model.model.Animal
import com.jonasrosendo.mvvmanimals.viewmodel.AnimalListViewModel
import kotlinx.android.synthetic.main.fragment_animal_list.*

class AnimalListFragment : Fragment() {

    private var  animalAdapter = AnimalAdapter(arrayListOf())
    private lateinit var viewModel: AnimalListViewModel

    private var animalListObserver = Observer<List<Animal>> { animals ->
        animals?.let {
            rvAnimals.visibility = View.VISIBLE
            animalAdapter.update(it)
        }
    }

    private var loadingObserver = Observer<Boolean> { isLoading ->
        pbAnimalsList.visibility = if(isLoading) View.VISIBLE else View.GONE
        if(isLoading){
            rvAnimals.visibility = View.GONE
            tvErrorMessage.visibility = View.GONE
        }
    }

    private var loadingErrorObserver = Observer<Boolean> { hasError ->
        tvErrorMessage.visibility = if(hasError) View.VISIBLE else View.GONE
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_animal_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProviders.of(this).get(AnimalListViewModel::class.java)
        viewModel.animals.observe(this, animalListObserver)
        viewModel.isLoading.observe(this, loadingObserver)
        viewModel.hasLoadError.observe(this, loadingErrorObserver)
        viewModel.refresh()

        rvAnimals.apply {
            layoutManager = GridLayoutManager(context, 2)
            adapter = animalAdapter
        }

        swipeRefreshAnimals.setOnRefreshListener {
            rvAnimals.visibility = View.GONE
            tvErrorMessage.visibility = View.GONE
            pbAnimalsList.visibility = View.VISIBLE
            viewModel.refresh()
            swipeRefreshAnimals.isRefreshing = false
        }
    }
}
