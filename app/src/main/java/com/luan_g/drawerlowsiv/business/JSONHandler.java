package com.luan_g.drawerlowsiv.business;

import com.luan_g.drawerlowsiv.entity.FileEntity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class JSONHandler {
    public static List<FileEntity> createFileList(String jsonString, int rootId, int level) {
        JSONObject json = null;
        List<FileEntity> list = new ArrayList<>();

        try {
            json = new JSONObject(jsonString);
            JSONArray jsonArray = json.getJSONArray("files");

            for (int i = 0; i < jsonArray.length(); i++) {
                String aux = jsonArray.getString(i);
                list.add(new FileEntity(i, aux, rootId, level));
            }
        } catch (
                JSONException jsonException) {
            jsonException.printStackTrace();
        }

        return list;
    }
}
