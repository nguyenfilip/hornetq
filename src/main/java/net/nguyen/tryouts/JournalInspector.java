package net.nguyen.tryouts;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.List;

import org.apache.log4j.BasicConfigurator;
import org.hornetq.api.core.HornetQBuffer;
import org.hornetq.api.core.HornetQBuffers;
import org.hornetq.core.journal.RecordInfo;
import org.hornetq.core.journal.SequentialFileFactory;
import org.hornetq.core.journal.impl.JournalFile;
import org.hornetq.core.journal.impl.JournalImpl;
import org.hornetq.core.journal.impl.JournalReaderCallback;
import org.hornetq.core.journal.impl.NIOSequentialFileFactory;
import org.hornetq.core.server.ServerMessage;
import org.hornetq.core.server.impl.ServerMessageImpl;

import net.nguyen.journal.inspect.AddRecordReader;

public class JournalInspector {
    private static AddRecordReader addRecordHandler=  new AddRecordReader();
    
    public static void main(String args[]) throws Exception {
        BasicConfigurator.configure();
//        exportCpDataJournal("outputjournal.txt");
        
        
    }
    
    private static void exportCpDataJournal(String string) throws Exception {
        FileOutputStream fileOut = new FileOutputStream(new File(string));
        BufferedOutputStream buffOut = new BufferedOutputStream(fileOut);
        PrintStream out = new PrintStream(buffOut);
        exportJournal("cpjournal", "hornetq-data", "hq", 2,
                10485760, out);
        out.close();
    }

    
    private static void exportDataJournal(String string) throws Exception {
        FileOutputStream fileOut = new FileOutputStream(new File(string));
        BufferedOutputStream buffOut = new BufferedOutputStream(fileOut);
        PrintStream out = new PrintStream(buffOut);
        exportJournal("journal", "hornetq-data", "hq", 2,
                10485760, out);
        out.close();
    }

    private static void exportBindingsJournal(String string) throws Exception {
        FileOutputStream fileOut = new FileOutputStream(new File(string));
        BufferedOutputStream buffOut = new BufferedOutputStream(fileOut);
        PrintStream out = new PrintStream(buffOut);
        exportJournal("bindings", "hornetq-bindings", "bindings",
                2, 1048576, out);
        out.close();
    }

    public static void exportJournal(final String directory,
            final String journalPrefix, final String journalSuffix,
            final int minFiles, final int fileSize, final PrintStream out)
                    throws Exception {
        NIOSequentialFileFactory nio = new NIOSequentialFileFactory(directory,
                null);

        JournalImpl journal = new JournalImpl(fileSize, minFiles, 0, 0, nio,
                journalPrefix, journalSuffix, 1);

        List<JournalFile> files = journal.orderFiles();

        for (JournalFile file : files) {
            out.println("#File," + file);

            exportJournalFile(out, nio, file);
        }
    }

    public static void exportJournalFile(final PrintStream out,
            final SequentialFileFactory fileFactory, final JournalFile file)
                    throws Exception {
        JournalImpl.readJournalFile(fileFactory, file,
                new JournalReaderCallback() {

                    public void onReadUpdateRecordTX(final long transactionID,
                            final RecordInfo recordInfo) throws Exception {
                        out.println("operation@UpdateTX,txID@" + transactionID
                                + "," + describeRecord(recordInfo));
                    }

                    public void onReadUpdateRecord(final RecordInfo recordInfo)
                            throws Exception {
                        out.println("operation@Update,"
                                + describeRecord(recordInfo));
                    }

                    public void onReadRollbackRecord(final long transactionID)
                            throws Exception {
                        out.println("operation@Rollback,txID@" + transactionID);
                    }

                    public void onReadPrepareRecord(final long transactionID,
                            final byte[] extraData, final int numberOfRecords)
                                    throws Exception {
                        out.println("operation@Prepare,txID@" + transactionID
                                + ",numberOfRecords@" + numberOfRecords
                              );
                    }

                    public void onReadDeleteRecordTX(final long transactionID,
                            final RecordInfo recordInfo) throws Exception {
                        out.println(
                                "operation@DeleteRecordTX,txID@" + transactionID
                                        + "," + describeRecord(recordInfo));
                    }

                    public void onReadDeleteRecord(final long recordID)
                            throws Exception {
                        out.println("operation@DeleteRecord,id@" + recordID);
                    }

                    public void onReadCommitRecord(final long transactionID,
                            final int numberOfRecords) throws Exception {
                        out.println("operation@Commit,txID@" + transactionID
                                + ",numberOfRecords@" + numberOfRecords);
                    }

                    public void onReadAddRecordTX(final long transactionID,
                            final RecordInfo recordInfo) throws Exception {
                        out.println(
                                "operation@AddRecordTX,txID@" + transactionID
                                        + "," + describeRecord(recordInfo));
                        addRecordHandler.handle(recordInfo);
                    }

                    public void onReadAddRecord(final RecordInfo recordInfo)
                            throws Exception {
                        out.println("operation@AddRecord,"
                                + describeRecord(recordInfo));
                        addRecordHandler.handle(recordInfo);
                    }

                    public void markAsDataFile(final JournalFile file) {
                    }
                });
    }

    private static String describeRecord(final RecordInfo recordInfo) {
        return "id@" + recordInfo.id + ",userRecordType@"
                + recordInfo.userRecordType + ",length@"
                + recordInfo.data.length + ",isUpdate@" + recordInfo.isUpdate
                + ",compactCount@" + recordInfo.compactCount;
    }
    
    
}
