package com.example.oscarapp

object Vote {
    var userId: Long? = null
    var userToken: String? = null
    var selectedMovieId: Long? = null
    var selectedMovieName: String? = null
    var selectedDirectorId: Long? = null
    var selectedDirectorName: String? = null

    fun clearSession() {
        selectedMovieId = null
        selectedMovieName = null
        selectedDirectorId = null
        selectedDirectorName = null
    }
}