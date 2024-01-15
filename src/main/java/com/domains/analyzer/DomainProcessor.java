package com.domains.analyzer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

class DomainProcessor {

    private static final Logger logger = Logger.getLogger(DomainProcessor.class.getName());
    private static final String EMAIL_REGEX = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
    private static final Pattern EMAIL_PATTERN = Pattern.compile(EMAIL_REGEX);

    //demo
    public static void main(String[] args) {
        DomainProcessor domainProcessor = new DomainProcessor();
        List<String> domains = domainProcessor.getMostPopularDomains();

        domains.forEach(System.out::println);
    }

    public List<String> getMostPopularDomains() {
        Map<String, Long> countedDomains = validateAndCountDomainsFromUserInput();

        return sortByOccurrences(countedDomains);
    }

    private Map<String, Long> validateAndCountDomainsFromUserInput() {
        Map<String, Long> countedDomains = new HashMap<>();
        Set<String> invalidEmails = new HashSet<>();

        try (BufferedReader br = new BufferedReader(new InputStreamReader(System.in))) {
            System.out.println("Enter email addresses (one per line), press Enter twice to finish:");

            String line;

            while ((line = br.readLine()) != null && !line.trim().isEmpty()) {
                String singleLine = line.trim();

                if (isValidEmail(singleLine)) {
                    String domain = extractDomain(singleLine);
                    countedDomains.merge(domain, 1L, Long::sum);
                } else {
                    invalidEmails.add(singleLine);
                }
            }

        } catch (IOException e) {
            logger.log(Level.SEVERE, "IOException while reading email addresses", e);
        }

        if (!invalidEmails.isEmpty()) {
            logger.log(Level.INFO, "Could not process emails: " + invalidEmails);
        }

        return countedDomains;
    }

    private List<String> sortByOccurrences(Map<String, Long> countedDomains) {
        return countedDomains.entrySet().stream()
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .limit(10)
                .map(entry -> entry.getKey() + " " + entry.getValue())
                .collect(Collectors.toList());
    }

    private String extractDomain(String email) {
        return email.substring(email.indexOf("@") + 1);
    }

    private boolean isValidEmail(String email) {
        return EMAIL_PATTERN
                .matcher(email)
                .matches();
    }
}