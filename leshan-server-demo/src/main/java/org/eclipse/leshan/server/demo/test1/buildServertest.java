package org.eclipse.leshan.server.demo.test1;
import java.util.Collection;

import org.eclipse.leshan.core.node.LwM2mResource;
import org.eclipse.leshan.core.observation.Observation;
import org.eclipse.leshan.core.request.ReadRequest;
import org.eclipse.leshan.core.response.ReadResponse;
import org.eclipse.leshan.server.californium.LeshanServer;
import org.eclipse.leshan.server.californium.LeshanServerBuilder;
import org.eclipse.leshan.server.registration.Registration;
import org.eclipse.leshan.server.registration.RegistrationListener;
import org.eclipse.leshan.server.registration.RegistrationUpdate;

import com.sun.corba.se.spi.activation.Server;
public class buildServertest {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		LeshanServerBuilder builder = new LeshanServerBuilder();
		final LeshanServer server = builder.build();
		server.start();
		
		server.getRegistrationService().addListener(new RegistrationListener() {
			
			@Override
			public void updated(RegistrationUpdate update, Registration updatedReg, Registration previousReg) {
				// TODO Auto-generated method stub
				System.out.println("device is still here: " + updatedReg.getEndpoint());
			}
			
			@Override
			public void unregistered(Registration registration, Collection<Observation> observations, boolean expired,
					Registration newReg) {
				// TODO Auto-generated method stub
				System.out.println("device left: " + registration.getEndpoint());
			}
			
			@Override
			public void registered(Registration registration, Registration previousReg,
					Collection<Observation> previousObsersations) {
				// TODO Auto-generated method stub
				System.out.println("new device: " + registration.getEndpoint());
				
				
				try {
					ReadResponse response = server.send(registration, new ReadRequest(3,0,13));
					if(response.isSuccess()) {
						System.out.println("Device time: " + ((LwM2mResource)response.getContent()).getValue());
					}
					else {
						System.out.println("Failed to read: " + response.getCode() + " " + response.getErrorMessage());
					}
				} catch (InterruptedException e) {
					e.printStackTrace();
					// TODO: handle exception
				}
			}
		});
	}
	
}
