/*

  Authors (group members): Austin Phillips, Evan Thompson, Fiona Cahalan, and Tommy Gingerelli
  Email addresses of group members:
  Group name: FATE

  Course: CSE2010
  Section: 14

  Description of the file:
  
*/

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Comparator;


public class RadixTree {
    
    /**/
    public static class Node {
        String word;        // portion of word or letter stored in node
        int freq;           // word frequency, frew > 0 indicates a word end
        Node[] children;    // children of node

        Node (String w, int f, Node[] c) {
            word = w;
            freq = f;
            children = c;
        }
    }

    Node root = new Node ("", 0, new Node[36]);  // 26 lower case letters + 10 numerical digits
    RadixTree () {} // constructor for radix tree

    /* unfinished function to add a given string to the radix tree */
    public void addWord (Node parent, String subword, int freq) {

        int index = calcIndex(subword.charAt(0));   // converts first character to index in array of children
        Node current = parent.children[index];        // node currently in index where the subword belongs

        if (current == null) {    // if there is no word in the spot, place the subword there
            parent.children[index] = new Node(subword, freq, new Node[36]);
        } else {
            String prefix = current.word;     // word that is currently in the spot

            // if the subword is smaller than the prefix and is actually the prefix of 'prefix'. e.g. prefix = freedom and subword = free
            if (prefix.length() > subword.length() && subword.equals(prefix.substring(0, subword.length()))) {      
                Node new_parent = new Node (subword, freq, new Node[36]);
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
                while (subword.charAt(i) == prefix.charAt(i) && i < prefix.length()){
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
                current.children = new Node[36];    // create empty list of children
                current.children[calcIndex(new_child.word.charAt(0))] = new_child;  // list new_child as current's child
                current.word = prefix.substring(0, i);      // remove part of current's word that is in new_child
                addWord (current, subword.substring(i, subword.length()), freq);  // call function on substring, exlcuding portion of string that current holds
            }
        }
    }

    public Node findNode (char c, Node parent) {
        int index = calcIndex(c);
        return parent.children[index];
    }

    public String[] prefixMatch (Node parent, String prefix) {
        ArrayList<Pair> answers = new ArrayList<Pair>();
        if (parent.freq > 0) {
            answers.add(new Pair (parent.freq, prefix + parent.word));
        }
        dfs (parent, prefix + parent.word, answers);

        answers.sort(new comparatorPair());
        String[] sortedWords = new String[answers.size()];
        for (int i = 0; i < answers.size(); i++) {
            sortedWords[i] = answers.get(i).word;
        }
        return sortedWords;
    }

    public void dfs (Node parent, String prefix, ArrayList<Pair> answers) {
        for (int i = parent.children.length - 1; i >= 0; i--) {
            if (parent.children[i] != null) {
                if (parent.children[i].freq > 0) {
                    answers.add(new Pair (parent.children[i].freq, prefix + parent.children[i].word));
                } 
                dfs (parent.children[i], prefix + parent.children[i].word, answers);
            }
        }
    }

    public static class comparatorPair implements Comparator<Pair> {
        public int compare (Pair x, Pair y) {
            return Integer.compare(x.freq, y.freq);
        }
    }

    public static class Pair {
        int freq;
        String word;

        Pair (int f, String w) {freq = f; word = w;}
    }
    
    
    /*
    Needed function: Return all words with the given prefix in an array, sorted by frequency. Since the frequency does
    not need to be returned, only a String[] needs to be returned.
    Possible way to do this: traverse the tree to the last node that matches, then us something like
    depth first search to get all the words. 

    public String[] prefixMatch (String prefix) {

    }
    */

    /* For testing, prints radix tree using breadth-first search */
    public void printTree () {
        ArrayDeque<Node> queue = new ArrayDeque<Node>();
        queue.addLast(root);
        while (queue.size() > 0) {
            Node k = queue.removeFirst();
            System.out.printf("\"%s\" contains:", k.word);
            if (k.children != null) {           // may be able to remove this if statement, it should never be null
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

    // calculates index that character corresponds to
    public static int calcIndex (char c) {
        int index = c;
        if (index > 96) // lowercase letter
            return index - 87;
        else            // number
            return index - 48;
    }
}