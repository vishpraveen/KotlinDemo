package com.example.magneto.kotlindemo.bean

import com.google.gson.annotations.SerializedName

class MovieBean {
    @SerializedName("Title")
    public lateinit var Title:String
    @SerializedName("Year")
    public lateinit var Year:String
    @SerializedName("Rated")
    public lateinit var Rated:String
    @SerializedName("Released")
    public lateinit var Released:String

    @SerializedName("Runtime")
    public lateinit var Runtime:String
    @SerializedName("Genre")
    public lateinit var Genre:String
    @SerializedName("Director")
    public lateinit var Director:String
    @SerializedName("Writer")
    public lateinit var Writer:String

    @SerializedName("Actors")
    public lateinit var Actors:String
    @SerializedName("Plot")
    public lateinit var Plot:String
    @SerializedName("Language")
    public lateinit var Language:String
    @SerializedName("Country")
    public lateinit var Country:String
    @SerializedName("Awards")
    public lateinit var Awards:String
    @SerializedName("Poster")
    public lateinit var Poster:String

    @SerializedName("Ratings")
    public lateinit var ratings: ArrayList<Ratings>

    @SerializedName("Metascore")
    public lateinit var Metascore:String
    @SerializedName("imdbRating")
    public lateinit var imdbRating:String
    @SerializedName("imdbVotes")
    public lateinit var imdbVotes:String
    @SerializedName("imdbID")
    public lateinit var imdbID:String
    @SerializedName("Type")
    public lateinit var Type:String
    @SerializedName("DVD")
    public lateinit var DVD:String
    @SerializedName("BoxOffice")
    public lateinit var BoxOffice:String
    @SerializedName("Production")
    public lateinit var Production:String
    @SerializedName("Website")
    public lateinit var Website:String
    @SerializedName("Response")
    public lateinit var Response:String
    @SerializedName("Error")
    public lateinit var Error:String

    public class Ratings{
        @SerializedName("Source")
        public lateinit var Source:String
        @SerializedName("Value")
        public lateinit var Value:String
    }
}
