package repository.dao;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.concurrent.locks.ReentrantLock;

public abstract class LogManager {
    private static final ReentrantLock lock = new ReentrantLock();

     static void logManage(String data) throws IOException {
        try (FileWriter fileWriter = new FileWriter("src/server/log/Log", true)) {
            BufferedWriter bufferedWriter= new BufferedWriter(fileWriter);
            lock.lock();
            try {
                System.out.println("data writing");
                bufferedWriter.write(data + " ");
                bufferedWriter.flush();
            } finally {
                lock.unlock();
            }
        } catch (IOException e) {
            throw new IOException(e);
        }
    }

}
