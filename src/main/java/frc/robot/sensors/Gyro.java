/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.sensors;

import com.kauailabs.navx.frc.AHRS;

import edu.wpi.first.wpilibj.Timer;
import frc.robot.RobotMap;

/**
 * This class represents the main Gyro for the robot. It was made a singleton to
 * avoid multiple instantiations of the same sensor.
 * 
 * @author acr, hrl
 */
public class Gyro {
    private static Gyro instance = null; // Instance of Gyro class for getInstance
    private AHRS gyro; // Instance of the NavX, used for Gyro reporting
    private AHRS accelerometer;

    private double prevlinearAccelX;
    private double prevlinearAccelY;
    private double currentlinearAccelX;
    private double currentlinearAccelY;

    final static double COLLISION_THRESHOLD = 0.2d; // TODO: Collision threshold is currently extremely low; will have
                                                    // to change

    /**
     * This is the constructor for our Gyro class. It is private because this is a
     * singleton, and it should never be instantiated outside of the getInstance()
     * method.
     */
    private Gyro() {
        gyro = new AHRS(RobotMap.Sensors.GYRO);

        try {
            accelerometer = new AHRS(RobotMap.CollisionDetection.NAVX);
        } catch (RuntimeException e) {
            System.out.println("Oopsie daisy");
        }
    }

    /**
     * Checks to see if the instance of this class has already been created. If so,
     * return it. If not, create it and return it.
     */
    public static Gyro getInstance() {
        if (instance == null) {
            instance = new Gyro();
        }
        return instance;
    }

    /**
     * Get the current gyro angle, in degrees.
     * 
     * @return the yaw angle
     */
    public double getAngle() {
        /*
         * Note: If we need to manipulate the data from the gyro before returning it for
         * use, we can do it here.
         */

        return gyro.getAngle();
    }

    /**
     * Gets the yaw, or horizontal rotation of the robot.
     * 
     * @return the yaw value
     */
    public double getYaw() {
        return gyro.getYaw();
    }

    /**
     * Returns the pitch, or vertical rotation of the robot (think tip).
     * 
     * @return the pitch value
     */
    public double getPitch() {
        return gyro.getPitch();
    }

    /**
     * Returns the roll, or left/right tip.
     * 
     * @return the roll value
     */
    public double getRoll() {
        return gyro.getRoll();
    }

    /**
     * Returns the status of NavX calibration.
     * 
     * @return true if calibrating, false if not
     */
    public boolean isCalibrating() {
        return gyro.isCalibrating();
    }

    /**
     * Reset the gyro to zero degrees.
     */
    public void reset() {
        gyro.reset();
    }

    /**
     * Gets jerk for x and y and checks if either are above collision threshold
     * 
     * @return a boolean that states whether there was a collision or not
     */
    public boolean detectCollision() {
        boolean collisionDetected = false;

        currentlinearAccelX = accelerometer.getWorldLinearAccelX();
        double currentJerkX = currentlinearAccelX - prevlinearAccelX;
        prevlinearAccelX = currentlinearAccelX;

        currentlinearAccelY = accelerometer.getWorldLinearAccelY();
        double currentJerkY = currentlinearAccelY - prevlinearAccelY;
        prevlinearAccelY = currentlinearAccelY;

        if ((Math.abs(currentJerkX) > COLLISION_THRESHOLD) || (Math.abs(currentJerkY) > COLLISION_THRESHOLD)) {
            collisionDetected = true;
        }

        return collisionDetected;
    }

    /**
     * Should stop all of the motors if a collision was detected
     */
    public void stopMotors() {
        if (detectCollision()) {
            // l_primary.set(0.0);
            // r_primary.set(0.0);
            // l_secondary.set(0.0);
            // r_secondary.set(0.0);
            // System.out.println("collision detected");
            Timer.delay(0.5);
        }
    }

    /**
     * Print the gyro's current angle, for use on the SmartDashboard.
     */
    @Override
    public String toString() {
        String toString = "" + this.getAngle();
        return toString;
    }
}