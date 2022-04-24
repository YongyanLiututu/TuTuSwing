package com.one.net;

import org.apache.http.HttpEntity;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.IOException;

public class ImageRequestUtils {

    //有参方式
    public static Image getImage(String url) {
        CloseableHttpClient httpClient = HttpClientBuilder.create().setDefaultCookieStore(CookiesHolder.getCookieStore()).build();
        CloseableHttpResponse response = null;
        Image image = null;
        try {

            HttpGet httpGet = new HttpGet(url);
            RequestConfig requestConfig = RequestConfig.custom()
                    .setSocketTimeout(3000)
                    .setConnectTimeout(3000)
                    .build();
            httpGet.setConfig(requestConfig);

            response = httpClient.execute(httpGet);

            HttpEntity responseEntity = response.getEntity();
            if (responseEntity != null) {
                image = ImageIO.read(response.getEntity().getContent());
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {

                if (httpClient != null) {
                    httpClient.close();
                }
                if (response != null) {
                    response.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return image;
    }


}
