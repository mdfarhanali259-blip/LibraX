package service;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Demonstrates multithreading by generating a report independently
 * from the command-line menu thread.
 */
public class ReportService extends Thread {
    private final Library library;
    private final boolean exportToFile;

    public ReportService(Library library) {
        this(library, false);
    }

    public ReportService(Library library, boolean exportToFile) {
        this.library = library;
        this.exportToFile = exportToFile;
        setName("Library-Report-Thread");
    }

    @Override
    public void run() {
        System.out.println("\n[Report thread] Preparing analytics...");
        try {
            Thread.sleep(300);
            String report = library.buildReport();
            System.out.println(report);

            if (exportToFile) {
                File directory = new File("data");
                directory.mkdirs();
                try (FileWriter writer = new FileWriter(
                        new File(directory, "library-report.txt"))) {
                    writer.write(report);
                }
                System.out.println("Report exported to data/library-report.txt");
            }
        } catch (InterruptedException ex) {
            Thread.currentThread().interrupt();
            System.out.println("Report generation interrupted.");
        } catch (IOException ex) {
            System.out.println("Could not export report: " + ex.getMessage());
        }
    }
}
