package service;

import java.io.*;

/**
 * Central file-storage utility. No database is required by LibraX.
 * Java serialization is used to persist application state between runs.
 */
public final class FileHandler {
    private FileHandler() { }

    public static <T> void save(String path, T data) throws IOException {
        File file = new File(path);
        File parent = file.getParentFile();
        if (parent != null) parent.mkdirs();

        try (ObjectOutputStream output =
                     new ObjectOutputStream(new FileOutputStream(file))) {
            output.writeObject(data);
        }
    }

    @SuppressWarnings("unchecked")
    public static <T> T load(String path, T fallback) {
        File file = new File(path);
        if (!file.exists()) return fallback;

        try (ObjectInputStream input =
                     new ObjectInputStream(new FileInputStream(file))) {
            return (T) input.readObject();
        } catch (IOException | ClassNotFoundException ex) {
            System.err.println("Warning: unable to load " + path
                    + ". Default data will be used.");
            return fallback;
        }
    }
}
