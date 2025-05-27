package com.furkanerdogan.besindegeriuygulamasi.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.furkanerdogan.besindegeriuygulamasi.databinding.FragmentBesinDetayBinding
import com.furkanerdogan.besindegeriuygulamasi.util.gorselIndir
import com.furkanerdogan.besindegeriuygulamasi.util.placeHolderYap
import com.furkanerdogan.besindegeriuygulamasi.viewmodel.BesinDetayViewModel

class BesinDetayFragment : Fragment() {

    private var _binding: FragmentBesinDetayBinding? = null
    private val binding get() = _binding!!

    var besinId = 0

    private lateinit var viewModel: BesinDetayViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentBesinDetayBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(this) [BesinDetayViewModel::class.java]

        arguments?.let {
            besinId = BesinDetayFragmentArgs.fromBundle(it).besinId
        }

        viewModel.roomVerisiniAl(besinId)

        observeLiveData()

    }

    private fun observeLiveData() {
        viewModel.besinLiveData.observe(viewLifecycleOwner) {
            binding.besinIsim.text = it.besinIsim
            binding.besinKalori.text = it.besinKalori
            binding.besinProtein.text = it.besinProtein
            binding.besinYag.text = it.besinYag
            binding.besinKarbonhidrat.text = it.besinKarbonhidrat
            binding.besinImage.gorselIndir(it.besinGorsel, placeHolderYap(requireContext()))
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}