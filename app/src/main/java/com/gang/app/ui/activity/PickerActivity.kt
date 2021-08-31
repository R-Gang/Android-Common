package com.gang.app.ui.activity

import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.bigkoo.pickerview.builder.OptionsPickerBuilder
import com.gang.app.R
import com.gang.app.data.ProvinceData
import com.gang.app.data.ProvinceData.Companion.options1Items
import com.gang.app.data.ProvinceData.Companion.options2Items
import kotlinx.android.synthetic.main.activity_picker.*

/**
 * 1.地址选择器底部弹框
 * 2.页面选择器
 */
class PickerActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_picker)
        showPickerView.setOnClickListener { showPickerView() }
    }

    private fun showPickerView() { // 城市弹出选择器

    }

    companion object {

        private var pname: String? = null
        private var cname: String? = null
        private var dname: String? = null
    }

}