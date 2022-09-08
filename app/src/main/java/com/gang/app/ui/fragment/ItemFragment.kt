package com.gang.app.ui.fragment

import android.os.Bundle
import com.gang.app.R
import com.gang.library.base.BaseFragment

class ItemFragment : BaseFragment() {

    companion object {
        fun newInstance(args: Bundle): ItemFragment {
            val fragment = ItemFragment()
            fragment.arguments = args
            return fragment
        }
    }

    override val layoutId: Int
        get() = R.layout.fragment_item_list

    override fun initView(savedInstanceState: Bundle?) {
    }

    override fun initData() {
    }
}