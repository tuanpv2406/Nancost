package com.example.nancost.fragments.update

import android.os.Bundle
import android.view.*
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.nancost.R
import com.example.nancost.databinding.FragmentDateUpdateBinding
import com.example.nancost.model.Nancost
import com.example.nancost.model.NancostData
import com.example.nancost.utils.AppConstant
import com.example.nancost.utils.SharedPreUtils
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class DateUpdateFragment : Fragment(), AdapterView.OnItemSelectedListener {
    private var _binding: FragmentDateUpdateBinding? = null
    private val binding get() = _binding!!
    val adapter = UpdateAdapter()
    private val args by navArgs<DateUpdateFragmentArgs>()
    val nancostDataList: ArrayList<NancostData?> = arrayListOf()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDateUpdateBinding.inflate(inflater, container, false)

        val userUid = SharedPreUtils.getString(AppConstant.Enum.USER_UID)

        Firebase.database.getReference("$userUid/nancost/${args.currentNancost?.nancostUid}/nancostDataList/")
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        for (ds in snapshot.children) {
                            val nancostData: NancostData? = ds.getValue(NancostData::class.java)
                            val nancostDataIterator = nancostDataList.iterator()
                            if (nancostDataIterator.hasNext()) {
                                if (nancostDataIterator.next()?.nancostDataUid == nancostData?.nancostDataUid)
                                    return
                                else {
                                    nancostDataList.add(nancostData)
                                }
                            } else {
                                nancostDataList.add(nancostData)
                            }
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

        initSpinner()

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

    private fun initSpinner() {
        val data = resources.getStringArray(R.array.str_array_spinner)
        val adapter = object : ArrayAdapter<String>(
            requireContext(),
            R.layout.item_custom_spinner_layout,
            R.id.spinner_item_text,
            data.asList()
        ) {
            override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
                return getCustomView(position, convertView, parent)
            }

            override fun getDropDownView(
                position: Int,
                convertView: View?,
                parent: ViewGroup
            ): View {
                return getCustomView(position, convertView, parent)
            }

            private fun getCustomView(position: Int, convertView: View?, parent: ViewGroup): View {
                val row = if (convertView != null && convertView !is TextView) convertView
                else LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_custom_spinner_layout, parent, false)
                val label = row.findViewById<View>(R.id.indicatorDesign)
                val label1 = row.findViewById<TextView>(R.id.spinner_item_text)
                label1.text = data[position]
                if (position == 3) label.visibility = View.GONE
                return row
            }
        }
        binding.tvSpinnerContent.adapter = adapter
        binding.tvSpinnerContent.onItemSelectedListener = this
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        val nancostDataFiltered: ArrayList<NancostData?> = arrayListOf()
        when (position) {
            0 -> {
                adapter.setData(nancostDataList)
            }
            1 -> {
                nancostDataList.forEach {
                    if (it?.amountPaid == it?.amountWillPay) {
                        nancostDataFiltered.add(it)
                    }
                }
                adapter.setData(nancostDataFiltered)
            }
            2 -> {
                nancostDataList.forEach {
                    if (it?.amountPaid == 0) {
                        nancostDataFiltered.add(it)
                    }
                }
                adapter.setData(nancostDataFiltered)
            }
            3 -> {
                nancostDataList.forEach {
                    if ((it?.amountPaid != 0) && ((it?.amountPaid ?: 0) < (it?.amountWillPay ?: 0))) {
                        nancostDataFiltered.add(it)
                    }
                }
                adapter.setData(nancostDataFiltered)
            }
        }
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {
    }
}