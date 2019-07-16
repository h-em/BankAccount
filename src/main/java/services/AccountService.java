package services;

import model.Account;
import model.CurrencyType;
import model.User;
import utils.*;

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

    public void updatedDataInAuxFile(Account sourceAccount, Account destinationAccount, int amountOfMoney) {
        //creez un fisier auxiliar pentu a scrie in el
        TxtFileWriter txtFileWriter = new TxtFileWriter(ApplicationConst.FILE_ACCOUNTS_PATH_AUX);

        //citesc fisierul in care sunt conturile + validare
        TxtFileReader fileReader = new TxtFileReader(ApplicationConst.FILE_ACCOUNTS_PATH);
        ArrayList<String> listOfAccounts = fileReader.read();

        //scriu in noul fsier toate datele din vechiul fisier + balanta actualizata
        String sourceAccountIdStr = sourceAccount.getAccountId();
        String destinationAccountIdStr = destinationAccount.getAccountId();
        txtFileWriter.customWrite(listOfAccounts, sourceAccountIdStr, amountOfMoney, destinationAccountIdStr);
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

    public void makePayments() {
        //afisez conturile disponibile
        displayAvalableAccounts();

        //introduc si validez contul sursa
        Account sourceAccount = null;
        while (sourceAccount == null) {
            ////aleg un accountId din lista. Cautarea o sa se faca in functie de id
            System.out.println("From which account do you want to transfer?");
            String sourceAccountStr = chooseAccountId();
            sourceAccount = user.getAccount(sourceAccountStr);
            if(sourceAccount == null){
                System.out.println("Account you entered is not in your account list!");
            }
        }


        ///intoduc noua valoare + validare
        boolean areEnoughMoney = false;
        int amountOfMoney = 0;
        while (!areEnoughMoney) {
            amountOfMoney = insertAmountOfMoney();
            BigDecimal sourceBalance = sourceAccount.getBalance();
            if (sourceBalance.subtract(new BigDecimal(amountOfMoney)).compareTo(BigDecimal.ZERO) == -1){
                System.out.println("You don't have enough money!");
                System.out.println("You should transfer a smaller amount!");
            } else if(amountOfMoney < 0) {
                System.out.println("The amount of money should be higher than 0!");
            }else{
                areEnoughMoney = true;
            }
        }

        //introduc si validez contul destinatie
        Account destinationAccount = null;
        while(destinationAccount == null) {
            System.out.println("Enter the beneficiary accountId: ");
            String beneficiaryAccountId = chooseAccountId();
            destinationAccount = user.getAccount(beneficiaryAccountId);
            if(destinationAccount == null){
                System.out.println("Account you entered is not in your account list!");
            }
        }

        //actualizez balantele
        transferMoney(sourceAccount, destinationAccount, amountOfMoney);
        //actualizez fisierele
        updateAccountsFile(sourceAccount,destinationAccount,amountOfMoney);

        //afisez conturile sa vad daca s-au facut modificarile
        displayAvalableAccounts();
    }


    public void transferMoney(Account sourceAccount, Account destinationAccount, int amountOfMoney){

        BigDecimal amountOfMoneyToSend = new BigDecimal(amountOfMoney);

        //actualizez balanta pentru contul sursa
        BigDecimal sourceNewBalance = sourceAccount.getBalance().subtract( amountOfMoneyToSend);
        if(sourceNewBalance.compareTo(BigDecimal.ZERO) == -1){
            throw new NegativeAmountOfMoneyException("payment can not be made because you do not have enough money!")
        }
        sourceAccount.setBalance(sourceNewBalance);
        user.addAccount(sourceAccount);

        //actualizez balanta pentru contul destinatie
        BigDecimal destinationNewBalance = destinationAccount.getBalance().add(amountOfMoneyToSend);
        destinationAccount.setBalance(destinationNewBalance);
        user.addAccount(destinationAccount);
    }

    public void updateAccountsFile(Account sourceAccount, Account destAcount,  int amountOfMoney){

        updatedDataInAuxFile(sourceAccount, destAcount, amountOfMoney);
        deleteOldFile(ApplicationConst.FILE_ACCOUNTS_PATH);
        renameFile(ApplicationConst.FILE_ACCOUNTS_PATH_AUX,
                ApplicationConst.FILE_ACCOUNTS_PATH);

    }



}
