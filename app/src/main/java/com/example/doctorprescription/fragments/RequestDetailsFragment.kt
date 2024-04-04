package com.example.doctorprescription.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import com.bumptech.glide.Glide
import com.example.doctorprescription.Constants
import com.example.doctorprescription.R
import com.example.doctorprescription.activities.DoctorPrescriptionActivity
import com.example.doctorprescription.databinding.FragmentRequestDetailsBinding
import com.example.doctorprescription.models.CustomerRegisterModel
import com.example.doctorprescription.models.PrescriptionModel
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.QueryDocumentSnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class RequestDetailsFragment : Fragment() {
    lateinit var binding: FragmentRequestDetailsBinding
    val db = Firebase.firestore
    var mAuth = Firebase.auth
    var prescriptionId=""
    var userauthId=""
    var DoctorAuthid=""
    lateinit var doctorActivity : DoctorPrescriptionActivity
    var progressBar: ProgressBar?=null
    var preObjectModel: PrescriptionModel?=null
    private val TAG = FragmentRequestDetailsBinding::class.java.canonicalName

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        doctorActivity = activity as DoctorPrescriptionActivity

        progressBar = ProgressBar(requireContext(), null, android.R.attr.progressBarStyleSmall)

        progressBar?.visibility=View.GONE

        arguments?.let {
            prescriptionId= it.getString(Constants.presId,"")?:""
            userauthId= it.getString(Constants.CustomerAuthId,"")?:""

        }
        Log.e("Id","CustomerAuthId: ${userauthId}")
        Log.e("Id","prescriptionId: ${prescriptionId}")

        val currentUser = mAuth.currentUser
        DoctorAuthid = currentUser?.uid.toString()

        db.collection(Constants.customers).whereEqualTo("userauthId",userauthId).addSnapshotListener{snapshots,e->
            if (e != null){
                return@addSnapshotListener
            }
            for (snapshot in snapshots!!.documentChanges) {
                val userModel = convertObject( snapshot.document)
                Log.e("model", "onCreate: $userModel")
                binding.tvName.setText(userModel?.username)
                binding.tvEmail.setText(userModel?.useremail)

            }
        }
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding= FragmentRequestDetailsBinding.inflate(layoutInflater)
        // Inflate the layout for this fragment
        db.collection(Constants.prescription).document(prescriptionId)
            .addSnapshotListener{snapshots,e->
                if (e != null){
                    return@addSnapshotListener
                }

                var model = snapshots?.toObject(PrescriptionModel::class.java)
                Log.e("model", "onCreate: $model")
                binding.tvproblem.setText(model?.customerProblems)
                Glide
                    .with(doctorActivity)
                    .load(model?.customerImage)
                    .centerCrop()
                    .placeholder(R.drawable.ic_launcher_background)
                    .into(binding.imgCustomerProblem)
                binding.tvSolution.setText(model?.doctorSolution)
                Glide
                    .with(doctorActivity)
                    .load(model?.doctotrImage)
                    .centerCrop()
                    .placeholder(R.drawable.ic_launcher_background)
                    .into(binding.imgdoctorSolution)
            }
        return (binding.root)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        progressBar=binding.pbar


    }
    fun convertObject(snapshot: QueryDocumentSnapshot) : CustomerRegisterModel?{
        val customerRegisterModel:CustomerRegisterModel? =
            snapshot.toObject(CustomerRegisterModel::class.java)
        customerRegisterModel?.customerId = snapshot.id ?: ""
        return customerRegisterModel
    }
}