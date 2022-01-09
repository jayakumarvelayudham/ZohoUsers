package com.example.quickapp.fragments;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.bumptech.glide.Glide;
import com.example.quickapp.common.CommonUtil;
import com.example.quickapp.dao.RandomUserDao;
import com.example.quickapp.database.InMobiDB;
import com.example.quickapp.entity.RandomUser;
import com.example.quickapp.models.Coord;
import com.example.quickapp.models.Coordinates;
import com.example.quickapp.models.Login;
import com.example.quickapp.common.MySingleton;
import com.example.quickapp.models.Main;
import com.example.quickapp.models.Name;
import com.example.quickapp.models.Picture;
import com.example.quickapp.R;
import com.example.quickapp.models.Registered;
import com.example.quickapp.models.Response;
import com.example.quickapp.models.ResultsItem;
import com.example.quickapp.models.Street;
import com.example.quickapp.models.Timezone;
import com.example.quickapp.models.Dob;
import com.example.quickapp.models.Id;
import com.example.quickapp.models.Location;
import com.example.quickapp.models.WeatherItem;
import com.example.quickapp.models.Wind;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationAvailability;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class RandomUserList extends Fragment {

    public static String API_RANDOM_USER = "https://randomuser.me/api/?results=25";
    public static String API_WEATHER_REPORT = "https://api.openweathermap.org/data/2.5/weather";
    private RecyclerView rv_randomuser;
    private ArrayList<ResultsItem> resultsItemArrayList = new ArrayList<ResultsItem>();
    private ArrayList<ResultsItem> resultsItemArrayListAll = new ArrayList<ResultsItem>();
    private ArrayList<ResultsItem> resultsItemArrayListTemp = new ArrayList<ResultsItem>();
    private RandomUserAdapter randomUserAdapter;
    private LinearLayoutManager linearLayoutManager;
    //public static String APP_ID = "1fdc9a791e21d5eb339dea5fa9a846b1";
    public static String APP_ID = "133018377b74f87203f13c15809a2b98";
    private ProgressDialog pDialog;
    RandomUserList context = this;
    private LinearLayout btnWeather;
    private Response responseModel = new Response();
    private EditText SearchFilter;

    private RandomUserDao randomUserDao;
    private InMobiDB inMobiDB;

    private FusedLocationProviderClient fusedLocationProviderClient;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        return super.onCreateView(inflater, container, savedInstanceState);

        inMobiDB = InMobiDB.getDatabase(requireContext());
        randomUserDao = inMobiDB.randomUserDao();

        return LayoutInflater.from(requireContext()).inflate(R.layout.fragment_random_user_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
//        super.onViewCreated(view, savedInstanceState);
        rv_randomuser = view.findViewById(R.id.rv_randomuser);
        btnWeather = view.findViewById(R.id.btnWeather);
        SearchFilter = view.findViewById(R.id.SearchFilter);
        randomUserAdapter = new RandomUserAdapter(requireContext(), resultsItemArrayList);
        linearLayoutManager = new LinearLayoutManager(requireContext());
        rv_randomuser.setLayoutManager(linearLayoutManager);
        rv_randomuser.setHasFixedSize(true);
        rv_randomuser.setAdapter(randomUserAdapter);
        rv_randomuser.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
//                super.onScrolled(recyclerView, dx, dy);
                if (dy > 0) {
                    int totalItemCount = linearLayoutManager.getItemCount();
                    int pastVisibleCount = linearLayoutManager.findLastVisibleItemPosition();
                    if (totalItemCount == pastVisibleCount + 1) {

                        callApiService(API_RANDOM_USER);
                    }
                }
            }
        });
        btnWeather.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showBottomSheetDialog(responseModel);
            }
        });
        SearchFilter.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void afterTextChanged(Editable editable) {
                String searchText = editable.toString();
                if (searchText != null && !searchText.equalsIgnoreCase("")) {
                    resultsItemArrayList = new ArrayList<>();
                    for (int i = 0; i < resultsItemArrayListAll.size(); i++) {
                        if (resultsItemArrayListAll.get(i).getName().getFirst().toUpperCase().contains(searchText.toUpperCase()) || resultsItemArrayListAll.get(i).getName().getLast().toUpperCase().contains(searchText.toUpperCase())) {
                            resultsItemArrayList.add(resultsItemArrayListAll.get(i));
                        }
                    }
                }else{
                    resultsItemArrayList = resultsItemArrayListAll;
                }
                randomUserAdapter.notifyDataSetChanged();
            }
        });
        if (new CommonUtil().isConnected(getContext())) {
            showLoader();
            callApiService(API_RANDOM_USER);
        } else {
            Toast.makeText(getContext(), "No Internet Connection", Toast.LENGTH_LONG).show();

        }

//        GPSTracker gpsTracker = new GPSTracker(requireContext());

        if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling

            return;
        }

        LocationRequest mLocationRequest = LocationRequest.create();
        mLocationRequest.setInterval(60000);
        mLocationRequest.setFastestInterval(5000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        LocationCallback mLocationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(@NonNull LocationResult locationResult) {
//                super.onLocationResult(locationResult);
                for(android.location.Location location: locationResult.getLocations()){
                    if(location!=null){
                        String weatherUrl = API_WEATHER_REPORT + "?lat=" + location.getLatitude() + "&lon=" + location.getLongitude() + "&appid=" + APP_ID;
//                        callWeatherReport(weatherUrl);
                    }
                }
            }

            @Override
            public void onLocationAvailability(@NonNull LocationAvailability locationAvailability) {
                super.onLocationAvailability(locationAvailability);
            }
        };

        LocationServices.getFusedLocationProviderClient(requireContext()).requestLocationUpdates(mLocationRequest,mLocationCallback,null);
        LocationServices.getFusedLocationProviderClient(requireContext()).getLastLocation().addOnSuccessListener(requireActivity(), new OnSuccessListener<android.location.Location>() {
            @Override
            public void onSuccess(android.location.Location location) {
                if (location != null) {
                    String weatherUrl = API_WEATHER_REPORT + "?lat=" + location.getLatitude() + "&lon=" + location.getLongitude() + "&appid=" + APP_ID;
                    callWeatherReport(weatherUrl);
                }
            }
        });
    }

    private void showBottomSheetDialog(Response responseModel) {
        BottomSheetWeather bottomSheetWeather =
                BottomSheetWeather.newInstance(responseModel);
        bottomSheetWeather.show(requireActivity().getSupportFragmentManager(),
                "Bottom Sheet Dialog for Weather");
    }

    private void callWeatherReport(String url) {
        RequestQueue queue = MySingleton.getInstance(requireContext().getApplicationContext()).
                getRequestQueue();

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new com.android.volley.Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            responseModel = new Response();
                            JSONObject jsonObject = new JSONObject(response);

                            JSONObject joCoord = jsonObject.optJSONObject("coord");
                            if (joCoord != null) {
                                Coord coord = new Coord();
                                coord.setLon(joCoord.optDouble("lon", 0.0));
                                coord.setLat(joCoord.optDouble("lat", 0.0));
                                responseModel.setCoord(coord);
                            }

                            JSONArray jaWeather = jsonObject.optJSONArray("weather");
                            if (jaWeather != null && jaWeather.length() > 0) {
                                WeatherItem weatherItem = new WeatherItem();
                                JSONObject joWeather = jaWeather.optJSONObject(0);
                                weatherItem.setMain(joWeather.optString("main", ""));
                                weatherItem.setDescription(joWeather.optString("description", ""));
                                responseModel.setWeather(weatherItem);
                            }

                            responseModel.setName(jsonObject.optString("name", ""));

                            JSONObject joWind = jsonObject.optJSONObject("wind");
                            if (joWind != null) {
                                Wind wind = new Wind();
                                wind.setSpeed(joWind.optDouble("speed", 0.0));
                                responseModel.setWind(wind);
                            }

                            JSONObject joMain = jsonObject.optJSONObject("main");
                            if (joMain != null) {
                                Main main = new Main();
                                main.setPressure(joMain.optInt("pressure", 0));
                                main.setHumidity(joMain.optInt("humidity", 0));
                                main.setTemp(joMain.optDouble("temp", 0.0));
                                responseModel.setMain(main);
                            }

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

    private void callApiService(String url) {
        RequestQueue queue = MySingleton.getInstance(requireContext().getApplicationContext()).
                getRequestQueue();

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new com.android.volley.Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            dismissLoader();
                            insertResponseModel(response);
                            /*JSONObject jsonObject = new JSONObject(response);
                            JSONArray jsonArray = jsonObject.optJSONArray("results");
                            if (jsonArray != null && jsonArray.length() > 0) {
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject jsonObject1 = jsonArray.optJSONObject(i);
                                    ResultsItem resultsItem = new ResultsItem();

                                    resultsItem.setGender(jsonObject1.optString("gender", ""));

                                    JSONObject joName = jsonObject1.optJSONObject("name");
                                    if (joName != null) {
                                        Name name = new Name();
                                        name.setTitle(joName.optString("title", ""));
                                        name.setFirst(joName.optString("first", ""));
                                        name.setLast(joName.optString("last", ""));
                                        resultsItem.setName(name);
                                    }

                                    JSONObject joLocation = jsonObject1.optJSONObject("location");
                                    if (joLocation != null) {
                                        Location location = new Location();
                                        JSONObject joStreet = joLocation.optJSONObject("street");
                                        if (joStreet != null) {
                                            Street street = new Street();
                                            street.setNumber(joStreet.optInt("number"));
                                            street.setName(joStreet.optString("name"));
                                            location.setStreet(street);
                                        }

                                        location.setCity(joLocation.optString("city", ""));
                                        location.setCity(joLocation.optString("state", ""));
                                        location.setCity(joLocation.optString("country", ""));
                                        location.setCity(joLocation.optString("postcode", ""));

                                        JSONObject joCoordinates = joLocation.optJSONObject("coordinates");
                                        if (joCoordinates != null) {
                                            Coordinates coordinates = new Coordinates();
                                            coordinates.setLatitude(joCoordinates.optString("latitude", ""));
                                            coordinates.setLongitude(joCoordinates.optString("longitude", ""));
                                            location.setCoordinates(coordinates);
                                        }

                                        JSONObject joTimezone = joLocation.optJSONObject("timezone");
                                        if (joTimezone != null) {
                                            Timezone timezone = new Timezone();
                                            timezone.setOffset(joStreet.optString("offset", ""));
                                            timezone.setDescription(joStreet.optString("longitude", ""));
                                            location.setTimezone(timezone);
                                        }

                                        resultsItem.setLocation(location);
                                    }

                                    resultsItem.setEmail(jsonObject1.optString("email", ""));

                                    JSONObject joLogin = jsonObject1.optJSONObject("login");
                                    if (joLogin != null) {
                                        Login login = new Login();
                                        login.setUuid(joLogin.optString("uuid", ""));
                                        login.setUsername(joLogin.optString("username", ""));
                                        login.setPassword(joLogin.optString("password", ""));
                                        login.setSalt(joLogin.optString("salt", ""));
                                        login.setMd5(joLogin.optString("md5", ""));
                                        login.setSha1(joLogin.optString("sha1", ""));
                                        login.setSha256(joLogin.optString("sha256", ""));
                                        resultsItem.setLogin(login);
                                    }

                                    JSONObject joDob = jsonObject1.optJSONObject("dob");
                                    if (joDob != null) {
                                        Dob dob = new Dob();
                                        dob.setAge(joDob.optInt("age"));
                                        dob.setDate(joDob.optString("date", ""));
                                        resultsItem.setDob(dob);
                                    }

                                    JSONObject joRegistered = jsonObject1.optJSONObject("registered");
                                    if (joRegistered != null) {
                                        Registered registered = new Registered();
                                        registered.setAge(joRegistered.optInt("age"));
                                        registered.setDate(joRegistered.optString("date", ""));
                                        resultsItem.setRegistered(registered);
                                    }

                                    resultsItem.setPhone(jsonObject1.optString("phone", ""));
                                    resultsItem.setCell(jsonObject1.optString("cell", ""));

                                    JSONObject joId = jsonObject1.optJSONObject("id");
                                    if (joId != null) {
                                        Id id = new Id();
                                        id.setName(joId.optString("name", ""));
                                        id.setValue(joId.optString("value", ""));
                                        resultsItem.setId(id);
                                    }

                                    JSONObject joPicture = jsonObject1.optJSONObject("picture");
                                    if (joPicture != null) {
                                        Picture picture = new Picture();
                                        picture.setLarge(joPicture.optString("large", ""));
                                        picture.setMedium(joPicture.optString("medium", ""));
                                        picture.setThumbnail(joPicture.optString("thumbnail", ""));
                                        resultsItem.setPicture(picture);
                                    }

                                    resultsItem.setNat(jsonObject1.optString("nat", ""));

                                    resultsItemArrayList.add(resultsItem);

                                }
                                resultsItemArrayListAll = resultsItemArrayList;
                                if (randomUserAdapter != null) {
                                    randomUserAdapter.notifyDataSetChanged();
                                }

                                RandomUser randomUser = new RandomUser(response);
                                randomUserDao.insert(randomUser);
                            }*/
                            System.out.println(response);
                        } catch (Exception e) {
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

    public void insertResponseModel(String response){
        RandomUser randomUser = new RandomUser(response);
//        randomUserDao.insert(randomUser);
        insert(randomUser);
    }

    public void retriveResponse(){
        List<RandomUser> randomUserResponseList = randomUserDao.getAllRandomUser();
        if(randomUserResponseList!=null && randomUserResponseList.size()>0){
            for(int i = 0; i < randomUserResponseList.size(); i++){
                String response = randomUserResponseList.get(i).getClobdata();
                loadResponseModel(response);
            }
            if (randomUserAdapter != null) {
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        randomUserAdapter.notifyDataSetChanged();
                    }
                });
            }
        }
    }

    public void loadResponseModel(String response){
        try {
            JSONObject jsonObject = new JSONObject(response);
            JSONArray jsonArray = jsonObject.optJSONArray("results");
            if (jsonArray != null && jsonArray.length() > 0) {
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject1 = jsonArray.optJSONObject(i);
                        ResultsItem resultsItem = new ResultsItem();

                        resultsItem.setGender(jsonObject1.optString("gender", ""));

                        JSONObject joName = jsonObject1.optJSONObject("name");
                        if (joName != null) {
                            Name name = new Name();
                            name.setTitle(joName.optString("title", ""));
                            name.setFirst(joName.optString("first", ""));
                            name.setLast(joName.optString("last", ""));
                            resultsItem.setName(name);
                        }

                        JSONObject joLocation = jsonObject1.optJSONObject("location");
                        if (joLocation != null) {
                            Location location = new Location();
                            JSONObject joStreet = joLocation.optJSONObject("street");
                            if (joStreet != null) {
                                Street street = new Street();
                                street.setNumber(joStreet.optInt("number"));
                                street.setName(joStreet.optString("name"));
                                location.setStreet(street);
                            }

                            location.setCity(joLocation.optString("city", ""));
                            location.setCity(joLocation.optString("state", ""));
                            location.setCity(joLocation.optString("country", ""));
                            location.setCity(joLocation.optString("postcode", ""));

                            JSONObject joCoordinates = joLocation.optJSONObject("coordinates");
                            if (joCoordinates != null) {
                                Coordinates coordinates = new Coordinates();
                                coordinates.setLatitude(joCoordinates.optString("latitude", ""));
                                coordinates.setLongitude(joCoordinates.optString("longitude", ""));
                                location.setCoordinates(coordinates);
                            }

                            JSONObject joTimezone = joLocation.optJSONObject("timezone");
                            if (joTimezone != null) {
                                Timezone timezone = new Timezone();
                                timezone.setOffset(joStreet.optString("offset", ""));
                                timezone.setDescription(joStreet.optString("longitude", ""));
                                location.setTimezone(timezone);
                            }

                            resultsItem.setLocation(location);
                        }

                        resultsItem.setEmail(jsonObject1.optString("email", ""));

                        JSONObject joLogin = jsonObject1.optJSONObject("login");
                        if (joLogin != null) {
                            Login login = new Login();
                            login.setUuid(joLogin.optString("uuid", ""));
                            login.setUsername(joLogin.optString("username", ""));
                            login.setPassword(joLogin.optString("password", ""));
                            login.setSalt(joLogin.optString("salt", ""));
                            login.setMd5(joLogin.optString("md5", ""));
                            login.setSha1(joLogin.optString("sha1", ""));
                            login.setSha256(joLogin.optString("sha256", ""));
                            resultsItem.setLogin(login);
                        }

                        JSONObject joDob = jsonObject1.optJSONObject("dob");
                        if (joDob != null) {
                            Dob dob = new Dob();
                            dob.setAge(joDob.optInt("age"));
                            dob.setDate(joDob.optString("date", ""));
                            resultsItem.setDob(dob);
                        }

                        JSONObject joRegistered = jsonObject1.optJSONObject("registered");
                        if (joRegistered != null) {
                            Registered registered = new Registered();
                            registered.setAge(joRegistered.optInt("age"));
                            registered.setDate(joRegistered.optString("date", ""));
                            resultsItem.setRegistered(registered);
                        }

                        resultsItem.setPhone(jsonObject1.optString("phone", ""));
                        resultsItem.setCell(jsonObject1.optString("cell", ""));

                        JSONObject joId = jsonObject1.optJSONObject("id");
                        if (joId != null) {
                            Id id = new Id();
                            id.setName(joId.optString("name", ""));
                            id.setValue(joId.optString("value", ""));
                            resultsItem.setId(id);
                        }

                        JSONObject joPicture = jsonObject1.optJSONObject("picture");
                        if (joPicture != null) {
                            Picture picture = new Picture();
                            picture.setLarge(joPicture.optString("large", ""));
                            picture.setMedium(joPicture.optString("medium", ""));
                            picture.setThumbnail(joPicture.optString("thumbnail", ""));
                            resultsItem.setPicture(picture);
                        }

                        resultsItem.setNat(jsonObject1.optString("nat", ""));

                        resultsItemArrayList.add(resultsItem);

                    }
                    resultsItemArrayListAll = resultsItemArrayList;
            }
//            System.out.println(response);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private class RandomUserAdapter extends RecyclerView.Adapter<RandomUserAdapter.RandomUserViewHolder> {

        private Context context;

        RandomUserAdapter(Context context, ArrayList<ResultsItem> arrayList) {
            this.context = context;
            resultsItemArrayList = arrayList;
            resultsItemArrayListAll = arrayList;
        }

        @NonNull
        @Override
        public RandomUserAdapter.RandomUserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(context).inflate(R.layout.random_user_list_item, parent, false);
            return new RandomUserAdapter.RandomUserViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull RandomUserAdapter.RandomUserViewHolder holder, int position) {
            ResultsItem resultsItem = resultsItemArrayList.get(position);
            holder.user_firstname.setText(resultsItem.getName().getFirst());
            holder.user_lastname.setText(resultsItem.getName().getLast());
            Glide.with(context).load(resultsItem.getPicture().getMedium()).into(holder.user_Profiles);
            holder.card_view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (resultsItemArrayList != null && resultsItemArrayList.size() > 0) {
                        ResultsItem resultsItem1 = resultsItemArrayList.get(holder.getAdapterPosition());

                        RandomUserDetails randomUserDetails = new RandomUserDetails();
                        if (resultsItem1 != null) {
                            Bundle args = new Bundle();
                            args.putSerializable("resultsItem", resultsItem1);
                            randomUserDetails.setArguments(args);
                        }
                        FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
                        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                        fragmentTransaction.replace(R.id.fragment_container, randomUserDetails, "Random User List").addToBackStack(null).commit();
                    }
                }
            });
        }

        @Override
        public int getItemCount() {
            return resultsItemArrayList.size();
        }

        private class RandomUserViewHolder extends RecyclerView.ViewHolder {

            private TextView user_firstname, user_lastname;
            private ImageView user_Profiles;
            private CardView card_view;

            public RandomUserViewHolder(@NonNull View itemView) {
                super(itemView);
                user_firstname = itemView.findViewById(R.id.user_firstname);
                user_lastname = itemView.findViewById(R.id.user_lastname);
                user_Profiles = itemView.findViewById(R.id.user_Profiles);
                card_view = itemView.findViewById(R.id.card_view);
            }
        }
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

    public void insert(RandomUser randomUser){
        new InsertAsyncTask(randomUserDao).execute(randomUser);
    }

    private class InsertAsyncTask extends AsyncTask<RandomUser,Void,Void> {

        RandomUserDao randomUserDao;

        public InsertAsyncTask(RandomUserDao randomUserDao){
            this.randomUserDao = randomUserDao;
        }

        @Override
        protected Void doInBackground(RandomUser... randomUsers) {
            randomUserDao.insert(randomUsers[0]);
            return null;
        }

        @Override
        protected void onPostExecute(Void unused) {
//            super.onPostExecute(unused);
//            randomUserDao.getAllRandomUser();
            new RetriveAsyncTask(randomUserDao).execute();
        }
    }

    private class RetriveAsyncTask extends AsyncTask<RandomUser,Void,Void> {

        RandomUserDao randomUserDao;

        public RetriveAsyncTask(RandomUserDao randomUserDao){
            this.randomUserDao = randomUserDao;
        }

        @Override
        protected Void doInBackground(RandomUser... randomUsers) {
//            randomUserDao.insert(randomUsers[0]);
            retriveResponse();
            return null;
        }

        @Override
        protected void onPostExecute(Void unused) {
//            super.onPostExecute(unused);
        }
    }
}
