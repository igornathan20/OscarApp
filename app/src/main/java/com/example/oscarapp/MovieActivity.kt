package com.example.oscarapp

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.Volley

class MovieActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_movie)

        // Inicializando as views
        val progressBar = findViewById<ProgressBar>(R.id.progressBar)
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        val tvRecyclerViewTitle = findViewById<TextView>(R.id.tvRecyclerViewTitle)

        // Ajustando o título
        tvRecyclerViewTitle.text = "Lista de Filmes"

        // Configurando o RecyclerView
        recyclerView.layoutManager = LinearLayoutManager(this)

        // Carregar filmes
        loadMovies(progressBar, recyclerView)
    }

    private fun loadMovies(progressBar: ProgressBar, recyclerView: RecyclerView) {
        val url = "http://wecodecorp.com.br/ufpr/filme"

        // Exibe o progress bar enquanto carrega os dados
        progressBar.visibility = View.VISIBLE

        // Realizando a requisição para carregar os filmes
        val request = JsonArrayRequest(
            Request.Method.GET, url, null,
            { response ->
                // Esconde o progress bar após carregar os filmes
                progressBar.visibility = View.GONE
                val movies = mutableListOf<Movie>()

                // Processa os filmes recebidos
                for (i in 0 until response.length()) {
                    val jsonMovie = response.getJSONObject(i)
                    movies.add(Movie(
                        jsonMovie.getLong("id"),
                        jsonMovie.getString("nome"),
                        jsonMovie.getString("genero"),
                        jsonMovie.getString("foto")
                    ))
                }

                // Configura o adapter para o RecyclerView
                recyclerView.adapter = MovieAdapter(movies) { movie ->
                    Vote.selectedMovieId = movie.id
                    startActivity(Intent(this, DetailsActivity::class.java).apply {
                        putExtra("MOVIE_ID", movie.id)
                    })
                }
            },
            { error ->
                // Esconde o progress bar e exibe erro
                progressBar.visibility = View.GONE
                Toast.makeText(this, "Erro ao carregar filmes", Toast.LENGTH_SHORT).show()
                error.printStackTrace()  // Exibe o stack trace completo no log
            }
        )

        // Adiciona a requisição à fila do Volley
        Volley.newRequestQueue(this).add(request)
    }
}
