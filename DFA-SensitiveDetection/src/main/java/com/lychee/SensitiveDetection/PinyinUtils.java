package com.lychee.SensitiveDetection;


import java.util.*;


public class PinyinUtils {

    private static PinyinHelper pinyinHelper = new PinyinHelper();

    /**
     * 将文本转换为拼音 如：你好->nihao
     * @param text 待转换文本
     * @return 拼音
     */
    public static List<String> toPinyin(String text)
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
    public static Set<String> toPinyin(Set<String> texts)
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

    public static List<List<String>> toPinyinList(String text)
    {
        PinyinIndex pinyinIndex = toPinyinListGraph(text);
        List<List<String>> results = getPinyinList(pinyinIndex);
        return results;
    }
    /**
     * 针对汉字的多音字问题
     * 将一句话的拼音构成一张有向无环图
     * @param text 待转换文本
     * @return 头节点(无意义)head 文本第一个字的拼音为head.next
     * 链表法
     */
    private static PinyinIndex toPinyinListGraph(String text)
    {
        Map<PinyinNode, List<PinyinNode>> PinyinMap = new HashMap<>();
        PinyinIndex root = new PinyinIndex(0);
        root.pinyinNodeList = new ArrayList<>();
        PinyinIndex help = root;
        char[] chinese = text.toCharArray();

        List<PinyinNode> lastPinyinList = null;
        for (char c : chinese) {
            //判断字符是否为汉字
            if (Utils.isChineseWord(c))
            {
                try {
                    String[] pinyins = pinyinHelper.getPinyinOf(c);

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
    /**
     * 针对汉字的多音字问题
     * 将一句话的拼音构成一张有向无环图
     * @param text 待转换文本
     * @return 头节点(无意义)head 文本第一个字的拼音为head.next
     * 图的邻接链表法
     */
    private static void toPinyinListByNeiborList(String text)
    {
        Map<PinyinNode, List<PinyinNode>> PinyinMap = new HashMap<>();

        char[] chinese = text.toCharArray();

        List<PinyinNode> lastPinyinList = null;
        for (char c : chinese) {
            //判断字符是否为汉字
            if (Utils.isChineseWord(c))
            {
                try {
                    String[] pinyins = pinyinHelper.getPinyinOf(c);
                    addPinyin2Graph(PinyinMap, lastPinyinList, pinyins);
                }catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
            else
            {

            }
        }
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
    private static void addPinyin2Graph(Map PinyinMap, List<PinyinNode> lastPinyinList, String[] pinyins)
    {
        List<PinyinNode> pinyinNodeList = new ArrayList<>();
        for (String pinyin : pinyins) {
            PinyinNode pinyinNode = new PinyinNode(pinyin);
            pinyinNodeList.add(pinyinNode);
        }
        for (PinyinNode pinyinNode : lastPinyinList) {
            PinyinMap.put(pinyinNode, pinyinNodeList);
        }
        lastPinyinList = pinyinNodeList;
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
