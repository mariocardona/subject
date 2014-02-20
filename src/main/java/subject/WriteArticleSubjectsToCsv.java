package subject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.List;

import com.csvreader.CsvWriter;
import org.javatuples.Pair;
import org.jsoup.nodes.Document;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by mcar on 2/19/14.
 */
public class WriteArticleSubjectsToCsv {
    private static final Logger logger =  LoggerFactory.getLogger(WriteArticleSubjectsToCsv.class);
    private final String filePath;
    private CsvWriter csv;

    public WriteArticleSubjectsToCsv (String filePath) {
        this.filePath = filePath;
    }

    public void write (Document doc) throws IOException {
        List<Pair<String, URL>> articleList;

        try {
             init();

            articleList = new GetArticlesSimple().get(doc);

            for (Pair<String,URL> e: articleList){
                String articleTitle = e.getValue0();
                String articleUrl = e.getValue1().toString();

                Pair<String,URL> a = new GetArticleSubjectSimple().get(e.getValue1());
                String companyName = a.getValue0();
                URL companyURL = a.getValue1();
                String companyWebsite = "N/A";
                if (companyURL != null)
                    companyWebsite = companyURL.toString();

                csv.write(articleTitle);    // "article title"
                csv.write(articleUrl);      // "article url"
                csv.write(companyName);     // "company name"
                csv.write(companyWebsite);  // "company website"
                csv.endRecord();
            }

            if(csv!=null) {
                csv.flush();
                csv.close();
            }
        }
        catch (IOException e) {
            System.out.println("Output file could not be written: " + e.toString());
        }
    }

    private void init () throws IOException {

        File file = new File(filePath);
        if(file.exists()){
            file.delete();
        }

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
}
