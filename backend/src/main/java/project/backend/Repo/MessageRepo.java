package project.backend.Repo;

import org.springframework.stereotype.Repository;
import project.backend.Entity.Message;
import project.backend.Utils.Util;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Timestamp;

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
}
