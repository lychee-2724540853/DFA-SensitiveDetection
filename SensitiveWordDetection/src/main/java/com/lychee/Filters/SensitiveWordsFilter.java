package com.lychee.Filters;

import com.lychee.Detecotor.Detector;
import com.lychee.Detecotor.WordsDetector;
import com.lychee.Detecotor.impl.simpleWordsDetector;
import com.lychee.Detecotor.impl.strictWordsDetector;
import com.lychee.Filters.FilterException.FilterModeException;

public class SensitiveWordsFilter implements WordsDetector {
    public WordsDetector detector;
    private int DetectorMode = 0;

    /**
     * 默认检测器模式为 simple
     */
    public SensitiveWordsFilter() {
        detector = simpleWordsDetector.getWordsDector();
    }

    /**
     * 设置检测器模式
     * @param mode: 0 for simple mode
     *              1 for strict mode
     */
    public SensitiveWordsFilter(int mode) throws Exception {

        if (mode==0)
            detector = simpleWordsDetector.getWordsDector();
        else if (mode==1)
            detector = strictWordsDetector.getWordsDector();
        else
            throw new FilterModeException("Sensitive Filter mode must be integer in [0,1]");

    }

    @Override
    public boolean isSensitive(String text) {
        return detector.isSensitive(text);
    }

    @Override
    public boolean contains(String text) {
        return detector.contains(text);
    }
}
