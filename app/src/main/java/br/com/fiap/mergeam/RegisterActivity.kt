package br.com.fiap.mergeam

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class RegisterActivity : AppCompatActivity() {

    lateinit var editTextUserName: EditText
    lateinit var editTextUserCPF: EditText
    lateinit var editTextUserEmail: EditText
    lateinit var editTextUserPassword: EditText
    lateinit var buttonRegister: Button
    lateinit var buttonBackLogin: TextView

    private lateinit var auth: FirebaseAuth
    private lateinit var database: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        editTextUserName = findViewById(R.id.editTextUserName)
        editTextUserCPF = findViewById(R.id.editTextUserCPF)
        editTextUserEmail = findViewById(R.id.editTextUserEmail)
        editTextUserPassword = findViewById(R.id.editTextUserPassword)
        buttonRegister = findViewById(R.id.buttonRegister)
        buttonBackLogin = findViewById(R.id.buttonBackLogin)

        database = Firebase.database.reference
        auth = Firebase.auth

        buttonRegister.setOnClickListener {
            userRegister()
        }

        buttonBackLogin.setOnClickListener {
            val intent = Intent(this, LoginActivity:: class.java)
            startActivity(intent)
        }

    }

    private fun userRegister() {
        val username = editTextUserName.text.toString()
        val cpf = editTextUserCPF.text.toString()
        val email = editTextUserEmail.text.toString()
        val password = editTextUserPassword.text.toString()

        // Validar se existe campos vazios
        if(username.isEmpty()) {
            editTextUserName.setError("Digite um nome")
            return
        } else if(cpf.isEmpty()) {
            editTextUserCPF.setError("Digite um cpf")
            return
        } else if(email.isEmpty()) {
            editTextUserEmail.setError("Digite uma e-mail")
            return
        } else if(password.isEmpty()) {
            editTextUserPassword.setError("Digite uma senha")
            return
        }

        validateIfEmpty(username, cpf, email, password)

        // Criando usuário com Firebase Authentication
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener {
                if(it.isSuccessful) {
                    Log.d("Main", "createUserWithEmail: Success")

                    val uid = auth.currentUser?.uid ?: ""
                    val email = auth.currentUser?.email ?: ""
                    createUserProfileInFirebase(uid, username, cpf, email)

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

    private fun createUserProfileInFirebase(uid: String, username: String, cpf: String, email: String) {
        val user = User(uid, username, cpf, email)
        database.child("users").child(uid).setValue(user)
    }

    private fun validateIfEmpty(username:String, cpf: String, email: String, password: String) {

    }
}