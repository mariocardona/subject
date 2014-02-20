package subject;

import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by mcar on 2/17/14.
 */

public class Subject {
    private static final Logger logger =  LoggerFactory.getLogger(Subject.class);
    private static String filePath = System.getProperty("user.dir") + "\\subject.csv";
    private static String techcrunchPage = "http://www.techcrunch.com/";

    public static void main(String... args) throws IOException  {
        try {
        Document doc = Jsoup.connect(techcrunchPage).get();
        WriteArticleSubjectsToCsv srvc = new WriteArticleSubjectsToCsv(filePath);
        srvc.write(doc);
        System.out.println("Output file is " + filePath);
        }
        catch (IOException e) {
            System.out.println("Can't access techcrunch.com: " + e.toString());
            logger.debug("Can't access techcrunch.com: " + e.toString());
        }
    }
}

