package com.example.baruch.android5779_6256_4843_part2.model.entities;

public class CurrentDriver {

    private static Driver mDriver;
    public static Driver getDriver(){
        return mDriver;
    }
    public static void setDriver(Driver driver){
        mDriver=driver;
    }

}
