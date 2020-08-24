package com.gang.library.ui.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter

/**
 * Created by haoruigang on 2016/1/20.
 */
class VPAdapter(
    fm: FragmentManager?,
    private var fragmentList: List<Fragment>,
    private var mTitleList: ArrayList<String>
) : FragmentPagerAdapter(fm!!) {

    override fun getItem(position: Int): Fragment {
        return fragmentList[position]
    }

    override fun getCount(): Int {
        return if (fragmentList.isNotEmpty()) fragmentList.size else 0
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return mTitleList[position]
    }

    // 刷新fragment
    fun setFragments(
        fm: FragmentManager,
        fragments: ArrayList<Fragment>?,
        mTitleList: ArrayList<String>
    ) {
        if (fragments != null) {
            val ft = fm.beginTransaction()
            for (f in fragments) {
                ft.remove(f)
            }
            ft.commitAllowingStateLoss()
            fm.executePendingTransactions()
        }
        if (fragments != null) {
            fragmentList = fragments
        }
        this.mTitleList = mTitleList
        notifyDataSetChanged()
    }

}