package com.weather.app.presentation.mainscreen

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.weather.app.R
import com.weather.app.databinding.BaseWeatherListItemBinding
import com.weather.app.databinding.HeadWeatherListItemBinding
import com.weather.app.domain.model.CurrentWeatherDomainModel
import com.weather.app.domain.model.DailyWeatherDomainModel
import com.weather.app.presentation.textOrGone
import kotlinx.android.extensions.LayoutContainer
import java.text.SimpleDateFormat
import java.util.*

class WeatherAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var currentWeather: CurrentWeatherDomainModel = CurrentWeatherDomainModel()
    private var dailyWeather: DailyWeatherDomainModel = DailyWeatherDomainModel()
    private val sdf = SimpleDateFormat("dd.MM.yy", Locale.US)

    fun bind(currentWeather: CurrentWeatherDomainModel, dailyWeather: DailyWeatherDomainModel) {
        this.currentWeather = currentWeather
        this.dailyWeather = dailyWeather
        notifyDataSetChanged()
    }

    private inner class HeaderViewHolder(override val containerView: View) :
        RecyclerView.ViewHolder(containerView), LayoutContainer {
        private val vb = HeadWeatherListItemBinding.bind(containerView)

        fun bind(currentWeather: CurrentWeatherDomainModel) {
            vb.temperature.textOrGone = currentWeather.currentTemperature?.toString()
            vb.city.textOrGone = currentWeather.city
        }
    }

    private inner class BaseViewHolder(override val containerView: View) :
        RecyclerView.ViewHolder(containerView), LayoutContainer {
        private val ctx: Context = containerView.context
        private val vb = BaseWeatherListItemBinding.bind(containerView)
        fun bind(day: DailyWeatherDomainModel.Day) {
            vb.date.textOrGone = getNormalDate(day.date)
            vb.temperature.textOrGone = day.dayTemperature?.let {
                ctx.getString(R.string.celsius, it.toString())
            }
            vb.pressure.textOrGone =  day.pressure?.let {
                ctx.getString(R.string.pressure, it.toString())
            }
            vb.humidity.textOrGone = day.humidity?.let {
                ctx.getString(R.string.humidity, it.toString())
            }
        }
    }

    private fun getNormalDate(date: Long?): String? {
        date ?: return null
        val calendar = Calendar.getInstance()
        val dateInMilliseconds = date * 1000
        calendar.timeInMillis = dateInMilliseconds
        return sdf.format(calendar.time)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            HEADER_ID -> {
                HeaderViewHolder(
                    LayoutInflater.from(parent.context).inflate(
                        R.layout.head_weather_list_item,
                        parent,
                        false
                    )
                )
            }
            else -> {
                BaseViewHolder(
                    LayoutInflater.from(parent.context).inflate(
                        R.layout.base_weather_list_item,
                        parent,
                        false
                    )
                )
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is HeaderViewHolder) {
            holder.bind(currentWeather)
        }
        if (holder is BaseViewHolder) {
            holder.bind(dailyWeather.days?.get(position)!!)
        }
    }

    override fun getItemCount(): Int {
        return dailyWeather.days?.size ?: 0 + 1
    }

    override fun getItemViewType(position: Int): Int {
        return if (position == 0) {
            HEADER_ID
        } else {
            BASE_ID
        }
    }

    companion object {
        const val HEADER_ID = 0
        const val BASE_ID = 1
    }
}