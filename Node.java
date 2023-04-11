import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

// Node helper class for the Radix tree data structure 
public class Node {
   private char letter;
   private boolean isWord;
   int weight;
   Map<String,Node> children = new HashMap<>();
   Node(char letter,int weight) {
      this.letter = letter;
      this.weight = weight;  
   }
   public char getLetter() {
      return this.letter;
   }
   public int getWeight() {
      return this.weight;
   }
   public boolean isWord() {
      return this.isWord;
   }
   public Map getChildren() {
      return this.children;
   }
   public ArrayList listChild() {
      return
   }
}
