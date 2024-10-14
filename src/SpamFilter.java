import Helpers.MailImporter;

import java.io.File;
import java.util.*;

public class SpamFilter {

    private final String SPAM_LEARN_PATH = "/Users/cedricwagner/Downloads/spam-anlern";
    private final String HAM_LEARN_PATH = "/Users/cedricwagner/Downloads/ham-anlern";
    private final double RARE_WORDS_PROBABILITY = 0.01;

    private Map<String, Double> SpamKorpus;
    private Map<String, Double> HamKorpus;

    private Set<String> SpamWords;
    private Set<String> HamWords;

    private Map<String, Double> SpamProbabilityMatrix = new HashMap<>();
    private Map<String, Double> HamProbabilityMatrix = new HashMap<>();

    private int TotalMailsSpam = 0;
    private int TotalMailsHam = 0;

    private final MailImporter mailImporter = new MailImporter();

    public void configureSPAMFilter(){
        SpamKorpus = mailImporter.calculateCountOfWords(SPAM_LEARN_PATH);
        HamKorpus = mailImporter.calculateCountOfWords(HAM_LEARN_PATH);

        TotalMailsSpam = mailImporter.getTotalFilesInFolder(SPAM_LEARN_PATH);
        TotalMailsHam = mailImporter.getTotalFilesInFolder(HAM_LEARN_PATH);

        // Greenflags
        // Redflags

        SpamWords = new HashSet<>(SpamKorpus.keySet());
        HamWords = new HashSet<>(HamKorpus.keySet());

        SpamWords.removeAll(HamWords);
        HamWords.removeAll(SpamWords);
    }
    
    public void calculateProbability(){

        // SPAM
        for (String word : SpamKorpus.keySet()){
            setSpamProbabilityForWord(word, (SpamKorpus.get(word) / (double) TotalMailsSpam));
        }
        for (String word : SpamWords){
            setSpamProbabilityForWord(word, RARE_WORDS_PROBABILITY);
        }

        // HAM
        for (String word : HamKorpus.keySet()){
            setHamProbabilityForWord(word, (HamKorpus.get(word) / (double) TotalMailsHam));
        }
        for (String word : HamWords){
            setHamProbabilityForWord(word, RARE_WORDS_PROBABILITY);
        }
    }

    public void analyzeMails(String pathToMailFolder){
        Map<List<String>, String> mails = new HashMap<>();

        File[] files = mailImporter.getFilesInFolder(pathToMailFolder);



    }

    private double calculateSpamProbalityForWord(String word){

    }


    // Helpers
    private void setSpamProbabilityForWord(String word, Double probability){
        SpamProbabilityMatrix.put(word, probability);
    }

    private double getSpamProbabilityForWord(String word){
        SpamProbabilityMatrix.get(word);
    }

    private void setHamProbabilityForWord(String word, Double probability){
        HamProbabilityMatrix.put(word, probability);
    }
}
