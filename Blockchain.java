import java.util.ArrayList;
import java.util.List;

public class Blockchain {
    private List<Block> chain;
    private int difficulty;

    public Blockchain(int difficulty) {
        this.chain = new ArrayList<>();
        this.difficulty = difficulty;
        // Createthegenesisblock
        createGenesisBlock();
    }

    private void createGenesisBlock() {
        Block genesisBlock = new Block(0, "0", "GenesisBlock");
        genesisBlock.mineBlock(difficulty);
        chain.add(genesisBlock);
    }

    public Block getLatestBlock() {
        return chain.get(chain.size() - 1);
    }

    public void addBlock(Block newBlock) {
        newBlock.mineBlock(difficulty);
        chain.add(newBlock);
    }

    public boolean isChainValid() {
        for (int i = 1; i < chain.size(); i++) {
            Block currentBlock = chain.get(i);
            Block previousBlock = chain.get(i - 1);
            if (!currentBlock.getHash().equals(currentBlock.calculateHash())) {
                System.out.println("Invalid hash for Block" + currentBlock.getIndex());
                return false;
            }
            if (!previousBlock.getHash().equals(currentBlock.getPreviousHash())) {
                System.out.println("Invalid previous hash for Block" + currentBlock.getIndex());
                return false;
            }
        }
        return true;
    }

    public static void main(String[] args) {
        Blockchain blockchain = new Blockchain(4);
        Block block1 = new Block(1, blockchain.getLatestBlock().getHash(), "Data1");
        blockchain.addBlock(block1);
        Block block2 = new Block(2, blockchain.getLatestBlock().getHash(), "Data2");
        blockchain.addBlock(block2);
        Block block3 = new Block(3, blockchain.getLatestBlock().getHash(), "Data3");
        blockchain.addBlock(block3);
        System.out.println("Block chain is valid:" + blockchain.isChainValid());
    }
}
