
# 敏感词过滤

## SensitiveWordsFilter
SensitiveWordsFilter 提供敏感词检测的两种模式：1).简单模式; 2).严格模式
SensitiveWordsFilter 实现 WordsDetector 接口，实现判断文本是否敏感、文本是否包含指定词汇

## WordsDector 接口
```java
public interface WordsDetector extends Detector{
    /**
     * 判断文本是否为敏感文本
     */
    boolean isSensitive(String text);
    /**
     * 判断文本是否包含给定字符串
     */
    boolean contains(String text);
}
```
### baseWordsDetector simpleWordsDetector strictWordsDetector
baseWordsDetector 为敏感词检测类 simpleWordsDetector strictWordsDetector 提供敏感词检测的通用方法，如contains
simpleWordsDetector 简单模式敏感词检测类
strictWordsDetector 严格模式敏感词检测类

## DFAEncoder
实现 确定有穷机DFA 算法，对敏感词库编码

## pinyinLoader wordsLoader pinyinUtils
pinyinLoader wordsLoader 均用于加载资源
pinyinUtils 用于拼音的相关处理

# 设计模式
## 单例模式
pinyinLoader wordsLoader pinyinUtils 三个资源加载相关的类 通过 静态内部类 实现 单例模式，在多线程并发下只实例化对象一次

## 工厂方法模式
WordsDetector 通过工厂方法模式，实现普通模式、严格模式的敏感词检测方法。便于扩展不同模式的敏感词检测方法；
simpleWordsDetector strictWordsDetector 实现 WordsDetector 接口；
# 多线程并发
## pinyinLoader wordsLoader
1. 在资源加载过程中，通过关键字 synchronized 实现对资源相关的对象加锁，保证多线程下资源仅加载一次； 
2. 多线程同步，减少内存消耗，加快线程获取资源类速度