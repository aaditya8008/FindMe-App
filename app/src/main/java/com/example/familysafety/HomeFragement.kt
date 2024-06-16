package com.example.familysafety

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.provider.ContactsContract
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.familysafety.databinding.FragmentHomeFragementBinding
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class HomeFragement : Fragment() {
    lateinit var adapter2: InviteAdapter
    lateinit var listContact: ArrayList<ContactModel>
    lateinit var mContext: Context

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext=context
    }

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

    }
lateinit var binding:FragmentHomeFragementBinding
    override fun onCreateView(

        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding= FragmentHomeFragementBinding.inflate(inflater,container,false)
        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        super.onViewCreated(view, savedInstanceState)
        val listMembers = listOf<MemberModel>(
            MemberModel("Ishan"),
            MemberModel("Rahul"),
            MemberModel("Aditya")
        )
        val adapter = MemberAdapter(listMembers)
        val recycler = binding.rv
        recycler.layoutManager = LinearLayoutManager(mContext)
        recycler.adapter = adapter
        listContact = ArrayList<ContactModel>()
        adapter2 = InviteAdapter(listContact)
        fetchDBContacts()


        CoroutineScope(Dispatchers.IO).launch {
            insertDBContacts(fetchContacts())

          
        }


        val recycler2 = binding.rvinvite
        recycler2.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        recycler2.adapter = adapter2
        val threeDots=requireView().findViewById<ImageView>(R.id.threeDots)
        threeDots.setOnClickListener{
           FirebaseAuth.getInstance().signOut()
            SharedPref.putBoolean(PrefConstants.IS_USER_LOGGED_IN,false)
            startActivity(Intent(requireContext(), LoginActivity::class.java))
            requireActivity().finish()

        }

    }

    private fun fetchDBContacts() {
        val db = ContactDB.getDB(requireContext())

        db.contactDao().getAllContacts().observe(viewLifecycleOwner) {
            listContact.clear()
            listContact.addAll(it)
            adapter2.notifyDataSetChanged()

        }
    }

    private suspend fun insertDBContacts(listContact: ArrayList<ContactModel>) {
        val db = ContactDB.getDB(requireContext())

        db.contactDao().insertAll(listContact)
    }

    private fun fetchContacts(): ArrayList<ContactModel> {
        val cr = requireActivity().contentResolver
        val listContacts = ArrayList<ContactModel>()
        val cursor = cr.query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null)
        if (cursor != null && cursor.count > 0) {
            while (cursor != null && cursor.moveToNext()) {
                val id = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID))
                val name =
                    cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME))
                val hasPhoneNumber =
                    cursor.getInt(cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER))
                if (hasPhoneNumber > 0) {
                    val pCur = cr.query(
                        ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                        null,
                        ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?",
                        arrayOf(id),
                        ""
                    )

                    if (pCur != null && pCur.count > 0) {
                        while (pCur != null && pCur.moveToNext()) {
                            val phoneNum =
                                pCur.getString(pCur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER))
                            listContacts.add(ContactModel(name, phoneNum))
                            // Do something with the phone number
                        }
                    }

                    pCur?.close()
                }
            }
        }
        if (cursor != null) {
            cursor.close()
        }
        return listContacts

    }

    companion object {


        @JvmStatic
        fun newInstance() =
            HomeFragement().apply {
                arguments = Bundle().apply {
                }
            }
    }
}