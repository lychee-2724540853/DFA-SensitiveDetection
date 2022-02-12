package com.lychee.SensitiveDetection;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class test {
    public static void main(String[] args) {
        final SensitiveFilter filter1 = new SensitiveFilter(false);
        final SensitiveFilter filter2 = new SensitiveFilter(true);
        String text1 = "我曹";
        String text2 = "我是代购";
        String text3 = "卧槽";
        System.out.println("=====================普通监测模式====================");
        filter1.isSensitive(text1);
        filter1.isSensitive(text2);
        filter1.isSensitive(text3);
        System.out.println("=====================严格监测模式====================");
        filter2.isSensitive(text1);
        filter2.isSensitive(text2);
        filter1.isSensitive(text3);
    }
}
