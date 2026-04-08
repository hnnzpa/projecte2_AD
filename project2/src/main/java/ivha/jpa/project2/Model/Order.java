package ivha.jpa.project2.Model;

import java.sql.Timestamp;

public class Order {

    private int id;
    private Timestamp orderDate;
    private float totalAmount;
    private String orderStatus;
    private boolean status;
    private Timestamp dataCreated;
    private Timestamp dataUpdated;
}
