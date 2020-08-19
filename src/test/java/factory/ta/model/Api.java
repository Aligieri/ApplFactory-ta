package factory.ta.model;

import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import static factory.ta.conf.Constants.*;

public class Api {

    public static HttpUriRequest getNewSerialIdForFactory(String factoryID){
        return new HttpGet(API_URI + "lambda?factoryId=" + factoryID);
    }
    public static HttpUriRequest getGapIdForFactory(String factoryID){
        return new HttpGet(API_URI + "lambda?factoryId=" + factoryID + "&toBeGap=true");
    }

    public static HttpUriRequest getFailureIdForFactory(String factoryID){
        return new HttpGet(API_URI + "lambda?factoryId=" + factoryID + "&toBeFailed=true");
    }

    public static HttpUriRequest getAppIdAndStatus(String token){
        return new HttpGet(API_URI + "service?requestToken=" + token.replace("\"", ""));
    }

    public static HttpUriRequest processRequests(){
        return new HttpGet(API_URI + "process");
    }

    public static HttpUriRequest clearDatabase(){
        return new HttpGet(API_URI + "clean");
    }

    public static HttpUriRequest getFactoryData(String factoryID){
        return new HttpGet(API_URI + "factory?=" + factoryID);
    }

    public static HttpUriRequest getAllRequests(){
        return new HttpGet(API_URI + "requests");
    }

}
