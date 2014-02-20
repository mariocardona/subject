package subject;

import java.io.IOException;
import java.net.URL;
import java.util.List;

import com.google.common.collect.Lists;
import org.javatuples.Pair;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by cardona on 2/18/14.
 */
public interface GetArticles {
    public static final Logger logger =  LoggerFactory.getLogger(GetArticles.class);

    default public List<Pair<String,URL>> get(Document doc) throws IOException {
        List<Pair<String,URL>> articles = Lists.newLinkedList();

        if (doc == null) {
            logger.debug("Argument doc is null. Articles cannot be retrieved.");
            System.out.println("Articles could not be retrieved because of a program error.");
        }
        else {
            Elements urls = doc.select("h2.post-title > a");
            for (Element url: urls){
                articles.add(Pair.with(url.text(), new URL (url.attr("href"))));
            }
        }
        return articles;
    }
}
