package com.example.taskbookxpert.view

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import coil.load
import com.example.taskbookxpert.R
import com.example.taskbookxpert.data.local.UserEntity
import com.example.taskbookxpert.viewmodel.UserViewModel
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SigninActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var googleClient: GoogleSignInClient

    private lateinit var goto_pdf: Button
    private lateinit var goto_image_picker: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signin)

        goto_pdf = findViewById(R.id.goto_pdf)
        goto_image_picker = findViewById(R.id.goto_image_picker)

        askNotificationPermission()

        auth = FirebaseAuth.getInstance()

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        googleClient = GoogleSignIn.getClient(this, gso)

        val currentUser = auth.currentUser
        if(currentUser != null){
            findViewById<Button>(R.id.signInBtn).text="Sign out"
            findViewById<ImageView>(R.id.user_img).load(currentUser?.photoUrl)
            findViewById<TextView>(R.id.user_name).text="Welcome ${currentUser?.displayName}"

            goto_pdf.visibility = View.VISIBLE
            goto_image_picker.visibility = View.VISIBLE
        }
        else{
            findViewById<Button>(R.id.signInBtn).text = "Sign in with Google"

            goto_pdf.visibility = View.GONE
            goto_image_picker.visibility = View.GONE
        }

        findViewById<Button>(R.id.signInBtn).setOnClickListener {
            if(auth.currentUser != null){
                signOut()
            }
            else {
                val signInIntent = googleClient.signInIntent
                launcher.launch(signInIntent)
            }
        }

        findViewById<Button>(R.id.goto_pdf).setOnClickListener {

                val intent = Intent(this, PdfViewerActivity::class.java)
                startActivity(intent)

        }

        findViewById<Button>(R.id.goto_image_picker).setOnClickListener {
                val intent = Intent(this, ImagePickerActivity::class.java)
                startActivity(intent)

        }

    }
    private val launcher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
        if (task.isSuccessful) {
            val account = task.result
            firebaseAuthWithGoogle(account.idToken!!)
        } else {
            Log.e("Sign", "Google Sign-In Failed")
        }
    }

    private fun firebaseAuthWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)

        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    findViewById<Button>(R.id.signInBtn).text="Sign out"

                    val user = auth.currentUser
                    findViewById<ImageView>(R.id.user_img).load(user?.photoUrl)
                    findViewById<TextView>(R.id.user_name).text="Welcome ${user?.displayName}"

                    Log.d("SignIn", "Welcome ${user?.displayName}")
                    // TODO: Save user in Room (coming next)

                    if(user != null){
                        val userEntity = UserEntity (
                            uid = user.uid,
                            name = user.displayName ?: "",
                            email = user.email ?: "",
                            photoUrl = user.photoUrl.toString()
                        )

                        val userViewModel = ViewModelProvider(this)[UserViewModel::class.java]
                        userViewModel.insertUser(userEntity)

                        lifecycleScope.launch {
                            val savedUser = withContext(Dispatchers.IO) {
                                userViewModel.getUserBlocking()  // create this method
                            }
                            Log.d("DB_CHECK", "Saved User: $savedUser")
                        }
                    }
                    goto_pdf.visibility = View.VISIBLE
                    goto_image_picker.visibility = View.VISIBLE

                } else {
                    Log.e("SignIn", "Firebase Auth Failed", task.exception)
                }
            }
    }


    private fun signOut(){
        auth.signOut()
        findViewById<Button>(R.id.signInBtn).text = "Sign in with Google"
        findViewById<TextView>(R.id.user_name).text=""

        findViewById<ImageView>(R.id.user_img).load(R.drawable.baseline_image_24)
        goto_pdf.visibility = View.GONE
        goto_image_picker.visibility = View.GONE

        Log.d("Sign", "User signed out.")
    }

    private fun askNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.POST_NOTIFICATIONS)
                != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(android.Manifest.permission.POST_NOTIFICATIONS),
                    1001
                )
            }
        }
    }
}