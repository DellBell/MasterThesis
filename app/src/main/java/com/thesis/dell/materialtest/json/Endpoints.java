package com.thesis.dell.materialtest.json;

/**
 * Created by DellMain on 30.09.2015.
 */

import static com.thesis.dell.materialtest.MyApplication.API_KEY;
import static com.thesis.dell.materialtest.MyApplication.FEED_ID;
import static com.thesis.dell.materialtest.extras.UrlEndpoints.URL_API_FEED_ENDPOINT;
import static com.thesis.dell.materialtest.extras.UrlEndpoints.URL_BASE;
import static com.thesis.dell.materialtest.extras.UrlEndpoints.URL_PARAM_KEY;
import static com.thesis.dell.materialtest.extras.UrlEndpoints.URL_QUESTION;

public class Endpoints {

    public static String getRequestUrlValues() {
        return URL_BASE
                + URL_API_FEED_ENDPOINT
                + FEED_ID
                + URL_QUESTION
                + URL_PARAM_KEY
                + API_KEY;
    }
}
