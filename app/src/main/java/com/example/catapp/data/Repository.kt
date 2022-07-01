package com.example.catapp.data

import androidx.annotation.WorkerThread
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.abstractions.CatPhoto
import com.example.api.api.RetrofitInstance
import com.example.database.daos.CatDao
import kotlinx.coroutines.flow.Flow
import okhttp3.ResponseBody

class Repository(private val catDao: CatDao?) {
    val getAllCats: Flow<PagingData<CatPhoto>> =
        Pager(
            PagingConfig(
                pageSize = 5,
                enablePlaceholders = false,
                initialLoadSize = 5
            ),
        ) {
            CatPagingSource(catDao!!)
        }.flow

    @WorkerThread
    suspend fun insertInDatabase(cat: CatPhoto?) {
        catDao?.insertCatInDB(cat)
    }

    @WorkerThread
    suspend fun deleteInDatabase(cat: CatPhoto?) {
        catDao?.deleteCatInDB(cat)
    }

    suspend fun getFromApiImage(): ResponseBody {
        return RetrofitInstance.API.getImage()
    }
}
