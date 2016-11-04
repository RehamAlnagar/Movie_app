package com.example.android.movieapp;



public class Movie {

        private String moviename;
        private String movieposter;

        public Movie(){

        }

        public Movie(String moviename , String movieposter){
            this.moviename = moviename;
            this.movieposter = movieposter;
        }

        public String getMoviename() {
            return moviename;
        }

        public void setMoviename(String moviename) {
            this.moviename = moviename;
        }

        public String getMovieposter() {return movieposter;}

        public void setMovieposter(String movieposter){this.movieposter = movieposter;}

}
