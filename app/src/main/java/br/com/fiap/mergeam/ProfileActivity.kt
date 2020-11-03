package br.com.fiap.mergeam

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class ProfileActivity : AppCompatActivity() {

    lateinit var editTextUserName: EditText
    lateinit var editTextUserEmail: EditText
    lateinit var buttonSaveProfile: Button
    lateinit var buttonBackMenu: Button

    private lateinit var auth: FirebaseAuth
    private lateinit var database: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        editTextUserName = findViewById(R.id.editTextUserName)
        editTextUserEmail = findViewById(R.id.editTextUserEmail)
        buttonSaveProfile = findViewById(R.id.buttonSaveProfile)
        buttonBackMenu = findViewById(R.id.buttonBackMenu)

        auth = Firebase.auth
        database = Firebase.database.reference


        val currentUser = auth.currentUser

        var currentUserData = currentUser?.uid?.let { database.child("users").child(it) }
        currentUserData?.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                Log.d("Main", "CurrentUser DataSnapshot: ${snapshot}")
                val user = returnUserData(snapshot);
                editTextUserName.setText(user.username)
                editTextUserEmail.setText(user.email)
            }

            override fun onCancelled(error: DatabaseError) {
                Log.d("Main", "Log User Error: ${error.message}")
            }
        })

        buttonSaveProfile.setOnClickListener {
            val username = editTextUserName.text.toString()
            val email = editTextUserEmail.text.toString()
            val uid = currentUser?.uid.toString()

            database.child("users").child(uid).child("username").setValue(username)

            currentUser?.updateEmail(email)
                ?.addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        editTextUserEmail.setText(email)
                    }
                }

            editTextUserName.setText(username)

            Toast.makeText(baseContext, "Salvo com Sucesso!",
                Toast.LENGTH_SHORT).show()
        }

        buttonBackMenu.setOnClickListener {
            val intent = Intent(this, MenuActivity:: class.java)
            startActivity(intent)
        }

    }

    private fun returnUserData(snapshot: DataSnapshot): User {
        val username = snapshot.child("username").getValue().toString()
        val cpf = snapshot.child("cpf").getValue().toString()
        val uid = snapshot.child("uid").getValue().toString()
        val email = snapshot.child("email").getValue().toString()

        val user:User = User(uid, username, cpf, email)
        return user
    }
}