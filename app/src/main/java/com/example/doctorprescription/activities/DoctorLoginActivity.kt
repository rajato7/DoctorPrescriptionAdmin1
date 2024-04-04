package com.example.doctorprescription.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.example.doctorprescription.R
import com.example.doctorprescription.databinding.ActivityDoctorLoginBinding
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class DoctorLoginActivity : AppCompatActivity() {
    lateinit var binding:ActivityDoctorLoginBinding
    var mAuth = Firebase.auth
    private val emailPattern = "[a-zA-Z0-9._-]+@[a-z]+[a-z]+"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDoctorLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnsave.setOnClickListener {
            if (binding.edtemail.text.isNullOrEmpty()) {
                binding.tilemail.isErrorEnabled = true
                binding.tilemail.error = "Enter Email"
            }else if(!binding.edtemail.text!!.matches(emailPattern.toRegex())) {
                binding.edtemail.error = "Enter Valid Email"
            }else if (binding.edtPassword.text.isNullOrEmpty()) {
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
        binding.tvResetPassword.setOnClickListener {
            AlertDialog.Builder(this)
                .setTitle("Forget Password ?")
                .setMessage("Are your sure to change th password.")
                .setPositiveButton("yes"){_,_->
                    mAuth.sendPasswordResetEmail(binding.edtemail.text.toString()).addOnCompleteListener {
                        if(it.isSuccessful){
                            Toast.makeText(this,"Done sent", Toast.LENGTH_LONG).show();
                        }else{
                            Toast.makeText(this,"Error Occurred",Toast.LENGTH_LONG).show();
                        }
                    }
                }
                .setNegativeButton("No"){_,_->

                }
                .show()
        }

    }
}