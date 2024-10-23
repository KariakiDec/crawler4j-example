import edu.uci.ics.crawler4j.crawler.CrawlConfig;
import edu.uci.ics.crawler4j.crawler.CrawlController;
import edu.uci.ics.crawler4j.fetcher.PageFetcher;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtConfig;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtServer;

public class Main {

    public final static String SEED_URL = "https://www.foxnews.com/";
    public final static String CORE_URL = "foxnews.com";

    public static void main(String[] args) throws Exception {
        CrawlConfig config = new CrawlConfig();
        config.setCrawlStorageFolder("results");
        config.setPolitenessDelay(500);
        config.setMaxDepthOfCrawling(16);
        config.setMaxPagesToFetch(20000);
        config.setIncludeBinaryContentInCrawling(false);
        PageFetcher pageFetcher = new PageFetcher(config);
        RobotstxtConfig robotstxtConfig = new RobotstxtConfig();
        robotstxtConfig.setEnabled(true); 
        RobotstxtServer robotstxtServer = new RobotstxtServer(robotstxtConfig, pageFetcher);
        CrawlController controller = new CrawlController(config, pageFetcher, robotstxtServer);
        controller.addSeed(SEED_URL);
        int numberOfCrawlers = 20;
        controller.start(BasicCrawler.class, numberOfCrawlers);
        CrawlStat.getInstance().close();
    }
}
