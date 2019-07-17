package com.dreamworld.smart.diary;

import android.content.Context;
import android.content.SharedPreferences;

public class Shared {
    private String email;
    Context context;
    SharedPreferences sharedPreferences;
    public Shared(Context context){
        this.context=context;
        sharedPreferences=context.getSharedPreferences("login_details",context.MODE_PRIVATE);
    }

    public  void setNightModeState(Boolean state){
        SharedPreferences.Editor editor=sharedPreferences.edit();
        editor.putBoolean("NightMode",state);
        editor.commit();
    }
    public Boolean lodeNightModeState(){
        Boolean state= sharedPreferences.getBoolean("NightMode",false);
        return state;
    }

    public String getEmail() {
       // return email;
        sharedPreferences.getString("name","");
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
        sharedPreferences.edit().putString("name",email).commit();
    }
    public void removeUser(){
        sharedPreferences.edit().clear().commit();
    }
}
