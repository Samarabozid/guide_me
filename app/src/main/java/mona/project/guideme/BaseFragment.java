package mona.project.guideme;

import androidx.fragment.app.Fragment;

public class BaseFragment extends Fragment {
    public BaseActivity baseActivity;

    public void intiFragment() {
        baseActivity = (BaseActivity) getActivity();
        baseActivity.baseFragment = this;
    }

    public void onBack() {
        baseActivity.superBackPressed();
    }
}
