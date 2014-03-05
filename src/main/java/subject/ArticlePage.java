package subject;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Iterator;

import org.javatuples.Pair;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by cardona on 2/18/14.
 */
public interface ArticlePage {
    public static final Logger logger =  LoggerFactory.getLogger(ArticlePage.class);

    default public Pair<String,URL> getSubjectNameAndUrl(Document doc) throws IllegalArgumentException {

        if (doc == null)
            throw new IllegalArgumentException();

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

        if (!companyWebsite.matches("N/A"))
            try {
                URL companyUrl = new URL(companyWebsite);
                return Pair.with(companyName,companyUrl);
            }
            catch (MalformedURLException e) {
                return Pair.with(companyName, null);
            }
        else
            return Pair.with(companyName, null);
    }
}
