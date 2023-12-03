import java.util.ArrayList;
import java.util.List;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

class MerkleTree {
    private List<String> transactions;
    private String root;

    public MerkleTree(List<String> transactions) {
        this.transactions = transactions;
        this.root = buildTree();
    }

    private String buildTree() {
        List<String> level = new ArrayList<>(transactions);
        while (level.size() > 1) {
            List<String> nextLevel = new ArrayList<>();
            for (int i = 0; i < level.size(); i += 2) {
                String left = level.get(i);
                String right = (i + 1 < level.size()) ? level.get(i + 1) : "";
                String combined = left + right;
                String hash = calculateHash(combined);
                nextLevel.add(hash);
            }
            level = nextLevel;
        }
        return level.get(0);
    }

    private String calculateHash(String input) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hashBytes = digest.digest(input.getBytes());
            StringBuilder hexString = new StringBuilder();
            for (byte hashByte : hashBytes) {
                String hex = Integer.toHexString(0xff & hashByte);
                if (hex.length() == 1)
                    hexString.append('0');
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }

    public String getRoot() {
        return root;
    }
}

public class Blockchain1 {
    private List<MerkleTree> blocks;

    public Blockchain1() {
        this.blocks = new ArrayList<>();
    }

    public void addBlock(List<String> transactions) {
        MerkleTree merkleTree = new MerkleTree(transactions);
        blocks.add(merkleTree);
    }

    public String getBlockRoot(int blockIndex) {
        if (blockIndex >= 0 && blockIndex < blocks.size()) {
            MerkleTree merkleTree = blocks.get(blockIndex);
            return merkleTree.getRoot();
        }
        return null;
    }

    public static void main(String[] args) {
        Blockchain1 blockchain = new Blockchain1();
        List<String> transactions1 = new ArrayList<>();
        transactions1.add("Transaction1");
        transactions1.add("Transaction2");
        transactions1.add("Transaction 3");
        blockchain.addBlock(transactions1);
        List<String> transactions2 = new ArrayList<>();
        transactions2.add("Transaction4");
        transactions2.add("Transaction 5");
        blockchain.addBlock(transactions2);
        String root1 = blockchain.getBlockRoot(0);
        System.out.println("Block 1 Root: " + root1);
        String root2 = blockchain.getBlockRoot(1);
        System.out.println("Block 2 Root: " + root2);
    }
}
