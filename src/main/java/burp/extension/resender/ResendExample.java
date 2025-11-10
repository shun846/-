package burp.extension.resender;

import burp.api.montoya.BurpExtension;
import burp.api.montoya.MontoyaApi;
import burp.api.montoya.http.handler.HttpHandler;
import burp.api.montoya.http.handler.HttpRequestToBeSent;
import burp.api.montoya.http.message.requests.HttpRequest;
import burp.api.montoya.http.message.responses.HttpResponse;
import burp.api.montoya.logging.Logging;

public class ResendExample implements BurpExtension, HttpHandler {

    private Logging logging;
    private MontoyaApi api;

    @Override
    public void initialize(MontoyaApi api) {
        this.api = api;
        this.logging = api.logging();
        api.extension().setName("ResendExample");

        // HttpHandler 登録
        api.http().registerHttpHandler(this);

        logging.logToOutput("ResendExample loaded!");
    }

    @Override
    public void handleHttpRequestToBeSent(HttpRequestToBeSent requestToBeSent) {
        HttpRequest originalRequest = requestToBeSent.request();

        // 再送信処理
        logging.logToOutput("Sending again: " + originalRequest.url());
        HttpResponse response = api.http().sendRequest(originalRequest).response();

        // Burp の History に追加
        api.siteMap().addRequestResponse(originalRequest, response);

        // 元のリクエストをそのまま返す（変更しない）
        requestToBeSent.continueWith(originalRequest);
    }
}
