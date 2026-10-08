// This code allows you to control the speed of one motor
package org.firstinspires.ftc.QCTruongCode.TestingFolder;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;

@TeleOp(name = "Speed Control Test")
public class EncoderTestRPM extends OpMode {

    DcMotorEx motor;

    double ticks = 28;
    double newTarget = 0;
    double rpm = 6000;          // current speed setting, adjustable live
    double rpmStep = 100;

    boolean lastA = false;
    boolean lastB = false;
    boolean lastX = false;
    boolean lastUp = false;
    boolean lastDown = false;

    @Override
    public void init() {
        motor = hardwareMap.get(DcMotorEx.class, "motor");
        motor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        motor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        telemetry.addData("Hardware: ", "Initialized");
        telemetry.update();
    }

    @Override
    public void loop() {
        boolean currentA = gamepad2.a;
        boolean currentB = gamepad2.b;
        boolean currentX = gamepad2.x;
        boolean currentUp = gamepad2.dpad_up;     // increase speed
        boolean currentDown = gamepad2.dpad_down; // decrease speed

        // adjust speed setting
        if (currentUp && !lastUp) {
            rpm += rpmStep;
        }
        if (currentDown && !lastDown) {
            rpm = Math.max(0, rpm - rpmStep);   // don't go negative
        }

        // A: turn 180 degrees at the currently set RPM
        if (currentA && !lastA) {
            // turnToDegrees(180, rpm);
            double ticksPerSecond = (rpm / 60.0) * ticks;
            motor.setVelocity(ticksPerSecond);
            // motor.setPower(1);
        }
        // B: return to 0 at the currently set RPM
        if (currentB && !lastB) {
            // turnToDegrees(0, rpm);
            motor.setVelocity(0);
        }
        
        if (currentX && !lastX) {
            double ticksPerSecond = (rpm / 60.0) * ticks;
            motor.setVelocity(-ticksPerSecond);
        }

        lastA = currentA;
        lastB = currentB;
        lastX = currentX;
        lastUp = currentUp;
        lastDown = currentDown;

        // if (!motor.isBusy()) {
        //     motor.setPower(0);
        // }

        double actualRPM = (motor.getVelocity() / ticks) * 60.0;

        telemetry.addData("Speed Setting (RPM): ", rpm);
        telemetry.addData("Actual RPM: ", actualRPM);
        telemetry.addData("Motor Ticks: ", motor.getCurrentPosition());
        telemetry.addData("Target Ticks: ", newTarget);
        telemetry.addData("Is Busy: ", motor.isBusy());
        telemetry.addLine();
        telemetry.addData("A", "= turn 180°");
        telemetry.addData("B", "= return to 0°");
        telemetry.addData("D-pad Up/Down", "= adjust speed (+/- " + rpmStep + " RPM)");
        telemetry.update();
    }

    public void turnToDegrees(double degrees, double speedRPM) {
        newTarget = (degrees / 360.0) * ticks;
        motor.setTargetPosition((int) newTarget);
        motor.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        double ticksPerSecond = (speedRPM / 60.0) * ticks;
        motor.setVelocity(ticksPerSecond);
    }
}
