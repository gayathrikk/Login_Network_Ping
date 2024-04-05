package com.machines.Ping_Status;

import java.io.BufferedReader;

import java.io.IOException;
import java.io.InputStreamReader;

import org.testng.Assert;
import org.testng.annotations.Test;



public class Login_Network_Ping {
	
	  @Test
	    public void testPing() {
	        String domain = "humanbrain-annotation.firebaseapp.com";
	        boolean isPingable = isDomainPingable(domain);
	        if (isPingable) {
	            System.out.println("Domain is pingable: " + domain);
	        } else {
	            System.out.println("Domain is not pingable: " + domain);
	        }
	        Assert.assertTrue(isPingable, "Domain is not pingable: " + domain);
	    }

	    private boolean isDomainPingable(String domain) {
	        try {
	            // Command to execute
	            String command = "ping " + domain;

	            // Execute the command
	            Process process = Runtime.getRuntime().exec(command);

	            // Read the output
	            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
	            String line;
	            StringBuilder pingOutput = new StringBuilder();
	            while ((line = reader.readLine()) != null) {
	                pingOutput.append(line).append("\n");
	            }

	            // Wait for the command to finish execution
	            process.waitFor();

	            // Print the output
	            System.out.println(pingOutput.toString());

	            // Check if ping was successful
	            return pingOutput.toString().contains("bytes=");
	        } catch (IOException | InterruptedException e) {
	            e.printStackTrace();
	            return false; // If an exception occurs, consider domain as not pingable
	        }
	    }
}
