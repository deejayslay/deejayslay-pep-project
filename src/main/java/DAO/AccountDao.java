package DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import Model.Account;
import Util.ConnectionUtil;

public class AccountDao {

    public Account getAccountByUsername(String username) {
        Connection connection = ConnectionUtil.getConnection();
        
        try {
            String sql = "SELECT * FROM Account WHERE username = ?;";
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setString(1, username);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return new Account(rs.getInt(1), rs.getString("username"), rs.getString("password"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;

    }   
    
    
    public Account createAccount(Account acct) {
        Connection connection = ConnectionUtil.getConnection();

        try {
            if ((acct.getUsername().length() > 0) && (acct.getPassword().length() >= 4) && (this.getAccountByUsername(acct.getUsername()) == null)) {
                String sql = "INSERT INTO Account(username, password) VALUES(?, ?);";
                PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
                ps.setString(1, acct.getUsername());
                ps.setString(2, acct.getPassword());
                ps.executeUpdate();
                ResultSet pkeyResultSet = ps.getGeneratedKeys();

                if (pkeyResultSet.next()) {
                    int acct_id = (int) pkeyResultSet.getInt(1);
                    return new Account(acct_id, acct.getUsername(), acct.getPassword());
                }
            } 
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Account loginAccount(String username, String password) {
        Account foundAccount = this.getAccountByUsername(username);
        if ((foundAccount != null) && (foundAccount.getPassword().equals(password))) {
            return foundAccount;
        }
        return null;
    }


}
