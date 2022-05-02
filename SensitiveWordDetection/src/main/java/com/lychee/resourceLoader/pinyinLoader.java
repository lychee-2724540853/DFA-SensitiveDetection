package com.lychee.resourceLoader;

import com.lychee.Encoders.DFAEncoder;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.logging.Logger;

public class pinyinLoader implements Loader{
    static Logger logger = Logger.getLogger("pinyinLoader");

    private Map<Character, List<String>> pinyinMap; // 汉字 --> 拼音
    private Set<String> dictWords;
    private DFAEncoder dfaEncoder;
    private HashMap sensitiveMap;

    private pinyinUtils pinyinHelper = pinyinUtils.getPinyinUtils();

    private pinyinLoader() {
    }

    /**
     * 使用静态内部类，实现单例模式
     * 线程安全
     * 必须将构造函数设置为 private，禁止使用 new 实例化对象
     */
    private static class InstanceLoador
    {
        private static pinyinLoader instance = new pinyinLoader();
    }

    public static pinyinLoader getPinyinLoader()
    {
        pinyinLoader loader = InstanceLoador.instance;
        /**
         * 线程安全
         * synchronized关键字实现同步，内部拥有锁和条件变量
         * 保证多线程下加载一次拼音字典库
         * 可以定义pinyinMap时初始化，则synchronized可以拥有loader.pinyinMap的锁，
         * 即pinyinMap不为null时使用synchronized
         */
        if (loader.pinyinMap == null) {
            synchronized (loader) {
                if (loader.pinyinMap==null) {
                    loader.pinyinMap = new HashMap<>();
                    loader.addPinyinFile();
                    loader.addUnicodePinyinFile();
                    if (loader.dictWords==null)
                        loader.dictWords = loader.pinyinHelper.toPinyin(wordsLoader.getWordsLoader().getDictWords());

                    loader.dfaEncoder = new DFAEncoder(loader.dictWords);
                    loader.sensitiveMap = loader.dfaEncoder.getSensitiveMap();
                }
            }
        }
        return loader;
    }

    public Map<Character, List<String>> getPinyinMap() {
        return pinyinMap;
    }

    public Set<String> getDictWords() {
//        synchronized (InstanceLoador.instance)
//        {
//            if (dictWords==null)
//                dictWords = pinyinHelper.toPinyin(wordsLoader.getWordsLoader().getDictWords());
//        }
        return dictWords;
    }

    private void addPinyinFile()
    {
        String filePath = this.getClass().getClassLoader().getResource("txt/pinyins.txt").getPath();
        try {
            final File file = new File(filePath);

            final BufferedReader reader = new BufferedReader(new FileReader(file));

            /**
             * 保存汉字对应，一对多
             */
            List<String> pinyins;

            String line = reader.readLine();
            while (line != null) {
                final String[] split = line.split(",");

                pinyins = new ArrayList<>();
                for (int i=1;i<split.length;i++)
                {
                    pinyins.add(pinyinHelper.removeToneOfPinyin(split[i]));
                }
                char now = split[0].charAt(0);

                pinyinMap.put(now, pinyins);

                line = reader.readLine();
            }
            reader.close();
            logger.info("loaded dictionary file from: "+filePath);
        }catch (IOException e)
        {
            logger.info("Pinyin dictionary file loaded failed: "+filePath);
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
                if (!pinyinMap.containsKey(now)){
                    List<String> temp = new ArrayList<>();
                    /**
                     * 去除拼音音调
                     */
                    temp.add(pinyinHelper.removeToneOfPinyin(split[1]));
                    pinyinMap.put(now, temp);
                }
                line = reader.readLine();
            }
            reader.close();
            logger.info("loaded dictionary file from: "+filePath);
        }catch (IOException e)
        {
            logger.info("Unicode Pinyin dictionary file loaded failed: "+filePath);
        }
    }

    public HashMap getSensitiveMap() {
        return sensitiveMap;
    }
}
