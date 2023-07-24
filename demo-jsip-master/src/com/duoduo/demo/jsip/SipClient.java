package com.duoduo.demo.jsip;

import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.Random;

import javax.sip.ClientTransaction;
import javax.sip.Dialog;
import javax.sip.DialogTerminatedEvent;
import javax.sip.IOExceptionEvent;
import javax.sip.ListeningPoint;
import javax.sip.RequestEvent;
import javax.sip.ResponseEvent;
import javax.sip.SipFactory;
import javax.sip.SipListener;
import javax.sip.SipProvider;
import javax.sip.SipStack;
import javax.sip.TimeoutEvent;
import javax.sip.TransactionTerminatedEvent;
import javax.sip.address.Address;
import javax.sip.address.AddressFactory;
import javax.sip.header.CSeqHeader;
import javax.sip.header.CallIdHeader;
import javax.sip.header.ContactHeader;
import javax.sip.header.ExpiresHeader;
import javax.sip.header.FromHeader;
import javax.sip.header.Header;
import javax.sip.header.HeaderFactory;
import javax.sip.header.MaxForwardsHeader;
import javax.sip.header.ToHeader;
import javax.sip.header.ViaHeader;
import javax.sip.message.MessageFactory;
import javax.sip.message.Request;

import org.apache.log4j.ConsoleAppender;
import org.apache.log4j.Logger;
import org.apache.log4j.SimpleLayout;

import com.duoduo.demo.jsip.util.AppUtil;

/**
 * TODO
 * @author chengesheng@kedacom.com
 * @date 2013-9-12 下午1:45:17
 * @note SipClient.java
 */
public class SipClient implements SipListener {

	private static final Logger logger = Logger.getLogger(Login.class.getSimpleName());

	private SipFactory sipFactory; // Used to access the SIP API.
	private MessageFactory messageFactory; // Used to create SIP message factory.
	private HeaderFactory headerFactory; // Used to create SIP headers.
	private AddressFactory addressFactory; // Used to create SIP URIs.

	private SipStack sipStack; // The SIP stack.
	private SipProvider sipProvider; // Used to send SIP messages.
	private ListeningPoint listeningPoint; // SIP listening IP address/port.
	private Properties properties; // Other properties.
	private Dialog dialog;
	private ClientTransaction inviteTid;

	// 服务器信息
	private String proxyHost = null; // TODO "sip.linphone.org";
	private String serverHost = "172.16.178.3"; // TODO "sip.linphone.org";

	// 客户端信息
	private String clientIp; // The local IP address.
	private int clientPort = 5063; // The local port.
	private String clientProtocol = "UDP"; // TODO The local protocol (UDP).
	private String clientUsername = "jason";
	private String clientPassword = "888888";
	// private String privateUserId;
	private int clientTag = (new Random()).nextInt(); // The local tag.
	private Address clientContactAddress; // The contact address.
	private ContactHeader clientContactHeader; // The contact header.

	private Request request;
	private boolean retry;
	private long cseq = 0;

	public SipClient(int clientPort, String clientProtocol) {
		this(clientPort, clientProtocol, null);
	}

	public SipClient(int clientPort, String clientProtocol, String proxyHost) {
		this.clientPort = clientPort;
		this.clientProtocol = clientProtocol;
		this.proxyHost = proxyHost;

		init();
	}

	// 初始化
	private void init() {
		try {
			logger.addAppender(new ConsoleAppender(new SimpleLayout()));
			// Get the local IP address.
			clientIp = InetAddress.getLocalHost().getHostAddress();

			// Create the SIP factory and set the path name.
			sipFactory = SipFactory.getInstance();
			sipFactory.setPathName("gov.nist");

			// Create the SIP message/header/address factory.
			messageFactory = sipFactory.createMessageFactory();
			headerFactory = sipFactory.createHeaderFactory();
			addressFactory = sipFactory.createAddressFactory();

			// Create the SIP stack.
			properties = getProperties();
			sipStack = sipFactory.createSipStack(properties);

			// Create the SIP listening point and bind it to the local IP address, port and protocol.
			listeningPoint = sipStack.createListeningPoint(clientIp, clientPort, clientProtocol);
			// Create the SIP provider.
			sipProvider = sipStack.createSipProvider(listeningPoint);
			// Add our application as a SIP listener.
			sipProvider.addSipListener(this);

			// Display the local IP address and port in the text area.
			logger.debug("Local address: " + clientIp + ":" + clientPort + "\n");
		} catch (Exception e) {
			e.printStackTrace();
			// If an error occurs, display an error message box and exit.
			logger.error(e);
			System.exit(-1);
		}
	}

	// 登录
	public boolean login(String server, String username, String password) {
		this.serverHost = server;
		this.clientUsername = username;
		this.clientPassword = password;

		try {
			// Create the contact address used for all SIP messages.
			String userUri = getContactAddress(clientUsername, clientIp, "tcp");
			clientContactAddress = addressFactory.createAddress(userUri);
			// Create the contact header used for all SIP messages.
			clientContactHeader = headerFactory.createContactHeader(clientContactAddress);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return true;
	}

	public static void main(String[] args) {
		Login login = new Login();
		login.init();

		String command = "";
		do {
			command = AppUtil.getCommand("1 - Register\n2 - UnRegister\n0 - Exit\nYour command is");
			if (command.equals("1")) {
				login.register(null);
			} else if (command.equals("2")) {
				login.unregister(null);
			} else {
				break;
			}

		} while (true);

		System.exit(0);
	}

	public void register(String server, String username) {
		register(server, username, null);
	}

	public void register(String server, String username, String password) {
		doRegister(server, username, null, 600);
	}

	public void unregister(String server, String username) {
		doRegister(serverHost, clientUsername, clientPassword, 0);
	}

	public void doRegister(String server, String username, String password, int expiresTime) {
		try {
			ArrayList<Header> headers = new ArrayList<Header>();

			// The "CSeq" header.
			cseq++;
			CSeqHeader cSeqHeader = headerFactory.createCSeqHeader(cseq, Request.REGISTER);
			headers.add(cSeqHeader);

			ViaHeader viaHeader = headerFactory.createViaHeader(clientIp, clientPort, "udp", null); // tcp
			headers.add(viaHeader);

			// The "Max-Forwards" header.
			MaxForwardsHeader maxForwardsHeader = headerFactory.createMaxForwardsHeader(70);
			headers.add(maxForwardsHeader);

			// The "Call-Id" header.
			CallIdHeader callIdHeader = sipProvider.getNewCallId();
			headers.add(callIdHeader);

			Address fromAddress = addressFactory.createAddress(getUserAddress(server, username));

			// The "From" header.
			FromHeader fromHeader = headerFactory.createFromHeader(fromAddress, String.valueOf(clientTag));
			headers.add(fromHeader);

			// The "To" header.
			ToHeader toHeader = headerFactory.createToHeader(fromAddress, null);
			headers.add(toHeader);

			clientContactHeader = headerFactory.createContactHeader(clientContactAddress);
			headers.add(clientContactHeader);

			ExpiresHeader expiresHeader = headerFactory.createExpiresHeader(expiresTime);
			headers.add(expiresHeader);

			doRequest(server, Request.REGISTER, headers);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void doRequest(String serverHost, String requestMethod, List<Header> headers) {
		if (headers == null || headers.isEmpty()) {
			return;
		}

		try {
			Request request = messageFactory.createRequest(requestMethod + " sip:" + serverHost + " SIP/2.0\r\n\r\n");
			for (Header header : headers) {
				request.addHeader(header);
			}

			inviteTid = sipProvider.getNewClientTransaction(request);
			// send the request out.
			inviteTid.sendRequest();
			dialog = inviteTid.getDialog();
			// Send the request statelessly through the SIP provider.
			// sipProvider.sendRequest(request);

			// Display the message in the text area.
			logger.debug("Request sent:\n" + request.toString() + "\n\n");
		} catch (Exception e) {
			// If an error occurred, display the error.
			e.printStackTrace();
			logger.debug("Request sent failed: " + e.getMessage() + "\n");
		}
	}

	@Override
	public void processResponse(ResponseEvent responseEvent) {
		int statusCode = responseEvent.getResponse().getStatusCode();
		if (statusCode > 400 && statusCode < 410 && !retry) {
			// register(responseEvent.getResponse());
			// TODO 若认证失败则增加认证Header
		}
	}

	@Override
	public void processRequest(RequestEvent requestEvent) {
		logger.debug("process request ..." + requestEvent.toString());
	}

	@Override
	public void processTimeout(TimeoutEvent timeoutEvent) {
		logger.debug("process timeout ..." + timeoutEvent.toString());
	}

	@Override
	public void processIOException(IOExceptionEvent exceptionEvent) {
		logger.debug("process IOException ..." + exceptionEvent.toString());
	}

	@Override
	public void processTransactionTerminated(TransactionTerminatedEvent transactionTerminatedEvent) {
		logger.debug("process TransactionTerminated ..." + transactionTerminatedEvent.toString());
	}

	@Override
	public void processDialogTerminated(DialogTerminatedEvent dialogTerminatedEvent) {
		logger.debug("process DialogTerminated ..." + dialogTerminatedEvent.toString());
	}

	private Properties getProperties() {
		// Create and set the SIP stack properties.
		Properties prop = new Properties();
		prop.setProperty("javax.sip.STACK_NAME", "SipClient");
		prop.setProperty("gov.nist.javax.sip.TRACE_LEVEL", "32");

		if (proxyHost != null) {
			prop.setProperty("javax.sip.OUTBOUND_PROXY", proxyHost + ':' + clientPort + '/' + clientProtocol);
		}

		prop.setProperty("gov.nist.javax.sip.LOG_MESSAGE_CONTENT", "true");
		prop.setProperty("gov.nist.javax.sip.DEBUG_LOG", "SipClient-debuglog.txt");
		prop.setProperty("gov.nist.javax.sip.SERVER_LOG", "SipClient-messages.xml");

		return prop;
	}

	private String getContactAddress(String username, String ip, String protocol) {
		return "sip:" + clientUsername + "@" + clientIp + ";transport=" + protocol;
	}

	private String getUserAddress(String server, String username) {
		return username + "<sip:" + username + '@' + server + ">";
	}

}
