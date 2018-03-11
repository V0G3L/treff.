package org.pispeb.treffpunkt.client.view.event;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import org.osmdroid.api.IMapController;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.overlay.MapEventsOverlay;
import org.pispeb.treffpunkt.client.R;
import org.pispeb.treffpunkt.client.data.entities.Event;
import org.pispeb.treffpunkt.client.databinding.ActivityEventBinding;
import org.pispeb.treffpunkt.client.view.group.GroupActivity;
import org.pispeb.treffpunkt.client.view.group.eventList.AddEventActivity;
import org.pispeb.treffpunkt.client.view.home.map.MapFragment;
import org.pispeb.treffpunkt.client.view.home.map.markers.EventMarker;
import org.pispeb.treffpunkt.client.view.util.TreffPunkt;
import org.pispeb.treffpunkt.client.view.util.ViewModelFactory;

/**
 * Created by Lukas on 3/11/2018.
 */

public class EventActivity extends AppCompatActivity {

    public static String EVENT_INTENT = "event";

    private int eventId;
    private Event event;

    private ActivityEventBinding binding;
    private EventViewModel vm;
    private IMapController controller;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_event);

        controller = binding.map.getController();
        controller.setZoom(MapFragment.STANDARD_ZOOM_LEVEL);
        binding.map.setMultiTouchControls(true);
        binding.map.setTilesScaledToDpi(true);

        eventId = (int) getIntent().getExtras().get(EVENT_INTENT);

        vm = ViewModelProviders
                .of(this, ViewModelFactory.getInstance(this))
                .get(EventViewModel.class);

        vm.setEventById(eventId);

        vm.getEvent().observe(this, event -> {
            this.event = event;
            if (event == null) {
                return;
            }
            binding.toolbar.setTitle(event.getName());
            binding.startDate.setText(date(event.getStart().toString()));
            binding.startTime.setText(time(event.getStart().toString()));
            binding.endDate.setText(date(event.getEnd().toString()));
            binding.endTime.setText(time(event.getEnd().toString()));

            controller.setCenter(new GeoPoint(event.getLocation().getLatitude
                    (), event.getLocation().getLongitude()));

            binding.map.getOverlays().clear();
            EventMarker marker = new EventMarker(binding.map, event);
            marker.setIcon(TreffPunkt.getAppContext()
                    .getDrawable(R.drawable.ic_marker_event));
            binding.map.getOverlays().add(marker);
        });

        //toolbar
        Toolbar toolbar = binding.toolbar;
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(v -> onBackPressed());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.event_bar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.edit_event) {

            Intent eventEditIntent = new Intent(this,
                    AddEventActivity.class);

            eventEditIntent
                    .putExtra(AddEventActivity.EVENT_ID, event.getId());
            eventEditIntent
                    .putExtra(AddEventActivity.EVENT_GROUPID, event.getGroupId());
            eventEditIntent
                    .putExtra(AddEventActivity.EVENT_NAME, event.getName());
            eventEditIntent
                    .putExtra(AddEventActivity.EVENT_LAT, event.getLocation()
                            .getLatitude());
            eventEditIntent
                    .putExtra(AddEventActivity.EVENT_LON, event.getLocation()
                            .getLongitude());
            eventEditIntent
                    .putExtra(AddEventActivity.EVENT_START, event.getStart()
                            .getTime());
            eventEditIntent
                    .putExtra(AddEventActivity.EVENT_END, event.getEnd().getTime());

            startActivity(eventEditIntent);

            return true;
        } else if (item.getItemId() == R.id.delete) {
            vm.deleteEvent(event);
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private String time(String date) {
        return date.substring(11, 19);
    }

    private String date(String date) {
        return date.substring(0, 10);
    }

}
