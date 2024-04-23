package backend;

public class Persona {
    private String first;
    private String last;
    private String address;
    private String phone;
    private int age;

    public Persona(String first, String last, String address, String phone, int age) {
        this.first = first;
        this.last = last;
        this.address = address;
        this.phone = phone;
        this.age = age;
    }

    public Persona() {
        this.first = "";
        this.last = "";
        this.address = "";
        this.phone = "";
        this.age = -1;
    }

    public Persona(String[] values) {
        if (values.length == 5) {
            this.first = values[0];
            this.last = values[1];
            this.address = values[2];
            this.phone = values[3];
            this.age = Integer.parseInt(values[4]);
        } else {
            System.out.println("Non è stato possibile creare la persona...");
            new Persona();
        }
    }

    public boolean same(Persona other) {
        if (!this.first.equals(other.getFirst()))
            return false;
        if (!this.last.equals(other.getLast()))
            return false;
        if (!this.address.equals(other.getAddress()))
            return false;
        if (!this.phone.equals(other.getPhone()))
            return false;
        if (this.age != other.getAge())
            return false;

        return true;
    }

    public String toString() {
        return  "\nNome: "+this.first+
                "\nCognome: "+this.last+
                "\nIndirizzo: "+this.address+
                "\nTelefono: "+this.phone+
                "\nEtà: "+this.age;
    }

    public String getSortingInfo() {
        return this.last.toLowerCase()+this.first.toLowerCase();
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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }
}
