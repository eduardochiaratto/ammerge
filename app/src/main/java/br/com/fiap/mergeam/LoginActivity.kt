package br.com.fiap.mergeam

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth

class LoginActivity : AppCompatActivity() {

    lateinit var editTextUserEmail: EditText
    lateinit var editTextUserPassword: EditText
    lateinit var buttonLogin: Button
    lateinit var buttonRegister: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)


        editTextUserEmail = findViewById(R.id.editTextUserEmail)
        editTextUserPassword = findViewById(R.id.editTextUserPassword)
        buttonLogin = findViewById(R.id.buttonLogin)
        buttonRegister = findViewById(R.id.buttonRegister)

        buttonLogin.setOnClickListener {
            loginUserInFirebase()
        }

        buttonRegister.setOnClickListener {
            val intent = Intent(this, RegisterActivity:: class.java)
            startActivity(intent)
        }
    }

    private fun loginUserInFirebase() {
        val email = editTextUserEmail.text.toString()
        val password = editTextUserPassword.text.toString()

        // VALIDAR CAMPOS VAZIOS
        if(email.isEmpty()) {
            editTextUserEmail.setError("Preencha este campo")
            return
        } else if(password.isEmpty()) {
            editTextUserEmail.setError("Preencha este campo")
            return
        }

        FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
            .addOnCompleteListener {
                if(it.isSuccessful) {
                    Log.d("Main", "signInWithEmailAndPassword: Success")

                    // Navegar para a tela MenuActivity
                    val intent = Intent(this, MenuActivity:: class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
                    startActivity(intent)
                }
            }
            .addOnFailureListener {
                Log.d("Main", "Failed to login user: ${it.message}")
                Toast.makeText(baseContext, "${it.message}",
                    Toast.LENGTH_SHORT).show()
            }
    }
}