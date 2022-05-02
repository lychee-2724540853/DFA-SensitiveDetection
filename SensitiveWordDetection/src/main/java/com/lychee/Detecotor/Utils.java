package com.lychee.Detecotor;

public class Utils {
    /**
     * 清洗数据：
     *         1. 去除标点符号
     * @param originalSentence
     * @return
     */
    public static String cleanSentence(String originalSentence)
    {
        String sentence = "";
        sentence = Utils.dropPunctuations(originalSentence);

        return sentence;
    }

    /**
     * 去除文本中的标点符号
     * @param originalSentence
     * @return
     */
    public static String dropPunctuations(String originalSentence)
    {
        String result = originalSentence.replaceAll("\\p{Punct}", "");
        return result;
    }
}
