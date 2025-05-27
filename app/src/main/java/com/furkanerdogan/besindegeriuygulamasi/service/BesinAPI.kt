package com.furkanerdogan.besindegeriuygulamasi.service

import com.furkanerdogan.besindegeriuygulamasi.model.Besin
import retrofit2.http.GET

interface BesinAPI {

    // api.google.com/updatedb
    // api.google.com/readusers
    // api.google.com/createuser   //bu tip bir API miz olduğunda base url i tekrar yazmayız. sadece istediğimiz fonksiyonları endpoint olarak yazarız. modülerlik sağlar.

    //BASE URL : https://raw.githubusercontent.com/
    //ENDPOINT : atilsamancioglu/BTK20-JSONVeriSeti/refs/heads/master/besinler.json

    @GET("atilsamancioglu/BTK20-JSONVeriSeti/refs/heads/master/besinler.json")
    suspend fun getBesin() : List<Besin>



}