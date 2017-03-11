package app.lastmineat.models;

/**
 * Created by andrew.lim.2013 on 11/3/2017.
 */

public class Redemption {
    private int id;
    private Deal deal;
    private User user;
    private String dateTime;

    public Redemption(int id, Deal deal, User user, String dateTime) {
        this.id = id;
        this.deal = deal;
        this.user = user;
        this.dateTime = dateTime;
    }

    public int getId() {
        return id;
    }

    public Deal getDeal() {
        return deal;
    }

    public User getUser() {
        return user;
    }

    public String getDateTime() {
        return dateTime;
    }
}
