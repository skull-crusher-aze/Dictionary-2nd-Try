import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class DictionaryManager {
    private static final String DICTIONARY_DIR = "dictionary";
    private static final String DICT_EXTENSION = ".dict";
    private static final String INFO_EXTENSION = ".info";

    public DictionaryManager() {
        File dir = new File(DICTIONARY_DIR);
        if (!dir.exists()) {
            dir.mkdir();
        }
    }

    public boolean createDictionary(String name, String sourceLang, String targetLang) throws IOException {
        File dictFile = new File(DICTIONARY_DIR + File.separator + name + DICT_EXTENSION);
        File infoFile = new File(DICTIONARY_DIR + File.separator + name + INFO_EXTENSION);

        if (dictFile.exists()) {
            return false;
        }

        dictFile.createNewFile();

        try (FileWriter writer = new FileWriter(infoFile)) {
            writer.write("sourceLang=" + sourceLang + "\n");
            writer.write("targetLang=" + targetLang + "\n");
        }
        return true;
    }

    public Dictionary loadDictionary(String name) throws IOException {
        File dictFile = new File(DICTIONARY_DIR + File.separator + name + DICT_EXTENSION);
        File infoFile = new File(DICTIONARY_DIR + File.separator + name + INFO_EXTENSION);

        if (!dictFile.exists() || !infoFile.exists()) {
            return null;
        }

        String sourceLang = "";
        String targetLang = "";

        try(BufferedReader reader = new BufferedReader(new FileReader(infoFile))) {
            String line;

            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("=", 2);
                if(parts.length == 2) {
                    if(parts[0].trim().equals("sourceLang")) {
                        sourceLang = parts[1].trim();
                    }
                    if(parts[0].trim().equals("targetLang")) {
                        targetLang = parts[1].trim();
                    }
                }
            }
        }

        return new Dictionary(name, sourceLang, targetLang, dictFile.getPath());
    }

    public boolean deleteDictionary(String name) {
        File dictFile = new File(DICTIONARY_DIR + File.separator + name + DICT_EXTENSION);
        File infoFile = new File(DICTIONARY_DIR + File.separator + name + INFO_EXTENSION);

        boolean dictDeleted = true;
        boolean infoDeleted = true;

        if (dictFile.exists()) {
            dictDeleted = dictFile.delete();
        }

        if (infoFile.exists()) {
            infoDeleted = infoFile.delete();
        }

        return dictDeleted && infoDeleted;
    }

    public List<String> listAllDictionaries() {
        List<String> dictionaries = new ArrayList<>();
        File dir = new File(DICTIONARY_DIR);

        if (dir.exists() && dir.isDirectory()) {
            File[] files = dir.listFiles((d, name) -> name.endsWith(DICT_EXTENSION));

            if (files != null) {
                for (File file : files) {
                    String dictName = file.getName();
                    dictName = dictName.substring(0, dictName.length() - DICT_EXTENSION.length());
                    dictionaries.add(dictName);
                }
            }
        }

        return dictionaries;
    }
}