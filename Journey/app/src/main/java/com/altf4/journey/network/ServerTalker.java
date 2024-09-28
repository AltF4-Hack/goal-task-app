package com.altf4.journey.network;

import android.content.Context;

import com.altf4.journey.entity.Validatable;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

public class ServerTalker {
    public static final String SERVER_TARGET = "";
    private static RequestQueue requestQueue;

    private ServerTalker() {}

    public static RequestQueue initRequestQueue(Context ctx) {
        if (requestQueue == null) {
            requestQueue = Volley.newRequestQueue(ctx.getApplicationContext());
        }
        return requestQueue;
    }

    public static void sendPrompt(String prompt, ResponseContainer container) {
        // send a question to the AI
        Map<String, String> params = new HashMap<>();
        params.put("prompt", prompt);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, SERVER_TARGET,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        container.setResponse(response);
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        container.setResponse("Error");
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                return params;
            }
        };

        requestQueue.add(stringRequest);
    }

    public static void validateData(String data, Validatable dataField) {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, SERVER_TARGET,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (response == null) {
                            dataField.displayInvalid();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                dataField.displayServerError();
            }
        });
    }

}
