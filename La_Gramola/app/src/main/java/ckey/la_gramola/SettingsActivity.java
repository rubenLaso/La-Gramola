package ckey.la_gramola;

import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.support.v7.widget.Toolbar;
import android.view.View;

/**
 * Created by ruben on 1/02/16.
 */
public class SettingsActivity extends PreferenceActivity implements Preference.OnPreferenceChangeListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pref_layout);
        /*findViewById(R.id.actionbar).setBackgroundResource(GetPreferences.getBackgroundColor(getApplicationContext()));
        GetPreferences.setColorScheme(this);*/

        Toolbar actionbar = (Toolbar) findViewById(R.id.actionbar);
        actionbar.setTitle(R.string.menu_settings);
        actionbar.setTitleTextColor(getResources().getColor(android.R.color.white));
        actionbar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_arrow_back_white_24dp));
        actionbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SettingsActivity.this.finish();
            }
        });

        addPreferencesFromResource(R.xml.pref_general);
        bindPreferenceSummaryToValue_Boolean(findPreference(getString(R.string.pref_descarga_letra_key)), new Boolean(getString(R.string.pref_descarga_letra_default_value)));
        bindPreferenceSummaryToValue_Boolean(findPreference(getString(R.string.pref_orden_aleatorio_key)), new Boolean(getString(R.string.pref_descarga_letra_default_value)));
        //bindPreferenceSummaryToValue_String(findPreference(getString(R.string.pref_color_key)));
    }

    /**
     * Attaches a listener so the summary is always updated with the preference value.
     * Also fires the listener once, to initialize the summary (so it shows up before the value
     * is changed.)
     */
    private void bindPreferenceSummaryToValue_String(Preference preference) {
        // Set the listener to watch for value changes.
        preference.setOnPreferenceChangeListener(this);

        // Trigger the listener immediately with the preference's
        // current value.
        onPreferenceChange(preference,
                PreferenceManager
                        .getDefaultSharedPreferences(preference.getContext())
                        .getString(preference.getKey(), ""));
    }

    private void bindPreferenceSummaryToValue_Boolean(Preference preference, boolean default_value) {
        // Set the listener to watch for value changes.
        preference.setOnPreferenceChangeListener(this);

        // Trigger the listener immediately with the preference's
        // current value.
        onPreferenceChange(preference,
                PreferenceManager
                        .getDefaultSharedPreferences(preference.getContext())
                        .getBoolean(preference.getKey(), default_value));
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {
        String stringValue = newValue.toString();

        if (preference instanceof ListPreference) {
            // For list preferences, look up the correct display value in
            // the preference's 'entries' list (since they have separate labels/values).
            ListPreference listPreference = (ListPreference) preference;
            int prefIndex = listPreference.findIndexOfValue(stringValue);
            if (prefIndex >= 0) {
                preference.setSummary(listPreference.getEntries()[prefIndex]);
            }
        } else {
            // For other preferences, set the summary to the value's simple string representation.
            preference.setSummary(stringValue);
        }

        return true;
    }
}
