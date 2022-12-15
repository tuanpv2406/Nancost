package com.example.nancost.viewmodel

import android.app.Application
import androidx.lifecycle.*
import com.example.nancost.data.NancostDatabase
import com.example.nancost.data.repository.NancostRepository
import com.example.nancost.model.Nancost
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.Flow

class NancostViewModel(private val repository: NancostRepository) : ViewModel() {

    val allNancost: LiveData<List<Nancost>> = repository.readAllData

    fun addNancost(nancost: Nancost) {
        viewModelScope.launch(Dispatchers.IO) {
            nancost.getRemainingVolume()
            nancost.getAmountPay()
            repository.add(nancost)
        }
    }

    fun updateNancost(nancost: Nancost) {
        viewModelScope.launch(Dispatchers.IO) {
            nancost.getRemainingVolume()
            nancost.getAmountPay()
            repository.update(nancost)
        }
    }

    fun deleteNancost(id: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.delete(id)
        }
    }
}

class NancostViewModelFactory constructor(private val repository: NancostRepository): ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(NancostViewModel::class.java)) {
            NancostViewModel(this.repository) as T
        } else {
            throw IllegalArgumentException("ViewModel Not Found")
        }
    }
}