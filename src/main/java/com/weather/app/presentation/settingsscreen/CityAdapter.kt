package com.weather.app.presentation.settingsscreen

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.weather.app.R
import com.weather.app.databinding.BaseCityListItemBinding
import com.weather.app.domain.model.City
import kotlinx.android.extensions.LayoutContainer

class CityAdapter(private val callback: CityCallback) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var cityList: List<City> = emptyList()

    fun bind(cityList: List<City>) {
        this.cityList = cityList
        notifyDataSetChanged()
    }

    private inner class CityViewHolder(override val containerView: View) :
        RecyclerView.ViewHolder(containerView), LayoutContainer {
        private val vb = BaseCityListItemBinding.bind(containerView)
        fun bind(city: City) {
            vb.city.text = city.name
            vb.city.isChecked = city.isCurrent
            vb.city.setOnClickListener {
                callback.selectCity(city.name)
            }
            vb.remove.setOnClickListener {
                callback.removeCity(city.name)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return CityViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.base_city_list_item,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is CityViewHolder) {
            holder.bind(cityList[position])
        }
    }

    override fun getItemCount(): Int {
        return cityList.size
    }

    interface CityCallback {
        fun selectCity(name: String)
        fun removeCity(name: String)
    }

}