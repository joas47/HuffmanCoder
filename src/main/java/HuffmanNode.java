// joas47

public class HuffmanNode {

    private final int frequency;
    private final String symbol;
    private HuffmanNode left;
    private HuffmanNode right;

    public HuffmanNode(int frequency, String symbol) {
        this.frequency = frequency;
        this.symbol = symbol;
    }

    public HuffmanNode(int frequency, HuffmanNode left, HuffmanNode right, String symbol) {
        this.frequency = frequency;
        this.left = left;
        this.right = right;
        this.symbol = symbol;
    }

    /**
     * Looks for the symbol in the tree and records the path
     * adds 0 for left and 1 for right to the path
     * returns the path as a string
     *
     * @param symbol the symbol to look for
     * @return the path to the symbol as a string of 0s and 1s
     */
    public String encodeSymbol(String symbol) {
        // if the current node is a leaf, check if the symbol matches
        if (isLeaf()) {
            if (this.symbol.equals(symbol)) {
                return "";
            } else {
                return null;
            }
        }
        // if the current node is not a leaf, check if the symbol is in the left or right subtree recursively
        // and add 0 or 1 to the path depending on which subtree the symbol is in
        // 0 for left and 1 for right
        String path = left.encodeSymbol(symbol);
        if (path != null) {
            return "0" + path;
        }
        path = right.encodeSymbol(symbol);
        if (path != null) {
            return "1" + path;
        }
        return null;
    }

    // if left and right are null (no children), then the node is a leaf
    public boolean isLeaf() {
        return left == null && right == null;
    }

    public int depth() {
        if (isLeaf()) {
            return 0;
        }
        int leftHeight = 0;
        int rightHeight = 0;
        if (left != null) {
            leftHeight = left.depth();
        }
        if (right != null) {
            rightHeight = right.depth();
        }
        return Math.max(leftHeight, rightHeight) + 1;
    }

    public int size() {
        int size = 1;
        if (left != null) {
            size += left.size();
        }
        if (right != null) {
            size += right.size();
        }
        return size;
    }

    public HuffmanNode getLeft() {
        return left;
    }

    public HuffmanNode getRight() {
        return right;
    }

    public int getFrequency() {
        return frequency;
    }

    public String getSymbol() {
        return symbol;
    }
}
