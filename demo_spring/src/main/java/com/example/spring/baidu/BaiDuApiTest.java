package com.example.spring.baidu;

import cn.hutool.http.HttpUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author pang
 * @version 1.0
 * @date 2024-07-03 13:32
 * @since 1.8
 **/
public class BaiDuApiTest {

    public static void main(String[] args) {

        Map<String, String> params = new HashMap<String, String>();
        List<Object> list = new ArrayList<Object>();
        String originDouble = HttpUtil.get("http://api.map.baidu.com/geocoder/v3/?output=json&ak" +
                "=bpL7HGMFl9ZfMK1t8ICcPsn5RS2Ahdam&address="
                        + "开封市顺河回族区北门大街");
        com.alibaba.fastjson.JSONObject jsonObjectOri = com.alibaba.fastjson.JSONObject.parseObject(originDouble);
        String status = jsonObjectOri.getString("status");
        if (status == "0" || "0".equals(status)) {// 解析的地址不为空时 进行值的获取
            String oriLng = jsonObjectOri.getJSONObject("result").getJSONObject("location").getString("lng");// 经度值
            String oriLat = jsonObjectOri.getJSONObject("result").getJSONObject("location").getString("lat");// 纬度值
            String location = oriLat + "," + oriLng;
            String result = HttpUtil.get(
                    "http://api.map.baidu.com/geocoder/v2/?output=json&ak=bpL7HGMFl9ZfMK1t8ICcPsn5RS2Ahdam&location="
                            + location);
            com.alibaba.fastjson.JSONObject jsonObjectAdds = com.alibaba.fastjson.JSONObject.parseObject(result);
            String province = jsonObjectAdds.getJSONObject("result").getJSONObject("addressComponent")
                    .getString("province");// 省
            String city = jsonObjectAdds.getJSONObject("result").getJSONObject("addressComponent").getString("city");// 市

            System.out.println("province:" + province);
            System.out.println("city:" + city);
        }
    }

}
