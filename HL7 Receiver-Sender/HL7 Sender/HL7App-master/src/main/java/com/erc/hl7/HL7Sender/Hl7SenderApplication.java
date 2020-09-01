package com.erc.hl7.HL7Sender;

import java.net.ServerSocket;
import java.net.Socket;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import ca.uhn.hl7v2.DefaultHapiContext;
import ca.uhn.hl7v2.HapiContext;
import ca.uhn.hl7v2.model.Message;
import ca.uhn.hl7v2.parser.Parser;
import ca.uhn.hl7v2.parser.PipeParser;

@SpringBootApplication
public class Hl7SenderApplication {

	public static final int PORT = 8020;
	public static final boolean VERBOSE = true;

	public static void main(String[] args) {
		SpringApplication.run(Hl7SenderApplication.class, args);
		//listener();
	}

	/*public static void listener() {

		try (ServerSocket serverSocket = new ServerSocket(PORT)) {

			while (true) {
				Socket socket = serverSocket.accept();

				System.out.println("New client connected");
				InputStream input = socket.getInputStream();
				BufferedReader in = new BufferedReader(new InputStreamReader(
						input));
				String line = in.readLine();
				line = line.substring(1);
				StringBuilder requestString = new StringBuilder();
				String strCurrentLine = "";
				System.out.println(line);
				requestString.append(line);
				
				while ((strCurrentLine = in.readLine()) != null) {
					System.out.println(strCurrentLine);
					requestString.append(strCurrentLine);
				}
				HapiContext context = new DefaultHapiContext();
				Parser parser = context.getPipeParser();

			}
		} catch (IOException ex) {
			System.out.println("Server exception: " + ex.getMessage());
			ex.printStackTrace();
		}
	}*/

}
