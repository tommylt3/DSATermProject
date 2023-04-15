import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

// Node helper class for the Radix tree data structure 
public class Node implements Comparable<Node> {
   // Variable Declaration 
   private String letter;
   private boolean isWord;
   private String fullWord;
   private int weight;
   private boolean isChecked;
   HashMap<String,Node> children = new HashMap<>();
   // Constructor 
   Node(String letter,int weight) {
      this.letter = letter;
      this.weight = weight;  
      this.isWord = false;
      this.fullWord = null;
      this.isChecked = false;
   }
   Node(String letter){
      this.letter = letter;
   }
   // Getter for the fullWord of this node 
   public String getFullWord() {
      return this.fullWord;
   }
   // Setter for the fullWord for this node 
   public void setFullWord(String fullWord) {
      this.fullWord = fullWord;
   }
   // Getter for the letter of this node 
   public String getLetter() {
      return this.letter;
   }
   // Setter for the letter for this node 
   public void setLetter(String letter) {
      this.letter = letter;
   }
   
   // Getter for the weight of this node 
   public int getWeight() {
      return this.weight;
   }
   
   // Setter for the weight for this node 
   public void setWeight(int weight) {
      this.weight = weight;
   }
   
   // Getter for the boolean of if the node is a word / end of this node 
   public boolean isWord() {
      return this.isWord;
   }

   // Setter for boolean of if the node is a word
   public void setWord(Boolean in) {
      this.isWord = in;
   }
   // Getter for the boolean of if the node is a Checked node during searching
   public boolean isChecked() {
      return this.isChecked;
   }

   // Setter for boolean of if the node is a checked
   public void setisChecked(Boolean in) {
      this.isChecked = in;
   }
   // Getter for the children of this node 
   public HashMap<String, Node> getChildren() {
      return this.children;
   }
   
   // Setter for the children
   public void setChildren(HashMap<String,Node> in) {
      this.children = in;
   }
   
   // Accessor method to add a child to the hash map 
   public void addChildren(String word, Node child) {
      this.children.put(word, child);
   }
   // accessor method to remove from HashMap of children
   public Node removeChildren(String word) {
      return this.children.remove(word);
   }
   // Getter for the children nodes in a array list of this node 
   public ArrayList<Node> listChild() {
      ArrayList<Node> childList = new ArrayList<>();
      for(String word : this.children.keySet()) {
         Node child = this.children.get(word);
         childList.add(child);
      }
      return childList;
   }
   
   // Getter for the all the names of children of a node
   public ArrayList<String> listChildNames() {
      ArrayList<String> childList = new ArrayList<>();
      for(String word : this.children.keySet()) {
         childList.add(word);
      }
      return childList;
   }
   
   // print all children 
   public void printChildren() {
	 for(String word : this.children.keySet()) {
	     System.out.println(this.children.get(word));
	  } 
   }
   // to String method for Node
   public String toString() {
      if(this.isWord) {
         return this.fullWord + " : " + this.weight;
      }
      return this.letter + " : " + this.weight;
   }
   public int compareTo(Node o) {
      // TODO Auto-generated method stub
      Integer hold = this.weight;
      return hold.compareTo(o.weight);
   }
   
}