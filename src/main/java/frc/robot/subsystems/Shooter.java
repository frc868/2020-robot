/*----------------------------------------------------------------------------*/
/* Copyright (c) 2020 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/
package frc.robot.subsystems;

import com.revrobotics.CANPIDController;
import com.revrobotics.CANSparkMax;
import com.revrobotics.ControlType;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import frc.robot.Robot;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.RobotMap;
import frc.robot.helpers.Helper;

/**
 * The shooter subsystem consists of the two-neo shooter mounted on the robot's
 * turret. It is controlled with REV's PID Controller on the SparkMAXes.
 *
 * @author dri
 */
public class Shooter {
    private static Shooter instance = null;

    private CANSparkMax primary;
    private CANSparkMax secondary;

    private CANPIDController pid;

    private double kP, kD, kFF, kI, kIa;

    private double setpoint = RobotMap.Shooter.SHOOTER_DEFAULT_SPEED;

    private Shooter() {
        primary = new CANSparkMax(RobotMap.Shooter.PRIMARY, MotorType.kBrushless);
        secondary = new CANSparkMax(RobotMap.Shooter.SECONDARY, MotorType.kBrushless);
        
        primary.restoreFactoryDefaults();
        secondary.restoreFactoryDefaults();

        primary.setInverted(RobotMap.Shooter.PRIMARY_IS_INVERTED);
        secondary.follow(primary, RobotMap.Shooter.SECONDARY_IS_OPPOSITE);
        
        pid = primary.getPIDController();

        // SmartDashboard.putNumber("kP", 0);
        // SmartDashboard.putNumber("kI", 0);
        // SmartDashboard.putNumber("kD", 0);
        // SmartDashboard.putNumber("kFF", 0);
        // SmartDashboard.putNumber("kIa", 0);
        // SmartDashboard.putNumber("Setpoint", 0);
    }

    /**
     * Returns the instance of the Shooter class
     * @return instance of shooter
     */
    public static Shooter getInstance() {
        if (instance == null) {
            instance = new Shooter();
        }
        return instance;
    }

    /**
     * Sets the PID gains and setpoint for the PID controller.
     */
    public void init() {
        // kP = SmartDashboard.getNumber("kP", 0);
        // kI = SmartDashboard.getNumber("kI", 0);
        // kD = SmartDashboard.getNumber("kD", 0);
        // kFF = SmartDashboard.getNumber("kFF", 0);
        // kIa = SmartDashboard.getNumber("kIa", 0);
        // setpoint = SmartDashboard.getNumber("Setpoint", RobotMap.Shooter.SHOOTER_DEFAULT_SPEED);

        // pid.setP(kP/1000);
        // pid.setI(kI/1000);
        // pid.setD(kD/1000);
        // pid.setFF(kFF/1000);
        // pid.setIMaxAccum(kIa, 0);

        kP = 0.2/1000;
        kI = 0.00001/1000;
        kD = 0.03/1000;
        kFF = 0.175/1000;
        kIa = 2;

        if (this.kI == 0) {
            pid.setIAccum(0);
        }
        
        pid.setOutputRange(0, 1);
    }

    /**
     * sets the output of the PID loop to the setpoint
     */
    public void update(double rpm) {
        this.setpoint = rpm;
        pid.setReference(setpoint, ControlType.kVelocity);
        SmartDashboard.putNumber("Output", primary.getEncoder().getVelocity());
        SmartDashboard.putNumber("Motor output", primary.get());
    }

    /**
     * runs the shooter at the current RPM
     */
    public void update() {
        this.update(this.setpoint);
    }

    /**
     * Checks whether the shooter is within a range of its target RPM.
     * @return is shooter at target
     */
    public boolean atTarget() {
        return Helper.tolerance(
            primary.getEncoder().getVelocity(),
            this.setpoint,
            0.01);
    }

    /**
     * Manually sets the speed of the motors.
     * @param speed the speed from -1 to 1
     */
    public void setSpeed(double speed) {
        primary.set(speed);
    }

    /**
     * Retrieves the RPM of the shooter.
     */
    public double getRPM() {
        return primary.getEncoder().getVelocity();
    }

    /**
     * Stops the shooter.
     */
    public void stop() {
        primary.stopMotor();
        secondary.stopMotor();
    }

    /**
     * Shoots until all balls are cleared from the hopper.
     * Useful in autonomous.
     * TODO: this should have checking as to the hopper state, but that logic doesn't exist yet
     * @param rpm the RPM to run the shooter at
     * @author hrl
     */
    public void shootUntilClear(double rpm) {
        Robot.hopper.forward(this.atTarget());
        this.setpoint = rpm;
        this.update();
    }
}
