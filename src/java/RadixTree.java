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

        int index = calcIndex(subword.charAt(0));   // converts first character to index in array of children
        Node current = parent.children[index];        // node currently in index where the subword belongs

        if (current == null) {    // if there is no word in the spot, place the subword there
            parent.children[index] = new Node(subword, true, freq);
        } else {
            String prefix = current.word;     // word that is currently in the spot

            // if the subword is smaller than the prefix and is actually the prefix of 'prefix'. e.g. prefix = freedom and subword = free
            if (prefix.length() > subword.length() && subword.equals(prefix.substring(0, subword.length()))) {      
                Node new_parent = new Node (subword, true, freq, new Node[36]);
                current.word = prefix.substring(subword.length(), prefix.length());
                new_parent.children[calcIndex(current.word.charAt(0))] = current;
                parent.children[index] = new_parent;

                // if the prefix is actually the prefix for subword, e.g. prefix = free and subword = freedom, call addWord on the suffix portion of the subword (in the example this would be "dom")
            } else if (prefix.length() < subword.length() && prefix.equals(subword.substring(0, prefix.length()))) {    
                addWord (current, subword.substring(1, prefix.length()), freq);
            
            // if there is a child but they aren't equal because the compressed word doesn't match. e.g. freedom and fire, 
            } else {
                // find first character that doesn't match
                int i = 0;
                while (subword.charAt(i) == prefix.charAt(i) && i < prefix.length()){
                    i++;
                }

                // split the parent
                /*
                Example: prefix = freedom and subword = fire, i = 1
                prefix will become "f" and the only child of prefix (current) will be "reedom", the "reedom" node (new_child) will
                take all of prefixes' children at its own, then we will call addWord on the current with the substring "ire"
                */
                Node new_child = new Node (prefix.substring(i, prefix.length()), current.isEnd, current.freq, current.children);   // create new node, taking on current's children
                current.isEnd = false;    // since new_child is taking the end of current's word, current cannot be a word ending
                current.children = new Node[36];    // create empty list of children
                current.children[calcIndex(new_child.word.charAt(0))] = new_child;  // list new_child as current's child
                current.word = prefix.substring(0, i);      // remove part of current's word that is in new_child
                addWord (current, subword.substring(i, subword.length()), freq);  // call function on substring, exlcuding portion of string that current holds
            }
        }
        /*
        cases to consider:
        ---------------------
        prefix      subword
        freedom     fire
        fire        freedom
        free        freedom
        freedom     free
        */
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
        int index = c;
        if (index > 96) // lowercase letter
            return index - 87;
        else            // number
            return index - 48;
    }
}