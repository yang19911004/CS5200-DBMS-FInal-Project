package Dao;

import Model.*;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class CommentsDao {

    //singleton
    public ConnectionManager connectionManager;

    private static CommentsDao instance = null;

    private CommentsDao() {
        connectionManager = new ConnectionManager();
    }

    public static CommentsDao getInstance() {
        if (instance == null) {
            instance = new CommentsDao();
        }
        return instance;
    }

    public Comments create(Comments comment) throws SQLException {
        String query = "INSERT INTO Comments(ProjectID,Content,CreateTime,Available,ReplyToCommentID,DisLikedCount,LikedCount,UserID) VALUES(?,?,?,?,?,?,?,?);";
        Connection connection = null;
        PreparedStatement ps = null;
        ResultSet resultKey = null;
        try {
            connection = connectionManager.getConnection();
            ps = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            ps.setInt(1, comment.getProjectID());
            ps.setString(2, comment.getContent());
            ps.setTimestamp(3, comment.getCreateTime());
            ps.setBoolean(4, comment.isAvailable());
            ps.setInt(5, comment.getReplyToCommentID());
            ps.setInt(6, comment.getDislikedCount());
            ps.setInt(7, comment.getLikedCount());
            ps.setInt(8, comment.getUserID());
            ps.executeUpdate();
            resultKey = ps.getGeneratedKeys();
            int commentID = -1;
            if (resultKey.next()) {
                commentID = resultKey.getInt(1);
            } else {
                throw new SQLException("Unable to retrieve auto-generated key.");
            }
            comment.setCommentID(commentID);
            System.out.println("Successfully created comment ID: " + commentID);
            return comment;
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        } finally {
            if (connection != null) {
                connection.close();
            }
            if (ps != null) {
                ps.close();
            }
        }
    }

    public Comments getCommentsByCommentId(int commentID) throws SQLException {
        String selectComments = "SELECT * FROM Comments WHERE CommentID=?;";
        Connection connection = null;
        PreparedStatement selectStmt = null;
        ResultSet results = null;
        try {
            connection = connectionManager.getConnection();
            selectStmt = connection.prepareStatement(selectComments);
            selectStmt.setInt(1, commentID);
            results = selectStmt.executeQuery();
            if (results.next()) {
                return new Comments(results.getInt(1), results.getInt(2), results.getString(3), results.getTimestamp(4),
                        results.getBoolean(5), results.getInt(6), results.getInt(7), results.getInt(8), results.getInt(9));
            } else {
                return null;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        } finally {
            if (connection != null) {
                connection.close();
            }
            if (selectStmt != null) {
                selectStmt.close();
            }
            if (results != null) {
                results.close();
            }
        }
    }

    public List<Comments> getCommentsByUserID(int userID) throws SQLException {
        List<Comments> result = new ArrayList<Comments>();
        String selectComments = "SELECT * FROM Comments WHERE UserID=?;";
        Connection connection = null;
        PreparedStatement selectStmt = null;
        ResultSet results = null;
        try {
            connection = connectionManager.getConnection();
            selectStmt = connection.prepareStatement(selectComments);
            selectStmt.setInt(1, userID);
            results = selectStmt.executeQuery();
            while (results.next()) {
                result.add(new Comments(results.getInt(1), results.getInt(2), results.getString(3),
                        results.getTimestamp(4),
                        results.getBoolean(5), results.getInt(6), results.getInt(7), results.getInt(8),
                        results.getInt(9)));
            }
            return result;

        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        } finally {
            if (connection != null) {
                connection.close();
            }
            if (selectStmt != null) {
                selectStmt.close();
            }
            if (results != null) {
                results.close();
            }
        }
    }

    public List<Comments> getCommentsByProjectID(int projectID) throws SQLException {
        List<Comments> result = new ArrayList<Comments>();
        String selectComments = "SELECT * FROM Comments WHERE projectID=?;";
        Connection connection = null;
        PreparedStatement selectStmt = null;
        ResultSet results = null;
        try {
            connection = connectionManager.getConnection();
            selectStmt = connection.prepareStatement(selectComments);
            selectStmt.setInt(1, projectID);
            results = selectStmt.executeQuery();
            while (results.next()) {
                result.add(new Comments(results.getInt(1), results.getInt(2), results.getString(3),
                        results.getTimestamp(4),
                        results.getBoolean(5), results.getInt(6), results.getInt(7), results.getInt(8),
                        results.getInt(9)));
            }
            return result;

        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        } finally {
            if (connection != null) {
                connection.close();
            }
            if (selectStmt != null) {
                selectStmt.close();
            }
            if (results != null) {
                results.close();
            }
        }
    }

    public void setAvailable(int commentID, boolean available) throws SQLException {

        String selectComments = "Update Comments SET Available=? WHERE CommentID=?;";
        Connection connection = null;
        PreparedStatement selectStmt = null;
        ResultSet results = null;
        try {
            connection = connectionManager.getConnection();
            selectStmt = connection.prepareStatement(selectComments);
            selectStmt.setBoolean(1, available);
            selectStmt.setInt(2, commentID);
            selectStmt.executeUpdate();
            System.out.println("Successfully updated comment ID: " + commentID + ", set availability = " + available);
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        } finally {
            if (connection != null) {
                connection.close();
            }
            if (selectStmt != null) {
                selectStmt.close();
            }
            if (results != null) {
                results.close();
            }
        }
    }

    public void addDislikeBy1(int commentID) throws SQLException {

        String selectComments = "update comments set DislikedCount = DislikedCount + 1 where commentID =?;";
        Connection connection = null;
        PreparedStatement selectStmt = null;
        ResultSet results = null;
        try {
            connection = connectionManager.getConnection();
            selectStmt = connection.prepareStatement(selectComments);
            selectStmt.setInt(1, commentID);
            selectStmt.executeUpdate();
            System.out.println("Successfully added 1 dislike to comment ID: " + commentID);
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        } finally {
            if (connection != null) {
                connection.close();
            }
            if (selectStmt != null) {
                selectStmt.close();
            }
            if (results != null) {
                results.close();
            }
        }
    }

    // update the whole comment object
    public void updateComment(Comments comment) throws SQLException {
        String updateComment = "UPDATE Comments SET ProjectId = ?, Content= ?, Available= ?, ReplyToCommentID=?," +
                "DislikedCount=?, LikedCount=? WHERE CommentId=?;";
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        try {
            connection = connectionManager.getConnection();
            preparedStatement = connection.prepareStatement(updateComment);
            preparedStatement.setInt(1, comment.getProjectID());
            preparedStatement.setString(2, comment.getContent());
            preparedStatement.setBoolean(3, comment.isAvailable());
            preparedStatement.setInt(4, comment.getReplyToCommentID());
            preparedStatement.setInt(5, comment.getDislikedCount());
            preparedStatement.setInt(6, comment.getLikedCount());
            preparedStatement.setInt(7, comment.getCommentID());
            System.out.println(preparedStatement);
            preparedStatement.executeUpdate();
            System.out.println("Successfully updated comment ID: " + comment.getCommentID());
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        } finally {
            if (connection != null) {
                connection.close();
            }
            if (preparedStatement != null) {
                preparedStatement.close();
            }
            if (preparedStatement != null) {
                preparedStatement.close();
            }
        }
    }


    public void addLikeBy1(int commentID) throws SQLException {

        String selectComments = "update comments set LikedCount = LikedCount + 1 where commentID =?;";
        Connection connection = null;
        PreparedStatement selectStmt = null;
        ResultSet results = null;
        try {
            connection = connectionManager.getConnection();
            selectStmt = connection.prepareStatement(selectComments);
            selectStmt.setInt(1, commentID);
            selectStmt.executeUpdate();
            System.out.println("Successfully added 1 to like comment ID: " + commentID);
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        } finally {
            if (connection != null) {
                connection.close();
            }
            if (selectStmt != null) {
                selectStmt.close();
            }
            if (results != null) {
                results.close();
            }
        }
    }

    public void deleteCommentByCommentID(int commentID) throws SQLException {

        String selectComments = "DELETE FROM Comments WHERE CommentID = ?;";
        Connection connection = null;
        PreparedStatement selectStmt = null;
        ResultSet results = null;
        try {
            connection = connectionManager.getConnection();
            selectStmt = connection.prepareStatement(selectComments);
            selectStmt.setInt(1, commentID);
            selectStmt.executeUpdate();
            System.out.println(selectStmt);
            System.out.println("Successfully deleted comment ID: " + commentID);
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        } finally {
            if (connection != null) {
                connection.close();
            }
            if (selectStmt != null) {
                selectStmt.close();
            }
            if (results != null) {
                results.close();
            }
        }
    }

    // delete comment by userId
    public void deleteCommenByUserId(int userId) throws SQLException {
        String deleteComment = "DELETE FROM Comments WHERE UserID = ?;";
        System.out.println("Delete comment by useID.");
        System.out.println(deleteComment);
        Connection connection = null;
        PreparedStatement deleteStmt = null;
        try {
            connection = connectionManager.getConnection();
            deleteStmt = connection.prepareStatement(deleteComment);
            deleteStmt.setInt(1, userId);
            deleteStmt.executeUpdate();
            System.out.println("Successfully deleted comment by User:" + userId);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            connection.close();
        }
    }
}
