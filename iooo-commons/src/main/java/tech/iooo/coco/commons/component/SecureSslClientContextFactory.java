package tech.iooo.coco.commons.component;

import io.netty.util.internal.SystemPropertyUtil;
import java.io.FileInputStream;
import java.security.KeyStore;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManagerFactory;

/**
 * Created on 2018/7/3 下午3:26
 *
 * @author <a href="mailto:yangkizhang@gmail.com?subject=iooo-netty-booster">Ivan97</a>
 */
public class SecureSslClientContextFactory {

    private static final String PROTOCOL = "SSL";
    private static final SSLContext CLIENT_CONTEXT;
    private static String CLIENT_KEY_STORE = "ssl/sslclientkeys";
    private static String CLIENT_TRUST_KEY_STORE = "ssl/sslclienttrust";
    private static String CLIENT_KEY_STORE_PASSWORD = "ioootech";
    private static String CLIENT_TRUST_KEY_STORE_PASSWORD = "ioootech";

    static {
        String algorithm = SystemPropertyUtil.get("ssl.KeyManagerFactory.algorithm");
        if (algorithm == null) {
            algorithm = "SunX509";
        }
        SSLContext clientContext;
        try {
            KeyStore ks2 = KeyStore.getInstance("JKS");
            ks2.load(new FileInputStream(CLIENT_KEY_STORE), CLIENT_KEY_STORE_PASSWORD.toCharArray());
            KeyStore tks2 = KeyStore.getInstance("JKS");
            tks2.load(new FileInputStream(CLIENT_TRUST_KEY_STORE), CLIENT_TRUST_KEY_STORE_PASSWORD.toCharArray());
            KeyManagerFactory kmf2 = KeyManagerFactory.getInstance(algorithm);
            TrustManagerFactory tmf2 = TrustManagerFactory.getInstance("SunX509");
            kmf2.init(ks2, CLIENT_KEY_STORE_PASSWORD.toCharArray());
            tmf2.init(tks2);
            clientContext = SSLContext.getInstance(PROTOCOL);
            clientContext.init(kmf2.getKeyManagers(), tmf2.getTrustManagers(), null);
        } catch (Exception e) {
            throw new Error(e);
        }
        CLIENT_CONTEXT = clientContext;
    }

    public static SSLContext getClientContext() {
        return CLIENT_CONTEXT;
    }
}
