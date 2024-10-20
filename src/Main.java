public class Main {
    public static void main(String[] args) {

        final String SPAM_CALIBRATION = "/Users/cedricwagner/Downloads/spam-kallibrierung";
        final String HAM_CALIBRATION = "/Users/cedricwagner/Downloads/ham-kallibrierung";

        final String SPAM_TEST = "/Users/cedricwagner/Downloads/spam-test";
        final String HAM_TEST = "/Users/cedricwagner/Downloads/ham-test";

        SpamFilter spamFilter = new SpamFilter();

        spamFilter.configureSPAMFilter();
        spamFilter.calculateProbability();

        spamFilter.analyzeMails(HAM_TEST);

    }
}