package testapp.midtermexam02;

import android.app.ListActivity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class Midterm02 extends ListActivity {

    private ProgressDialog pDialog;
    private static String url="http://joseniandroid.herokuapp.com/api/books";

    private static final String TAG_books="BOOK";
    private static final String TAG_title="TITLE";
    private static final String TAG_genre="GENRE";
    private static final String TAG_author="AUTHOR";

    JSONArray books = null;

    // Hashmap for ListView
    ArrayList<HashMap<String, String>> bookList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_midterm02);

        bookList = new ArrayList<HashMap<String, String>>();

        ListView lv = getListView();



    }
    private class GetBooks extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog
            pDialog = new ProgressDialog(Midterm02.this);
            pDialog.setMessage("Please wait...");
            pDialog.setCancelable(false);
            pDialog.show();

        }

        @Override
        protected Void doInBackground(Void... arg0) {

            httpUtils http = new httpUtils();


            String jsonStr = http.getResponse(url,http.GET);

            Log.d("Response: ", "> " + jsonStr);

            if (jsonStr != null) {
                try {
                    JSONObject jsonObj = new JSONObject(jsonStr);


                   books = jsonObj.getJSONArray(TAG_books);


                    for (int i = 0; i < books.length(); i++) {
                        JSONObject b = books.getJSONObject(i);

                        String title = b.getString(TAG_title);
                        String genre = b.getString(TAG_genre);
                        String author = b.getString(TAG_author);



                        HashMap<String, String> book = new HashMap<String, String>();


                        book.put(TAG_title, title);
                        book.put(TAG_genre, genre);
                        book.put(TAG_author, author);



                        bookList.add(book);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                Log.d("httpUtils", "Couldn't get any data from the url");
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            // Dismiss the progress dialog
            if (pDialog.isShowing())
                pDialog.dismiss();
            /**
             * Updating parsed JSON data into ListView
             * */
            ListAdapter adapter = new SimpleAdapter(
                    Midterm02.this, bookList,
                    R.layout.list_item, new String[] { TAG_title, TAG_genre,
                    TAG_author }, new int[] { R.id.title,
                    R.id.genre, R.id.author });

            setListAdapter(adapter);
        }

    }



}
