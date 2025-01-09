package decisionTree;

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
