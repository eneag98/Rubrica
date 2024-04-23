package backend;

public class Utente {
    private String first;
    private String last;
    private String username;
    private String password;
    private boolean admin;

    public Utente(String username, String password){
        this.first = null;
        this.last = null;
        this.username = username;
        this.password = password;
        admin = false;
    }

    public Utente(String first, String last, boolean admin){
        this.first = first;
        this.last = last;
        this.username = null;
        this.password = null;
        this.admin = admin;
    }

    public String getFirst() {
        return first;
    }

    public void setFirst(String first) {
        this.first = first;
    }

    public String getLast() {
        return last;
    }

    public void setLast(String last) {
        this.last = last;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isAdmin() {
        return admin;
    }

    public void setAdmin(boolean admin) {
        this.admin = admin;
    }
}
