
package com.example.android.quakereport;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

public class EarthquakeActivity extends AppCompatActivity {

    /**
     * URL for earthquake data from the USGS dataset
     */
    private static final String USGS_REQUEST_URL =
            "https://earthquake.usgs.gov/fdsnws/event/1/query?format=geojson&eventtype=earthquake&orderby=time&minmag=6&limit=10";

    public static final String LOG_TAG = EarthquakeActivity.class.getName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.earthquake_activity);

        // Create a fake list of earthquake locations.
        earthquakeAsyncTask task = new earthquakeAsyncTask();
        task.execute(USGS_REQUEST_URL);

        /*
        ArrayList<Earthquake> earthquakes = new ArrayList<Earthquake>();
        earthquakes.add(new Earthquake("7.2", "San Francisco", "Feb 2, 2016"));
        earthquakes.add(new Earthquake("6.1", "London", "July 20, 2015"));
        earthquakes.add(new Earthquake("6.1", "Tokyo", "Nov 10, 2015"));
        earthquakes.add(new Earthquake("3.9", "Mexico City", "May 3, 2016"));
        earthquakes.add(new Earthquake("5,4", "Moscow", "jan 30, 2014"));
        earthquakes.add(new Earthquake("2,8", "Rio de Janeiro", "Jan 31, 2013"));
        earthquakes.add(new Earthquake("4,9", "Paris", "Aug 19, 2012"));
        earthquakes.add(new Earthquake("7.2", "San Francisco", "Oct 30, 2011"));
*/


    }

    private class earthquakeAsyncTask extends AsyncTask<String, Void, ArrayList<Earthquake>> {

        /**
         * Esse método é chamado por uma thread em segundo plano, então, podemos executar
         * operações mais lentas como conexões de rede
         * <p>
         * Não é correto atualizar a UI usando uma operação em segundo plano, então, apenas
         * retornamos {@link ArrayList<Earthquake>} como resultado.
         */
        @Override
        protected ArrayList<Earthquake> doInBackground(String... urls) {

            // Não execute o pedido se não houver URLs ou se for nula
            if (urls.length < 1 || urls[0] == null) {
                return null;
            }

            // Execute a solicitação HTTP para dados de terremoto e processe a resposta.

            ArrayList<Earthquake> result = QueryUtils.buscarDadosTerremoto(urls[0]);

            return result;
        }

        /**
         * Esse método é chamado na UI principal depois que o trabalho em segundo plano foi executado.
         * <p>
         * Está correto modificar a UI com esse método. Pegamos o objeto {@link ArrayList<Earthquake>} (que
         * foi retornado do doInBackground()) e atualizamos a tela.
         */
        @Override
        protected void onPostExecute(ArrayList<Earthquake> result) {

            // Se não houver resultados, não faça nada.
            if (result == null) {
                return;
            }

            updateUi(result);
        }

    }

    private void updateUi(ArrayList<Earthquake> ListEarthquake) {

        final EarthquakeAdapter eartquakeAdapter = new EarthquakeAdapter(this, ListEarthquake);

        // Find a reference to the {@link ListView} in the layout
        ListView earthquakeListView = (ListView) findViewById(R.id.list);

        // Seta o adapter na {@link earthquakeListView}
        // para que a lista possa ser populada na interface do usuário
        earthquakeListView.setAdapter(eartquakeAdapter);

        earthquakeListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                                      @Override
                                                      public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {

                                                          // Achar o terremoto atual que foi clicado
                                                          Earthquake currentEarthquake = eartquakeAdapter.getItem(position);

                                                          // Converte o URL String em um objeto URI (para passar no construtor de Intent)
                                                          Uri earthquakeUri = Uri.parse(currentEarthquake.getUrl());

                                                          // Cria um novo intent para visualizar a URI do earthquake
                                                          Intent websiteIntent = new Intent(Intent.ACTION_VIEW, earthquakeUri);

                                                          // Envia o intent para lançar uma nova activity
                                                          startActivity(websiteIntent);

                                                      }
                                                  }
        );
    }


}
