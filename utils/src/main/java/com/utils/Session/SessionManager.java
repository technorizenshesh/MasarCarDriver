package com.utils.Session;

import android.content.Context;
import android.content.SharedPreferences;


import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.utils.ModelUser;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;

public class SessionManager {
    private final SharedPreferences prf;
    private final SharedPreferences.Editor edit;
    Context context;
    private ModelUser user;

    public static SessionManager get(Context context) {
        return new SessionManager(context);
    }
    public SessionManager(Context context) {
        this.context = context;
        prf=context.getSharedPreferences(SessionKey.DB_MasarTexiDriver.name(), Context.MODE_PRIVATE);
        edit=prf.edit();
    }
    public void setUserType(String type){
        edit.putString(SessionKey.type.name(),type);
        edit.commit();
    }public void setIsOnline(boolean b){
        edit.putBoolean(SessionKey.IsOnline.name(),b);
        edit.commit();
    }
    public String getUserType(){
        return prf.getString(SessionKey.type.name(),"");
    }
    public boolean IsOnline(){
        return prf.getBoolean(SessionKey.IsOnline.name(),false);
    }

  public boolean isUserLogin(){
        return prf.getBoolean(SessionKey.IsUserLogedIn.name(),false);
    }
    public String getLastRequestStatus(){
        return prf.getString(SessionKey.IsShow.name(),"");
    }
    public void setLastRequestStatus(String b){
        edit.putString(SessionKey.IsShow.name(),b);
        edit.commit();
    }public void CreateSession(String result){
        edit.putBoolean(SessionKey.IsUserLogedIn.name(),true);
        edit.putString(SessionKey.UserProfile.name(),result);
        edit.commit();
    }
    public String getAllDetails(){
        return prf.getString(SessionKey.UserProfile.name(),"");
    }
    public String getValue(SessionKey key){
        JSONObject jsonObject = null;
        String result="";
        try {
            jsonObject=new JSONObject(prf.getString(SessionKey.UserProfile.name(),""));
            result=jsonObject.getString(key.name());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return result;
    }
    public ModelUser getValue(){
        if (user==null) {
            String result = prf.getString(SessionKey.UserProfile.name(), "");
            Type listType = new TypeToken<ModelUser>() {
            }.getType();
            user = new GsonBuilder().create().fromJson(result, listType);
        }
        return user;
    }
    public String getUserID(){
        JSONObject jsonObject = null;
        String result="";
        try {
            jsonObject=new JSONObject(prf.getString(SessionKey.UserProfile.name(),""));
            result=jsonObject.getString(SessionKey.id.name());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return result;
    }
    public String  CheckSession(){
        if (prf.getBoolean(SessionKey.IsUserLogedIn.name(),false)){
           return getValue(SessionKey.type);
        }else {
            return "Not Login";
        }
    }
    public void Logout(){
        edit.clear();
        edit.commit();
    }

    public void setLanguage(String lng){
        SharedPreferences lng_prf = context.getSharedPreferences("masar_lng", Context.MODE_PRIVATE);
        SharedPreferences.Editor lng_edit = lng_prf.edit();
        lng_edit.putString(SessionKey.language.name(),lng);
        lng_edit.apply();
    }
 public void setLastSearch(String lng){
      edit.putString("last_key",lng);
      edit.commit();
    }
    public String getLastSearch(){
        return prf.getString("last_key","a");
    }

    public String getSelectedLanguage(){
        SharedPreferences lng_prf = context.getSharedPreferences("masar_lng", Context.MODE_PRIVATE);
        return lng_prf.getString(SessionKey.language.name(),"en");
    }

}

