package utils;

import model.Account;
import model.User;

import java.io.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.logging.Logger;

public class TxtFileWriter {
    private String fileName;
    private static final Logger loger = Logger.getLogger(Logger.class.getName());


    public TxtFileWriter(String fileName) {
        this.fileName = fileName;
    }

    public void write(String line) {
        try (BufferedWriter bufferedWriter =
                     new BufferedWriter(
                             new FileWriter(ApplicationConst.FILE_ACCOUNTS_PATH, true))) {
            bufferedWriter.write(line);
        } catch (IOException e) {
            loger.warning("Error!" + e.getMessage());
        }
    }

    public void customWrite(ArrayList<String> listOfAccounts, String currentAccountId,
                            int amountOfMoney, String beneficiaryAccountId, User user) {

        try (BufferedWriter bufferedWriterAux = new BufferedWriter(
                new FileWriter(ApplicationConst.FILE_ACCOUNTS_PATH_AUX))) {

            for (String line : listOfAccounts) {
                String[] tokens = line.split(" ");
                if (tokens.length != 4) continue;
                if (!AccountUtil.isValidId(tokens[0])) continue;
                int accountBalance = Integer.parseInt(tokens[2]);
                if (accountBalance < 0) continue;
                if (!AccountUtil.isCurrencyType(tokens[3])) continue;

                if (line.contains(currentAccountId)) {
                    String[] args = line.split(" ");
                    String updateCurrentBalance = (Integer.parseInt(args[2]) - amountOfMoney) + "";
                    line = args[0] + " " + args[1] + " " + updateCurrentBalance + " " + args[3] + " ";

                   /* Account account = new Account(tokens[1], tokens[0],
                            new BigDecimal(updateCurrentBalance), AccountUtil.getCurrencyType(tokens[3]));
                    user.addAccount(account);*/
                }

                if (line.contains(beneficiaryAccountId)) {
                    String[] args = line.split(" ");
                    String updateCurrentBalance = (Integer.parseInt(args[2]) + amountOfMoney) + "";
                    line = args[0] + " " + args[1] + " " + updateCurrentBalance + " " + args[3] + " ";

                  /*  Account account = new Account(tokens[1], tokens[0],
                            new BigDecimal(updateCurrentBalance), AccountUtil.getCurrencyType(tokens[3]));
                    user.addAccount(account);*/
                }


                //scriu in noul fisier toate liniile + cea modificata
                bufferedWriterAux.write(line);
                bufferedWriterAux.write("\n");
                bufferedWriterAux.flush();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
