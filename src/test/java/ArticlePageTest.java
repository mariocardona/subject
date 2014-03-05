
import java.io.IOException;
import java.net.URL;
import java.nio.charset.Charset;

import com.csvreader.CsvReader;
import org.javatuples.Pair;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import org.testng.Assert;
import org.testng.annotations.Test;

import subject.ArticlePageSimple;

/**
 * Created by mcar on 2/19/14.
 */
public class ArticlePageTest {
    @Test
    public void validate () throws IOException {

        // path to test descriptor and directory where the test data is
        String testFilesSubpath = System.getProperty("user.dir") + "\\testdata\\ArticlePageTestData\\";
        String descriptorFilePath = testFilesSubpath + "descriptor.csv";

        // read descriptor & execute tests
        CsvReader descriptor = new CsvReader(descriptorFilePath, ',', Charset.forName("UTF-8"));

        // read headers but do not validate them
        if(descriptor.readHeaders()) {
            descriptor.getHeaders();
        }

        // execute test case
        while (descriptor.readRecord()) {
            String[] testCaseParams = descriptor.getValues();
            Assert.assertEquals(descriptor.getColumnCount(), 2, "Number of test case file parameters: ");

            String csvFileName = testCaseParams[0];
            String benchmarkCsvFilePath = testFilesSubpath + csvFileName;

            execute(benchmarkCsvFilePath);
        }
    }

    private void execute (String csvFilePath) throws IOException {
        CsvReader descriptor = new CsvReader(csvFilePath, ',', Charset.forName("UTF-8"));

        // read headers but do not validate them
        if(descriptor.readHeaders()) {
            descriptor.getHeaders();
        }

        while (descriptor.readRecord()) {
            String[] values = descriptor.getValues();
            Assert.assertEquals(descriptor.getColumnCount(), 3, "Number of test case parameters: ");

            String articleUrl = values[0];
            String expectedCompanyName = values[1];
            String expectedCompanyUrl = values[2];

            Document articlePage = Jsoup.connect(articleUrl).get();
            Pair<String,URL> actual = new ArticlePageSimple().getSubjectNameAndUrl(articlePage);
            String actualCompanyName = (actual.getValue0() != null) ? actual.getValue0() : "N/A";
            String actualCompanyUrl = (actual.getValue1() != null) ? actual.getValue1().toString() : "N/A";

            Assert.assertEquals(actualCompanyName.compareTo(expectedCompanyName),0, "Actual: "
                    + actualCompanyName + ", " + "Expected: " + expectedCompanyName);

            Assert.assertEquals(actualCompanyUrl.compareTo(expectedCompanyUrl),0, "Actual: "
                    + actualCompanyUrl + ", " + "Expected: " + expectedCompanyUrl);
        }
    }
}

