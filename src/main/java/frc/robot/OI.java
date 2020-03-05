package frc.robot;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.helpers.ControllerWrapper;
import frc.robot.helpers.Helper;

/**
 * The class in which we map our driver/operator input to specific tasks on the robot
 * Init should be called once in the robotInit() method in the Robot class
 * Update should be called either in robotPeriodic() or teleopPeriodic()
 * @author hrl
 */

public class OI {
    public static ControllerWrapper driver = new ControllerWrapper(RobotMap.Controllers.DRIVER_PORT, true);
    public static ControllerWrapper operator = new ControllerWrapper(RobotMap.Controllers.OPERATOR_PORT, true);
    public static boolean armReset = false;
    public static boolean engaged = false;

    public static void init() {
        
    }

    public static void update() {
        // HUGE MEGA TODO: figure out controls with driver and operator
        // GENERAL CONTROLS/CONTROL METHODS
        Robot.drivetrain.arcadeDrive(1);
        Robot.turret.manualTurret();
        //TODO: change manual turret to joystick

        // DRIVER CONTROLS

        // OPERATOR CONTROLS

        // shoot
        operator.bA.whileHeld(() -> Robot.shooter.setSpeed(-0.6));
        operator.bSTART.whileHeld(() -> Robot.hopper.forward());
        operator.bSTART.whenReleased(() -> {
            Robot.hopper.stop();
            Robot.hopper.resetOverride();
        });

        // intake
        operator.bLB.whenPressed(() -> Robot.intake.toggle());
        /*operator.bRB.whileHeld(() -> {
            Robot.hopper.update();
            Robot.intake.setSpeed(1);
        });
        operator.bRB.whenReleased(() -> {
            Robot.hopper.stop();
            Robot.intake.setSpeed(0);
        });*/

        Robot.hopper.update(Helper.analogToDigital(operator.getRT(), .1, .6));
        Robot.intake.setSpeed(Helper.analogToDigital(operator.getRT(), .1, 1));
        Robot.hopper.reverse(Helper.analogToDigital(operator.getLT(), .1, .6));
        Robot.intake.setSpeed(Helper.analogToDigital(operator.getLT(), .1, -1));

        // hopper
        operator.bB.whileHeld(() -> Robot.hopper.reverse(.6));
        operator.bB.whenReleased(() -> Robot.hopper.stop());

        // WOF
        operator.dN.whenPressed(() -> Robot.wheel.actuatorUp());
        operator.dS.whenPressed(() -> Robot.wheel.actuatorDown());

        // if it hasn't already been handled...
        driver.updateStates();
        operator.updateStates();

        // climber
        // operator.bX.whileHeld(() -> Robot.climber.testWinch());
        // operator.bX.whenReleased(() -> Robot.climber.testWinch());

        //only for testing - pt 1
        driver.bX.whenPressed(() -> Robot.climber.engageBrake());
        driver.bY.whenPressed(() -> Robot.climber.disengageBrake());
        driver.bA.whileHeld(() -> Robot.climber.testWinch());

        // pt 2 testing
        driver.bA.whileHeld(() -> {
            Robot.climber.setEngaged(false);
            Robot.climber.testWinch();
        });
        driver.bA.whenReleased(() -> {
            Robot.climber.setEngaged(true);
            Robot.climber.disengageBrake();
        });

        //pt 3 testing
        driver.bA.whenPressed(() -> {
            Robot.climber.disengageBrake();
            Robot.climber.moveArmUp(0, 0); //TODO: set parameters
            Robot.climber.engageBrake();
        });




        updateSD();

        if (armReset == false) {
            Robot.climber.resetArmPosition();;
            armReset = true;
        }

        
    }

    public static void updateSD() {
        SmartDashboard.putString("WoF Color", Robot.wheel.toString());
        SmartDashboard.putBoolean("Left limit", Robot.turret.getLeftLimit()); // TODO: for testing
        SmartDashboard.putBoolean("Right limit", Robot.turret.getRightLimit()); // TODO: for testing
        SmartDashboard.putNumber("Turret pos", Robot.turret.getPracticeEncPosition()); // TODO: for testing
        SmartDashboard.putBoolean("Bot Sensor", Robot.hopper.getBotSensor());
        SmartDashboard.putBoolean("Mid Sensor", Robot.hopper.getMidLimit());
        SmartDashboard.putBoolean("Top Sensor", Robot.hopper.getTopLimit());
        SmartDashboard.putNumber("CL_deploy", Robot.climber.getArmPosition());
    }
}
