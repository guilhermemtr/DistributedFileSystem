package contact_server;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.MulticastSocket;

import utils.Logger;

public class ContactSearchRequestHandler implements Runnable {

	private String contactServerAddress;
	private long clientHandlerId;
	private MulticastSocket sc;
	private DatagramPacket requestPacket;

	public ContactSearchRequestHandler(String contactServerAddress,
			MulticastSocket s, DatagramPacket requestPacket) {
		this.contactServerAddress = contactServerAddress;
		this.clientHandlerId = this.hashCode();
		this.sc = s;
		this.requestPacket = requestPacket;
	}

	@Override
	public void run() {
		String clientHandlerId = "[id=" + this.clientHandlerId + "]";

		try {
			Logger.log("Handling new client request " + clientHandlerId);
			// The contact server address in byte array.
			byte[] myAddress = this.contactServerAddress.getBytes();

			Logger.log("Creating a response packet to " + clientHandlerId);
			// Creates a datagram response packet.
			DatagramPacket responsePacket = new DatagramPacket(myAddress,
					myAddress.length);

			Logger.log("Setting the address for " + clientHandlerId);
			// Sets the response datagram packet destination to be the client.
			responsePacket.setPort(requestPacket.getPort());
			responsePacket.setAddress(requestPacket.getAddress());
			Logger.log("Setting the port for " + clientHandlerId);
			// Sets the response datagram packet destination port.

			Logger.log("Creating a datagram socket to send the response to "
					+ clientHandlerId);

			Logger.log("Sending the packet " + clientHandlerId);
			
			// Sends the packet to the client.
			sc.send(responsePacket);

			Logger.log("Closing resources for handler " + clientHandlerId);
			// Closes the socket.

			Logger.log("Completed request handling " + clientHandlerId);

		} catch (IOException e) {
			Logger.log("Something has gone wrong " + clientHandlerId);
		}
	}
}
