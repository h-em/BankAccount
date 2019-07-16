package services;

import model.Account;
import model.User;
import utils.ApplicationConst;
import utils.TxtFileReader;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class LoginService {


    public User login(String userId, String password){
        User user = null;
        TxtFileReader txtFileReader = new TxtFileReader(ApplicationConst.FILE_USERS_PATH);
        ArrayList<String> lines = txtFileReader.read();

        for (String line : lines){
            String[] tokens = line.split(" ");
            if(tokens.length != 2){
                continue;
            }
            if(tokens[0].equals(userId) && tokens[1].equals(password)) {
                Map<String, Account> accounts = new HashMap<String, Account>();
                user = new User(userId, password, accounts);
                break;
            }
        }
        return user;
    }
}
