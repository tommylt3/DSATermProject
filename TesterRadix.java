/* Used to test RadixTree*/

public class TesterRadix {
    public static void main (String args[]) {
        RadixTree tester = new RadixTree();
        tester.addWord(tester.root, "freebe", 5);
        tester.addWord(tester.root, "fire", 8);
        tester.addWord(tester.root, "fire", 8);
        tester.addWord(tester.root, "freedom", 7);
        tester.addWord(tester.root, "lucky", 4);
        tester.printTree();


        /*How to properly search for all words with the given prefix*/
        char[] fr = {'f', 'i'};
        RadixTree.Node last = tester.root;
        
        int i = 0;
        for (int j = 0; j < fr.length; j++) {
            RadixTree.Node temp = tester.findNode(fr[j], last, i);
            if (temp == last) {
                i++;
            } else {
                i = 0;
            }
            last = temp;

            String[] answers = new String[5];
            System.out.println("results: ");
            for (int k = 0; k < answers.length; k++) {
                System.out.println(answers[k]);
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
}