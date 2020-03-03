package com.android.daily.ui


import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import android.text.Editable
import android.text.TextWatcher
import android.view.*
import androidx.navigation.fragment.findNavController
import com.android.daily.R
import com.android.daily.ui.adapters.LabelAdapter
import com.android.daily.viewModel.SharedViewModel
import kotlinx.android.synthetic.main.fragment_choose_label.*
import kotlin.collections.ArrayList


class ChooseLabelFragment : androidx.fragment.app.Fragment(), TextWatcher {

    private lateinit var mView: View
    private var sharedViewModel: SharedViewModel? = null
    private var listOfLabels = ArrayList<String>()
    private lateinit var labelAdapter: LabelAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        mView = inflater.inflate(R.layout.fragment_choose_label, container, false)
        return mView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getMainActivity()?.showLabelEditText()
        configureRecyclerView()
        listOfLabels = arguments?.get("labels") as ArrayList<String>
        if (listOfLabels.isNotEmpty()) {
            label_recycler_view.visibility = View.VISIBLE
            labelAdapter.setData(listOfLabels)
            add_label_button.visibility = View.VISIBLE
        } else {
            label_recycler_view.visibility = View.GONE
        }
        ViewCompat.setElevation(getMainActivity()?.getToolbar()!!, 8.0f)
        getMainActivity()?.getLabeleEditText()?.setHintTextColor(ContextCompat.getColor(context!!, R.color.white))
        getMainActivity()?.showBackButton()
        sharedViewModel = activity?.run {
            ViewModelProviders.of(this).get(SharedViewModel::class.java)
        } ?: throw Exception("Invalid activity")
        create_label_constraint_layout.visibility = View.GONE
        getMainActivity()?.getLabeleEditText()?.addTextChangedListener(this)
        getMainActivity()?.getLabeleEditText()?.setTextColor(ContextCompat.getColor(context!!, R.color.white))

        create_label_constraint_layout.setOnClickListener {
            val label = getMainActivity()?.getLabeleEditText()?.text.toString()
            if (label.isNotEmpty()) {
                listOfLabels.add(label)
                create_label_constraint_layout.visibility = View.GONE
                label_recycler_view.visibility = View.VISIBLE
                labelAdapter.setData(listOfLabels)
                getMainActivity()?.getLabeleEditText()?.setText("")
                add_label_button.visibility = View.VISIBLE
            }
        }

        add_label_button.setOnClickListener {
            sharedViewModel?.setNotesLableList(labelAdapter.getChoosenLabels())
            findNavController().popBackStack()
        }


    }

    private fun configureRecyclerView() {
        labelAdapter = LabelAdapter(listOfLabels)
        val mLayoutManager = androidx.recyclerview.widget.LinearLayoutManager(context)
        label_recycler_view.layoutManager = mLayoutManager
        label_recycler_view.itemAnimator = androidx.recyclerview.widget.DefaultItemAnimator()
        label_recycler_view.adapter = labelAdapter
    }


    private fun getMainActivity(): MainActivity? {
        return if (activity is MainActivity)
            activity as MainActivity
        else
            null
    }

    override fun onDetach() {
        super.onDetach()
        getMainActivity()?.getLabeleEditText()?.removeTextChangedListener(this)
        getMainActivity()?.getLabeleEditText()?.text?.clear()
        getMainActivity()?.hideLabelEditText()
        ViewCompat.setElevation(getMainActivity()?.getToolbar()!!, 0.0f)
    }

    override fun afterTextChanged(text: Editable?) {
        if (text.toString().isEmpty()) {
            create_label_constraint_layout?.visibility = View.GONE
        } else {
            create_label_constraint_layout?.visibility = View.VISIBLE
        }
    }

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
    }

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        val text = s.toString()
        if (text.isNotEmpty()) {
            label_recycler_view?.visibility = View.GONE
            create_label_constraint_layout?.visibility = View.VISIBLE
            label_name.text = String.format(getString(R.string.create_label), text)
        }
    }

}
