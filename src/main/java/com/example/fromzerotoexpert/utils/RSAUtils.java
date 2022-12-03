package com.example.fromzerotoexpert.utils;

import javax.crypto.Cipher;
import java.io.*;
import java.security.*;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.ArrayList;
import java.util.List;
import java.util.Base64;

/**
 * @author RabbitFaFa
 * @date 2022/12/2
 */
public class RSAUtils {
    //公钥私钥文件存放路径
    private final static String path = "D:\\Program Files (x86)\\Java_code_IDEA\\from-zero-to-expert\\src\\main\\resources\\rsa\\";
    //公钥文件全称
    private final static String pubKeyFileName = "rsa_pubKey.pub";
    //私钥文件全称
    private final static String priKeyFileName = "rsa_private.key";
    // 加密算法
    private final static String ALGORITHM_RSA = "RSA";
    //公钥私钥对象
    private static List<Key> keyObjList = null;
    //公钥私钥字符串
    private static List<String> keyStrList = null;

    public RSAUtils() {

        File keyFile = new File(path + pubKeyFileName);
        try {
            if (keyFile.exists()
                    && (keyStrList == null || keyObjList == null)) { //公钥私钥文件存在但List为null
                //从文件读取公钥私钥并初始化 keyStrList
                keyStrList = readKeysFile();
                //初始化 keyObjList
                RSAPublicKey pubObjKey = getPublicKey(keyStrList.get(0));
                RSAPrivateKey priObjKey = getPrivateKey(keyStrList.get(1));
                keyObjList = new ArrayList<>(2);
                keyObjList.add(pubObjKey);
                keyObjList.add(priObjKey);
            } else { //公钥私钥文件不存在 执行生成公钥私钥并存为文件
                //密钥长度
                int modules = 512;
                getRSAKeyObject(modules);
                getRSAKeyString(modules);
                generateKeysFile();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //暴露获取公钥私钥对象方法
    public static List<Key> getKeyObjList() {
        return keyObjList;
    }

    //暴露获取公钥私钥字符串方法
    public static List<String> getKeyStrList() {
        return keyStrList;
    }

    /**
     * 生成公钥私钥文件到磁盘
     *
     * @throws IOException IO异常
     */
    private static void generateKeysFile() throws IOException {

        //公钥私钥文件全路径
        File pubKeyFile = new File(path + pubKeyFileName);
        File priKeyFile = new File(path + priKeyFileName);
        //文件输出流 每次写入都覆盖原先的内容
        FileOutputStream pubOps = new FileOutputStream(pubKeyFile);
        FileOutputStream priOps = new FileOutputStream(priKeyFile);
        //写入文件
        pubOps.write(keyStrList.get(0).getBytes());
        priOps.write(keyStrList.get(1).getBytes());

    }

    /**
     * 读取公钥私钥文件中的公钥私钥
     * @return 公钥私钥 List
     * @throws IOException IO异常
     */
    private static List<String> readKeysFile() throws IOException {
        //公钥私钥文件全路径
        File pubKeyFile = new File(path + pubKeyFileName);
        File priKeyFile = new File(path + priKeyFileName);
        //从文件读取公钥私钥的输入流
        FileInputStream pubIps = new FileInputStream(pubKeyFile);
        FileInputStream priIps = new FileInputStream(priKeyFile);
        byte[] buff = new byte[512];
        int readLen;
        List<String> res = new ArrayList<>(2);
        StringBuilder key = new StringBuilder();
        //获取公钥字符串
        while ((readLen = pubIps.read(buff)) != -1) {
            key.append(new String(buff, 0, readLen));
        }
        res.add(key.toString());
        //获取私钥字符串
        key = new StringBuilder();
        while ((readLen = priIps.read(buff)) != -1) {
            key.append(new String(buff, 0, readLen));
        }
        res.add(key.toString());
        return res;
    }

    /**
     * 直接生成公钥私钥对象
     *
     * @param modulus 密钥长度
     * @throws NoSuchAlgorithmException 无此算法异常
     */
    private static void getRSAKeyObject(int modulus) throws NoSuchAlgorithmException {

        // 创建RSA密钥生成器
        KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance(ALGORITHM_RSA);
        // 设置密钥的大小，此处是RSA算法的模长 = 最大加密数据的大小
        keyPairGen.initialize(modulus);
        KeyPair keyPair = keyPairGen.generateKeyPair();
        //初始化KeyObjList
        keyObjList = new ArrayList<>(2);
        // keyPair.getPublic() 生成的是RSAPublic的实例
        keyObjList.add(keyPair.getPublic());
        // keyPair.getPrivate() 生成的是RSAPrivateKey的实例
        keyObjList.add(keyPair.getPrivate());
    }

    /**
     * 生成公钥、私钥的字符串
     * 方便传输
     *
     * @param modulus 模长
     * @throws NoSuchAlgorithmException 无此算法异常
     */
    private static void getRSAKeyString(int modulus) throws NoSuchAlgorithmException {

        KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance(ALGORITHM_RSA);
        keyPairGen.initialize(modulus);
        KeyPair keyPair = keyPairGen.generateKeyPair();
        String publicKey = Base64.getEncoder().encodeToString(keyPair.getPublic().getEncoded());
        String privateKey = Base64.getEncoder().encodeToString(keyPair.getPrivate().getEncoded());
        keyStrList = new ArrayList<>(2);
        keyStrList.add(publicKey);
        keyStrList.add(privateKey);
    }

    /**
     * 传入String类型的公钥返回公钥对象
     * Java中RSAPublicKeySpec、X509EncodedKeySpec支持生成RSA公钥
     * 此处使用X509EncodedKeySpec生成
     *
     * @param publicKey String类型的公钥
     * @return 公钥对象
     * @throws Exception 抛出异常
     */
    public static RSAPublicKey getPublicKey(String publicKey) throws Exception {

        KeyFactory keyFactory = KeyFactory.getInstance(ALGORITHM_RSA);
        byte[] keyBytes = Base64.getDecoder().decode(publicKey);
        X509EncodedKeySpec spec = new X509EncodedKeySpec(keyBytes);
        return (RSAPublicKey) keyFactory.generatePublic(spec);
    }

    /**
     * 传入String类型的私钥返回私钥对象
     * Java中只有RSAPrivateKeySpec、PKCS8EncodedKeySpec支持生成RSA私钥
     * 此处使用PKCS8EncodedKeySpec生成
     *
     * @param privateKey String类型的私钥
     * @return 私钥对象
     * @throws Exception 抛出异常
     */
    public static RSAPrivateKey getPrivateKey(String privateKey) throws Exception {

        KeyFactory keyFactory = KeyFactory.getInstance(ALGORITHM_RSA);
        byte[] keyBytes = Base64.getDecoder().decode(privateKey);
        PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(keyBytes);
        return (RSAPrivateKey) keyFactory.generatePrivate(spec);
    }

    /**
     * 公钥加密
     *
     * @param data      待加密数据
     * @param publicKey 公钥对象
     * @return 加密后的String类型的data
     * @throws Exception 抛出异常
     */
    public static String encryptByPublicKey(String data, RSAPublicKey publicKey)
            throws Exception {
        Cipher cipher = Cipher.getInstance(ALGORITHM_RSA);
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);
        // 模长n转换成字节数
        int modulusSize = publicKey.getModulus().bitLength() / 8;
        // PKCS Padding长度为11字节，所以实际要加密的数据不能要 - 11byte
        int maxSingleSize = modulusSize - 11;
        // 切分字节数组，每段不大于maxSingleSize
        byte[][] dataArray = splitArray(data.getBytes(), maxSingleSize);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        // 分组加密，并将加密后的内容写入输出字节流
        for (byte[] s : dataArray) {
            out.write(cipher.doFinal(s));
        }
        // 使用Base64将字节数组转换String类型
        return Base64.getEncoder().encodeToString(out.toByteArray());
    }

    /**
     * 私钥解密
     *
     * @param data 公钥加密了的数据
     * @param privateKey 私钥对象
     * @return 解密了的data
     * @throws Exception 抛出异常
     */
    public static String decryptByPrivateKey(String data, RSAPrivateKey privateKey)
            throws Exception {
        Cipher cipher = Cipher.getInstance(ALGORITHM_RSA);
        cipher.init(Cipher.DECRYPT_MODE, privateKey);
        // RSA加密算法的模长 n
        int modulusSize = privateKey.getModulus().bitLength() / 8;
        byte[] dataBytes = data.getBytes();
        // 之前加密的时候做了转码，此处需要使用Base64进行解码
        byte[] decodeData = Base64.getDecoder().decode(dataBytes);
        // 切分字节数组，每段不大于modulusSize
        byte[][] splitArrays = splitArray(decodeData, modulusSize);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        for (byte[] arr : splitArrays) {
            out.write(cipher.doFinal(arr));
        }
        return new String(out.toByteArray());
    }

    /**
     * 按指定长度切分数组
     *
     * @param data 加解密的数据
     * @param len  单个字节数组长度
     * @return 切分后的数组
     */
    private static byte[][] splitArray(byte[] data, int len) {

        int dataLen = data.length;
        if (dataLen <= len) {
            return new byte[][]{data};
        }
        byte[][] result = new byte[(dataLen - 1) / len + 1][];
        int resultLen = result.length;
        for (int i = 0; i < resultLen; i++) {
            if (i == resultLen - 1) {
                int sLen = dataLen - len * i;
                byte[] single = new byte[sLen];
                System.arraycopy(data, len * i, single, 0, sLen);
                result[i] = single;
                break;
            }
            byte[] single = new byte[len];
            System.arraycopy(data, len * i, single, 0, len);
            result[i] = single;
        }
        return result;
    }
}
