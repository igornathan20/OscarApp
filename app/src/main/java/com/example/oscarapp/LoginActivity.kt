package com.example.oscarapp

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.BuildConfig
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import org.json.JSONException
import org.json.JSONObject

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val btnLogin = findViewById<Button>(R.id.btnLogin)
        btnLogin.setOnClickListener {
            val username = findViewById<EditText>(R.id.etEmail).text.toString()
            val password = findViewById<EditText>(R.id.etPassword).text.toString()

            Log.d("LoginDebug", "Login button clicked. Email: $username, Password: $password")

            authenticateUser(username, password)
        }
    }

    private fun authenticateUser(username: String, password: String) {
        val url = "http://10.0.2.2:3000/login" // URL do backend
        if (BuildConfig.DEBUG) Log.d("LoginDebug", "Tentando autenticar o usuário com URL: $url")

        // Construir o corpo da requisição
        val jsonBody = JSONObject().apply {
            put("username", username)
            put("password", password)
        }
        if (BuildConfig.DEBUG) Log.d("LoginDebug", "Corpo da Requisição (JSON): $jsonBody")

        // Requisição usando Volley
        val request = JsonObjectRequest(
            Request.Method.POST, url, jsonBody,
            { response ->
                try {
                    Log.d("LoginDebug", "Resposta do Servidor: $response")

                    // Extrair os dados do JSON retornado
                    val userId = response.optInt("id", -1)
                    val token = response.optString("token", "N/A")
                    val issuedAt = response.optString("issuedAt", "N/A")

                    // Exibir mensagem de sucesso no log
                    Log.d("LoginDebug", "Autenticação bem-sucedida. ID: $userId, Token: $token, Emitido em: $issuedAt")

                    // Exibir uma mensagem ao usuário
                    Toast.makeText(this, "Bem-vindo!", Toast.LENGTH_SHORT).show()

                    // Armazenar o token em armazenamento seguro (Opcional)
                    // saveToken(token)

                    // Continuar para a próxima Activity
                    startActivity(Intent(this, MenuActivity::class.java))
                } catch (e: JSONException) {
                    Log.e("LoginDebug", "Erro ao processar a resposta JSON: ${e.message}")
                    Toast.makeText(this, "Erro no servidor", Toast.LENGTH_SHORT).show()
                }
            },
            { error ->
                // Log completo do erro
                Log.e("LoginDebug", "Erro na Requisição: ${error.message}")

                // Captura o código de status HTTP para tratamento de erros
                val statusCode = error.networkResponse?.statusCode
                val responseData = error.networkResponse?.data?.let { String(it) } ?: "Sem detalhes"
                val errorMessage = when (statusCode) {
                    401 -> "Credenciais inválidas. Verifique o nome de usuário e a senha."
                    400 -> "Requisição inválida. Tente novamente."
                    else -> "Erro ao autenticar. Código: $statusCode"
                }

                // Log de resposta detalhada do servidor (se disponível)
                Log.e("LoginDebug", "Código HTTP: $statusCode, Resposta: $responseData")

                // Mostrar mensagem ao usuário
                Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show()
            }
        )

        // Adicionar a requisição à fila do Volley
        if (BuildConfig.DEBUG) Log.d("LoginDebug", "Requisição adicionada à fila")
        Volley.newRequestQueue(this).add(request)
    }


}
