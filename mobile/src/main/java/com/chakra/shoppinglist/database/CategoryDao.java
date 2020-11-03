package com.chakra.shoppinglist.database;

import android.content.Context;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.chakra.shoppinglist.model.Category;

import java.util.List;

@Dao
public interface CategoryDao
{
    @Query("SELECT * FROM Category")
    List<Category> all();

    @Query("SELECT EXISTS(SELECT * FROM Category WHERE name=:name)")
    boolean contains(String name);

    @Query("UPDATE Category SET name=:newName WHERE name=:oldName")
    void rename(String oldName, String newName);

    @Insert
    void insert(Category... categories);

    @Delete
    void delete(Category category);

    static CategoryDao instance(Context context)
    {
        return AppDatabase.instance(context).categoryDao();
    }
}