package tech.iooo.coco.commons;

import io.netty.util.internal.SystemPropertyUtil;
import java.io.FileInputStream;
import java.security.KeyStore;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManagerFactory;

/**
 * Created on 2018/7/3 下午3:16
 *
 * @author <a href="mailto:yangkizhang@gmail.com?subject=iooo-netty-booster">Ivan97</a>
 */
public class SecureSslServerContextFactory {

    private static final String PROTOCOL = "SSL";
    private static final SSLContext SERVER_CONTEXT;
    private static String SERVER_KEY_STORE = "ssl/sslserverkeys";
    private static String SERVER_TRUST_KEY_STORE = "ssl/sslservertrust";
    private static String SERVER_KEY_STORE_PASSWORD = "ioootech";
    private static String SERVER_TRUST_KEY_STORE_PASSWORD = "ioootech";

    static {
        String algorithm = SystemPropertyUtil.get("ssl.KeyManagerFactory.algorithm");
        if (algorithm == null) {
            algorithm = "SunX509";
        }
        SSLContext serverContext;
        try {
            KeyStore ks = KeyStore.getInstance("JKS");
            ks.load(new FileInputStream(SERVER_KEY_STORE), SERVER_KEY_STORE_PASSWORD.toCharArray());
            KeyStore tks = KeyStore.getInstance("JKS");
            tks.load(new FileInputStream(SERVER_TRUST_KEY_STORE), SERVER_TRUST_KEY_STORE_PASSWORD.toCharArray());
            KeyManagerFactory kmf = KeyManagerFactory.getInstance(algorithm);
            TrustManagerFactory tmf = TrustManagerFactory.getInstance("SunX509");
            kmf.init(ks, SERVER_KEY_STORE_PASSWORD.toCharArray());
            tmf.init(tks);
            serverContext = SSLContext.getInstance(PROTOCOL);
            serverContext.init(kmf.getKeyManagers(), tmf.getTrustManagers(), null);
        } catch (Exception e) {
            throw new Error(e);
        }
        SERVER_CONTEXT = serverContext;
    }

    public static SSLContext getServerContext() {
        return SERVER_CONTEXT;
    }
}
