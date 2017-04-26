package com.example.tim_w.voiceadventure;

import android.widget.TextView;

import java.util.HashSet;

/**
 * Created by tim_w on 4/17/2017.
 */



public class FrontHouseScene implements Scene {
    private Inventory _inventory;
    private String _desc;
    private HashSet<Item> sceneItems;
    private boolean mailboxOpen = false;
    private Item lantern, key;
    private TextView tView;

    public FrontHouseScene(Inventory inventory){
        this._inventory = inventory;
        this._desc = "You stand before a dark, broken down house. The front door is locked. There is a mailbox and a lantern on the ground.";

        this.lantern = new Item("lantern", "A battery powered lantern.");
        this.key = new Item("key", "A rusty old key");

        sceneItems = new HashSet<Item>();

        sceneItems.add(lantern);
        sceneItems.add(key);


    }

    @Override
    public void load(TextView v) {
        v.setText(this._desc);
        if(this.tView == null) this.tView = v;
    }

    @Override
    public String performAction(String keyword, String command) {
        switch(keyword){
            case "OPEN":
            case "CHECK":
            case "LOOK":
                if(command.contains("MAILBOX")){
                    if(!this.mailboxOpen || this.sceneItems.contains(key)){
                        this.mailboxOpen = true;
                        return "There's a key in the mailbox.";
                    }else{
                        return "It's a mailbox with nothing inside";
                    }
                }else{
                    return "Input unknown. Try something else.";
                }
            case "TAKE":
                if(command.equalsIgnoreCase("KEY")){
                    if(this.mailboxOpen && this.sceneItems.contains(key)){
                        this._inventory.addItem(key);
                        this.sceneItems.remove(key);
                        return "You put the key in your bag.";
                    }
                }else if(command.equalsIgnoreCase("LANTERN") && this.sceneItems.contains(lantern)){
                    this._inventory.addItem(lantern);
                    this.sceneItems.remove(lantern);
                    this._desc = "You stand before a dark, broken down house. The front door is locked. There is a mailbox.";
                    return "You take the lantern.";
                }else{
                    return "Input unknown. Try something else.";
                }
            case "TAKEE":
            case "TIKKI":
            case "TIKI":
                if(this.mailboxOpen && this.sceneItems.contains(key)){
                    this._inventory.addItem(key);
                    this.sceneItems.remove(key);
                    return "You put the key in your bag.";
                }else{
                    return "Input unknown. Try something else.";
                }
            default:
                return "Input unknown. Try something else.";
        }
    }

    @Override
    public Scene navigate(String direction, AdventureMap map) {
        return null;
    }

    @Override
    public Scene navigate(String keyword, String object, AdventureMap map){
        Position currPos = map.getCurrPos();
        switch(keyword){
            case "OPEN":
            case "ENTER":
                if(object.equalsIgnoreCase("DOOR")){
                    if(this._inventory.checkItem("key")){
                        Scene nextScene = map.getSceneAtPosition(currPos.getX() + 1, currPos.getY());
                        map.setCurrPos(currPos.getX() + 1, currPos.getY());
                        nextScene.setInventory(this._inventory);
                        return nextScene;
                    }else{
                        this.tView.setText("The door is locked.");
                    }
                }else if(object.equalsIgnoreCase("MAILBOX")){
                    String result = this.performAction("OPEN", "MAILBOX");
                    this.tView.setText(result);
                }

        }

        return null;
    }


    @Override
    public Inventory getInventory() {
        return null;
    }

    @Override
    public void setInventory(Inventory inventory) {
        this._inventory = inventory;
    }
}