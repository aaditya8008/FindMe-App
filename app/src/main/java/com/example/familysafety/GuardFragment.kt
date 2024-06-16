package com.example.familysafety

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.familysafety.databinding.FragmentGuardBinding
import com.example.familysafety.databinding.FragmentHomeFragementBinding
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.firestore

class GuardFragment : Fragment(),InviteMailAdapter.OnActionClick {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }
    lateinit var binding: FragmentGuardBinding
    lateinit var mContext: Context

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext=context
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding=FragmentGuardBinding.inflate(inflater,container,false)





        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.invite.setOnClickListener{
            sendInvite()
        }
        getInvites()
    }

    private fun getInvites() {
        val firestore=Firebase.firestore
        val senderMail=FirebaseAuth.getInstance().currentUser?.email.toString()
        firestore.collection("users")
            .document(FirebaseAuth.getInstance().currentUser?.email.toString())
            .collection("invites").get().addOnCompleteListener{
                if(it.isSuccessful){
                    val list:ArrayList<String> =ArrayList()
                    for(item in it.result){
                        if(item.get("invite_status")==0L){
                            list.add(item.id)
                        }
                    }
                    val adapter=InviteMailAdapter(list,this)
                    binding.inviteRecycler.layoutManager=LinearLayoutManager(mContext,LinearLayoutManager.VERTICAL,false)
                    binding.inviteRecycler.adapter=adapter
                }
            }


    }

    private fun sendInvite() {
val mail=binding.emailId.text.toString()
        val firestore=Firebase.firestore
        val data= hashMapOf(
            "invite_status" to 0
        )
        val senderMail=FirebaseAuth.getInstance().currentUser?.email.toString()
        firestore.collection("users")
            .document(mail)
            .collection("invites")
            .document(senderMail).set(data)
            .addOnSuccessListener {

            }.addOnFailureListener {

            }


    }


    companion object {

        @JvmStatic
        fun newInstance() =
            GuardFragment()
    }

    override fun onAcceptClick(mail: String) {
       Toast.makeText(requireContext(),mail,Toast.LENGTH_LONG).show()

        val firestore=Firebase.firestore
        val data= hashMapOf(
            "invite_status" to 1
        )
        val senderMail=FirebaseAuth.getInstance().currentUser?.email.toString()
        firestore.collection("users")
            .document(senderMail)
            .collection("invites")
            .document(mail).set(data)
            .addOnSuccessListener {

            }.addOnFailureListener {

            }
    }

    override fun onDenyClick(mail: String) {

        Toast.makeText(requireContext(),mail,Toast.LENGTH_LONG).show()


        val firestore=Firebase.firestore
        val data= hashMapOf(
            "invite_status" to -1
        )
        val senderMail=FirebaseAuth.getInstance().currentUser?.email.toString()
        firestore.collection("users")
            .document(senderMail)
            .collection("invites")
            .document(mail).set(data)
            .addOnSuccessListener {

            }.addOnFailureListener {

            }
    }
}