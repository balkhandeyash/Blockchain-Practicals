import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

class Block {
    private int index;
    private long timestamp;
    private String previousHash;
    private String hash;
    private int nonce;
    private List<Transaction> transactions;

    public Block(int index, long timestamp, String previousHash, List<Transaction> transactions) {
        this.index = index;
        this.timestamp = timestamp;
        this.previousHash = previousHash;
        this.transactions = transactions;
        this.nonce = 0;
        this.hash = calculateHash();
    }

    public Block(int i, String hash2, String string) {
    }

    public String calculateHash() {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            String data = index + timestamp + previousHash + nonce + transactions.toString();
            byte[] hashBytes = digest.digest(data.getBytes(StandardCharsets.UTF_8));
            StringBuilder hexString = new StringBuilder();
            for (Byte hashByte : hashBytes) {
                String hex = Integer.toHexString(0xff & hashByte);
                if (hex.length() == 1)
                    hexString.append('0');
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void mineBlock(int difficulty) {
        String target = new String(new char[difficulty]).replace('\0', '0');
        while (!hash.substring(0, difficulty).equals(target)) {
            nonce++;
            hash = calculateHash();
        }
        System.out.println("Block mined : " + hash);
    }

    public int getIndex() {
        return index;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public String getPreviousHash() {
        return previousHash;
    }

    public String getHash() {
        return hash;
    }

    public int getNonce() {
        return nonce;
    }

    public List<Transaction> getTransactions() {
        return transactions;
    }
}

class Transaction {
    private String from;
    private String to;
    private double amount;

    public Transaction(String from, String to, double amount) {
        this.from = from;
        this.to = to;
        this.amount = amount;
    }

    @Override
    public String toString() {
        return from + "->" + to + ":" + amount;
    }
}

class Blockchain {
    private List<Block> chain;
    private int difficulty;

    public Blockchain(int difficulty) {
        this.chain = new ArrayList<>();
        this.difficulty = difficulty;
        createGenesisBlock();
    }

    private void createGenesisBlock() {
        List<Transaction> transactions = new ArrayList<>();
        transactions.add(new Transaction("Genesis", "Alice", 100));
        Block genesisBlock = new Block(0, System.currentTimeMillis(), "0", transactions);
        genesisBlock.mineBlock(difficulty);
        chain.add(genesisBlock);
    }

    public void addBlock(Block block) {
        block.mineBlock(difficulty);
        chain.add(block);
    }

    public boolean isChainValid() {
        for (int i = 1; i < chain.size(); i++) {
            Block currentBlock = chain.get(i);
            Block previousBlock = chain.get(i - 1);
            if (!currentBlock.getHash().equals(currentBlock.calculateHash()))
                return false;
            if (!currentBlock.getPreviousHash().equals(previousBlock.getHash()))
                return false;
        }
        return true;
    }

    public Block getLastBlock() {
        return chain.get(chain.size() - 1);
    }
}

class Node {
    private Blockchain blockchain;
    private List<Transaction> pendingTransactions;

    public Node(Blockchain blockchain) {
        this.blockchain = blockchain;
        this.pendingTransactions = new ArrayList<>();
    }

    public void minePendingTransactions() {
        Block newBlock = new Block(blockchain.getLastBlock().getIndex() + 1, System.currentTimeMillis(),
                blockchain.getLastBlock().getHash(), pendingTransactions);
        blockchain.addBlock(newBlock);
        pendingTransactions.clear();
    }

    public void createTransaction(Transaction transaction) {
        pendingTransactions.add(transaction);
    }

    public Blockchain getBlockchain() {
        return blockchain;
    }

    public List<Transaction> getPendingTransactions() {
        return pendingTransactions;
    }
}

public class PeerToPeerBlockchain {
    public static void main(String[] args) {
        // Create a blockchain with difficulty 4
        Blockchain blockchain = new Blockchain(4);
        // Create two nodes
        Node node1 = new Node(blockchain);
        Node node2 = new Node(blockchain);
        // Node1 creates a transaction
        Transaction transaction1 = new Transaction("Alice", "Bob", 10.0);
        node1.createTransaction(transaction1);
        // Node2 creates a transaction
        Transaction transaction2 = new Transaction("Bob", "Charlie", 5.0);
        node2.createTransaction(transaction2);
        // Node1 mines the pending transactions
        node1.minePendingTransactions();
        // Node2 mines the pending transactions
        node2.minePendingTransactions();
        // Validate the blockchain
        System.out.println("Is blockchain valid ? " + blockchain.isChainValid());
    }
}
