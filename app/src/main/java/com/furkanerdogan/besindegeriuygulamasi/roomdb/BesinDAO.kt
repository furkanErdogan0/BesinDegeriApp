package com.furkanerdogan.besindegeriuygulamasi.roomdb

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.furkanerdogan.besindegeriuygulamasi.model.Besin

@Dao
interface BesinDAO {

    @Insert                 //vararg yapınca birden fazla değişken alabiliyor.
    suspend fun insertAll(vararg besin : Besin) : List<Long>
    //eklediği besinlerin id sini long listesi halinde geri veriyor.

    @Query("SELECT * FROM besin")
    suspend fun getAll() : List<Besin>

    @Query("SELECT * FROM besin WHERE uuid = :besinId")
    suspend fun getBesin(besinId : Int) : Besin

    @Query("DELETE FROM besin")
    suspend fun deleteAll()

}