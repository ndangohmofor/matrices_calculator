package org.example;

import java.io.FileReader;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;

public class MainApplication {
    public static void main(String[] args) {
    }

    private static class MatricesReaderProducer extends Thread {
        private Scanner scanner;
        private ThreadSafeQueue queue;

        public MatricesReaderProducer(FileReader reader, ThreadSafeQueue queue){
            this.scanner = new Scanner(reader);
            this.queue = queue;
        }
    }

    private static class ThreadSafeQueue {
        private Queue<MatricesPair> queue = new LinkedList<>();
        private boolean isEmpty = true;
        private boolean isTerminate = false;

        public synchronized void add(MatricesPair matricesPair) {
            queue.add(matricesPair);
            isEmpty = false;
            notify();
        }

        public synchronized MatricesPair remove() {
            while (isEmpty && !isTerminate) {
                try {
                    wait();
                } catch (InterruptedException e) {
                }
            }
            if (queue.size() == 1) {
                isEmpty = true;
            }
            if (queue.size() == 0 && isTerminate) {
                return null;
            }
            System.out.println("queue size: " + queue.size());
            return queue.remove();
        }

        public synchronized void terminate() {
            isTerminate = true;
            notifyAll();
        }
    }

    public static class MatricesPair {
        public float[][] matrix1;
        public float[][] getMatrix2;
    }
}
