// This code is a full code to test the robot moving
package org.firstinspires.ftc.QCTruongCode.TestingFolder;

// package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;


@TeleOp(name = "final move Test")
public class FinishedMoveTest extends OpMode {

    DcMotorEx RightFront;
    DcMotorEx RightBack;
    DcMotorEx LeftFront;
    DcMotorEx LeftBack;

    double ticks = 537.7;
    double newTarget = 0;
    double rpm = 312;          // current speed setting, adjustable live
    double rpmStep = 10;

    double ticksPerSecond = (rpm / 60.0) * ticks;

    boolean lastA = false;
    boolean lastB = false;
    boolean lastUp = false;
    boolean lastDown = false;

    double RBspeed;
    double RFspeed;
    double LBspeed;
    double LFspeed;
    
    @Override
    public void init() {
        RightFront = hardwareMap.get(DcMotorEx.class, "Right Front");
        RightFront.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        RightFront.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        
        RightBack = hardwareMap.get(DcMotorEx.class, "Right Back");
        RightBack.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        RightBack.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        
        LeftFront = hardwareMap.get(DcMotorEx.class, "Left Front");
        LeftFront.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        LeftFront.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        
        LeftBack = hardwareMap.get(DcMotorEx.class, "Left Back");
        LeftBack.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        LeftBack.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        
        telemetry.addData("Hardware: ", "Initialized");
        telemetry.update();
    }

   @Override
public void loop() {
    boolean currentUp = gamepad2.dpad_up;
    boolean currentDown = gamepad2.dpad_down;

    if (currentUp && !lastUp) rpm += rpmStep;
    if (currentDown && !lastDown) rpm = Math.max(0, rpm - rpmStep);
    lastUp = currentUp;
    lastDown = currentDown;

    ticksPerSecond = (rpm / 60.0) * ticks;

    double leftY = -gamepad2.left_stick_y;
    double leftX = gamepad2.left_stick_x;
    double rightX = gamepad2.right_stick_x;

    double deadzone = 0.05;
    if (Math.abs(leftY) < deadzone) leftY = 0;
    if (Math.abs(leftX) < deadzone) leftX = 0;
    if (Math.abs(rightX) < deadzone) rightX = 0;

    RFspeed = (leftY - leftX - rightX) * ticksPerSecond;
    LFspeed = (leftY + leftX + rightX) * ticksPerSecond;
    RBspeed = (leftY + leftX - rightX) * ticksPerSecond;
    LBspeed = -(leftY - leftX + rightX) * ticksPerSecond;

    double maxMag = Math.max(1.0, Math.max(Math.abs(RFspeed), Math.max(Math.abs(LFspeed),
                     Math.max(Math.abs(RBspeed), Math.abs(LBspeed))))) / ticksPerSecond;
    if (maxMag > 1.0) {
        RFspeed /= maxMag; LFspeed /= maxMag; RBspeed /= maxMag; LBspeed /= maxMag;
    }

    RightFront.setVelocity(RFspeed);
    RightBack.setVelocity(RBspeed);
    LeftBack.setVelocity(LBspeed);
    LeftFront.setVelocity(LFspeed);

    double RFactualRPM = (RightFront.getVelocity() / ticks) * 60.0;
    double RBactualRPM = (RightBack.getVelocity() / ticks) * 60.0;
    double LFactualRPM = (LeftFront.getVelocity() / ticks) * 60.0;
    double LBactualRPM = (LeftBack.getVelocity() / ticks) * 60.0;

    telemetry.addData("Speed Setting (RPM): ", rpm);
    telemetry.addData("RF Actual RPM: ", RFactualRPM);
    telemetry.addData("RB Actual RPM: ", RBactualRPM);
    telemetry.addData("LF Actual RPM: ", LFactualRPM);
    telemetry.addData("LB Actual RPM: ", LBactualRPM);
        
    telemetry.addData("leftY", leftY);
    telemetry.addData("leftX", leftX);
    telemetry.addData("rightX (turn)", rightX);
    telemetry.update();
    } 
}
