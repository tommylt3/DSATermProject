import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

// Node helper class for the Radix tree data structure 
public class Words {
   // Variable Declaration 
   private String letter;
   private boolean isWord;
   int weight;
   Map<String,Words> children = new HashMap<>();
   // Constructor 
   Words(String letter,int weight) {
      this.letter = letter;
      this.weight = weight;  
      this.isWord = false;
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
   public void getWeight(int weight) {
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
   
   // Getter for the children of this node 
   public Map<String, Words> getChildren() {
      return this.children;
   }
   
   // Setter for the children
   public void setChildren(HashMap<String,Words> in) {
      this.children = in;
   }
   
   // Accessor method to add a child to the hash map 
   public void addChildren(String word, Words child) {
      this.children.put(word, child);
   }
   // accessor method to remove from HashMap of children
   public Words removeChildren(String word) {
      return this.children.remove(word);
   }
   // Getter for the children nodes in a array list of this node 
   public ArrayList<Words> listChild() {
      ArrayList<Words> childList = new ArrayList<>();
      for(String word : this.children.keySet()) {
         Words child = this.children.get(word);
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
   
}