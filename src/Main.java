import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class Main {
    static String randomWord(){
        BufferedReader reader = null;
        Random rand = new Random();
        int count = 0;
        int numberInGlossary = rand.nextInt(51302);
        String word = "";
        try {
            reader = new BufferedReader(new FileReader("russian_nouns.txt"));
            String line;
            while ((line = reader.readLine()) != null) {
                if (numberInGlossary == count){
                    word = line;
                    break;
                }
                count++;
            }
        } catch (IOException e) {
            System.err.println("Ошибка чтения файла: " + e.getMessage());
        } finally {
            try {
                if (reader != null) {
                    reader.close();
                }
            } catch (IOException e) {
                System.err.println("Ошибка закрытия файла: " + e.getMessage());
            }
        }
        return word;
    }

    static String replacement(String letter, String  hiddenWord, String word){
        if (word.contains(letter)){
            for (int i = 0; i<word.length();i++){
                if (word.charAt(i)==letter.charAt(0)){
                    hiddenWord = hiddenWord.substring(0,i) + letter.toUpperCase() + hiddenWord.substring(i+1);
                }
            }
        }
        return hiddenWord;
    }

    static  boolean isRussian(String letter){
        String russianLetters = "абвгдеёжзийклмнопрстуфхцчшщъыьэюяАБВГДЕЁЖЗИЙКЛМНОПРСТУФХЦЧШЩЪЫЬЭЮЯ";
        return (russianLetters.contains(letter));
    }

    static String gallows(int attempt){
        Map<Integer,String> attempts = new HashMap<>();
        attempts.put(1,"------\n" +
                "|  |\n" +
                "|  O\n" +
                "|\n" +
                "|\n" +
                "-");
        attempts.put(2,"------\n" +
                "|  |\n" +
                "|  O\n" +
                "|  |\n" +
                "|\n" +
                "-");
        attempts.put(3,"------\n" +
                "|  |\n" +
                "|  O\n" +
                "| /|\n" +
                "|\n" +
                "-");
        attempts.put(4,"------\n" +
                "|  |\n" +
                "|  O\n" +
                "| /|\\\n" +
                "|\n" +
                "-");
        attempts.put(5,"------\n" +
                "|  |\n" +
                "|  O\n" +
                "| /|\\\n" +
                "| /\n" +
                "-");
        attempts.put(6,"------\n" +
                "|  |\n" +
                "|  O\n" +
                "| /|\\\n" +
                "| / \\\n" +
                "-");
        return(attempts.get(attempt));
    }

    static String makeHiddenWord(String word){
        String hiddenWord="";
        for (int i=0;i<word.length();i++){
            hiddenWord+='*';
        }
        return hiddenWord;
    }

    static void game(){
        System.out.println("51301 слово");

        String word = randomWord();
        String hiddenWord = makeHiddenWord(word);
        int attempt = 0;
        Set<String> usedLetters = new LinkedHashSet<>();

        while (attempt<6){
            System.out.println(hiddenWord);
            String letter;
            do{

                System.out.print("Введите букву:");
                Scanner scanner = new Scanner(System.in);
                letter = scanner.next();
            }while(usedLetters.contains(letter.toUpperCase()) || !isRussian(letter));


            letter = letter.toLowerCase();
            String replace = replacement(letter,hiddenWord,word);
            if (!hiddenWord.equals(replace)){
                hiddenWord = replacement(letter,hiddenWord,word);
                System.out.println("Верно! Осталось попыток: " + (6-attempt));
                if (word.toUpperCase().equals(hiddenWord)){
                    System.out.println("Победа!");
                    break;
                }
            }else {
                attempt+=1;
                System.out.println(gallows(attempt));
                System.out.println("Неверно! Осталось попыток: " + (6-attempt));
                if (attempt==6){
                    System.out.println("Вы проиграли!");
                    break;
                }
            }

            usedLetters.add(letter.toUpperCase());
            System.out.print("Вы использовали буквы: ");
            for ( String usedLetter:usedLetters){
                System.out.print(usedLetter+" ");
            }
            System.out.println();
        }
        System.out.println("Загаданное слово: " + word);
    }


    public static void main(String[] args) {
        int choice=0;
        do {
            try {
                System.out.println("Для начала новой игры введите 1");
                System.out.println("Для завершения игры введите 2");
                System.out.print("Ваш выбор: ");
                Scanner sc = new Scanner(System.in);
                choice = sc.nextInt();
                switch (choice) {
                    case 1: {
                        game();
                        break;
                    }
                    case 2: {
                        break;
                    }
                    default:{
                        System.out.println("Неверный выбор, введите ещё раз");
                    }
                }
            }catch (Exception ex){
                System.out.println("Неверный выбор, введите ещё раз");
            }
        } while (choice != 2);
    }
}
