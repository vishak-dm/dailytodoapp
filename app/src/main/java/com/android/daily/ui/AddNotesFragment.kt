package com.android.daily.ui

import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.*
import com.android.daily.R
import android.os.Build
import com.google.android.material.snackbar.Snackbar
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.navigation.fragment.findNavController
import com.android.daily.utilities.InjectorUtils
import com.android.daily.viewModel.AddNotesViewModel
import com.android.daily.viewModel.SharedViewModel
import com.android.daily.vo.Status
import kotlinx.android.synthetic.main.fragment_add_notes.*
import org.joda.time.DateTime
import timber.log.Timber
import android.view.LayoutInflater
import androidx.core.os.bundleOf
import com.android.daily.ui.adapters.NotesLabelsAdapter
import kotlin.collections.ArrayList
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.android.daily.ui.adapters.NoteLabelClickListener


class AddNotesFragment : androidx.fragment.app.Fragment() {
    private lateinit var mView: View
    private lateinit var addNotesViewModel: AddNotesViewModel
    private lateinit var sharedViewModel: SharedViewModel
    private var labelList = ArrayList<String>()
    private lateinit var labelAdapter: NotesLabelsAdapter
    private val labelClickListener: NoteLabelClickListener = this::onLabelCLicked

    private fun onLabelCLicked() {
        chooseLabels()
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        mView = inflater.inflate(R.layout.fragment_add_notes, container, false)
        return mView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)
        getMainActivity()?.hideBottomNavigationView()
        getMainActivity()?.setToolBarTitle("")
        configureRecylerView()
        addDarkToolbar(true)
        setNote()
        getMainActivity()?.getBackButton()?.setOnClickListener {
            findNavController().popBackStack()
        }
        addNotesViewModel = ViewModelProviders.of(this, InjectorUtils.provideAddNotesViewModelFactory()).get(AddNotesViewModel::class.java)
        sharedViewModel = activity?.run {
            ViewModelProviders.of(this).get(SharedViewModel::class.java)
        } ?: throw Exception("Invalid activity")
        sharedViewModel.getNoteLabelsLiveData().observe(viewLifecycleOwner, Observer {
            if (it != null) {
                labelList = it as ArrayList<String>
                labelAdapter.setData(labelList)
            }
        })

    }

    private fun setNote() {
        val note = arguments?.let { AddNotesFragmentArgs.fromBundle(it).note }
        if (note != null) {
            note_title_edit_text.setText(note.t)
            note_contents_edit_text.setText(note.c)
            labelList = note.nl as ArrayList<String>
            labelAdapter.setData(note.nl)
        }
    }

    private fun configureRecylerView() {
        labelAdapter = NotesLabelsAdapter(labelList, labelClickListener)
        val mLayoutManager = StaggeredGridLayoutManager(4, LinearLayoutManager.VERTICAL)
        add_notes_label_recycler_view.layoutManager = mLayoutManager
        add_notes_label_recycler_view.itemAnimator = DefaultItemAnimator()
        add_notes_label_recycler_view.adapter = labelAdapter
        add_notes_label_recycler_view.setOnClickListener {
            chooseLabels()
        }
    }


    private fun getMainActivity(): MainActivity? {
        return if (activity is MainActivity)
            activity as MainActivity
        else
            null
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        inflater?.inflate(R.menu.notes_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.done_add_notes -> addNotes()
            R.id.add_notes_labels -> chooseLabels()
        }
        return true
    }


    override fun onResume() {
        getMainActivity()?.showBackButton()
        super.onResume()
    }

    private fun chooseLabels() {
        findNavController().navigate(R.id.action_addNotesFragment_to_chooseLabelFragment, bundleOf(Pair("labels", labelList)))
    }

    private fun addNotes() {
        val noteTitle = note_title_edit_text.text
        val noteContents = note_contents_edit_text.text
        val createdTime = DateTime.now().millis
        if (noteTitle.isNullOrEmpty() || noteContents.isNullOrEmpty()) {
            Snackbar.make(mView, R.string.empty_note, Snackbar.LENGTH_LONG).show()
            return
        }
        val note = arguments?.let { AddNotesFragmentArgs.fromBundle(it).note }
        add_note_progressbar.visibility = View.VISIBLE

        if (note != null) {
            //then update notes
            addNotesViewModel.updateNote(noteTitle.toString(), noteContents.toString(), createdTime, labelList, note.id).observe(viewLifecycleOwner, Observer {
                if (it != null) {
                    if (it.status == Status.ERROR) {
                        Timber.i("Error while updating notes %s", it.message)
                    } else if (it.status == Status.SUCCESS) {
                        Timber.i("Successfully updated note")
                        findNavController().popBackStack()
                    }
                    add_note_progressbar.visibility = View.GONE
                }
            })
        } else {
            addNotesViewModel.addNotes(noteTitle.toString(), noteContents.toString(), createdTime, labelList).observe(viewLifecycleOwner, Observer {
                if (it != null) {
                    if (it.status == Status.ERROR) {
                        Timber.i("Error while adding notes %s", it.message)
                    } else if (it.status == Status.SUCCESS) {
                        Timber.i("Successfully added note")
                        findNavController().popBackStack()
                    }
                    add_note_progressbar.visibility = View.GONE
                }
            })
        }


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

}
