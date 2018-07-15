package org.litespring.service.v3;

/**
 * Created by wjt on 2018/6/30.
 */
public class PetStoreService2 {

    private int anInt;

    private String aString;


    public PetStoreService2() {
    }

    public PetStoreService2(int anInt, String aString) {
        this.anInt = anInt;
        this.aString = aString;
    }

    public int getAnInt() {
        return anInt;
    }

    public void setAnInt(int anInt) {
        this.anInt = anInt;
    }

    public String getaString() {
        return aString;
    }

    public void setaString(String aString) {
        this.aString = aString;
    }
}
