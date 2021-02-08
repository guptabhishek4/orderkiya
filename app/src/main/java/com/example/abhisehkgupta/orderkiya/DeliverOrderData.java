package com.example.abhisehkgupta.orderkiya;

public class DeliverOrderData
{
    public String deliver_item;
    public String qty;
    public String orderdate;
    public String units;
    public String status;
    public String orderId;
    public boolean checked = false;

    public DeliverOrderData(String deliver_item, String qty, String orderdate, String units, String status)
    {
        this.deliver_item = deliver_item;
        this.qty = qty;
        this.orderdate = orderdate;
        this.units=units;
        this.status=status;
    }

    public DeliverOrderData(String orderId,String deliver_item, String qty, String orderdate, String units, String status)
    {
        this.orderId = orderId;
        this.deliver_item = deliver_item;
        this.qty = qty;
        this.orderdate = orderdate;
        this.units=units;
        this.status=status;
    }
}
