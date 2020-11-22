package com.chakra.shoppinglist.database

import android.app.Activity
import android.content.Context
import android.content.res.AssetManager
import android.util.Log
import androidx.annotation.StringRes
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.chakra.shoppinglist.R
import com.chakra.shoppinglist.fragments.SplashScreenActivity
import com.chakra.shoppinglist.model.*
import com.google.gson.Gson
import java.io.IOException


@Database(entities = [Product::class,
    ShoppingPlan::class,
    ShoppingPlanType::class,
    Cart::class,
    TopProducts::class,
    Category::class], version = 1)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun categoryDao(): CategoryDao
    abstract fun productDao(): ProductDao
    abstract fun shoppingPlanDao(): ShoppingPlanDao

    fun initialize(context: Context) {
        Log.d("chakra", "Database init() called")
        val jsonData = readTemplateData(context)
        Log.d("chakra", "Template json: " + jsonData)
        val gson = Gson()
        val templateData = gson.fromJson(jsonData, TemplateData::class.java)
        templateData.shoppingPlanTypes.forEach {
            shoppingPlanDao().insert(it)
        }

        Log.d("chakra", "shopping plan types added")

        val categoryDao = categoryDao()
        val bathroom = Category(context.getString(R.string.category_bathroom), null, true)
        bathroom.id = categoryDao.insert(bathroom).get(0)

        val shampoo = product(context, R.string.product_shampoo, bathroom, "http://i.imgur.com/BM0GFUC.png")
        val toiletPaper = product(context, R.string.product_toiletPaper, bathroom, "http://i.imgur.com/LDI12E4.png")
        val toothbrush = product(context, R.string.product_toothbrush, bathroom, "http://i.imgur.com/Lgz5fTy.png")
        val toothpaste = product(context, R.string.product_toothpaste, bathroom, "http://i.imgur.com/glyrlhS.png")

        val beverages = Category(context.getString(R.string.category_beverage))
        beverages.id = categoryDao.insert(beverages).get(0)

        val beer = product(context, R.string.product_beer, beverages, "http://i.imgur.com/OAYaqkM.png")
        val coffee = product(context, R.string.product_coffee, beverages, "http://i.imgur.com/SCW2fcK.png")
        val soda = product(context, R.string.product_soda, beverages, "http://i.imgur.com/FagEbsU.png")
        val water = product(context, R.string.product_water, beverages, "http://i.imgur.com/XWVwKDK.png")

        val breadAndGrain = Category(context.getString(R.string.category_breadAndGrain))
        breadAndGrain.id = categoryDao.insert(breadAndGrain).get(0)

        val baguette = product(context, R.string.product_baguette, breadAndGrain, "http://i.imgur.com/hkI7tAI.png")
        val cereals = product(context, R.string.product_cereals, breadAndGrain, "http://i.imgur.com/cY6UouB.png")
        val pasta = product(context, R.string.product_pasta, breadAndGrain, "http://i.imgur.com/yzrIBX1.png")
        val rice = product(context, R.string.product_rice, breadAndGrain, "http://i.imgur.com/psXXfuG.png")

        val condiments = Category(context.getString(R.string.category_condimentsAndOthers))
        condiments.id = categoryDao.insert(condiments).get(0)
        val oil = product(context, R.string.product_oil, condiments, "http://i.imgur.com/r2ZecGS.png")
        val pepper = product(context, R.string.product_pepper, condiments, "http://i.imgur.com/SjEIvSI.png")
        val salt = product(context, R.string.product_salt, condiments, "http://i.imgur.com/o14W3sV.png")
        val tomatoSauce = product(context, R.string.product_tomatoSauce, condiments, "http://i.imgur.com/GGnudqb.png")

        val frozen = Category(context.getString(R.string.category_frozen))
        frozen.id = categoryDao.insert(frozen).get(0)
        val frenchFries = product(context, R.string.product_frenchFries, frozen, "http://i.imgur.com/UEeHBCY.png")
        val iceCream = product(context, R.string.product_iceCream, frozen, "http://i.imgur.com/ewXqBfF.png")
        val lasagna = product(context, R.string.product_lasagna, frozen, "http://i.imgur.com/dLp0saj.png")
        val pizza = product(context, R.string.product_pizza, frozen, "http://i.imgur.com/Zist1Zr.png")

        val fruitsAndVegetables = Category(context.getString(R.string.category_fruitsAndVegetables))
        fruitsAndVegetables.id = categoryDao.insert(fruitsAndVegetables).get(0)
        val apples = product(context, R.string.product_apples, fruitsAndVegetables, "http://i.imgur.com/tQRbEZh.png")
        val bananas = product(context, R.string.product_bananas, fruitsAndVegetables, "http://i.imgur.com/9iCHh7G.png")
        val carrots = product(context, R.string.product_carrots, fruitsAndVegetables, "http://i.imgur.com/h8FxUEd.png")
        val potatoes = product(context, R.string.product_potatoes, fruitsAndVegetables, "http://i.imgur.com/f2OsMoE.png")

        val household = Category(context.getString(R.string.category_household))
        household.id = categoryDao.insert(household).get(0)
        val cleaningSupplies = product(context, R.string.product_cleaningSupplies, household, "http://i.imgur.com/mU7N85R.png")
        val dishwashingLiquid = product(context, R.string.product_dishwashingLiquid, household, "http://i.imgur.com/gMZ40BT.png")
        val garbageBags = product(context, R.string.product_garbageBags, household, "http://i.imgur.com/DekDs24.png")
        val laundryDetergent = product(context, R.string.product_laundryDetergent, household, "http://i.imgur.com/oi5EFNa.png")

        val meatAndFish = Category(context.getString(R.string.category_meatAndFish))
        meatAndFish.id = categoryDao.insert(meatAndFish).get(0)
        val chicken = product(context, R.string.product_chicken, meatAndFish, "http://i.imgur.com/Cjsu3Sl.png")
        val fish = product(context, R.string.product_fish, meatAndFish, "http://i.imgur.com/Wzumh0P.png")
        val meat = product(context, R.string.product_meat, meatAndFish, "http://i.imgur.com/4ru5VVy.png")
        val tuna = product(context, R.string.product_tuna, meatAndFish, "http://i.imgur.com/MgrEyWa.png")

        val milkAndCheese = Category(context.getString(R.string.category_milkAndCheese))
        milkAndCheese.id = categoryDao.insert(milkAndCheese).get(0)
        val cheese = product(context, R.string.product_cheese, milkAndCheese, "http://i.imgur.com/ebP7mp2.png")
        val eggs = product(context, R.string.product_eggs, milkAndCheese, "http://i.imgur.com/aIXuDqG.png")
        val milk = product(context, R.string.product_milk, milkAndCheese, "http://i.imgur.com/ERuiwHw.png")
        val yogurt = product(context, R.string.product_yogurt, milkAndCheese, "http://i.imgur.com/mhxVBOA.png")

        val productDao = productDao()
        val ids = productDao.insert( // Bathroom
                shampoo, toiletPaper, toothbrush, toothpaste,  // Beverages
                beer, coffee, soda, water,  // Bread & Grain Products
                baguette, cereals, pasta, rice,  // Condiments & Others
                oil, pepper, salt, tomatoSauce,  // Frozen
                frenchFries, iceCream, lasagna, pizza,  // Fruits & Vegetables
                apples, bananas, carrots, potatoes,  // Household
                cleaningSupplies, dishwashingLiquid, garbageBags, laundryDetergent,  // Meat & Fish
                chicken, fish, meat, tuna,  // Milk & Cheese
                cheese, eggs, milk, yogurt)
        Log.d(TAG, "product ID's: $ids")

        setMigrationDone(context)
    }

    private fun setMigrationDone(context: Context) = context.getSharedPreferences(DATABASE_INIT_PREF, Activity.MODE_PRIVATE).edit().putBoolean(FIELD_MIGRATION_DONE, true).commit()

    private fun readTemplateData(context: Context): String {
        val assetManager: AssetManager = context.assets
        try {
            var inputStream = assetManager.open("template.json")
            return inputStream.bufferedReader().use { it.readText() }
        } catch (e: IOException) {
            Log.e("message: ", e.message!!)
        }
        return ""
    }

    private fun product(context: Context, @StringRes resId: Int, category: Category, imageUrl: String): Product {
        return Product(context.getString(resId), imageUrl, true, category.id)
    }

    companion object {
        // Singleton prevents multiple instances of database opening at the
        // same time.
        @Volatile
        private var INSTANCE: AppDatabase? = null
        const val TAG: String = "AppDatabase"
        const val DATABASE_INIT_PREF = "data_base_init_pref"
        const val FIELD_MIGRATION_DONE = "migration.done"

        @JvmStatic
        fun instance(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                        context.applicationContext,
                        AppDatabase::class.java,
                        "shopping_planner_database"
                ).build()
                INSTANCE = instance
                // return instance
                instance
            }
        }
    }
}