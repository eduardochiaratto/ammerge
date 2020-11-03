package br.com.fiap.mergeam

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth

class LoginActivity : AppCompatActivity() {

    lateinit var editTextUserEmail: EditText
    lateinit var editTextUserPassword: EditText
    lateinit var buttonLogin: Button
    lateinit var buttonRegister: TextView

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

        if(email.isEmpty()) {
            editTextUserEmail.setError("Digite um e-mail")
            return
        } else if(password.isEmpty()) {
            editTextUserPassword.setError("Digite uma senha")
            return
        }

        FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
            .addOnCompleteListener {
                if(it.isSuccessful) {
                    Log.d("Main", "signInWithEmailAndPassword: Success")

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