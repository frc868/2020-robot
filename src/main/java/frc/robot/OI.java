package frc.robot;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.helpers.ControllerWrapper;
import frc.robot.Robot;

/**
 * The class in which we map our driver/operator input to specific tasks on the robot
 * Init should be called once in the robotInit() method in the Robot class
 * Update should be called either in robotPeriodic() or teleopPeriodic()
 * @author hrl
 */
public class OI {
    public static ControllerWrapper driver = new ControllerWrapper(RobotMap.Controllers.DRIVER_PORT, true);
    public static ControllerWrapper operator = new ControllerWrapper(RobotMap.Controllers.OPERATOR_PORT, true);

    public static void init() {

    }

    public static void update() {
        // HUGE MEGA TODO: figure out controls with driver and operator
        // GENERAL CONTROLS/CONTROL METHODS
        Robot.drivetrain.arcadeDrive(1);

        // DRIVER CONTROLS
        // driver.bRB.whileHeld(() -> Robot.intake.setSpeed(0.7));
        // driver.bRB.whenReleased(() -> Robot.intake.setSpeed(0));

        
        driver.bRB.whenPressed(() -> {
            Robot.hopper.shoot();
        });

        driver.bLB.whenPressed(() -> {
            Robot.hopper.stop();
        });

        driver.bX.whenPressed(() -> Robot.intake.setSpeed(1));
        driver.bY.whenPressed(() -> Robot.intake.setSpeed(0));

        driver.bA.whenPressed(() -> Robot.shooter.setSpeed(0.85));
        driver.bB.whenPressed(() -> Robot.shooter.setSpeed(0));

        Robot.wheel.setSpeed(driver.getLT());

        driver.dN.whenPressed(() -> Robot.wheel.actuatorUp());
        driver.dS.whenPressed(() -> Robot.wheel.actuatorDown());

        driver.bMENU.whenPressed(() -> Robot.intake.actuatorDown());
        driver.bSTART.whenPressed(() -> Robot.intake.actuatorUp());
        //driver.bA.whenReleased(() -> Robot.intake.setSpeed(0));

        // driver.bA.whileHeld(() -> {
        //     Robot.shooter.setSpeed(0.7);
        //     Robot.hopper.shoot();
        // });
        // driver.bA.whenReleased(() -> {
        //     Robot.shooter.setSpeed(0);
        //     Robot.hopper.stop();
        // });

        updateSD();
    }

    public static void updateSD() {
        SmartDashboard.putString("WoF Color", Robot.wheel.toString());
    }
}
