package model.commands.storageCommands;

import controller.Respondent;
import model.commands.Command;
import model.interfaces.IStore;
import requests.interfaces.IRequestElement;
import requests.interfaces.Request;

public class InfoCommand implements Command {
    private final IStore storage;
    public InfoCommand(IStore storage){
        this.storage = storage;
    }
    @Override
    public void execute(Request request){
        Respondent.sendToOutput(storage.info());
    }
}