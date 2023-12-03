
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.util.ArrayList;
import java.util.List;

public class MerkleTree {
    private List<String> transactions;
    private List<String> merkleTree;

    public MerkleTree(List<String> transactions) {
        this.transactions = transactions;
        this.merkleTree = buildMerkleTree(transactions);
    }

    private String calculateHash(String data) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hashBytes = digest.digest(data.getBytes(StandardCharsets.UTF_8));
            StringBuilder hexString = new StringBuilder();
            for (byte hashByte : hashBytes) {
                String hex = Integer.toHexString(0xff & hashByte);
                if (hex.length() == 1) {
                    hexString.append('0');
                }
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }

    private List<String> buildMerkleTree(
            List<String> transactions) {
        List<String> merkleTree = new ArrayList<>(transactions);
        int levelOffset = 0;
        for (int levelSize = transactions.size(); levelSize > 1; levelSize = (levelSize + 1) / 2) {
            for (int left = 0; left < levelSize; left += 2) {
                int right = Math.min(left + 1, levelSize - 1);
                String leftHash = merkleTree.get(levelOffset + left);
                String rightHash = merkleTree.get(levelOffset + right);
                String parentHash = calculateHash(leftHash + rightHash);
                merkleTree.add(parentHash);
            }
            levelOffset += levelSize;
        }
        return merkleTree;
    }

    public List<String> getMerkleTree() {
        return merkleTree;
    }

    public static void main(String[] args) {
        List<String> transactions = new ArrayList<>();
        transactions.add("Transaction1");
        transactions.add("Transaction2");
        transactions.add("Transaction3");
        transactions.add("Transaction4");
        MerkleTree merkleTree = new MerkleTree(transactions);
        List<String> tree = merkleTree.getMerkleTree();
        for (String hash : tree) {
            System.out.println(hash);
        }
    }
}
