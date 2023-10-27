package edu.vt.cs3714.retrofitrecyclerviewguide

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity(tableName = "movie_table")
data class MovieItem(@PrimaryKey @ColumnInfo(name ="id") var id: Long,
                     @ColumnInfo(name ="vote_count") var vote_count: Long,
                     @ColumnInfo(name ="vote_average") var vote_average: Float,
                     @ColumnInfo(name ="title") var title: String,
                     @ColumnInfo(name ="popularity") var popularity: Float,
                     @ColumnInfo(name ="poster_path") var poster_path: String,
                     @ColumnInfo(name ="overview") var overview: String,
                     @ColumnInfo(name ="release_date") var release_date: Date

)

//Example JSON record from MovieDB
/*
*
{
  "adult": false,
  "backdrop_path": "/iGCHoFp4VUwMoolO2C2u12AgiKl.jpg",
  "belongs_to_collection": null,
  "budget": 0,
  "genres": [
    {
      "id": 18,
      "name": "Drama"
    }
  ],
  "homepage": null,
  "id": 515916,
  "imdb_id": "tt8254556",
  "original_language": "nl",
  "original_title": "Girl",
  "overview": "A 15-year-old girl, born in a boy's body, dreams of becoming a ballerina and will push her body to its limits in order for her dream to succeed.",
  "popularity": 31.534,
  "poster_path": "/a6WKjZ1eHrKV8u1DYqjW4yUPuC.jpg",
  "release_date": "2018-09-27",
  "revenue": 0,
  "runtime": 105,
  "spoken_languages": [
    {
      "iso_639_1": "nl",
      "name": "Nederlands"
    },
    {
      "iso_639_1": "en",
      "name": "English"
    },
    {
      "iso_639_1": "fr",
      "name": "Français"
    }
  ],
  "status": "Released",
  "tagline": "",
  "title": "Girl",
  "video": false,
  "vote_average": 7.7,
  "vote_count": 140
}
* */
