package br.com.fiap.mergeam

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class MenuActivity: AppCompatActivity() {

    lateinit var textViewId: TextView
    lateinit var textViewName: TextView
    lateinit var textViewEmail: TextView
    lateinit var buttonSignOut: Button

    private lateinit var auth: FirebaseAuth
    private lateinit var database: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu)

        textViewId = findViewById(R.id.textViewId)
        textViewName = findViewById(R.id.textViewName)
        textViewEmail = findViewById(R.id.textViewEmail)
        buttonSignOut =findViewById(R.id.buttonSignOut)

        auth = Firebase.auth
        database = Firebase.database.reference

        val currentUser = auth.currentUser

        var currentUserData = currentUser?.uid?.let { database.child("users").child(it) }

        currentUserData?.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                Log.d("Main", "CurrentUser DataSnapshot: ${snapshot}")
                val user = returnUserData(snapshot);
                textViewId.text = user.uid
                textViewName.text = user.username
                textViewEmail.text = user.email
            }

            override fun onCancelled(error: DatabaseError) {
                Log.d("Main", "Log User Error: ${error.message}")
            }
        })

        buttonSignOut.setOnClickListener {
            auth.signOut()
            val intent = Intent(this, LoginActivity:: class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
        }
    }

    override fun onStart() {
        super.onStart()

        val currentUser = auth.currentUser
        if(currentUser != null) {
            Log.d("Main", "CurrentUser [LOGADO]!")
        } else {
            Log.d("Main", "CurrentUser [DESLOGADO] -> Redirecionado para LogInActivity")
            val intent = Intent(this, LoginActivity:: class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
        }
    }

    private fun returnUserData(snapshot: DataSnapshot): User {
        val username = snapshot.child("username").getValue().toString()
        val uid = snapshot.child("uid").getValue().toString()
        val email = snapshot.child("email").getValue().toString()

        val user:User = User(username, uid, email)
        return user
    }
}