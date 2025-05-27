package com.furkanerdogan.besindegeriuygulamasi.viewmodel

import android.app.Application
import android.view.View
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel //getApplication() u alabilmek için AndroidViewModel ı kullanıyoruz.
import androidx.lifecycle.viewModelScope
import com.furkanerdogan.besindegeriuygulamasi.model.Besin
import com.furkanerdogan.besindegeriuygulamasi.roomdb.BesinDatabase
import com.furkanerdogan.besindegeriuygulamasi.service.BesinAPIService
import com.furkanerdogan.besindegeriuygulamasi.util.OzelSharedPreferences
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class BesinListeViewModel (application: Application) : AndroidViewModel(application) {

    val besinler = MutableLiveData<List<Besin>>()
    val besinHataMesaji = MutableLiveData<Boolean>()
    val besinYukleniyor = MutableLiveData<Boolean>()

    private val besinAPIService = BesinAPIService()
    private val ozelSharedPreferences = OzelSharedPreferences(getApplication())

    private val guncellemeZamani = 1 * 60 * 1000 * 1000 * 1000L   //10 dk. değiştirmek için ör/baştakini 1 yap = 1dk.

    fun refreshData() {
        val kaydedilmeZamani = ozelSharedPreferences.zamaniAl()

        if(kaydedilmeZamani != null && kaydedilmeZamani != 0L && System.nanoTime() - kaydedilmeZamani < guncellemeZamani) {
            verileriRoomdanAl()
        } else {
            verileriInternettenAl()
        }
    }

    fun refreshDataFromInternet() {
        verileriInternettenAl()
    }

    private fun verileriRoomdanAl() {
        besinYukleniyor.value = true

        viewModelScope.launch(Dispatchers.IO) {
            val besinListesi = BesinDatabase(getApplication()).besinDao().getAll()

            withContext(Dispatchers.Main) {
                besinleriGoster(besinListesi)
                Toast.makeText(getApplication(), "Besinleri Room'dan aldık.", Toast.LENGTH_LONG).show()
            }
        }
    }


    private fun verileriInternettenAl() {
        besinYukleniyor.value = true

        //dispatchers.io dememize gerek yok. //normalde CoroutineScope oluştururduk ancak viewmodel a özel bir fonksiyon yapmışlar. direkt böyle alabiliyoruz asenkron olarak.
        viewModelScope.launch(Dispatchers.IO) {
            val besinListesi = besinAPIService.getData()

            withContext(Dispatchers.Main) {    //Coroutine'ler için yerleşik desteğiyle Kotlin, geliştiricilere eşzamansız işlemleri sorunsuz bir şekilde ele almaları için güçlü araçlar sağlar. Bu araçlardan biri withContext, geliştiricilerin bir coroutine içindeki yürütme bağlamını değiştirmelerine olanak tanıyan çok yönlü bir işlevdir.
                besinYukleniyor.value = false
                roomaKaydet(besinListesi)
                Toast.makeText(getApplication(), "Besinleri Internetten aldık.", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun besinleriGoster (besinListesi: List<Besin>) {

        besinler.value = besinListesi
        besinHataMesaji.value = false
        besinYukleniyor.value = false

    }

    private fun roomaKaydet(besinListesi: List<Besin>) {

        viewModelScope.launch {

            val dao = BesinDatabase(getApplication()).besinDao()
            dao.deleteAll()
            val uuidListesi = dao.insertAll(*besinListesi.toTypedArray()) //for loop yapmadan listeyi tek tek gönderdik. //geriye id listesi döner.(long olarak)
            var i = 0
            while (i < besinListesi.size) {
                besinListesi[i].uuid = uuidListesi[i].toInt()
                i = i + 1
            }

            besinleriGoster(besinListesi)
        }

        ozelSharedPreferences.zamaniKaydet(System.nanoTime()) //nanotime saniyenin milyarda biridir. bunu normale çevireceğiz.saniyeden sonra milyar ile çarpacağız.
    }


}