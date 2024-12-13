package com.example.oscarapp

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.oscarapp.R
import org.json.JSONObject

class ConfirmActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_confirm)

        val tvSelectedMovie = findViewById<TextView>(R.id.tvSelectedMovie)
        val tvSelectedDirector = findViewById<TextView>(R.id.tvSelectedDirector)
        val etToken = findViewById<EditText>(R.id.etToken)
        val btnConfirmVote = findViewById<Button>(R.id.btnConfirmVote)

        // Exibe o filme e o diretor selecionado
        tvSelectedMovie.text = "Filme: ${Vote.selectedMovieName ?: "Não selecionado"}"
        tvSelectedDirector.text = "Diretor: ${Vote.selectedDirectorName ?: "Não selecionado"}"

        // Ao clicar no botão de confirmar voto
        btnConfirmVote.setOnClickListener {
            val token = etToken.text.toString()
            if (token == null) {
                Toast.makeText(this, "Insira um token válido.", Toast.LENGTH_SHORT).show()
            } else {
                sendVote(token)
            }
        }
    }

    // Função para enviar o voto
    private fun sendVote(token: String) {
        val url = "http://10.0.2.2:3000/vote" // URL do seu servidor

        // Corpo da requisição JSON
        val jsonBody = JSONObject().apply {
            put("userId", Vote.userId) // ID do usuário
            put("movieId", Vote.selectedMovieId) // ID do filme
            put("directorId", Vote.selectedDirectorId) // ID do diretor
        }

        // Criando a requisição POST com o JSON no corpo
        val request = JsonObjectRequest(
            Request.Method.POST, url, jsonBody,
            { response ->
                // Exibe mensagem de sucesso
                Toast.makeText(this, "Voto registrado com sucesso!", Toast.LENGTH_SHORT).show()
                // Limpa a sessão de voto e fecha a activity
                Vote.clearSession()
                finish()
            },
            { error ->
                // Exibe mensagem de erro
                Toast.makeText(this, "Erro ao registrar voto: ${error.message}", Toast.LENGTH_SHORT).show()
            }
        )

        // Envia a requisição
        Volley.newRequestQueue(this).add(request)
    }
}
