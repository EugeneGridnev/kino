package ru.eugeneprojects.avitofilms.data.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import retrofit2.HttpException
import retrofit2.Response
import ru.eugeneprojects.avitofilms.data.models.movieCardItem.PageResponse
import ru.eugeneprojects.avitofilms.utils.Constants

abstract class BasePagingSource<T : Any> () : PagingSource<Int, T>() {
    override fun getRefreshKey(state: PagingState<Int, T>): Int? {
        val anchorPosition = state.anchorPosition ?: return null
        val page = state.closestPageToPosition(anchorPosition) ?: return null
        return page.prevKey?.plus(1) ?: page.nextKey?.minus(1)
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, T> {
        val pageNumber = params.key ?: INITIAL_PAGE_NUMBER
        val pageSize: Int = params.loadSize.coerceAtMost(Constants.PAGE_SIZE)

        try {
            val response = requestPage(pageNumber, pageSize)

            return if (response.isSuccessful) {
                val responseBody = checkNotNull(response.body())

                val nextPageNumber = if (responseBody.pages <= pageNumber) null else pageNumber + 1
                val prevPageNumber = if (pageNumber > 1) pageNumber - 1 else null

                LoadResult.Page(responseBody.docs, prevPageNumber, nextPageNumber)
            } else {
                LoadResult.Error(HttpException(response))
            }
        } catch (e: Exception) {
            return LoadResult.Error(e)
        }
    }

    abstract suspend fun requestPage(page: Int, size: Int) : Response<PageResponse<T>>

    companion object {
        const val INITIAL_PAGE_NUMBER = 1
    }
}