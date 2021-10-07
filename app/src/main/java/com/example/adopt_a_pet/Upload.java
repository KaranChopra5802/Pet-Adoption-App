package com.example.adopt_a_pet;

import com.google.firebase.database.Exclude;

public class Upload {

    public  Upload()
    {

    }

    private String mPetName;
    private String mImageURL;
    private String mPetAge;
    private String mPetDesc;
    private String mKey;
    private String madopt;

    public String getMadopt() {
        return madopt;
    }

    public void setMadopt(String madopt) {
        this.madopt = madopt;
    }

    public Upload(String PetName, String ImageURL, String PetAge, String PetDesc, String Adopt) {
        this.mPetName =  PetName;
        this.mImageURL = ImageURL;
        this.mPetAge = PetAge;
        this.mPetDesc = PetDesc;
        this.madopt = Adopt;
    }

    public String getmPetName() {
        return mPetName;
    }

    public void setmPetName(String mPetName) {
        this.mPetName = mPetName;
    }

    public String getmImageURL() {
        return mImageURL;
    }

    public void setmImageURL(String mImageURL) {
        this.mImageURL = mImageURL;
    }

    public String getmPetAge() {
        return mPetAge;
    }

    public void setmPetAge(String mPetAge) {
        this.mPetAge = mPetAge;
    }

    public String getmPetDesc() {
        return mPetDesc;
    }

    public void setmPetDesc(String mPetDesc) {
        this.mPetDesc = mPetDesc;
    }

    @Exclude
    public String getmKey() {
        return mKey;
    }
    @Exclude
    public void setmKey(String mKey) {
        this.mKey = mKey;
    }
}


