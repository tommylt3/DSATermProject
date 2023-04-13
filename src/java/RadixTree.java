/*

  Authors (group members): Austin Phillips, Evan Thompson, Fiona Cahalan, and Tommy Gingerelli
  Email addresses of group members:
  Group name: FATE

  Course: CSE2010
  Section: 14

  Description of the file:
  
*/

public class RadixTree {
    public class Node {
        String word;        // portion of word or letter stored in node
        boolean isEnd;      // is node the end of a word?
        int freq;           // word frequency
        Node[] children;    // children of node

        Node (String w, boolean b, int f, Node[] c) {
            word = w;
            isEnd = b;
            freq = f;
            children = c;
        }

        Node (String w, boolean b, int f) {this(w, b, f, null);}
    }

    Node root = new Node ("", false, 0, new Node[36]);  // 26 lower case letters + 10 numerical digits


    /* unfinished function to add a given string to the radix tree */
    public void addWord (Node parent, String subword, int freq) {
        // Should add check that string is long enough
        
        String child = root.children[calcIndex(subword.charAt(0))];
        String prefix = child.word;

        // if there is no child with the letter, add it
        if (prefix == null) {
            prefix = new Node(subword, true, freq);
        } else if (!prefix.equals(subword.subString(0, prefix.length()))) {     // if the prefix is the prefix for subword, call to add the suffix portion as a child
            addWord (child, subword.subString(1, prefix.length()), freq);
        } else { // if there is a child but they aren't equal because the compressed word doesn't match. e.g. fire and free
            // find first character that doesn't match
            int i = 0;
            while (subword.charAt(i) == prefix.subword.charAt(i)){
                i++;
            }

            // split the root 
            root_substring = prefix.subString(i, prefix.length());
            Node new_child = new Node (root_substring, root.isEnd, root.freq, root.children);
            root.isEnd = false;
            root.children = new Node[36];
            root.children[calcIndex(root_substring.charAt(0))];
            addWord (root, subword, freq);  // call function again, now that the root is split, it should work
        }
        // need to deal with edge case if prefix is freedom and subword is free
    }

    /*
    Needed function: Return all words with the given prefix in an array, sorted by frequency. Since the frequency does
    not need to be returned, only a String[] needs to be returned.
    Possible way to do this: traverse the tree to the last node that matches, then us something like
    depth first search to get all the words. 

    public String[] prefixMatch (String prefix) {

    }
    */

    // calculates index that character corresponds to
    public int calcIndex (char c) {
        int index = c.charValue();
        if (index > 96) // lowercase letter
            return index - 87;
        else            // number
            return index - 48;
    }
}