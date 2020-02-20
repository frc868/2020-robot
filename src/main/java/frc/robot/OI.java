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
        driver.bRB.whileHeld(() -> Robot.intake.setSpeed(0.7));
        driver.bRB.whenReleased(() -> Robot.intake.setSpeed(0));

        driver.bRB.whileHeld(() -> Robot.hopper.shoot());
        driver.bRB.whenReleased(() -> Robot.hopper.stop());

        driver.bA.whileHeld(() -> Robot.shooter.setSpeed(0.7));
        driver.bA.whenReleased(() -> Robot.shooter.setSpeed(0));

        driver.bA.whileHeld(() -> Robot.hopper.shoot());
        driver.bA.whenReleased(() -> Robot.hopper.stop());

        

        updateSD();
    }

    public static void updateSD() {
        SmartDashboard.putString("WoF Color", Robot.wheel.toString());
    }
}
