package com.lychee.SensitiveDetection;

import org.apache.log4j.Logger;
import org.springframework.util.ClassUtils;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Utils {
    private static Logger logger = Logger.getLogger(Utils.class);


    /**
     * 判断字符是否是汉字
     * @param c 待判断字符
     * @return true: 汉字
     */
    public static boolean isChineseWord(char c)
    {
        return (""+c).matches("[\u4e00-\u9fcc]+");
    }

    /**
     * 将繁体字转化为简体字
     * @param c
     * @return
     */
    public static char toSimpleChineseWord(char c)
    {
        return c;
    }

    /**
     * 判断汉字是否是繁体字
     * @param c 待判断字符
     * @return  true: 繁体字
     */
    public static boolean isTraditionalChineseWord(char c)
    {
        return true;
    }

    /**
     * 将文本转化为字符串列表
     * 如：
     * 你好->["你","好"]
     * @param text
     * @return
     */
    public static List<String> sentence2List(String text)
    {
        List<String> result = new ArrayList<>();
        for(int i=0;i<text.length();i++)
            result.add(""+text.charAt(i));
        return result;
    }
    /**
     * 构建繁体字->简体字对应map
     * @return
     */
    public Map createT2SMapper()
    {
        Map<Character,Character> T2SMapper = new HashMap<>();
        String path = this.getClass().getClassLoader().getResource("t2s").getPath();

        List<File> files = getAllFile(path);

        for (File file : files) {
            try (FileInputStream fileInputStream = new FileInputStream(file)) {
                InputStreamReader reader = new InputStreamReader(fileInputStream);
                final BufferedReader bufferedReader = new BufferedReader(reader);

                String words = "";
                while ((words = bufferedReader.readLine()) != null) {
                    words = words.strip();
                    final String[] split = words.split("\t");
                    if (split[0].length()==1)
                        if(!T2SMapper.containsKey(split[0].charAt(0)))
                            T2SMapper.put(split[0].charAt(0),split[1].charAt(0));
                }
                bufferedReader.close();

                logger.info("loaded dictionary file from: "+file.getAbsolutePath());
            } catch (Exception e) {
                logger.info("T2S dictionary file loaded failed: "+file.getAbsolutePath());
            }
        }
        return T2SMapper;
    }

    private List<File> getAllFile(String path){
        List<File> fileList = new ArrayList<>();

        if(path=="")
            return fileList;

        final File file = new File(path);

        final File[] files = file.listFiles();
        if (files==null) {
            logger.warn("繁转简字典加载失败：资源路径("+path+")下未找到资源...");
            return fileList;
        }
        for (File file1 : files) {
            if(!file1.isDirectory()) {
                if(file1.getName().contains(".dat")) {
                    fileList.add(file1.getAbsoluteFile());
                }
            }
        }
        return fileList;
    }
}
