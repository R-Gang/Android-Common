package com.gang.app.ui.fragment

import android.os.Bundle
import android.view.View
import com.gang.app.R
import com.gang.library.ui.fragment.BaseFragment

class ItemFragment : BaseFragment() {

    fun newInstance(): ItemFragment {
        val fragment = ItemFragment()
        val args = Bundle()
        fragment.arguments = args
        return fragment
    }

    override val layoutId: Int
        get() = R.layout.fragment_item_list

    override fun initView(view: View?, savedInstanceState: Bundle?) {
    }

    override fun initData() {
    }
}