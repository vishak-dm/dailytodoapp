package com.android.daily.ui


import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.android.daily.R
import com.android.daily.repository.model.NotesData
import com.android.daily.ui.adapters.MyNotesAdapter
import com.android.daily.ui.adapters.NotesClicklistener
import com.android.daily.ui.adapters.TasksListAdapter
import com.android.daily.utilities.CommonUtils
import com.android.daily.utilities.InjectorUtils
import com.android.daily.utilities.views.GridSpacingItemDecoration
import com.android.daily.viewModel.MyThoughtsViewModel
import com.android.daily.vo.Status
import kotlinx.android.synthetic.main.fragment_my_goals_details.*
import kotlinx.android.synthetic.main.fragment_my_projects.*
import kotlinx.android.synthetic.main.my_thoughts_layout.*
import timber.log.Timber
import java.util.*

class MyThoughtsFragment : androidx.fragment.app.Fragment() {
    private lateinit var mView: View
    private lateinit var myThoughtsViewModel: MyThoughtsViewModel
    private var notesList = Collections.emptyList<NotesData>()
    private lateinit var notesAdapter: MyNotesAdapter
    private val noteClickListener: NotesClicklistener = this::onNoteClicked


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        mView = inflater.inflate(R.layout.my_thoughts_layout, container, false)
        return mView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getMainActivity()?.setToolBarTitle(getString(R.string.my_thoughts))
        getMainActivity()?.showBottomNavigationView()
        setupRecyclerView()
        myThoughtsViewModel = ViewModelProviders.of(this, InjectorUtils.provideMyThoughtsViewModelFactory()).get(MyThoughtsViewModel::class.java)
        add_notes_button.setOnClickListener { findNavController().navigate(R.id.action_thoughtsFragment_to_addNotesFragment) }
        add_notes_floating_button.setOnClickListener { findNavController().navigate(R.id.action_thoughtsFragment_to_addNotesFragment) }
        my_notes_progressbar.visibility = View.VISIBLE
        myThoughtsViewModel.getMyNotes().observe(viewLifecycleOwner, Observer {
            if (it != null) {
                if (it.status == Status.ERROR) {
                    Timber.i("Error while getting my notes %s", it.message)
                } else if (it.status == Status.SUCCESS) {
                    notesList = it.data
                    if (notesList.isEmpty()) {
                        //show the empty state
                        empty_notes_state_constraint_layout.visibility = View.VISIBLE
                    } else {
                        //we got the list hide the empty state and show the recylcer view
                        empty_notes_state_constraint_layout.visibility = View.GONE
                        my_notes_recycler_view.visibility = View.VISIBLE
                        add_notes_floating_button.visibility = View.VISIBLE
                        it.data?.let { it1 -> notesAdapter.setData(it1) }
                    }
                }
                my_notes_progressbar.visibility = View.GONE
            }
        })
    }

    private fun setupRecyclerView() {
        notesAdapter = MyNotesAdapter(context!!, notesList, noteClickListener)
        val mLayoutManager = androidx.recyclerview.widget.GridLayoutManager(context, 2)
        my_notes_recycler_view.layoutManager = mLayoutManager
        my_notes_recycler_view.addItemDecoration(GridSpacingItemDecoration(2, CommonUtils.dpToPx(10, resources), true))
        my_notes_recycler_view.itemAnimator = androidx.recyclerview.widget.DefaultItemAnimator()
        my_notes_recycler_view.adapter = notesAdapter
    }


    private fun getMainActivity(): MainActivity? {
        return if (activity is MainActivity)
            activity as MainActivity
        else
            null
    }

    private fun onNoteClicked(note: NotesData) {
        val directions = MyThoughtsFragmentDirections.actionThoughtsFragmentToAddNotesFragment().setNote(note)
        findNavController().navigate(directions)
    }


}
