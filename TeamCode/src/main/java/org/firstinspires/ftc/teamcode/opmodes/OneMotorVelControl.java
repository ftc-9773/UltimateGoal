package org.firstinspires.ftc.teamcode.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;

import org.firstinspires.ftc.teamcode.Utilities.PIDController;
import org.firstinspires.ftc.teamcode.Utilities.json.JsonReader;

@TeleOp(name = "do stuff")
public class OneMotorVelControl extends LinearOpMode {
    double lasttime = System.currentTimeMillis();
    static double ENCODER_TICKS_PER_REVOLUTION = 537.6;
    @Override
    public void runOpMode() throws InterruptedException {
        waitForStart();

        DcMotorEx motor = hardwareMap.get(DcMotorEx.class, "UnqMotor");
        //JsonReader reader = new JsonReader("onemotoropmode");

        PIDController controller = new PIDController(.1,0.0,0.5);
        double going_down_modifier = 0.1;
        double target_rps = 4.5 * 0.7; //340 RPM * 60s/m * 70%
        int last_pos = motor.getCurrentPosition();
        int cur_pos = 0;
        double power = 0.5;
        double dt;
        double avgSpeed = 0;
        double n = 0;
        double adjustment = 0;
        while(opModeIsActive()){
            motor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            motor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
            cur_pos = motor.getCurrentPosition();
            dt = timedeltaseconds();
            adjustment = controller.getPIDCorrection(target_rps, (cur_pos - last_pos) / ENCODER_TICKS_PER_REVOLUTION / dt);
            if (adjustment < 0){
                adjustment *= going_down_modifier;
            }
            power += adjustment;
            motor.setPower(power);
            //motor.setPower(1);
            n+=1;
            avgSpeed = (avgSpeed * n + (cur_pos - last_pos) / ENCODER_TICKS_PER_REVOLUTION / dt) / (n + 1);
            telemetry.addLine("Current RPS " + ((cur_pos - last_pos) / ENCODER_TICKS_PER_REVOLUTION / dt));
            telemetry.addLine("Target RPS" + target_rps);
            telemetry.addLine("AVG speed " + avgSpeed);
            telemetry.addLine("Postion" + motor.getCurrentPosition());
            last_pos = cur_pos;
            telemetry.update();
        }
    }

    double timedeltaseconds(){
        double dtime = (System.currentTimeMillis() - lasttime) / 1000;
        lasttime = System.currentTimeMillis();
        return dtime;
    }
}
