package com.example.android.quakereport;

import android.text.TextUtils;
import android.util.EventLog;
import android.util.Log;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;

import static com.example.android.quakereport.EarthquakeActivity.LOG_TAG;

/**
 * Helper methods related to requesting and receiving earthquake data from USGS.
 */
public final class QueryUtils {

    /**
     * Crie um construtor privado porque ninguém deve criar um objeto {@link QueryUtils}.
     * Esta classe destina-se apenas a manter variáveis estáticas e métodos, aos quais pode ser acessado
     * diretamente do nome da classe QueryUtils (e uma instância de objeto do QueryUtils não é necessária).
     */
    private QueryUtils() {
    }

    /** 
    * Retorne uma Arraylist de objetos {@link Earthquake} que foram construídos a partir da 
    * analise da resposta JSON
    */
    public static ArrayList<Earthquake> buscarDadosTerremoto(String requisicaoUrl) {
        // Cria um objeto URL
        URL url = criarUrl(requisicaoUrl);

        String respostaJson = null;

        // Execute a solicitação HTTP para o URL e receba uma resposta JSON de volta
        try {
            respostaJson = requisicaoHttp(url);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Erro ao fechar o fluxo de entrada", e);
        }

        // Extraia campos relevantes da resposta JSON e retorne arraylist {@link Earthquake}
        ArrayList<Earthquake> earthquakes = extrairArrayJson(respostaJson);

        // Retornar uma lista de objetos {@link Earthquake}
        return earthquakes;
    }

    /**
    * Retorna o novo URL do URL fornecido.
    */
    private static URL criarUrl(String stringUrl) {
        URL url = null;

        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Erro com a criação de URL ", e);
        }
        return url;
    }

    /**
     * Faça uma solicitação HTTP para o URL fornecido e devolva um String como a resposta dos terremotos.
     */
    private static String requisicaoHttp(URL url) throws IOException {
        String respostaJson = "";

        // Se o URL for nulo, volte mais cedo.
        if (url == null) {
            return respostaJson;
        }

        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(10000 /* milisegundos */);
            urlConnection.setConnectTimeout(15000 /* milisegundos */);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            // Se a solicitação foi bem sucedida (código de resposta 200),
            // então leia o fluxo de entrada e analise a resposta.
            if (urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
                respostaJson = lerStream(inputStream);
            } else {
                Log.e(LOG_TAG, "Código de resposta de erro: " + urlConnection.getResponseCode());
            }
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problema ao recuperar os resultados do terremoto JSON.", e);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                inputStream.close();
            }
        }
        return respostaJson;
    }

    /**
     * Converta o {@link InputStream} em uma String que contém toda a resposta JSON do servidor.
     */
    private static String lerStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }

    /**
     * Retornar uma lista de Objetos {@link ArrayList<Earthquake>}, analisando as informações da resposta EarthquakeJSON convertida em String.
     */
    private static ArrayList<Earthquake> extrairArrayJson(String earthquakeJSON) {


        // Se a String JSON estiver vazio ou nulo, volte mais cedo.
        if (TextUtils.isEmpty(earthquakeJSON)) {
            return null;
        }

        try {

            //Crie um ArrayList vazio para que possamos começar a adicionar os terremotos
            ArrayList<Earthquake> earthquakes = new ArrayList<>();

            // Extraia campos relevantes da resposta JSON e retorne arraylist {@link earthquakeArray}
            JSONObject objetoRespostaJson = new JSONObject(earthquakeJSON);
            JSONArray earthquakeArray = objetoRespostaJson.getJSONArray("features");

            for (int i = 0; i < earthquakeArray.length(); i++) {
                JSONObject currentEarthquake = earthquakeArray.getJSONObject(i);
                JSONObject properties = currentEarthquake.getJSONObject("properties");

                double magnitude = properties.getDouble("mag");
                String location = properties.getString("place");
                long time = properties.getLong("time");

                // Extrai o valor da chave chamada "url"
                String url = properties.getString("url");

                Earthquake earthquake = new Earthquake(magnitude, location, time, url);
                earthquakes.add(earthquake);
            }

            // Crie uma lista de Objetos {@link ArrayList<Earthquake>}
            return new ArrayList<Earthquake>(earthquakes);
        } catch (JSONException e) {
            Log.e(LOG_TAG, "Problema ao analisar os resultados dos terremotos JSON", e);
        }
        return null;
    }
}
