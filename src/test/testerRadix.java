/* Used to test RadixTree*/

public class testerRadix {
    public static void main (String args[]) {
        RadixTree tester = new RadixTree();
        tester.addWord(tester.root, "freebee", 5);
        tester.addWord(tester.root, "freedom", 7);
        tester.addWord(tester.root, "lucky", 4);
        tester.printTree();

        RadixTree.Node last = tester.findNode('f', tester.root);
        String[] answers = tester.prefixMatch(last, "");
        
        System.out.println("results: ");
        for (int i = 0; i < answers.length; i++) {
            System.out.println(answers[i]);
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