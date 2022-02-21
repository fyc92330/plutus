package org.chun.plutus;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.chun.plutus.util.JsonBean;
import org.junit.jupiter.api.Test;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;

class PlutusApplicationTests {

  @Test
  void contextLoads() throws IOException {

    HttpUrl httpUrl = new HttpUrl.Builder()
        .scheme("https")
        .host("develop.land.moi.gov.tw")
        .addPathSegment("DcDl_dlZeApr")
        .addQueryParameter("pjId", "WEH2244710113")
        .addQueryParameter("autoId", "18")
        .build();

    OkHttpClient client = new OkHttpClient().newBuilder().build();
    Request request = new Request.Builder()
        .url(httpUrl)
        .get().build();
    System.out.println(0);
    Response response = client.newCall(request).execute();

    try {
      System.out.println("------------>" + response.body().string());
    } catch (Exception e) {
      System.out.println(123);
      System.out.println("------------>" + response.body().string());
    } finally {
      response.close();
    }
  }

}
