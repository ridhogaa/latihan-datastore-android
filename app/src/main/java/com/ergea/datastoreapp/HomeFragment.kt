package com.ergea.datastoreapp

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.ergea.datastoreapp.databinding.FragmentHomeBinding
import com.ergea.datastoreapp.databinding.FragmentLoginBinding
import com.ergea.datastoreapp.databinding.FragmentRegisterBinding

class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private lateinit var pref: DataStoreManager
    private lateinit var viewModel: UserViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        pref = DataStoreManager(requireContext())
        viewModel = ViewModelProvider(this, ViewModelFactory(pref))[UserViewModel::class.java]
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getDataStoreUsername().observe(viewLifecycleOwner) {
            binding.tv1.text = "Hai " + it.toString()
        }
        viewModel.getDataStorePassword().observe(viewLifecycleOwner) {
            binding.tv2.text = "Password anda:" + it.toString()
        }
        binding.tv3.setOnClickListener {
            viewModel.isClear()
            it.findNavController().navigate(R.id.action_homeFragment2_to_loginFragment)
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}