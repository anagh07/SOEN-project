package team14.warzone.Console;

import team14.warzone.GameEngine.GameEngine;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Console {
    static Scanner l_Scanner = new Scanner(System.in);
    private Command d_CommandBuffer;

//    public static void main(String[] p_Args) {
//        System.out.println("Welcome to warzone game, please enter your command : ");
//        readInput();
//    }

    public void readInput() {
        while (true) {
            String[] l_UserInput = l_Scanner.nextLine().split(" ");
            Command l_UserCommand = new Command();
            //if user wants to exit the game
            if (l_UserInput[0].equals("exit"))
                break;
            //define keywords and argument for the command
            String l_Keyword = l_UserInput[0];
            List<String> l_Arguments = new ArrayList<>();
            boolean l_ValidCommand = true;// to store the command validity
            //check one word command : a keyword only, no options or arguments
            if (l_UserInput.length == 1) {
                if (InputValidator.validateInput(l_Keyword, "noOption", l_Arguments))
                    System.out.println("valid command");
            }
            //check two words command : : a keyword with an argument, no options
            else if (l_UserInput.length == 2) {
                l_Arguments.add(l_UserInput[1]);
                if (InputValidator.validateInput(l_Keyword, "noOption", l_Arguments))
                    System.out.println("valid command");
            } else { //check three words or more command
                for (int i = 1; i < l_UserInput.length; i++) {
                    String l_Opt = ""; //to store command options
                    if (l_UserInput[i].charAt(0) == '-') { //check if a word is an option
                        l_Arguments.clear();//reset arguments list
                        l_Opt = l_UserInput[i];
                        //store all arguments for that option
                        while ((i + 1) < l_UserInput.length && l_UserInput[i + 1].charAt(0) != '-') {
                            l_Arguments.add(l_UserInput[i + 1]);
                            i++;
                        }
                    } else {
                        //if the word is not an option then add it to the arguments list
                        while (i < l_UserInput.length) {
                            l_Arguments.add(l_UserInput[i]);
                            i++;
                        }
                    }
                    //check command validity
                    System.out.println("keyword : " + l_Keyword + ", options: " + l_Opt + ", arguments : " + l_Arguments);
                    l_ValidCommand = l_ValidCommand && InputValidator.validateInput(l_Keyword, l_Opt, l_Arguments);
                }
                if (l_ValidCommand) {
                    System.out.println("valid command");
                    setD_CommandBuffer(l_UserCommand);
                    // Since filter is static, we need to pass the gameengine object to store command in
//                    filterCommand(l_UserCommand);
                } else
                    System.out.println("invalid command");
            }
        }
    }

    public void filterCommand(GameEngine p_GameEngine) {
        if (d_CommandBuffer.getD_Keyword().equals("showmap")) {
            d_CommandBuffer.execute();
        } else {
            switch (InputValidator.CURRENT_PHASE) {
                case MAPEDITOR:
                case STARTUP:
                    d_CommandBuffer.execute();
                    break;

                case GAMEPLAY:
                    p_GameEngine.receiveCommand(d_CommandBuffer);
                    break;
            }
        }
    }

    public void setD_CommandBuffer(Command d_CommandBuffer) {
        this.d_CommandBuffer = d_CommandBuffer;
    }
}
