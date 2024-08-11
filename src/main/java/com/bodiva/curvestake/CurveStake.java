import java.util.ArrayList;

public class CurveStake {
    public static ArrayList<Block> blockchain = new ArrayList<>();
    private static ProofOfStake pos = new ProofOfStake();

    public static void main(String[] args) {
        // Create stakeholders
        Stakeholder st1 = new Stakeholder(10);
        Stakeholder st2 = new Stakeholder(20);
        Stakeholder st3 = new Stakeholder(30);

        pos.addStakeholder(st1);
        pos.addStakeholder(st2);
        pos.addStakeholder(st3);

        // Select the validator
        Stakeholder validator = pos.selectValidator();

        // Add blocks to the blockchain
        blockchain.add(new Block("Genesis block", "0", validator.getPrivateKey()));
        blockchain.add(new Block("Second block", blockchain.get(blockchain.size() - 1).hash, validator.getPrivateKey()));
        blockchain.add(new Block("Third block", blockchain.get(blockchain.size() - 1).hash, validator.getPrivateKey()));

        // Validate the blockchain
        System.out.println("Blockchain is valid: " + isChainValid());

        // Print out the blockchain
        for (Block block : blockchain) {
            System.out.println("Hash: " + block.hash);
            System.out.println("Previous Hash: " + block.previousHash);
            System.out.println();
        }
    }

    public static Boolean isChainValid() {
        Block currentBlock;
        Block previousBlock;

        for (int i = 1; i < blockchain.size(); i++) {
            currentBlock = blockchain.get(i);
            previousBlock = blockchain.get(i - 1);

            if (!currentBlock.hash.equals(currentBlock.calculateHash())) {
                System.out.println("Current Hashes not equal");
                return false;
            }

            if (!previousBlock.hash.equals(currentBlock.previousHash)) {
                System.out.println("Previous Hashes not equal");
                return false;
            }

            if (!currentBlock.verifyBlock(previousBlock.getPublicKey())) {
                System.out.println("Block signature is not valid");
                return false;
            }
        }
        return true;
    }
}
