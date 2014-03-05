package subject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.List;

import com.csvreader.CsvWriter;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.javatuples.Pair;


/**
 * Created by mcar on 2/17/14.
 */

public class Subject {
    private static final Logger logger =  LoggerFactory.getLogger(Subject.class);
    private static String techcrunchPage = "http://www.techcrunch.com/";
    private static String filePath = System.getProperty("user.dir") + "/subject.csv";
    private static CsvWriter csv;

    public static void main(String... args) throws IOException  {

        boolean isSuccess = true;

        try {
            initCsv();

            Document newsPage = Jsoup.connect(techcrunchPage).get();
            List<Pair<String, URL>> articlesTitleAndUrl = new IndexPageSimple().getArticlesTitleAndUrl(newsPage);


            for (Pair<String,URL> a: articlesTitleAndUrl) {
               URL articleUrl = a.getValue1();
               Document articlePage = Jsoup.connect(articleUrl.toString()).get();
               Pair<String,URL> s = new ArticlePageSimple().getSubjectNameAndUrl(articlePage);
               writeToCsv(a.getValue0(), a.getValue1(), s.getValue0(), s.getValue1());
               }

        } catch (IllegalArgumentException | IOException e) {
            System.out.println("Error: " + e.getMessage());
            isSuccess = false;

        } finally {
            closeCsv();
            if (isSuccess)
                System.out.println("Output file is: " + filePath);
            else {
                deleteCsv();
                System.out.println("Output file not created.");
            }
        }

    }

    private static void initCsv () throws IOException {
        deleteCsv();

        OutputStream os = new FileOutputStream(filePath);
        // UTF-8 BOM
        os.write(239);
        os.write(187);
        os.write(191);
        csv =  new CsvWriter(os,',', Charset.forName("UTF-8"));
        csv.setRecordDelimiter('\n');
        csv.setForceQualifier(true);
        // Headings
        csv.write("article title");
        csv.write("article url");
        csv.write("company name");
        csv.write("company website");
        csv.endRecord();
    }

    private static void closeCsv () throws IOException {
        if(csv!=null) {
            csv.flush();
            csv.close();
        }
    }

    private static void deleteCsv() throws IOException {
        File file = new File(filePath);
        if(file.exists()){
            file.delete();
        }
    }

    private static void writeToCsv (String aTitle,URL aUrl, String sName, URL sUrl) throws IOException {

        if (aTitle != null)
            csv.write(aTitle);
        else
            csv.write("N/A");


        if (aUrl != null)
            csv.write(aUrl.toString());
        else
            csv.write("N/A");


        if (sName != null)
            csv.write(sName);
        else
            csv.write("N/A");


        if (sUrl != null)
            csv.write(sUrl.toString());
        else
            csv.write("N/A");

        csv.endRecord();
    }
}

