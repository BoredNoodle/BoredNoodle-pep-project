package Service;

import DAO.AccountDAO;
import Model.Account;

public class AccountService {
    
    public AccountDAO accountDAO;

    // No-args constructor for accountService which creates a AccountDAO.
    public AccountService() {
        accountDAO = new AccountDAO();
    }

    /**
     * Constructor for an AccountService when an AccountDAO is provided.
     * This is used for when a mock AccountDAO that exhibits mock behavior is used in the test cases.
     * This would allow the testing of AccountService independently of AccountDAO.
     * @param accountDAO
     */
    public AccountService(AccountDAO accountDAO) {
        this.accountDAO = accountDAO;
    }

    // Use AccountDAO to verify and insert new user account
    public Account insertAccount(Account account) {
        if (account.getUsername() == null || account.getUsername().isEmpty()) {
            return null;
        } else if (account.getPassword().length() < 4 || account.getPassword() == null) {
            return null;
        } else if (accountDAO.getAccountByUsername(account.getUsername()) != null) {
            return null;
        }
        return accountDAO.insertAccount(account);
    }

    // Use AccountDAO to log in a user
    public Account login(Account account) {
        return accountDAO.getAccountByUsernameAndPassword(account.getUsername(), account.getPassword());
    }
}
