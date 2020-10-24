package org.firstinspires.ftc.teamcode.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotorEx;

import org.firstinspires.ftc.teamcode.Utilities.PIDController;
import org.firstinspires.ftc.teamcode.Utilities.json.JsonReader;

public class OneMotorVelControl extends LinearOpMode {
    double lasttime = System.currentTimeMillis();
    static double ENCODER_TICKS_PER_REVOLUTION = 560;
    @Override
    public void runOpMode() throws InterruptedException {
        DcMotorEx motor = hardwareMap.get(DcMotorEx.class, "UnqMotor");
        JsonReader reader = new JsonReader("onemotoropmode");

        PIDController controller = new PIDController(reader.getDouble("kd"),reader.getDouble("ki"),reader.getDouble("kd"));
        double target_rps = reader.getDouble("targetRPS");
        int last_pos = motor.getCurrentPosition();
        int cur_pos = 0;
        while(opModeIsActive()){
            cur_pos = motor.getCurrentPosition();
            motor.setPower(controller.getPIDCorrection(target_rps, (cur_pos - last_pos) / ENCODER_TICKS_PER_REVOLUTION / timedeltaseconds()));
            last_pos = cur_pos;
        }
    }

    double timedeltaseconds(){
        double dtime = (System.currentTimeMillis() - lasttime) / 1000;
        lasttime = System.currentTimeMillis();
        return dtime;
    }
}
