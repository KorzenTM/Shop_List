package pl.edu.pum.shop_list.handlers;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import pl.edu.pum.shop_list.models.ShoppingList;

public class DBHandler extends SQLiteOpenHelper
{
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "shoppingListsDB_JAVA.db";

    public static final String TABLE_SHOPPING_LIST = "shopping_lists";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_LIST_NAME = "listName";
    public static final String COLUMN_DATE = "date";
    public static final String COLUMN_PRODUCTS = "products";

    public DBHandler(Context context)
    {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase)
    {
        String CREATE_SHOPPING_LISTS_TABLE = "CREATE TABLE " +
                TABLE_SHOPPING_LIST +
                "(" +
                COLUMN_ID + " " +
                "INTEGER PRIMARY KEY," +
                COLUMN_LIST_NAME +
                " TEXT," +
                COLUMN_DATE +
                " TEXT," +
                COLUMN_PRODUCTS +
                " TEXT" +
                ")";
        sqLiteDatabase.execSQL(CREATE_SHOPPING_LISTS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1)
    {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_SHOPPING_LIST);
        onCreate(sqLiteDatabase);
    }

    public Cursor getShoppingLists()
    {
        String query = "SELECT * FROM " + TABLE_SHOPPING_LIST;
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery(query, null);
    }

    public void addShoppingList(ShoppingList shoppingList)
    {
        ContentValues values = new ContentValues();
        values.put(COLUMN_LIST_NAME, shoppingList.getListName());
        values.put(COLUMN_DATE, shoppingList.getDate().toString());
        values.put(COLUMN_PRODUCTS, String.valueOf(shoppingList.getProductsList()));

        SQLiteDatabase db = this.getWritableDatabase();

        db.insert(TABLE_SHOPPING_LIST, null, values);
        db.close();
    }

    public void deleteShoppingList(String listName)
    {
        String query = "Select * FROM " +
                TABLE_SHOPPING_LIST +
                " WHERE " +
                COLUMN_LIST_NAME +
                "= \"" +
                listName +
                "\"";

        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst())
        {
            int currentID = Integer.parseInt(cursor.getString(0));
            db.delete(TABLE_SHOPPING_LIST, COLUMN_ID + " = ?",
                    new String[]{String.valueOf(currentID)});
        }
        db.close();
    }

    public void updateShoppingList(int id, String listName, String date)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_ID, id);
        contentValues.put(COLUMN_LIST_NAME, listName);
        contentValues.put(COLUMN_DATE, date);

        db.update(TABLE_SHOPPING_LIST, contentValues, COLUMN_ID + "=" + id, null);
        db.close();
    }

    public void updateProducts(ShoppingList shoppingList)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_ID, shoppingList.getId());
        contentValues.put(COLUMN_PRODUCTS, String.valueOf(shoppingList.getProductsList()));

        db.update(TABLE_SHOPPING_LIST, contentValues, COLUMN_ID + "=" + shoppingList.getId(), null);
        db.close();
    }
}
