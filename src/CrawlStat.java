import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class CrawlStat {
    private static CrawlStat instance = null;
    private PrintWriter fetchFile;
    private PrintWriter visitFile;
    private PrintWriter urlsFile;

    private CrawlStat() {
        try {
            fetchFile = new PrintWriter(new FileWriter("fetch_website.csv"));
            visitFile = new PrintWriter(new FileWriter("visit_website.csv"));
            urlsFile = new PrintWriter(new FileWriter("urls_website.csv"));
            fetchFile.println("URL,Status");
            visitFile.println("URL,Size (bytes),OutLinks,Content-Type");
            urlsFile.println("URL,Indicator");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static synchronized CrawlStat getInstance() {
        if (instance == null) {
            instance = new CrawlStat();
        }
        return instance;
    }

    public synchronized void writeFetch(String url, int status) {
        fetchFile.println(url + "," + status);
    }

    public synchronized void writeVisit(String url, int size, int outLinks, String contentType) {
        visitFile.println(url + "," + size + "," + outLinks + "," + contentType);
    }

    public synchronized void writeUrl(String url, String indicator) {
        urlsFile.println(url + "," + indicator);
    }

    public void close() {
        if (fetchFile != null) {
            fetchFile.close();
        }
        if (visitFile != null) {
            visitFile.close();
        }
        if (urlsFile != null) {
            urlsFile.close();
        }
    }
}
