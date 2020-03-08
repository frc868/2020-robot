package frc.robot;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.helpers.ControllerWrapper;

/**
 * The class in which we map our driver/operator input to specific tasks on the
 * robot Init should be called once in the robotInit() method in the Robot class
 * Update should be called either in robotPeriodic() or teleopPeriodic()
 * 
 * @author hrl
 */
public class OI {

    public static ControllerWrapper driver = new ControllerWrapper(RobotMap.Controllers.DRIVER_PORT, true);
    public static ControllerWrapper operator = new ControllerWrapper(RobotMap.Controllers.OPERATOR_PORT, true);

    public static void init() {
        initSD();
        Robot.shooter.init();
    }

    public static void update() {

        // GENERAL CONTROLS/CONTROL METHODS
        if (driver.isAltMode()) {
            Robot.drivetrain.arcadeDrive(0.4); // driver LY and RX
        } else {
            Robot.drivetrain.arcadeDrive(1);
        }
        Robot.turret.manualTurret(); // operator LY
        // ----------------------------------------------------------------------------------------

        // ALT MODE TOGGLES
        driver.bMENU.whenPressed(() -> {
            driver.toggleAltMode();
            operator.toggleAltMode();
        });

        // disable alt mode on operator controller
        // operator.bMENU.whenPressed(() -> {
        //     driver.toggleAltMode();
        //     operator.toggleAltMode();
        // });
        // ----------------------------------------------------------------------------------------



        // ============ EVERY MODE CONTROLS ===============

        // CLIMBER
        driver.bMENU.whenReleased(() -> {
            Robot.climber.stopWinch();
            Robot.climber.engageBrake();
        });
        operator.bMENU.whenReleased(() -> {
            Robot.climber.stopWinch();
            Robot.climber.engageBrake();
        });        
        

        // HOPPER
        driver.bB.whileHeld(() -> Robot.hopper.reverse(.6));
        driver.bB.whenReleased(Robot.hopper::stop);
        
        // TURRET
        // TODO: check if we want this back in
        operator.bLB.whileHeld(Robot.turret::manualTurret); // operator LX
        operator.bLB.whenReleased(Robot.turret::stop);

        operator.bRB.whileHeld(Robot.turret::trackVision);
        operator.bRB.whenReleased(Robot.turret::stop);
        driver.bRB.whileHeld(Robot.turret::trackVision);
        driver.bRB.whenReleased(Robot.turret::stop);

        // SHOOTER
        operator.bSTART.whileHeld(() -> {
            Robot.shooter.update(RobotMap.Shooter.TRENCH_FRONT_SPD);
            Robot.hopper.forward(Robot.shooter.atTarget());
        });
        operator.bSTART.whenReleased(() -> {
            Robot.shooter.stop();
            Robot.hopper.stop();
        });

        driver.bSTART.whileHeld(() -> {
            Robot.shooter.update(RobotMap.Shooter.TRENCH_FRONT_SPD);
            Robot.hopper.forward(Robot.shooter.atTarget());
        });
        driver.bSTART.whenReleased(() -> {
            Robot.shooter.stop();
            Robot.hopper.stop();
        });
        
        // ----------------------------------------------------------------------------------------


        // ==================== ALT MODE CONTROLS ======================

        // OPERATOR

        if (operator.isAltMode()) {

            // ======= CLIMBER ========

            // ARM DEPLOY        
            operator.bY.whenPressed(() -> Robot.climber.deployHook());
            operator.bY.whenReleased(() -> Robot.climber.stopArm());

            operator.bA.whenPressed(() -> {
                Robot.climber.setSpeedArm(-0.3);
            });
            operator.bA.whenReleased(() -> Robot.climber.stopArm());

            // WINCHING
            operator.dN.whenPressed(() -> {
                Robot.climber.disengageBrake();
                Robot.climber.setSpeedWinch(0.5);
            });
            operator.dN.whenReleased(() -> {
                Robot.climber.stopWinch();
                Robot.climber.engageBrake();
            });
            operator.dS.whenPressed(() -> {
                Robot.climber.disengageBrake();
                Robot.climber.setSpeedWinch(-0.5);
            });
            operator.dS.whenReleased(() -> {
                Robot.climber.stopWinch();
                Robot.climber.engageBrake();
            });

            // TURRET
            Robot.turret.stop();

            // operator.bA.whileHeld(() -> Robot.climber.manualArm(-0.3));
            // operator.bA.whenReleased(Robot.climber::stopArm);
            // ===========================================================
            // working manual arm
            // operator.bY.whileHeld(() -> Robot.climber.manualArm(0.3));
            // operator.bY.whenReleased(Robot.climber::stopArm);
            //
            // operator.trigLSTK.whileHeld(() -> {
            // Robot.climber.manualClimb(RobotMap.Climber.HOLD_POWER);
            // });
            // operator.trigRSTK.whileHeld(() -> {
            // Robot.climber.setSpeedArm(RobotMap.Climber.ARM_POWER);
            // });

            // operator.trigRSTK.whenReleased(() -> {
            // Robot.climber.stopArm();
            // });
            // operator.trigLSTK.whenReleased(() -> {
            // Robot.climber.stopWinch();
            // });

            // operator.trigLSTK.whenPressed(() -> {
            // if (operator.getLY() > 0) {
            // Robot.climber.disengageBrake();
            // Robot.climber.setSpeedWinch(0.3); // TODO Test value
            // } else if (operator.getLY() < 0) {
            // Robot.climber.disengageBrake();
            // Robot.climber.setSpeedWinch(-0.3);
            // } else {
            // Robot.climber.stopWinch();
            // Robot.climber.engageBrake();
            // }
            // });
            // operator.trigRSTK.whenPressed(() -> {
            // if (operator.getRY() > 0) {
            // Robot.climber.setSpeedArm(0.1);
            // } else if (operator.getRY() < 0) {
            // Robot.climber.setSpeedArm(-0.1);
            // } else {
            // Robot.climber.setSpeedArm(0);
            // }
            // });
            // TODO: check these 2020-03-07

            // auto mode
            // operator.dN.whenPressed(() -> {
            // Robot.climber.moveArmUp(0.1);
            // });
            // operator.dS.whenReleased(() -> {
            // Robot.climber.moveArmDown(-0.1);
            // Robot.climber.activateWinch();
            // });
        } else {

            // INTAKE
            operator.bY.whenPressed(Robot.intake::toggle);
            // -------------

            // SHOOTER
            operator.bA.whileHeld(() -> {
                Robot.shooter.update(Robot.camera.getCalculatedRPM());
                Robot.hopper.forward(Robot.shooter.atTarget());
            });
            operator.bA.whenReleased(() -> {
                Robot.shooter.stop();
                Robot.hopper.stop();
            });

            // INTAKE/HOPPER
            operator.bRT.whileHeld(() -> {
                Robot.hopper.update();
                Robot.intake.setSpeed(1);
            });
            operator.bRT.whenReleased(() -> {
                Robot.hopper.stop();
                Robot.intake.setSpeed(0);
            });
            operator.bLT.whileHeld(() -> {
                Robot.hopper.reverse(.6);
                Robot.intake.setSpeed(-1);
            });
            operator.bLT.whenReleased(() -> {
                Robot.hopper.stop();
                Robot.intake.setSpeed(0);
            });

            operator.bB.whileHeld(() -> Robot.hopper.reverse(.6));
            operator.bB.whenReleased(Robot.hopper::stop);
            // ---------------------------

            //TODO: TO DELETE!!!!!!!!!!!

            // operator.bRB.whileHeld(Robot.turret::trackVision);
            // operator.bRB.whenReleased(Robot.turret::stop);

            // // shoot
            // operator.bA.whileHeld(() -> Robot.shooter.setSpeed(0.6));
            // operator.bA.whenReleased(Robot.shooter::stop);
            // operator.bSTART.whileHeld(() ->
            // Robot.hopper.forward(Robot.shooter.atTarget()));
            // operator.bSTART.whenReleased(Robot.hopper::stop);
            // -----------------------------------------------------------------------------------            
        }

        // if it hasn't already been handled...
        driver.updateStates();
        operator.updateStates();

        // climber
        // operator.bX.whileHeld(() -> Robot.climber.testWinch());
        // operator.bX.whenReleased(() -> Robot.climber.testWinch());

        updateSD();
    }

    public static void initSD() {
        SmartDashboard.putBoolean("Bot Left Sensor", Robot.hopper.getBotLeftSensor());
        SmartDashboard.putBoolean("Bot Right Sensor", Robot.hopper.getBotRightSensor());
        SmartDashboard.putBoolean("Mid Sensor", Robot.hopper.getMidLimit());
        SmartDashboard.putBoolean("Top Sensor", Robot.hopper.getTopLimit());

        SmartDashboard.putBoolean("Shooter running?", Robot.shooter.getRPM() > 0);
        SmartDashboard.putBoolean("At target", Robot.shooter.atTarget());

        SmartDashboard.putBoolean("Alt Mode", operator.isAltMode());

        SmartDashboard.putNumber("Shooter RPM", Robot.shooter.getRPM());

    }

    public static void updateSD() {
        SmartDashboard.putBoolean("Bot Left Sensor", Robot.hopper.getBotLeftSensor());
        SmartDashboard.putBoolean("Bot Right Sensor", Robot.hopper.getBotRightSensor());
        SmartDashboard.putBoolean("Mid Sensor", Robot.hopper.getMidLimit());
        SmartDashboard.putBoolean("Top Sensor", Robot.hopper.getTopLimit());

        SmartDashboard.putBoolean("Shooter running?", Robot.shooter.getRPM() > 0);
        SmartDashboard.putBoolean("At target", Robot.shooter.atTarget());

        SmartDashboard.putBoolean("Alt Mode", operator.isAltMode());

        SmartDashboard.putNumber("Shooter RPM", Robot.shooter.getRPM());

        // =======================TROUBLESHOOTING========================
        
        // SmartDashboard.putNumber("Hopper count", Robot.hopper.getBallCount());

        SmartDashboard.putBoolean("Turret left", Robot.turret.getLeftLimit());
        SmartDashboard.putBoolean("Turret right", Robot.turret.getRightLimit());

        SmartDashboard.putNumber("Turret Speed", Robot.turret.getSpeed());
        // SmartDashboard.putNumber("Limelight X Pos", Robot.camera.getPosition());
        SmartDashboard.putNumber("Calculated RPM", Robot.camera.getCalculatedRPM());


        SmartDashboard.putNumber("CL_Winch", Robot.climber.getWinchPosition());
        SmartDashboard.putNumber("CL_Arm", Robot.climber.getArmPosition());
        SmartDashboard.putBoolean("CL_Sensor", Robot.climber.getArmDeploy());


        
        SmartDashboard.putBoolean("Operator Alt Mode", operator.isAltMode());

    }
}
