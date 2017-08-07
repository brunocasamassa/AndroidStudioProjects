package studio.brunocasamassa.ajudaquiapp;

import android.os.AsyncTask;

import org.jsoup.Jsoup;

import java.io.IOException;

/**
 * Created by bruno on 07/08/2017.
 */
public class VersionChecker extends AsyncTask<String, String, String> {

    String newVersion;


    @Override
    protected String doInBackground(String... params) {

        try {
            newVersion = Jsoup.connect("https://play.google.com/store/apps/details?id=" + "studio.brunocasamassa"+ ".ajudaquiapp")
                    .timeout(30000)
                    .userAgent("Mozilla/5.0 (Windows; U; WindowsNT 5.1; en-US; rv1.8.1.6) Gecko/20070725 Firefox/2.0.0.6")
                    .referrer("http://www.google.com")
                    .get()
                    .select("div[itemprop=softwareVersion]")
                    .first()
                    .ownText();

        } catch (IOException e) {
            e.printStackTrace();
        }

        return newVersion;
    }

    ;
}