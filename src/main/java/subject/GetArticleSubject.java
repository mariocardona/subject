package subject;

import java.io.IOException;
import java.net.URL;
import java.util.Iterator;

import org.javatuples.Pair;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by cardona on 2/18/14.
 */
public interface GetArticleSubject {
    public static final Logger logger =  LoggerFactory.getLogger(GetArticleSubject.class);

    default public Pair<String,URL> get (URL articleUrl) throws IOException {

        try {
            Document doc = Jsoup.connect(articleUrl.toString()).get();
            Elements handle = doc.select("li >h4.card-title.card-acc-handle");
            Iterator iter = handle.iterator();

            String companyName = "N/A";
            String companyWebsite = "N/A";

            while (iter.hasNext()) {
                companyName = ((Element) iter.next()).text();

                String selector = "li.crunchbase-card>h4.card-title.card-acc-handle:contains(" + companyName + ")+div.card-acc-panel strong.key:contains(Website)+ span.value > a:first-child";
                Elements handleHasWebsite = doc.select(selector);

                logger.info("Candidate Company Name: " + companyName);

                if (handleHasWebsite.size() > 0) {
                    logger.info("Candidate Company Website: " + handleHasWebsite.get(0).text());
                    companyWebsite = handleHasWebsite.get(0).text();
                    break;
                }
            }

            if (!companyWebsite.matches("N/A")) {
                return Pair.with(companyName,new URL (companyWebsite));
            } else {
                return Pair.with(companyName, null);
            }
        }
        catch (IOException e) {
            logger.debug("Invalid article URL: " + articleUrl + " or no network connection: " + e.toString());
            return Pair.with("N/A", null);
        }
    }
}
