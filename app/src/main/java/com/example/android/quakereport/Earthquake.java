package com.example.android.quakereport;

/**
 * Created by VALDIR on 04/01/2018.
 */

public class Earthquake {

    //Magnitude do terremoto
    private Double mMag;

    //Local do registro
    private String mLoc;

    //Tempo do terremoto
    private long mTimeInMilliseconds;

    /** URL do website do terremoto */
    private String mUrl;

    /*
    * Criando um novo objeto Earthquake
    *
    * @param mMag  é a magnitude (tamanho) do terremoto
    * @param mLoc é a localização da cidade do terremoto
    * @param mtimeInMilliseconds é o tempo em milissegundos (da época) quando o
    *                           terremoto aconteceu
    *@param mUrl é o URL do website para achar mais detalhes sobre o earthquake
    * */
    public Earthquake(Double mag, String loc, long timeInMilliseconds, String url) {
        this.mMag = mag;
        this.mLoc = loc;
        this.mTimeInMilliseconds = timeInMilliseconds;
        this.mUrl = url;
    }


    //obter a Magnitude
    public Double getMag() {
        return mMag;
    }

    //obter a localidade
    public String getLoc() {
        return mLoc;
    }

    //obter a data
    public long getTimeInMilliseconds() {
        return mTimeInMilliseconds;
    }
    /**
     * Obter a URL do website para achar mais informações sobre o earthquake.
     */
    public String getUrl() {
        return mUrl;
    }
}