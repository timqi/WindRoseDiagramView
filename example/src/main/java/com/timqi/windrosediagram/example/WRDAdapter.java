package com.timqi.windrosediagram.example;

import com.timqi.windrosediagram.WindRoseDiagramAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by qiqi on 16/5/10.
 */
public class WRDAdapter extends WindRoseDiagramAdapter {

  ArrayList<Map<String, Object>> dataList;

  public WRDAdapter() {
    this.dataList = buildData();
  }

  @Override
  public int getCount() {
    return dataList.size();
  }

  @Override
  public String getName(int position) {
    return (String) dataList.get(position).get("name");
  }

  @Override
  public float getPercent(int position) {
    return (float) dataList.get(position).get("percent");
  }

  private ArrayList<Map<String, Object>> buildData() {

    ArrayList<Map<String, Object>> result = new ArrayList<>();

    Map<String, Object> map0 = new HashMap<>();
    map0.put("name", "听力");
    map0.put("percent", 0.54f);
    result.add(map0);

    Map<String, Object> map1 = new HashMap<>();
    map1.put("name", "阅读");
    map1.put("percent", 0.62f);
    result.add(map1);

    Map<String, Object> map2 = new HashMap<>();
    map2.put("name", "写作");
    map2.put("percent", 0.35f);
    result.add(map2);

    Map<String, Object> map3 = new HashMap<>();
    map3.put("name", "口语");
    map3.put("percent", 0.73f);
    result.add(map3);

    Map<String, Object> map4 = new HashMap<>();
    map4.put("name", "综合");
    map4.put("percent", 0.57f);
    result.add(map4);

    Map<String, Object> map5 = new HashMap<>();
    map5.put("name", "丁丁");
    map5.put("percent", 0.92f);
    result.add(map5);

    return result;
  }
}
