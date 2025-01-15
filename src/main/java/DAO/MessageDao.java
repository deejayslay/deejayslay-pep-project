package DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import Model.Message;
import Util.ConnectionUtil;

public class MessageDao {

    public Message createMessage(Message message) {
        Connection connection = ConnectionUtil.getConnection();

        try {
            if ((message.getMessage_text().length() > 0) && (message.getMessage_text().length() <= 255)) {
                String sql = "INSERT INTO Message(posted_by, message_text, time_posted_epoch) VALUES(?, ?, ?);";
                PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
                ps.setInt(1, message.getPosted_by());
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

    public List<Message> getAllMessages() {
        Connection connection = ConnectionUtil.getConnection();
        List<Message> messages = new ArrayList<>();
        
        try {
            String sql = "SELECT * FROM message;";
            PreparedStatement ps = connection.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            while(rs.next()) {
                Message message = new Message(rs.getInt(1), rs.getInt(2), rs.getString(3), rs.getLong(4));
                messages.add(message);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return messages;
    }

    public Message getMessageById(int message_id) {
        Connection connection = ConnectionUtil.getConnection();

        try {
            String sql = "SELECT * FROM message WHERE message_id = ?;";
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1, message_id);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                Message message = new Message(message_id, rs.getInt(2), rs.getString(3), rs.getLong(4));
                return message;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
        
    }

    public Message deleteMessageById(int message_id) {
        Connection connection = ConnectionUtil.getConnection();
        Message foundMessage = this.getMessageById(message_id);

        try {
            if (foundMessage != null) {
                String sql = "DELETE FROM message WHERE message_id = ?;";
                PreparedStatement ps = connection.prepareStatement(sql);
                ps.setInt(1, message_id);
    
                ps.executeUpdate(); 
            };

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return foundMessage;
    }

    public Message updateMessageById(int message_id, String newMessageText) {
        Connection connection = ConnectionUtil.getConnection();

        try {
            Message oldMessage = this.getMessageById(message_id);
            if ((oldMessage != null) && (newMessageText.length() > 0) && (newMessageText.length() <= 255)) {
                String sql = "UPDATE message SET message_text = ? WHERE message_id = ?;";
                PreparedStatement ps = connection.prepareStatement(sql);
                ps.setString(1, newMessageText);
                ps.setInt(2, message_id);
    
                ps.executeUpdate();
                Message newMessage = this.getMessageById(message_id);
                return newMessage;
            };

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
        
    }

    public List<Message> getAllMessagesByUser(int account_id) {
        Connection connection = ConnectionUtil.getConnection();
        List<Message> messages = new ArrayList<>();

        try {
            String sql = "SELECT * FROM message WHERE message_id = ?;";
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1, account_id);
            ResultSet rs = ps.executeQuery();

            while(rs.next()) {
                Message message = new Message(rs.getInt(1), rs.getInt(2), rs.getString(3), rs.getLong(4));
                messages.add(message);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return messages;
    }   
}
