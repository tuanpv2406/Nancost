package com.example.nancost.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.nancost.data.NancostDatabase
import com.example.nancost.data.repository.NancostRepository
import com.example.nancost.model.Nancost
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class NancostViewModel(application: Application): AndroidViewModel(application) {

    val readAllData: LiveData<ArrayList<Nancost>>
    private val repository: NancostRepository

    init {
        val nancostDao = NancostDatabase.getDatabase(application).nancostDao()
        repository = NancostRepository(nancostDao)
        readAllData = repository.readAllData
    }
    fun getAll() {
        viewModelScope.launch(Dispatchers.IO) {
            repository.getAll()
        }
    }

    fun addNancost(nancost: Nancost) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.add(nancost)
        }
    }

    fun updateNancost(nancost: Nancost) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.update(nancost)
        }
    }

    fun deleteNancost(id: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.delete(id)
        }
    }
}