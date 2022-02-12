package com.lychee.SensitiveDetection;

import org.apache.log4j.Logger;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PinyinHelper {

    /**
     * 构造汉字：拼音表PinyinMap  和：he,huo,hu,hai
     */
    private static Map<Character, List<String>> PinyinMap = new HashMap<>();

    private static Map<Character,Character> PinyinwithoutTone = removeTone();

    private static Logger logger = Logger.getLogger(PinyinHelper.class);
    private Utils utils = new Utils();

    public PinyinHelper() {
        createPinyinMap();
    }

    public String[] getPinyinOf(char c)
    {
        if (Utils.isChineseWord(c)) {
            if (PinyinMap.containsKey(c)) {
                List<String> pinyins = PinyinMap.get(c);
                return transformer(pinyins);
            }
        }
        return new String[]{""+c};
    }

    /**
     * 构造汉字：拼音表PnyinMap  和：he,huo,hu,hai
     */
    private void createPinyinMap() {
        addPinyinFile();
        addUnicodePinyinFile();
    }

    /**
     * pinyin.txt
     */
    private void addPinyinFile()
    {
        String filePath = this.getClass().getClassLoader().getResource("txt/pinyin.txt").getFile();
        try {
            final File file = new File(filePath);

            final BufferedReader reader = new BufferedReader(new FileReader(file));

            String line = reader.readLine();
            while (line != null) {
                final String[] split = line.split(",");
                for (int i=1;i<split.length;i++) {
                    char now = split[i].charAt(0);
                    if (PinyinMap.containsKey(now))
                        PinyinMap.get(now).add(split[0]);
                    else {
                        List<String> temp = new ArrayList<>();
                        temp.add(split[0]);
                        PinyinMap.put(now, temp);
                    }
                }
                line = reader.readLine();
            }
            reader.close();
            logger.info("loaded dictionary file from: "+filePath);
        }catch (IOException e)
        {
            logger.warn("Pinyin dictionary file loaded failed: "+filePath);
        }
    }

    /**
     * 添加Unicode2Pinyin.txt
     * 作为pinyin.txt文件的补充
     * 增补不常用汉字的拼音
     * 如  愛：ai
     * 文件存储格式为：汉字对应的Unicode 带音调拼音
     */
    private void addUnicodePinyinFile()
    {
        String filePath = this.getClass().getClassLoader().getResource("txt/Unicode2Pinyin.txt").getFile();
        try {
            final File file = new File(filePath);

            final BufferedReader reader = new BufferedReader(new FileReader(file));

            String line = reader.readLine();
            while (line != null) {
                final String[] split = line.split("\t");

                char now = (char) Integer.parseInt(split[0],16);
                if (!PinyinMap.containsKey(now)){
                    List<String> temp = new ArrayList<>();
                    /**
                     * 去除拼音音调
                     */
                    temp.add(removeTone(split[1]));
                    PinyinMap.put(now, temp);
                }
                line = reader.readLine();
            }
            reader.close();
            logger.info("loaded dictionary file from: "+filePath);
        }catch (IOException e)
        {
            logger.warn("Unicode Pinyin dictionary file loaded failed: "+filePath);
        }
    }

    /**
     * 按照属性更改拼音
     * @param pinyins
     * @return
     */
    private String[] transformer(List<String> pinyins)
    {
        String[] strings = new String[pinyins.size()];

        for (int i=0;i< pinyins.size();i++)
            strings[i] = pinyins.get(i);

        return strings;
    }
    /**
     * 将拼音首字母大写 如 ni -> Ni
     * @param pinyin 待转换拼音
     * @return 转换结果
     */
    private String upperFirst(String pinyin)
    {
        char[] temp = pinyin.toCharArray();
        temp[0] = (char) (temp[0] - 32);
        return String.valueOf(temp);
    }

    /**
     * 去除拼音中的音调
     * @param pinyin
     * @return
     */
    private String removeTone(String pinyin)
    {
        for(int i=0;i<pinyin.length();i++)
        {
            if (PinyinwithoutTone.containsKey(pinyin.charAt(i))) {
                pinyin = pinyin.replace(pinyin.charAt(i), PinyinwithoutTone.get(pinyin.charAt(i)));
                return pinyin;
            }
        }
        return pinyin;
    }

    /**
     * 构造带音调字符->去音调字符的映射表
     * @return
     */
    private static Map removeTone()
    {
        Map<Character,Character> PinyinwithoutTone = new HashMap<>();
        PinyinwithoutTone.put('ā','a');
        PinyinwithoutTone.put('á','a');
        PinyinwithoutTone.put('ǎ', 'a');
        PinyinwithoutTone.put('à', 'a');
        PinyinwithoutTone.put('ē', 'e');
        PinyinwithoutTone.put('é', 'e');
        PinyinwithoutTone.put('ě', 'e');
        PinyinwithoutTone.put('è', 'e');
        PinyinwithoutTone.put('ī', 'i');
        PinyinwithoutTone.put('í', 'i');
        PinyinwithoutTone.put('ǐ', 'i');
        PinyinwithoutTone.put('ì', 'i');
        PinyinwithoutTone.put('ō', 'o');
        PinyinwithoutTone.put('ó', 'o');
        PinyinwithoutTone.put('ǒ', 'o');
        PinyinwithoutTone.put('ò', 'o');
        PinyinwithoutTone.put('ū', 'u');
        PinyinwithoutTone.put('ú', 'u');
        PinyinwithoutTone.put('ǔ', 'u');
        PinyinwithoutTone.put('ù', 'u');
        PinyinwithoutTone.put('ǖ', 'v');
        PinyinwithoutTone.put('ǘ', 'v');
        PinyinwithoutTone.put('ǚ', 'v');
        PinyinwithoutTone.put('ǜ', 'v');

        return PinyinwithoutTone;
    }

}
