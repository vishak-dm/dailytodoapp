package com.android.daily.ui

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.*
import com.android.daily.R
import android.view.WindowManager
import android.os.Build
import android.support.design.widget.Snackbar
import android.support.v4.content.ContextCompat
import androidx.navigation.fragment.findNavController
import com.android.daily.utilities.InjectorUtils
import com.android.daily.viewModel.AddNotesViewModel
import com.android.daily.vo.Status
import kotlinx.android.synthetic.main.fragment_add_notes.*
import org.joda.time.DateTime
import timber.log.Timber


class AddNotesFragment : Fragment() {
    private lateinit var mView: View
    private lateinit var addNotesViewModel: AddNotesViewModel
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
        addNotesViewModel = ViewModelProviders.of(this, InjectorUtils.provideAddNotesViewModelFactory()).get(AddNotesViewModel::class.java)
    }


    private fun getMainActivity(): MainActivity? {
        if (activity is MainActivity)
            return activity as MainActivity
        else
            return null
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        inflater?.inflate(R.menu.notes_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.done_add_notes -> addNotes()
            R.id.add_notes_labels -> chooseLabels()
            R.id.choose_notes_color -> chooseNotesColors()
        }
        return true
    }

    private fun chooseNotesColors() {

    }

    private fun chooseLabels() {


    }

    private fun addNotes() {
        val noteTitle = note_title_edit_text.text
        val noteContents = note_contents_edit_text.text
        val createdTime = DateTime.now().millis
        if (noteTitle.isNullOrEmpty() || noteContents.isNullOrEmpty()) {
            Snackbar.make(mView, R.string.empty_note, Snackbar.LENGTH_LONG).show()
            return
        }
        add_note_progressbar.visibility = View.VISIBLE
        addNotesViewModel.addNotes(noteTitle.toString(), noteContents.toString(), createdTime).observe(viewLifecycleOwner, Observer {
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
