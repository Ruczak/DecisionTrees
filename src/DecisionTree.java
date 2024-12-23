public class DecisionTree<T, V extends Comparable<V>> {
    public final boolean isLeaf;
    private Tester<T, V> tester;
    public final V splitValue;
    private DecisionTree yesTree;
    private DecisionTree noTree;

    /**
     * Creates a decision tree, can be added as a subtree to a another tree.
     * @param splitValue splitting value
     * @param tester testing interface (function)
     * @param yesTree subtree returned in case of filling the test's criteria (can be set to null to return true immidietaly)
     * @param noTree subtree returned in case of not filling the test's criteria (can be set to null to return false immidietaly)
     */
    public DecisionTree(V splitValue, Tester<T, V> tester, DecisionTree yesTree, DecisionTree noTree) {
        this.splitValue = splitValue;
        this.yesTree = yesTree;
        this.noTree = noTree;
        this.tester = tester;
        isLeaf = false;
    }

    /**
     * Creates a decision tree functioning as a leaf node (assigning only true/false value)
     * @param splitValue splitting value
     * @param tester testing interface (function)
     */
    public DecisionTree(V splitValue, Tester<T, V> tester) {
        this.splitValue = splitValue;
        this.tester = tester;
        isLeaf = true;
    }

    /**
     * Tests the object in the current node
     * @param object object we want to test.
     * @return true if object passes the testing function
     */
    public boolean test(T object) {
        return tester.test(object, splitValue);
    }

    public interface Tester<T, V extends Comparable<V>> {
        boolean test(T object, V splitValue);
    }

    /**
     * Gets the class of the object we want to test through recursion.
     * @param object object we want to test.
     * @return boolean value representing class: yes (true), no (false).
     */
    public boolean getConditionalClass(T object) {
        if (isLeaf) return test(object);
        else if (test(object)) {
            if (yesTree != null) return yesTree.getConditionalClass(object);
            else return true;
        }
        else {
            if (noTree != null) return noTree.getConditionalClass(object);
            else return false;
        }
    }
}

