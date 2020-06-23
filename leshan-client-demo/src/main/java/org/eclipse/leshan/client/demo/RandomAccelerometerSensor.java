package org.eclipse.leshan.client.demo;
import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.print.attribute.standard.Severity;

import org.eclipse.leshan.client.resource.BaseInstanceEnabler;
import org.eclipse.leshan.client.servers.ServerIdentity;
import org.eclipse.leshan.core.response.ExecuteResponse;
import org.eclipse.leshan.core.response.ReadResponse;
import org.eclipse.leshan.core.util.NamedThreadFactory;

public class RandomAccelerometerSensor extends BaseInstanceEnabler {

    private static final String UNIT_ACCELERATION = "m/s^2";
    private static final int UNITS = 5701;
    private static final int X_VALUE = 5702;
    private static final int Y_VALUE = 5703;
    private static final int Z_VALUE = 5704;
    private final Random r = new Random();
    private final ScheduledExecutorService scheduler;

    public RandomAccelerometerSensor() {
        this.scheduler = Executors.newSingleThreadScheduledExecutor(new NamedThreadFactory("Accelerometer Sensor"));
        // This method sets up a ScheduledExecutorService to execute every 10 seconds
        scheduler.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                adjustReadings();
            }
        }, 1, 1, TimeUnit.SECONDS);
    }

    @Override
    public synchronized ReadResponse read(ServerIdentity identity,int resourceId) {
        switch (resourceId) {
        case X_VALUE:
            return ReadResponse.success(resourceId, getXValue());
        case Y_VALUE:
            return ReadResponse.success(resourceId, getYValue());
        case Z_VALUE:
            return ReadResponse.success(resourceId, getZValue());
        case UNITS:
            return ReadResponse.success(resourceId, UNIT_ACCELERATION);
        default:
            return super.read(identity,resourceId);
        }
    }

    @Override
    public synchronized ExecuteResponse execute(ServerIdentity identity,int resourceId, String params) {
        switch (resourceId) {
        case X_VALUE:
            getXValue();
            return ExecuteResponse.success();
        case Y_VALUE:
            getYValue();
            return ExecuteResponse.success();
        case Z_VALUE:
            getZValue();
            return ExecuteResponse.success();
        default:
            return super.execute(identity,resourceId, params);
        }
    }

    private Float getZValue() {
        return r.nextFloat() * 100;
    }

    private Float getYValue() {
        return r.nextFloat() * 100;
    }

    private Float getXValue() {
        return r.nextFloat() * 100;
    }

    private synchronized void adjustReadings() {
        fireResourcesChange(X_VALUE, Y_VALUE, Z_VALUE);

    }
}