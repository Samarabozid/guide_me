package mona.project.guideme.view.activity;


import android.os.Bundle;

import mona.project.guideme.BaseActivity;
import mona.project.guideme.R;
import mona.project.guideme.view.fragment.userCycle.UserHomeFragment;

import static mona.project.guideme.helper.HelperMethod.replaceFragment;

public class UserStartActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_start);

        replaceFragment(getSupportFragmentManager(),R.id.user_container, new UserHomeFragment());
    }
}
