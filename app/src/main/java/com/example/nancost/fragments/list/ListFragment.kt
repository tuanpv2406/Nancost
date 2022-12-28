package com.example.nancost.fragments.list

import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.nancost.R
import com.example.nancost.databinding.FragmentListBinding
import com.example.nancost.fragments.dialog.ActionDialog
import com.example.nancost.model.Nancost
import com.example.nancost.model.NancostVolume
import com.example.nancost.utils.AppConstant
import com.example.nancost.utils.SharedPreUtils
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import java.util.*


class ListFragment : Fragment() {
    private var _binding: FragmentListBinding? = null
    private val binding get() = _binding!!
    val nancostList: ArrayList<Nancost?> = arrayListOf()
    val adapter = ListAdapter()
    val volumeList: ArrayList<NancostVolume?> = arrayListOf()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentListBinding.inflate(inflater, container, false)
        val view = binding.root

        val userUid = SharedPreUtils.getString(AppConstant.Enum.USER_UID)

        Firebase.database.getReference("$userUid/nancost/")
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        for (ds in snapshot.children) {
                            val nancost: Nancost? = ds.getValue(Nancost::class.java)
                            val nancostIterator = nancostList.iterator()
                            if (nancostIterator.hasNext()) {
                                if (nancostIterator.next()?.nancostUid == nancost?.nancostUid)
                                    return
                                else {
                                    nancostList.add(nancost)
                                }
                            } else {
                                nancostList.add(nancost)
                            }
                        }
                        adapter.setData(nancostList)
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                }
            })

        Firebase.database.getReference("$userUid/nancostVolume/")
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        for (ds in snapshot.children) {
                            val nancostVolume: NancostVolume? = ds.getValue(NancostVolume::class.java)
                            val nancostVolumeIterator = volumeList.iterator()
                            if (nancostVolumeIterator.hasNext()) {
                                if (nancostVolumeIterator.next()?.nancostUid == nancostVolume?.nancostUid)
                                    return
                                else {
                                    volumeList.add(nancostVolume)
                                }
                            } else {
                                volumeList.add(nancostVolume)
                            }
                        }
                        adapter.setData(nancostList, volumeList)
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                }
            })

        val recyclerView = binding.recyclerView
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        val simpleItemTouchCallback: ItemTouchHelper.SimpleCallback = object :
            ItemTouchHelper.SimpleCallback(
                0,
                ItemTouchHelper.LEFT
            ) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, swipeDir: Int) {
                adapter.removeNancostAt(viewHolder.adapterPosition, childFragmentManager, userUid)
            }
        }

        val itemTouchHelper = ItemTouchHelper(simpleItemTouchCallback)
        itemTouchHelper.attachToRecyclerView(recyclerView)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.searchView.setOnQueryTextListener(object :
            android.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                filter(query)
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                filter(newText)
                return false
            }
        })

        binding.fabAdd.setOnClickListener {
            findNavController().navigate(R.id.action_listFragment_to_addFragment)
        }

        binding.btnMenu.setOnClickListener {
            findNavController().navigate(R.id.action_listFragment_to_menuFragment)
        }

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                requireActivity().finish()
            }
        })
    }

    private fun filter(e: String?) {
        val filteredItem = ArrayList<Nancost?>()
        val nancostListTemp = nancostList
        if (e.isNullOrBlank()) {
            adapter.setData(nancostList)
        } else {
            for (item in nancostListTemp) {
                if (item?.nancostName?.lowercase(Locale.ROOT)
                        ?.contains(e.lowercase(Locale.ROOT)) == true
                ) {
                    filteredItem.add(item)
                }
            }
            adapter.setData(filteredItem)
        }
    }

}