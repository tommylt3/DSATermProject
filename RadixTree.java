/*
  Authors (group members): Austin Phillips, Evan Thompson, Fiona Cahalan, and Tommy Gingerelli
  Email addresses of group members: fcahalan2022@my.fit.edu, Thompsone2021@my.fit.edu
									Aphillps2022@my.fit.edu, tgingerelli2021@my.fit.edu
  Group name: FATE

  Course: CSE2010
  Section: 14

  Description of the file: Radix tree/compressed trie for words/queries. The radix tree also stores old guesses.
*/

import java.util.ArrayDeque; 
import java.util.ArrayList;
import java.util.Comparator;

public class RadixTree {
    
    /* Node within the radix tree holding the frequency, a word or a portion of a word, and the node's children*/
    public static class Node {
        String word;        // portion of word or letter stored in node
        int freq;           // word frequency, freq > 0 indicates a word end
        Node[] children;    // children of node
        String[][] oldGuesses;      // stores top 5 gusses for this node that wouldn't have been guessed by parent nodes

        Node (String w, int f, Node[] c) {
            word = w;
            freq = f;
            children = c;
        }

        Node (String w, int f) {
            word = w;
            freq = f;
        }
    }

    static int ARRAY_SIZE = 68;      // size of children[] in Node
    Node root = new Node ("", 0, new Node[ARRAY_SIZE]);  // root of radix tree, holds empty string
    RadixTree () {} // constructor for radix tree

    // given a character, calculates corresponding index in children[] in a Node
    public static int calcIndex (char c) {
        int index = c;
        if (index > 91)
            return index - 58;
        else            
            return index - 32;
    }
    
    public void deleteLowFreq() {
        deleteLowFreq(root);
    }

    public boolean deleteLowFreq(Node current) {
        if (current != null) {
            int numChild = 0;
            if (current.children != null) {
                for (int i = 0; i < current.children.length; i++) {
                    if (current.children[i] != null) {
                        if (deleteLowFreq(current.children[i]))
                            current.children[i] = null;
                        else
                            numChild++;
                    }
                }
            }
            if (numChild == 0 & current.freq < 2) {
                return true;
            }
        }
        return false;
    }

    // add word without calling the root 
    public void addWord(String word, int freq) {
       this.addWord(root, word, freq);
    }

    /* recursive function that adds a given word or subword to the radix tree*/
    public void addWord (Node parent, String subword, int freq) {
        int index = calcIndex(subword.charAt(0));       // index in children[] corresponding to first character of given word
        if (parent.children == null)
            parent.children = new Node[ARRAY_SIZE];
        Node current = parent.children[index];          // node currently stored at the index where the subword belongs

        if (current == null) {    // if there is no word in the spot, place the subword there
            parent.children[index] = new Node(subword, freq);
        } else {
            String prefix = current.word;     // word that is currently in the spot

            // if the subword is smaller than the prefix and is actually the prefix of 'prefix'. e.g. prefix = freedom and subword = free
            // updated to only check if its equal and not check length 
            if (subword.equals(prefix)) {
                current.freq++;
                
                // if prefix is longer than the subword, then the actual prefix may be the subword
            } else if (prefix.length() > subword.length() && subword.equals(prefix.substring(0, subword.length()))) {      
                Node new_parent = new Node (subword, freq, new Node[ARRAY_SIZE]);
                current.word = prefix.substring(subword.length(), prefix.length());
                new_parent.children[calcIndex(current.word.charAt(0))] = current;
                parent.children[index] = new_parent;

                // if the prefix is actually the prefix for subword, e.g. prefix = free and subword = freedom, call addWord on the suffix portion of the subword (in the example this would be "dom")
            } else if (prefix.length() < subword.length() && prefix.equals(subword.substring(0, prefix.length()))) {    
                addWord (current, subword.substring(prefix.length(), subword.length()), freq);
            
            // if there is a child but they aren't equal because the compressed word doesn't match. e.g. freedom and fire, 
            } else {
                // find first character that doesn't match
                int i = 0;
                while (i < prefix.length() && subword.charAt(i) == prefix.charAt(i)){
                    i++;
                }

                // split the parent
                /*
                Example: prefix = freedom and subword = fire, i = 1
                prefix will become "f" and the only child of prefix (current) will be "reedom", the "reedom" node (new_child) will
                take all of prefixes' children at its own, then we will call addWord with the substring "ire"
                */
                Node new_child = new Node (prefix.substring(i, prefix.length()), current.freq, current.children);   // create new node, taking on current's children
                current.freq = 0;   // since new_child is taking the end of current's word, current cannot be a word ending
                current.children = new Node[ARRAY_SIZE];    // create empty list of children
                current.children[calcIndex(new_child.word.charAt(0))] = new_child;  // list new_child as current's child
                current.word = prefix.substring(0, i);      // remove part of current's word that is in new_child
                addWord (current, subword.substring(i, subword.length()), freq);  // call function on substring, exlcuding portion of string that current holds
            }
        }
    }

    /* determines if the given word is in the Radix tree */
    public boolean isWordInTree (String word) {
        Node current = root;
        int i = 0;
        while (i < word.length() && current.children[calcIndex(word.charAt(i))] != null) {
            current = current.children[calcIndex(word.charAt(i))];
            
            if (current.word.length() <= word.length() - i && current.word.equals(word.substring(i, current.word.length() + i))) {
                i += current.word.length();
            } else {
                return false;
            }
            
        }
        if (i >= word.length()) 
            return true;
        return false;
    }

    /* given a parent node and a character, returns node stored in spot corresponding to the character */
    public Node findNode (char c, Node parent, int i) {
        int index = calcIndex(c);
        if (parent == null || (parent.word.length() > i && parent.word.charAt(i) == c)) {
            return parent;
        } else {
            if (parent.children == null)
                return null;
            return parent.children[index];
        }
    }

    /* given a parent node, returns all words from the parent node, appending the prefix on the front */
    public ArrayList<Pair> prefixMatch (Node parent, String prefix) {
        ArrayList<Pair> answers = new ArrayList<Pair>();
        if (parent != null) {
            if (parent.freq > 0) {
                answers.add(new Pair(parent.freq, prefix));
            }
            preorder (parent, prefix, answers);
        }

        answers.sort(new comparatorPair());
        return answers;
    }

    /* visits nodes in preorder, constructing words and adding them to answers ArrayList */
    public void preorder (Node parent, String prefix, ArrayList<Pair> answers) {
        if (parent.children != null) {
            for (int i = parent.children.length - 1; i >= 0; i--) {
                if (parent.children[i] != null) {           // if there is a word
                    if (parent.children[i].freq > 0) {      // if Node contains the end of a word, add word to answers
                        answers.add(new Pair (parent.children[i].freq, prefix + parent.children[i].word));
                    } 
                    preorder (parent.children[i], prefix + parent.children[i].word, answers);
                }
            }
        }
    }

    /* sort Pair by frequency */
    public static class comparatorPair implements Comparator<Pair> {
        public int compare (Pair x, Pair y) {
            return -1 * Integer.compare(x.freq, y.freq);
        }
    }

    /* holds a word and its frequency together as a pair */
    public static class Pair {
        int freq;
        String word;

        Pair (int f, String w) {freq = f; word = w;}
    }

    /* For testing, prints radix tree using breadth-first search */
    public void printTree () {
        ArrayDeque<Node> queue = new ArrayDeque<Node>();
        queue.addLast(root);
        while (queue.size() > 0) {
            Node k = queue.removeFirst();
            System.out.printf("\"%s\" contains:", k.word);
            if (k.children != null) {
                for (int i = 0; i < k.children.length; i++) {
                    if (k.children[i] != null) {
                        System.out.printf(" \"%s\"", k.children[i].word);
                        queue.addLast(k.children[i]);
                    }
                }
            }
            System.out.printf("%n");
        }
    }
}