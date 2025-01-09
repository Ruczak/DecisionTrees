package decisionTree;

/**
 * (Binary) decision tree structure, for now it does not support splitting into more subtrees
 * @param <T> tested structure type
 * @param <C> classification type
 */
public class DecisionTree<T, C> implements Node<T, C> {
    private final Tester<T> tester;
    private final Node<T, C> yesTree;
    private final Node<T, C> noTree;

    /**
     * Creates a decision tree, can be added as a subtree to a another tree.
     * @param tester testing interface (function)
     * @param yesTree subtree returned in case of filling the test's criteria (can be set to null to return true immidietaly)
     * @param noTree subtree returned in case of not filling the test's criteria (can be set to null to return false immidietaly)
     */
    public DecisionTree(Tester<T> tester, Node<T,C> yesTree, Node<T,C> noTree) throws IllegalArgumentException {
        if (tester == null) throw new IllegalArgumentException("tester cannot be null");
        if (yesTree == null) throw new IllegalArgumentException("yesTree cannot be null");
        if (noTree == null) throw new IllegalArgumentException("noTree cannot be null");

        this.yesTree = yesTree;
        this.noTree = noTree;
        this.tester = tester;
    }

    /**
     * Tests the object in the current node
     * @param object object we want to test.
     * @return true if object passes the testing function
     */
    public boolean test(T object) throws IllegalCallerException {
        return tester.test(object);
    }

    /**
     * Tester function for a decision tree object
     * @param <T> type of the tested object
     */
    public interface Tester<T> {
        boolean test(T object);
    }

    /**
     * Recursively finds the correct class that fits the decision tree model
     * @param object object we want to test.
     * @return integer representing class number
     */
    @Override
    public C getConditionalClass(T object) {
        if (test(object)) {
            return yesTree.getConditionalClass(object);
        }
        else {
            return noTree.getConditionalClass(object);
        }
    }
}

