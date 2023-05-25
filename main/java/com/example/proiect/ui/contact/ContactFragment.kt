package com.example.proiect.ui.contact

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.proiect.databinding.FragmentContactBinding

class ContactFragment : Fragment() {
    private var binding: FragmentContactBinding? = null
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        val contactViewModel: ContactViewModel
        contactViewModel = ViewModelProvider(this).get(ContactViewModel::class.java)
        binding = FragmentContactBinding.inflate(inflater, container, false)
        val root: View = binding!!.root
        val textView = binding!!.textContact
        contactViewModel.text.observe(viewLifecycleOwner) { text: String? -> textView.text = text }
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}