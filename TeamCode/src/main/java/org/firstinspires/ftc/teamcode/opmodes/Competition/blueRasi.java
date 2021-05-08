package org.firstinspires.ftc.teamcode.opmodes.Competition;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.Logic.RASI.Rasi.RasiInterpreter;
import org.firstinspires.ftc.teamcode.Logic.RASI.RasiCommands.RobotV1Commands;
import org.firstinspires.ftc.teamcode.Utilities.json.JsonReader;
import org.firstinspires.ftc.teamcode.opmodes.Templates.BasicFullOpMode;
import org.firstinspires.ftc.teamcode.opmodes.Templates.BasicVisionOpMode;

@Autonomous(name = "blueRasi")
public class blueRasi extends BasicVisionOpMode {
    RasiInterpreter rasiInterpreter;

    @Override
    public boolean isblue() {
        return true;
    }

    @Override
    public void initialise() {
        super.initialise();
        rasiInterpreter = new RasiInterpreter("/sdcard/FIRST/ftc9773/2021/rasi/", "blue.rasi");
        rasiInterpreter.processRasiCommands(new RobotV1Commands());
        updateReadings();
    }

    @Override
    public void run() {
        String[] Tags = new String[1];
        JsonReader reader = new JsonReader("componentJson");
        Tags[0] = number.toString().toUpperCase();
        rasiInterpreter.setTags(Tags);
        rasiInterpreter.compileRasi();
    }
}
