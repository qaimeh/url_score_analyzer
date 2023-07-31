package com.url.score.analyzer;

import com.url.score.analyzer.exceptions.CSVFormatException;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        URLManager urlManager = new URLManager();
        Scanner scanner = new Scanner(System.in);

        System.out.println("URL Social Analyzer System");
        System.out.println("Available commands: ADD, REMOVE, EXPORT, CSV-IMPORT-PATH, EXIT");

        while (true) {
            System.out.print("> ");
            String input = scanner.nextLine();
            String[] parts = input.split("\\s+");

            String command = parts[0].toUpperCase();

            switch (command) {
                case "ADD":
                    if (parts.length == 3) {
                        String url = parts[1];
                        int socialScore = Integer.parseInt(parts[2]);
                        URLData urlData = new URLData(url, socialScore);
                        urlManager.addURL(urlData);
                        System.out.println("URL added successfully.");
                    } else {
                        System.out.println("Invalid command. Usage: ADD <URL> <social_score>");
                    }
                    break;

                case "REMOVE":
                    if (parts.length == 2) {
                        String url = parts[1];
                        urlManager.removeURL(url);
                        System.out.println("URL removed successfully.");
                    } else {
                        System.out.println("Invalid command. Usage: REMOVE <URL>");
                    }
                    break;
                case "CSV-IMPORT-PATH":
                    if (parts.length == 2) {
                        try {
                            urlManager.addURLsFromCSV(parts[1]);
                        } catch (CSVFormatException e) {
                            throw new RuntimeException(e);
                        }
                    }else {
                            System.out.println("Invalid command. Usage: CSV-IMPORT <PATH>");
                        }
                    break;

                case "EXPORT":
                    String statistics = urlManager.exportStatistics();
                    System.out.println(statistics);
                    break;

                case "EXIT":
                    System.out.println("Exiting URL Social Analyzer System.");
                    scanner.close();
                    System.exit(0);
                    break;

                default:
                    System.out.println("Invalid command. Available commands: ADD, REMOVE, EXPORT, EXIT");
            }
        }
    }
}
