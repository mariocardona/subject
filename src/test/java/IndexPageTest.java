import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.*;

import subject.IndexPageSimple;

import com.csvreader.CsvReader;
import org.javatuples.Pair;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * Created by mcar on 2/19/14.
 */
public class IndexPageTest {

    @Test
    public void validate () throws IOException {

        // path to test descriptor and directory where the test data is
        String testFilesSubpath = System.getProperty("user.dir") + "/testdata/IndexPageTestData/";
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

            String htmlFileName = testCaseParams[0];
            String csvFileName = testCaseParams[1];

            String benchmarkHtmlFilePath = testFilesSubpath + htmlFileName;
            String benchmarkCsvFilePath = testFilesSubpath + csvFileName;

            execute(benchmarkHtmlFilePath, benchmarkCsvFilePath);
        }
    }

    private void execute (String htmlFilePath, String csvFilePath) throws IOException {
        // create container for set of expected outputs
        double falsePositiveProbability = 0.01;
        int expectedNumberOfElements = 100;
        BloomFilter<String> bloomFilter = new BloomFilter<String>(falsePositiveProbability, expectedNumberOfElements);

        // add expected outputs to container
        CsvReader descriptor = new CsvReader(csvFilePath, ',', Charset.forName("UTF-8"));

        // read headers but do not validate them
        if(descriptor.readHeaders()) {
            descriptor.getHeaders();
        }

        int count = 0;
        while (descriptor.readRecord()) {
            count++;

            String[] values = descriptor.getValues();
            Assert.assertEquals(descriptor.getColumnCount(), 2, "Number of test case parameters: ");

            String articleTitle = values[0];
            String articleUrl = values[1];

            Pair<String,URL> pair = Pair.with(articleTitle, new URL(articleUrl));
            bloomFilter.add(pair.toString());
            }

        // execute test case
        File benchmarkHtmlFile = new File(htmlFilePath);
        Document doc = Jsoup.parse(benchmarkHtmlFile, "UTF-8");
        List<Pair<String, URL>> articleList = new IndexPageSimple().getArticlesTitleAndUrl(doc);

        // check results
        Assert.assertEquals(count, articleList.size(), "Number of articles returned and in expected set differ: ");

        for (Pair<String,URL> pair : articleList) {
            String actualTitleMsg = "Actual title = " + pair.getValue0();
            String actualUrlMsg = "Actual URL = " + pair.getValue1().toString();

            Assert.assertTrue(bloomFilter.contains(pair.toString()), actualTitleMsg + ", " + actualUrlMsg);
        }
    }
}
