package com.url.score.analyzer;

import com.url.score.analyzer.exceptions.CSVFormatException;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.url.score.analyzer.exceptions.URLManagerException;
import com.url.score.analyzer.exceptions.URLNotFoundException;


/**
 * Manages URLs and their associated statistics.
 */
public class URLManager {
    private Map<String, DomainStatistics> domainStatisticsMap;

    /**
     * Initializes a new instance of the URLManager class.
     */
    public URLManager() {
        domainStatisticsMap = new HashMap<>();
    }

    /**
     * Reads URLs and their associated social scores from a CSV file and adds them to the URLManager.
     *
     * @param csvFilePath The file path of the CSV file to be read.
     * @throws CSVFormatException If the CSV file has an invalid format (e.g., missing data or invalid social scores).
     * @throws URLManagerException If there is an error reading the CSV file or managing URLs.
     */
    public void addURLsFromCSV(String csvFilePath) throws CSVFormatException, URLManagerException {
        try (BufferedReader br = new BufferedReader(new FileReader(csvFilePath))) {
            String line;

            // Skip the header row
            br.readLine();

            // Loop through each line in the CSV file
            while ((line = br.readLine()) != null) {
                // Split the line by commas to get the data
                String[] data = line.split(",");

                // Check if the data contains both URL and social score
                if (data.length == 2) {
                    String url = data[0].trim();
                    String scoreString = data[1].trim();

                    // Try to parse the social score to an integer
                    try {
                        int socialScore = Integer.parseInt(scoreString);

                        // Add the URL and its social score to the URLManager
                        addURL(new URLData(url, socialScore));
                    } catch (NumberFormatException e) {
                        // If the social score cannot be parsed to an integer, throw a custom exception
                        throw new CSVFormatException("Invalid social score in CSV: " + scoreString);
                    }
                } else {
                    // If the data does not contain both URL and social score, throw a custom exception
                    throw new CSVFormatException("Invalid CSV format. Each row should contain a URL and its social score separated by a comma.");
                }
            }
        } catch (IOException e) {
            // If there is an error reading the CSV file, handle it and throw a URLManagerException
            throw new URLManagerException("Error reading CSV file: " + e.getMessage());
        }
    }

    /**
     * Adds a URL to the URLManager and updates its statistics.
     *
     * @param urlData The URLData object representing the URL and its social score.
     */
    public void addURL(URLData urlData) {
        String domain = getDomain(urlData.getUrl());

        // Get or create the DomainStatistics object for the domain
        DomainStatistics domainStatistics = domainStatisticsMap.getOrDefault(domain, new DomainStatistics());

        // Update the domain statistics with the new URLData
        domainStatistics.incrementURLCount();
        domainStatistics.addSocialScore(urlData.getSocialScore());
        domainStatistics.addURL(urlData);

        // Put the updated DomainStatistics object back into the map
        domainStatisticsMap.put(domain, domainStatistics);
    }

    /**
     * Removes a URL from the URLManager and updates its statistics.
     *
     * @param url The URL to be removed.
     * @throws URLNotFoundException If the URL is not found in the URLManager.
     */
    public void removeURL(String url) throws URLNotFoundException {
        String domainToRemove = getDomain(url);

        if (domainStatisticsMap.containsKey(domainToRemove)) {
            DomainStatistics domainStatistics = domainStatisticsMap.get(domainToRemove);
            URLData urlDataToRemove = null;

            // Find the URLData associated with the URL being removed (ignoring the social score)
            for (URLData urlData : domainStatistics.getUrls()) {
                if (urlData.getUrl().equals(url)) {
                    urlDataToRemove = urlData;
                    break;
                }
            }

            if (urlDataToRemove != null) {
                // Subtract the social score of the URL being removed from the domain's social score
                int socialScoreToRemove = urlDataToRemove.getSocialScore();
                domainStatistics.subtractSocialScore(socialScoreToRemove);

                // Remove the URL from the domain statistics
                domainStatistics.removeURL(urlDataToRemove);
                domainStatistics.decrementURLCount();

                // If no URLs are left for this domain, remove it from the map
                if (domainStatistics.getUrlCount() == 0) {
                    domainStatisticsMap.remove(domainToRemove);
                }
            } else {
                // If the URL is not found in the URLManager, throw a custom exception
                throw new URLNotFoundException("URL not found: " + url);
            }
        } else {
            // If the domain is not found in the URLManager, throw a custom exception
            throw new URLNotFoundException("Domain not found for URL: " + url);
        }
    }

    /**
     * Exports the statistics for all domains in the URLManager.
     *
     * @return A formatted string containing the domain statistics.
     */
    public String exportStatistics() {
        StringBuilder sb = new StringBuilder();
        sb.append("domain;urls;social_score\n");

        // Sort the domain statistics based on social score in descending order
        List<Map.Entry<String, DomainStatistics>> sortedDomainStatistics = new ArrayList<>(domainStatisticsMap.entrySet());
        sortedDomainStatistics.sort((entry1, entry2) -> Integer.compare(entry2.getValue().getSocialScoreSum(), entry1.getValue().getSocialScoreSum()));

        // Append the sorted domain statistics to the result string
        for (Map.Entry<String, DomainStatistics> entry : sortedDomainStatistics) {
            String domain = entry.getKey();
            DomainStatistics domainStatistics = entry.getValue();
            sb.append(domain).append(";").append(domainStatistics.getUrlCount()).append(";").append(domainStatistics.getSocialScoreSum()).append("\n");
        }

        return sb.toString();
    }

    /**
     * Extracts the domain from a given URL using regular expressions.
     *
     * @param url The URL from which to extract the domain.
     * @return The domain extracted from the URL.
     */
    private String getDomain(String url) {
        // Extract the domain from the URL using regular expressions
        String domain = "";

        Pattern pattern = Pattern.compile("^(?:https?:\\/\\/)?(?:[^@\\n]+@)?(?:www\\.)?([^:\\/\\n?]+)");
        Matcher matcher = pattern.matcher(url);

        if (matcher.find()) {
            domain = matcher.group(1);
        }

        return domain;
    }

}
