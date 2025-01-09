package decisionTree;

/**
 * Leaf node in the decision tree
 * @param <T> tested structure type
 * @param <C> classification type
 */
public class LeafNode<T, C> implements Node<T, C> {
    public final C value;

    public LeafNode(C value) {
        this.value = value;
    }

    @Override
    public C getConditionalClass(T object) {
        return value;
    }
}
