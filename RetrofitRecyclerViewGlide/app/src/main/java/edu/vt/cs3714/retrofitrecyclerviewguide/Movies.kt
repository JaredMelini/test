package edu.vt.cs3714.retrofitrecyclerviewguide

data class Movies(
    val results: List<MovieItem>,
    val total_pages: Int,
    val page: Int
)
