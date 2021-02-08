package com.example.abhisehkgupta.orderkiya;

public class PastOrder
{
    String item_name,qty,unit,status,order_date,deliver_date;
    public PastOrder(String item_name, String qty,String unit ,String status,String order_date,String deliver_date)
    {
        this.item_name = item_name;
        this.qty=qty;
        this.unit=unit;
        this.status=status;
        this.order_date=order_date;
        this.deliver_date=deliver_date;
    }
}
