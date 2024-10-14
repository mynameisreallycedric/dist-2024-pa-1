import Helpers.MailImporter;

import java.util.Map;

public class Main {
    public static void main(String[] args) {

        SpamFilter spamFilter = new SpamFilter();

        spamFilter.configureSPAMFilter();
        spamFilter.calculateProbability();


    }
}