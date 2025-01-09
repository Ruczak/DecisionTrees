import decisionTree.DecisionTree;
import decisionTree.LeafNode;

// Example from project manual
public class ProjectExample {
    private record Weather(String outlook, double windspeed, double temperature) {
        public String toString() {
            return "Weather{outlook=" + outlook + ", windspeed=" + windspeed + ", temperature=" + temperature + '}';
        }
    }

    public enum GoPlaying {
        YES,
        NO;

        public String toString() {
            return name().toLowerCase();
        }
    }

    public static class WindspeedTester implements DecisionTree.Tester<Weather> {
        private double splitValue;

        public WindspeedTester(double splitValue) {
            this.splitValue = splitValue;
        }

        @Override
        public boolean test(Weather object) {
            return object.windspeed >= splitValue;
        }
    }

    public static class OutlookTester implements DecisionTree.Tester<Weather> {
        private String testedOutlook;

        public OutlookTester(String testedOutlook) {
            this.testedOutlook = testedOutlook;
        }

        @Override
        public boolean test(Weather object) {
            return object.outlook.equals(testedOutlook);
        }
    }

    public static void main(String[] args) {
        DecisionTree<Weather, GoPlaying> windspeedTree = new DecisionTree<Weather, GoPlaying>(
                new WindspeedTester(10.0),
                new LeafNode<Weather, GoPlaying>(GoPlaying.YES),
                new LeafNode<Weather, GoPlaying>(GoPlaying.NO)
        );

        DecisionTree<Weather, GoPlaying> outlookTree = new DecisionTree<Weather, GoPlaying>(
                new OutlookTester("Overcast"),
                new LeafNode<Weather, GoPlaying>(GoPlaying.YES),
                windspeedTree
        );

        Weather[] weathers = new Weather[] {
                new Weather("Sunny", 12.5, 23.2),
                new Weather("Sunny", 8.4, 16.6),
                new Weather("Rain", 10.3, 21.8),
                new Weather("Overcast", 6.2, 5.3),
                new Weather("Sunny", 4.2, 12.0),
        };

        for (Weather weather : weathers) {
            System.out.println(weather + ": " + outlookTree.getConditionalClass(weather));
        }
    }
}
