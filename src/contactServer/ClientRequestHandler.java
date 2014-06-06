package contactServer;

import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import utils.Domains;
import utils.Logger;
import utils.RemoteUtils;

public class ClientRequestHandler {

	public static void handleClientRequests(String contactServerName) {
		try {
			Logger.log("Creating thread pool to handle the requests");
			// Creates a thread pool to handle client requests.
			ExecutorService pool = Executors.newFixedThreadPool(Runtime
					.getRuntime().availableProcessors());

			Logger.log("Creating multicast socket listerner on port: " + Domains.CONTACTSEARCHPORT);
			
			// Creates a multicast socket that listens on default contact search
			// port
			MulticastSocket s = new MulticastSocket(
					Domains.CONTACTSEARCHPORT);
			s.joinGroup(InetAddress
					.getByName(Domains.CONTACTSEARCHADDRESS));

			// Gets it's own ip address
			String ipAddress = Domains.getIP();
			Logger.log("Address is " + ipAddress);
			
			Logger.log("Contact server name is " + contactServerName);
			
			
			Logger.log("Creating a datagram packet to handle the requests");
			// Creates datagram packet to receive the requests.
			byte[] buf = new byte[1024];
			DatagramPacket requestPacket = new DatagramPacket(buf, buf.length);
			
			Logger.log("Now ready to handle client requests");
			while (true) {
				
				// Waits for a request packet to appear.
				s.receive(requestPacket);
				Logger.log("Got new request");
				
				// Submits a new task to the thread pool to handle the client
				// request.
				pool.submit(new ContactSearchRequestHandler(Domains.buildRmiURL(contactServerName, ipAddress, RemoteUtils.RMIPORT), s, requestPacket));
				requestPacket = new DatagramPacket(buf, buf.length);
			}
		} catch (Exception e) {

		}
	}

}
