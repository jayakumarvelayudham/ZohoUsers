package com.example.quickapp.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.quickapp.R;
import com.example.quickapp.models.Response;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class BottomSheetWeather extends BottomSheetDialogFragment {

    private TextView cloud_tex,tv_name,temperature_txt,wind_text,tv_humidval,speed_text;

    private static Response responseModel;
    public static BottomSheetWeather newInstance(Response responseModel1) {
        responseModel = new Response();
        responseModel = responseModel1;
        return new BottomSheetWeather();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        // get the views and attach the listener

        return inflater.inflate(R.layout.bottomsheet_weather, container,
                false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
//        super.onViewCreated(view, savedInstanceState);
        cloud_tex = view.findViewById(R.id.cloud_tex);
        tv_name = view.findViewById(R.id.tv_name);
        temperature_txt = view.findViewById(R.id.temperature_txt);
        wind_text = view.findViewById(R.id.wind_text);
        tv_humidval = view.findViewById(R.id.tv_humidval);
        speed_text = view.findViewById(R.id.speed_text);

        try {
            if (responseModel != null) {
                cloud_tex.setText(responseModel.getWeather().getMain());
                tv_name.setText(responseModel.getName());
                temperature_txt.setText(String.valueOf(responseModel.getMain().getTemp()));
                wind_text.setText(String.valueOf(responseModel.getWind().getSpeed()));
                tv_humidval.setText(String.valueOf(responseModel.getMain().getHumidity()));
                speed_text.setText(String.valueOf(responseModel.getMain().getPressure()));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
