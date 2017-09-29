package studio.brunocasamassa.retrofit;

import java.util.List;

import JSON.Info;
import JSON.Result;

/**
 * Created by bruno on 13/04/2017.
 */

public class User {
    private List<Result> results = null;
    private Info info;

    public List<Result> getResults() {
        return results;
    }

    public void setResults(List<Result> results) {
        this.results = results;
    }

/*    public Info getInfo() {
        return info;
    }

    public void setInfo(Info info) {
        this.info = info;
    }
*/
}
