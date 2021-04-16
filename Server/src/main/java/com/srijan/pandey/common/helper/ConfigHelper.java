/**
 * --- ConfigHelper
 * Util Class which I havent used
 * 02/28/2020
 */

package com.srijan.pandey.common.helper;


import java.io.*;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ConfigHelper {


    public static Map<String, String> readConfig(String configLocation) {

        byte[] val = new byte[100];
        int size = 0;
        String configStr = "";
        try {

            File file = new File(configLocation);
            BufferedInputStream bufferedInputStream = new BufferedInputStream(new FileInputStream(file));

            while ((size = bufferedInputStream.read(val, 0, 100)) != -1) {
                String curBuffer = new String(val, 0, size);
                configStr += curBuffer;
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        configStr.replace(" ", "");

        List<String> configList = Arrays.asList(configStr.split("\n"));
        Map<String, String> configMap = new HashMap<>();

        for (String config : configList) {
            if(config.length() == 0)
                continue;
            String[] values = config.split(":");
            configMap.put(values[0].trim() , values[1].trim());
        }

        return configMap;
    }


}
