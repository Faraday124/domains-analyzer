package com.domains.analyzer

import spock.lang.Specification

class DomainProcessorSpec extends Specification {

    DomainProcessor domainProcessor

    def setup() {
        domainProcessor = new DomainProcessor()
    }

    def "should return 10 most popular domains in a descending order"() {
        given:
            StringBuilder sb = new StringBuilder()
            sb.append(generateEmails("domain.com", 55432))
            sb.append(generateEmails("gmail.com", 4301))
            sb.append(generateEmails("hotmail.com", 1234))
            sb.append(generateEmails("yahoo.com", 987))
            sb.append(generateEmails("domain.pl", 90))
            sb.append(generateEmails("domain2.pl", 90))
            sb.append(generateEmails("domain3.pl", 80))
            sb.append(generateEmails("domain4.pl", 70))
            sb.append(generateEmails("domain5.pl", 60))
            sb.append(generateEmails("domain6.pl", 50))
            sb.append(generateEmails("domain7.pl", 40))
            sb.append(generateEmails("domain8.pl", 30))

            String input = sb.toString()
            System.in = new ByteArrayInputStream(input.getBytes())

        when:
            List<String> domains = domainProcessor.getMostPopularDomains()

        then:
            domains.size() == 10
            domains[0] == "domain.com 55432"
            domains[1] == "gmail.com 4301"
            domains[2] == "hotmail.com 1234"
            domains[3] == "yahoo.com 987"
            domains[4] == "domain.pl 90"
            domains[5] == "domain2.pl 90"
            domains[6] == "domain3.pl 80"
            domains[7] == "domain4.pl 70"
            domains[8] == "domain5.pl 60"
            domains[9] == "domain6.pl 50"
            !domains.contains("domain7.pl")
            !domains.contains("domain8.pl")

    }

    def "should return an empty list when empty input"() {
        given:
            String input = ""
            System.in = new ByteArrayInputStream(input.getBytes())

        when:
            List<String> domains = domainProcessor.getMostPopularDomains()

        then:
            domains.size() == 0

    }

    def "should ignore invalid email"() {
        given:
            String input = "domain@domain.com\n" + "incorrect_email#wrong.com\n"
            System.in = new ByteArrayInputStream(input.getBytes())

        when:
            List<String> domains = domainProcessor.getMostPopularDomains()

        then:
            domains.size() == 1
            domains[0] == "domain.com 1"
            !domains.contains("wrong.com")
    }

    private String generateEmails(String domain, int count) {
        StringBuilder builder = new StringBuilder();

        for (int i = 0; i < count; i++) {
            builder.append("domain")
                    .append(i)
                    .append("@")
                    .append(domain)
                    .append("\n");
        }

        return builder.toString();
    }
}