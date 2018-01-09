package com.example.android.quakereport;

import android.app.Activity;

import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import android.graphics.drawable.GradientDrawable;

/**
 * Created by VALDIR on 04/01/2018.
 */

public class EarthquakeAdapter extends ArrayAdapter<Earthquake> {

    private static final String LOCATION_SEPARATOR = " of ";

    public EarthquakeAdapter(Activity context, ArrayList<Earthquake> earthquakes) {
        super(context, 0, earthquakes);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        // Checa se há um item view de lista existente (chamado convertView) que podemos reutilizar,
        // caso contrário, se convertView for null, inflaremos uma novo item layout de lista.
        View listItemView = convertView;
        if(listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.list_item, parent, false);
        }

        // Acha o terremoto na dada posição na lista de earthquakes
        Earthquake currentEarthquake = getItem(position);

        //Encontre o TextView com a magnitude da ID da visualização
        TextView magTextView = (TextView) listItemView.findViewById(R.id.magnitude);

        // Formate a magnitude para mostrar 1 casa decimal
        String formattedMagnitude = formatMagnitude(currentEarthquake.getMag());

        // Exibe a magnitude do terremoto atual nesse TextView
        magTextView.setText(formattedMagnitude);

        // Defina a cor de fundo apropriada no círculo de magnitude.
        // Obtém o plano de fundo do TextView, que é um GradientDrawable
        GradientDrawable magnitudeCircle = (GradientDrawable) magTextView.getBackground();

        // Obtenha a cor de fundo apropriada com base na magnitude do terremoto atual
        int magnitudeColor = getMagnitudeColor(currentEarthquake.getMag());
        // Define a cor no círculo de magnitude
        magnitudeCircle.setColor(magnitudeColor);

        // Obter a cadeia de localização original do objeto do terremoto,
        // que pode ser no formato de "5 km N do Cairo, Egito" ou "Pacific-Antarctic Ridge"
        String originalLocation = currentEarthquake.getLoc();


        // Se a cadeia de localização original (ou seja, "5 km N do Cairo, Egito") contém
        // uma localização principal (Cairo, Egito) e um deslocamento de localização (5 km N dessa cidade)
        // então armazene a localização principal separadamente da localização deslocada em 2 cordas,
        // para que eles possam ser exibidos em 2 TextViews.
        String primaryLocation;
        String locationOffset;

        // Verifique se a seqüência originalLocation contém o texto "de"
        if (originalLocation.contains(LOCATION_SEPARATOR)) {
            // Divida a string em diferentes partes (como uma matriz de Strings)
            // com base no "de" texto. Esperamos uma variedade de 2 strings, onde
            // a primeira String será "5 km N" e a segunda Cadeia será "Cairo, Egito"
            String[] parts = originalLocation.split(LOCATION_SEPARATOR);
            // O deslocamento de localização deve ser "5 km N" + "de" -> "5 km N de"
            locationOffset = parts[0] + LOCATION_SEPARATOR;
            // Localização primária deve ser "Cairo, Egito"
            primaryLocation = parts[1];
        } else {
            // Caso contrário, não existe nenhum texto "of" na cadeia originalLocation.
            // Por isso, defina o deslocamento de localização padrão para dizer "Perto do".
            locationOffset = getContext().getString(R.string.near_the);
            // A localização principal será a cadeia de localização completa "Pacific-Antarctic Ridge".
            primaryLocation = originalLocation;
        }

        // Encontre o TextView com visualização da localização da ID
        TextView primaryLocationView = (TextView) listItemView.findViewById(R.id.primary_location);
        // Exibe a localização do terremoto atual nesse TextView
        primaryLocationView.setText(primaryLocation);

        // Encontre o TextView com visualização ID localização offset
        TextView locationOffsetView = (TextView) listItemView.findViewById(R.id.location_offset);
        // Exibe o deslocamento da localização do terremoto atual nesse TextView
        locationOffsetView.setText(locationOffset);

        // Cria um novo objeto Date do tempo em milissegundos do terremoto
        Date dateObject = new Date(currentEarthquake.getTimeInMilliseconds());

        //Acha o TextView com view ID de date
        TextView dateTextView = (TextView) listItemView.findViewById(R.id.date);

        // Formata a data string (i.e. "Mar 3, 1984")
        String formattedDate = formatDate(dateObject);
        // Mostra a data do terremoto atual na TextView
        dateTextView.setText(formattedDate);

        // Acha a TextView com view ID de time
        TextView timeView = (TextView) listItemView.findViewById(R.id.time);
        // Formata o tempo em string (i.e. "4:30PM")
        String formattedTime = formatTime(dateObject);

        // Mostra o tempo do terremoto atual na TextView
        timeView.setText(formattedTime);

        // Retorna a item view da lista que agora está mostrando os dados apropriados

        return listItemView;
    }

    /**
     * Retorna a data string formatada (i.e. "Mar 3, 1984") de um objeto Date.
     */
    private String formatDate(Date dateObject) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("LLL dd, yyyy");

        return dateFormat.format(dateObject);
    }


    /**
     * Retorna a data string formatada (i.e. "4:30 PM") de um objeto Date.
     */
    private String formatTime(Date dateObject) {
        SimpleDateFormat timeFormat = new SimpleDateFormat("h:mm a");

        return timeFormat.format(dateObject);
    }


    /*
      * Retorna a string de magnitude formatada mostrando 1 casa decimal (ou seja, "3.2")
      * de um valor de magnitude decimal.
   */
    private String formatMagnitude(double magnitude) {
        DecimalFormat magnitudeFormat = new DecimalFormat("0.0");

        return magnitudeFormat.format(magnitude);
    }


    /**
     * Return the color for the magnitude circle based on the intensity of the earthquake.
     *
     * @param magnitude of the earthquake
     */
    private int getMagnitudeColor(double magnitude) {
        int magnitudeColorResourceId;
        int magnitudeFloor = (int) Math.floor(magnitude);

        switch (magnitudeFloor) {
            case 0:
            case 1:
                magnitudeColorResourceId = R.color.magnitude1;
                break;
            case 2:
                magnitudeColorResourceId = R.color.magnitude2;
                break;
            case 3:
                magnitudeColorResourceId = R.color.magnitude3;
                break;
            case 4:
                magnitudeColorResourceId = R.color.magnitude4;
                break;
            case 5:
                magnitudeColorResourceId = R.color.magnitude5;
                break;
            case 6:
                magnitudeColorResourceId = R.color.magnitude6;
                break;
            case 7:
                magnitudeColorResourceId = R.color.magnitude7;
                break;
            case 8:
                magnitudeColorResourceId = R.color.magnitude8;
                break;
            case 9:
                magnitudeColorResourceId = R.color.magnitude9;
                break;
            default:
                magnitudeColorResourceId = R.color.magnitude10plus;
                break;
        }

        return ContextCompat.getColor(getContext(), magnitudeColorResourceId);
    }
}
