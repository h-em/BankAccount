package menu;

import model.Account;
import model.User;
import services.AccountService;
import utils.ApplicationConst;
import utils.NegativeAmountOfMoneyException;
import utils.TxtFileWriter;

import java.io.IOException;
import java.util.Scanner;
import java.util.logging.Logger;

public class AccountsMenu extends AbstractMenu {

    private static final Logger loger = Logger.getLogger(Logger.class.getName());
    private User user;


    public AccountsMenu(User user) {
        this.user = user;
    }

    protected void displayOption() {
        System.out.println("1 ->Reads accounts.");
        System.out.println("2 ->Create accounts.");
        System.out.println("3 ->Make payments.");
        System.out.println("4 ->Display info.");
        System.out.println("0->Logout.");
    }

    protected void executeOption(Integer option) throws IOException {
        AccountService accountService = new AccountService(user);
        Scanner scanner = new Scanner(System.in);

        switch (option) {
            case 1:
                accountService.buildAccounts();
                break;
            case 2:
                Account account = accountService.createAccount();
                TxtFileWriter txtFileWriter = new TxtFileWriter(ApplicationConst.FILE_ACCOUNTS_PATH);
                txtFileWriter.write(buildString(account));
                break;
            case 3:
                try {
                    accountService.makePayments();
                } catch (NegativeAmountOfMoneyException e) {
                    e.printStackTrace();
                }
                break;
            case 4:
                accountService.displayCurrnetInfo();
                break;
            case 0:
                loger.info("User " + user.getUserId() + " is successfully logged out!");
                break;
            default:
                loger.warning("Invalide option!");
                break;
        }

    }

    private String buildString(Account account) {
        StringBuilder sb = new StringBuilder();
        sb.append(account.getAccountId() + " " + account.getOwner() + " " +
                account.getBalance() + " " + account.getCurrencyType() + "\n");

        return sb.toString();
    }

}
