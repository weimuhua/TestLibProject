package baidu.com.testlibproject.ui;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.widget.SearchView;

import baidu.com.testlibproject.R;

public class SearchViewActivity extends Activity implements SearchView.OnQueryTextListener {

    private ListView mListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_view_layout);
        initView();
    }

    private void initView() {
        SearchView searchView = findViewById(R.id.search_view);
        searchView.setQueryHint(getString(R.string.search));
        searchView.setSubmitButtonEnabled(true);
        searchView.setOnQueryTextListener(this);

        mListView = findViewById(R.id.search_view_listview);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.main_activity_item,
                getResources().getStringArray(R.array.search_view_arr));
        mListView.setAdapter(adapter);
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        Toast.makeText(this, "your choose is : " + query, Toast.LENGTH_LONG).show();
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        if (TextUtils.isEmpty(newText)) {
            mListView.clearTextFilter();
        } else {
            mListView.setFilterText(newText);
        }
        return true;
    }
}
