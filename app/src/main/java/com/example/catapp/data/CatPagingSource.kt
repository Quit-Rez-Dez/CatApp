package com.example.catapp.data

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.abstractions.CatPhoto
import com.example.database.daos.CatDao
import retrofit2.HttpException
import java.io.IOException

private const val STARTING_PAGE_INDEX = 1

class CatPagingSource(private val service: CatDao) : PagingSource<Int, CatPhoto>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, CatPhoto> {
        val page = params.key ?: STARTING_PAGE_INDEX
        return try {
            val response = service.getCatsFromDB(params.loadSize, page * params.loadSize)
            Log.d("gatinho numerooo:", (page).toString())
            LoadResult.Page(
                data = response,
                prevKey = if (page == STARTING_PAGE_INDEX) null else page - 1,
                nextKey = if (response.isEmpty()) null else page + 1
            )
        } catch (exception: IOException) {
            Log.d("Error kekw", exception.toString())
            return LoadResult.Error(exception)
        } catch (exception: HttpException) {
            Log.d("Error kekw", exception.toString())
            return LoadResult.Error(exception)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, CatPhoto>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }
}