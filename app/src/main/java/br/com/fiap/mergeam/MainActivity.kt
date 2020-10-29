package br.com.fiap.mergeam

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity() {

    lateinit var editTextUserName: EditText
    lateinit var editTextUserEmail: EditText
    lateinit var editTextUserPassword: EditText
    lateinit var buttonRegister: Button

    private lateinit var auth: FirebaseAuth
    private lateinit var database: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        editTextUserName = findViewById(R.id.editTextUserName)
        editTextUserEmail = findViewById(R.id.editTextUserEmail)
        editTextUserPassword = findViewById(R.id.editTextUserPassword)
        buttonRegister = findViewById(R.id.buttonRegister)

        // Initialize Firebase Auth
        auth = Firebase.auth
        database = Firebase.database.reference

        buttonRegister.setOnClickListener {
            userRegister()
        }

    }

    private fun userRegister() {
        val name = editTextUserName.text.toString()
        val email = editTextUserEmail.text.toString()
        val password = editTextUserPassword.text.toString()

        // Se o usuario deixar os campos vazios
        if(email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Please enter text in email/pw",
                Toast.LENGTH_SHORT).show()
            return
        }

        // Criando usuario no Firebase
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener {
                if(it.isSuccessful) {
                    Log.d("Main", "createUserWithEmail: Success")

                    createUserProfileInFirebase(name)
                } else {
                    Toast.makeText(baseContext, "Authentication failed.",
                        Toast.LENGTH_SHORT).show()
                    return@addOnCompleteListener
                }
            }
            .addOnFailureListener {
                Log.d("Main", "Failed to create user: ${it.message}")
            }
    }

    private fun createUserProfileInFirebase(name: String) {
        val uid = auth.uid ?: ""

        val user = User(uid, name)

        database.child("users").child(uid).setValue(user)
    }
}