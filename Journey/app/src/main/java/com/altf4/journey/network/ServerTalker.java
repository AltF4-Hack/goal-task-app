package com.altf4.journey.network;

import android.content.Context;

import com.altf4.journey.entity.User;
import com.altf4.journey.entity.Validatable;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.*;

import java.util.HashMap;
import java.util.Map;

public class ServerTalker {
    public static final String SERVER_TARGET = "https://journeyserver.onrender.com";
    private static OkHttpClient client = new OkHttpClient();

    private ServerTalker() {}

    public static JSONObject sendPrompt(String prompt) {
        // send a question to the AI
        try {
            Request request = new Request.Builder()
                    .url(SERVER_TARGET + "/api/prompt/" + prompt)
                    .build();

            Call call = client.newCall(request);
            Response response = call.execute();

            if (response.isSuccessful()) {
                JSONObject json = new JSONObject();
                json.put("json", response.body().string());
            }
        } catch (IOException e) {
            System.out.println("Error: " + e.getMessage());
        } catch (JSONException e) {
            System.out.println("Error: " + e.getMessage());
        }
        return null;
    }
//
//    public static void validateData(String data, Validatable dataField) {
//        StringRequest stringRequest = new StringRequest(Request.Method.GET, SERVER_TARGET,
//                new Response.Listener<String>() {
//                    @Override
//                    public void onResponse(String response) {
//                        if (response == null) {
//                            dataField.displayInvalid();
//                        }
//                    }
//                }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                dataField.displayServerError();
//            }
//        });
//
//        requestQueue.add(stringRequest);
//    }

    public static void saveNewUser(User user, ResponseContainer container) {

        JSONObject json = new JSONObject(user.getDatabaseRepresentation());

        RequestBody body = RequestBody.create(
                json.toString(),
                MediaType.parse("application/json; charset=utf-8")
        );

        Request request = new Request.Builder().url(SERVER_TARGET + "/api/addUser").post(body).build();

        client.newCall(request).enqueue(
                new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        container.setResponse("Server error");
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        if (response.isSuccessful()) {
                            container.setResponse("success");
                        }
                    }
                }
        );
    }

    public static void loginUser(String username, String password, ResponseContainer container) {

    }

}
