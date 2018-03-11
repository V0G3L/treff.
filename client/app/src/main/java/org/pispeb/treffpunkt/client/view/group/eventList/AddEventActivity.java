package org.pispeb.treffpunkt.client.view.group.eventList;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingUtil;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import org.osmdroid.api.IGeoPoint;
import org.osmdroid.api.IMapController;
import org.osmdroid.config.Configuration;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.pispeb.treffpunkt.client.R;
import org.pispeb.treffpunkt.client.databinding.ActivityAddEventBinding;
import org.pispeb.treffpunkt.client.view.home.map.MapFragment;
import org.pispeb.treffpunkt.client.view.util.State;
import org.pispeb.treffpunkt.client.view.util.ViewModelFactory;
import java.util.Calendar;
import java.util.Date;

/**
 * Lets the user create a new event inside a group
 */

public class AddEventActivity extends AppCompatActivity {

    public static final String EVENT_ID = "id";
    public static final String EVENT_GROUPID = "groupid";
    public static final String EVENT_NAME = "name";
    public static final String EVENT_LAT = "lat";
    public static final String EVENT_LON = "lon";
    public static final String EVENT_START = "start";
    public static final String EVENT_END = "end";

    private AddEventViewModel vm;
    private ActivityAddEventBinding binding;

    public static final String INTENT_GRP = "intentGroup";
    private MapView map;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil
                .setContentView(this, R.layout.activity_add_event);
        vm = ViewModelProviders.of(this,
                ViewModelFactory.getInstance(this))
                .get(AddEventViewModel.class);

        if (getIntent().getIntExtra(EVENT_ID, -1) != -1) {
            Log.i("Add Event", "Change Event");
            int id = getIntent().getIntExtra(EVENT_ID, -1);
            int groupId = getIntent().getIntExtra(EVENT_GROUPID, -1);
            String name = getIntent().getStringExtra(EVENT_NAME);
            double lat = getIntent().getDoubleExtra(EVENT_LAT, -1);
            double lon = getIntent().getDoubleExtra(EVENT_LON, -1);
            long start = getIntent().getLongExtra(EVENT_START, -1);
            long end = getIntent().getLongExtra(EVENT_END, -1);

            Location l = new Location("gps");
            l.setLatitude(lat);
            l.setLongitude(lon);
            binding.map.getController().setCenter(new GeoPoint(lat, lon));
            vm.setupForEdit(id, groupId, name, l, new Date(start), new Date
                    (end));
        } else {
            int groupId = getIntent().getIntExtra(INTENT_GRP, -1);
            vm.setupForCreate(groupId);
            binding.map.getController().setCenter(new GeoPoint(49d, 9d));
        }

        binding.notifyChange();

        vm.getState().observe(this, state -> callback(state));

        vm.getStart().observe(this, c -> {
            int year, month, day, hour, minute;
            year = c.get(Calendar.YEAR);
            month = c.get(Calendar.MONTH);
            day = c.get(Calendar.DAY_OF_MONTH);
            hour = c.get(Calendar.HOUR_OF_DAY);
            minute = c.get(Calendar.MINUTE);
            binding.startDate.setText(day + "." + (month+1) + "." + year);
            binding.startTime.setText(hour + ":" + minute);
        });

        vm.getEnd().observe(this, c -> {
            int year, month, day, hour, minute;
            year = c.get(Calendar.YEAR);
            month = c.get(Calendar.MONTH);
            day = c.get(Calendar.DAY_OF_MONTH);
            hour = c.get(Calendar.HOUR_OF_DAY);
            minute = c.get(Calendar.MINUTE);
            binding.endDate.setText(day + "." + (month+1) + "." + year);
            binding.endTime.setText(hour + ":" + minute);
        });

        //toolbar
        Toolbar toolbar = binding.toolbar;
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(v -> onBackPressed());
        toolbar.inflateMenu(R.menu.ok_bar);

        Configuration.getInstance().load(this,
                PreferenceManager.getDefaultSharedPreferences(this));

        map = binding.map;
        map.setTileSource(TileSourceFactory.MAPNIK);
        map.setMultiTouchControls(true);
        map.setTilesScaledToDpi(true);
        IMapController controller = map.getController();
        controller.setZoom(MapFragment.STANDARD_ZOOM_LEVEL);

        binding.setVm(vm);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.ok_bar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.done) {
            IGeoPoint g = map.getMapCenter();
            Location l = new Location(LocationManager.GPS_PROVIDER);
            l.setLongitude(g.getLongitude());
            l.setLatitude(g.getLatitude());

            vm.onSaveClick(l);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }




    private void callback(State state) {
        switch (state.call) {
            case IDLE:
                break;
            case SUCCESS:
                finish();
                break;
            case SHOW_TIME_DIALOG:
                showTimeDialog();
                break;
            case SHOW_DATE_DIALOG:
                showDateDialog();
                break;
            default:
                Log.i("Add Event", "Illegal VM State");
        }
    }

    private void showDateDialog() {
        Calendar c = Calendar.getInstance();

        DatePickerDialog dialog = new DatePickerDialog(
                this, vm,
                c.get(Calendar.YEAR),
                c.get(Calendar.MONTH),
                c.get(Calendar.DAY_OF_MONTH));
        dialog.show();
    }

    private void showTimeDialog() {
        Calendar c = Calendar.getInstance();
        TimePickerDialog dialog = new TimePickerDialog(
                this, vm,
                c.get(Calendar.HOUR),
                c.get(Calendar.MINUTE),
                true);
        dialog.show();
    }

}
