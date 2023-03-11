import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;

public class HuffmanTreeTest {

    private HuffmanTree tree = new HuffmanTree();
    private HashMap<String, Integer> frequencyTable = new HashMap<>();

    private HuffmanCoder coder = new HuffmanCoder();


    // before each test, clear the tree
    @BeforeEach
    void setUp() {
        tree.clear();
    }

    void buildHashMap() {
        // add the frequency of each symbol to the frequency table
        // "this is a test"
        frequencyTable.put("t", 3);
        frequencyTable.put("h", 1);
        frequencyTable.put("i", 2);
        frequencyTable.put("s", 3);
        frequencyTable.put(" ", 3);
        frequencyTable.put("a", 1);
        frequencyTable.put("e", 1);
    }

    @Test
    void testEmptyTree() {
        tree.clear();
        assertEquals(0, tree.size());
        assertEquals(-1, tree.depth());
        //assertEquals("[]", tree.toString());
    }

    @Test
    void goFromStringToHuffmanCodeToStringSeeIfSame() {
        // string to encode
        String testString = "this is a test";
        // the bit length of the original table

        tree.encode(testString);
        // build the encoded string
        //String encodedString = tree.decode(testString);
        // compare the original string to the decoded string
        //assertEquals(testString, encodedString);
        // compare the bit length of the original table to the encoded table

    }

    @Test
    void oneMethodEntryway() {
        String testString = "this is a test";
        EncodedMessage<HuffmanTree, String> encodedMessage = coder.encode(testString);
        String decoded = coder.decode(encodedMessage);

        assertEquals(testString, decoded);
        System.out.println("Original string: " + testString + '\n' +
                "Encoded string: " + encodedMessage.message + '\n' +
                "Decoded string: " + decoded);
    }

    @Test
    void cachedStringOf1sAnd0s() {
        String testString = "aaee öla";
        tree.encode(testString);
        String cachedString = "0011";
        //assertEquals(testString, tree.decode(testString));
    }
}
