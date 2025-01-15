package Service;

import DAO.AccountDao;
import Model.Account;

public class AccountService {

    private AccountDao accountDao;

    public AccountService() {
        this.accountDao = new AccountDao();
    }

    public AccountService(AccountDao accountDao) {
        this.accountDao = accountDao;
    }

    public Account createAccount(Account acct) {
        return this.accountDao.createAccount(acct);
    }

    public Account loginAccount(String username, String password) {
        return this.accountDao.loginAccount(username, password);
    }
    
}
