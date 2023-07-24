/**
 * @(#)AppUtil.java   2013-3-26
 * Copyright 2013  it.kedacom.com, Inc. All rights reserved.
 */
package com.duoduo.demo.jsip.util;


/**
 * Java应用程序帮助类
 * @author chengesheng@kedacom.com
 * @date 2013-3-26 下午4:57:19
 */
public class AppUtil {


	// 获取输入信息
	public static String getCommand() {
		return getCommand("请输入命令", 1);
	}

	public static String getCommand(String message) {
		return getCommand(message, null, 100);
	}

	public static String getCommand(String message, int limit) {
		return getCommand(message, null, limit);
	}

	public static String getCommand(String message, String defaultValue) {
		return getCommand(message, defaultValue, 100);
	}

	public static String getCommand(String message, String defaultValue, int limit) {
		String strCommand = "";
		try {
			do {
				System.out.println();
				if (defaultValue == null) {
					System.out.print(message + ": ");
				} else {
					System.out.print(message + " [" + defaultValue + "]: ");
				}

				byte[] command = new byte[100];
				System.in.read(command);
				strCommand = new String(command);
				strCommand = strCommand.replaceAll("\r\n", "").trim();
				// 若存在默认值且直接输入回车，则直接使用默认值作为返回值
				if (defaultValue != null && "".equals(strCommand)) {
					strCommand = defaultValue;
				}
			} while (strCommand.length() > limit);
		} catch (Exception e) {
			System.out.println("输入错误！！！");
		}
		return strCommand;
	}
}

