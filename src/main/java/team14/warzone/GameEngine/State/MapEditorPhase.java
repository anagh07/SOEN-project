package team14.warzone.GameEngine.State;

import team14.warzone.Console.Console;
import team14.warzone.Console.InputValidator;
import team14.warzone.GameEngine.Commands.Command;
import team14.warzone.GameEngine.Commands.Option;
import team14.warzone.GameEngine.GameEngine;

import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.List;

public abstract class MapEditorPhase extends Phase {

    public MapEditorPhase(GameEngine p_GameEngine) {
        super(p_GameEngine);
    }

    @Override
    public void run() {
        issueCommands();
        executeCommands();
    }

    @Override
    public void reinforce() {

    }

    @Override
    public void issueCommands() {
        // use console to prompt user to enter command
//        System.out.println("Current phase is: " + this.getClass().getSimpleName());
        System.out.println("Enter command:");
        List<List<String>> l_CommandStrList = Console.readInput();
        /*
        loadmap
        showmap
        editmap filename
        savemap filename
        editcontinent -add continentName controlValue -remove continentName
        editcountry -add countryName continentName -remove countryName
        editneighbor -add countryName neighborCountryName -remove countryName neighborCountryName
         */
        for (List<String> l_CommandStr : l_CommandStrList) {
            Option l_Option = new Option(l_CommandStr.get(1),
                    Arrays.asList(l_CommandStr.get(2).replaceAll(" ", "").split(
                            ",")));
            Command l_Command = new Command(l_CommandStr.get(0), l_Option, d_GameEngine);
            d_GameEngine.appendToCommandBuffer(l_Command);
        }

        /*
        for 1 word commands: {{"keyword", "", ""}}
        for 2 words commands: {{"keyword", "", "argument"}}
        for longer commands: {{"keyword", "optionName1", "arguments1_separated_by_comma"}, {"keyword", "optionName2",
         "arguments2_separated_by_comma"}}
        so you may use "argumentList".split(",") => to convert the arguments into a list
         */
    }

    @Override
    public void executeCommands() {
        // execute the appropriate map editor command
        for (Command l_Command : d_GameEngine.getD_CommandBuffer()) {
            l_Command.execute();
        }
        d_GameEngine.clearCommandBuffer();

    }

    @Override
    public void loadMap(String p_FileName) {
        try {
            d_GameEngine.getD_MapEditor().loadMap(p_FileName);
            d_GameEngine.setD_LoadedMap(d_GameEngine.getD_MapEditor().getD_LoadedMap());
            // validate map right after loading
            if (!d_GameEngine.getD_MapEditor().validateMap(d_GameEngine.getD_LoadedMap()))
                System.out.println("Error: map validation failed, not loaded");
            else {
                System.out.println("Success: map loaded and validated");
                // move to the startup phase
                d_GameEngine.setD_CurrentPhase(d_GameEngine.getD_StartupPhase());
                InputValidator.CURRENT_PHASE = InputValidator.Phase.STARTUP;
            }
        } catch (FileNotFoundException e) {
            System.out.println("Error: invalid filename");
        }
    }

    @Override
    public void addPlayer(String p_Name) {
        invalidCommandMessage();
    }

    @Override
    public void removePlayer(String p_Name) {
        invalidCommandMessage();
    }

    @Override
    public void assignCountries() {
        invalidCommandMessage();
    }

    @Override
    public void deploy() {
        invalidCommandMessage();
    }

    @Override
    public void advance() {
        invalidCommandMessage();
    }

    @Override
    public void bomb() {
        invalidCommandMessage();
    }

    @Override
    public void blockade() {
        invalidCommandMessage();
    }

    @Override
    public void airlift() {
        invalidCommandMessage();
    }

    @Override
    public void diplomacy() {
        invalidCommandMessage();
    }
}
