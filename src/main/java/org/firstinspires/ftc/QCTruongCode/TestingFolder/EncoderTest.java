// no idea what I did here
package org.firstinspires.ftc.QCTruongCode.TestingFolder;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
@TeleOp(name = "Encoder Test ")
public class EncoderTest extends OpMode {
    DcMotor motor;
    double ticks = 28.0;
    double newTarget;
    @Override
    public void init() {
        motor = hardwareMap.get(DcMotor.class, "motor");
        telemetry.addData("Hardware: ", "Initialized");
        motor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    }

    @Override
    public void loop() {
        telemetry.addData("Motor Ticks: ", motor.getCurrentPosition());
        if(gamepad2.a){
            encoder(2);
        }
        if(gamepad2.b){
            tracker();
        }
        telemetry.addData("gamepad a: ", gamepad2.a);
        telemetry.update();
    }
    public void encoder(int turnage){
        motor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        newTarget = ticks/turnage;
        motor.setTargetPosition((int)newTarget);
        motor.setPower(0.1);
        motor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
    }
    public void tracker(){
        motor.setTargetPosition(0);
        motor.setPower(0.8);
        motor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
    }

}
