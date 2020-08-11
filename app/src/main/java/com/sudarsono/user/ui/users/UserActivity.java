package com.sudarsono.user.ui.users;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.sudarsono.user.R;
import com.sudarsono.user.dto.User;
import com.sudarsono.user.util.schedulers.SchedulerProvider;

import java.util.List;

public class UserActivity extends AppCompatActivity implements UserView {

    private UserPresenter presenter;

    private Handler searchFieldHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

        initListView();
        initSearchTextField();

        UserDataSource dataSource = new UserDataSource();
        presenter = new UserPresenterImpl(dataSource, this, SchedulerProvider.getInstance());
        presenter.subscribe();
    }

    private void initSearchTextField() {
        searchFieldHandler = new Handler();

        EditText etSearch = findViewById(R.id.etSearch);
        etSearch.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence text, int start, int before, int count) {
                findViewById(R.id.progressBar).setVisibility(View.VISIBLE);

                clearListView();
                addScrollListener();

                searchFieldHandler.removeCallbacksAndMessages(null);
                searchFieldHandler.postDelayed(() -> {
                    String query = etSearch.getText().toString();
                    presenter.findFirstPageUsers(query);
                }, 200);
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }

    private void clearListView() {
        findViewById(R.id.tvEndOfData).setVisibility(View.GONE);
        findViewById(R.id.tvWarningMessage).setVisibility(View.GONE);

        RecyclerView rvUser = findViewById(R.id.rvUser);
        UserAdapter adapter = (UserAdapter) rvUser.getAdapter();
        if (adapter != null) {
            adapter.clear();
        }
    }

    private void initListView() {
        RecyclerView rvUser = findViewById(R.id.rvUser);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        rvUser.setLayoutManager(layoutManager);
        UserAdapter adapter = new UserAdapter();
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(this,
                layoutManager.getOrientation());
        rvUser.addItemDecoration(dividerItemDecoration);
        rvUser.setAdapter(adapter);

        addScrollListener();
    }

    private void addScrollListener() {
        NestedScrollView nestedScrollView = findViewById(R.id.nestedScrollView);
        nestedScrollView.setOnScrollChangeListener(genScrollListener());
    }

    private void removeScrollListener() {
        NestedScrollView nestedScrollView = findViewById(R.id.nestedScrollView);
        nestedScrollView.setOnScrollChangeListener((NestedScrollView.OnScrollChangeListener) null);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        searchFieldHandler.removeCallbacksAndMessages(null);
        presenter.unsubscribe();
    }

    @Override
    public void showMovie(List<User> users) {
        findViewById(R.id.tvWarningMessage).setVisibility(View.GONE);
        RecyclerView rvUser = findViewById(R.id.rvUser);
        UserAdapter adapter = (UserAdapter) rvUser.getAdapter();
        if (adapter != null && !users.isEmpty()) {
            adapter.addItems(users);
        }
        findViewById(R.id.progressBar).setVisibility(View.GONE);
    }

    @Override
    public void displayEndOfData() {
        findViewById(R.id.progressBar).setVisibility(View.GONE);
        findViewById(R.id.tvEndOfData).setVisibility(View.VISIBLE);
        removeScrollListener();

        RecyclerView rvUser = findViewById(R.id.rvUser);
        UserAdapter adapter = (UserAdapter) rvUser.getAdapter();
        if (adapter != null && adapter.isEmpty()) {
            findViewById(R.id.tvEndOfData).setVisibility(View.GONE);
            TextView tvWarningMessage = findViewById(R.id.tvWarningMessage);
            tvWarningMessage.setText(R.string.no_data_to_display);
            tvWarningMessage.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void showLimitExceeded() {
        findViewById(R.id.progressBar).setVisibility(View.GONE);

        TextView tvWarningMessage = findViewById(R.id.tvWarningMessage);
        tvWarningMessage.setVisibility(View.VISIBLE);
        tvWarningMessage.setText(R.string.limit_exceeded);
    }

    @Override
    public void showNetworkError() {
        TextView tvWarningMessage = findViewById(R.id.tvWarningMessage);
        tvWarningMessage.setText(R.string.network_error);
        tvWarningMessage.setVisibility(View.VISIBLE);
        findViewById(R.id.progressBar).setVisibility(View.GONE);

        clearListView();
    }

    @Override
    public void setPresenter(UserPresenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void showGeneralError(String code, String message) {
        findViewById(R.id.progressBar).setVisibility(View.GONE);

        TextView tvWarningMessage = findViewById(R.id.tvWarningMessage);
        tvWarningMessage.setVisibility(View.VISIBLE);
        String text = message + "\n" + code;
        tvWarningMessage.setText(text);
    }

    private NestedScrollView.OnScrollChangeListener genScrollListener() {
        return (v, scrollX, scrollY, oldScrollX, oldScrollY) -> {
            if (scrollY == ( v.getChildAt(0).getMeasuredHeight() - v.getMeasuredHeight() )) {
                findViewById(R.id.progressBar).setVisibility(View.VISIBLE);
                EditText etSearch = findViewById(R.id.etSearch);
                presenter.findNextPageUsers(etSearch.getText().toString());
            }
        };
    }

}