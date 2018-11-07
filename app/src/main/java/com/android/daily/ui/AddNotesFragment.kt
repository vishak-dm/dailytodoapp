package com.android.daily.ui

import android.annotation.SuppressLint
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.*
import com.android.daily.R
import android.os.Build
import android.support.design.widget.Snackbar
import android.support.v4.content.ContextCompat
import androidx.navigation.fragment.findNavController
import com.android.daily.utilities.InjectorUtils
import com.android.daily.viewModel.AddNotesViewModel
import com.android.daily.viewModel.SharedViewModel
import com.android.daily.vo.Status
import kotlinx.android.synthetic.main.fragment_add_notes.*
import org.joda.time.DateTime
import timber.log.Timber
import android.view.LayoutInflater
import com.flask.colorpicker.ColorPickerView
import com.flask.colorpicker.OnColorSelectedListener
import com.flask.colorpicker.builder.ColorPickerDialogBuilder
import kotlin.collections.ArrayList


class AddNotesFragment : Fragment(), OnColorSelectedListener {
    override fun onColorSelected(p0: Int) {

    }

    private lateinit var mView: View
    private lateinit var addNotesViewModel: AddNotesViewModel
    private lateinit var sharedViewModel: SharedViewModel
    private var labelList = ArrayList<String>()
    private var noteColor = R.color.white


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
        addDarkToolbar(true)
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
            }
        })

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
            R.id.choose_notes_color -> chooseNotesColors()
        }
        return true
    }

    private fun chooseNotesColors() {
        ColorPickerDialogBuilder
                .with(context)
                .setTitle("Choose color")
                .initialColor(R.color.drab)
                .wheelType(ColorPickerView.WHEEL_TYPE.CIRCLE)
                .density(12)
                .setOnColorSelectedListener(this)
                .setPositiveButton(getString(R.string.ok)) { dialog, selectedColor, allColors ->
                    noteColor = selectedColor
                    activity?.invalidateOptionsMenu()
                }
                .setNegativeButton(getString(R.string.cancel)) { dialog, which -> }
                .build()
                .show()
    }

    override fun onResume() {
        getMainActivity()?.showBackButton()
        super.onResume()
    }

    private fun chooseLabels() {
        findNavController().navigate(R.id.action_addNotesFragment_to_chooseLabelFragment)
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
        addNotesViewModel.addNotes(noteTitle.toString(), noteContents.toString(), createdTime, noteColor, labelList).observe(viewLifecycleOwner, Observer {
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

    @SuppressLint("ResourceAsColor")
    override fun onPrepareOptionsMenu(menu: Menu?) {
        val notesColorItem = menu?.findItem(R.id.choose_notes_color)
        // set your desired icon here based on a flag if you like
        val drawable = ContextCompat.getDrawable(context!!, R.drawable.small_colored_circle)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            drawable?.setTint(noteColor)
        }
        notesColorItem?.icon = drawable
        super.onPrepareOptionsMenu(menu)
    }


}
