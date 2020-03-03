package com.android.daily.ui


import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.core.content.ContextCompat
import android.view.*
import androidx.navigation.fragment.findNavController
import com.android.daily.R
import com.android.daily.ui.NotesDetailFragmentArgs.fromBundle
import com.android.daily.ui.adapters.NoteLabelClickListener
import com.android.daily.ui.adapters.NotesLabelsAdapter
import kotlinx.android.synthetic.main.fragment_notes_detail.*

class NotesDetailFragment : androidx.fragment.app.Fragment() {
    private lateinit var mView: View
    private val note by lazy {
        arguments?.let { fromBundle(it).note }
    }
    private var labelList = ArrayList<String>()
    private lateinit var labelAdapter: NotesLabelsAdapter
    private val labelClickListener: NoteLabelClickListener = this::onLabelCLicked

    private fun onLabelCLicked() {

    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        mView = inflater.inflate(R.layout.fragment_notes_detail, container, false)
        return mView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)
        addDarkToolbar(true)
        getMainActivity()?.hideBottomNavigationView()
        getMainActivity()?.getBackButton()?.setOnClickListener {
            findNavController().popBackStack()
        }
        getMainActivity()?.showBackButton()
        getMainActivity()?.setToolBarTitle("")
        configureRecylerView()
        setNote()
    }

    private fun setNote() {
        note_details_title_edit_text.setText(note?.t)
        note_details_contents_edit_text.setText(note?.c)
        labelAdapter.setData(note!!.nl)
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        inflater?.inflate(R.menu.note_details_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onDetach() {
        super.onDetach()
        getMainActivity()?.hideBackButton()
        addDarkToolbar(false)
    }

    private fun addDarkToolbar(isDark: Boolean) {
        val backgroundColor: Int = if (isDark) {
            R.color.colorPrimary
        } else {
            R.color.background_light
        }
        getMainActivity()?.getToolbar()?.setBackgroundColor(ContextCompat.getColor(context!!, backgroundColor))
        if (Build.VERSION.SDK_INT >= 21) {
            val window = activity?.window
            window?.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window?.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            window?.statusBarColor = ContextCompat.getColor(context!!, backgroundColor)
        }
    }

    private fun getMainActivity(): MainActivity? {
        return if (activity is MainActivity)
            activity as MainActivity
        else
            null
    }

    private fun configureRecylerView() {
        labelAdapter = NotesLabelsAdapter(labelList,labelClickListener)
        val mLayoutManager = androidx.recyclerview.widget.StaggeredGridLayoutManager(4, androidx.recyclerview.widget.LinearLayoutManager.VERTICAL)
        notes__details_label_recycler_view.layoutManager = mLayoutManager
        notes__details_label_recycler_view.itemAnimator = androidx.recyclerview.widget.DefaultItemAnimator()
        notes__details_label_recycler_view.adapter = labelAdapter
    }


}
