package com.example.doctorprescription.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.doctorprescription.R
import com.example.doctorprescription.databinding.ActivityDoctorLoginBinding
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class DoctorLoginActivity : AppCompatActivity() {
    lateinit var binding:ActivityDoctorLoginBinding
    var mAuth = Firebase.auth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDoctorLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnsave.setOnClickListener {
            if (binding.edtemail.text.isNullOrEmpty()) {
                binding.tilemail.isErrorEnabled = true
                binding.tilemail.error = "Enter Email"
            } else if (binding.edtPassword.text.isNullOrEmpty()) {
                binding.tilPassword.isErrorEnabled = true
                binding.tilPassword.error = "Enter password"
            } else {
                binding.tilPassword.isErrorEnabled = false
                binding.tilemail.isErrorEnabled = false
                mAuth.signInWithEmailAndPassword(
                    binding.edtemail.text.toString(),
                    binding.edtPassword.text.toString()
                ).addOnCompleteListener { loginTask ->
                    if (loginTask.isSuccessful) {
                        // Login successful
//                binding.llProgressBar.visibility = View.GONE
                        Snackbar.make(
                            binding.edtemail,
                            "Login Successfully",
                            Snackbar.LENGTH_SHORT
                        ).show()
                        startActivity(Intent(this, DoctorPrescriptionActivity::class.java))
                        this.finish()
                        println("Login Successfully")
                    } else {
                        // Login failed
                        Snackbar.make(
                            binding.edtemail,
                            "Login Error",
                            Snackbar.LENGTH_SHORT
                        ).show()
                        println("Login Error: ${loginTask.exception}")
                    }
                }
            }
        }

    }
}