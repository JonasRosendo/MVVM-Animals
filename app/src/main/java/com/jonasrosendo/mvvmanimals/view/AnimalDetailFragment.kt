package com.jonasrosendo.mvvmanimals.view


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation

import com.jonasrosendo.mvvmanimals.R
import kotlinx.android.synthetic.main.fragment_animal_detail.*

class AnimalDetailFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_animal_detail, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        fabList.setOnClickListener {
            val action = AnimalDetailFragmentDirections.actionAnimalDetailFragmentToAnimalListFragment()
            Navigation.findNavController(it).navigate(action)
        }
    }


}
