package net.nguyen.journal.inspect;

import org.apache.commons.io.FileUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.io.File;

public class AddRecordExporter {
    private static AddRecordReader addRecordHandler = new AddRecordReader();

    public static void main(String args[]) throws Exception {
        Long messageToExport=36507419216l;
        String outputPath = "/home/fnguyen/candlepin/hornet/lutz/message.json";
        ApplicationContext context = new AnnotationConfigApplicationContext(
                Config.class);
        LargeMessageReader lmr = context.getBean(LargeMessageReader.class);
        
        FileUtils.writeStringToFile(new File(outputPath),lmr.readJsonInsideFile(messageToExport));
    }

}
