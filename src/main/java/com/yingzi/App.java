package com.yingzi;


import java.util.Map;

/**
 * Hello world!
 */
public class App {
    public static void main(String args[]) {

        String JsonString = "{\"timestamp\":1554256445,\"rect_id\":324387192,\"yz_id\":\"4zorOttoYKwG0000\",\"locationId\":\"3130_324387167_324387192\",\"device_item_id\":19229,\"device_id\":1,\"tenant_id\":178985597786914818,\"mac_address\":\"0810234533dc\",\"slave_id\":\"006\",\"iot_id\":\"66c082004e354b7ba780d376a93c7eb7\",\"metric\":{\"device_status\":0.0,\"g_sensor\":0.0,\"weight\":520.0,\"humidity\":40.0,\"env_temp\":40.0,\"pig_temp\":35.0,\"sow_action\":1.0,\"boar_action\":1.0,\"distance_1\":120.0,\"distance_2\":100.0,\"distance_vertical\":88.0,\"NH3\":0.05,\"CO2\":0.1,\"illumination\":8.1,\"water_meter_reading\":30.0,\"oestrus_percent\":90.0}}";
        Map jsonMap = JsonUtil.jsonToMap(JsonString);
        StringBuffer finalStr = new StringBuffer();
        for (String keyStr : FlumeJsonInterceptor.KEYS) {
            if(keyStr.equals("metric")){
                Map<String, Object> jsonValue = (Map<String, Object>) jsonMap.get(keyStr);
                for(String metricKey : FlumeJsonInterceptor.METRIC_KEYS){
                    Object value = jsonValue.get(metricKey);
                    finalStr.append(value).append(FlumeJsonInterceptor.SEPARATORSTR);
                }
            }else{
                finalStr.append(jsonMap.get(keyStr)).append(FlumeJsonInterceptor.SEPARATORSTR);
            }
        }
        String endStr = finalStr.substring(0,finalStr.length() -1);
        System.out.println(endStr);
    }
}
