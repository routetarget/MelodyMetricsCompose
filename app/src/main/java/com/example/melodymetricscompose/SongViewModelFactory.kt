package com.example.melodymetricscompose

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.melodymetricscompose.db.SongDao
import java.lang.IllegalArgumentException

class SongViewModelFactory (
    private val dao: SongDao
): ViewModelProvider.Factory{
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(SongViewModel::class.java)){
            return SongViewModel(dao) as T
        }
        throw  IllegalArgumentException("Unknown ViewModelClass")
    }


}