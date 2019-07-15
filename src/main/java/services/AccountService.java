package services;

import model.Account;
import model.CurrencyType;
import model.User;
import utils.AccountUtil;
import utils.ApplicationConst;
import utils.TxtFileReader;
import utils.TxtFileWriter;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.logging.Logger;

public class AccountService {
    private User user;
    private static final Logger loger = Logger.getLogger(Logger.class.getName());

    public AccountService(User user) {
        this.user = user;
    }

    public void buildAccounts() {

        TxtFileReader txtFileReader = new TxtFileReader(ApplicationConst.FILE_ACCOUNTS_PATH);
        ArrayList<String> lines = txtFileReader.read();
        Account account = null;
        for (String line : lines) {
            String[] tokens = line.split(" ");
            if (tokens.length != 4) continue;
            if (!AccountUtil.isValidId(tokens[0])) continue;
            if (!tokens[1].equals(user.getUserId())) continue;
            int accountBalance = Integer.parseInt(tokens[2]);
            if (accountBalance < 0) continue;
            if (!AccountUtil.isCurrencyType(tokens[3])) continue;

            account = new Account(tokens[1], tokens[0],
                    new BigDecimal(accountBalance), AccountUtil.getCurrencyType(tokens[3]));
            user.addAccount(account);
        }
    }

    public void displayCurrnetInfo() {
        loger.info("" + user);
        Scanner scanner = new Scanner(System.in);
        System.out.println("\nPress any key...");
        scanner.nextLine();
    }

    public Account createAccount() {
        Scanner scanner = new Scanner(System.in);
        boolean isValidAccount = false;
        String accountId = "";
        while (!isValidAccount) {
            System.out.println("Account number(should start with 'RO'" +
                    "and should have 10 characters): ");
            accountId = scanner.nextLine();
            isValidAccount = AccountUtil.isValidId(accountId);
        }


        System.out.println("Amount of money: (should be grather than 0, only number are accepted)");
        int balanceStr = 0;
        boolean isValidBalnece = false;
        while (!isValidBalnece) {

            try {
                balanceStr = scanner.nextInt();
            } catch (Exception e) {
                scanner.next();
                continue;
            }
            isValidBalnece = AccountUtil.isAValidBalance(balanceStr);
        }
        BigDecimal balance = new BigDecimal(balanceStr);

        System.out.println("Currency type: ");
        String currencyTypeStr = null;
        boolean isValidCurrentType = false;
        while (!isValidCurrentType) {
            currencyTypeStr = scanner.nextLine();
            isValidCurrentType = AccountUtil.isValidCurrentType(currencyTypeStr);
            System.out.println("Valid options : RON or EUR");
        }
        CurrencyType currencyType = AccountUtil.getCurrencyType(currencyTypeStr);

        Account account = new Account(user.getUserId(), accountId, balance, currencyType);
        user.addAccount(account);

        return account;
    }


    public void displayAvalableAccounts() {
        loger.info("" + user);
    }

    public void updatedDataInAuxFile(String currentAccountId, int amountOfMoney, String beneficiaryAccountId, User user) {
        //creez un fisier auxiliar pentu a scrie in el
        TxtFileWriter txtFileWriter = new TxtFileWriter(ApplicationConst.FILE_ACCOUNTS_PATH_AUX);

        //citesc fisierul in care sunt conturile + validare
        TxtFileReader fileReader = new TxtFileReader(ApplicationConst.FILE_ACCOUNTS_PATH);
        ArrayList<String> listOfAccounts = fileReader.read();

        //scriu in noul fsier toate datele din vechiul fisier + balanta actualizata
        txtFileWriter.customWrite(listOfAccounts, currentAccountId, amountOfMoney, beneficiaryAccountId, user);

    }

    public void deleteOldFile(String fileAccountsPath) {
        //sterg vechiul fisier cu date
        try {
            Files.deleteIfExists(Paths.get(fileAccountsPath));
        } catch (NoSuchFileException e) {
            loger.warning("No such file/directory exists");
        } catch (DirectoryNotEmptyException e) {
            loger.warning("Directory is not empty.");
        } catch (IOException e) {
            loger.warning("Invalid permissions.");
        }
        loger.info("Deletion successful.");
    }

    public void renameFile(String fileAccountsPathAux, String fileAccountsPath) {

        // redenumesc fisierul auxiliar cu numele fisierului original
        Path source = Paths.get(fileAccountsPathAux);
        try {
            Files.move(source, source.resolveSibling(fileAccountsPath));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String chooseAccountId() {
        Scanner scanner = new Scanner(System.in);

        ////introduc accountId. Cautarea o sa se faca in functie de id
        System.out.println("AccountId:(should start with 'RO'" +
                "and should have 10 characters): ");
        boolean isValidAccount = false;
        String currentAccount = "";
        while (!isValidAccount) {
            currentAccount = scanner.nextLine();
            isValidAccount = AccountUtil.isValidId(currentAccount);
        }

        return currentAccount;
    }

    public int insertAmountOfMoney() {
        Scanner scanner = new Scanner(System.in);

        ///intoduc noua valoare
        System.out.println("how much money you want to send?  ");
        int amountOfMoney = 0;
        boolean isValidBalnece = false;
        while (!isValidBalnece) {
            try {
                amountOfMoney = scanner.nextInt();
            } catch (Exception e) {
                scanner.next();
                System.out.println("Invalid option! Try again!");
                continue;
            }
            isValidBalnece = AccountUtil.isAValidBalance(amountOfMoney);
        }
        return amountOfMoney;
    }

    public int getCurrentBalance(String currentAccountId) {

        TxtFileReader txtFileReader = new TxtFileReader(ApplicationConst.FILE_ACCOUNTS_PATH);
        ArrayList<String> listOfAccounts = txtFileReader.read();
        int currentBalance = 0;

        for (String line : listOfAccounts) {
            String[] tokens = line.split(" ");
            if (tokens.length != 4) continue;
            if (!AccountUtil.isValidId(tokens[0])) continue;
            int accountBalance = Integer.parseInt(tokens[2]);
            if (accountBalance < 0) continue;
            if (!AccountUtil.isCurrencyType(tokens[3])) continue;

            if (line.contains(currentAccountId)) {
                String[] args = line.split(" ");
                currentBalance = Integer.parseInt(args[2]);
                break;
            }
        }
        return currentBalance;
    }
}
