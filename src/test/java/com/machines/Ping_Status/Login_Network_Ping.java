package com.machines.Ping_Status;

import org.testng.annotations.Test;
import org.testng.Assert;

import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class Login_Network_Ping {

    @Test(priority = 1)
    public void Login_Link() throws Exception {
        connect("humanbrain-annotation.firebaseapp.com");
    }

    public static void connect(String... values) throws Exception {

        Session session = null;
        ChannelExec channel = null;

        try {
            session = new JSch().getSession("hbp", "apollo2.humanbrain.in", 22);
            session.setPassword("Health#123");
            session.setConfig("StrictHostKeyChecking", "no");
            session.connect();

            for (String s : values) {
                if (channel != null) {
                    channel.disconnect();
                }
                channel = (ChannelExec) session.openChannel("exec");
                channel.setCommand("ping -c 7 " + s);
                ByteArrayOutputStream responseStream = new ByteArrayOutputStream();
                channel.setOutputStream(responseStream);
                channel.connect();

                while (!channel.isClosed()) {
                    Thread.sleep(100);
                }

                String responseString = new String(responseStream.toByteArray());
                System.out.println("Output of ping command for " + s + ":\n" + responseString);

                String[] lines = responseString.split("\\r?\\n|\\r");
                try {
                    String line = lines[lines.length - 2];
                    String[] str = line.split(", ", 3);
                    int r = Integer.parseInt(String.valueOf(str[1].charAt(0)));
                    if (r != 0) {
                        System.out.println(s + " is pingable");
                    } else {
                        System.out.println(s + " is not pingable");
                    }
                    Assert.assertNotEquals(r, 0);
                } catch (Exception e) {
                    System.err.println("Unable to ping : " + s);
                    e.printStackTrace(); // Adding stack trace for debugging
                }
            }
        } finally {
            if (session != null) {
                session.disconnect();
            }
        }
    }
}
