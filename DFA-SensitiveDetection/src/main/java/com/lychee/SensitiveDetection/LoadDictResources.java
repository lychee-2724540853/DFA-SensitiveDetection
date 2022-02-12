package com.lychee.SensitiveDetection;

import org.apache.log4j.Logger;
import org.springframework.util.ClassUtils;

import java.io.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class LoadDictResources {

    static Logger logger = Logger.getLogger(LoadDictResources.class);

    /**
     * 导入敏感词词库
     * @return 敏感词词库中的所有敏感词列表
     */
    public static Set<String> importdict()
    {
        Set<String> dict = new HashSet<>();

        final List<File> allFiles = getAllFile();

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
                logger.error("Sensitive word dictionary file loaded failed");
            }
        }

        return dict;
    }

    /**
     * 获取项目resources资源路径下敏感词词库中的所有文件
     * @return 敏感词词库文件列表
     */
    private static List<File> getAllFile(){
        List<File> fileList = new ArrayList<>();

        /**
         * 获取当前调用该方法的类的resources资源路径
         * 方便打包成jar后能够访问项目resources路径
         **/
        ClassLoader cl = ClassUtils.getDefaultClassLoader();
        String path = cl==null ? "" : cl.getResource("").getPath();

        if(path!="")
            path += "sensitiveDict/";
        else
            return fileList;

        final File file = new File(path);

        final File[] files = file.listFiles();
        if (files==null) {
            logger.warn("敏感词词库加载失败：资源路径("+path+")下未找到敏感词词库...");
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


}
