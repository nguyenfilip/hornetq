package net.nguyen.journal.inspect;

import java.nio.file.FileSystems;
import java.nio.file.Path;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class Main {
    private static AddRecordReader addRecordHandler = new AddRecordReader();

    public static void main(String args[]) throws Exception {

        ApplicationContext context = new AnnotationConfigApplicationContext(
                Config.class);
        Path dataJournalDirPath = FileSystems.getDefault().getPath("cpjournal");
        context.getBean(JournalInspector.class)
                .inspectCpDataJournal(dataJournalDirPath.toFile());
        CandlepinJournalStats statistics = context
                .getBean(CandlepinJournalStatisticsBuilder.class)
                .getJournalStatistics();
        System.out.println(statistics);
    }

}
