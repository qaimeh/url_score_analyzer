# URL Score Analyzer
The URL Analyzer is a Java application that allows you to manage URLs and their associated statistics. It provides functionalities to add URLs, remove URLs, and export statistics for all domains.

## Setup and Installation

### Prerequisites

Before running the application, make sure you have the following installed on your system:

- Java Development Kit (JDK) 8 or higher
- Apache Maven
- IntelliJ IDEA (optional, for development)

### Build the Project using Maven

To build the project and package it into a JAR file, cd to home directory and use the following maven command:
- `mvn clean package`

This will create an executable JAR file named `url-score-analyzer-1.0-SNAPSHOT.jar` in the ?target? directory

## Running the Application

## From Command Line

After building the project, you can run the application from the command line using the following command:
- `java -jar target/url-score-analyzer-1.0-SNAPSHOT.jar`

### From IntelliJ IDEA

If you prefer to run the application from IntelliJ IDEA, follow these steps:

1. Open IntelliJ IDEA and import the project by selecting the `pom.xml` file.

1. Build the project by selecting "Build" > "Build Project" from the top menu.

1. Find the main class `com.newswhip.analyzer.Main` and right-click on it.

1. Choose "Run 'Main.main()'".

## Managing URLs

Once the application is running, either using jar or from Intellij, you can use the following commands to manage URLs:

- `ADD <url> <socialScore>`: Add a new URL with the specified social score.
- `EXPORT`: Export statistics for all domains in the URL manager.
- `REMOVE <url>`: Remove a URL from the URL manager and execute `export`.
- `CSV-IMPORT-PATH`:  Add list of URLs from CSV file with the specified social score. Make sure the CSV file is in the correct format with each row containing a URL and its associated social score, separated by commas. Execute `export` command to see the results

## Running Tests

To run the test cases, use the following Maven command:
- `mvn test`

This will execute all the test classes in the `src/test/java` directory and display the test results.

## Calculating score statistics using PostgreSQL and SQL script
The sample dataset file `sample_dataset.csv` containing URLs and respective score was also tested by dumping the data into PostgreSQL.
##STEPS:
1. Create table
```
CREATE TABLE urls_with_score (
    id SERIAL PRIMARY KEY,
    url VARCHAR(255) NOT NULL,
    social_score INTEGER NOT NULL
);
```
1. COPY data from csv to table
```
COPY urls_with_score (url, social_score) from 'path to csvfile' DELIMITER ',' CSV HEADER;
```
1. To check if data was loaded successfully
```
select * from urls_with_score limit 10;
```
1. To find  and cross check the desired results which java implementation produces
```
 SELECT SUBSTRING(url FROM '(?:https?://)?(?:[^@\\n]+@)?(?:www\\.)?([^:/\\n?]+)') as domain,
       COUNT(*) as urls,
       SUM(social_score) as social_score
FROM urls_with_score
GROUP BY domain
ORDER BY social_score DESC;
```