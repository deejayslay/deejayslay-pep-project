package DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import Model.Message;
import Util.ConnectionUtil;

public class MessageDao {

    public Message createMessage(Message message) {
        Connection connection = ConnectionUtil.getConnection();

        try {
            if ((message.getMessage_text().length() > 0) && (message.getMessage_text().length() <= 255)) {
                String sql = "INSERT INTO Message(posted_by, message_text, time_posted_epoch) VALUES(?, ?, ?);";
                PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
                ps.setInt(1, message.getMessage_id());
                ps.setString(2, message.getMessage_text());
                ps.setLong(3, message.getTime_posted_epoch());
                ps.executeUpdate();
                ResultSet pkeyResultSet = ps.getGeneratedKeys();

                if (pkeyResultSet.next()) {
                    int message_id = (int) pkeyResultSet.getInt(1);
                    return new Message(message_id, message.getPosted_by(), message.getMessage_text(), message.getTime_posted_epoch());
                }
            } 
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    
}
