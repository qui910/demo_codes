/**
 * @(#)MessageProcessor.java   2013-8-9
 * Copyright 2013  it.kedacom.com, Inc. All rights reserved.
 */
package com.duoduo.demo.jsip;

/** 
 *  
 * @author  chengesheng@gmail.com 
 * @date    2013-8-8 下午11:12:59 
 * @note    MessageProcessor 
 */  
public interface MessageProcessor {  
    public void processMessage(String sender, String message);  
  
    public void processError(String errorMessage);  
  
    public void processInfo(String infoMessage);  
} 