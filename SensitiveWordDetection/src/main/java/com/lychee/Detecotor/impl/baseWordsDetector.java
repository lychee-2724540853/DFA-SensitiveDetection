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
        StringBuilder sensitiveWord = new StringBuilder();  //检测到的词
        
        for (int i=0;i<text.length();i++)
        {
            temp = text.charAt(i);

            HashMap map = (HashMap) pre.get(temp);
            if(map==null) {
                if(useMap.get(temp)==null) {
                    pre = useMap;
                    sensitiveWord.delete(0, sensitiveWord.length());
                    continue;  //如果输出敏感词，把这里注释掉
                }
                else {
                    pre = (HashMap) useMap.get(temp);
                    sensitiveWord.delete(0, sensitiveWord.length());
                    sensitiveWord.append(temp);
                }
            }
            else if ("1".equals(map.get("isEnd"))) {
                sensitiveWord.append(temp);
                return true;
            }
            else {
                pre = map;
                sensitiveWord.append(temp);
            }
        }
        return false;
    }
}
