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
        
        int index = calcIndex(subword.charAt(0));

        // if there is no child with the letter, add it
        if (root.children[index] == null) {
            root.children[index] = new Node(subword, true, freq);
        } else if (!subword.equals(root.children[index].subword)) { // if there is a child but they aren't equal because the compressed word doesn't match
            // need to add part to deal with edge case if root is a prefix of the subword, ex: free and freedom
            
            // find first character that doesn't match
            int i = 0;
            while (subword.charAt(i) == root.children[index].subword.charAt(i)){
                i++;
            }

            // split the root 
            root_substring = root.children[index].subString(i, root.children[index].length());
            Node new_child = new Node (root_substring, root.isEnd, root.freq, root.children);
            root.isEnd = false;
            root.children = new Node[36];
            root.children[calcIndex(root_substring.charAt(0))];
            addWord (root, subword, freq);  // call function again, now that the root is split, it should work
        } else {
            addWord (root.children[index], subword.subString(1, subword.length()), freq);   // if matches, call on corresponding child spot
        }
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