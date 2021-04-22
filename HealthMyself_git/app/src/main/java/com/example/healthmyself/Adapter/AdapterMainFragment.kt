package com.example.healthmyself.Adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.example.healthmyself.Fragment.*

class AdapterMainFragment(fm : FragmentManager, private val fragmentCount : Int) : FragmentStatePagerAdapter(fm){
    override fun getCount(): Int = fragmentCount

    override fun getItem(position: Int): Fragment {
        return when(position){
            0 -> FragmentMainTimer_Java()
            1 -> FragmentMainCalendar()
            2 -> FragmentMainHome()
            3 -> FragmentMainVideo()
            4 -> FragmentMainSetting()
            else -> FragmentMainHome()
        }
    }

}