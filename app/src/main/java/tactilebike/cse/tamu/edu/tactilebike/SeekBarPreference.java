package tactilebike.cse.tamu.edu.tactilebike;

import android.content.Context;
import android.preference.Preference;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;

public class SeekBarPreference extends Preference implements OnSeekBarChangeListener {
    private SeekBar mSeekBar;
    private int mProgress;

    public SeekBarPreference(Context context) {
        super(context);
    }

    public SeekBarPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SeekBarPreference(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected View onCreateView(ViewGroup parent)	{
        super.onCreateView(parent);
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.seekbarpreference, parent, false);
        mSeekBar = (SeekBar) view.findViewById(R.id.seekBar);
        mSeekBar.setProgress(mProgress);
        mSeekBar.setOnSeekBarChangeListener(this);
        mSeekBar.setFocusable(true);
        this.setOnPreferenceClickListener(new OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                return false;
            }
        });
        return view;
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        if (!fromUser)
            return;
        String command = "";
        if (progress == 0)
        {
            command = "8";
        }
        else if (progress == 1)
        {
            command = "9";
        }
        else if (progress == 2)
        {
            command = "a";
        }
        SettingsTab.mChatService.write(command.getBytes());
        setValue(progress);
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
        // not used
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        // not used
    }

    @Override
    protected void onSetInitialValue(boolean restoreValue, Object defaultValue) {
        setValue(restoreValue ? getPersistedInt(mProgress) : (Integer) defaultValue);
    }

    public void setValue(int value) {
        if (shouldPersist()) {
            persistInt(value);
        }

        if (value != mProgress) {
            mProgress = value;
        }
    }
}