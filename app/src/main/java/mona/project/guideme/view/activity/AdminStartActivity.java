package mona.project.guideme.view.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import mona.project.guideme.BaseActivity;
import mona.project.guideme.R;
import mona.project.guideme.view.fragment.adminCycle.AdminHomeFragment;

import static mona.project.guideme.helper.HelperMethod.replaceFragment;

public class AdminStartActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_start);

        replaceFragment(getSupportFragmentManager(), R.id.admin_container, new AdminHomeFragment());

    }
}
