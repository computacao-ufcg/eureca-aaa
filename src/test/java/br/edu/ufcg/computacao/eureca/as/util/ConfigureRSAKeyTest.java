package br.edu.ufcg.computacao.eureca.as.util;

import br.edu.ufcg.computacao.eureca.as.core.util.CryptoUtil;
import br.edu.ufcg.computacao.eureca.as.core.util.HomeDir;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;

public abstract class ConfigureRSAKeyTest {

    protected static String privateKeyPath;
    protected static String publicKeyPath;


    protected static void init() throws GeneralSecurityException, IOException {
        String keysPath = HomeDir.getPath();
        publicKeyPath = keysPath + "public.key";
        privateKeyPath = keysPath + "private.key";

        KeyPair keyPair = CryptoUtil.generateKeyPair();
        PublicKey publicKey= keyPair.getPublic();
        PrivateKey privateKey = keyPair.getPrivate();

        saveKeyToFile(publicKeyPath, CryptoUtil.toBase64(publicKey));
        saveKeyToFile(privateKeyPath, CryptoUtil.toBase64(privateKey));
    }

    private static void saveKeyToFile(String path, String key) throws IOException {
        File file = new File(path);
        file.getParentFile().mkdirs();
        FileOutputStream stream = new FileOutputStream(file);
        stream.write(key.getBytes());
        stream.close();
    }

    private static void deleteKey(String path){
        File file = new File(path);
        file.delete();
    }

    protected static void tearDown() {
        deleteKey(publicKeyPath);
        deleteKey(privateKeyPath);
    }
}
