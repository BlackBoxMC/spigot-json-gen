package net.ioixd.spigotjsongen;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.CharBuffer;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class WebScraper {
    Pattern patternGenerics = Pattern.compile("(<|&lt;)([A-Za-z,\s]*?)(>|&gt;)");
    Pattern nonAlphabet = Pattern.compile("[^a-zA-Z\\d\\s:]");

    WebScraper() {
        File file = new File(".javadocCache");
        file.mkdir();
    }

    public Document getUrl(String url) throws IOException {
        String urlFilename = nonAlphabet.matcher(url).replaceAll("");
        File file = new File(".javadocCache" + File.separator + urlFilename + ".txt");
        if (file.exists()) {
            return Jsoup.parse(file);
        } else {
            Document doc = Jsoup.connect(url).get();
            FileWriter fileWriter = new FileWriter(".javadocCache" + File.separator + urlFilename + ".txt");
            fileWriter.write(doc.html());
            fileWriter.close();
            return doc;
        }

    }

    public String[] getGenerics(String url) throws IOException {
        Document doc = this.getUrl(url);
        // We have to scrape the title for the generics instead of where we should
        // because some classes "inherit" their generics and those generics aren't
        // listed there.
        Elements title = doc.select(".title");

        Matcher m = patternGenerics.matcher(title.text());
        if (m.find()) {
            String[] fuck = m.group(2).replace("<", "").replace(">", "").split(",");
            System.out.println(url + ": " + title.text());
            System.out.println(url + ": " + Arrays.toString(fuck));
            return fuck;
        } else {
            return null;
        }

    }
}
