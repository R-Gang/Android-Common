package com.gang.app.common.view.picker;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;

import com.gang.app.R;
import com.gang.app.data.ProvinceData;
import com.wx.wheelview.adapter.ArrayWheelAdapter;
import com.wx.wheelview.util.WheelUtils;
import com.wx.wheelview.widget.WheelView;

/**
 * @ProjectName: JieTu
 * @Package: com.jietu.software.app.common.view.picker
 * @ClassName: ProvincePickerView
 * @Description: java类作用描述
 * @Author: haoruigang
 * @CreateDate: 8/24/21 3:51 PM
 * @UpdateUser: 更新者：
 * @UpdateDate: 8/24/21 3:51 PM
 * @UpdateRemark: 更新说明：
 * @Version:
 */
public class ProvincePickerView extends RelativeLayout {

    public ProvincePickerView(Context context) {
        this(context, null);
    }

    public ProvincePickerView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ProvincePickerView(Context context, AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    public ProvincePickerView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }

    public void init(Context context) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.pickerview_options, null);
        LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        addView(view, params);


        WheelView.WheelViewStyle style = new WheelView.WheelViewStyle();
        style.selectedTextSize = 20;
        style.textSize = 16;
        style.holoBorderColor = Color.argb(1, 250, 84, 28);
//        style.selectedTextColor = Color.argb(1, 255, 255, 255);
//        style.textColor = Color.parseColor("#333333");

        WheelView mainWheelView = findViewById(R.id.ProvinceWV);
        mainWheelView.setWheelAdapter(new ArrayWheelAdapter(context));
        mainWheelView.setWheelData(ProvinceData.Companion.getOptions4Items());
        mainWheelView.setSkin(WheelView.Skin.None);
        mainWheelView.setWheelSize(5);
        mainWheelView.setSelection(3);
        mainWheelView.setStyle(style);
        mainWheelView.setOnWheelItemSelectedListener((WheelView.OnWheelItemSelectedListener<String>) (position, data) -> WheelUtils.log("selected:" + position));

        WheelView subWheelView = findViewById(R.id.CityWV);
        subWheelView.setWheelAdapter(new ArrayWheelAdapter(context));
        subWheelView.setWheelData(ProvinceData.Companion.getOptions5Items().get(ProvinceData.Companion.getOptions4Items().get(mainWheelView.getSelection())));
        subWheelView.setSkin(WheelView.Skin.None);
        subWheelView.setWheelSize(5);
        subWheelView.setSelection(0);
        subWheelView.setStyle(style);
        subWheelView.setOnWheelItemSelectedListener((WheelView.OnWheelItemSelectedListener<String>) (position, data) -> WheelUtils.log("selected:" + position));
        mainWheelView.join(subWheelView);
        mainWheelView.joinDatas(ProvinceData.Companion.getOptions5Items());

        WheelView childWheelView = findViewById(R.id.AreaWV);
        childWheelView.setWheelAdapter(new ArrayWheelAdapter(context));
        childWheelView.setWheelData(ProvinceData.Companion.getOptions6Items().get(ProvinceData.Companion.getOptions5Items().get(ProvinceData.Companion.getOptions4Items().get(mainWheelView
                .getSelection())).get(subWheelView.getSelection())));
        childWheelView.setSkin(WheelView.Skin.None);
        childWheelView.setWheelSize(5);
        childWheelView.setSelection(0);
        childWheelView.setStyle(style);
        childWheelView.setOnWheelItemSelectedListener((WheelView.OnWheelItemSelectedListener<String>) (position, data) -> WheelUtils.log("selected:" + position));
        subWheelView.join(childWheelView);
        subWheelView.joinDatas(ProvinceData.Companion.getOptions6Items());
    }


}
