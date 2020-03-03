package com.android.daily.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.android.daily.R

class ReminderFragment : Fragment()  {
//    override fun onRemindModeSelected() {
//        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
//    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.reminder_fragment , container , false)
    }
}