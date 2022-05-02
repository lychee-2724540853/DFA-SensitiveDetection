package com.lychee.Detecotor.impl;

import com.lychee.Detecotor.WordsDetector;
import com.lychee.resourceLoader.pinyinLoader;
import com.lychee.resourceLoader.pinyinUtils;

import java.util.List;
import java.util.logging.Logger;

/**
 * 严格模式
 * 敏感词检测类
 */
public class strictWordsDetector implements WordsDetector {

    Logger logger = Logger.getLogger(this.getClass().getName());
    private baseWordsDetector detector;

    private strictWordsDetector() {
    }

    private static class wordsdetectorHolder
    {
        private static strictWordsDetector instance = new strictWordsDetector();
    }

    public static WordsDetector getWordsDector()
    {
        strictWordsDetector instance = strictWordsDetector.wordsdetectorHolder.instance;

        synchronized (instance) {
            if (instance.detector==null) {
                instance.detector = new baseWordsDetector();
                instance.detector.setSensitiveMap(pinyinLoader.getPinyinLoader().getSensitiveMap());
            }
        }
//        if (instance.detector==null)
//        {
//            synchronized (instance) {
//                instance.detector = new baseWordsDetector();
//                instance.detector.setSensitiveMap(pinyinLoader.getPinyinLoader().getSensitiveMap());
//            }
//        }
        return instance;
    }
    @Override
    public boolean isSensitive(String text) {
        List<String> help;
        help = pinyinUtils.getPinyinUtils().toPinyin(text);
        for (String list : help) {
            if(contains(list)) {
                logger.info("敏感评论："+ text);
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean contains(String text) {
        return detector.contains(text);
    }


}
