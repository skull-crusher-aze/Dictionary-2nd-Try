import java.util.*;
import java.io.*;

public class Dictionary {
    private String name;
    private String sourceLanguage;
    private String targetLanguage;
    private String filePath;
    private Map<String,String> words;

    public Dictionary(String name, String sourceLanguage, String targetLanguage, String filePath) {
        this.name = name;
        this.sourceLanguage = sourceLanguage;
        this.targetLanguage = targetLanguage;
        this.filePath = filePath;
        this.words = new HashMap<>();

        try {
            loadWordsFromFile();
        } catch (IOException e) {
            System.out.println("Error loading words from file");
        }
    }

    public String getName() {
        return name;
    }

    public String getSourceLanguage() {
        return sourceLanguage;
    }

    public String getTargetLanguage() {
        return targetLanguage;
    }

    private void loadWordsFromFile() throws FileNotFoundException, IOException {
        File file = new File(filePath);
        if (!file.exists())
            return;

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("=", 2);
                if (parts.length == 2) {
                    words.put(parts[0].trim(), parts[1].trim());
                }
            }
        }
    }

    private void saveWordsToFile() throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            for (Map.Entry<String, String> entry : words.entrySet()) {
                writer.write(entry.getKey() + "=" + entry.getValue());
                writer.newLine();
            }
        }
    }

    public boolean addWord(String word, String translation) throws IOException {
        words.put(word, translation);
        saveWordsToFile();
        return true;
    }

    public boolean deleteWord(String word) throws IOException {
        if(words.containsKey(word)) {
            words.remove(word);
            saveWordsToFile();
            return true;
        }
        return false;
    }

    public String searchWord(String word){
        return words.get(word);
    }

    public Map<String, String> getAllWords() {
        return new HashMap<>(words);
    }
}
