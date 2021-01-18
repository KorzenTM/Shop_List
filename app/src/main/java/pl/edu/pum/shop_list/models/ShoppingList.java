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
    private List<String> mNumberOfProducts = new ArrayList<>();
    private int mNumberOfProductsBought = 0;

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

    public void addProduct(String ingredient, String how_many)
    {
        mProducts.add(ingredient);
        mNumberOfProducts.add(how_many);
    }

    public List<String> getProductsList()
    {
        return mProducts;
    }

    public void setNumberOfProductsList(List<String> numberOfProductsList)
    {
        this.mNumberOfProducts = numberOfProductsList;
    }

    public List<String> getNumberOfProductsList()
    {
        return mNumberOfProducts;
    }

    public int getNumberOfProductsBought()
    {
        return mNumberOfProductsBought;
    }

    public void incrementNumberOfProductsBought()
    {
        mNumberOfProductsBought++;
    }

    public void decrementNumberOfProductsBought()
    {
        mNumberOfProductsBought--;
        if (mNumberOfProductsBought < 0)
            throw new IllegalArgumentException("Cannot be less than zero!");
    }

    public void setNumberOfProductsBought(int numberOfProductsBought)
    {
        this.mNumberOfProductsBought = numberOfProductsBought;
    }


}
