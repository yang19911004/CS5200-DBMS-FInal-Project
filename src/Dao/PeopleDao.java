package Dao;

import Model.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class PeopleDao {
  //singleton
  public ConnectionManager connectionManager;

  private static PeopleDao instance = null;

  private PeopleDao() {
    connectionManager = new ConnectionManager();
  }

  public static PeopleDao getInstance() {
    if (instance == null) {
      instance = new PeopleDao();
    }
    return instance;
  }

  public People create(People people) throws SQLException {
    String query = "INSERT INTO People(FirstName,LastName,Occupation,Description,DOB,HourlyRate,UserID) VALUES(?,?,?,?,?,?,?);";
    Connection connection = null;
    PreparedStatement ps = null;
    ResultSet resultKey = null;
    try {
      connection = connectionManager.getConnection();
      ps = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
      ps.setString(1, people.getFirstName());
      ps.setString(2, people.getLastName());
      ps.setString(3, people.getOccupation());
      ps.setString(4, people.getDescription());
      ps.setDate(5, people.getDob());
      ps.setInt(6, people.getHourlyRate());
      ps.setInt(7, people.getUserID());
      ps.execute();
      resultKey = ps.getGeneratedKeys();
      int PeopleID = -1;
      if (resultKey.next()) {
        PeopleID = resultKey.getInt(1);
      } else {
        throw new SQLException("Unable to retrieve auto-generated key.");
      }
      people.setPeopleID(PeopleID);
      System.out.println("Successfully created People ID: " + PeopleID);
      return people;
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

  public List<People> getPeopleByOccupation(String occupation) throws SQLException {
    List<People> result = new ArrayList<>();
    String selectPeople = "select * from People where Occupation=?;";
    Connection connection = null;
    PreparedStatement selectStmt = null;
    ResultSet results = null;
    try {
      connection = connectionManager.getConnection();
      selectStmt = connection.prepareStatement(selectPeople);
      selectStmt.setString(1, occupation);
      results = selectStmt.executeQuery();
      while (results.next()) {
        result.add(new People(results.getInt(1), results.getString(2), results.getString(3),
            results.getString(4), results.getString(5), results.getDate(6), results.getInt(7),
            results.getInt(8)));
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

  public People getPeopleByID(int peopleID) throws SQLException {
    String selectPeople = "select * from People where PeopleID=?;";
    Connection connection = null;
    PreparedStatement selectStmt = null;
    ResultSet results = null;
    try {
      connection = connectionManager.getConnection();
      selectStmt = connection.prepareStatement(selectPeople);
      selectStmt.setInt(1, peopleID);
      results = selectStmt.executeQuery();
      if(results.next()) {
        return new People(results.getInt(1), results.getString(2), results.getString(3),
            results.getString(4), results.getString(5), results.getDate(6), results.getInt(7),
            results.getInt(8));
      }
      else return null;
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

  public List<People> getRandomPeople() throws SQLException {
    List<People> result = new ArrayList<>();
    String selectPeople = "select * from People Order By rand() LIMIT 20;";
    Connection connection = null;
    PreparedStatement selectStmt = null;
    ResultSet results = null;
    try {
      connection = connectionManager.getConnection();
      selectStmt = connection.prepareStatement(selectPeople);
      results = selectStmt.executeQuery();
      while (results.next()) {
        result.add(new People(results.getInt(1), results.getString(2), results.getString(3),
            results.getString(4), results.getString(5), results.getDate(6), results.getInt(7),
            results.getInt(8)));
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

  public List<People> getPeopleByHourlyRate(boolean ifLarger,int amount) throws SQLException {
    List<People> result = new ArrayList<>();
    String selectPeople;
    if(ifLarger) {
       selectPeople= "select * from People where HourlyRate>=?;";
    }else{
       selectPeople = "select * from People where HourlyRate<=?;";
    }
    Connection connection = null;
    PreparedStatement selectStmt = null;
    ResultSet results = null;
    try {
      connection = connectionManager.getConnection();
      selectStmt = connection.prepareStatement(selectPeople);
      selectStmt.setInt(1, amount);
      results = selectStmt.executeQuery();
      while (results.next()) {
        result.add(new People(results.getInt(1), results.getString(2), results.getString(3),
            results.getString(4), results.getString(5), results.getDate(6), results.getInt(7),
            results.getInt(8)));
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

  public List<People> getPeopleByDescriptionSearch(String keyword) throws SQLException {
    List<People> result = new ArrayList<>();
    String selectPeople= "select * from People where Description LIKE %?%;";

    Connection connection = null;
    PreparedStatement selectStmt = null;
    ResultSet results = null;
    try {
      connection = connectionManager.getConnection();
      selectStmt = connection.prepareStatement(selectPeople);
      selectStmt.setString(1, keyword);
      results = selectStmt.executeQuery();
      while (results.next()) {
        result.add(new People(results.getInt(1), results.getString(2), results.getString(3),
            results.getString(4), results.getString(5), results.getDate(6), results.getInt(7),
            results.getInt(8)));
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

  public void UpdatePeople(int PeopleID,People newPeople) throws SQLException {
    String selectPeople= "UPDATE People SET FirstName=?, LastName=?,Occupation=?,Description=?,DOB=?,HourlyRate=? where PeopleID=?;";
    Connection connection = null;
    PreparedStatement ps = null;
    ResultSet results = null;
    try {
      connection = connectionManager.getConnection();
      ps = connection.prepareStatement(selectPeople);
      ps.setString(1, newPeople.getFirstName());
      ps.setString(2, newPeople.getLastName());
      ps.setString(3, newPeople.getOccupation());
      ps.setString(4, newPeople.getDescription());
      ps.setDate(5, newPeople.getDob());
      ps.setInt(6, newPeople.getHourlyRate());
      ps.setInt(7, PeopleID);
      ps.executeUpdate();
      System.out.println("Successfully UPdated People "+ PeopleID);
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
      if (results != null) {
        results.close();
      }
    }
  }

  public void deletePeopleByID(int peopleID) throws SQLException {
    String query = "DELETE FROM People where PeopleID=?;";
    Connection connection = null;
    PreparedStatement selectStmt = null;
    try {
      connection = connectionManager.getConnection();
      selectStmt = connection.prepareStatement(query);
      selectStmt.setInt(1, peopleID);
      selectStmt.execute();
      System.out.println("Successfully deleted People " + peopleID);
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
    }
  }

  public List<People> getPeopleByCondition(int peopleID,String firstName, String lastName, String occupation,int rate) throws SQLException {
    List<People> result = new ArrayList<>();
    String selectPeople = "select * from People where PeopleID>0";
    Connection connection = null;
    PreparedStatement selectStmt = null;
    ResultSet results = null;
    try {
      connection = connectionManager.getConnection();

      if(peopleID>0) {
        selectPeople += (" AND PeopleID=" + peopleID);
      }
      if(firstName.length()>0) {
        selectPeople += (" AND FirstName=" + firstName+"'");
      }
      if(lastName.length()>0) {
        selectPeople += (" AND LastName='" + lastName+"'");
      }
      if(occupation.length()>0) {
        selectPeople += (" AND Occupation='" + occupation)+"'";
      }
      if(rate>0) {
        selectPeople += ( " AND HourlyRate<=" + rate);
      }
      selectStmt = connection.prepareStatement(selectPeople);
      System.out.println(selectStmt);
      results = selectStmt.executeQuery();
      while (results.next()) {
        result.add(new People(results.getInt(1), results.getString(2), results.getString(3),
            results.getString(4), results.getString(5), results.getDate(6), results.getInt(7),
            results.getInt(8)));
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
}
