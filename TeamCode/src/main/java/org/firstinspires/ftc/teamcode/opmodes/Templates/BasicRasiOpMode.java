package org.firstinspires.ftc.teamcode.opmodes.Templates;

import org.firstinspires.ftc.teamcode.Logic.RASI.Rasi.RasiInterpreter;
import org.firstinspires.ftc.teamcode.Logic.RASI.Rasi.RasiLexer;
import org.firstinspires.ftc.teamcode.Logic.RASI.RasiCommands.RasiCommands;

public abstract class BasicRasiOpMode extends BasicOpMode{
    RasiInterpreter rasiInterpreter;

    public abstract String filename();

    public void initRasi(){
        rasiInterpreter = new RasiInterpreter("/sdcard/FIRST/ftc9773/rasi/", filename());
    }

    public void registerRasiCommands(RasiCommands rc){
        rasiInterpreter.processRasiCommands(rc);
    }

    //Relies on having compiled the file.
    public void executeRasi(){
        rasiInterpreter.run();
    }
}
