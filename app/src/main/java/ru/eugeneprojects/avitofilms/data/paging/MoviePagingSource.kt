package ru.eugeneprojects.avitofilms.data.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import retrofit2.HttpException
import ru.eugeneprojects.avitofilms.data.models.Movie
import ru.eugeneprojects.avitofilms.data.network.repository.MoviesRepository
import ru.eugeneprojects.avitofilms.utils.Constants

class MoviePagingSource (
    private val productsRepository: MoviesRepository,
    private val query: String
) : PagingSource<Int, Movie>() {
    override fun getRefreshKey(state: PagingState<Int, Movie>): Int? {
        val anchorPosition = state.anchorPosition ?: return null
        val page = state.closestPageToPosition(anchorPosition) ?: return null
        return page.prevKey?.plus(1) ?: page.nextKey?.minus(1)
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Movie> {

        val pageNumber = params.key ?: INITIAL_PAGE_NUMBER
        val pageSize: Int = params.loadSize.coerceAtMost(Constants.PAGE_SIZE)
        val skip = pageNumber * pageSize

        try {
            val response = productsRepository.getMovies(query)

            if (response.isSuccessful) {
                val products = checkNotNull(response.body()).movies.map { movie ->
                    Movie(
                        movie.ageRating,
                        movie.alternativeName,
                        movie.budget,
                        movie.countries,
                        movie.description,
                        movie.enName,
                        movie.genres,
                        movie.id,
                        movie.movieLength,
                        movie.name,
                        movie.poster,
                        movie.rating,
                        movie.slogan,
                        movie.year
                    )
                }

                val nextPageNumber = if (skip + products.size >= (response.body()?.total ?: pageSize)) null
                else pageNumber + 1
                val prevPageNumber = if (pageNumber == 0) null else pageNumber - 1

                return LoadResult.Page(products, prevPageNumber, nextPageNumber)
            } else {
                return LoadResult.Error(HttpException(response))
            }
        } catch (e: Exception) {
            return LoadResult.Error(e)
        }
    }

    companion object {
        const val INITIAL_PAGE_NUMBER = 0
    }
}