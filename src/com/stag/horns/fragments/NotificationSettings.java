 package com.stag.horns.fragments;

import com.android.internal.logging.nano.MetricsProto;

import android.os.Bundle;
import com.android.settings.R;
import android.provider.Settings;

import androidx.preference.Preference;
import androidx.preference.PreferenceCategory;
import androidx.preference.PreferenceScreen;

import com.android.settings.SettingsPreferenceFragment;

import net.margaritov.preference.colorpicker.ColorPickerPreference;
import com.stag.horns.preferences.Utils;
import com.stag.horns.preferences.SystemSettingMasterSwitchPreference;

public class NotificationSettings extends SettingsPreferenceFragment
        implements Preference.OnPreferenceChangeListener {

    private static final String INCALL_VIB_OPTIONS = "incall_vib_options";
    private static final String LIGHTS_CATEGORY = "notification_lights";
    private static final String BATTERY_LIGHT_ENABLED = "battery_light_enabled";
    private static final String PULSE_AMBIENT_LIGHT = "pulse_ambient_light";

    private PreferenceCategory mLightsCategory;
    private SystemSettingMasterSwitchPreference mBatteryLightEnabled;
    private SystemSettingMasterSwitchPreference mEdgePulse;

    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        addPreferencesFromResource(R.xml.horns_notifications);

        PreferenceScreen prefScreen = getPreferenceScreen();

        PreferenceCategory incallVibCategory = (PreferenceCategory) findPreference(INCALL_VIB_OPTIONS);
        if (!Utils.isVoiceCapable(getActivity())) {
            prefScreen.removePreference(incallVibCategory);
        }
        mBatteryLightEnabled = (SystemSettingMasterSwitchPreference) findPreference(BATTERY_LIGHT_ENABLED);
        mBatteryLightEnabled.setOnPreferenceChangeListener(this);
        int batteryLightEnabled = Settings.System.getInt(getContentResolver(),
                BATTERY_LIGHT_ENABLED, 1);
        mBatteryLightEnabled.setChecked(batteryLightEnabled != 0);

        mLightsCategory = (PreferenceCategory) findPreference(LIGHTS_CATEGORY);
        if (!getResources().getBoolean(com.android.internal.R.bool.config_hasNotificationLed)) {
            getPreferenceScreen().removePreference(mLightsCategory);
        }

        mEdgePulse = (SystemSettingMasterSwitchPreference) findPreference(PULSE_AMBIENT_LIGHT);
        mEdgePulse.setOnPreferenceChangeListener(this);
        int edgePulse = Settings.System.getInt(getContentResolver(),
                PULSE_AMBIENT_LIGHT, 0);
        mEdgePulse.setChecked(edgePulse != 0);
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {
        if (preference == mBatteryLightEnabled) {
            boolean value = (Boolean) newValue;
            Settings.System.putInt(getContentResolver(),
		            BATTERY_LIGHT_ENABLED, value ? 1 : 0);
            return true;
        } else if (preference == mEdgePulse) {
            boolean value = (Boolean) newValue;
            Settings.System.putInt(getContentResolver(),
		            PULSE_AMBIENT_LIGHT, value ? 1 : 0);
            return true;
	}
        return false;
    }

    @Override
    public int getMetricsCategory() {
        return MetricsProto.MetricsEvent.HORNS;
    }
}
