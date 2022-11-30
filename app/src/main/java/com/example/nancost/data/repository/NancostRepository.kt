package com.example.nancost.data.repository

import androidx.lifecycle.LiveData
import com.example.nancost.data.NancostDao
import com.example.nancost.model.Nancost

class NancostRepository(private val nancostDao: NancostDao) {
    val readAllData: LiveData<ArrayList<Nancost>> = nancostDao.readAllData()

    suspend fun add(nancost: Nancost) =
        nancostDao.add(nancost)

    suspend fun getAll(): ArrayList<Nancost> =
        nancostDao.getAll()

    suspend fun getByName(name: String): Nancost =
        nancostDao.getByName(name)

    suspend fun update(nancost: Nancost) =
        nancostDao.update(nancost)

    suspend fun delete(id: Int) =
        nancostDao.delete(id)
}