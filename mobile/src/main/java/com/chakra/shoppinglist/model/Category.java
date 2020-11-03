package com.chakra.shoppinglist.model;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity
public class Category implements Serializable
{
    @PrimaryKey
    @NonNull
    public String name;

    public Category(String name)
    {
        this.name = name;
    }

    public String name()
    {
        return name;
    }

    @Override
    public String toString()
    {
        return name();
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o)
        {
            return true;
        }
        else if ((o == null) || (getClass() != o.getClass()))
        {
            return false;
        }

        Category category = (Category) o;

        return name.equals(category.name);
    }

    @Override
    public int hashCode()
    {
        return name.hashCode();
    }
}