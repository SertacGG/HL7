package com.erc.hl7.HL7Sender.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HL7ReceiverController {

	private static final int PORT_NUMBER = 3403;// change this to whatever your
												// port number is
	private ServerSocket serverSocket;

	@GetMapping("/listen")
	public void greeting() throws IOException {
		ServerSocket serverSocket = new ServerSocket(PORT_NUMBER);
		Socket socket = serverSocket.accept();
		InputStream input = socket.getInputStream();
		InputStreamReader reader = new InputStreamReader(input);
		BufferedReader reader1 = new BufferedReader(new InputStreamReader(input));
		String line = reader1.readLine();    // reads a line of text
		System.err.println(line);
	}

}
