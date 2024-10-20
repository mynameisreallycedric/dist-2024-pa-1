import Helpers.MailHelper;

import java.io.File;
import java.util.*;

public class SpamFilter {

    private final String SPAM_LEARN_PATH = "/Users/cedricwagner/Downloads/spam-anlern";
    private final String HAM_LEARN_PATH = "/Users/cedricwagner/Downloads/ham-anlern";

    private Map<String, Double> SpamVocabulary;
    private Map<String, Double> HamVocabulary;

    private Set<String> SpamOnlyWords;
    private Set<String> HamOnlyWords;

    private Map<String, Double> SpamProbabilityMatrix = new HashMap<>();
    private Map<String, Double> HamProbabilityMatrix = new HashMap<>();

    private final MailHelper mailHelper = new MailHelper();

    /* Configurable Parameters  */
    private final double P_S = 0.5;
    private final double P_H = 0.5;

    private final double ALPHA = 0.00000001;
    private final double THRESHOLD_VALUE = 0.7;

    /**
     * Creates the Spam and Ham Vocabulary
     */
    public void configureSPAMFilter(){
        SpamVocabulary = mailHelper.calculateCountOfWords(SPAM_LEARN_PATH);
        HamVocabulary = mailHelper.calculateCountOfWords(HAM_LEARN_PATH);

        SpamOnlyWords = new HashSet<>(SpamVocabulary.keySet());
        HamOnlyWords = new HashSet<>(HamVocabulary.keySet());

        SpamOnlyWords.removeAll(HamVocabulary.keySet());
        HamOnlyWords.removeAll(SpamVocabulary.keySet());
    }

    /**
     * Calculates the Spam and Ham probability of each word in the vocabulary
     */
    public void calculateProbability() {
        // SPAM
        double totalSpamWords = getTotalSpamWords();

        for (String word : SpamVocabulary.keySet()){
            setSpamProbabilityForWord(word, (SpamVocabulary.get(word) / totalSpamWords));
        }
        for (String word : HamOnlyWords){
            setSpamProbabilityForWord(word, ALPHA);
        }

        double totalHamWords = getTotalHamWords();

        // HAM
        for (String word : HamVocabulary.keySet()){
            setHamProbabilityForWord(word, (HamVocabulary.get(word) / totalHamWords));
        }
        for (String word : SpamOnlyWords){
            setHamProbabilityForWord(word, ALPHA);
        }
    }

    /**
     * Analyzes a folder of mail documents if they contain spam or ham mails
     * The calulation is based on the formula found in the following article:
     * https://en.wikipedia.org/wiki/Naive_Bayes_spam_filtering#Other_expression_of_the_formula_for_combining_individual_probabilities
     * @param pathToMailFolder - path to the folder containing the mail documents
     */
    public void analyzeMails(String pathToMailFolder){
        int SpamCount = 0;
        File[] files = mailHelper.getFilesInFolder(pathToMailFolder);

        for (File mail : files){
            System.out.println();
            System.out.println("Analyzing Mail: " + mail.getName() + "🕵🏻‍♂️");

            List<String> mailContent = MailHelper.readMailDocument(mail.getAbsolutePath());
            Set<String> distinctWords = new HashSet<>(mailContent);

            double SpamProbabiltyOfMail = ((calculateHamProbalityForMail(distinctWords)) - (calculateSpamProbabilityForMail(distinctWords)));

            double TotalSpamProbabilityOfMail = 1 / (1 + Math.exp(SpamProbabiltyOfMail));

            System.out.println("Spam Probality: " + TotalSpamProbabilityOfMail);


            if (TotalSpamProbabilityOfMail > THRESHOLD_VALUE) {
                System.out.println("This email is likely SPAM 👹");
                SpamCount ++;
            } else {
                System.out.println("This email is likely HAM 🍖");
            }
        }

        System.out.println("------------------------------------------------------");
        System.out.println();
        System.out.println("The results are in. 🥁");
        System.out.println();
        System.out.println("Configured Values:");
        System.out.println("------------------------------------------------------");
        System.out.println("Threshold: " + THRESHOLD_VALUE);
        System.out.println("Alpha: " + ALPHA);
        System.out.println("Analyzed Folder: " + pathToMailFolder);
        System.out.println();
        System.out.println("Classified:");
        System.out.println("------------------------------------------------------");
        System.out.println("Mails classified as Spam " + SpamCount + "/" + files.length);
    }

    /**
     * Calculates the total spam probability of a mail document by adding the spam probability of each word.
     * the calculation is done in the log domain to ensure that no floating point underflow can happen.
     * @param mailContent - list of words contained in a given mail
     */
    private double calculateSpamProbabilityForMail(Set<String> mailContent){
        double probability = Math.log(P_S);

        for (String word : mailContent){
            double wordProbability = getSpamProbabilityForWord(word);
            if (wordProbability > 0) {
                probability += Math.log(wordProbability);
            }
        }

        return probability;
    }

    /**
     * Calculates the total ham probability of a mail document by adding the ham probability of each word.
     * the calculation is done in the log domain to ensure that no floating point underflow can happen.
     * @param mailContent - list of words contained in a given mail
     */
    private double calculateHamProbalityForMail(Set<String> mailContent){
        double probability = Math.log(P_H);

        for (String word : mailContent){
            double wordProbability = getHamProbabilityForWord(word);
            if (wordProbability > 0) {
                probability += Math.log(wordProbability);
            }
        }

        return probability;
    }

    // Helpers
    private void setSpamProbabilityForWord(String word, Double probability){
        SpamProbabilityMatrix.put(word, probability);
    }

    private double getSpamProbabilityForWord(String word){
        return SpamProbabilityMatrix.getOrDefault(word, 0.0);
    }

    private void setHamProbabilityForWord(String word, Double probability){
        HamProbabilityMatrix.put(word, probability);
    }

    private double getHamProbabilityForWord(String word){
        return HamProbabilityMatrix.getOrDefault(word, 0.0);
    }

    private double getTotalSpamWords() {
        return SpamVocabulary.values().stream().mapToDouble(Double::doubleValue).sum();
    }

    private double getTotalHamWords() {
        return HamVocabulary.values().stream().mapToDouble(Double::doubleValue).sum();
    }
}
