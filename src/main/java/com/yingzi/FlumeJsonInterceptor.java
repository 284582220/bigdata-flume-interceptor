package com.yingzi;


import java.util.List;
import java.util.Map;

import org.apache.flume.Context;
import org.apache.flume.Event;
import org.apache.flume.interceptor.Interceptor;

import com.google.common.base.Charsets;
import com.google.common.collect.Lists;

/**
 * @author pengzhe
 * 自定义的一个flume拦截器，用来将json字符串转化成map，再按照指定顺序将map中的value按照指定格式输出
 * 例子：
 * 输入：String JsonString = {"timestamp":1554256445,"rect_id":324387192,"yz_id":"4zorOttoYKwG0000","locationId":"3130_324387167_324387192","device_item_id":19229,"device_id":1,"tenant_id":178985597786914818,"mac_address":"0810234533dc","slave_id":"006","iot_id":"66c082004e354b7ba780d376a93c7eb7","metric":{"device_status":0.0,"g_sensor":0.0,"weight":520.0,"humidity":40.0,"env_temp":40.0,"pig_temp":35.0,"sow_action":1.0,"boar_action":1.0,"distance_1":120.0,"distance_2":100.0,"distance_vertical":88.0,"NH3":0.05,"CO2":0.1,"illumination":8.1,"water_meter_reading":30.0,"oestrus_percent":90.0}}
 * 输出：1554256445,324387192,4zorOttoYKwG0000,3130_324387167_324387192,...
 */
public class FlumeJsonInterceptor implements Interceptor {

    static final String SEPARATORSTR = ",";

    //以keys数组中的元素顺序，输出jsonMap中对应key的value的值
    static final String[] KEYS = {"timestamp", "rect_id", "yz_id", "locationId", "device_item_id", "device_id", "tenant_id", "mac_address", "slave_id", "iot_id", "metric"};

    static final String[] METRIC_KEYS = {"device_status", "g_sensor", "weight", "humidity", "env_temp", "pig_temp", "sow_action", "boar_action", "distance_1", "distance_2", "distance_vertical", "NH3", "CO2", "illumination", "water_meter_reading", "oestrus_percent"};

    @Override
    public void close() {
    }

    @Override
    public void initialize() {
    }

    @Override
    public Event intercept(Event event) {
        String JsonString = new String(event.getBody(), Charsets.UTF_8);
        Map jsonMap = JsonUtil.jsonToMap(JsonString);

        StringBuffer finalStr = new StringBuffer();
        for (String keyStr : KEYS) {
            if (keyStr.equals("metric")) {
                Map<String, Object> jsonValue = (Map<String, Object>) jsonMap.get(keyStr);
                for (String metricKey : METRIC_KEYS) {
                    Object value = jsonValue.get(metricKey);
                    finalStr.append(value).append(SEPARATORSTR);
                }
            } else {
                finalStr.append(jsonMap.get(keyStr)).append(SEPARATORSTR);
            }
        }
        String endStr = finalStr.substring(0, finalStr.length() - 1);
        event.setBody(endStr.getBytes());
        return event;
    }

    @Override
    public List<Event> intercept(List<Event> events) {
        List<Event> intercepted = Lists.newArrayListWithCapacity(events.size());
        for (Event event : events) {
            Event interceptedEvent = intercept(event);
            if (interceptedEvent != null) {
                intercepted.add(interceptedEvent);
            }
        }
        return intercepted;
    }

    public static class Builder implements Interceptor.Builder {
        @Override
        public void configure(Context context) {
        }

        @Override
        public Interceptor build() {
            return new FlumeJsonInterceptor();
        }
    }
}
