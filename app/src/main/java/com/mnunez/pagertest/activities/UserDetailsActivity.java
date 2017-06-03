package com.mnunez.pagertest.activities;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.mnunez.pagertest.R;
import com.mnunez.pagertest.application.PagerTestApplication;
import com.mnunez.pagertest.fragments.StatusDialogFragment;
import com.mnunez.pagertest.models.AppInfoSingleton;
import com.mnunez.pagertest.models.User;
import com.mnunez.pagertest.utils.StringUtils;
import com.squareup.picasso.Picasso;

import java.util.Locale;

/**
 * Created by mnunez on 5/31/17.
 */

public class UserDetailsActivity extends Activity implements StatusDialogFragment.StatusHandlerListener {

    private ImageView vAvatar, vLocationFlag;
    private TextView vName, vRole, vGithub, vSkills, vLanguages, vGender, vStatus, vHeaderTitle;
    private User mUser;
    private boolean mIsUserLogged;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_details);
        mUser = (User) getIntent().getExtras().getSerializable("user");
        mUser.setRole(AppInfoSingleton.getInstance().getmRoles().get(mUser.getRoleId()));
        mIsUserLogged = getIntent().getExtras().getBoolean("isUser");
        vName = (TextView) findViewById(R.id.name);
        vHeaderTitle = (TextView) findViewById(R.id.header_title);
        vRole = (TextView) findViewById(R.id.role);
        vGithub = (TextView) findViewById(R.id.github);
        vSkills = (TextView) findViewById(R.id.skills);
        vLanguages = (TextView) findViewById(R.id.languages);
        vLocationFlag = (ImageView) findViewById(R.id.location);
        vGender = (TextView) findViewById(R.id.gender);
        vStatus = (TextView) findViewById(R.id.status);
        vHeaderTitle.setText(getIntent().getExtras().getString("header"));
        vAvatar = (ImageView) findViewById(R.id.avatar);
        Picasso.with(this).load(mUser.getAvatar()).resize(100, 100).centerCrop().placeholder(R.drawable.ic_user_placeholder).into(vAvatar);
        vName.setText(mUser.getName());
        vRole.setText(mUser.getRole().getDescription());
        vGithub.setText(mUser.getGithub());
        vGithub.setPaintFlags(vGithub.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        vGithub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String url = "https://github.com/";
                url = url.concat(mUser.getGithub());
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
            }
        });
        String skills = "";
        if (mUser.getTags() != null) {
            for (int i = 0; i < mUser.getTags().length; i++) {
                if (i == 0) {
                    skills = mUser.getTags()[i];
                } else {
                    skills = skills.concat(", ");
                    skills = skills.concat(mUser.getTags()[i]);
                }
            }
        }
        vSkills.setText(skills);
        String langs = "";
        if (mUser.getLanguages() != null) {
            for (int i = 0; i < mUser.getLanguages().length; i++) {
                Locale locale = new Locale(mUser.getLanguages()[i]);
                if (i == 0) {
                    langs = locale.getDisplayLanguage();
                } else {
                    langs = langs.concat(", ");
                    langs = langs.concat(locale.getDisplayLanguage());
                }
            }
        }
        vLanguages.setText(langs);
        vLocationFlag.setImageDrawable(getResources().getDrawable(getResources().getIdentifier(mUser.getLocation(), "drawable", getPackageName())));
        vGender.setText(mUser.getGender());
        if (StringUtils.isNotEmpty(mUser.getStatus())) {
            vStatus.setText(mUser.getStatus());
        }
        if (mIsUserLogged) {
            vStatus.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_edit, 0);
            vStatus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    StatusDialogFragment statusDialogFragment = new StatusDialogFragment();
                    statusDialogFragment.show(getFragmentManager(), StatusDialogFragment.class.getSimpleName());
                }
            });
        }
    }


    @Override
    public void updateStatus(String status) {
        mUser.setStatus(status);
        vStatus.setText(mUser.getStatus());
    }
}
