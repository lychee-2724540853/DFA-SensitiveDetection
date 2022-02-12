package com.lychee.SensitiveDetection;

import org.apache.log4j.Logger;

import java.util.*;

public class SensitiveFilter {
    private Map sensitiveMap;
    private Logger logger = Logger.getLogger(this.getClass());
    /**
     * 构造词典的方式
     * false: 用敏感词库的原始汉字构造
     * true: 用汉字对应的拼音构造
     */
    private boolean strictMode = false;

    public SensitiveFilter(boolean strictMode) {
        this.strictMode = strictMode;
        sensitiveMap = new DictionaryInit().initSensitiveKeyWord(strictMode);
    }

    /**
     * 判断字符串是否包含敏感词
     * @param sentence 待检测的字符串
     * @return ture:包含敏感词
     */
    public boolean isSensitive(String sentence)
    {
        sentence = cleanSentence(sentence);

        if (contains(sentence,sentence))
            return true;
        /**
         * 严格模式
         * 利用拼音检索敏感词
         */
        if (strictMode) {
            List<String> help;
            help = PinyinUtils.toPinyin(sentence);
            for (String list : help) {
                if(contains(list,sentence))
                    return true;
            }
            return false;
        }

        return false;
    }

    /**
     * 判断列表形式的文本字符串是否包含敏感词
     * @param text 待检测的文本序列   严格模式(strictMode：true)下，为文本对应的拼音序列
     * @param sentence 待检测的文本序列     辅助严格模式下日志输出未被转换为拼音序列的文本
     * @return
     */
    public boolean contains(String text, String sentence)
    {
        char temp;
        Map useMap = sensitiveMap;
        Map pre = useMap;
        boolean flag = false;
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
                logger.info("敏感评论："+sentence);
                return true;
            }
            else
                pre = map;
        }
        return false;
    }

    /**
     * 检测给定字符串中所有的
     * @param text 待检测字符串
     * @return 敏感词集合
     */
    public Set<String> findAllSensitiveWords(String text)
    {
        text = cleanSentence(text);
        Set<String> sensitiveWords = new HashSet<>();

        String word;
        for(int i=0; i<text.length();i++)
        {
            Set<String> words = findSensitiveWordsfromIndex(text, i);
            if (words!=null)
            {
                sensitiveWords.addAll(words);
            }
        }
        return sensitiveWords;
    }

    /**
     * 以‘*’替换文本中的敏感词
     * @param text 待替换文本
     * @return 替换后的文本
     */
    public String replaceAllSensitiveWords(String text)
    {
        Set<String> sensitiveWords = findAllSensitiveWords(text);

        for (String sensitiveWord : sensitiveWords) {
            text = text.replaceAll(sensitiveWord,"*");
        }
        return text;
    }

    /**
     * 检测所有以位置index处字符开始的敏感词
     * @param text 待检测字符串
     * @param index 给定字符串所在位置
     * @return 敏感词集合，null表示集合为空
     */
    public Set<String> findSensitiveWordsfromIndex(String text, int index)
    {
        if (index<0){
            logger.warn("非法参数：给定index为负");
            return null;
        }
        char temp = ' ';
        Map useMap = sensitiveMap;

        temp = text.charAt(index);
        HashMap map = (HashMap) useMap.get(temp);

        if(map!=null)
        {
            Set<String> sensitiveWords = new HashSet<>();
            String sensitiveWord = ""+temp;

            for (int i=index+1;i<text.length();i++)
            {
                temp = text.charAt(i);
                useMap = map;
                map = (HashMap) useMap.get(temp);

                if(map==null)
                    break;
                else if("1".equals(map.get("isEnd")))
                {
                    sensitiveWord += temp;
                    sensitiveWords.add(sensitiveWord);
                }
                else
                    sensitiveWord += temp;
            }
            return sensitiveWords;
        }
        return null;
    }

    /**
     * 清洗数据：
     *         1. 去除标点符号
     * @param originalSentence
     * @return
     */
    private String cleanSentence(String originalSentence)
    {
        String sentence = "";
        sentence = dropPunctuations(originalSentence);

        return sentence;
    }

    /**
     * 去除文本中的标点符号
     * @param originalSentence
     * @return
     */
    private String dropPunctuations(String originalSentence)
    {
        String result = originalSentence.replaceAll("\\p{Punct}", "");
        return result;
    }
}