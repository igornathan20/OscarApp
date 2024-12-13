package com.example.oscarapp

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ProgressBar
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Request
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.Volley
import org.json.JSONArray
import org.json.JSONObject

class DirectorActivity : AppCompatActivity() {

    private lateinit var progressBar: ProgressBar
    private lateinit var spinnerDirectors: Spinner
    private lateinit var btnVote: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_director)

        // Inicializando as views
        progressBar = findViewById(R.id.progressBar)
        spinnerDirectors = findViewById(R.id.spinnerDirectors)
        btnVote = findViewById(R.id.btnVoteDirector)

        // Carregar os diretores
        loadDirectors()

        // Configurar o botão de voto
        btnVote.setOnClickListener { handleVote() }
    }

    // Função para carregar os diretores
    private fun loadDirectors() {
        val url = "http://wecodecorp.com.br/ufpr/diretor"
        toggleProgressBar(true)

        val request = JsonArrayRequest(
            Request.Method.GET, url, null,
            { response ->
                toggleProgressBar(false)
                populateDirectors(response)
            },
            { error ->
                toggleProgressBar(false)
                showToast("Erro ao carregar diretores")
                logError("Erro ao carregar diretores: ${error.message}")
            }
        )

        Volley.newRequestQueue(this).add(request)
    }

    // Função para alternar a visibilidade da progress bar
    private fun toggleProgressBar(isLoading: Boolean) {
        progressBar.visibility = if (isLoading) ProgressBar.VISIBLE else ProgressBar.GONE
    }

    // Função para popular o Spinner com os nomes dos diretores
    private fun populateDirectors(response: JSONArray) {
        val directorsList = mutableListOf<String>()
        val directorsIdList = mutableListOf<Long>()

        for (i in 0 until response.length()) {
            val director = response.getJSONObject(i)
            val directorName = director.getString("nome")
            directorsList.add(directorName)
            directorsIdList.add(director.getLong("id"))
        }

        // Configurando o Spinner com os nomes dos diretores
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, directorsList)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerDirectors.adapter = adapter

        // Armazenando os IDs dos diretores no tag do Spinner
        spinnerDirectors.tag = directorsIdList
    }

    // Função para lidar com o voto
    private fun handleVote() {
        val selectedPosition = spinnerDirectors.selectedItemPosition

        if (selectedPosition == -1) {
            showToast("Por favor, selecione um diretor.")
        } else {
            val directorsIdList = spinnerDirectors.tag as List<Long>
            val selectedDirectorId = directorsIdList[selectedPosition]
            val selectedDirectorName = spinnerDirectors.selectedItem.toString()

            // Salvando a seleção na sessão de voto
            Vote.selectedDirectorId = selectedDirectorId
            Vote.selectedDirectorName = selectedDirectorName

            showToast("Diretor selecionado com sucesso!")
            finish()
        }
    }

    // Função para exibir uma mensagem de Toast
    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    // Função para logar erros
    private fun logError(message: String) {
        println(message)
    }
}
