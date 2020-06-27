package mona.project.guideme.view.activity;

import android.os.Bundle;

import mona.project.guideme.BaseActivity;
import mona.project.guideme.R;
import mona.project.guideme.view.fragment.splashCycle.SplashFragment;

import static mona.project.guideme.helper.HelperMethod.replaceFragment;

public class MainActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        replaceFragment(getSupportFragmentManager(),R.id.container, new SplashFragment());
    }
}
