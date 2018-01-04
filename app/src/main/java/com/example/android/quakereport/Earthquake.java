package com.example.android.quakereport;

/**
 * Created by VALDIR on 04/01/2018.
 */

public class Earthquake {

    //Magnitude do terremoto
    private String mMag;

    //Local do registro
    private String mLoc;

    //Data do ocorrido
    private String mDat;

    /*
    * Criando um novo objeto Earthquake
    *
    * @param mMag é a magnitude na escala richster
    * @param Loc é o local onde foi registrado o terremoto
    * @param mDat é a data em que ocorreu o terremoto
    * */

    public Earthquake(String mMag, String mLoc, String mDat) {
        this.mMag = mMag;
        this.mLoc = mLoc;
        this.mDat = mDat;
    }


    //obter a Magnitude
    public String getmMag() {
        return mMag;
    }

    //obter a localidade
    public String getmLoc() {
        return mLoc;
    }

    //obter a data
    public String getmDat() {
        return mDat;
    }
}
