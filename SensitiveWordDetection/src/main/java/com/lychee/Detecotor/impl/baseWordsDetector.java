package com.lychee.Detecotor.impl;

import com.lychee.Detecotor.WordsDetector;

import java.util.HashMap;
import java.util.Map;

public class baseWordsDetector implements WordsDetector {
    private Map sensitiveMap;

    public void setSensitiveMap(Map sensitiveMap) {
        this.sensitiveMap = sensitiveMap;
    }

    @Override
    public boolean isSensitive(String text) {
        return false;
    }

    @Override
    public boolean contains(String text) {
        char temp;
        Map useMap = sensitiveMap;
        Map pre = useMap;

        for (int i=0;i<text.length();i++)
        {
            temp = text.charAt(i);

            HashMap map = (HashMap) pre.get(temp);
            if(map==null) {
                if(useMap.get(temp)==null) {
                    pre = useMap;
                    continue;
                }
                else
                    pre = (HashMap) useMap.get(temp);
            }
            else if ("1".equals(map.get("isEnd"))) {
                return true;
            }
            else
                pre = map;
        }
        return false;
    }
}
