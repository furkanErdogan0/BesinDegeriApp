package com.furkanerdogan.besindegeriuygulamasi.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.furkanerdogan.besindegeriuygulamasi.adapter.BesinRecyclerAdapter
import com.furkanerdogan.besindegeriuygulamasi.databinding.FragmentBesinDetayBinding
import com.furkanerdogan.besindegeriuygulamasi.databinding.FragmentBesinListeBinding
import com.furkanerdogan.besindegeriuygulamasi.service.BesinAPI
import com.furkanerdogan.besindegeriuygulamasi.viewmodel.BesinListeViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create


class BesinListeFragment : Fragment() {


    private var _binding: FragmentBesinListeBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: BesinListeViewModel

    private val besinRecyclerAdapter = BesinRecyclerAdapter(arrayListOf())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentBesinListeBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(this)[BesinListeViewModel::class.java]
        viewModel.refreshData()

        binding.besinRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.besinRecyclerView.adapter = besinRecyclerAdapter

        binding.swipeRefreshLayout.setOnRefreshListener {
            binding.besinRecyclerView.visibility = View.GONE
            binding.besinHataMesaji.visibility = View.GONE
            binding.besinProgressBar.visibility = View.VISIBLE
            viewModel.refreshDataFromInternet()
            binding.swipeRefreshLayout.isRefreshing = false
        }

        observeLiveData()
    }

    private fun observeLiveData() {

        viewModel.besinler.observe(viewLifecycleOwner) {
            //adapter
            besinRecyclerAdapter.besinListesiniGuncelle(it)
            binding.besinRecyclerView.visibility = View.VISIBLE

        }

        viewModel.besinHataMesaji.observe(viewLifecycleOwner) {

            if(it) {
                binding.besinHataMesaji.visibility = View.VISIBLE
                binding.besinRecyclerView.visibility = View.GONE
            } else {
                binding.besinHataMesaji.visibility = View.GONE
            }

        }

        viewModel.besinYukleniyor.observe(viewLifecycleOwner) {

            if(it) {
                binding.besinHataMesaji.visibility = View.GONE
                binding.besinRecyclerView.visibility = View.GONE
                binding.besinProgressBar.visibility = View.VISIBLE
            } else {
                binding.besinProgressBar.visibility = View.GONE
            }

        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}