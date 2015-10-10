package com.dmitrykolesnikovich.weather;

import android.content.Context;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

public final class VolleySender {

  private static VolleySender instance;
  private RequestQueue queue;

  private VolleySender(Context context) {
    this.queue = Volley.newRequestQueue(context);
  }

  public static VolleySender getInstance(Context context) {
    if (instance == null) {
      instance = new VolleySender(context);
    }
    return instance;
  }

  public void add(Context context, Request request) {
    if (this.queue == null) {
      this.queue = Volley.newRequestQueue(context);
    }
    request.setRetryPolicy(new DefaultRetryPolicy(9000, 0, 1));
    this.queue.add(request);
  }

}