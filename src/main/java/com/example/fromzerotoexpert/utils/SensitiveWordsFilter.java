package com.example.fromzerotoexpert.utils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * 敏感词过滤实现
 *
 * @author RabbitFaFa
 * @date 2022/12/25
 */
@Component
public class SensitiveWordsFilter {

    @Value("${sensitiveWordFilter.sensitiveWords.filePath}")
    private static String sensitiveWordsFilePath = "D:\\Program Files (x86)\\Java_code_IDEA\\from-zero-to-expert\\src\\main\\resources\\sensitiveWords\\sensitiveWords.txt";

    //前缀树 root 结点
    private static final TreeNode root = new TreeNode();

    public SensitiveWordsFilter() throws IOException {
        initializeTrieTree();
    }

    //前缀树结点定义
    static class TreeNode {
        //是否为结束结点
        private boolean isEnd = false;
        //子结点集合
        Map<String, TreeNode> childNode = new HashMap<>();

        public boolean isEnd() {
            return isEnd;
        }

        public void setEnd(boolean end) {
            isEnd = end;
        }

        public void addChildNode(String key, TreeNode treeNode) {
            childNode.put(key, treeNode);
        }

        public TreeNode getChildByKey(String key) {
            return childNode.get(key);
        }
    }

    /***
     * 读取敏感词文件 初始化敏感词前缀树
     */
    private static void initializeTrieTree() throws IOException {

        File file = new File(sensitiveWordsFilePath);
        System.out.println(file.length());
        FileInputStream fis = new FileInputStream(file);

        byte[] buff = new byte[1024];
        int readLen;
        StringBuilder word = new StringBuilder();

        while ((readLen = fis.read(buff)) != -1) {

            String words = new String(buff, 0, readLen);
            for (int i = 0; i < words.length(); i++) {
                if (words.charAt(i) == '\r' || words.charAt(i) == '\n') {
                    //将敏感词加入前缀树
                    addSensitiveWords(word.toString());
                    //word重置
                    word = new StringBuilder();
                    //跳过 '\n'
                    if (words.charAt(i) == '\r') i++;
                } else {
                    word.append(words.charAt(i));
                }
            }
        }
    }

    /***
     * 向前缀树中添加敏感词
     * @param word 待添加的敏感词
     */
    private static void addSensitiveWords(String word) {

        TreeNode temp = root;

        for (int i = 0; i < word.length(); i++) {

            String c = ((Character) word.charAt(i)).toString();
            TreeNode childNode = temp.getChildByKey(c);
            if (childNode == null) {
                childNode = new TreeNode();
                temp.addChildNode(c, childNode);//将新字符加入集合
            }
            //移动temp
            temp = childNode;
            //到达一个敏感词结尾
            if (i == word.length() - 1) {
                childNode.setEnd(true);
            }
        }
    }

    /***
     * 判断内容是否包含敏感词
     * @param word 待判断内容
     * @return 内容是否包含敏感词
     */
    public static boolean isSensitiveWord(String word) {

        TreeNode tempNode = root;//辅助指针
        int begin = 0, position = 0;

        while (position < word.length()) {//遍历word
            TreeNode node = tempNode.getChildByKey(word.charAt(position) + "");
            if (node == null) {//未找到以word(position)开头的敏感词
                begin++;
                position = begin;
                tempNode = root;
            } else if (node.isEnd()) {//敏感词结尾
                return true;
            } else {
                tempNode = node;//移动tempNode指针
                position++;
            }
        }
        return false;
    }

    /***
     * 过滤敏感词
     * @param word 待过滤内容
     * @return 过滤后的内容
     */
    public String filterSensitiveWord(String word) {

        TreeNode tempNode = root;//辅助指针
        int begin = 0, position = 0;
        StringBuilder newWord = new StringBuilder(word.length());

        while (position < word.length()) {//遍历word
            TreeNode node = tempNode.getChildByKey(word.charAt(position) + "");
            if (node == null) {//未找到以word(position)开头的敏感词
                newWord.append(word.charAt(begin));
                position = ++begin;
                tempNode = root;
            } else if (node.isEnd()) {//敏感词结尾
                //敏感词过滤
                while (begin <= position) {
                    newWord.append("*");
                    begin++;
                }
                position = begin;
                tempNode = root;
            } else {
                tempNode = node;//移动tempNode指针
                position++;
            }
        }
        newWord.append(word.substring(begin));//将剩余部分拼接

        return newWord.toString();
    }
}
