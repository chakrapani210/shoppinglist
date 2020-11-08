package com.chakra.shoppinglist.utils;

import android.content.Context;

import com.chakra.shoppinglist.model.Product;

public class Analytics
{
    //private final Tracker tracker;

    public Analytics(Context context) {
        /*GoogleAnalytics googleAnalytics = GoogleAnalytics.getInstance(context);
        this.tracker = googleAnalytics.newTracker(R.xml.app_tracker);*/
    }

    public void appLaunched()
    {
        /*tracker.setScreenName("App Launched");
        tracker.send(new HitBuilders.ScreenViewBuilder().build());

        Date date = new Date();
        Calendar calendar = GregorianCalendar.getInstance();
        calendar.setTime(date);
        int hour = calendar.get(Calendar.HOUR_OF_DAY);

        EventBuilder eventBuilder = new EventBuilder();
        eventBuilder.setCategory("TIME_APP_OPEN");
        eventBuilder.setAction(String.valueOf(hour));
        tracker.send(eventBuilder.build());*/
    }

    public void cartItemAdded(Product product)
    {
        /*EventBuilder eventBuilder = new EventBuilder();
        eventBuilder.setCategory("ADDED_PRODUCT_TO_CART");
        eventBuilder.setAction(product.category());
        tracker.send(eventBuilder.build());*/
    }
}