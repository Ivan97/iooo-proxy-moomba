package tech.iooo.coco.test;

import java.net.InetSocketAddress;
import java.net.PasswordAuthentication;
import java.net.Proxy;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class HttpProxyClient {

  @Test
  public void test() throws Exception {
    final String user = "ivan97";
    final String password = "test";

    Proxy proxyTest = new Proxy(Proxy.Type.SOCKS, new InetSocketAddress("127.0.0.1", 1087));

    java.net.Authenticator.setDefault(new java.net.Authenticator() {
      private PasswordAuthentication authentication = new PasswordAuthentication(user, password.toCharArray());

      @Override
      protected PasswordAuthentication getPasswordAuthentication() {
        return authentication;
      }
    });

    OkHttpClient client = new OkHttpClient.Builder().proxy(proxyTest).build();
    Request request = new Request.Builder().url("https://www.baidu.com").build();
    Response response = client.newCall(request).execute();
    System.out.println(response.code());
    System.out.println(response.body().string());

    client.dispatcher().executorService().shutdown();
    client.connectionPool().evictAll();
  }

}
