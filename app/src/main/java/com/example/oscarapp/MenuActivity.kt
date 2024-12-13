package com.example.oscarapp

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import java.util.UUID

class MenuActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu)

        // Gerar um token aleatório ao entrar na página
        val randomToken = UUID.randomUUID().toString()
        Vote.userToken = randomToken

        // Exibir o token na tela
        val tvToken = findViewById<TextView>(R.id.tvToken)
        tvToken.text = "Token: $randomToken"

        // Configurar os botões de navegação
        findViewById<Button>(R.id.btnVoteDirector).setOnClickListener {
            startActivity(Intent(this, DirectorActivity::class.java))
        }
        findViewById<Button>(R.id.btnVoteMovie).setOnClickListener {
            startActivity(Intent(this, MovieActivity::class.java))
        }
        findViewById<Button>(R.id.btnConfirmVote).setOnClickListener {
            startActivity(Intent(this, ConfirmActivity::class.java))
        }
        findViewById<Button>(R.id.btnLogout).setOnClickListener {
            Vote.clearSession()  // Limpa a sessão e finaliza a activity
            finish()
        }
    }
}
