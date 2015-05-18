package com.miaotu.util;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;

import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.conn.scheme.LayeredSocketFactory;
import org.apache.http.conn.scheme.SocketFactory;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

/**
 * @author ying
 *
 */
public class EasySSLSocketFactory implements SocketFactory,
LayeredSocketFactory {

private SSLContext sslcontext = null;

private SSLContext createEasySSLContext() throws IOException {
try {
SSLContext context = SSLContext.getInstance("TLS");
context.init(null,
	new TrustManager[] { new EasyX509TrustManager(null) },
	null);
return context;
} catch (Exception e) {
throw new IOException(e.getMessage());
}
}

private SSLContext getSSLContext() throws IOException {
if (this.sslcontext == null) {
this.sslcontext = createEasySSLContext();
}
return this.sslcontext;
}

@Override
public Socket connectSocket(Socket sock, String host, int port,
InetAddress localAddress, int localPort, HttpParams params)
throws IOException, UnknownHostException,
ConnectTimeoutException {
int connTimeout = HttpConnectionParams.getConnectionTimeout(params);
int soTimeout = HttpConnectionParams.getSoTimeout(params);

InetSocketAddress remoteAddress = new InetSocketAddress(host, port);
SSLSocket sslsock = (SSLSocket) ((sock != null) ? sock
    : createSocket());

if ((localAddress != null) || (localPort > 0)) {
// we need to bind explicitly
if (localPort < 0) {
    localPort = 0; // indicates "any"
}
InetSocketAddress isa = new InetSocketAddress(localAddress,
	localPort);
sslsock.bind(isa);
}

sslsock.connect(remoteAddress, connTimeout);
sslsock.setSoTimeout(soTimeout);
return sslsock;

}

@Override
public Socket createSocket() throws IOException {
return getSSLContext().getSocketFactory().createSocket();
}

@Override
public boolean isSecure(Socket socket) throws IllegalArgumentException {
return true;
}

@Override
public Socket createSocket(Socket socket, String host, int port,
boolean autoClose) throws IOException, UnknownHostException {
return getSSLContext().getSocketFactory().createSocket(socket,
    host, port, autoClose);
}

@Override
public boolean equals(Object obj) {
return ((obj != null) && obj.getClass().equals(
    EasySSLSocketFactory.class));
}

@Override
public int hashCode() {
return EasySSLSocketFactory.class.hashCode();
}
public class EasyX509TrustManager implements X509TrustManager {

	private X509TrustManager standardTrustManager = null;

	public EasyX509TrustManager(KeyStore keystore)
		throws NoSuchAlgorithmException, KeyStoreException {
	    super();
	    TrustManagerFactory factory = TrustManagerFactory
		    .getInstance(TrustManagerFactory.getDefaultAlgorithm());
	    factory.init(keystore);
	    TrustManager[] trustmanagers = factory.getTrustManagers();
	    if (trustmanagers.length == 0) {
		throw new NoSuchAlgorithmException("no trust manager found");
	    }
	    this.standardTrustManager = (X509TrustManager) trustmanagers[0];
	}

	@Override
	public void checkClientTrusted(X509Certificate[] certificates,
		String authType) throws CertificateException {
	    standardTrustManager.checkClientTrusted(certificates, authType);
	}

	@Override
	public void checkServerTrusted(X509Certificate[] certificates,
		String authType) throws CertificateException {
	    if ((certificates != null) && (certificates.length == 1)) {
		certificates[0].checkValidity();
	    } else {
		// standardTrustManager.checkServerTrusted(certificates,
		// authType);
	    }
	}

	@Override
	public X509Certificate[] getAcceptedIssuers() {
	    return this.standardTrustManager.getAcceptedIssuers();
	}
    }
}
