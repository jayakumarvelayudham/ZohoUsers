package com.example.quickapp.fragments;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.bumptech.glide.Glide;
import com.example.quickapp.R;
import com.example.quickapp.common.MySingleton;
import com.example.quickapp.models.Coord;
import com.example.quickapp.models.Main;
import com.example.quickapp.models.Response;
import com.example.quickapp.models.ResultsItem;
import com.example.quickapp.models.WeatherItem;
import com.example.quickapp.models.Wind;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class RandomUserDetails extends Fragment {

    private TextView first_name,location,designation,phno_num;
    private ImageView user_profile;
    private ResultsItem resultsItem;
    private TextView cloud_tex,tv_name,temperature_txt,wind_text,tv_humidval,speed_text;
    private Response responseModel = new Response();
    private ProgressDialog pDialog;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return LayoutInflater.from(getContext()).inflate(R.layout.random_user_details,container,false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        ResultsItem resultsItem = (ResultsItem) requireArguments().getSerializable("resultsItem");
        first_name = view.findViewById(R.id.first_name);
        location = view.findViewById(R.id.location);
        phno_num = view.findViewById(R.id.phno_num);
        designation = view.findViewById(R.id.designation);
        user_profile = view.findViewById(R.id.user_profile);

        cloud_tex = view.findViewById(R.id.cloud_tex);
        tv_name = view.findViewById(R.id.tv_name);
        temperature_txt = view.findViewById(R.id.temperature_txt);
        wind_text = view.findViewById(R.id.wind_text);
        tv_humidval = view.findViewById(R.id.tv_humidval);
        speed_text = view.findViewById(R.id.speed_text);

        if(resultsItem!=null){
            first_name.setText(resultsItem.getName().getFirst());
            location.setText(resultsItem.getLocation().getCity()+","+resultsItem.getNat());
            designation.setText(resultsItem.getEmail());
            phno_num.setText(resultsItem.getPhone());
            Glide.with(requireContext()).load(resultsItem.getPicture().getLarge()).into(user_profile);
            String weatherUrl = RandomUserList.API_WEATHER_REPORT+"?lat="+resultsItem.getLocation().getCoordinates().getLatitude()+"&lon="+resultsItem.getLocation().getCoordinates().getLongitude()+"&appid="+RandomUserList.APP_ID;
            //showLoader();
            callWeatherReport(weatherUrl);
        }
    }

    private void callWeatherReport(String url){
        RequestQueue queue = MySingleton.getInstance(requireContext().getApplicationContext()).
                getRequestQueue();

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new com.android.volley.Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            //dismissLoader();
                            responseModel = new Response();
                            JSONObject jsonObject = new JSONObject(response);

                            JSONObject joCoord = jsonObject.optJSONObject("coord");
                            if(joCoord!=null){
                                Coord coord = new Coord();
                                coord.setLon(joCoord.optDouble("lon",0.0));
                                coord.setLat(joCoord.optDouble("lat",0.0));
                                responseModel.setCoord(coord);
                            }

                            JSONArray jaWeather = jsonObject.optJSONArray("weather");
                            if(jaWeather!=null && jaWeather.length()>0){
                                WeatherItem weatherItem = new WeatherItem();
                                JSONObject joWeather = jaWeather.optJSONObject(0);
                                weatherItem.setMain(joWeather.optString("main",""));
                                weatherItem.setDescription(joWeather.optString("description",""));
                                responseModel.setWeather(weatherItem);
                            }

                            responseModel.setName(jsonObject.optString("name",""));

                            JSONObject joWind = jsonObject.optJSONObject("wind");
                            if(joWind!=null){
                                Wind wind = new Wind();
                                wind.setSpeed(joWind.optDouble("speed",0.0));
                                responseModel.setWind(wind);
                            }

                            JSONObject joMain = jsonObject.optJSONObject("main");
                            if(joMain!=null){
                                Main main = new Main();
                                main.setPressure(joMain.optInt("pressure",0));
                                main.setHumidity(joMain.optInt("humidity",0));
                                main.setTemp(joMain.optDouble("temp",0.0));
                                responseModel.setMain(main);
                            }

                            cloud_tex.setText(responseModel.getWeather().getMain());
                            tv_name.setText(responseModel.getName());
                            temperature_txt.setText(String.valueOf(responseModel.getMain().getTemp()));
                            wind_text.setText(String.valueOf(responseModel.getWind().getSpeed()));
                            tv_humidval.setText(String.valueOf(responseModel.getMain().getHumidity()));
                            speed_text.setText(String.valueOf(responseModel.getMain().getPressure()));

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println(error.toString());
            }
        });

        MySingleton.getInstance(requireContext()).addToRequestQueue(stringRequest);
    }

    private void showLoader() {
        pDialog = new ProgressDialog(getContext());
        pDialog.setMessage("Loading...");
        pDialog.setCancelable(false);
        pDialog.show();
    }

    private void dismissLoader() {
        if (pDialog != null && pDialog.isShowing()) {
            pDialog.dismiss();
        }
    }
}
