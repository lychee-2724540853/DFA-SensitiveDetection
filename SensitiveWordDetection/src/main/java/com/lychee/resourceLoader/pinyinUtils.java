package com.lychee.resourceLoader;

import java.util.*;

public class pinyinUtils {

    private Map<Character, Character> alphabetWithputTone;

    private pinyinUtils() {
    }

    /**
     * 使用静态内部类，实现单例模式
     * 线程安全
     */
    private static class pinyinUtilsLoader
    {
        private static pinyinUtils instance = new pinyinUtils();
    }

    public static pinyinUtils getPinyinUtils()
    {
        pinyinUtils instance = pinyinUtilsLoader.instance;

        if (instance.alphabetWithputTone==null) {
            synchronized (instance) {
                instance.alphabetWithputTone = instance.removeTone();
            }
        }

        return pinyinUtilsLoader.instance;
    }

    /**
     * 去除拼音中的音调
     * @param pinyin
     * @return
     */
    public String removeToneOfPinyin(String pinyin)
    {
        for(int i=0;i<pinyin.length();i++)
        {
            if (alphabetWithputTone.containsKey(pinyin.charAt(i))) {
                pinyin = pinyin.replace(pinyin.charAt(i), alphabetWithputTone.get(pinyin.charAt(i)));
                return pinyin;
            }
        }
        return pinyin;
    }

    /**
     * 构造带音调字符->去音调字符的映射表
     * @return
     */
    public Map removeTone()
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

    /**
     * 判断字符是否是汉字
     * @param c 待判断字符
     * @return true: 汉字
     */
    public boolean isChineseWord(char c)
    {
        return (""+c).matches("[\u4e00-\u9fcc]+");
    }

    public String[] getPinyinOf(char c)
    {
        if (isChineseWord(c)) {
            Map<Character, List<String>> map = pinyinLoader.getPinyinLoader().getPinyinMap();
            if (map.containsKey(c)) {
                List<String> pinyins = map.get(c);
                return transformer(pinyins);
            }
        }
        return new String[]{""+c};
    }
    /**
     * 将文本转换为拼音 如：你好->nihao
     * @param text 待转换文本
     * @return 拼音
     */
    public List<String> toPinyin(String text)
    {
        List<String> pinyinReturn = new ArrayList<>();

        PinyinIndex pinyinIndex = toPinyinListGraph(text);

        List<List<String>> results = getPinyinList(pinyinIndex);

        for (List<String> result : results) {
            String pinyin = "";
            for (String s1 : result) {
                pinyin += s1;
            }
            pinyinReturn.add(pinyin);
        }
        return pinyinReturn;
    }

    /**
     * 将文本转换为拼音 如：{[你好],[世界]}->{[n,i,H,a,o],[s,h,i, j,i,e]}
     * {[饮水和吃饭]}->{[y,i,n,s,h,u,i,h,e,c,h,i,f,a,n],[y,i,n,s,h,u,i,h,u,c,h,i,f,a,n],[y,i,n,s,h,u,i,h,u,o,c,h,i,f,a,n]}
     * @param texts 待转换词语集合，每个词语表示为列表
     * @return
     */
    public Set<String> toPinyin(Set<String> texts)
    {
        Set<String> result = new HashSet<>();

        for (String item:texts)
        {
            /**
             * 循环每一个敏感词
             */
            List<String> temp = toPinyin(item);
            result.addAll(temp);
        }
        return result;
    }

    /**
     * 将 拼音的List 转换为 string[]
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
     * 针对汉字的多音字问题
     * 将一句话的拼音构成一张有向无环图
     * @param text 待转换文本
     * @return 头节点(无意义)head 文本第一个字的拼音为head.next
     * 链表法
     */
    private PinyinIndex toPinyinListGraph(String text)
    {
        PinyinIndex root = new PinyinIndex(0);
        root.pinyinNodeList = new ArrayList<>();
        PinyinIndex help = root;
        char[] chinese = text.toCharArray();

        for (char c : chinese) {
            //判断字符是否为汉字
            if (isChineseWord(c))
            {
                try {
                    String[] pinyins = getPinyinOf(c);

                    help = addPinyin2Graph(help, pinyins);
                }catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
            else {
                help = addPinyin2Graph(help, new String[]{""+c});
            }
        }
        return root;
    }

    private static PinyinIndex addPinyin2Graph(PinyinIndex help, String[] pinyin)
    {
        PinyinIndex temp = new PinyinIndex(help.num+1);
        List<PinyinNode> pinyinNodeList = new ArrayList<>();
        Set<String> set = new HashSet<>();
        for (String item:pinyin)
        {
            if (set.contains(item))
                continue;
            set.add(item);
            pinyinNodeList.add(new PinyinNode(item));
        }
        for (PinyinNode pinyinNode : help.pinyinNodeList) {
            pinyinNode.next = pinyinNodeList;
        }
        temp.pinyinNodeList = pinyinNodeList;
        help.next = temp;
        return help.next;
    }

    /**
     * 考虑汉字的多音字问题，一句话的拼音可以构造成一张有向无环图
     * 将每个汉字的拼音构造为类
     */
    private static class PinyinNode
    {
        String value;
        List<PinyinNode> next;
        public PinyinNode(String value) {
            this.value = value;
        }
    }

    /**
     * 辅助构造有向无环图
     * num: 记录当前字符在文本中的位置，0表示头节点，第一个字符num=1
     * pinyinNodeList: 当前字符的所有拼音
     * next: 指向下一个字符
     */
    private static class PinyinIndex
    {
        int num;
        List<PinyinNode> pinyinNodeList;
        PinyinIndex next;
        public PinyinIndex(int num) {
            this.num = num;
        }
    }

    /**
     * 获取以root为根节点(无实际值)的拼音树的所有拼音组合
     * @param root 根节点
     * @return 所有的拼音组合列表
     */
    private static List<List<String>> getPinyinList(PinyinIndex root)
    {
        List<List<String>> result = new ArrayList<>();
        result.add(new ArrayList<>());
        root = root.next;
        while (root!=null)
        {
            result = addPinyin(result, root.pinyinNodeList);
            root = root.next;
        }
        return result;
    }

    /**
     * 将当前拼音树节点的拼音追加至已遍历的拼音列表中
     * @param pinyinLists
     * @param nodeList
     * @return
     */
    private static List<List<String>> addPinyin(List<List<String>> pinyinLists, List<PinyinNode> nodeList)
    {
        List<List<String>> result = new ArrayList<>();
        for (List<String> pinyinList : pinyinLists) {
            for (PinyinNode item : nodeList) {
                List<String> temp = new ArrayList<>();
                temp.addAll(pinyinList);
                temp.add(item.value);
                result.add(temp);
            }
        }
        return result;
    }
}
