package model;

import java.util.Map;

public class User {
    private String userId;
    private String password;
    private Map<String, Account> accounts;

    public User(String userId, String password, Map<String,Account> accounts) {
        this.userId = userId;
        this.password = password;
        this.accounts = accounts;
    }


    public Account getAccount(String accountId){
        Account account = null;
        if(accounts.containsKey(accountId)){
            account = accounts.get(accountId);
        }
        return account;
    }

    public void addAccount(Account account){
        accounts.put(account.getAccountId(),account);
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Map<String, Account> getAccounts() {
        return accounts;
    }

    public void setAccounts(Map<String, Account> accounts) {
        this.accounts = accounts;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("User: " + userId+".\n" );
        if(accounts.isEmpty() || accounts == null){
            sb.append(" There is no account for user " + userId+ "!");
        }else{
            for(Map.Entry<String,Account> entry : accounts.entrySet()){
                sb.append(entry.getValue()+"\n");
            }
        }
        return sb.toString();
    }
}
