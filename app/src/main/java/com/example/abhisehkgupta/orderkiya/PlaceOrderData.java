package com.example.abhisehkgupta.orderkiya;

import java.io.Serializable;

public class PlaceOrderData implements Serializable
{

    public String itemName="",qty = "",itemUnit = "";

    public PlaceOrderData(String itemName,String qty,String itemUnit)
    {
        this.itemName = itemName;
        this.qty = qty;
        this.itemUnit = itemUnit;
    }



}
