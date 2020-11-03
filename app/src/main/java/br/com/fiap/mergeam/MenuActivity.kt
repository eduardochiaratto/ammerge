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

class MenuActivity : AppCompatActivity() {

    lateinit var buttonSignOut: TextView
    lateinit var buttonProfile: TextView
    lateinit var buttonChat: TextView

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu)

        buttonSignOut =findViewById(R.id.buttonSignOut)
        buttonProfile =findViewById(R.id.buttonProfile)
        buttonChat = findViewById(R.id.buttonChat)

        auth = Firebase.auth


        buttonProfile.setOnClickListener {
            val intent = Intent(this, ProfileActivity:: class.java)
            startActivity(intent)
        }

        buttonChat.setOnClickListener {
            val intent = Intent(this, ChatActivity:: class.java)
            startActivity(intent)
        }

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
}