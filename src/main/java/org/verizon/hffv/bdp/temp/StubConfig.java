package org.verizon.hffv.bdp.temp;


public class StubConfig {
    private String name;
    private String method;
    private String url;
    private int statusCode;

    public StubConfig(String name, String method, String url, int statusCode) {
        this.name = name;
        this.method = method;
        this.url = url;
        this.statusCode = statusCode;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }
}
