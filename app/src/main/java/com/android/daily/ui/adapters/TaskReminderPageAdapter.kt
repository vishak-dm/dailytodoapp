package com.android.daily.ui.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.android.daily.ui.ReminderFragment
import com.android.daily.ui.ReminderRepeatFragment

class TaskReminderPageAdapter(fm: FragmentManager) : FragmentStatePagerAdapter(fm) {
    override fun getCount(): Int {
        return 2
    }

    override fun getItem(position: Int): Fragment {
        return if (position == 0)
            ReminderFragment()
        else
            ReminderRepeatFragment()
    }
}