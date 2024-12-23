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

    public static void main(String[] args) {
        ExampleStructure[] exampleStructures = {
                new ExampleStructure(1, 3.0, 90),
                new ExampleStructure(2, -4.5, 60),
                new ExampleStructure(3, 1.3, 30),
                new ExampleStructure(4, 2.4, 80),
                new ExampleStructure(5, -3, 39),
                new ExampleStructure(6, 1.8, 107),
        };

        DecisionTree.Tester<ExampleStructure, Double> value1Tester = new DecisionTree.Tester<ExampleStructure, Double>() {
            @Override
            public boolean test(ExampleStructure object, Double splitValue) {
                return object.getValue1() >= splitValue;
            }
        };
        DecisionTree.Tester<ExampleStructure, Integer> value2Tester = new DecisionTree.Tester<ExampleStructure, Integer>() {
            @Override
            public boolean test(ExampleStructure object, Integer splitValue) {
                return object.getValue2() >= splitValue;
            }
        };

        DecisionTree<ExampleStructure, Integer> value2Subtree = new DecisionTree<>(60, value2Tester);
        DecisionTree<ExampleStructure, Double> value1Subtree = new DecisionTree<>(0.0, value1Tester, null, value2Subtree);

        for (ExampleStructure exampleStructure : exampleStructures) {
            System.out.println(exampleStructure + ": " + value1Subtree.getConditionalClass(exampleStructure));
        }
    }
}
