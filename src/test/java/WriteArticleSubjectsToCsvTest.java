import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.security.NoSuchAlgorithmException;

import com.csvreader.CsvReader;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import org.testng.annotations.Test;
import org.testng.Assert;

import subject.WriteArticleSubjectsToCsv;


/**
 * Created by mcar on 2/19/14.
 */
public class WriteArticleSubjectsToCsvTest {

    @Test
    public void validate () throws IOException, NoSuchAlgorithmException {
        // path to the csv output by WriteArticleSubjectsToCsv.write()
        String outputCsvFilePath = System.getProperty("user.dir") + "\\subject.csv";

        // path to test descriptor and directory where the test data is
        String testFilesSubpath = System.getProperty("user.dir") + "\\testdata\\WriteArticleSubjectsToCsvTestData\\";
        String descriptorFilePath = testFilesSubpath + "descriptor.csv";

        // read descriptor & execute tests
        CsvReader descriptor = new CsvReader(descriptorFilePath, ',',Charset.forName("UTF-8"));

        // read headers but do not validate them
        if(descriptor.readHeaders()) {
            descriptor.getHeaders();
        }

        // execute tests
        while(descriptor.readRecord()) {
            // precondition: there must be one input and one expected output
            Assert.assertEquals(descriptor.getColumnCount(), 2, "Number of test case parameters:");

            // execute
            String[] testCaseParams = descriptor.getValues();
            String benchmarkHtmlFilePath = testFilesSubpath + testCaseParams[0];
            String benchmarkCsvFilePath = testFilesSubpath + testCaseParams[1];

            // create output csv from benchmark HTML file
            File benchmarkHtmlFile = new File(benchmarkHtmlFilePath);
            Document doc = Jsoup.parse(benchmarkHtmlFile,"UTF-8");
            WriteArticleSubjectsToCsv srvc = new WriteArticleSubjectsToCsv(outputCsvFilePath);
            srvc.write(doc);

            try {
            // validate that the output and benchmark csvs are identical
            String benchmarkCsvFileSha1 = Utils.sha1(benchmarkCsvFilePath);
            String outputCsvFileSha1 = Utils.sha1(outputCsvFilePath);

            String expectedSha1 = "Expected Sha1 = " + benchmarkCsvFileSha1;

            String actualSha1 = "Actual Sha1 = " + outputCsvFileSha1;
            Assert.assertEquals(benchmarkCsvFileSha1.compareTo(outputCsvFileSha1), 0, expectedSha1 + ", " + actualSha1);
            } catch (IOException e) {
                Assert.assertTrue(false, "Class did not create output file " + outputCsvFilePath);
            }
        }
    }
}
