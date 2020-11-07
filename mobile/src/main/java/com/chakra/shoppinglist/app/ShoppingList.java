package com.chakra.shoppinglist.app;

import com.orm.SugarApp;

public class ShoppingList extends SugarApp
{
    @Override
    public void onCreate()
    {
        super.onCreate();

        //Fabric.with(this, new Crashlytics());
    }
}