package pl.edu.pum.shop_list.models;

import java.util.ArrayList;
import java.util.Date;

public class ShoppingList
{
    private int id;
    private String mListName;
    private Date date;

    public ShoppingList()
    {
        date = new Date();
    }

    public int getId()
    {
        return id;
    }

    public void setId(int id)
    {
        this.id = id;
    }

    public String getListName()
    {
        return mListName;
    }

    public void setListName(String mListName)
    {
        this.mListName = mListName;
    }

    public Date getDate()
    {
        return date;
    }
}
