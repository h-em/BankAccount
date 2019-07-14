package main;

import model.User;
import services.LoginService;

import java.io.IOException;
import java.util.Scanner;
import java.util.logging.Logger;

public class LoginMenu extends AbstractMenu {

    private static final Logger loger = Logger.getLogger(Logger.class.getName());

    protected void displayOption() {
        System.out.println("press 1->Login \n 0->Exit");
    }

    protected void executeOption(Integer option) {

        switch (option) {
            case 1:
                Scanner scanner = new Scanner(System.in);
                System.out.println("User: ");
                String userId = scanner.nextLine();
                System.out.println("Password: ");
                String password = scanner.nextLine();

                LoginService loginService = new LoginService();
                User user = loginService.login(userId, password);
                if (user == null) {
                    loger.warning("UserId\\Password incorect!");
                } else {
                    loger.info("Hello, " + userId + "!");
                    AccountsMenu accountsMenu = new AccountsMenu(user);
                    while (option != 0) {
                        accountsMenu.displayOption();
                        System.out.println("Your option: ");
                        option = scanner.nextInt();
                        try {
                            accountsMenu.executeOption(option);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
                break;
            case 0:
                loger.info("Exiting...");
                break;
            default:
                loger.warning("Invalide option!");
                break;
        }
    }
}
