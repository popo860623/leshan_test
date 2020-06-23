package org.eclipse.leshan.client.text1;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.security.Security;
import java.util.*;
import org.eclipse.leshan.client.californium.LeshanClient;
import org.eclipse.leshan.client.californium.LeshanClientBuilder;
import org.eclipse.leshan.client.object.Device;
import org.eclipse.leshan.client.resource.ObjectsInitializer;
import org.eclipse.leshan.core.LwM2m;
import org.eclipse.leshan.core.LwM2mId;
import org.eclipse.leshan.core.request.BindingMode;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import com.sun.org.apache.xerces.internal.impl.dv.util.Base64;
import com.sun.security.ntlm.Server;
import sun.net.www.http.HttpClient;
import sun.security.krb5.internal.APRep;
import sun.security.util.Password;

import org.jdom.*;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;
import org.omg.CORBA.DataOutputStream;
public class buildClienttext {

	
	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		String endpoint = "my-test";
		LeshanClientBuilder clientBuilder = new LeshanClientBuilder(endpoint);
		LeshanClient client = clientBuilder.build();
		client.start();
		
		// create objects
//		ObjectsInitializer initializer = new ObjectsInitializer();
//		initializer.setInstancesForObject(LwM2mId.SECURITY, Security.noSec("coap://leshan.eclipseprojects.io:5683", 12345));
//		initializer.setInstancesForObject(LwM2mId.SERVER, new Server(12345, 5 * 60, BindingMode.U, false));
//		initializer.setInstancesForObject(LwM2mId.DEVICE, new Device("Eclipse Leshan", "model12345", "12345", "U"));
		String resourceName = "test6";
		Namespace m2m = Namespace.getNamespace("m2m","http://www.onem2m.org/xml/protocols");
		Document document = new Document();
		
		Element ae = new Element("ae",m2m);		
		document.setRootElement(ae);
		
		Element api = new Element("api");
		Element lbl = new Element("lbl");
		Element rr = new Element("rr");
		ae.addNamespaceDeclaration(m2m);
		ae.setAttribute("rn",resourceName);
		api.addContent("app-sensor");
		lbl.addContent("Type/sensor Category/temperature Location/home");
		rr.addContent("false");
		
		ae.addContent(api);
		ae.addContent(lbl);
		ae.addContent(rr);
		XMLOutputter outputter = new XMLOutputter(Format.getPrettyFormat());
		send_request(outputter.outputString(ae));
	}
	public static void send_request(String data) {
		try {
		URL url = new URL("http://127.0.0.1:8282/~/mn-cse");
		HttpURLConnection connection = (HttpURLConnection) url.openConnection();
		
		connection.setRequestMethod("POST");
		connection.setDoOutput(true);
		connection.setDoInput(true);
		connection.setRequestProperty("Content-Type", "application/xml;ty=2");
		connection.setRequestProperty("Authorization", "Basic " + Base64.encode("admin:admin".getBytes()));
		connection.setUseCaches(false);		
		OutputStreamWriter outputStreamWriter = new OutputStreamWriter(connection.getOutputStream());		
//		System.out.println(data);
		outputStreamWriter.write(data);
		outputStreamWriter.flush();
		outputStreamWriter.close();
		connection.connect();
		
		System.out.println(connection.getResponseCode());
		
		if(connection.getResponseCode() == 201) { //201 表資源成功被創建
			System.out.println("Connect Success.");
			InputStream inputStream = connection.getInputStream();
			String reString = null;
			try {
				byte[] data1 = new byte[inputStream.available()];
				inputStream.read(data1);
				reString=new String(data1);
				System.out.println(reString);
			}catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}
		}
		else {
			System.out.println("Connect Failed.");
		}
		
		} catch (MalformedURLException e) {
			// TODO: handle exception
			e.printStackTrace();
		}catch (UnsupportedEncodingException e) {
			// TODO: handle exception
			e.printStackTrace();
		}catch (IOException e) {
			e.printStackTrace();
			// TODO: handle exception
		}
	}

}
