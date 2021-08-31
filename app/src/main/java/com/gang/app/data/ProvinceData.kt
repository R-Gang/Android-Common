package com.gang.app.data

import com.gang.app.ui.bean.JsonBean
import com.gang.library.BaseApplication
import com.gang.library.common.utils.ResUtils
import com.google.gson.Gson
import org.json.JSONArray

/**
 *
 * @ProjectName:    JieTu
 * @Package:        com.jietu.software.app.data
 * @ClassName:      AddressData
 * @Description:     java类作用描述
 * @Author:         haoruigang
 * @CreateDate:     8/23/21 4:54 PM
 * @UpdateUser:     更新者：
 * @UpdateDate:     8/23/21 4:54 PM
 * @UpdateRemark:   更新说明：
 * @Version:        AddressData
 */
class ProvinceData {

    companion object {

        fun initJsonData() { //解析数据
            val JsonData: String = ResUtils.readAssetsText(
                BaseApplication.appContext,
                "province.json"
            ) //获取assets目录下的json文件数据
            val jsonBean: ArrayList<JsonBean> = parseData(JsonData) //用Gson 转成实体
            options1Items = jsonBean
            for (i in jsonBean.indices) { //遍历省份

                val CityList = ArrayList<String>() //该省的城市列表（第二级）
                val Province_AreaList =
                    ArrayList<ArrayList<String>>() //该省的所有地区列表（第三极）
                for (c in 0 until jsonBean[i].city?.size!!) { //遍历该省份的所有城市
                    val CityName: String? = jsonBean[i].city?.get(c)?.name
                    if (CityName != null) {
                        CityList.add(CityName)
                    } //添加城市
                    val City_AreaList = ArrayList<String>() //该城市的所有地区列表
                    //如果无地区数据，建议添加空字符串，防止数据为null 导致三个选项长度不匹配造成崩溃
                    if (jsonBean[i].city?.get(c)?.area == null
                        || jsonBean[i].city?.get(c)?.area?.size == 0
                    ) {
                        City_AreaList.add("")
                    } else {
                        jsonBean[i].city?.get(c)?.area?.let { City_AreaList.addAll(it) }

                        /**
                         * 添加地区数据
                         */
                        jsonBean[i].city?.get(c)?.name?.let {
                            options6Items.put(it, City_AreaList)
                        }
                    }
                    Province_AreaList.add(City_AreaList) //添加该省所有地区数据
                }

                /**
                 * 添加城市数据
                 */
                options2Items.add(CityList)
                /**
                 * 添加地区数据
                 */
                options3Items.add(Province_AreaList)

                /**
                 * 添加省数据
                 */
                jsonBean[i].name?.let { options4Items.add(it) }

                /**
                 * 添加城市数据
                 */
                jsonBean[i].name?.let { options5Items.put(it, CityList) }

            }

        }

        fun parseData(result: String?): ArrayList<JsonBean> { //Gson 解析
            val detail = ArrayList<JsonBean>()
            try {
                val data = JSONArray(result)
                val gson = Gson()
                for (i in 0 until data.length()) {
                    val entity: JsonBean =
                        gson.fromJson(data.optJSONObject(i).toString(), JsonBean::class.java)
                    detail.add(entity)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
            return detail
        }

        var options1Items = ArrayList<JsonBean>()
        val options2Items = ArrayList<ArrayList<String>>()
        val options3Items = ArrayList<ArrayList<ArrayList<String>>>()

        var options4Items = arrayListOf<String>()
        var options5Items = hashMapOf<String, List<String>>()
        var options6Items = hashMapOf<String, List<String>>()
    }
}