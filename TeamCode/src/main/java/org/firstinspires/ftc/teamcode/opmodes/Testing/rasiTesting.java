package org.firstinspires.ftc.teamcode.opmodes.Testing;

import android.nfc.Tag;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.Logic.RASI.Rasi.RasiInterpreter;
import org.firstinspires.ftc.teamcode.Logic.RASI.RasiCommands.RobotV1Commands;
import org.firstinspires.ftc.teamcode.Utilities.json.JsonReader;
import org.firstinspires.ftc.teamcode.opmodes.Templates.BasicFullOpMode;
import org.firstinspires.ftc.teamcode.opmodes.Templates.BasicRasiOpMode;

@Autonomous(name = "T_Rasi")
public class rasiTesting extends BasicFullOpMode {
    RasiInterpreter rasiInterpreter;


    @Override
    public void initialise() {
        super.initialise();
        rasiInterpreter = new RasiInterpreter("/sdcard/FIRST/ftc9773/2021/rasi/", "testing");
        rasiInterpreter.processRasiCommands(new RobotV1Commands());

    }

    @Override
    public void run() {
        String[] Tags = new String[1];
        JsonReader reader = new JsonReader("componentJson");
        Tags[0] = reader.getString("TAG");
        rasiInterpreter.setTags(Tags);
        rasiInterpreter.compileRasi();
    }
}
