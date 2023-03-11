// joas47

/*
  Det du ska lämna in är:
  -koden
  -en kort beskrivning av hur implementationen fungerar och vilka designval du gjort.
  -Tänk också på kravet på att minst en (icketrivial) metod ska vara ordentligt dokumenterad.

  Beskrivning av kodningen:
    -Implementationen fungerar genom att först räkna ut frekvensen för varje tecken i strängen
    och lagrar informationen i en HashMap<String, Integer>.

    -Sedan går den igenom HashMappen och lägger till varje nyckel i en prioritetskö
    som är sorterad efter lägst frekvens som en enskild nod med frekvensen som värde
    (ju lägre frekvens desto högre prioritet).

    -Sedan bygger den trädet från prioritetskön genom att ta ut de två noderna med lägst frekvens
    och länka dem med en ny gemensam nod som får summan av de två nodernas frekvens som värde.
    Detta upprepas tills det bara finns en nod kvar i prioritetskön. Denna nod sätts till roten i trädet.
    Då är trädet färdigbyggt.

    -Sedan går den igenom trädet, nyckel för nyckel i den första tabellen, och noterar "vägen" ner i trädet,
    0 för vänster och 1 för höger, för varje symbol, och sparar den i en ny kodtabell
    implementerad som HashMap<String, String>, där man kan slå upp en symbol och få tillbaka
    "vägen" till symbolen som en sträng som representerar huffmankoden för den.

    -Sedan kodas den ursprungliga strängen genom att för varje tecken slå upp koden i kodtabellen
    och konkatenera alla koder till en sträng som bildar en serie av huffmankoder.

    -Då är den kodade strängen färdig och kan skickas iväg.

  Beskrivning av avkodningen:
    -För att sedan avkoda strängen läses den kodade strängen av tecken för tecken och går ner i trädet,
    0 för vänster och 1 för höger, tills den når en lövnod.
    Lövnoden innehåller symbolen som sedan konkateneras till den avkodade strängen.
    Detta upprepas tills den har gått igenom hela den kodade strängen och på så vis
    byggt upp den avkodade strängen tecken för tecken.

    -Den avkodade strängen ska då naturligtvis vara samma som den ursprungliga strängen.

  Designval:
    -Jag har valt att använda mig av en prioritetskö för att bygga trädet,
     då uppbyggnaden av trädet sker genom att ta ut två noder med lägst frekvens kändes en prioritetskö,
     med prioriteten baserad på nodernas värde, naturlig.
     Och att bara kunna skriva .poll() vid avläsning är lättförståeligt och enkelt.

    -Jag har valt att använda mig av en HashMap vid både notering av frekvenser och kodning av symboler
     då nyckel-värde-par gör det enkelt att både slå upp värden och lägga till nya värden.
     Lösningen kan förbättras genom att, istället för att skapa en ny HashMap,
     uppdatera den befintliga tabellen som skapades för att notera frekvenserna
     med koderna för varje symbol så sparar man minne och tid.

    -Jag valde att representera den kodade strängen som en sträng av 0:or och 1:or
     istället för en bitsträng då det är simplare.

 */

import java.util.Comparator;
import java.util.HashMap;
import java.util.PriorityQueue;

public class HuffmanTree {

    private int noOfMerges;
    private HuffmanNode root;

    private final PriorityQueue<HuffmanNode> queue = new PriorityQueue<>(Comparator.comparing(HuffmanNode::getFrequency));

    private final HashMap<String, Integer> frequencyTable = new HashMap<>();
    private final HashMap<String, String> codeTable = new HashMap<>();

    /**
     * Takes a string and encodes it using Huffman encoding
     * First counts the frequency of each character in the string
     * Then adds each character to the priority queue as a node
     * Then builds the tree from the priority queue
     * Then traverses the tree and creates a code table
     * Then encodes the string using the code table
     * The result is returned as an EncodedMessage object and can be decoded using the decode method
     *
     * @param stringToEncode the string to encode
     * @return the encoded message as an EncodedMessage object
     */
    public EncodedMessage<HuffmanTree, String> encode(String stringToEncode) {
        countFrequency(stringToEncode);
        addFromFrequencyTable();
        buildTree();
        createCodeTable();
        return new EncodedMessage<>(this, encodeTheString(stringToEncode));
    }

    // Step 1: count the frequency of each character in the string
    // and add the result to a hashmap
    /**
     * Counts the frequency of each character in the string
     * and adds the result to a hashmap as a key-value pair
     *
     * @param testString the string to count the frequency of
     */
    private void countFrequency(String testString) {
        // add all the characters to a hashmap as keys and count the frequency as values
        for (int i = 0; i < testString.length(); i++) {
            String key = String.valueOf(testString.charAt(i));
            if (frequencyTable.containsKey(key)) {
                frequencyTable.put(key, frequencyTable.get(key) + 1);
            } else {
                frequencyTable.put(key, 1);
            }
        }
    }

    // Step 2: go through the frequency table and add each key to the priority queue as a node
    /**
     * Adds the keys from the frequency table to the priority queue as nodes with their values as frequencies
     * The nodes are prioritized by their frequency, the lowest frequency is first in the queue
     * the lower the frequency, the higher the priority
     */
    private void addFromFrequencyTable() {
        for (String symbol : frequencyTable.keySet()) {
            queue.add(new HuffmanNode(frequencyTable.get(symbol), symbol));
        }
    }

    // Step 3: build the tree from the priority queue
    /**
     * Builds the tree from the priority queue
     * The tree is built by merging the two nodes with the lowest frequency
     * into a new small (sub)tree with a new root with the sum of the frequencies
     * and adding the new merged node to the queue.
     * The process is repeated until only one node is left in the queue, which is the root.
     * the node is then removed from the queue and set as the root.
     * The tree is then built.
     */
    private void buildTree() {
        HuffmanNode left;
        HuffmanNode right;
        // while there are more than one node in the queue
        while (queue.size() > 1) {
            // get the two nodes with the lowest frequency
            left = queue.poll();
            right = queue.poll();
            // and merge them into a new small (sub)tree with a new root with the sum of the frequencies
            root = merge(left, right);
            // add the new merged node/tree to the queue.
            queue.add(root);
        }
        // Last node left in the queue is the root, the tree is built.
        if (queue.size() == 1) {
            // set the root to the last node in the queue.
            root = queue.poll();
        }
    }

    // creates a small tree with a new root with the sum of the frequencies
    // with the two nodes as children
    private HuffmanNode merge(HuffmanNode left, HuffmanNode right) {
        return new HuffmanNode(left.getFrequency() + right.getFrequency(),
                left, right, "T" + ++noOfMerges);
    }

    // Step 4: create a code table from the tree
    private void createCodeTable() {
        // for each key in the frequency table, create a code for it
        // and add it to the code table
        for (String key : frequencyTable.keySet()) {
            codeTable.put(key, root.encodeSymbol(key));
        }
    }

    // step 5: encode the string using the code table
    /**
     * Encodes a string using the code table by looking up the code for each character in the string
     * and adding it to a stringbuilder.
     * Returns the encoded string.
     *
     * @param string the string to encode
     * @return the encoded string
     */
    private String encodeTheString(String string) {
        // for each character in the string
        // get the code from the code table
        // and add it to a stringbuilder
        StringBuilder encodedString = new StringBuilder();
        for (int i = 0; i < string.length(); i++) {
            encodedString.append(codeTable.get(String.valueOf(string.charAt(i))));
        }
        return encodedString.toString();
    }

    // step 6: decode the encoded string using the tree.
    // The result should be the same as the original string
    /**
     * Decodes an encoded string using the tree.
     * Reads the string character by character and traverses the tree in the direction of the character.
     * Left is 0, right is 1.
     * If the current node is a leaf, the symbol is added to the decoded string.
     * And the current node is reset to the root.
     * And the process starts over at the next character.
     *
     * @param message the EncodedMessage to decode
     * @return the decoded string
     */
    public String decode(EncodedMessage<HuffmanTree, String> message) {
        // decodes the string by traversing the tree
        // and adds the symbols to a stringbuilder
        StringBuilder decodedString = new StringBuilder();
        HuffmanNode currentNode = root;
        // for each character in the string
        // gets the path to the symbol from the code table
        // and traverses the tree to find the symbol.
        for (int i = 0; i < message.message.length(); i++) {
            String path = message.message.substring(i, i + 1);
            // for each character in the path.
            for (int j = 0; j < path.length(); j++) {
                // if the character is 0, go left.
                if (path.charAt(j) == '0') {
                    currentNode = currentNode.getLeft();
                // if the character is 1, go right.
                } else if (path.charAt(j) == '1') {
                    currentNode = currentNode.getRight();
                }
                // if the current node is a leaf, add the symbol to the decoded string
                // and reset the current node to the root.
                if (currentNode.isLeaf()) {
                    decodedString.append(currentNode.getSymbol());
                    currentNode = root;
                }
            }
        }
        return decodedString.toString();
    }

    // TODO: remove these test methods
    /**
     * test methods
     **/
    public void clear() {
        root = null;
        queue.clear();
        frequencyTable.clear();
        codeTable.clear();
        noOfMerges = 0;
    }

    // throw exception instead?
    //  test method
    public int size() {
        // if root is null, then the tree is empty
        if (root == null) {
            return 0;
        } else {
            // otherwise, call the size method on the root
            return root.size();
        }
    }

    // throw exception instead?
    //  test method
    public int depth() {
        // if root is null, then the tree is empty
        if (root == null) {
            return -1;
        } else {
            // otherwise, call the depth method on the root
            return root.depth();
        }
    }
}