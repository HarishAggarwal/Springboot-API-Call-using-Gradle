package com.example.demo;

import org.springframework.boot.CommandLineRunner;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class ChallengeRunner implements CommandLineRunner {

    // Main method that runs on startup
    @Override
    public void run(String... args) throws Exception {
        System.out.println("🚀 Starting the Bajaj Finserv Health Challenge...");
        
        // --- Part 1: Generate Webhook ---
        WebhookResponse webhookData = generateWebhook();

        if (webhookData != null && webhookData.getWebhookUrl() != null) {
            System.out.println("✅ Webhook and Token received successfully.");
            
            // --- Part 2: Solve SQL and Submit ---
            solveAndSubmit(webhookData.getWebhookUrl(), webhookData.getAccessToken(), "REG12347"); // Use your actual regNo here
        } else {
            System.err.println("❌ Failed to get Webhook. Aborting.");
        }
    }

    // Method to handle the first POST request
    private WebhookResponse generateWebhook() {
        RestTemplate restTemplate = new RestTemplate();
        String registrationUrl = "https://bfhldevapigw.healthrx.co.in/hiring/generateWebhook/JAVA"; [cite: 9]

        // 1. Create the request body [cite: 11]
        RegistrationRequest requestBody = new RegistrationRequest();
        requestBody.setName("John Doe"); // Use your name [cite: 12]
        requestBody.setRegNo("REG12347"); // Use your registration number [cite: 13]
        requestBody.setEmail("john@example.com"); // Use your email [cite: 14]

        System.out.println("Sending registration request to " + registrationUrl);
        try {
            // 2. Send the POST request
            WebhookResponse response = restTemplate.postForObject(registrationUrl, requestBody, WebhookResponse.class);
            return response;
        } catch (Exception e) {
            System.err.println("Error during webhook generation: " + e.getMessage());
            return null;
        }
    }

    // Method to handle the logic and the second POST request
    private void solveAndSubmit(String webhookUrl, String accessToken, String regNo) {
        RestTemplate restTemplate = new RestTemplate();
        
        // 1. Get your final SQL query
        String sqlQuery = getSqlQuery(regNo);
        System.out.println("Final SQL Query: " + sqlQuery);

        // 2. Create the request body [cite: 30]
        SubmissionRequest submissionBody = new SubmissionRequest();
        submissionBody.setFinalQuery(sqlQuery); [cite: 31]

        // 3. Set the required headers [cite: 26]
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON); [cite: 28]
        headers.set("Authorization", accessToken); // Note: It might need to be "Bearer " + accessToken [cite: 27]

        HttpEntity<SubmissionRequest> entity = new HttpEntity<>(submissionBody, headers);

        System.out.println("Submitting solution to: " + webhookUrl);
        try {
            // 4. Send the final POST request
            ResponseEntity<String> response = restTemplate.postForEntity(webhookUrl, entity, String.class);
            System.out.println("✅ Submission successful! Status: " + response.getStatusCode());
            System.out.println("Response Body: " + response.getBody());
        } catch(Exception e) {
            System.err.println("❌ Error during submission: " + e.getMessage());
        }
    }

    // Helper method to get the SQL query
    private String getSqlQuery(String regNo) {
        // 1. Extract the numeric part of the registration number
        String numericPart = regNo.replaceAll("[^0-9]", "");
        
        // 2. Get the last two digits
        String lastTwoDigitsStr = numericPart.substring(numericPart.length() - 2);
        int lastTwoDigits = Integer.parseInt(lastTwoDigitsStr);
        
        String finalQuery;
        
        // 3. Check if the number is odd or even
        if (lastTwoDigits % 2 != 0) {
            // ODD: Solve Question 1 [cite: 20]
            System.out.println("RegNo ends in an odd number (" + lastTwoDigits + "). Solving Question 1.");
            // IMPORTANT: Replace this placeholder with your actual SQL query from the Google Drive link
            finalQuery = "SELECT patient_name FROM patients WHERE age > 50;";
        } else {
            // EVEN: Solve Question 2 [cite: 22]
            System.out.println("RegNo ends in an even number (" + lastTwoDigits + "). Solving Question 2.");
            // IMPORTANT: Replace this placeholder with your actual SQL query from the Google Drive link
            finalQuery = "SELECT doctor_name FROM doctors WHERE specialization = 'Cardiology';";
        }
        
        return finalQuery;
    }
}