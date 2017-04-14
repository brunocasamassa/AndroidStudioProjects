package studio.brunocasamassa.retrofit;

import java.util.List;

import JSON.Info;
import JSON.Results;

/**
 * Created by bruno on 13/04/2017.
 */

public class User {
    private List<Results> results = null;
    private Info info;

    public List<Results> getResults() {
        return results;
    }

    public void setResults(List<Results> results) {
        this.results = results;
    }

    public Info getInfo() {
        return info;
    }

    public void setInfo(Info info) {
        this.info = info;
    }

}
