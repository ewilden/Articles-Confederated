package com.codepath.nytimessearch;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import cz.msebera.android.httpclient.Header;

public class SearchActivity extends AppCompatActivity {

    //private final String API_KEY = "aded083163334d4fb0c54f6b9ede94ea";
    private final String API_KEY = "93718dded010432eb97833398a2db737";
    private final String SEARCH_URL = "https://api.nytimes.com/svc/search/v2/articlesearch.json";
    private final String TOP_URL = "https://api.nytimes.com/svc/topstories/v2/home.json";


    // @BindView(R.id.etQuery) EditText etQuery;
    @BindView(R.id.rvResults)
    RecyclerView rvResults;
    // @BindView(R.id.btnSearch) Button btnSearch;


    private ArrayList<Article> articles;
    private ArticleAdapter adapter;
    private Query lastQuery;
    private StaggeredGridLayoutManager gridLayoutManager;
    private MenuItem filterItem;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        ButterKnife.bind(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        articles = new ArrayList<>();
        adapter = new ArticleAdapter(articles);
        rvResults.setAdapter(adapter);
        gridLayoutManager =
                new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        rvResults.setLayoutManager(gridLayoutManager);
        setUpClickListener();

        topSearch();

    }

    public void loadMoreArticles(int offset) {
        // Use API to get more items to add to the articles list
        if (offset > 99) {
            return; // limit of 100 pages of results
        }
        articleSearch(offset);
    }

    private void setUpClickListener() {
        ItemClickSupport.addTo(rvResults).setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
            @Override
            public void onItemClicked(RecyclerView recyclerView, int position, View v) {
                Intent i = new Intent(getApplicationContext(), ArticleActivity.class);

                // get the article to display
                Article article = articles.get(position);

                // pass in that article into intent
                i.putExtra("article", article);

                // launch the activity
                startActivity(i);
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_search, menu);

        // hook up a listener for when a search is performed
        MenuItem searchItem = menu.findItem(R.id.action_search);
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // perform query here
                articleSearch(query);

                // workaround to avoid issues with some emulators and keyboard devices firing twice if a keyboard enter is used
                // see https://code.google.com/p/android/issues/detail?id=24599
                searchView.clearFocus();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        filterItem = menu.findItem(R.id.action_filter);
        filterItem.setVisible(false);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void topSearch() {
        RequestParams params = new RequestParams();
        AsyncHttpClient client = new AsyncHttpClient();
        params.put("api-key", API_KEY);
        client.get(TOP_URL, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                JSONArray articleJsonResults = null;
                try {
                    articleJsonResults = response.getJSONArray("results");
                    articles.clear();
                    articles.addAll(Article.fromJSONArray(articleJsonResults, true));
                    adapter.notifyDataSetChanged();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                Toast.makeText(SearchActivity.this, "Top stories failed!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void articleSearch(int offset) {
        Query query = new Query(lastQuery, offset);
        articleSearch(query, false);
    }

    private void articleSearch(String queryStr) {
        filterItem.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        filterItem.setVisible(true);
        Query query;
        if (lastQuery == null) {
            query = new Query(queryStr);
        } else {
            query = new Query(lastQuery, queryStr);
        }
        lastQuery = query;
        articleSearch(query, true);
    }

    private void articleSearch(Query query, boolean clear) {
        RequestParams params = query.getParams(API_KEY);

        AsyncHttpClient client = new AsyncHttpClient();

        if (clear) {
            rvResults.clearOnScrollListeners();
            rvResults.addOnScrollListener(new EndlessRecyclerViewScrollListener(gridLayoutManager) {
                @Override
                public void onLoadMore(int page, int totalItemsCount) {
                    loadMoreArticles(page);
                }
            });
            client.get(SEARCH_URL, params, new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    //Log.d("DEBUG", response.toString());
                    JSONArray articleJsonResults = null;
                    //Toast.makeText(SearchActivity.this, "Initial search succeeded!", Toast.LENGTH_SHORT).show();
                    try {
                        articleJsonResults = response.getJSONObject("response").getJSONArray("docs");
                        //Log.d("DEBUG", articleJsonResults.toString());
                        articles.clear();
                        articles.addAll(Article.fromJSONArray(articleJsonResults, false));
                        adapter.notifyDataSetChanged();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                    super.onFailure(statusCode, headers, throwable, errorResponse);
                    Log.d("SearchActivity", String.valueOf(errorResponse));
                }
            });
        } else {
            client.get(SEARCH_URL, params, new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

                    //Toast.makeText(SearchActivity.this, "Loading more articles succeeded!", Toast.LENGTH_SHORT).show();
                    //Log.d("DEBUG", response.toString());
                    JSONArray articleJsonResults = null;

                    try {
                        articleJsonResults = response.getJSONObject("response").getJSONArray("docs");
                        //Log.d("DEBUG", articleJsonResults.toString());
                        articles.addAll(Article.fromJSONArray(articleJsonResults, false));
                        int curSize = adapter.getItemCount();
                        adapter.notifyItemRangeInserted(curSize, articles.size() - 1);
                        //adapter.notifyDataSetChanged();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                    //Log.d("SearchActivity", "loadMoreArticles failed!");
                    Toast.makeText(SearchActivity.this, "Loading more articles failed!", Toast.LENGTH_SHORT).show();
                    //super.onFailure(statusCode, headers, throwable, errorResponse);
                }
            });
        }
    }

    /*
    public void onFilter(MenuItem mi) {
        FragmentManager fm = getSupportFragmentManager();
        FilterFragment ff = FilterFragment.newInstance("title");
        ff.show(fm, "fragment_filter");

    }
    */

    private final int REQUEST_CODE = 23;

    public void onFilter(MenuItem mi) {
        Intent i = new Intent(SearchActivity.this, FilterActivity.class);
        i.putExtra("query", lastQuery);
        startActivityForResult(i, REQUEST_CODE);
    }

    /*
    public void onArticleSearch(View view) {
        String query = etQuery.getText().toString();
        articleSearch(query, SEARCH_URL);
    }
    */

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        switch (requestCode) {
            case REQUEST_CODE:
                if (resultCode == RESULT_OK) {
                    Query q = data.getParcelableExtra("query");
                    lastQuery = q;
                    articleSearch(q, true);
                } else {
                    // Handle failure case
                }
        }
    }
}
