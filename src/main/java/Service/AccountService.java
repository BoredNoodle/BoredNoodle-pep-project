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

    /**
     * Use the AccountDAO to insert a new account into the database.
     * @param account an object representing a new account.
     * @return the newly added account if the insert operation was successful, including 
     *         its account id. Return <code>null</code> if the new account failed to meet 
     *         the account requirements or if the insert operation was unsuccessful.
     */
    public Account insertAccount(Account account) {
        if (account.getUsername().isBlank())
            return null;
        else if (account.getPassword().length() < 4 || account.getPassword().isBlank())
            return null;
        else if (accountDAO.getAccount(account.getUsername()) != null)
            return null;
        
        return accountDAO.insertAccount(account);
    }

    /**
     * Use the AccountDAO to retrieve an account from the database, i.e., "login" a user.
     * @param account an object representing the account to retrieve.
     * @return the retrieved account if the login operation was successful, including 
     *         its account id. Return <code>null</code> if the login operation was 
     *         unsuccessful.
     */
    public Account login(Account account) {
        return accountDAO.getAccount(account.getUsername(), account.getPassword());
    }
}
