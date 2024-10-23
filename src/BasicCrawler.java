import edu.uci.ics.crawler4j.crawler.Page;
import edu.uci.ics.crawler4j.crawler.WebCrawler;
import edu.uci.ics.crawler4j.parser.HtmlParseData;
import edu.uci.ics.crawler4j.url.WebURL;
import java.util.regex.Pattern;
import java.util.Set;

public class BasicCrawler extends WebCrawler {
    private static final Pattern IMAGE_EXTENSIONS = Pattern.compile(".*\\.(bmp|gif|jpg|png)$");
    @Override
    public boolean shouldVisit(Page referringPage, WebURL url) {
        String href = url.getURL().toLowerCase();

        if (IMAGE_EXTENSIONS.matcher(href).matches()) {
            return false;
        }
        String domain = url.getDomain();
        String subDomain = url.getSubDomain();
        return domain.equals("foxnews.com") && (subDomain.equals("") || subDomain.equals("www"));
    }

    @Override
    protected void handlePageStatusCode(WebURL webUrl, int statusCode, String statusDescription) {
        String url = webUrl.getURL();
        CrawlStat.getInstance().writeFetch(url, statusCode);
    }

    @Override
    public void visit(Page page) {
        String url = page.getWebURL().getURL();
        int size = page.getContentData().length;
        String contentType = page.getContentType();
        if (contentType != null) {
            contentType = contentType.split(";")[0];
        } else {
            contentType = "";
        }

        int outLinks = 0;
        if (page.getParseData() instanceof HtmlParseData) {
            HtmlParseData htmlParseData = (HtmlParseData) page.getParseData();
            Set<WebURL> links = htmlParseData.getOutgoingUrls();
            outLinks = links.size();
            for (WebURL link : links) {
                String linkUrl = link.getURL().toLowerCase();
                String linkDomain = link.getDomain();
                String linkSubDomain = link.getSubDomain();
                String indicator;
                if (linkDomain.equals(Main.CORE_URL) && (linkSubDomain.equals("") || linkSubDomain.equals("www"))) {
                    indicator = "OK";
                } else {
                    indicator = "N_OK";
                }
                CrawlStat.getInstance().writeUrl(linkUrl, indicator);
            }
        }

        System.out.println("Visited URL: " + url + " | Outgoing Links: " + outLinks);
        CrawlStat.getInstance().writeVisit(url, size, outLinks, contentType);
    }
}
