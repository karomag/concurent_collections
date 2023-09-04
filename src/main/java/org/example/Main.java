package org.example;

import java.util.Random;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.atomic.AtomicLong;

public class Main {

    public static ArrayBlockingQueue<String> queue1 = new ArrayBlockingQueue<>(100); // очередь по 100 элементов
    public static ArrayBlockingQueue<String> queue2 = new ArrayBlockingQueue<>(100);
    public static ArrayBlockingQueue<String> queue3 = new ArrayBlockingQueue<>(100);
    public static Thread textsThread;
    public static void main(String[] args) throws InterruptedException {
        textsThread = new Thread(() -> {
            for (int i = 0; i < 10_000; i++) { // 10 000 текстов
                try {
                    queue1.put(generateText("abc", 100_000)); // 100 000 символов в строке
                    queue2.put(generateText("abc", 100_000)); // put добавляет, если может, иначе спит
                    queue3.put(generateText("abc", 100_000));
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        });
        textsThread.start();

        Thread threadA = getThread(queue1, 'a');
        Thread threadB = getThread(queue2, 'b');
        Thread threadC = getThread(queue3, 'c');

        threadA.start();
        threadB.start();
        threadC.start();

        threadA.join();
        threadB.join();
        threadC.join();
    }

    private static Thread getThread(ArrayBlockingQueue<String> queue, char ch) {
        AtomicLong maxCh = new AtomicLong();
        return new Thread(() -> {
            while (textsThread.isAlive()) {
                try {
                    String currentStr = queue.take();
                    long currentMaxA = currentStr.chars().filter(x -> x == ch).count();
                    if (currentMaxA > maxCh.get()) {
                        maxCh.set(currentMaxA);
                    }
                    Thread.sleep(150);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            };
            System.out.println("Максимум " + ch + ": " + maxCh);
        });
    }

    public static String generateText(String letters, int length) {
        Random random = new Random();
        StringBuilder text = new StringBuilder();
        for (int i = 0; i < length; i++) {
            text.append(letters.charAt(random.nextInt(letters.length())));
        }
        return text.toString();
    }

}