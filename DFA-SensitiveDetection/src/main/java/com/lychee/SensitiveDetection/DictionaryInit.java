package com.lychee.SensitiveDetection;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class DictionaryInit {
    /**
     * DFA存储敏感词库
     */
    private HashMap sensitiveMap;

    /**
     * 初始化敏感词词典
     * @return 由DFA算法构造得到的敏感词词典sensitiveMap
     */
    public Map initSensitiveKeyWord(boolean strictMode)
    {
        Set<String> keywords = readSensitiveDict();

        if (strictMode)
        {
            keywords.addAll(PinyinUtils.toPinyin(keywords));
        }

        sensitiveMap = new HashMap(keywords.size());
        setSensitiveMap(keywords);
        return sensitiveMap;
    }
    /**
     * 读取敏感词词库
     * @return 敏感词集合
     * 例
     * 我去->{"我去"]}
     */
    private Set<String> readSensitiveDict()
    {
        return LoadDictResources.importdict();
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
