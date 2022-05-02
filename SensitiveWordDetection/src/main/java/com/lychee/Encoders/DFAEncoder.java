package com.lychee.Encoders;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * 将 读取的字典 构建为 确定有穷自动机(DFA)
 */
public class DFAEncoder {
    private HashMap sensitiveMap;

    public DFAEncoder(Set<String> dictWords) {
        sensitiveMap = new HashMap();
        setSensitiveMap(dictWords);
    }

    public HashMap getSensitiveMap() {
        return sensitiveMap;
    }

    /**
     * 确定有穷自动机(Deterministic Finite Automation,DFA)算法构造敏感词典库
     * @param keywords 敏感词集合
     */
    private void setSensitiveMap(Set<String> keywords)
    {
        String key;
        Map<String, String> keyMap;
        Map tempMap;

        for (String keyword : keywords) {
            tempMap = sensitiveMap;
            for(int i=0;i<keyword.length();i++)
            {
                char c = keyword.charAt(i);
                Object map = tempMap.get(c);

                if(map==null)
                {
                    keyMap = new HashMap<>();
                    keyMap.put("isEnd", "0");
                    tempMap.put(c, keyMap);
                    tempMap = keyMap;
                }
                else {
                    tempMap = (Map) map;
                }
                if(i==keyword.length()-1)
                    tempMap.put("isEnd", "1");
            }
        }
    }

}
