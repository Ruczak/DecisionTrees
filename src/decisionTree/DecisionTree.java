package decisionTree;

/**
 * (Binary) decision tree structure, for now it does not support splitting into more subtrees
 * @param <T> tested structure type
 * @param <C> classification type
 */
public class DecisionTree<T, C> implements Node<T, C> {
    public final Tester<T> tester;
    public final Node<T, C> leftTree;
    public final Node<T, C> rightTree;

    /**
     * Creates a decision tree, can be added as a subtree to a another tree.
     * @param tester testing interface (function)
     * @param leftTree subtree returned in case of filling the test's criteria (can be set to null to return true immidietaly)
     * @param rightTree subtree returned in case of not filling the test's criteria (can be set to null to return false immidietaly)
     */
    public DecisionTree(Tester<T> tester, Node<T,C> leftTree, Node<T,C> rightTree) throws IllegalArgumentException {
        if (tester == null) throw new IllegalArgumentException("tester cannot be null");
        if (leftTree == null) throw new IllegalArgumentException("yesTree cannot be null");
        if (rightTree == null) throw new IllegalArgumentException("noTree cannot be null");

        this.leftTree = leftTree;
        this.rightTree = rightTree;
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
            return leftTree.getConditionalClass(object);
        }
        else {
            return rightTree.getConditionalClass(object);
        }
    }

    /**
     * Prints recursively the whole tree in a readible way
     * @param depth maximum depth of print
     * @param indent initial indent, for each level increases by 4 spaces
     */
    public void prettyPrint(int depth, String indent) {
        if (depth < 0) {
            System.out.println("...");
            return;
        }


        System.out.println("DecisionTree { ");
        System.out.println(indent + "    tester=" + tester);

        System.out.print(indent + "    leftTree=");
        if (leftTree instanceof DecisionTree<T, C>) {
            ((DecisionTree<T, C>) leftTree).prettyPrint(depth - 1, indent + "    ");
        }
        else System.out.println(leftTree.toString());

        System.out.print(indent + "    rightTree=");
        if (rightTree instanceof DecisionTree<T, C>) {
            ((DecisionTree<T, C>) rightTree).prettyPrint(depth - 1, indent + "    ");
        }
        else System.out.println(rightTree.toString());

        System.out.println(indent + "}");
    }

    @Override
    public String toString() {
        return "DecisionTree[tester=" + tester + ", leftTree=" + leftTree + ", rightTree=" + rightTree + "]";
    }
}

