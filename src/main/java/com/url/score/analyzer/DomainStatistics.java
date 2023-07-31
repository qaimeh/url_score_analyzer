package com.url.score.analyzer;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents the statistics for a domain.
 */
public class DomainStatistics {
    private int urlCount;
    private int socialScoreSum;
    private List<URLData> urls; // New data structure to store URLs

    /**
     * Initializes a new instance of the DomainStatistics class.
     */
    public DomainStatistics() {
        urlCount = 0;
        socialScoreSum = 0;
        urls = new ArrayList<>();
    }

    /**
     * Gets the count of URLs associated with this domain.
     *
     * @return The URL count.
     */
    public int getUrlCount() {
        return urlCount;
    }

    /**
     * Gets the sum of social scores for URLs associated with this domain.
     *
     * @return The social score sum.
     */
    public int getSocialScoreSum() {
        return socialScoreSum;
    }

    /**
     * Gets the list of URLData objects associated with this domain.
     *
     * @return The list of URLs.
     */
    public List<URLData> getUrls() {
        return urls;
    }

    /**
     * Increments the URL count for this domain.
     */
    public void incrementURLCount() {
        urlCount++;
    }

    /**
     * Decrements the URL count for this domain.
     */
    public void decrementURLCount() {
        urlCount--;
    }

    /**
     * Adds the given social score to the total social score for this domain.
     *
     * @param score The social score to add.
     */
    public void addSocialScore(int score) {
        socialScoreSum += score;
    }

    /**
     * Subtracts the given social score from the total social score for this domain.
     *
     * @param score The social score to subtract.
     */
    public void subtractSocialScore(int score) {
        socialScoreSum -= score;
    }

    /**
     * Adds a URLData object to the list of URLs associated with this domain.
     *
     * @param urlData The URLData object to add.
     */
    public void addURL(URLData urlData) {
        urls.add(urlData);
    }

    /**
     * Removes a URLData object from the list of URLs associated with this domain.
     *
     * @param urlData The URLData object to remove.
     */
    public void removeURL(URLData urlData) {
        urls.remove(urlData);
    }
}
