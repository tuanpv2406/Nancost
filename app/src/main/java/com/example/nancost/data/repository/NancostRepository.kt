package com.example.nancost.data.repository

import android.annotation.SuppressLint
import android.app.Application
import androidx.lifecycle.LiveData
import com.example.nancost.data.NancostDao
import com.example.nancost.data.NancostDatabase
import com.example.nancost.model.Nancost
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob


class NancostRepository(private val nancostDao: NancostDao) {
    val readAllData: LiveData<List<Nancost>> = nancostDao.readAllData()

    suspend fun add(nancost: Nancost) =
        nancostDao.add(nancost)

    suspend fun getByName(name: String): Nancost =
        nancostDao.getByName(name)

    suspend fun update(nancost: Nancost) =
        nancostDao.update(nancost)

    suspend fun delete(id: Int) =
        nancostDao.delete(id)
}