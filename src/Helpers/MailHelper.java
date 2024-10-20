package Helpers;

import java.io.*;
import java.util.*;

public final class MailHelper {

    /**
     * Reads Content of a Document in a Buffered Reader and returns a list of the containing words
     * @param pathToMail - path to the Mail Document
     * @return list of words contained in the Mail Document
     */
    public static List<String> readMailDocument(String pathToMail){
        List<String> words = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(pathToMail))) {
            String line;

            while ((line = br.readLine()) != null) {
                String[] lineWords = line.split("\\s+");
                for (String word : lineWords) {
                    if (!word.isEmpty()) {
                        words.add(word.toLowerCase());
                    }
                }
            }

        } catch (IOException e) {
            System.out.println("Error reading file: " + e.getMessage());
        }

        return words;
    }

    /**
     * Calculates occurrences of a word in a set of mail documents and returns a map with the key "word" and value of the occurrence
     * @param pathToMailFolder - path to the Mail Document
     * @return returns a Map of string and their occurrences
     */
    public static Map<String, Double> calculateCountOfWords(String pathToMailFolder){
        List<String> words;
        Map<String, Double> countedWords = new HashMap<>();

        File path = new File(pathToMailFolder);
        File [] files = path.listFiles();

        for (int i = 0; i < files.length; i++) {
            System.out.println("🔍 Reading Mails (" + (i+1) + "/" + files.length + ")" );
            words = readMailDocument(files[i].getAbsolutePath());
            Set<String> distinctWords = new HashSet<>(words);

            for (String word : distinctWords){
                countedWords.put(word, countedWords.getOrDefault(word, 0.0) + 1);
            }
        }

        return countedWords;
    }

    /**
     * Returns a File Array containing all files in a folder
     * @param pathToFolder - path to the folder
     * @return returns an Array of files in the given folder
     */
    public static File[] getFilesInFolder(String pathToFolder){
        File path = new File(pathToFolder);
        return path.listFiles();
    }

}
