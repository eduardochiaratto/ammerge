package br.com.fiap.mergeam

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class RegisterActivity : AppCompatActivity() {

    lateinit var editTextUserName: EditText
    lateinit var editTextUserEmail: EditText
    lateinit var editTextUserPassword: EditText
    lateinit var buttonRegister: Button

    private lateinit var auth: FirebaseAuth
    private lateinit var database: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        editTextUserName = findViewById(R.id.editTextUserName)
        editTextUserEmail = findViewById(R.id.editTextUserEmail)
        editTextUserPassword = findViewById(R.id.editTextUserPassword)
        buttonRegister = findViewById(R.id.buttonRegister)

        database = Firebase.database.reference
        auth = Firebase.auth

        buttonRegister.setOnClickListener {
            userRegister()
        }

    }

    private fun userRegister() {
        val name = editTextUserName.text.toString()
        val email = editTextUserEmail.text.toString()
        val password = editTextUserPassword.text.toString()

        // Validar se existe campos vazios
        validateIfEmpty(name, email, password)

        // Criando usuário com Firebase Authentication
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener {
                if(it.isSuccessful) {
                    Log.d("Main", "createUserWithEmail: Success")

                    val uid = auth.currentUser?.uid ?: ""
                    val email = auth.currentUser?.email ?: ""
                    createUserProfileInFirebase(uid, name, email)

                    val intent = Intent(this, MenuActivity:: class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
                    startActivity(intent)
                }
            }
            .addOnFailureListener {
                Log.d("Main", "createUserWithEmailAndPassword: Failed - ${it.message}")
                Toast.makeText(baseContext, "${it.message}",
                    Toast.LENGTH_SHORT).show()
            }
    }

    private fun createUserProfileInFirebase(uid: String, name: String, email: String) {
        val user = User(uid, name, email)
        database.child("users").child(uid).setValue(user)
    }

    private fun validateIfEmpty(name:String, email: String, password: String) {
        if(name.isEmpty()) {
            editTextUserName.setError("Preencha este campo")
            return
        } else if(email.isEmpty()) {
            editTextUserEmail.setError("Preencha este campo")
            return
        } else if(password.isEmpty()) {
            editTextUserPassword.setError("Preencha este campo")
            return
        }
    }
}