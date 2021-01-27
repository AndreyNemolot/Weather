package com.weather.app.presentation.settingsscreen

import android.graphics.Rect
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.weather.app.R
import com.weather.app.databinding.AddCityDialogBinding
import com.weather.app.databinding.SettingsFragmentBinding
import com.weather.app.presentation.observeOn

class SettingsFragment : Fragment(), CityAdapter.CityCallback {

    private lateinit var binding: SettingsFragmentBinding
    private lateinit var viewModel: SettingsViewModel
    private var adapter: CityAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = SettingsFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this).get(SettingsViewModel::class.java)
        adapter = CityAdapter(this)

        binding.listView.adapter = adapter
        binding.listView.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        binding.listView.addItemDecoration(object: RecyclerView.ItemDecoration() {
            override fun getItemOffsets(
                outRect: Rect,
                view: View,
                parent: RecyclerView,
                state: RecyclerView.State
            ) {
                outRect.top = 10
                outRect.bottom = 10
                outRect.right = 20
                outRect.left = 20
            }
        })

        viewModel.stateFlow.observeOn(viewLifecycleOwner) {
            updateState(it)
        }

        binding.addCity.setOnClickListener {
            createDialog()
        }
    }

    override fun onDestroyView() {
        adapter = null
        super.onDestroyView()
    }

    private fun updateState(state: State) {
        val currentState = state.currentState
        when(currentState) {
            is CityScreenState.Success -> {
                adapter?.bind(currentState.cityList)
            }
        }
    }

    private fun createDialog() {
        requireActivity().let {
            val builder = AlertDialog.Builder(it)
            val inflater = it.layoutInflater
            val vb = AddCityDialogBinding.bind(inflater.inflate(R.layout.add_city_dialog, null))
            builder.setView(vb.root)
                .setPositiveButton(R.string.add_city) { dialog, id ->
                    viewModel.addCity(vb.cityName.editableText.toString())
                }
                .setNegativeButton(R.string.cancel) { dialog, id ->
                    dialog.cancel()
                }
            builder.create().show()
        }
    }

    override fun selectCity(name: String) {
        viewModel.selectCity(name)
    }

    override fun removeCity(name: String) {
        viewModel.removeCity(name)
    }

}