package com.url.score.analyzer;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class URLManagerTest {
    private URLManager urlManager;

    @BeforeEach
    public void setup() {
        urlManager = new URLManager();
    }

    @Test
    public void testAddURL() {
        URLData urlData1 = new URLData("https://www.rte.ie/news/ulster/2018/1004/1000952-moanghan-mine/", 30);
        URLData urlData2 = new URLData("http://www.rte.ie/news/politics/2018/1004/1001034-cso/", 20);
        URLData urlData3 = new URLData("https://www.bbc.com/news/world-europe-58850973", 10);

        urlManager.addURL(urlData1);
        urlManager.addURL(urlData2);
        urlManager.addURL(urlData3);

        String expectedOutput = "domain;urls;social_score\n" +
                "rte.ie;2;50\n" +
                "bbc.com;1;10\n";
        assertEquals(expectedOutput, urlManager.exportStatistics());
    }

    @Test
    public void testRemoveURL() {
        URLData urlData1 = new URLData("https://www.rte.ie/news/ulster/2018/1004/1000952-moanghan-mine/", 30);
        URLData urlData2 = new URLData("http://www.rte.ie/news/politics/2018/1004/1001034-cso/", 20);
        URLData urlData3 = new URLData("https://www.bbc.com/news/world-europe-58850973", 10);

        urlManager.addURL(urlData1);
        urlManager.addURL(urlData2);
        urlManager.addURL(urlData3);

        // Remove one of the URLs
        urlManager.removeURL("https://www.rte.ie/news/ulster/2018/1004/1000952-moanghan-mine/");

        String expectedOutput = "domain;urls;social_score\n" +
                "rte.ie;1;20\n" +
                "bbc.com;1;10\n";
        assertEquals(expectedOutput, urlManager.exportStatistics());
    }
}
