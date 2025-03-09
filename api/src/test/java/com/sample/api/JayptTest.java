package com.sample.api;

import org.jasypt.encryption.pbe.PooledPBEStringEncryptor;
import org.jasypt.encryption.pbe.config.SimpleStringPBEConfig;
import org.junit.jupiter.api.Test;

public class JayptTest {

    private static final String ENCRYPTION_KEY = "encryptPwd";

    @Test
    void test() {
        PooledPBEStringEncryptor encryptor = new PooledPBEStringEncryptor();
        SimpleStringPBEConfig config = new SimpleStringPBEConfig();
        config.setPassword(ENCRYPTION_KEY);
        config.setAlgorithm("PBEWITHHMACSHA512ANDAES_256");
        config.setKeyObtentionIterations("1000");
        config.setPoolSize("1");
        config.setProviderName("SunJCE");
        config.setSaltGeneratorClassName("org.jasypt.salt.RandomSaltGenerator");
        config.setStringOutputType("base64");
        config.setIvGeneratorClassName("org.jasypt.iv.RandomIvGenerator");
        encryptor.setConfig(config);

        String str = "jdbc:oracle:thin:@[URL]:[PORT]:[serviceName]";
        String encryption = encryptor.encrypt(str);
        String decryption = encryptor.decrypt(encryption);

        System.out.println("URL 암호화: " + encryption);
        System.out.println("URL 복호화: " + decryption);

    }
}