package com.url.score.analyzer;

public class URLData {
    private String url;
    private int socialScore;

    /**
     * Constructs a new URLData object with the given URL and social score.
     *
     * @param url         The URL to be stored.
     * @param socialScore The social score associated with the URL.
     */
     public URLData(String url, int socialScore) {
        this.url = url;
        this.socialScore = socialScore;
    }

    /**
     * Gets the URL.
     * @return The URL.
     */
    public String getUrl() {
        return url;
    }

    /**
     * Gets the social score associated with the URL.
     * @return The social score.
     */
    public int getSocialScore() {
        return socialScore;
    }
}
