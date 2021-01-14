package pl.edu.pum.shop_list.models;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ShoppingList
{
    private int id;
    private String mListName;
    private Date mDate;
    private List<String> mProducts = new ArrayList<>();

    public ShoppingList() { }

    public void setDate(Date date)
    {
        this.mDate = date;
    }

    public Date getDate()
    {
        return mDate;
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

    public void setProductList(List<String> products)
    {
        this.mProducts = products;
    }

    public void addProduct(String ingredient)
    {
        mProducts.add(ingredient);
    }

    public List<String> getProductsList()
    {
        return mProducts;
    }
}
