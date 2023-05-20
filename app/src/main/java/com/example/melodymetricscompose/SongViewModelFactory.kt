package com.example.melodymetricscompose

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.melodymetricscompose.db.SongDao
import java.lang.IllegalArgumentException
class SongViewModelFactory (
    private val dao: SongDao,
    private val dataStore: DataStore<Preferences>
): ViewModelProvider.Factory{
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(SongViewModel::class.java)){
            return SongViewModel(dao, dataStore) as T
        }
        throw  IllegalArgumentException("Unknown ViewModelClass")
    }
}
