package Servlets;

import Dao.FavoriteDao;
import Dao.NotificationDao;
import Dao.PeopleDao;
import Model.Favorite;
import Model.Notification;
import Model.People;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.List;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;


public class PeopleServlet extends HttpServlet {

    public void doGet(HttpServletRequest req,HttpServletResponse resp) {
      HttpSession session = req.getSession();
      String username = (String) session.getAttribute("UserName");
      int userID = (int) session.getAttribute("userID");
      String URL = req.getRequestURI();
      int actionIndex = URL.lastIndexOf("/");
      String action = URL.substring(actionIndex + 1);
      System.out.println(action);
      PeopleDao peopleDao = PeopleDao.getInstance();
      switch (action) {
        case "getPeople":
          try {
            List<People> PeopleList = peopleDao.getRandomPeople();
            req.setAttribute("peopleList", PeopleList);
            req.getRequestDispatcher("./People.jsp").forward(req, resp);
          } catch (Exception e) {
            System.out.println(e);
          }
          break;
        case "SearchPeople":
          try {
            String PeolpeIDStr= req.getParameter("PeopleID");
            int PeopleID= 0;
            if(PeolpeIDStr.length()!=0) {
              PeopleID = Integer.valueOf(PeolpeIDStr);
            }
            String FirstName= req.getParameter("FirstName");
            String LastName= req.getParameter("LastName");
            String Occupation= req.getParameter("Occupation");
            String RateStr= req.getParameter("Rate");
            int Rate = 0;
            if(RateStr.length()!=0) {
             Rate = Integer.valueOf(RateStr);
            }
            List<People> PeopleList = peopleDao.getPeopleByCondition(PeopleID,FirstName,LastName,Occupation,Rate);
            req.setAttribute("peopleList", PeopleList);
            req.getRequestDispatcher("./People.jsp").forward(req, resp);
          } catch (Exception e) {
            System.out.println(e);
          }
          break;

        case "ContactPeople":
          try {

            List<People> PeopleList = peopleDao.getRandomPeople();
            NotificationDao notificationDao = NotificationDao.getInstance();
            notificationDao.create(new Notification(new Timestamp(System.currentTimeMillis()), username +" is interested in You !",false, "normal",
                peopleDao.getPeopleByID(Integer.valueOf(req.getParameter("PeopleID"))).getUserID()));
            req.setAttribute("contactInfo","Contact Notification successfully sent to People "+req.getParameter("PeopleID"));
            req.setAttribute("peopleList", PeopleList);
            req.getRequestDispatcher("./People.jsp").forward(req, resp);
          } catch (Exception e) {
            System.out.println(e);
          }
          break;

        case "PublishPeople":
          try {
            String firstName = req.getParameter("firstName");
            String lastName = req.getParameter("lastName");
            String description = req.getParameter("description");
            String occupation = req.getParameter("occupation");
            int rate = Integer.valueOf(req.getParameter("hourlyRate"));
            Date date = new Date(0);
            People result = peopleDao.create(new People(firstName,lastName,description,occupation,date,rate,userID));
            List<People> PeopleList = peopleDao.getRandomPeople();
            req.setAttribute("publishInfo","Successfully published yourself! Your ID is " + result.getPeopleID());
            req.setAttribute("peopleList", PeopleList);
            req.getRequestDispatcher("./People.jsp").forward(req, resp);
          } catch (Exception e) {
            System.out.println(e);
          }
          break;

        case "AddFavPeople":
          try {
            int peopleID = Integer.valueOf(req.getParameter("PeopleID"));
            FavoriteDao favoriteDao = FavoriteDao.getInstance();
            favoriteDao.createFavorite(new Favorite("People",peopleID,userID));
            List<People> PeopleList = peopleDao.getRandomPeople();
            req.setAttribute("addFav","Successfully add favorite People "+req.getParameter("PeopleID"));
            req.setAttribute("peopleList", PeopleList);
            req.getRequestDispatcher("./People.jsp").forward(req, resp);
          } catch (Exception e) {
            System.out.println(e);
          }
          break;
      }
    }
    public void doPost(HttpServletRequest req,HttpServletResponse resp){
      doGet(req,resp);
    }
}
