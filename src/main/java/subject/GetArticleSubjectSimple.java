package subject;

import org.javatuples.Pair;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URL;


/**
 * Created by mcar on 2/18/14.
 */
public class GetArticleSubjectSimple implements GetArticleSubject {
    public static final Logger logger =  LoggerFactory.getLogger(GetArticleSubjectSimple.class);
    private URL articleUrl;
    private String startName;
    private String revisedName;
    private URL startUrl;
    private URL revisedUrl;

    public GetArticleSubjectSimple() {
    }

    public Pair<String,URL> get (URL articleUrl) throws IOException {
        /*
        Save the article's URL for looking up the companyName in the body
        of the article in the future. See TODO(s) below
         */
        this.articleUrl = articleUrl;
        Pair<String, URL> start = GetArticleSubject.super.get(articleUrl);

        startName = start.getValue0();
        startUrl = start.getValue1();
        return findCompanyNameIfMissing().findCompanyUrlIfMissing().result();
    }

    private GetArticleSubjectSimple findCompanyNameIfMissing () throws IOException {
        if (startName.matches("N/A")) {
            /*
            TODO: on-top-of-my head(this class is for the simple solution): retrieve the text of the article,
            put it through a summarizing algorithm, extract the nouns that have the first letter capitalized
            and store them in a hash table with the key for a noun set to the noun's relative position
            (to other nouns) in the text. Then iterate over the entries in the table, using
            findCompanyUrlIfMissing() below to see whether they have a URL tied to them. Pick up the first name
            for which findCompanyUrlIfMissing() returns a URL
            */
            int dummyPlaceholder = 0;
        }
        revisedName = startName;
        return this;
    }

    private GetArticleSubjectSimple findCompanyUrlIfMissing () throws IOException {
        if ((! revisedName.matches("N/A")) && startUrl == null) {
            /*
            To-do: (need a developer api key and custom search engine id)
            query google, setting the query to return just one item, and regex the
            inputName vs the returned URL. The code would be something like this:
            String selector = "http://www.google.com/search?q=" + inputName +"&start=0&num=1";
            URL myUrl = new URL (selector);
            Document doc = Jsoup.connect(myUrl.toString()).get();
            Elements snippet = doc.select("h3.r > a[href]");
            Iterator iter = snippet.iterator();
            while (iter.hasNext()) {
            extract url from href attribute
            }
            for now we'll leave the url null
            */

            int dummyPlaceholder = 0;
        }

        revisedUrl = startUrl;
        return this;
    }

    private Pair<String, URL> result () {
        return Pair.with(revisedName, revisedUrl);
    }
}
