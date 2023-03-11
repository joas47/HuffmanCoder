// joas47
public class HuffmanCoder {

    private final HuffmanTree huffmanTree;

    public HuffmanCoder() {
        huffmanTree = new HuffmanTree();
    }

    public EncodedMessage<HuffmanTree, String> encode(String msg) {
        return huffmanTree.encode(msg);
    }

    public String decode(EncodedMessage<HuffmanTree, String> msg) {
        return huffmanTree.decode(msg);
    }
}
