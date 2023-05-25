package com.example.proiect.ui.register

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.proiect.databinding.FragmentRegisterBinding

class RegisterFragment : Fragment() {
    private var binding: FragmentRegisterBinding? = null
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        val registerViewModel: RegisterViewModel
        registerViewModel = ViewModelProvider(this).get(RegisterViewModel::class.java)
        binding = FragmentRegisterBinding.inflate(inflater, container, false)
        val root: View = binding!!.root
        val textView = binding!!.textRegister
        registerViewModel.text.observe(viewLifecycleOwner) { text: String? -> textView.text = text }
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}