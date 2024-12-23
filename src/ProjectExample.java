// Example from project manual
public class ProjectExample {
    private record Weather(String outlook, double windspeed, double temperature) {
        public String toString() {
            return "Weather{outlook=" + outlook + ", windspeed=" + windspeed + ", temperature=" + temperature + '}';
        }
    }

    public static void main(String[] args) {
        DecisionTree.Tester<Weather, Double> windspeedTester = new DecisionTree.Tester<Weather, Double>() {
            @Override
            public boolean test(Weather object, Double splitValue) {
                return object.windspeed >= splitValue;
            }
        };
        DecisionTree.Tester<Weather, String> outlookTester = new DecisionTree.Tester<Weather, String>() {
            @Override
            public boolean test(Weather object, String splitValue) {
                return object.outlook.equals(splitValue);
            }
        };

        DecisionTree<Weather, String> outlookTree = new DecisionTree<Weather, String>(
                "Overcast",
                outlookTester,
                null,
                new DecisionTree<Weather, Double>(10.0, windspeedTester)
        );

        Weather[] weathers = new Weather[] {
                new Weather("Sunny", 12.5, 23.2),
                new Weather("Sunny", 8.4, 16.6),
                new Weather("Rain", 10.3, 21.8),
                new Weather("Overcast", 6.2, 5.3),
                new Weather("Sunny", 4.2, 12.0),
        };

        for (Weather weather : weathers) {
            System.out.println(weather + ": " + (outlookTree.getConditionalClass(weather) ? "Yes" : "No"));
        }
    }
}
