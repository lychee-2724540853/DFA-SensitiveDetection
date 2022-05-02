package com.lychee.resourceLoader;

import com.lychee.Encoders.DFAEncoder;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class wordsLoader implements Loader{
    static Logger logger = Logger.getLogger("ResourceLoador");

    private Set<String> dictWords = null;
    private com.lychee.Encoders.DFAEncoder DFAEncoder;
    private HashMap sensitiveMap;

    private wordsLoader() {
    }

    /**
     * 使用类的内部类，实现单例模式
     * 线程安全
     * 必须将构造函数设置为 private，禁止使用 new 实例化对象
     */
    private static class InstanceLoador
    {
        private static wordsLoader instance = new wordsLoader();
    }

    public static wordsLoader getWordsLoader() {
        wordsLoader loader = InstanceLoador.instance;
        /**
         * 线程安全
         * synchronized关键字同步，内部持有锁和状态变量
         * 保证多线程只加载一次字典库
         * 不能将loader.dictWords作为synchronized锁的对象，
         * 因为loaderdictWords初始为null，加锁为抛出异常
         */
        if (loader.dictWords==null) {
            synchronized (loader) {
                if (loader.dictWords==null) { //必要操作，防止多线程等待、进入锁，执行多次加载
                    loader.dictWords = loader.loadWordsDict();
                    loader.DFAEncoder = new DFAEncoder(loader.dictWords);
                    loader.sensitiveMap = loader.DFAEncoder.getSensitiveMap();
                }
            }
        }
        return loader;
    }

    public Set<String> getDictWords() {
        return dictWords;
    }

    /**
     * 加载敏感词词库
     * @return
     */
    private Set<String> loadWordsDict()
    {
        Set<String> dict = new HashSet<>();

        String path = InstanceLoador.instance.getClass().getClassLoader().getResource("sensitiveDict/").getPath();

        List<File> allFiles = getAllFile(path);

        if (allFiles.size()<=0) {
            return dict;
        }

        for (File file : allFiles) {
            try (FileInputStream fileInputStream = new FileInputStream(file)) {
                InputStreamReader reader = new InputStreamReader(fileInputStream);
                final BufferedReader bufferedReader = new BufferedReader(reader);

                String word = "";
                while ((word = bufferedReader.readLine()) != null) {
                    if(!dict.contains(word)&&word.length()>1)
                        dict.add(word);
                }
                bufferedReader.close();
                logger.info("loaded dictionary file from: "+file.getAbsolutePath());
            } catch (Exception e) {
                logger.info("Sensitive word dictionary file loaded failed");
            }
        }

        return dict;
    }

    /**
     * 过去字典库目录下在所有敏感词文件
     * @param path
     * @return
     */
    private List<File> getAllFile(String path){
        List<File> fileList = new ArrayList<>();

        final File file = new File(path);

        final File[] files = file.listFiles();
        if (files==null) {
            logger.log(Level.SEVERE, "敏感词词库加载失败：资源路径("+path+")下未找到敏感词词库...");
            return fileList;
        }
        for (File file1 : files) {
            if(!file1.isDirectory()) {
                if(file1.getName().contains(".txt")) {
                    fileList.add(file1.getAbsoluteFile());
                }
            }
        }
        return fileList;
    }

    public HashMap getSensitiveMap() {
        return sensitiveMap;
    }
}
