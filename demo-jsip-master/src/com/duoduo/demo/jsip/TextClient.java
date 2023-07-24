package com.duoduo.demo.jsip;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.net.InetAddress;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

/**
 * @author chengesheng@gmail.com
 * @date 2013-8-9 上午12:43:54
 * @note TextClient
 */
public class TextClient extends JFrame implements MessageProcessor {

	/** serialVersionUID */
	private static final long serialVersionUID = -5041780879343358334L;

	private SipLayer sipLayer;
	private JTextField fromAddress;
	private JLabel fromLbl;
	private JLabel receivedLbl;
	private JTextArea receivedMessages;
	private JScrollPane receivedScrollPane;
	private JButton registerBtn;
	private JButton sendBtn;
	private JLabel sendLbl;
	private JTextField sendMessages;
	private JTextField toAddress;
	private JLabel toLbl;

	public static void main(String[] args) {
//		if (args.length != 2) {
//			printUsage();
//			System.exit(-1);
//		}

		try {
//			String username = args[0];
//			int port = Integer.parseInt(args[1]);
			String username = "jason2";
			int port = 5062;

//			String ip = InetAddress.getLocalHost().getHostAddress();
			String ip = "192.168.1.133";
			System.out.println("--username = " + username + "\n--port = " + port + "\n--ip = " + ip);

			SipLayer sipLayer = new SipLayer(username, ip, port);
			TextClient tc = new TextClient(sipLayer);

			sipLayer.setMessageProcessor(tc);
			// tc.show();
			tc.setVisible(true);
		} catch (Throwable e) {
			System.out.println("Problem initializing the SIP stack.");
			e.printStackTrace();
			System.exit(-1);
		}
	}

	private static void printUsage() {
		System.out.println("Syntax:");
		System.out.println("  java -jar textclient.jar <username> <port>");
		System.out.println("where <username> is the nickname of this user");
		System.out.println("and <port> is the port number to use. Usually 5060 if not used by another process.");
		System.out.println("Example:");
		System.out.println("  java -jar textclient.jar snoopy71 5061");
	}

	public TextClient(SipLayer sip) {
		super();
		sipLayer = sip;
		initWindow();
		String from = "sip:" + sip.getUsername() + "@" + sip.getHost() + ":" + sip.getPort();
		this.fromAddress.setText(from);
		this.toAddress.setText("sip:1001@192.168.1.133:5060");
	}

	private void initWindow() {
		receivedLbl = new JLabel();
		sendLbl = new JLabel();
		sendMessages = new JTextField();
		receivedScrollPane = new JScrollPane();
		receivedMessages = new JTextArea();
		fromLbl = new JLabel();
		fromAddress = new JTextField();
		toLbl = new JLabel();
		toAddress = new JTextField();
		registerBtn = new JButton();
		sendBtn = new JButton();
		getContentPane().setLayout(null);
		setTitle("TextClient");
		addWindowListener(new WindowAdapter() {

			public void windowClosing(WindowEvent evt) {
				System.exit(0);

			}
		});
		receivedLbl.setText("Received Messages:");
		receivedLbl.setAlignmentY(0.0F);
		receivedLbl.setPreferredSize(new java.awt.Dimension(25, 100));
		getContentPane().add(receivedLbl);
		receivedLbl.setBounds(5, 0, 136, 20);
		sendLbl.setText("Send Message:");
		getContentPane().add(sendLbl);
		sendLbl.setBounds(5, 150, 90, 20);
		getContentPane().add(sendMessages);
		sendMessages.setBounds(5, 170, 270, 20);
		receivedMessages.setAlignmentX(0.0F);
		receivedMessages.setEditable(false); // 设置为不可编辑 receivedMessages.setLineWrap(true);

		// 设置可换行
		receivedMessages.setWrapStyleWord(true);
		// 设置换行方式为单词边界换行
		receivedScrollPane.setViewportView(receivedMessages);
		receivedScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

		getContentPane().add(receivedScrollPane);
		receivedScrollPane.setBounds(5, 20, 270, 130);
		fromLbl.setText("From:");
		getContentPane().add(fromLbl);
		fromLbl.setBounds(5, 200, 35, 15);
		getContentPane().add(fromAddress);
		fromAddress.setBounds(40, 200, 235, 20);
		fromAddress.setEditable(false);
		toLbl.setText("To:");
		getContentPane().add(toLbl);
		toLbl.setBounds(5, 225, 35, 15);
		getContentPane().add(toAddress);
		toAddress.setBounds(40, 225, 235, 21);
		
		registerBtn.setText("Register");
		registerBtn.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent evt) {
				registerBtnActionPerformed(evt);
			}

		});
		getContentPane().add(registerBtn);
		registerBtn.setBounds(100, 255, 75, 25);
		
		sendBtn.setText("Send");
		sendBtn.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent evt) {
				sendBtnActionPerformed(evt);
			}

		});
		getContentPane().add(sendBtn);
		sendBtn.setBounds(200, 255, 75, 25);
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		setBounds((screenSize.width - 288) / 2, (screenSize.height - 310) / 2, 288, 320);
	}

	private void registerBtnActionPerformed(ActionEvent evt) {
		try {
			String to = this.toAddress.getText();
			String message = this.sendMessages.getText();
			sipLayer.sendMessage(to, message);

		} catch (Throwable e) {

			e.printStackTrace();
			this.receivedMessages.append("ERROR sending message: " + e.getMessage() + "\n");
		}
	}

	private void sendBtnActionPerformed(ActionEvent evt) {
		try {
			String to = this.toAddress.getText();
			String message = this.sendMessages.getText();
			sipLayer.sendMessage(to, message);

		} catch (Throwable e) {

			e.printStackTrace();
			this.receivedMessages.append("ERROR sending message: " + e.getMessage() + "\n");
		}
	}

	public void processMessage(String sender, String message) {
		this.receivedMessages.append("From " + sender + ": " + message + "\n");
	}

	public void processError(String errorMessage) {
		this.receivedMessages.append("ERROR: " + errorMessage + "\n");
	}

	public void processInfo(String infoMessage) {
		this.receivedMessages.append(infoMessage + "\n");
	}
}
