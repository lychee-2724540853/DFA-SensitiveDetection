package com.lychee.Detecotor;

public interface WordsDetector extends Detector{

    /**
     * 判断文本是否为敏感文本
     * @param text: 待检测文本
     * @return True敏感文本，False正常文本
     */
    boolean isSensitive(String text);

    /**
     * 判断文本是否包含给定字符串
     * @param text: 待检测文本
     * @param target: 目标字符串
     * @return True: text包含target
     */
    boolean contains(String text);

}
