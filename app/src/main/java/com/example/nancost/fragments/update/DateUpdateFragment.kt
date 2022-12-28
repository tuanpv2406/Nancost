package com.example.nancost.fragments.update

import android.os.Bundle
import android.view.*
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.nancost.R
import com.example.nancost.databinding.FragmentDateUpdateBinding
import com.example.nancost.model.NancostData
import com.example.nancost.utils.AppConstant
import com.example.nancost.utils.SharedPreUtils
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class DateUpdateFragment : Fragment() {
    private var _binding: FragmentDateUpdateBinding? = null
    private val binding get() = _binding!!
    val adapter = UpdateAdapter()
    private val args by navArgs<DateUpdateFragmentArgs>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDateUpdateBinding.inflate(inflater, container, false)

        val userUid = SharedPreUtils.getString(AppConstant.Enum.USER_UID)

        val nancostDataList: ArrayList<NancostData?> = arrayListOf()
        Firebase.database.getReference("$userUid/nancost/${args.currentNancost?.nancostUid}/nancostDataList/")
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        for (ds in snapshot.children) {
                            val nancostData: NancostData? = ds.getValue(NancostData::class.java)
                            nancostDataList.add(nancostData)
                        }
                        adapter.setData(nancostDataList)
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
                adapter.removeNancostAt(
                    viewHolder.adapterPosition,
                    childFragmentManager,
                    nancostDataList,
                    userUid,
                    args.currentNancost?.remainVolume
                )
            }
        }

        val itemTouchHelper = ItemTouchHelper(simpleItemTouchCallback)
        itemTouchHelper.attachToRecyclerView(recyclerView)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val data = args.currentNancost
        binding.fabAdd.setOnClickListener {
            val action =
                DateUpdateFragmentDirections.actionDateUpdateFragmentToDateAddFragment(data)
            findNavController().navigate(action)
        }

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                findNavController().navigate(R.id.action_dateUpdateFragment_to_listFragment)
            }
        })
    }
}