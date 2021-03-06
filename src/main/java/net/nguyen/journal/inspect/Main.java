package net.nguyen.journal.inspect;

import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.util.Map;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import com.fasterxml.jackson.databind.ObjectMapper;

public class Main {
    private static AddRecordReader addRecordHandler = new AddRecordReader();

    public static void main(String args[]) throws Exception {

        ApplicationContext context = new AnnotationConfigApplicationContext(
                Config.class);
        context.getBean(JournalInspector.class).inspect();
        CandlepinJournalStats statistics = context
                .getBean(CandlepinJournalStatisticsBuilder.class)
                .getJournalStatistics();
        System.out.println(statistics);
    }

}
