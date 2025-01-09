import decisionTree.DecisionTree;
import decisionTree.LeafNode;

public class ExampleStructure {
    private int id;
    private double value1;
    private int value2;

    public ExampleStructure(int id, double value1, int value2) {
        this.id = id;
        this.value1 = value1;
        this.value2 = value2;
    }

    public int getId() {
        return id;
    }

    public double getValue1() {
        return value1;
    }

    public double getValue2() {
        return value2;
    }

    public String toString() {
        return "ExampleStructure{id=" + id + ", value1=" + value1 + ", value2=" + value2 + '}';
    }

    public static class Value1Tester implements DecisionTree.Tester<ExampleStructure> {
        private final double splitValue;

        public Value1Tester(double splitValue) {
            this.splitValue = splitValue;
        }

        @Override
        public boolean test(ExampleStructure object) {
            return object.getValue1() >= splitValue;
        }
    }

    public static class Value2Tester implements DecisionTree.Tester<ExampleStructure> {
        private final int splitValue;

        public Value2Tester(int splitValue) {
            this.splitValue = splitValue;
        }

        @Override
        public boolean test(ExampleStructure object) {
            return object.getValue2() >= splitValue;
        }
    }

    public static void main(String[] args) {
        ExampleStructure[] exampleStructures = {
                new ExampleStructure(1, 3.0, 90),
                new ExampleStructure(2, -4.5, 60),
                new ExampleStructure(3, 1.3, 30),
                new ExampleStructure(4, 2.4, 80),
                new ExampleStructure(5, -3, 39),
                new ExampleStructure(6, 1.8, 107),
        };

        DecisionTree<ExampleStructure, String> value2Subtree = new DecisionTree<>(
                new Value2Tester(60),
                new LeafNode<>("Yes"),
                new LeafNode<>("No")
        );

        DecisionTree<ExampleStructure, String> value1Subtree = new DecisionTree<>(
                new Value1Tester(0.0),
                new LeafNode<>("Yes"),
                value2Subtree
        );

        for (ExampleStructure exampleStructure : exampleStructures) {
            System.out.println(exampleStructure + ": " + value1Subtree.getConditionalClass(exampleStructure));
        }
    }
}
