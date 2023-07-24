package com.duoduo.demo.jsip;

import gov.nist.javax.sip.header.CSeq;
import gov.nist.javax.sip.header.ContentLength;
import gov.nist.javax.sip.header.ContentType;
import gov.nist.javax.sip.header.From;
import gov.nist.javax.sip.header.Via;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Properties;
import java.util.Timer;

import javax.sip.ClientTransaction;
import javax.sip.Dialog;
import javax.sip.DialogTerminatedEvent;
import javax.sip.IOExceptionEvent;
import javax.sip.InvalidArgumentException;
import javax.sip.ListeningPoint;
import javax.sip.PeerUnavailableException;
import javax.sip.RequestEvent;
import javax.sip.ResponseEvent;
import javax.sip.ServerTransaction;
import javax.sip.SipException;
import javax.sip.SipFactory;
import javax.sip.SipListener;
import javax.sip.SipProvider;
import javax.sip.SipStack;
import javax.sip.TimeoutEvent;
import javax.sip.TransactionAlreadyExistsException;
import javax.sip.TransactionTerminatedEvent;
import javax.sip.TransactionUnavailableException;
import javax.sip.address.Address;
import javax.sip.address.AddressFactory;
import javax.sip.address.URI;
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
import javax.sip.message.Response;

public class SipPhone implements SipListener {

	private static Hashtable<URI, URI> currUser = new Hashtable<URI, URI>();

	class TimerTask extends Timer {

		public TimerTask() {

		}

		public void run() {

		}
	}

	private String ipAddr = "192.168.0.20";

	private int port = 5060;

	private void processRegister(Request request, RequestEvent requestEvent) {
		if (null == request) {
			System.out.println("processInvite request is null.");
			return;
		}
		// System.out.println("Request " + request.toString());
		ServerTransaction serverTransactionId = requestEvent.getServerTransaction();

		try {
			Response response = null;
			ToHeader head = (ToHeader) request.getHeader(ToHeader.NAME);
			Address toAddress = head.getAddress();
			URI toURI = toAddress.getURI();
			ContactHeader contactHeader = (ContactHeader) request.getHeader("Contact");
			Address contactAddr = contactHeader.getAddress();
			URI contactURI = contactAddr.getURI();
			System.out.println("processRegister from: " + toURI + " request str: " + contactURI);
			int expires = request.getExpires().getExpires();
			// 如果expires不等于0,则为注册，否则为注销。
			if (expires != 0 || contactHeader.getExpires() != 0) {
				currUser.put(toURI, contactURI);
				System.out.println("register user " + toURI);
			} else {
				currUser.remove(toURI);
				System.out.println("unregister user " + toURI);
			}

			response = msgFactory.createResponse(200, request);
			System.out.println("send register response  : " + response.toString());

			if (serverTransactionId == null) {
				serverTransactionId = sipProvider.getNewServerTransaction(request);
				serverTransactionId.sendResponse(response);
				// serverTransactionId.terminate();
				System.out.println("register serverTransaction: " + serverTransactionId);
			} else {
				System.out.println("processRequest serverTransactionId is null.");
			}

		} catch (ParseException e) {
			e.printStackTrace();
		} catch (SipException e) {
			e.printStackTrace();
		} catch (InvalidArgumentException e) {
			e.printStackTrace();
		}
	}

	private void processInvite(Request request, RequestEvent requestEvent) {
		if (null == request) {
			System.out.println("processInvite request is null.");
			return;
		}
		try {
			// 发送100 Trying
			serverTransactionId = requestEvent.getServerTransaction();
			if (serverTransactionId == null) {
				serverTransactionId = sipProvider.getNewServerTransaction(request);
				callerDialog = serverTransactionId.getDialog();
				Response response = msgFactory.createResponse(Response.TRYING, request);
				serverTransactionId.sendResponse(response);
			}
			// 查询目标地址
			URI reqUri = request.getRequestURI();
			URI contactURI = currUser.get(reqUri);
			ViaHeader callerVia = (ViaHeader) request.getHeader(Via.NAME);

			System.out.println("processInvite rqStr=" + reqUri + " cUDP" + callerVia.getBranch() + "sipphone");

			// FIXME 需要测试是否能够通过设置VIA头域来修改VIA头域值
			request.removeHeader(Via.NAME);
			request.addHeader(callerVia);

			// 更新contact的地址
			ContactHeader contactHeader = headerFactory.createContactHeader();
			Address address = addressFactory.createAddress("sip:sipsoft@" + ipAddr + ":" + port);
			contactHeader.setAddress(address);
			contactHeader.setExpires(3600);
			request.setHeader(contactHeader);

			clientTransactionId = sipProvider.getNewClientTransaction(request);
			clientTransactionId.sendRequest();

			System.out.println("processInvite clientTransactionId=" + clientTransactionId.toString());

			System.out.println("send invite to callee: " + request);
		} catch (TransactionUnavailableException e1) {
			e1.printStackTrace();
		} catch (SipException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void processSubscribe(Request request) {
		if (null == request) {
			System.out.println("processSubscribe request is null.");
			return;
		}
		ServerTransaction serverTransactionId = null;
		try {
			serverTransactionId = sipProvider.getNewServerTransaction(request);
		} catch (TransactionAlreadyExistsException e1) {
			e1.printStackTrace();
		} catch (TransactionUnavailableException e1) {
			e1.printStackTrace();
		}

		try {
			Response response = null;
			response = msgFactory.createResponse(200, request);
			if (response != null) {
				ExpiresHeader expireHeader = headerFactory.createExpiresHeader(30);
				response.setExpires(expireHeader);
			}
			System.out.println("response : " + response.toString());

			if (serverTransactionId != null) {
				serverTransactionId.sendResponse(response);
				serverTransactionId.terminate();
			} else {
				System.out.println("processRequest serverTransactionId is null.");
			}

		} catch (ParseException e) {
			e.printStackTrace();
		} catch (SipException e) {
			e.printStackTrace();
		} catch (InvalidArgumentException e) {
			e.printStackTrace();
		}
	}

	private void processBye(Request request, RequestEvent requestEvent) {
		if (null == request || null == requestEvent) {
			System.out.println("processBye request is null.");
			return;
		}
		Request byeReq = null;
		Dialog dialog = requestEvent.getDialog();
		System.out.println("calleeDialog : " + calleeDialog);
		System.out.println("callerDialog : " + callerDialog);
		try {
			if (dialog.equals(calleeDialog)) {
				byeReq = callerDialog.createRequest(request.getMethod());
				ClientTransaction clientTran = sipProvider.getNewClientTransaction(byeReq);
				callerDialog.sendRequest(clientTran);
				calleeDialog.setApplicationData(requestEvent.getServerTransaction());
			} else if (dialog.equals(callerDialog)) {
				byeReq = calleeDialog.createRequest(request.getMethod());
				ClientTransaction clientTran = sipProvider.getNewClientTransaction(byeReq);
				calleeDialog.sendRequest(clientTran);
				callerDialog.setApplicationData(requestEvent.getServerTransaction());
			} else {
				System.out.println("");
			}

			System.out.println("send bye to peer:" + byeReq.toString());
		} catch (SipException e) {
			e.printStackTrace();
		}

	}

	private void processCancel(Request request) {
		if (null == request) {
			System.out.println("processCancel request is null.");
			return;
		}
	}

	private void processInfo(Request request) {
		if (null == request) {
			System.out.println("processInfo request is null.");
			return;
		}
	}

	private void processAck(Request request, RequestEvent requestEvent) {
		if (null == request) {
			System.out.println("processAck request is null.");
			return;
		}

		try {
			Request ackRequest = null;
			CSeq csReq = (CSeq) request.getHeader(CSeq.NAME);
			ackRequest = calleeDialog.createAck(csReq.getSeqNumber());
			calleeDialog.sendAck(ackRequest);
			System.out.println("send ack to callee:" + ackRequest.toString());
		} catch (SipException e) {
			e.printStackTrace();
		} catch (InvalidArgumentException e) {
			e.printStackTrace();
		}

	}

	private void processCancel(Request request, RequestEvent requestEvent) {
		// 判断参数是否有效
		if (request == null || requestEvent == null) {
			System.out.println("processCancel input parameter invalid.");
			return;
		}

		try {
			// 发送CANCEL 200 OK消息
			Response response = msgFactory.createResponse(Response.OK, request);
			ServerTransaction cancelServTran = requestEvent.getServerTransaction();
			if (cancelServTran == null) {
				cancelServTran = sipProvider.getNewServerTransaction(request);
			}
			cancelServTran.sendResponse(response);

			// 向对端发送CANCEL消息
			Request cancelReq = null;
			Request inviteReq = clientTransactionId.getRequest();
			List<Header> list = new ArrayList<Header>();
			Via viaHeader = (Via) inviteReq.getHeader(Via.NAME);
			list.add(viaHeader);

			CSeq cseq = (CSeq) inviteReq.getHeader(CSeq.NAME);
			CSeq cancelCSeq = (CSeq) headerFactory.createCSeqHeader(cseq.getSeqNumber(), Request.CANCEL);
			cancelReq = msgFactory.createRequest(inviteReq.getRequestURI(), inviteReq.getMethod(),
					(CallIdHeader) inviteReq.getHeader(CallIdHeader.NAME), cancelCSeq,
					(FromHeader) inviteReq.getHeader(From.NAME), (ToHeader) inviteReq.getHeader(ToHeader.NAME), list,
					(MaxForwardsHeader) inviteReq.getHeader(MaxForwardsHeader.NAME));
			ClientTransaction cancelClientTran = sipProvider.getNewClientTransaction(cancelReq);
			cancelClientTran.sendRequest();
		} catch (ParseException e) {
			e.printStackTrace();
		} catch (TransactionAlreadyExistsException e) {
			e.printStackTrace();
		} catch (TransactionUnavailableException e) {
			e.printStackTrace();
		} catch (SipException e) {
			e.printStackTrace();
		} catch (InvalidArgumentException e) {
			e.printStackTrace();
		}
	}

	private ServerTransaction serverTransactionId = null;

	public void processRequest(RequestEvent arg0) {
		Request request = arg0.getRequest();
		if (null == request) {
			System.out.println("processRequest request is null.");
			return;
		}
		System.out.println("processRequest:" + request.toString());
		if (Request.INVITE.equals(request.getMethod())) {
			processInvite(request, arg0);
		} else if (Request.REGISTER.equals(request.getMethod())) {
			processRegister(request, arg0);
		} else if (Request.SUBSCRIBE.equals(request.getMethod())) {
			processSubscribe(request);
		} else if (Request.ACK.equalsIgnoreCase(request.getMethod())) {
			processAck(request, arg0);
		} else if (Request.BYE.equalsIgnoreCase(request.getMethod())) {
			processBye(request, arg0);
		} else if (Request.CANCEL.equalsIgnoreCase(request.getMethod())) {
			processCancel(request, arg0);
		} else {
			System.out.println("no support the method!");
		}
	}

	private Dialog calleeDialog = null;

	private Dialog callerDialog = null;

	ClientTransaction clientTransactionId = null;

	private void doByeResponse(Response response, ResponseEvent responseEvent) {
		Dialog dialog = responseEvent.getDialog();

		try {
			Response byeResp = null;
			if (callerDialog.equals(dialog)) {
				ServerTransaction servTran = (ServerTransaction) calleeDialog.getApplicationData();
				byeResp = msgFactory.createResponse(response.getStatusCode(), servTran.getRequest());
				servTran.sendResponse(byeResp);
			} else if (calleeDialog.equals(dialog)) {
				ServerTransaction servTran = (ServerTransaction) callerDialog.getApplicationData();
				byeResp = msgFactory.createResponse(response.getStatusCode(), servTran.getRequest());
				servTran.sendResponse(byeResp);
			} else {

			}
			System.out.println("send bye response to peer:" + byeResp.toString());
		} catch (ParseException e) {
			e.printStackTrace();
		} catch (SipException e) {
			e.printStackTrace();
		} catch (InvalidArgumentException e) {
			e.printStackTrace();
		}
	}

	public void processResponse(ResponseEvent arg0) {
		// FIXME 需要判断各个响应对应的是什么请求
		Response response = arg0.getResponse();

		System.out.println("recv the response :" + response.toString());
		System.out.println("respone to request : " + arg0.getClientTransaction().getRequest());

		if (response.getStatusCode() == Response.TRYING) {
			System.out.println("The response is 100 response.");
			return;
		}

		try {
			ClientTransaction clientTran = (ClientTransaction) arg0.getClientTransaction();

			if (Request.INVITE.equalsIgnoreCase(clientTran.getRequest().getMethod())) {
				int statusCode = response.getStatusCode();
				Response callerResp = null;

				callerResp = msgFactory.createResponse(statusCode, serverTransactionId.getRequest());

				// 更新contact头域值，因为后面的消息是根据该URI来路由的
				ContactHeader contactHeader = headerFactory.createContactHeader();
				Address address = addressFactory.createAddress("sip:sipsoft@" + ipAddr + ":" + port);
				contactHeader.setAddress(address);
				contactHeader.setExpires(3600);
				callerResp.addHeader(contactHeader);

				// 拷贝to头域
				ToHeader toHeader = (ToHeader) response.getHeader(ToHeader.NAME);
				callerResp.setHeader(toHeader);

				// 拷贝相应的消息体
				ContentLength contentLen = (ContentLength) response.getContentLength();
				if (contentLen != null && contentLen.getContentLength() != 0) {
					ContentType contentType = (ContentType) response.getHeader(ContentType.NAME);
					System.out.println("the sdp contenttype is " + contentType);

					callerResp.setContentLength(contentLen);
					// callerResp.addHeader(contentType);
					callerResp.setContent(response.getContent(), contentType);
				} else {
					System.out.println("sdp is null.");
				}
				if (serverTransactionId != null) {
					callerDialog = serverTransactionId.getDialog();
					calleeDialog = clientTran.getDialog();
					serverTransactionId.sendResponse(callerResp);
					System.out.println("callerDialog=" + callerDialog);
					System.out.println("serverTransactionId.branch=" + serverTransactionId.getBranchId());
				} else {
					System.out.println("serverTransactionId is null.");
				}

				System.out.println("send response to caller : " + callerResp.toString());
			} else if (Request.BYE.equalsIgnoreCase(clientTran.getRequest().getMethod())) {
				doByeResponse(response, arg0);
			} else if (Request.CANCEL.equalsIgnoreCase(clientTran.getRequest().getMethod())) {
				// doCancelResponse(response, arg0);
			} else {

			}

		} catch (ParseException e) {
			e.printStackTrace();
		} catch (SipException e) {
			e.printStackTrace();
		} catch (InvalidArgumentException e) {
			e.printStackTrace();
		} catch (Exception ex) {
			ex.printStackTrace();
		}

	}

	private void doCancelResponse(Response response, ResponseEvent responseEvent) {
		// FIXME 需要验证参数的有效性
		ServerTransaction servTran = (ServerTransaction) callerDialog.getApplicationData();
		try {
			Response cancelResp = msgFactory.createResponse(response.getStatusCode(), servTran.getRequest());
			servTran.sendResponse(cancelResp);
		} catch (ParseException e) {
			e.printStackTrace();
		} catch (SipException e) {
			e.printStackTrace();
		} catch (InvalidArgumentException e) {
			e.printStackTrace();
		}

	}

	public void processDialogTerminated(DialogTerminatedEvent event) {
		System.out.println("processDialogTerminated " + event.toString());
	}

	public void processIOException(IOExceptionEvent event) {
		System.out.println("processIOException " + event.toString());
	}

	public void processTimeout(TimeoutEvent event) {
		System.out.println(" processTimeout " + event.toString());
	}

	public void processTransactionTerminated(TransactionTerminatedEvent event) {
		System.out.println(" processTransactionTerminated " + event.getClientTransaction().getBranchId() + " "
				+ event.getServerTransaction().getBranchId());
	}

	private static SipStack sipStack = null;

	private static AddressFactory addressFactory = null;

	private static MessageFactory msgFactory = null;

	private static HeaderFactory headerFactory = null;

	private static SipProvider sipProvider = null;

	private void init() {
		SipFactory sipFactory = null;

		sipFactory = SipFactory.getInstance();
		if (null == sipFactory) {
			System.out.println("init sipFactory is null.");
			return;
		}

		sipFactory.setPathName("gov.nist");
		Properties properties = new Properties();
		properties.setProperty("javax.sip.STACK_NAME", "sipphone");
		// You need 16 for logging traces. 32 for debug + traces.
		// Your code will limp at 32 but it is best for debugging.
		properties.setProperty("gov.nist.javax.sip.TRACE_LEVEL", "32");
		properties.setProperty("gov.nist.javax.sip.DEBUG_LOG", "sipphonedebug.txt");
		properties.setProperty("gov.nist.javax.sip.SERVER_LOG", "sipphonelog.txt");
		try {
			sipStack = sipFactory.createSipStack(properties);
		} catch (PeerUnavailableException e) {
			e.printStackTrace();
			return;
		}

		try {
			headerFactory = sipFactory.createHeaderFactory();
			addressFactory = sipFactory.createAddressFactory();
			msgFactory = sipFactory.createMessageFactory();
			ListeningPoint lp = sipStack.createListeningPoint("192.168.0.20", 5060, "udp");
			SipPhone listener = this;

			sipProvider = sipStack.createSipProvider(lp);
			System.out.println("udp provider " + sipProvider.toString());
			sipProvider.addSipListener(listener);
		} catch (Exception ex) {
			ex.printStackTrace();
			return;
		}

	}

	public static void main(String[] args) {
		new SipPhone().init();
	}

}
