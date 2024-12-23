public class DecisionTree<T, V extends Comparable<V>> {
    public final boolean isLeaf;
    private Tester<T, V> tester;
    public final V splitValue;
    private DecisionTree yesTree;
    private DecisionTree noTree;

    public DecisionTree(V splitValue, Tester<T, V> tester, DecisionTree yesTree, DecisionTree noTree) {
        this.splitValue = splitValue;
        this.yesTree = yesTree;
        this.noTree = noTree;
        this.tester = tester;
        isLeaf = false;
    }

    public DecisionTree(V splitValue, Tester<T, V> tester) {
        this.splitValue = splitValue;
        this.tester = tester;
        isLeaf = true;
    }

    public boolean test(T object) {
        return tester.test(object, splitValue);
    }

    public interface Tester<T, V extends Comparable<V>> {
        boolean test(T object, V splitValue);
    }

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

