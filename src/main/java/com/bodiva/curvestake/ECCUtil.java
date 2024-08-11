package com.bodiva.curvestake;

import java.security.*;
import java.security.spec.ECGenParameterSpec;

public class ECCUtil {

    public static KeyPair generateKeyPair() {
        try {
            KeyPairGenerator keyGen = KeyPairGenerator.getInstance("EC");
            ECGenParameterSpec ecSpec = new ECGenParameterSpec("secp256r1");
            keyGen.initialize(ecSpec, new SecureRandom());
            return keyGen.generateKeyPair();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static byte[] signData(byte[] data, PrivateKey privateKey) {
        try {
            Signature dsa = Signature.getInstance("SHA256withECDSA");
            dsa.initSign(privateKey);
            dsa.update(data);
            return dsa.sign();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static boolean verifySignature(byte[] data, byte[] signature, PublicKey publicKey) {
        try {
            Signature ecdsaVerify = Signature.getInstance("SHA256withECDSA");
            ecdsaVerify.initVerify(publicKey);
            ecdsaVerify.update(data);
            return ecdsaVerify.verify(signature);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
