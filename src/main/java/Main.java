import java.util.Arrays;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

public class Main {
    static AtomicInteger countLength3 = new AtomicInteger();
    static AtomicInteger countLength4 = new AtomicInteger();
    static AtomicInteger countLength5 = new AtomicInteger();

    public static void main(String[] args) throws InterruptedException {
        String[] nicknames = generateNickname();

        Thread thread1 = new Thread(() -> {
            for (String nicks : nicknames) {
                char[] nick = nicks.toCharArray();
                boolean asc = true;
                for (int i = 0; i < nick.length/2; ++i) {
                    if (nick[i] != nick[nick.length - i - 1]) {
                        asc = false;
                        break;
                    }
                }
                if (asc) increment(nicks);
            }
        });

        Thread thread2 = new Thread(() -> {
            Arrays.stream(nicknames).forEach(nick -> {
                if (nick.chars().distinct().count() == 1) increment(nick);
            });
        });

        Thread thread3 = new Thread(() -> {
            Arrays.stream(nicknames).forEach(nick -> {
                boolean asc = true;
                for (int i = 1; i < nick.length(); ++i) {
                    if (nick.charAt(i) < nick.charAt(i - 1) || nick.charAt(i) != nick.charAt(i - 1)) {
                        asc = false;
                        break;
                    }
                }
                if (asc) increment(nick);
            });
        });

        thread1.start();
        thread2.start();
        thread3.start();

        System.out.println("Красивых слов с длиной 3: " + countLength3.get() + " шт");
        System.out.println("Красивых слов с длиной 4: " + countLength4.get() + " шт");
        System.out.println("Красивых слов с длиной 5: " + countLength5.get() + " шт");
    }

    public static void increment(String nick) {
        switch (nick.length()) {
            case 3:
                countLength3.incrementAndGet();
                break;
            case 4:
                countLength4.incrementAndGet();
                break;
            case 5:
                countLength5.incrementAndGet();
                break;
        }
    }

    public static String generateText(String letters, int length) {
        Random random = new Random();
        StringBuilder text = new StringBuilder();
        for (int i = 0; i < length; i++) {
            text.append(letters.charAt(random.nextInt(letters.length())));
        }
        return text.toString();
    }

    public static String[] generateNickname() {
        Random random = new Random();
        String[] texts = new String[15];
        for (int i = 0; i < texts.length; i++) {
            texts[i] = generateText("abc", 3 + random.nextInt(3));
        }
        return texts;
    }
}
