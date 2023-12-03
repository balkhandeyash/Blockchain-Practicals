import java.security.*;
import java.security.spec.ECGenParameterSpec;

public class CryptoWallet {
    private PrivateKey privateKey;
    private PublicKey publicKey;

    public CryptoWallet() {
        generateKeyPair();
    }

    public void generateKeyPair() {
        try {
            KeyPairGenerator keyGen = KeyPairGenerator.getInstance("EC");
            SecureRandom random = SecureRandom.getInstanceStrong();
            ECGenParameterSpec ecSpec = new ECGenParameterSpec("secp256r1");
            keyGen.initialize(ecSpec, random);
            KeyPair keyPair = keyGen.generateKeyPair();
            privateKey = keyPair.getPrivate();
            publicKey = keyPair.getPublic();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        CryptoWallet wallet = new CryptoWallet();
        System.out.println("PrivateKey : " + wallet.privateKey);
        System.out.println("PublicKey : " + wallet.publicKey);
    }
}
