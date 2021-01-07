package project.backend.Repo;

import org.springframework.stereotype.Repository;
import project.backend.Entity.Message;
import project.backend.Entity.Patient;
import project.backend.Utils.Util;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.LinkedList;
import java.util.List;

@Repository
public class MessageRepo {
    public void insertMessage(String type, Message message) {
        Connection conn = Util.connect(type);
        assert conn != null;
        Timestamp timestamp = new Timestamp(message.getTime().getTime());
        String sql = "insert into database_project.message(receiver_id, content, status, time) values (?, ?, ?, ?)";
        try {
            PreparedStatement preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setString(1, message.getReceiver_id());
            preparedStatement.setString(2, message.getContent());
            preparedStatement.setInt(3, message.getStatus());
            preparedStatement.setTimestamp(4, timestamp);
            preparedStatement.executeUpdate();
        }
        catch (Exception e){
            e.printStackTrace();
        }
        Util.close(conn);
    }

    public List<Message> findMessagesById(String type, String id) {
        Connection conn = Util.connect(type);
        assert conn != null;
        String sql = "select * from database_project.message where receiver_id = ?";
        ResultSet rs;
        List<Message> messages = new LinkedList<>();
        Message message;
        try {
            PreparedStatement preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setString(1, id);
            rs = preparedStatement.executeQuery();
            while (rs.next()){
                message = new Message();
                Util.toObject(rs, message);
                messages.add(message);
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
        Util.close(conn);
        return messages;
    }

    public void updateStatusToRead(String type, int messageId) {
        Connection conn = Util.connect(type);
        assert conn != null;
        String sql = "update database_project.message set status = 1 where id = ?";
        try {
            PreparedStatement preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setInt(1, messageId);
            preparedStatement.executeUpdate();
        }
        catch (Exception e){
            e.printStackTrace();
        }
        Util.close(conn);
    }
}
