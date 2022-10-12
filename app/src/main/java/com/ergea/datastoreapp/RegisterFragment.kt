package com.ergea.datastoreapp

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.ergea.datastoreapp.databinding.FragmentLoginBinding
import com.ergea.datastoreapp.databinding.FragmentRegisterBinding

class RegisterFragment : Fragment() {

    private var _binding: FragmentRegisterBinding? = null
    private val binding get() = _binding!!
    private lateinit var pref: DataStoreManager
    private lateinit var viewModel: UserViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRegisterBinding.inflate(inflater, container, false)
        pref = DataStoreManager(requireContext())
        viewModel = ViewModelProvider(this, ViewModelFactory(pref))[UserViewModel::class.java]
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btnRegister.setOnClickListener {
            val username = binding.etUsernameRegister.text.toString().trim()
            val password = binding.etPasswordRegister.text.toString().trim()
            val cPassword = binding.etKonfirmPasswordRegister.text.toString().trim()

            if (password.equals(cPassword, ignoreCase = false)) {
                viewModel.saveUser(username, password)
                it.findNavController().navigate(R.id.action_registerFragment2_to_loginFragment)
            } else {
                Toast.makeText(requireContext(), "Register tidak valid", Toast.LENGTH_SHORT)
                    .show()
            }


        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}