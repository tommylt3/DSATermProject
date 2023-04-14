import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;

// Radix Tree Class for CSE2010 Term Project 
public class RadixTree {
   // Variable Declaration 
   private Node root;
   // Constructor
   public RadixTree() {
      this.root = new Node(":D",-1);
   }
   public Node getRoot() {
      return this.root;
   }
   // Insert Method 
   public void insert(String word, int weight) {
      // if the word exist update its weight + 1 
      Node hold = this.findNode(word);
      if(this.findNode(word) != null) {
         hold.setWeight(hold.getWeight() + 1);
         return;
      }
      // starts at the root node 
      Node curr = root;
      // for loop to go through the entire word to add each part of the word into the tree
      for (int i = 0; i < word.length(); i++) {
          // pulls each part of the 
          String substring = word.substring(i,i+1);
          Node child = curr.children.get(substring);

          if (child == null) {
              child = new Node(substring.substring(0, 1));
              curr.children.put(substring, child);
          }

          curr = child;
      }

      curr.setWord(true);
      curr.setWeight(weight);
      curr.setFullWord(word);
   }
   // Find Node Method- Need to add more comments 
   public Node findNode(String word) {
     // starts at the root node 
     Node curr = root;
     // for loop to traverse entire word 
     for (int i = 0; i < word.length();i++) {
         Node child = curr.children.get(word.substring(i, i+1));
         if (child == null) {
             return null;
         }
         if (child.getLetter().equals(word.substring(i, i + child.getLetter().length()))) {
             curr = child;
             //i += child.getLetter().length();
         } else {
             return null;
         }
     }

     return curr;
   }
   // Find prefix method 
   public ArrayList<Node> findPrefix(String prefix) {
      ArrayList<Node> nodes = new ArrayList<>();
      // finds where the prefix is in the RadixTree 
      // returns null if the prefix is not found in the 
      // radix tree and prints an error 
      Node word = this.findNode(prefix);
      if(word == null) {
         //System.out.println(prefix + " no node found");
         return null;
      }
      // Checks if the prefix that was passed as a parameter is a full word 
      // adds the word to the nodes array list
      if(word.isWord()) {
         nodes.add(word);
      }
      // BFS 
      Queue<Node> queueHold = new LinkedList<>();
      // Start with prefix node in queue 
      queueHold.add(word);
      // loop to run until no children are left 
      while(!queueHold.isEmpty()) {
         // remove one Node from the queue 
         Node curr = queueHold.remove();
         // if the node is a 
         if(curr.isWord()) {
            nodes.add(curr);
         }
         // get a list of all of curr's children 
         ArrayList<Node> listChildren = curr.listChild();
         // loop through all children and enqueue 
         for(int i = 0; i < listChildren.size(); i++) {
            queueHold.add(listChildren.get(i));
         }
      }
      return nodes;
   }
   // Breadth first search to print tree 
   public void bfs() {
      // Queue to store children of Node curr
      Queue<Node> queueNode = new LinkedList<>();
      // Start with root node in queue 
      queueNode.add(this.root);
      // loop to run until no children are left 
      while(!queueNode.isEmpty()) {
         // remove one Node from the queue 
         Node curr = queueNode.remove();
         // Print the node curr s
         System.out.println(curr + " contains : ");
         // get a list of all of curr's children 
         ArrayList<Node> listChildren = curr.listChild();
         // loop through all children and enqueue 
         for(int i = 0; i < listChildren.size(); i++) {
            queueNode.add(listChildren.get(i));
         }
      }
   }
   public String[] bestGuesses(String prefix) {
      String[] output = new String[5];
      ArrayList<Node> guesses = this.findPrefix(prefix);
      if(guesses == null) {
         System.out.println("No matching prefixes");
         return null;
      }
      Collections.sort(guesses);
      int min = Integer.min(guesses.size(), 5);
      int j = 0;
      for(int i = guesses.size() -  1; i >= guesses.size() - min; i--) {
         output[j] = guesses.get(i).getFullWord();
         j++;
      }
      
      return output;
   }
   public void printBestGuesses(String prefix) {
      ArrayList<Node> guesses = this.findPrefix(prefix);
      if(guesses == null) {
         System.out.println("No matching prefixes");
         return;
      }
      Collections.sort(guesses);
      int min = Integer.min(guesses.size(), 5);
      System.out.println("Guess size" + guesses.size());
      for(int i = guesses.size() -  1; i >= guesses.size() - min; i--) {
         System.out.print(guesses.get(i) + " ");
      }
      System.out.println();
   }
   public void  buildRadix(String fileName) throws FileNotFoundException {
      RadixTree out = new RadixTree();
      Scanner sc = new Scanner(new File(fileName));
      while(sc.hasNextLine()) {
         String hold = sc.nextLine();
         this.insert(hold,1);
      }
   }
   
   
}