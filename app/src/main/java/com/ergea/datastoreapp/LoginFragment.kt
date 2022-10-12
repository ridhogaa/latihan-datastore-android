package com.ergea.datastoreapp

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.ergea.datastoreapp.databinding.FragmentLoginBinding

class LoginFragment : Fragment() {

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!
    private lateinit var pref: DataStoreManager
    private lateinit var viewModel: UserViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        pref = DataStoreManager(requireContext())
        viewModel = ViewModelProvider(this, ViewModelFactory(pref))[UserViewModel::class.java]
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.textBpa.setOnClickListener {
            it.findNavController().navigate(R.id.action_loginFragment_to_registerFragment2)
        }
        binding.btnLogin.setOnClickListener {
            val username = binding.etUsername.text.toString().trim()
            val password = binding.etPassword.text.toString().trim()
            var vmU : String? = ""
            var vmP : String? = ""
            viewModel.getDataStoreUsername().observe(viewLifecycleOwner){
                vmU = it.toString()
            }
            viewModel.getDataStorePassword().observe(viewLifecycleOwner){
                vmP = it.toString()
            }
            if (username.equals(vmU, ignoreCase = false) && password.equals(vmP, ignoreCase = false) && username.isNotEmpty() && password.isNotEmpty()){
                viewModel.setIsLogin(true)
                it.findNavController().navigate(R.id.action_loginFragment_to_homeFragment2)
            } else {
                Toast.makeText(requireContext(), "Login Tidak Valid", Toast.LENGTH_SHORT).show()
            }
//            if (username.equals(viewModel.getDataStoreUsername()))
//            it.findNavController().navigate(R.id.action_loginFragment_to_homeFragment2)
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.getDataStoreIsLogin().observe(viewLifecycleOwner){
            if (it == true){
                findNavController().navigate(R.id.action_loginFragment_to_homeFragment2)
            }else {
                requireContext()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}