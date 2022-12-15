package com.example.nancost.fragments.list

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.nancost.NancostApplication
import com.example.nancost.R
import com.example.nancost.databinding.FragmentListBinding
import com.example.nancost.viewmodel.NancostViewModel
import com.example.nancost.viewmodel.NancostViewModelFactory
import com.google.android.material.floatingactionbutton.FloatingActionButton


class ListFragment : Fragment() {
    private lateinit var viewModel: NancostViewModel
    private var _binding: FragmentListBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentListBinding.inflate(inflater, container, false)
        val view = binding.root

        val adapter = ListAdapter()
        val recyclerView = binding.recyclerView
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        viewModel = ViewModelProvider(this, NancostViewModelFactory(
            (requireActivity().application as NancostApplication).repository
        ))[NancostViewModel::class.java]
        viewModel.allNancost.observe(viewLifecycleOwner) { nancost ->
            adapter.setData(nancost)
        }

        binding.fabAdd.setOnClickListener {
            findNavController().navigate(R.id.action_listFragment_to_addFragment)
        }

        setHasOptionsMenu(true)

        return view
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu, menu)
    }

}