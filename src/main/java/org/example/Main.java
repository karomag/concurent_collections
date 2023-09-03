package org.example;

import java.util.Random;
import java.util.concurrent.ArrayBlockingQueue;

public class Main {

    public static ArrayBlockingQueue<String> queue1 = new ArrayBlockingQueue<>(100); // очередь по 100 элементов
    public static ArrayBlockingQueue<String> queue2 = new ArrayBlockingQueue<>(100);
    public static ArrayBlockingQueue<String> queue3 = new ArrayBlockingQueue<>(100);
    public static long maxA = 0;
    public static long maxB = 0;
    public static long maxC = 0;
    public static String maxAStr;
    public static String maxBStr;
    public static String maxCStr;

    public static void main(String[] args) throws InterruptedException {

        Thread thread0 = new Thread(() -> {
            for (int i = 0; i < 10_000; i++) { // 10 000 текстов
                try {
                    queue1.put(generateText("abc", 100_000)); // 100 000 символов в строке
                    queue2.put(generateText("abc", 100_000)); // put добавляет, если может, иначе спит
                    queue3.put(generateText("abc", 100_000));
                    Thread.sleep(100);
//                    System.out.println("Queues put() " + queue1);
//                    System.out.println("Queues put() " + queue2);
//                    System.out.println("Queues put() " + queue3);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        });

        Thread thread1 = new Thread(() -> {
            for (int i = 0; i < 100; i++) {
                try {
                    String currentStr = queue1.take();
                    long currentMaxA = currentStr.chars().filter(ch -> ch == 'a').count();
                    if (currentMaxA > maxA) {
                        maxA = currentMaxA;
                        maxAStr = currentStr;
                    }
                    Thread.sleep(150);
//                    System.out.println("Queue1 take()" + queue1);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        });

        Thread thread2 = new Thread(() -> {
            for (int i = 0; i < 100; i++) {
                try {
                    String currentStr = queue2.take();
                    long currentMaxA = currentStr.chars().filter(ch -> ch == 'b').count();
                    if (currentMaxA > maxB) {
                        maxB = currentMaxA;
                        maxBStr = currentStr;
                    }
                    Thread.sleep(150);
//                    System.out.println("Queue2 take()" + queue2);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        });

        Thread thread3 = new Thread(() -> {
            for (int i = 0; i < 100; i++) {
                try {
                    String currentStr = queue3.take();
                    long currentMaxA = currentStr.chars().filter(ch -> ch == 'c').count();
                    if (currentMaxA > maxC) {
                        maxC = currentMaxA;
                        maxCStr = currentStr;
                    }
                    Thread.sleep(150);
//                    System.out.println("Queue3 take()" + queue3);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        });

        thread0.start();
        thread1.start();
        thread2.start();
        thread3.start();

        thread0.join();
        thread3.join();
        thread2.join();
        thread1.join();


        System.out.println("Максимум a " + maxA + ": " + maxAStr);
        System.out.println("************************************************************************");
        System.out.println("Максимум b " + maxB + ": " + maxBStr);
        System.out.println("************************************************************************");
        System.out.println("Максимум c " + maxC + ": " + maxCStr);
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