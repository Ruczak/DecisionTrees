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

    private static class WeatherStringTester implements DecisionTree.Tester<WeatherStructure, String> {
        public enum Property {
            Outlook, Temperature, Humidity, Wind
        }

        private final Property property;

        public WeatherStringTester(Property property) {
            this.property = property;
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
        public boolean test(WeatherStructure object, String splitValue) {
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
                "Normal",
                new WeatherStringTester(WeatherStringTester.Property.Humidity)
        );

        DecisionTree<WeatherStructure, String> windSubTree = new DecisionTree<WeatherStructure, String>(
                "Weak",
                new WeatherStringTester(WeatherStringTester.Property.Wind)
        );

        DecisionTree<WeatherStructure, String> outlookTree = new DecisionTree<WeatherStructure, String>(
                "Sunny",
                new WeatherStringTester(WeatherStringTester.Property.Outlook),
                humiditySubTree,
                windSubTree
        );

        for (WeatherStructure object : structures) {
            System.out.println(object + ": " + outlookTree.getConditionalClass(object));
        }
    }
}
