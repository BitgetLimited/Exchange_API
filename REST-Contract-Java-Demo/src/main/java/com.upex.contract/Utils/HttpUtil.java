
package com.upex.contract.Utils;


import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.CoreConnectionPNames;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ssl.*;
import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.net.URI;
import java.security.GeneralSecurityException;
import java.security.KeyStore;
import java.security.SecureRandom;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.*;
import java.util.Map.Entry;
import java.util.concurrent.TimeUnit;

/**
 * Http Utility for process remote http invocation.
 *
 * @author pierre
 * @version $ v1.0 Sep 5, 2014 $
 */
@Slf4j
public class HttpUtil {
    private static Logger logger = LoggerFactory.getLogger(HttpUtil.class);
    private static OkHttpClient mOkHttpClient = null;
    public static final Long TIME_OUT_SECONDS = 30L;
    public static final String MEDIA_TYPE_JSON = "application/json; charset=utf-8";
    public static final String CHARSET_UTF8 = "UTF-8";

    public HttpUtil() {
    }
    public static void init(Long timeOutSeconds, Long readTimeOut, Long writeTimeOut) {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        if (null != timeOutSeconds && 0L <= timeOutSeconds) {
            builder.connectTimeout(timeOutSeconds, TimeUnit.SECONDS);
        } else {
            builder.connectTimeout(TIME_OUT_SECONDS, TimeUnit.SECONDS);
        }

        if (null != readTimeOut && 0L <= readTimeOut) {
            builder.readTimeout(readTimeOut, TimeUnit.SECONDS);
        }

        if (null != writeTimeOut && 0L <= writeTimeOut) {
            builder.writeTimeout(writeTimeOut, TimeUnit.SECONDS);
        }

        mOkHttpClient = builder.build();
    }

    private static OkHttpClient getHttpClient(Request request) {
        if (null == mOkHttpClient) {
            init(TIME_OUT_SECONDS, (Long)null, (Long)null);
        }

        OkHttpClient.Builder builder = mOkHttpClient.newBuilder();
        if (!request.isHttps()) {
            return builder.build();
        } else {
            SSLBuilder sslBuilder = SSLBuilder.builder();
            return builder.sslSocketFactory(sslBuilder.getSSLSocketFactory(), sslBuilder.getX509TrustManager()).hostnameVerifier(sslBuilder.getNotVerifyHostnameVerifier()).build();
        }
    }

    private static Request getRequest(String url, Headers headers, Map<String, String> paramsMap, String body) {
        Request.Builder builder = (new Request.Builder()).url(attachParam(url, paramsMap));
        if (headers != null) {
            builder.headers(headers);
        }

        if (StringUtils.isNotBlank(body)) {
            builder.post(RequestBody.create(MediaType.parse("application/json; charset=utf-8"), body));
        }

        return builder.build();
    }

    private static Request getFormRequest(String url, Headers headers, Map<String, String> paramsMap, Map<String, String> body) {
        Request.Builder builder = (new Request.Builder()).url(attachParam(url, paramsMap));
        if (headers != null) {
            builder.headers(headers);
        }

        if (null != body && !body.isEmpty()) {
            FormBody.Builder formBuilder = new FormBody.Builder();
            Set<Entry<String, String>> entrySet = body.entrySet();
            Iterator var7 = entrySet.iterator();

            while(var7.hasNext()) {
                Entry<String, String> entry = (Entry)var7.next();
                formBuilder.add((String)entry.getKey(), (String)entry.getValue());
            }

            builder.post(formBuilder.build());
        }

        return builder.build();
    }

    private static String attachParam(String url, Map<String, String> paramsMap) {
        if (MapUtils.isEmpty(paramsMap)) {
            return url;
        } else {
            List<BasicNameValuePair> params = new ArrayList();
            Iterator var3 = paramsMap.keySet().iterator();

            while(var3.hasNext()) {
                String key = (String)var3.next();
                params.add(new BasicNameValuePair(key, (String)paramsMap.get(key)));
            }

            return url.endsWith("?") ? url + URLEncodedUtils.format(params, "UTF-8") : url + "?" + URLEncodedUtils.format(params, "UTF-8");
        }
    }

    public static okhttp3.Response execute(Request request) {
        try {
            return getHttpClient(request).newCall(request).execute();
        } catch (IOException var2) {
            throw new RuntimeException(var2);
        }
    }

    public static void enqueue(Request request, Callback callback) {
        getHttpClient(request).newCall(request).enqueue(callback);
    }

    public static void asyncGet(String url, Callback callback) {
        asyncGet(url, (Headers)null, callback);
    }

    public static void asyncGet(String url, Headers headers, Callback callback) {
        asyncGet(url, headers, (Map)null, callback);
    }

    public static void asyncGet(String url, Headers headers, Map<String, String> paramsMap, Callback callback) {
        enqueue(getRequest(url, headers, paramsMap, (String)null), callback);
    }

    public static void asyncPost(String url, Callback callback) {
        asyncPost(url, (Headers)null, callback);
    }

    public static void asyncPost(String url, Headers headers, Callback callback) {
        asyncPost(url, headers, (String)null, callback);
    }

    public static void asyncPost(String url, Headers headers, String body, Callback callback) {
        enqueue(getRequest(url, headers, (Map)null, body), callback);
    }

    public static okhttp3.Response syncGet(String url) {
        return syncGet(url, (Headers)null);
    }

    public static okhttp3.Response syncGet(String url, Headers headers) {
        return syncGet(url, headers, (Map)null);
    }

    public static okhttp3.Response syncGet(String url, Headers headers, Map<String, String> paramsMap) {
        return execute(getRequest(url, headers, paramsMap, (String)null));
    }

    public static String syncGetString(String url) {
        return syncGetString(url, (Headers)null);
    }

    public static String syncGetString(String url, Headers headers) {
        return syncGetString(url, headers, (Map)null);
    }

    public static String syncGetString(String url, Headers headers, Map<String, String> paramsMap) {
        try {
            okhttp3.Response response = syncGet(url, headers, paramsMap);
            Throwable var4 = null;

            String var5;
            try {
                verifySuccess(response.code());
                var5 = response.body().string();
            } catch (Throwable var15) {
                var4 = var15;
                throw var15;
            } finally {
                if (response != null) {
                    if (var4 != null) {
                        try {
                            response.close();
                        } catch (Throwable var14) {
                            var4.addSuppressed(var14);
                        }
                    } else {
                        response.close();
                    }
                }

            }

            return var5;
        } catch (IOException var17) {
            throw new RuntimeException(var17);
        }
    }

    public static okhttp3.Response syncPost(String url) {
        return syncPost(url, (Headers)null);
    }

    public static okhttp3.Response syncPost(String url, Headers headers) {
        return syncPost(url, headers, (String)null);
    }

    public static okhttp3.Response syncPost(String url, Headers headers, String body) {
        return execute(getRequest(url, headers, (Map)null, body));
    }

    public static okhttp3.Response syncPostForm(String url, Headers headers, Map<String, String> body) {
        return execute(getFormRequest(url, headers, (Map)null, body));
    }

    public static String syncPostString(String url) {
        return syncPostString(url, (Headers)null);
    }

    public static String syncPostString(String url, Headers headers) {
        return syncPostString(url, (Headers)null, (String)null);
    }

    public static String syncPostString(String url, Headers headers, String body) {
        try {
            okhttp3.Response response = syncPost(url, headers, body);
            Throwable var4 = null;

            String var5;
            try {
                verifySuccess(response.code());
                var5 = response.body().string();
            } catch (Throwable var15) {
                var4 = var15;
                throw var15;
            } finally {
                if (response != null) {
                    if (var4 != null) {
                        try {
                            response.close();
                        } catch (Throwable var14) {
                            var4.addSuppressed(var14);
                        }
                    } else {
                        response.close();
                    }
                }

            }

            return var5;
        } catch (IOException var17) {
            throw new RuntimeException(var17);
        }
    }

    public static String syncFormPostString(String url, Headers headers, Map<String, String> body) {
        try {
            okhttp3.Response response = syncPostForm(url, headers, body);
            Throwable var4 = null;

            String var5;
            try {
                verifySuccess(response.code());
                var5 = response.body().string();
            } catch (Throwable var15) {
                var4 = var15;
                throw var15;
            } finally {
                if (response != null) {
                    if (var4 != null) {
                        try {
                            response.close();
                        } catch (Throwable var14) {
                            var4.addSuppressed(var14);
                        }
                    } else {
                        response.close();
                    }
                }

            }

            return var5;
        } catch (IOException var17) {
            throw new RuntimeException(var17);
        }
    }

    private static void verifySuccess(int code) {
        if (code < 200 && code > 302) {
            logger.error("Unexpected code " + code);
            try {
                throw new Exception("");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private static class SSLBuilder {
        private String certFileLocation;

        private SSLBuilder() {
        }

        public static SSLBuilder builder() {
            return new SSLBuilder();
        }

        public static SSLBuilder builder(String certFileLocation) {
            SSLBuilder builder = new SSLBuilder();
            builder.certFileLocation = certFileLocation;
            return builder;
        }

        public HostnameVerifier getNotVerifyHostnameVerifier() {
            return new HostnameVerifier() {
                public boolean verify(String hostname, SSLSession session) {
                    return true;
                }
            };
        }

        public X509TrustManager getX509TrustManager() {
            try {
                return StringUtils.isNotBlank(this.certFileLocation) ? this.trustManagerForCertificates(this.getClass().getResourceAsStream(this.certFileLocation)) : this.trustManagerForCertificates();
            } catch (Exception var2) {
                throw new RuntimeException(var2);
            }
        }

        public  javax.net.ssl.SSLSocketFactory getSSLSocketFactory() {
            try {
                SSLContext sslContext = SSLContext.getInstance("TLS");
                sslContext.init((KeyManager[])null, new TrustManager[]{this.getX509TrustManager()}, new SecureRandom());
                return sslContext.getSocketFactory();
            } catch (Exception var2) {
                throw new RuntimeException(var2);
            }
        }

        private X509TrustManager trustManagerForCertificates() {
            return new X509TrustManager() {
                public void checkClientTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {
                }

                public void checkServerTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {
                }

                public X509Certificate[] getAcceptedIssuers() {
                    return new X509Certificate[0];
                }
            };
        }

        private X509TrustManager trustManagerForCertificates(InputStream in) throws GeneralSecurityException {
            CertificateFactory certificateFactory = CertificateFactory.getInstance("X.509");
            Collection<? extends Certificate> certificates = certificateFactory.generateCertificates(in);
            if (certificates.isEmpty()) {
                throw new IllegalArgumentException("expected non-empty set of trusted certificates");
            } else {
                char[] password = "password".toCharArray();
                KeyStore keyStore = this.newEmptyKeyStore(password);
                int index = 0;
                Iterator var7 = certificates.iterator();

                while(var7.hasNext()) {
                    Certificate certificate = (Certificate)var7.next();
                    String certificateAlias = Integer.toString(index++);
                    keyStore.setCertificateEntry(certificateAlias, certificate);
                }

                KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
                keyManagerFactory.init(keyStore, password);
                TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
                trustManagerFactory.init(keyStore);
                TrustManager[] trustManagers = trustManagerFactory.getTrustManagers();
                if (trustManagers.length == 1 && trustManagers[0] instanceof X509TrustManager) {
                    return (X509TrustManager)trustManagers[0];
                } else {
                    throw new IllegalStateException("Unexpected default trust managers:" + Arrays.toString(trustManagers));
                }
            }
        }

        private KeyStore newEmptyKeyStore(char[] password) throws GeneralSecurityException {
            try {
                KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
                InputStream in = null;
                keyStore.load((InputStream)in, password);
                return keyStore;
            } catch (IOException var4) {
                throw new AssertionError(var4);
            }
        }
    }
    // TODO 2019.01.08
    public static class Response {
        public static final int STATUS_LINE_SUC = 200;

        public int code;
        public String content;

        public boolean success() {
            return STATUS_LINE_SUC == code;
        }

        public String toString() {
            return "{\"code\":\"" + code + "\", \"content\":\"" + content + "\"}  ";
        }
    }

    public static Response sendPostRequest(String url, String charset, Map<String, String> params) throws Exception {
        // 1. Create uri
        URI uri = URI.create(url);
        // 2. Create HttpUriRequest
        HttpPost request = new HttpPost(uri);
        // 3. Create HttpClient
        HttpClient httpClient = getHttpClientByProtocal(uri);

        // Assemble parameters
        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
        for (Iterator<Entry<String, String>> it = params.entrySet().iterator(); it.hasNext(); ) {
            Entry<String, String> entry = it.next();
            nameValuePairs.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
        }

        request.setEntity(new UrlEncodedFormEntity(nameValuePairs, charset));
        // 4. Parse response
        return retrieveResponse(httpClient, request);
    }

    public static Response sendGetRequest(String url, Map<String, String> params) throws Exception {
        // 1. Create URI
        URI uri = URI.create(url);
        // 2. Create request  mode
        HttpGet request = new HttpGet(uri);
        // 3. Create HttpClient
        HttpClient httpClient = getHttpClientByProtocal(uri);

        // Assemble parameters
        BasicHttpParams basicHttpParams = new BasicHttpParams();
        for (Entry<String, String> entry : params.entrySet()) {
            basicHttpParams.setParameter(entry.getKey(), entry.getValue());
        }
        request.setParams(basicHttpParams);

        // 4. Parse response
        return retrieveResponse(httpClient, request);
    }

    private static Response retrieveResponse(HttpClient httpClient, HttpUriRequest request) throws Exception {
        HttpResponse httpResponse = httpClient.execute(request);
        Response response = new Response();
        response.code = httpResponse.getStatusLine().getStatusCode();

        BufferedReader reader = null;
        StringBuilder rc = new StringBuilder();
        String charset = "utf-8";
        try {
        	Header charsetHeader = httpResponse.getEntity().getContentType();
        	if (charsetHeader != null) {
        		String contentType = charsetHeader.getValue();
        		String charsetReal = getCharset(contentType);
        		if (charsetReal != null) {
        			charset = charsetReal;
        		}
        	}
            InputStream is = httpResponse.getEntity().getContent();
            reader = new BufferedReader(new InputStreamReader(is, charset));
            String line = null;
            while ((line = reader.readLine()) != null) {
                rc.append(line);
            }
        } finally {
            if (null != reader) {
                reader.close();
            }
        }

        response.content = rc.toString();
        return response;
    }

    private static String getCharset(String contentType) {
		String charset = null;
		final String CharsetIden = "charset=";
		if (StringUtils.isNotEmpty(contentType) || contentType.toLowerCase().indexOf(CharsetIden) < 0) {
			return charset;
		}

		int startIndex = contentType.toLowerCase().indexOf(CharsetIden);
		int endIndex = contentType.toLowerCase().indexOf(";", startIndex);
		if (endIndex == -1) {
			endIndex = contentType.length();
		}
		charset = contentType.substring(startIndex + CharsetIden.length(), endIndex);
		return charset;
	}

	private static HttpClient getHttpClientByProtocal(URI uri) throws Exception {
        HttpClient httpClient = null;
        // determine the scheme
        String scheme = uri.getScheme();
        if (StringUtils.isEmpty(scheme) || "http".equals(scheme.toLowerCase())) {
            // plain http context
            httpClient = getHttpClient(false);
        }
        else {
            // need ssl context
            httpClient = getHttpClient(true);
        }
        return httpClient;
    }

    private static HttpClient getHttpClient(boolean enableSSL) throws Exception {
        DefaultHttpClient httpClient = new DefaultHttpClient();
        httpClient.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 45000);
        httpClient.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, 45000);

        if (enableSSL) {
            SSLContext sslContext = null;
            try {
                sslContext = SSLContext.getInstance("SSL");
                sslContext.init(null,
                        new TrustManager[] {
                        new X509TrustManager() {
                            public X509Certificate[] getAcceptedIssuers() {
                                return null;
                            }

                            public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
                            }

                            public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
                            }
                        }
                }, new SecureRandom());
            } catch (Exception e) {
                throw e;
            }

            SSLSocketFactory sslSocketFactory = new SSLSocketFactory(sslContext,
                                                                     SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
            ClientConnectionManager clientConnectionManager = httpClient.getConnectionManager();
            SchemeRegistry schemeRegistry = clientConnectionManager.getSchemeRegistry();
            schemeRegistry.register(new Scheme("https", 443, sslSocketFactory));
        }
        return httpClient;
    }



    /**
     * Retrieve the real ip address.
     *
     * @param request {@link HttpServletRequest}
     * @return ip地址: 10.20.140.132
     */
    public static String getRemoteIp(HttpServletRequest request) {
        String ip = request.getHeader("x-real-ip");
        if (ip == null) {
            ip = request.getRemoteAddr();
        }

        log.debug("x-real-ip = {}", new Object[] { ip });

        // filter proxy ip
        String[] stemps = ip.split(",");
        if (stemps != null && stemps.length >= 1) {
            // firth ip is the real client ip
            ip = stemps[0];
        }
        log.debug("Filter proxy ip = {}", new Object[] { ip });

        ip = ip.trim();
        if (ip.length() > 23) {
            ip = ip.substring(0, 23);
        }

        return ip;
    }
}
