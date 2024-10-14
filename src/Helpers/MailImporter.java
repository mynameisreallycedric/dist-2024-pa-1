package Helpers;

import java.io.*;
import java.util.*;

public final class MailImporter {

    private static Map<String, Double> countedWords = new HashMap<>();

    public static List<String> readMailDocument(String pathToMail){
        List<String> words = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(pathToMail))) {
            String line;

            while ((line = br.readLine()) != null) {
                String[] lineWords = line.split("\\s+");
                for (String word : lineWords) {
                    if (!word.isEmpty()) {
                        words.add(word);
                    }
                }
            }

        } catch (IOException e) {
            System.out.println("Error reading file: " + e.getMessage());
        }

        return words;
    }

    public static Map<String, Double> calculateCountOfWords(String pathToMailFolder){
        List<String> words;

        File path = new File(pathToMailFolder);
        File [] files = path.listFiles();

        for (int i = 0; i < files.length; i++) {
            System.out.println("🔍 Reading Mails (" + (i+1) + "/" + files.length + ")" );
            words = readMailDocument(files[i].getAbsolutePath());
            Set<String> distinctWords = new HashSet<>(words);

            for (String word : distinctWords){
                countedWords.put(word, countedWords.getOrDefault(word, 1.0) + 1);
            }
        }

        return countedWords;
    }

    public static int getTotalFilesInFolder(String pathToFolder){
        File path = new File(pathToFolder);
        return path.listFiles().length;
    }

    public static File[] getFilesInFolder(String pathToFolder){
        File path = new File(pathToFolder);
        return path.listFiles();
    }

}
