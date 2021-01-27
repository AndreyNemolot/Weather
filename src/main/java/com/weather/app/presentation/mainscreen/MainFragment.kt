package com.weather.app.presentation.mainscreen

import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.core.app.ActivityCompat
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.weather.app.R
import com.weather.app.databinding.MainFragmentBinding
import com.weather.app.presentation.observeOn
import kotlinx.android.synthetic.main.main_fragment.view.*
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.buffer
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.launch

@InternalCoroutinesApi
class MainFragment : Fragment(R.layout.main_fragment) {

    private lateinit var viewModel: MainViewModel
    private lateinit var binding: MainFragmentBinding
    private lateinit var navController: NavController
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private val adapter = WeatherAdapter()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = MainFragmentBinding.bind(view)
        setHasOptionsMenu(true)
        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)
        navController = NavHostFragment.findNavController(this)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireContext())

        viewModel.stateFlow.observeOn(viewLifecycleOwner) {
            updateState(it)
        }
        viewModel.command.observeOn(viewLifecycleOwner) {
            handleCommand(it)
        }

        binding.listView.adapter = adapter
        binding.listView.addItemDecoration(MarginDecoration(ITEM_MARGIN))
        binding.listView.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        binding.swipeToRefresh.setOnRefreshListener {
            viewModel.refresh(false)
            binding.swipeToRefresh.isRefreshing = false
        }

        binding.error.refresh.setOnClickListener {
            viewModel.refresh(true)
        }

        binding.addCity.setOnClickListener {
            showAddCityDialog()
        }

        binding.useLocation.setOnClickListener {
            getLocation()
        }

    }

    private fun handleCommand(command: MainCommand) {
        when (command) {
            is MainCommand.RequestCoordinates -> {
                getLocation()
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.main, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
        R.id.action_settings -> {
            navController.navigate(R.id.settingsFragment)
            true
        }
        else -> {
            super.onOptionsItemSelected(item)
        }
    }

    private fun getLocation() {
        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            val permissions = arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            )
            requestPermissions(permissions, LOCATION_PERMISSION_REQUEST_CODE)
            return
        }
        fusedLocationClient.lastLocation
            .addOnSuccessListener { location: Location? ->
                viewModel.loadByLocation(location?.latitude, location?.longitude, true)
            }
    }

    private fun updateState(state: State) {
        val currentState = state.weatherScreenState
        when(currentState) {
            is WeatherScreenState.Success -> {
                adapter.bind(currentState.currentWeather, currentState.dailyWeather)
                binding.content.isVisible = true
                binding.progress.isVisible = false
                binding.error.isVisible = false
                binding.newCity.isVisible = false
            }
            is WeatherScreenState.Loading -> {
                binding.progress.isVisible = true
                binding.content.isVisible = false
                binding.error.isVisible = false
                binding.newCity.isVisible = false
            }
            is WeatherScreenState.NewCityAdd -> {
                binding.newCity.isVisible = true
                binding.progress.isVisible = false
                binding.content.isVisible = false
                binding.error.isVisible = false
            }
            is WeatherScreenState.Error -> {
                binding.error.errorMessage.text = getString(R.string.error)
                binding.error.isVisible = true
                binding.newCity.isVisible = false
                binding.progress.isVisible = false
                binding.content.isVisible = false
            }
        }

    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if ((grantResults.isNotEmpty() &&
                        grantResults[0] == PackageManager.PERMISSION_GRANTED)
            ) {
                getLocation()
            }
        }
    }

    private fun showAddCityDialog() {
        navController.navigate(R.id.settingsFragment)
    }

    companion object {
        private const val LOCATION_PERMISSION_REQUEST_CODE = 1
        private const val ITEM_MARGIN = 10
    }

}