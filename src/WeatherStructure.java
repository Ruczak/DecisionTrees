import decisionTree.DecisionTree;
import decisionTree.LeafNode;

// example from Introduction to DSAI
public class WeatherStructure {
    public final String outlook;
    public final String temperature;
    public final String humidity;
    public final String wind;

    public WeatherStructure(String outlook, String temperature, String humidity, String wind) {
        this.outlook = outlook;
        this.temperature = temperature;
        this.humidity = humidity;
        this.wind = wind;
    }

    public String toString() {
        return "WeatherStructure{outlook=" + outlook + ", temperature=" + temperature + ", humidity=" + humidity + ", wind=" + wind + '}';
    }

    private static class WeatherStringTester implements DecisionTree.Tester<WeatherStructure> {
        public enum Property {
            Outlook, Temperature, Humidity, Wind
        }

        private final String splitValue;
        private final Property property;

        public WeatherStringTester(Property property, String splitValue) {
            this.property = property;
            this.splitValue = splitValue;
        }

        private String getPropertyValue(WeatherStructure object) {
            return switch (property) {
                case Outlook -> object.outlook;
                case Temperature -> object.temperature;
                case Humidity -> object.humidity;
                case Wind -> object.wind;
            };
        }

        @Override
        public boolean test(WeatherStructure object) {
            return getPropertyValue(object).equals(splitValue);
        }
    }

    public static void main(String[] args) {
        WeatherStructure[] structures = new WeatherStructure[] {
            new WeatherStructure("Sunny", "Hot", "High", "Weak"),
            new WeatherStructure("Sunny", "Hot", "High", "Strong"),
            new WeatherStructure("Sunny", "Mild", "High", "Weak"),
            new WeatherStructure("Sunny", "Cool", "Normal", "Weak"),
            new WeatherStructure("Sunny", "Mild", "Normal", "Strong"),
            new WeatherStructure("Rain", "Mild", "High", "Weak"),
            new WeatherStructure("Rain", "Cool", "Normal", "Weak"),
            new WeatherStructure("Rain", "Cool", "Normal", "Strong"),
            new WeatherStructure("Rain", "Mild", "Normal", "Weak"),
            new WeatherStructure("Rain", "Mild", "High", "Strong"),
        };

        DecisionTree<WeatherStructure, String> humiditySubTree = new DecisionTree<>(
                new WeatherStringTester(WeatherStringTester.Property.Humidity, "Normal"),
                new LeafNode<>("Yes"),
                new LeafNode<>("No")
        );

        DecisionTree<WeatherStructure, String> windSubTree = new DecisionTree<WeatherStructure, String>(
                new WeatherStringTester(WeatherStringTester.Property.Wind, "Weak"),
                new LeafNode<>("Yes"),
                new LeafNode<>("No")
        );

        DecisionTree<WeatherStructure, String> outlookTree = new DecisionTree<WeatherStructure, String>(
                new WeatherStringTester(WeatherStringTester.Property.Outlook, "Sunny"),
                humiditySubTree,
                windSubTree
        );

        for (WeatherStructure object : structures) {
            System.out.println(object + ": " + outlookTree.getConditionalClass(object));
        }
    }
}
