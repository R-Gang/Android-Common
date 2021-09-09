package com.gang.library.ui.widget

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.*
import androidx.annotation.DrawableRes
import com.gang.library.R
import com.gang.library.common.utils.gone
import com.gang.library.common.utils.show
import com.makeramen.roundedimageview.RoundedImageView
import org.jetbrains.annotations.NotNull

class BaseSearchBar @JvmOverloads constructor(context:Context, attributeSet: AttributeSet?=null, defInt:Int=0):FrameLayout(context,attributeSet,defInt) {
    private val mView= View.inflate(context, R.layout.base_search_bar,null)
    private val leftView=mView.findViewById<RelativeLayout>(R.id.rl_back_button)
    private val leftText=mView.findViewById<TextView>(R.id.tv_title_left)
    private val leftIv=mView.findViewById<ImageView>(R.id.iv_left)
    private val leftRoundIv=mView.findViewById<RoundedImageView>(R.id.iv_title_left)
    private val ivSearch=mView.findViewById<ImageView>(R.id.iv_search)
    private val vLine=mView.findViewById<View>(R.id.title_line)
    private val etSearch=mView.findViewById<EditText>(R.id.et_search)

    init {
        addView(mView)
    }

    fun setLeftText(text:String){
        leftView.show()
        leftText.text=text
    }

    fun setLeftIvRes(@DrawableRes @NotNull resId:Int){
        leftView.show()
        leftIv.setImageResource(resId)
        leftIv.scaleType=ImageView.ScaleType.CENTER_INSIDE
    }

    fun setLeftRoundIvRes(@DrawableRes @NotNull resId:Int){
        leftView.show()
        leftRoundIv.setImageResource(resId)
    }

    fun setSearchIv(@DrawableRes @NotNull resId:Int){
        ivSearch.setImageResource(resId)
        ivSearch.scaleType=ImageView.ScaleType.CENTER_INSIDE
    }

    fun setSearchIcon(@DrawableRes @NotNull resId:Int){
        etSearch.setCompoundDrawablesWithIntrinsicBounds(resId,0,0,0)
    }

    fun goneLine(){
        vLine.gone()
    }

    fun goneLeftView(){
        leftView.gone()
    }

    fun getSearchView()=etSearch

    fun getSearchIcon()=ivSearch
}