/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.sensors;

import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.networktables.NetworkTableInstance;

import frc.robot.RobotMap;

/**
 * Controls the Limelight camera connected to the robot over NetworkTables.
 * 
 * @author hrl
 */
public class Camera {
    private static Camera instance = null;

    private NetworkTable table;
    private NetworkTableEntry tEnabled, tArea, tXpos, tAngle;

    private Camera() {
        table = NetworkTableInstance.getDefault().getTable("limelight");
        this.update(); // initial run

        // be extra sure about pipeline
        table.getEntry("pipeline").setDouble(RobotMap.Camera.PIPELINE);
    }

    /**
     * Get the singleton instance of the Camera class.
     */
    public static Camera getInstance() {
        if (instance == null) {
            instance = new Camera();
        }
        return instance;
    }

    /**
     * A standard method for updating a subsystem or sensor.
     * Call in robotPeriodic().
     * 
     * This updates the various state variables used for followVision.
     */
    public void update() {
        tEnabled = table.getEntry("tv"); // target enabled?
        tArea = table.getEntry("ta"); // target area
        tXpos = table.getEntry("tx"); // target x-position (y unused)
        tAngle = table.getEntry("ts"); // target angle/"skew"
    }

    /**
     * Determines whether the target has been found.
     * @return boolean representing target detection status
     */
    public boolean hasTarget() {
        return tEnabled.getDouble(0.0) == 1;
    }

    /**
     * Returns the area of the target, if detected.
     * @return 0.0 if no target, area otherwise
     */
    public double getArea() {
        return tArea.getDouble(0.0);
    }

    /**
     * Get the x-position of the target, if detected.
     * @return 0.0 if no target, x-position otherwise
     */
    public double getPosition() {
        return tXpos.getDouble(0.0);
    }

    /**
     * Get the angle (with respect to the horizontal axis) of the target, if detected.
     * @return 0.0 if no target, x-angle otherwise
     */
    public double getAngle() {
        double angle = tAngle.getDouble(0.0);
        if (angle < -45) {
            angle += 90;
        }
        return angle;
    }

    /**
     * Print the position, angle, and target status, for use on the SmartDashboard.
     * @return "Position,Angle,TargetFound"
     */
    @Override
    public String toString() {
        String toString = "" + this.getPosition() + "," + this.getAngle() + "," + this.hasTarget();
        return toString;
    }
}
