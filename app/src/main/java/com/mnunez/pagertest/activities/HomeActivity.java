package com.mnunez.pagertest.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.mikhaellopez.circularimageview.CircularImageView;
import com.mnunez.pagertest.BuildConfig;
import com.mnunez.pagertest.R;
import com.mnunez.pagertest.adapters.UsersAdapter;
import com.mnunez.pagertest.application.PagerTestApplication;
import com.mnunez.pagertest.models.AppInfoSingleton;
import com.mnunez.pagertest.models.Role;
import com.mnunez.pagertest.models.SocketEventEnum;
import com.mnunez.pagertest.models.User;
import com.mnunez.pagertest.network.ApiHandler;
import com.mnunez.pagertest.utils.FilterUtils;
import com.mnunez.pagertest.utils.ParseUtils;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

import okhttp3.WebSocket;
import okhttp3.WebSocketListener;

public class HomeActivity extends Activity implements View.OnClickListener, UsersAdapter.NavigationDetailsListener, AdapterView.OnItemSelectedListener {

    private RecyclerView vUsersList;
    private CircularImageView vUserIcon;
    private TextView vHeaderTitle, vNoResults;
    private WebSocket mWebSocket;
    private UsersAdapter mUsersAdapter;
    private RelativeLayout vFilterLayout;
    private ImageView vFilterImage, vCloseImage;
    private Spinner vRolesSpinner;
    private Role mSelectedRole;
    private EditText vSkills, vStatus;
    private Button vClearFilter, vApplyFilter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        vUsersList = (RecyclerView) findViewById(R.id.users_list);
        vHeaderTitle = (TextView) findViewById(R.id.header_title);
        vUserIcon = (CircularImageView) findViewById(R.id.user_icon);
        vFilterLayout = (RelativeLayout) findViewById(R.id.filter_layout);
        vFilterImage = (ImageView) findViewById(R.id.filter_btn);
        vCloseImage = (ImageView) findViewById(R.id.close_filter_btn);
        vRolesSpinner = (Spinner) findViewById(R.id.role_spinner);
        vClearFilter = (Button) findViewById(R.id.cancel);
        vApplyFilter = (Button) findViewById(R.id.confirm);
        vSkills = (EditText) findViewById(R.id.skills);
        vStatus = (EditText) findViewById(R.id.status);
        vNoResults = (TextView) findViewById(R.id.no_results);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        vUsersList.setLayoutManager(layoutManager);
        mUsersAdapter = new UsersAdapter(this);
        vUsersList.setAdapter(mUsersAdapter);
        Picasso.with(this).load(AppInfoSingleton.getInstance().getmLoggedUser().getAvatar())
                .resize(30, 30).centerInside().placeholder(R.drawable.ic_user_placeholder).into(vUserIcon);
        vHeaderTitle.setText(getString(R.string.home_greet_message, AppInfoSingleton.getInstance().getmLoggedUser().getName()));
        vUserIcon.setOnClickListener(this);
        vFilterImage.setOnClickListener(this);
        vCloseImage.setOnClickListener(this);
        vClearFilter.setOnClickListener(this);
        vApplyFilter.setOnClickListener(this);
        handleRolesForSpinner();
    }

    private void handleWebSocket() {
        ApiHandler apihHandler = new ApiHandler();
        mWebSocket = apihHandler.createWebSocket(BuildConfig.WEB_SOCKET_URL, new WebSocketListener() {

            @Override
            public void onMessage(WebSocket webSocket, final String text) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        SocketEventEnum socketEventEnum = ParseUtils.parseEventType(text);
                        if (socketEventEnum != null) {
                            switch (socketEventEnum) {
                                case STATUS:
                                    handleSocketStatusChanged(text);
                                    break;
                                case USER:
                                    handleSocketUserNew(text);
                                    break;
                                default:
                                    break;
                            }
                        }
                    }
                });
            }
        });
    }

    @Override
    public void onClick(View view) {
        int viewId = view.getId();
        switch (viewId) {
            case R.id.user_icon:
                goToDetails(AppInfoSingleton.getInstance().getmLoggedUser(), getString(R.string.home_user_details), true);
                break;
            case R.id.filter_btn:
                showFilter();
                break;
            case R.id.close_filter_btn:
                hideFilter();
                break;
            case R.id.cancel:
                clearFilter();
                break;
            case R.id.confirm:
                applyFilter();
                break;
            default:
                break;
        }
    }

    @Override
    public void goToDetails(User user, String header, boolean isUserLogged) {
        Intent intent = new Intent(HomeActivity.this, UserDetailsActivity.class);
        Bundle extra = new Bundle();
        extra.putSerializable("user", user);
        extra.putString("header", header);
        extra.putBoolean("isUser", isUserLogged);
        intent.putExtras(extra);
        startActivity(intent);
    }

    private void handleSocketStatusChanged(String text) {
        ParseUtils.parseAndUpdateStatusChanged(text);
        mUsersAdapter.notifyDataSetChanged();
    }

    private void handleSocketUserNew(String text) {
        ParseUtils.parseAndUpdateUser(text);

        mUsersAdapter.notifyDataSetChanged();
    }

    private void showFilter() {
        if (vFilterLayout.getVisibility() == View.GONE) {
            vFilterLayout.setVisibility(View.VISIBLE);
            vFilterLayout.animate().translationY(0);
            vFilterLayout.bringToFront();
            vUsersList.setVisibility(View.GONE);
        }
    }

    private void hideFilter() {
        if (vFilterLayout.getVisibility() == View.VISIBLE) {
            vFilterLayout.setVisibility(View.GONE);
            vFilterLayout.animate().translationY(vFilterLayout.getHeight());
            vUsersList.setVisibility(View.VISIBLE);
            vUsersList.bringToFront();
        }
    }

    private void clearFilter() {
        mSelectedRole = null;
        vSkills.setText("");
        vStatus.setText("");
        AppInfoSingleton.getInstance().getmFilteredUsers().clear();
        AppInfoSingleton.getInstance().getmFilteredUsers().addAll(AppInfoSingleton.getInstance().getmUsers());
        mUsersAdapter.notifyDataSetChanged();
        hideFilter();

    }

    private void applyFilter() {
        Map<String, User> filter =
                FilterUtils.filterUsers(mSelectedRole, vSkills.getText().toString(), vStatus.getText().toString());
        mUsersAdapter.notifyDataSetChanged();
        hideFilter();
        if (filter.values().isEmpty()) {
            vNoResults.setVisibility(View.VISIBLE);
            vNoResults.bringToFront();
        } else {
            vNoResults.setVisibility(View.GONE);
        }
    }

    private void handleRolesForSpinner() {
        Collection<Role> rolesCollection = new ArrayList<>();
        rolesCollection.add(new Role(null, getString(R.string.home_filter_select_role)));
        rolesCollection.addAll(AppInfoSingleton.getInstance().getmRoles().values());
        ArrayAdapter<Role> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item,
                rolesCollection.toArray(new Role[rolesCollection.size()]));
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        vRolesSpinner.setAdapter(adapter);
        vRolesSpinner.setOnItemSelectedListener(this);
    }

    @Override
    public void onBackPressed() {
        if (vFilterLayout.getVisibility() == View.VISIBLE) {
            hideFilter();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        handleWebSocket();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mWebSocket.close(1000, "Activity Destroyed");
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        mSelectedRole = (Role) vRolesSpinner.getSelectedItem();
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
        mSelectedRole = null;
    }
}
