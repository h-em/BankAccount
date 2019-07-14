package model;

import utils.NegativeAmountOfMoneyException;

import java.math.BigDecimal;

public class Account {
    private String owner;
    private String accountId;
    private BigDecimal balance;
    private CurrencyType currencyType;

    public Account(String owner, String accountId, BigDecimal balance, CurrencyType currencyType) {
        this.owner = owner;
        this.accountId = accountId;
        this.balance = balance;
        this.currencyType = currencyType;
    }


    public void decreseBalance(BigDecimal value) throws NegativeAmountOfMoneyException {
        if (balance.compareTo(balance.subtract(value)) == -1) {
            throw new NegativeAmountOfMoneyException("Not enought money!");
        } else {
            balance = balance.subtract(value);
        }
    }

    public void increesBalance(BigDecimal value){
        balance.add(value);
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public CurrencyType getCurrencyType() {
        return currencyType;
    }

    public void setCurrencyType(CurrencyType currencyType) {
        this.currencyType = currencyType;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Account account = (Account) o;

        if (owner != null ? !owner.equals(account.owner) : account.owner != null) return false;
        if (accountId != null ? !accountId.equals(account.accountId) : account.accountId != null) return false;
        if (balance != null ? !balance.equals(account.balance) : account.balance != null) return false;
        return currencyType == account.currencyType;
    }

    @Override
    public int hashCode() {
        int result = owner != null ? owner.hashCode() : 0;
        result = 31 * result + (accountId != null ? accountId.hashCode() : 0);
        result = 31 * result + (balance != null ? balance.hashCode() : 0);
        result = 31 * result + (currencyType != null ? currencyType.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "owner: '" + owner + '\'' +
                ", accountId:'" + accountId + '\'' +
                ", balance: " + balance +
                ", currencyType: " + currencyType;
    }
}
