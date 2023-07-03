package com.sszt.basis.util;

import android.content.Context;
import android.content.res.AssetManager;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * @author: zcr
 * @date: 2022/4/2
 * @Description:
 */
public class GetJsonDataUtil {
    public static String getJson(Context context,String fileName){

        StringBuilder stringBuilder = new StringBuilder();

        try {
            AssetManager assets = context.getAssets();
            BufferedReader reader = new BufferedReader(new InputStreamReader(assets.open(fileName)));
            String line;
            while ((line = reader.readLine()) != null){
                stringBuilder.append(line);
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return stringBuilder.toString();
    }
}
