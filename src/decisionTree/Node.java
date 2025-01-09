package decisionTree;

/**
 * Node interface used in decision trees
 * @param <T> tested structure type
 * @param <C> classification type
 */
public interface Node<T, C> {
    /**
     * Gets conditional classification of the object
     * @param object tested object
     * @return classification object
     */
    C getConditionalClass(T object);
}