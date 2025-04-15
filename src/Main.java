import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class Main {
    public static void main(String[] args) {
    System.out.println(GLOSSARY_SIZE + " слово");
    startGame();
}
    private final static int GLOSSARY_SIZE = 51302;
    private final static int ATTEMT_NUMBER_1 = 1;
    private final static int ATTEMT_NUMBER_2 = 2;
    private final static int ATTEMT_NUMBER_3 = 3;
    private final static int ATTEMT_NUMBER_4 = 4;
    private final static int ATTEMT_NUMBER_5 = 5;
    private final static int ATTEMT_NUMBER_6 = 6;
    private final static int MAX_ATTEMTS = 6;
    private final static String START = "1";
    private final static String QUIT = "2";
    private final static String MASK_CHAR = "*";
    private final static String WORDS_FILE = "russian_nouns.txt";
    private final static String RUSSIAN_LETTERS = "абвгдеёжзийклмнопрстуфхцчшщъыьэюяАБВГДЕЁЖЗИЙКЛМНОПРСТУФХЦЧШЩЪЫЬЭЮЯ";

    static String getRandomWord(){
        BufferedReader reader = null;
        Random rand = new Random();
        int count = 0;
        int numberInGlossary = rand.nextInt(GLOSSARY_SIZE);
        String randomWord = "";
        try {
            reader = new BufferedReader(new FileReader(WORDS_FILE));
            String line;
            while ((line = reader.readLine()) != null) {
                if (numberInGlossary == count){
                    randomWord = line;
                    break;
                }
                count++;
            }
        } catch (IOException error) {
            System.err.println("Ошибка чтения файла: " + error.getMessage());
        } finally {
            try {
                if (reader != null) {
                    reader.close();
                }
            } catch (IOException error) {
                System.err.println("Ошибка закрытия файла: " + error.getMessage());
            }
        }
        return randomWord;
    }

    static String updateMaskWord(String letter, String  maskWord, String word){
        if (word.contains(letter)){
            for (int i = 0; i<word.length();i++){
                if (word.charAt(i)==letter.charAt(0)){
                    maskWord = maskWord.substring(0,i) + letter.toUpperCase() + maskWord.substring(i+1);
                }
            }
        }
        return maskWord;
    }

    static  boolean isRussianLetter(String letter){
        String russianLetters = RUSSIAN_LETTERS;
        return (russianLetters.contains(letter));
    }

    static String getGallowsImage(int attemptNumber){
        Map<Integer,String> imagesOfGallows = new HashMap<>();
        imagesOfGallows.put(ATTEMT_NUMBER_1, """
                ------
                |  |
                |  O
                |
                |
                -
                """);
        imagesOfGallows.put(ATTEMT_NUMBER_2, """
                ------
                |  |
                |  O
                |  |
                |
                -
                """);
        imagesOfGallows.put(ATTEMT_NUMBER_3, """
                ------
                |  |
                |  O
                | /|
                |
                -
                """);
        imagesOfGallows.put(ATTEMT_NUMBER_4, """
                ------
                |  |
                |  O
                | /|\\
                |
                -
                """);
        imagesOfGallows.put(ATTEMT_NUMBER_5, """
                ------
                |  |
                |  O
                | /|\\
                | /
                -
                """);
        imagesOfGallows.put(ATTEMT_NUMBER_6, """
                ------
                |  |
                |  O
                | /|\\
                | / \\
                -
                """);
        return(imagesOfGallows.get(attemptNumber));
    }

    static String inputLetter(Set <String> usedLetters){
        String letter;
        do{

            System.out.print("Введите букву:");
            Scanner scanner = new Scanner(System.in);
            letter = scanner.next();
        }while(usedLetters.contains(letter.toUpperCase()) || !isRussianLetter(letter));
        return letter;
    }

    static boolean isWin(String randomWord, String maskWord){
        if (randomWord.toUpperCase().equals(maskWord)){
            System.out.println("Победа!");
            return true;
        }
        return false;
    }

    static boolean isLose(int numberAttempt){
        if (numberAttempt==MAX_ATTEMTS){
            System.out.println("Вы проиграли!");
            return true;
        }
        return false;
    }

    static void showUsedLetters(Set <String> usedLetters){
        System.out.print("Вы использовали буквы: ");
        for ( String usedLetter:usedLetters){
            System.out.print(usedLetter+" ");
        }
        System.out.println();
    }

    static void startGameIteration(){
        String randomWord = getRandomWord();
        String maskWord = MASK_CHAR.repeat(randomWord.length());
        int numberAttempt = 0;
        Set<String> usedLetters = new LinkedHashSet<>();
        while (numberAttempt<MAX_ATTEMTS){
            System.out.println(maskWord);
            String letter = inputLetter(usedLetters).toLowerCase();
            String newMaskWord = updateMaskWord(letter,maskWord,randomWord);
            if (!maskWord.equals(newMaskWord)){
                maskWord = newMaskWord;
                if (numberAttempt!=0) {
                    System.out.println(getGallowsImage(numberAttempt));
                }
                System.out.println("Верно! Осталось попыток: " + (MAX_ATTEMTS-numberAttempt));
                if (isWin(randomWord,maskWord)) break;
            }else {
                ++numberAttempt;
                System.out.println(getGallowsImage(numberAttempt));
                System.out.println("Неверно! Осталось попыток: " + (MAX_ATTEMTS-numberAttempt));
                if (isLose(numberAttempt)) break;
            }
            usedLetters.add(letter.toUpperCase());
            showUsedLetters(usedLetters);
        }
        System.out.println("Загаданное слово: " + randomWord);
    }

    static boolean isTrueChoice(String choice){
        try{
            Integer.parseInt(choice);
            return true;
        }catch(Exception error){
            return false;
        }
    }

    static void startGame(){
        String choice;
        do {
            System.out.println("Для начала новой игры введите " + START);
            System.out.println("Для завершения игры введите " + QUIT);
            System.out.print("Ваш выбор: ");
            Scanner scanner = new Scanner(System.in);
            choice = scanner.next();

            if (isTrueChoice(choice)) {
                if (choice.equals(START)) {
                    startGameIteration();
                }
                if ( choice.equals(QUIT)){
                    break;
                }
            }else{
                continue;
            }
        } while (choice!=QUIT);
    }


}
