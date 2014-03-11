package subject;

import java.net.MalformedURLException;
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
public interface IndexPage {
    public static final Logger logger =  LoggerFactory.getLogger(IndexPage.class);

    /**
    * @param doc HTML document
    * @throws IllegalArgumentException if argument is null
    */
    default public List<Pair<String,URL>> getArticlesTitleAndUrl(Document doc) {
        final List<Pair<String,URL>> result = Lists.newLinkedList();

        if (doc == null)
            throw new IllegalArgumentException();

        Elements urls = doc.select("h2.post-title > a");
        for (Element url: urls){
            try {
                URL articleUrl = new URL (url.attr("href"));
                result.add(Pair.with(url.text(), articleUrl));
            } catch (MalformedURLException e) {
                ;
            }
        }
        return result;
    }
}
