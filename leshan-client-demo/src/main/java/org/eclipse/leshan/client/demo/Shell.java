package org.eclipse.leshan.client.demo;

import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Properties;

import javax.sql.rowset.serial.SerialArray;

import org.slf4j.Logger;
import com.jcraft.*;
import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.ChannelShell;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.UserInfo;

public class Shell {
	private static final Logger LOG = LoggerFactory.getLogger(Shell.class);
	private static final String USER = "root";
	private static final String PASSWORD = "0000";
	private static final String HOST = "140.116.234.130";
	private static final int DEFAULT_SSH_PORT = 22;

	private static String resultString = ""; // 回傳值要放的String

	public void SSH_Setting(String host, String username, String password, int port) throws Exception,JSchException {
		LOG.info("嘗試連接到.... host:" + host + " ,username:" + username, " ,password:" + password + " ,port:" + port);
		try {
			JSch jSch = new JSch();
			Properties config = new Properties();
			Session session = jSch.getSession(username, host, port);

			session = jSch.getSession(username, host, port);
			session.setPassword(password);
			config.put("StrictHostKeyChecking", "no");
			session.setConfig(config);
			session.setTimeout(30000); // 设置timeout时间
			session.connect(); // 通过Session建立链接
			ChannelExec channel = (ChannelExec) session.openChannel("exec");
			channel.setCommand("echo '123'");
			channel.setErrStream(System.err);
			
			InputStream inputStream = channel.getInputStream();					
			channel.connect();
			System.out.println("Connected.");
			
			// 接收遠端伺服器執行命令的結果
			byte[] tmp = new byte[1024];
			while (true) {
				while (inputStream.available() > 0) {
					int i = inputStream.read(tmp, 0, 1024);
					if (i < 0)
						break;
					System.out.print(new String(tmp, 0, i));
					resultString = resultString + (new String(tmp, 0, i));
				}
				if (channel.isClosed()) {
					System.out.println("Exit Status: " + channel.getExitStatus());
					break;
				}
			}
			channel.disconnect();
			session.disconnect();
			System.out.println("DONE!!!");
		} catch (JSchException e) {
			System.out.println(e);
		}
		
		System.out.println("result = " + resultString);
	}
}
