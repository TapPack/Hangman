import java.nio.file.Paths;
import java.util.*;
import java.io.*;

public class Hangman {

    private Scanner scanner = new Scanner(System.in);

    static Map<String, HashSet<String>> gameDictionary;

    private String currentCategory = "";
    private String currentWord = "";
    private ArrayList<String> wordProgress = new ArrayList<>();

    private int score = 0;
    private int totalLetters = 0;
    private int intLetterProgress = 0;
    private int attemptsLeft = 10;

    public static void main(String[] args) throws Exception {

        File file = new File(Paths.get(".").toAbsolutePath().normalize().toString() + "\\Dictionary.txt");
        gameDictionary = fileToArrayString(file);

        Hangman hangman = new Hangman();

        hangman.playGame();
    }

    public  void playGame(){

        totalLetters = 0;
        intLetterProgress = 0;
        attemptsLeft = 10;
        wordProgress = new ArrayList<>();

        System.out.println("Please choose a category:");

        for ( String key : gameDictionary.keySet() ) {
            key = key.substring(0, 1).toUpperCase() + key.substring(1);
            System.out.println(key);
        }

        selectCategory();
        selectRandomWordFromCategory(gameDictionary.get(currentCategory));
        writeLetter();
    }

    private  void selectCategory(){

        String category = scanner.nextLine().toLowerCase();

        while(true) {

            if(!gameDictionary.keySet().contains(category)) {

                System.out.println("Category doesn't exist.");
                category = scanner.nextLine().toLowerCase();
            }
            else {
                currentCategory = category;

                break;
            }
        }
    }

    private  void selectRandomWordFromCategory(HashSet<String> words){

        ArrayList<String> wordsArray = new ArrayList<>();
        wordsArray.addAll(words);

        int randomIndex = new Random().nextInt((wordsArray.size() - 1));
        currentWord = wordsArray.get(randomIndex);

        for (int i = 0; i < currentWord.length(); i++){

            Character wordChar = currentWord.charAt(i);

            if(wordChar.equals(' ')) {
                wordProgress.add(" ");
            }
            else{
                wordProgress.add("_");
                totalLetters++;
            }
        }

        showCurrentWordProgress();
    }

    private  void showCurrentWordProgress(){

        System.out.println("Attempts left: " + attemptsLeft);
        System.out.println("Current word/phrase: " + getWordProgress());
    }

    private  StringBuilder getWordProgress(){
        StringBuilder wordSB = new StringBuilder();

        for (int i = 0; i < currentWord.length(); i++){
            wordSB.append(wordProgress.get(i) + " ");
        }

        return wordSB;
    }

    private  void writeLetter(){

        String wordLetter;

        while(attemptsLeft > 0){

            System.out.println("Please enter a letter: ");
            //wordChar = scanner.nextLine().charAt(0);
            wordLetter = scanner.nextLine();

            if(!wordLetter.isEmpty()) {

                Character wordChar = wordLetter.charAt(0);

                if (currentWord.toLowerCase().contains(wordChar.toString().toLowerCase())) {
                    unlockChar(wordChar.toString());
                } else {
                    System.out.println("The word/phrase doesn’t have this letter.");

                    attemptsLeft--;
                }
            }
            showCurrentWordProgress();
        }

        System.out.println("GAME OVER\nYou ended with " + score + " points");
    }

    private void unlockChar(String wordChar){

        Character currentChar;

        for (int i = 0; i < currentWord.length(); i++){
            currentChar = currentWord.charAt(i);

            if(currentChar.toString().toLowerCase().equals(wordChar.toLowerCase())){
                wordProgress.set(i, currentChar.toString());
                intLetterProgress++;
            }
        }

        winGame();
    }

    private void winGame(){

        if(intLetterProgress == totalLetters)
        {
            score++;

            System.out.println("Congratulations you have revealed the word/phrase:\n" +
                    getWordProgress() + "\n" +
                    "Current score: " + score);

            playGame();
        }
    }

    private static Map<String, HashSet<String>> fileToArrayString(File file) throws Exception{

        Map<String, HashSet<String>> gameDictionary = new HashMap<>();
        BufferedReader br = new BufferedReader(new FileReader(file));

        String st;
        String currentCategory = "";
        HashSet<String> words = new HashSet<>();

        while ((st = br.readLine()) != null){
            if(st.charAt(0) == '_'){  //Check if category is matched

                currentCategory = st.substring(1).toLowerCase();
                //categories.add(currentCategory);
                words = new HashSet<>();

                continue;
            }

            words.add(st);
            gameDictionary.put(currentCategory, words);
        }

        return gameDictionary;
    }
}
