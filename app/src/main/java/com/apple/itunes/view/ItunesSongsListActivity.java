package com.apple.itunes.view;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.apple.itunes.controller.SongsListController;
import com.apple.itunes.controller.SongsViewModelProvider;
import com.apple.itunes.controller.services.helper.SongListAdapterAdapter;
import com.apple.itunes.model.SongListModel;
import com.apple.itunes.R;
import com.apple.itunes.controller.services.helper.LCEStatus;
import com.apple.itunes.controller.services.IRemoteServices;
import com.apple.itunes.controller.services.LocalServices;
import com.apple.itunes.controller.services.RemoteServices;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class ItunesSongsListActivity extends AppCompatActivity implements androidx.appcompat.widget.SearchView.OnQueryTextListener{

    @Bind(R.id.songs_list_recycler_view)
    RecyclerView songsListRecyclerView;

    @Bind(R.id.no_data_found_tv)
    TextView noDataTv;

    private ProgressDialog mProgressDialog;
    private MaterialDialog errorDialog;

    private SongsListController viewModel;
    private SongListAdapterAdapter itunesSongsListAdapter;
    private ArrayList<SongListModel> itunesSongsList = new ArrayList<>();
    private  ArrayList<SongListModel> albumListSearch = new ArrayList<>();

    private SearchView searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        // Initializing  Butterknife library
        ButterKnife.bind(this);

        searchView = findViewById(R.id.searchView);
        searchView.setOnQueryTextListener(this);
        searchView.setIconified(false);


        // Initializing remote services and view model provide instance
        IRemoteServices remoteServices = new RemoteServices();
        SongsViewModelProvider postProvider = new SongsViewModelProvider(remoteServices, new LocalServices(), getApplicationContext());

        viewModel = ViewModelProviders.of(this, postProvider).get(SongsListController.class);

        itunesSongsListAdapter = new SongListAdapterAdapter(this, albumListSearch);
        songsListRecyclerView.setHasFixedSize(true);
        songsListRecyclerView.setAdapter(itunesSongsListAdapter);

        final int spanCount = getResources().getInteger(R.integer.grid_columns);
        final GridLayoutManager layoutManager = new GridLayoutManager(this, spanCount);
        songsListRecyclerView.setLayoutManager(layoutManager);


        setUpObservers();

        setupProgressBar();


    }


    public void getNewRequestData(View v){
        viewModel.getDataFromApi(searchQuerytext);
    }
    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }



    private void prepareSearchDataList() {


        try {
            if (itunesSongsList.size() > 0) {
                noDataTv.setVisibility(View.GONE);
            } else {
                noDataTv.setVisibility(View.VISIBLE);
            }

            albumListSearch.addAll(itunesSongsList);
            itunesSongsListAdapter.notifyDataSetChanged();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    String searchQuerytext;
    @Override
    public boolean onQueryTextChange(String query) {
        searchQuerytext = query;
        //if (query.equals("")) {
            albumListSearch.clear();
            prepareSearchDataList();

       // }
        List<SongListModel> filteredModelList = filter(albumListSearch, query);
        itunesSongsListAdapter.animateTo(filteredModelList);
        songsListRecyclerView.scrollToPosition(0);

        return true;
    }

    private List<SongListModel> filter(List<SongListModel> models, String query) {
        query = query.toLowerCase();

        final List<SongListModel> filteredModelList = new ArrayList<>();
        for (SongListModel model : models) {
            String text = model.getCollectionCensoredName().toLowerCase();
            //    String title = model.get().toLowerCase();
            if (text.contains(query)) {
                filteredModelList.add(model);
            }
        }
        return filteredModelList;
    }



    private void setUpObservers() {

        viewModel.getiTunesSearchApiData()
                .observe(this, this::updateDataset);

        viewModel.getLceStatus()
                .observe(this, this::showLceStatus);

        viewModel.getWarning()
                .observe(this, this::showToast);



    }

    protected void hideKeyboard() {
        if (getCurrentFocus() != null
                && getCurrentFocus().getWindowToken() != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(getCurrentFocus()
                            .getWindowToken(),
                    WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        }
    }
    private void updateDataset(ArrayList<SongListModel> data) {
        albumListSearch.clear();
        albumListSearch.addAll(data);
        itunesSongsList.clear();
        itunesSongsList.addAll(data);
        itunesSongsListAdapter.notifyDataSetChanged();

        if (itunesSongsList.size() > 0) {
            noDataTv.setVisibility(View.GONE);
        } else {
            noDataTv.setVisibility(View.VISIBLE);
        }

        hideKeyboard();
    }

    private void showLceStatus(LCEStatus status) {
        if (status.getStatus() == LCEStatus.Status.SUCCESS) {
            showProgress(false, "");
        } else if (status.getStatus() == LCEStatus.Status.ERROR) {
            showErrorAlertDialog(status.getTitle(), status.getMsg());
        } else if (status.getStatus() == LCEStatus.Status.LOADING) {
            showProgress(true, status.getMsg());
        }

    }
    private void setupProgressBar() {
        mProgressDialog = new ProgressDialog(this, R.style.AppCompatAlertDialogStyle);
        mProgressDialog.setMessage(getResources().getString(R.string.processing));
        mProgressDialog.setCancelable(false);
    }
    public void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
    private void showProgress(boolean showed, String msg) {
        if (showed) {
            if (!mProgressDialog.isShowing()) {
                if (!msg.equals("")) {
                    mProgressDialog.setMessage(msg);
                }
                mProgressDialog.show();
            }
        } else {
            if (mProgressDialog.isShowing()) {
                mProgressDialog.dismiss();
            }
        }
    }
    private void showErrorAlertDialog(String title, String msg) {
        if (errorDialog != null && errorDialog.isShowing()) {
            errorDialog.dismiss();
        }

        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }

        errorDialog = new MaterialDialog.Builder(this)
                .title(title)
                .content(msg)
                .positiveText("OK")
                .build();

        errorDialog.show();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.refresh_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.refreshBtn:
                     viewModel.getDataFromApi("jack+johnson");
                     break;

        }
        return super.onOptionsItemSelected(item);
    }

}
