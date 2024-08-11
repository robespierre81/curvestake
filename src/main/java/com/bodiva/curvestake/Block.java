import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Date;

public class Block {
    public String hash;
    public String previousHash;
    private String data;
    private long timeStamp;
    private byte[] signature;
    private PublicKey publicKey;

    public Block(String data, String previousHash, PrivateKey privateKey) {
        this.data = data;
        this.previousHash = previousHash;
        this.timeStamp = new Date().getTime();
        this.hash = calculateHash();
        this.signature = ECCUtil.signData(data.getBytes(), privateKey); // Sign the block
    }
    
    public String calculateHash() {
        String calculatedHash = StringUtil.applySha256(
                previousHash +
                Long.toString(timeStamp) +
                data
        );
        return calculatedHash;
    }

    public boolean verifyBlock(PublicKey publicKey) {
        return ECCUtil.verifySignature(data.getBytes(), signature, publicKey);
    }
    
    // Method to get the signature
    public byte[] getSignature() {
        return signature;
    }

    public PublicKey getPublicKey() {
        return publicKey;
    }
}
