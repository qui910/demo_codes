package com.duoduo.demo.jsip;

import java.net.InetAddress;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

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
import javax.sip.header.AuthorizationHeader;
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

import com.duoduo.demo.jsip.util.AppUtil;

public class SipTestAll implements SipListener {

	private final static int DEFAULT_EXPIRES = 600;
	private final static int DEFAULT_MAX_FORWARDS = 70;

	private SipFactory sipFactory;
	private HeaderFactory headerFactory;
	private AddressFactory addressFactory;
	private MessageFactory messageFactory;
	private SipStack sipStack;
	private ListeningPoint listeningPoint;
	private SipProvider sipProvider;

	private String localIp;
	private int localPort = 5065;
	private String localProtocol = "UDP";

	private long cseq = 0;

	public SipTestAll() {
		init();
	}

	public SipTestAll(String proxyHost, int proxyPort, String proxyProtocol) {
		init(createSipProperties(proxyHost, proxyPort, proxyProtocol));
	}

	public static void main(String[] augs) {
		String defaultServerHost = "192.168.31.155";
		SipTestAll siptest = new SipTestAll();

		String command = "";
		do {
			printMenu();
			command = AppUtil.getCommand("请输入", "0");
			if (command.equals("1")) {
				String serverHost = AppUtil.getCommand("请输入服务器", defaultServerHost);
				String username = AppUtil.getCommand("请输入用户名", "jason");
				siptest.register(serverHost, username);
			} else if (command.equals("2")) {
				String serverHost = AppUtil.getCommand("请输入服务器", defaultServerHost);
				String username = AppUtil.getCommand("请输入用户名", "jason");
				siptest.unRegister(serverHost, username);
			} else {
				break;
			}

		} while (true);

		System.exit(0);
	}

	private void init() {
		init(createSipProperties());
	}

	private void init(Properties properties) {
		try {
			sipFactory = SipFactory.getInstance();
			headerFactory = sipFactory.createHeaderFactory();
			addressFactory = sipFactory.createAddressFactory();
			messageFactory = sipFactory.createMessageFactory();

			sipStack = sipFactory.createSipStack(createSipProperties());
			localIp = InetAddress.getLocalHost().getHostAddress();
			// localIp = "127.0.0.1";
			listeningPoint = sipStack.createListeningPoint(localIp, localPort, localProtocol);
			sipProvider = sipStack.createSipProvider(listeningPoint);
			sipProvider.addSipListener(this);
		} catch (Exception e) {
			System.out.println("初始化失败！");
			e.printStackTrace();
		}

	}

	private Properties createSipProperties() {
		return createSipProperties(null);
	}

	private Properties createSipProperties(String proxyHost, int proxyPort, String proxyProtocol) {
		return createSipProperties(proxyHost + ':' + proxyPort + '/' + proxyProtocol);
	}

	private Properties createSipProperties(String outboundProxy) {
		Properties properties = new Properties();
		properties.setProperty("javax.sip.STACK_NAME", "stack");
		properties.setProperty("gov.nist.javax.sip.TRACE_LEVEL", "32");

		if (outboundProxy != null) {
			properties.setProperty("javax.sip.OUTBOUND_PROXY", outboundProxy);
		}

		properties.setProperty("gov.nist.javax.sip.LOG_MESSAGE_CONTENT", "true");
		properties.setProperty("gov.nist.javax.sip.DEBUG_LOG", "sipalltest-debuglog.txt");
		properties.setProperty("gov.nist.javax.sip.SERVER_LOG", "sipalltest-messages.xml");

		return properties;
	}

	public Dialog register(String serverHost, String username) {
		return sendRequest(serverHost, Request.REGISTER, username, DEFAULT_EXPIRES);
	}

	public Dialog unRegister(String serverHost, String username) {
		return sendRequest(serverHost, Request.REGISTER, username, 0);
	}

	public Dialog invite(String serverHost, String username) {
		return sendRequest(serverHost, Request.INVITE, username, DEFAULT_EXPIRES);
	}

	public Dialog ack(String serverHost, String username) {
		return sendRequest(serverHost, Request.ACK, username, DEFAULT_EXPIRES);
	}

	private Dialog sendRequest(String serverHost, String requestMethod, String username, int expiresTime) {
		List<Header> headers = new ArrayList<Header>();
		try {
			cseq++;
			CSeqHeader cSeqHeader = headerFactory.createCSeqHeader(cseq, requestMethod);
			headers.add(cSeqHeader);

			ViaHeader viaHeader = headerFactory.createViaHeader(localIp, localPort, localProtocol, null);
			headers.add(viaHeader);

			MaxForwardsHeader maxForwardsHeader = headerFactory.createMaxForwardsHeader(DEFAULT_MAX_FORWARDS);
			headers.add(maxForwardsHeader);

			CallIdHeader callIdHeader = sipProvider.getNewCallId();
			headers.add(callIdHeader);

			Address fromAddress = createAddress(serverHost, username);
			String requestTag = "" + System.currentTimeMillis();
			FromHeader fromHeader = headerFactory.createFromHeader(fromAddress, requestTag);
			headers.add(fromHeader);

			ToHeader toHeader = headerFactory.createToHeader(fromAddress, null);
			headers.add(toHeader);

			Address contactAddress = createContactAddress(username);
			ContactHeader contactHeader = headerFactory.createContactHeader(contactAddress);
			headers.add(contactHeader);

			ExpiresHeader expiresHeader = headerFactory.createExpiresHeader(expiresTime);
			headers.add(expiresHeader);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return sendRequest(serverHost, requestMethod, headers);
	}

	private Dialog sendRequest(String serverHost, String requestMethod, List<Header> headers,
			AuthorizationHeader authorizationHeader) {
		headers.add(authorizationHeader);
		return sendRequest(serverHost, requestMethod, headers);
	}

	private Dialog sendRequest(String serverHost, String requestMethod, List<Header> headers) {
		if (headers == null || headers.isEmpty()) {
			System.out.println("缺少请求头信息！");
			return null;
		}

		try {
			Request request = messageFactory.createRequest(requestMethod + " sip:" + serverHost + " SIP/2.0\r\n\r\n");
			for (Header header : headers) {
				request.addHeader(header);
			}
			ClientTransaction inviteTid = sipProvider.getNewClientTransaction(request);
			inviteTid.sendRequest();
			// Display the message in the text area.
			System.out.println("Request sent:\n" + request.toString() + "\n\n");

			return inviteTid.getDialog();
		} catch (Exception e) {
			// If an error occurred, display the error.
			e.printStackTrace();
			System.out.println("Request sent failed: " + e.getMessage() + "\n");
			return null;
		}
	}

	@Override
	public void processDialogTerminated(DialogTerminatedEvent event) {
		System.out.println("processDialogTerminated: " + event.toString());
	}

	@Override
	public void processIOException(IOExceptionEvent event) {
		System.out.println("processIOException: " + event.toString());
	}

	@Override
	public void processRequest(RequestEvent event) {
		System.out.println("processRequest: " + event.toString());

		String method = event.getRequest().getMethod();
		if (Request.INVITE.equals(method)) {

		}
	}

	@Override
	public void processResponse(ResponseEvent event) {
		int statusCode = event.getResponse().getStatusCode();
		System.out.println("respons status code: " + statusCode);
		System.out.println("respons headers: " + event.getResponse().getHeaderNames());
		// if (statusCode > 400 && statusCode < 410 && !retry) {
		// register(event.getResponse());
		// }
	}

	@Override
	public void processTimeout(TimeoutEvent event) {
		System.out.println("processTimeout: " + event.toString());
	}

	@Override
	public void processTransactionTerminated(TransactionTerminatedEvent event) {
		System.out.println("processTransactionTerminated: " + event.toString());
	}

	private static void printMenu() {
		System.out.println("1 - 注册用户");
		System.out.println("2 - 删除用户");
		System.out.println("");
		System.out.println("0 - 退出");
	}

	private Address createAddress(String serverHost, String username) {
		Address address = null;
		try {
			address = addressFactory.createAddress(username + "<sip:" + username + '@' + serverHost + ">");
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return address;
	}

	private Address createContactAddress(String username) {
		Address address = null;
		try {
			String contactUri = "sip:" + username + "@" + localIp + ";transport=" + localProtocol;
			address = addressFactory.createAddress(contactUri);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return address;
	}
}
