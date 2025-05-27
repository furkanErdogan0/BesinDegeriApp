package com.furkanerdogan.besindegeriuygulamasi.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.furkanerdogan.besindegeriuygulamasi.databinding.RecyclerRowBinding
import com.furkanerdogan.besindegeriuygulamasi.model.Besin
import com.furkanerdogan.besindegeriuygulamasi.util.gorselIndir
import com.furkanerdogan.besindegeriuygulamasi.util.placeHolderYap
import com.furkanerdogan.besindegeriuygulamasi.view.BesinListeFragment
import com.furkanerdogan.besindegeriuygulamasi.view.BesinListeFragmentDirections

class BesinRecyclerAdapter(val besinListesi : ArrayList<Besin>) : RecyclerView.Adapter<BesinRecyclerAdapter.BesinViewHolder>() {

    class BesinViewHolder(val binding : RecyclerRowBinding) : RecyclerView.ViewHolder(binding.root) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BesinViewHolder {
        val binding = RecyclerRowBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return BesinViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return besinListesi.size
    }

    fun besinListesiniGuncelle(yeniBesinListesi: List<Besin>) {
        besinListesi.clear()
        besinListesi.addAll(yeniBesinListesi)
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: BesinViewHolder, position: Int) {
        holder.binding.isim.text = besinListesi[position].besinIsim
        holder.binding.kalori.text = besinListesi[position].besinKalori

        holder.itemView.setOnClickListener {
            val action = BesinListeFragmentDirections.actionBesinListeFragmentToBesinDetayFragment(besinListesi[position].uuid)
            Navigation.findNavController(it).navigate(action)
        }

        holder.binding.imageView.gorselIndir(besinListesi[position].besinGorsel, placeHolderYap(holder.itemView.context))

    }

}