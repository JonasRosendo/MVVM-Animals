package com.jonasrosendo.mvvmanimals.view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.jonasrosendo.mvvmanimals.R
import com.jonasrosendo.mvvmanimals.model.model.Animal
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.adapter_animals.view.*

class AnimalAdapter(private val animals : ArrayList<Animal>) : RecyclerView.Adapter<AnimalAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.adapter_animals, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount() = animals.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val animal = animals[position]
        holder.containerView?.tvAnimalName?.text = animal.name
    }

    fun update(newAnimalList : List<Animal>){
        animals.clear()
        animals.addAll(newAnimalList)
        notifyDataSetChanged()
    }

    class ViewHolder(override val containerView: View?) : RecyclerView.ViewHolder(containerView!!), LayoutContainer {}
}