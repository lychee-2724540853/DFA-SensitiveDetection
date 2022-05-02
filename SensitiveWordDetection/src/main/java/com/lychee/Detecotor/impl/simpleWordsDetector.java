package com.lychee.Detecotor.impl;

import com.lychee.Detecotor.WordsDetector;
import com.lychee.resourceLoader.wordsLoader;

import java.util.logging.Logger;

import static com.lychee.Detecotor.Utils.cleanSentence;

/**
 * 普通模式
 * 敏感词检测
 */
public class simpleWordsDetector implements WordsDetector {
    Logger logger = Logger.getLogger(this.getClass().getName());

    private baseWordsDetector detector;

    private simpleWordsDetector() {
    }

    /**
     * 静态内部类 实现单例模式
     * 线程安全
     */
    private static class wordsdetectorHolder{
        private static simpleWordsDetector instance = new simpleWordsDetector();
    }

    public static WordsDetector getWordsDector() {
        simpleWordsDetector instance = wordsdetectorHolder.instance;

        synchronized (instance) {
            if (instance.detector==null) {
                instance.detector = new baseWordsDetector();
                instance.detector.setSensitiveMap(wordsLoader.getWordsLoader().getSensitiveMap());
            }
        }
//        if (instance.detector == null) {
//            synchronized (instance) {
//                instance.detector = new baseWordsDetector();
//                instance.detector.setSensitiveMap(wordsLoader.getWordsLoader().getSensitiveMap());
//            }
//        }
        return instance;
    }

    @Override
    public boolean isSensitive(String text) {
        text = cleanSentence(text);

        if (contains(text))
        {
            logger.info("敏感评论："+ text);
            return true;
        }
        return false;
    }

    @Override
    public boolean contains(String text) {
        return detector.contains(text);
    }

}
