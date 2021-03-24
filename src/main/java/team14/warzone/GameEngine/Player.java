package team14.warzone.GameEngine;

import team14.warzone.Console.Console;
import team14.warzone.GameEngine.Commands.*;
import team14.warzone.MapModule.Country;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Class implements the player model
 *
 * @author Anagh
 * @version 1.0
 */
public class Player {
    /**
     * name of player
     */
    private String d_Name;
    /**
     * number of armies in players possession
     */
    private int d_TotalNumberOfArmies;
    /**
     * track how many armies have been ordered to be deployed
     */
    private int d_ArmiesOrderedToBeDeployed;
    /**
     * list of countries owned by player
     */
    private ArrayList<Country> d_CountriesOwned;
    /**
     * list of orders issued by player that has not been executed
     */
    private ArrayList<Order> d_OrderList;
    /**
     * list of cards the player is holding
     */
    private ArrayList<Card> d_CardList;
    /**
     * Reference to the GameEngine object
     */
    private GameEngine d_GE;

    /**
     * Default constructor that takes no params
     */
    public Player() {
    }

    /**
     * Constructor that takes all attributes as params
     *
     * @param d_Name                name of the player
     * @param d_TotalNumberOfArmies total number of armies
     * @param d_CountriesOwned      list of countries (Country objects) owned by the player
     * @param d_OrderList           list of orders the player has issued but has not executed yet
     */
    public Player(String d_Name, int d_TotalNumberOfArmies, ArrayList<Country> d_CountriesOwned,
                  ArrayList<Order> d_OrderList, GameEngine p_GE) {
        this.d_Name = d_Name;
        this.d_TotalNumberOfArmies = d_TotalNumberOfArmies;
        this.d_CountriesOwned = d_CountriesOwned;
        this.d_OrderList = d_OrderList;
        d_GE = p_GE;
        d_ArmiesOrderedToBeDeployed = 0;
    }

    /**
     * Constructor that accepts playername and sets the other attributes with default values
     *
     * @param p_Name name of the player
     */
    public Player(String p_Name, GameEngine p_GE) {
        this(p_Name, 20, new ArrayList<Country>(Collections.emptyList()),
                new ArrayList<Order>(Collections.emptyList()), p_GE);
    }

    /**
     * This method adds order to the list of orders
     */
    public void issueOrder() {
        List<String> l_OrderStr = d_GE.getD_OrderStrBuffer().get(0);
        switch (l_OrderStr.get(0)) {
            // { "deploy", "", "countryFrom, numOfArmies" }
            case "deploy":
                String[] l_Temp = l_OrderStr.get(2).replaceAll(" ", "").split(",");
                Deploy l_DeployOrder = new Deploy(l_Temp[0], Integer.parseInt(l_Temp[1]), d_GE);
                d_OrderList.add(l_DeployOrder);
                d_GE.getD_LogEntryBuffer().setD_log(getD_Name()+" issued deploy command");
                d_GE.getD_LogEntryBuffer().notifyObservers(d_GE.getD_LogEntryBuffer());
                break;

            // { "advance", "", "countryFrom, countryTo, numOfArmies" }
            case "advance":
                String[] l_ArgsAdvance = l_OrderStr.get(2).replaceAll(" ", "").split(",");
                Advance l_AdvanceOrder = new Advance(l_ArgsAdvance[0], l_ArgsAdvance[1], Integer.parseInt(l_ArgsAdvance[2]), d_GE);
                d_OrderList.add(l_AdvanceOrder);
                d_GE.getD_LogEntryBuffer().setD_log(getD_Name()+" issued advance command");
                d_GE.getD_LogEntryBuffer().notifyObservers(d_GE.getD_LogEntryBuffer());
                break;

            // { "airlift", "", "countryFrom, countryTo, numOfArmies" }
            case "airlift":
                String[] l_ArgsAirlift = l_OrderStr.get(2).replaceAll(" ", "").split(",");
                Airlift l_AirliftOrder = new Airlift(l_ArgsAirlift[0], l_ArgsAirlift[1], Integer.parseInt(l_ArgsAirlift[2]), d_GE);
                d_OrderList.add(l_AirliftOrder);
                d_GE.getD_LogEntryBuffer().setD_log(getD_Name()+" issued airlift command");
                d_GE.getD_LogEntryBuffer().notifyObservers(d_GE.getD_LogEntryBuffer());
                break;

            // { "blockade", "", "countryFrom, numOfArmies" }
            case "blockade":
                String[] l_ArgsBlockade = l_OrderStr.get(2).replaceAll(" ", "").split(",");
                Blockade l_BlockadeOrder = new Blockade(l_ArgsBlockade[0], Integer.parseInt(l_ArgsBlockade[1]), d_GE);
                d_OrderList.add(l_BlockadeOrder);
                d_GE.getD_LogEntryBuffer().setD_log(getD_Name()+" issued blockade command");
                d_GE.getD_LogEntryBuffer().notifyObservers(d_GE.getD_LogEntryBuffer());
                break;

            // { "bomb", "", "countryFrom, numOfArmies" }
            case "bomb":
                String[] l_ArgsBomb = l_OrderStr.get(2).replaceAll(" ", "").split(",");
                Bomb l_BombOrder = new Bomb(l_ArgsBomb[0], Integer.parseInt(l_ArgsBomb[1]), d_GE);
                d_OrderList.add(l_BombOrder);
                d_GE.getD_LogEntryBuffer().setD_log(getD_Name()+" issued bomb command");
                d_GE.getD_LogEntryBuffer().notifyObservers(d_GE.getD_LogEntryBuffer());
                break;

            // { "diplomacy", "", "playerId" }
            case "diplomacy":
                String[] l_ArgsDiplomacy = l_OrderStr.get(2).replaceAll(" ", "").split(",");
                Diplomacy l_DiplomacyOrder = new Diplomacy(l_ArgsDiplomacy[0], d_GE);
                d_OrderList.add(l_DiplomacyOrder);
                d_GE.getD_LogEntryBuffer().setD_log(getD_Name()+" issued diplomacy command");
                d_GE.getD_LogEntryBuffer().notifyObservers(d_GE.getD_LogEntryBuffer());
                break;

            default:
                Console.displayMsg("Error in issue order!");
                d_GE.getD_LogEntryBuffer().setD_log("Error in issue order!");
                d_GE.getD_LogEntryBuffer().notifyObservers(d_GE.getD_LogEntryBuffer());

        }
        d_GE.clearOrderBuffer();
    }

    /**
     * This method executes the first order in the order list
     * <ul>
     *     <li>Order list is maintained in a FIFO fasion</li>
     *     <li>Check if order list is empty</li>
     *     <li>Execute the order that was issued first</li>
     *     <li>After execution resize the list</li>
     * </ul>
     */
    public void nextOrder() {
        if (!d_OrderList.isEmpty()) {
            try {
                d_OrderList.get(0).execute();//execute first order from the order list
            } catch (Exception e) {
                Console.displayMsg(e.getMessage());
            }
            // remove the command after execution
            d_OrderList.remove(0);// remove first order from the order list
        }
    }

    /**
     * This method adds a country to the list of countries owned by the player
     *
     * @param p_Country country that is to be added
     */
    public void addCountryOwned(Country p_Country) {
        this.d_CountriesOwned.add(p_Country);
    }

    public boolean hasCard(Card p_Card) {
        return d_CardList.contains(p_Card);
    }

    /**
     * Getter method for name of the player
     *
     * @return name of the player
     */
    public String getD_Name() {
        return d_Name;
    }

    /**
     * Getter method for the total number of armies owned by the player
     *
     * @return total number of armies currently in players possession
     */
    public int getD_TotalNumberOfArmies() {
        return d_TotalNumberOfArmies;
    }

    /**
     * Getter method for the list of countries owned by the player
     *
     * @return list of countries owned
     */
    public ArrayList<Country> getD_CountriesOwned() {
        return d_CountriesOwned;
    }

    /**
     * Getter method for the list of orders issued by the player
     *
     * @return list of orders issued and not yet executed
     */
    public ArrayList<Order> getD_OrderList() {
        return d_OrderList;
    }

    /**
     * Setter method for the list of orders
     *
     * @param d_OrderList list or orders
     */
    public void setD_OrderList(ArrayList<Order> d_OrderList) {
        this.d_OrderList = d_OrderList;
    }

    /**
     * Setter method for the list of countries owned
     *
     * @param d_CountriesOwned list of countries owned
     */
    public void setD_CountriesOwned(ArrayList<Country> d_CountriesOwned) {
        this.d_CountriesOwned = d_CountriesOwned;
    }

    /**
     * Setter method for the number of armies
     *
     * @param d_TotalNumberOfArmies number of armies
     */
    public void setD_TotalNumberOfArmies(int d_TotalNumberOfArmies) {
        this.d_TotalNumberOfArmies = d_TotalNumberOfArmies;
    }

    /**
     * Setter method for the name of player
     *
     * @param d_Name name of player
     */
    public void setD_Name(String d_Name) {
        this.d_Name = d_Name;
    }

    /**
     * The method to add card to players' card list
     * @param card card to be added
     */
    public void addCard(Card card) {
        d_CardList.add(card);
    }
    /**
     * Getter for the number of armies ordered to be deployed field
     *
     * @return
     */
    public int getD_ArmiesOrderedToBeDeployed() {
        return d_ArmiesOrderedToBeDeployed;
    }

    /**
     * Increase the number of armies ordered to be deployed field
     *
     * @param p_ArmiesOrderedToBeDeploy
     */
    public void increaseArmiesOrderedToBeDeployed(int p_ArmiesOrderedToBeDeploy) {
        d_ArmiesOrderedToBeDeployed += p_ArmiesOrderedToBeDeploy;
    }

    /**
     * Decrease the number of armies ordered to be deployed field
     *
     * @param p_ArmiesOrderedToBeDeploy
     */
    public void decreaseArmiesOrderedToBeDeployed(int p_ArmiesOrderedToBeDeploy) {
        d_ArmiesOrderedToBeDeployed -= p_ArmiesOrderedToBeDeploy;
    }
}
