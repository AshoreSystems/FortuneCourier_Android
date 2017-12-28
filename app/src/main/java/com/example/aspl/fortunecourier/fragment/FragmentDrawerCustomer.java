package com.example.aspl.fortunecourier.fragment;

/**
 * Created by Ravi on 29/07/15.
 */

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.aspl.fortunecourier.R;
import com.example.aspl.fortunecourier.activity.customer.UpdateProfileCustomerActivity;
import com.example.aspl.fortunecourier.adapter.NavigationDrawerAdapter;
import com.example.aspl.fortunecourier.model.NavDrawerItem;
import com.example.aspl.fortunecourier.utility.AppConstant;
import com.example.aspl.fortunecourier.utility.JSONConstant;
import com.example.aspl.fortunecourier.utility.RoundedImageView;
import com.example.aspl.fortunecourier.utility.SeparatorDecoration;
import com.example.aspl.fortunecourier.utility.SessionManager;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.example.aspl.fortunecourier.R.id.img_profile_pic;


public class FragmentDrawerCustomer extends Fragment {

    private static String TAG = FragmentDrawerCustomer.class.getSimpleName();

    private RecyclerView recyclerView;
    private ActionBarDrawerToggle mDrawerToggle;
    private DrawerLayout mDrawerLayout;
    private NavigationDrawerAdapter adapter;
    private View containerView;
    //private static String[] titles = null;
    static  List<String>  titles = new ArrayList<>();
    private SessionManager mSessionManager;
    private static int[] images = {R.drawable.ic_dashboard,R.drawable.ic_orderlist_red,R.drawable.ic_track_red,R.drawable.ic_history_red,R.drawable.ic_calculate_red,R.drawable.ic_update_profile_red,R.drawable.ic_change_password_red,R.drawable.ic_change_number_red,R.drawable.ic_share_app_red,R.drawable.ic_notifications_red,R.drawable.ic_contact_us_red,R.drawable.ic_logout};
    private FragmentDrawerListener drawerListener;
    TextView tv_username;
    RoundedImageView img_profile_pic;

    public FragmentDrawerCustomer() {

    }

    public void setDrawerListener(FragmentDrawerListener listener) {
        this.drawerListener = listener;
    }

    public static List<NavDrawerItem> getData() {
        List<NavDrawerItem> data = new ArrayList<>();


     /*   // preparing navigation drawer items
        for (int i = 0; i < titles.length; i++) {
            NavDrawerItem navItem = new NavDrawerItem();
            navItem.setTitle(titles[i]);
            navItem.setImages(images[i]);
            data.add(navItem);
        }*/
        for (int i = 0; i < titles.size(); i++) {
            NavDrawerItem navItem = new NavDrawerItem();
            navItem.setTitle(titles.get(i));
            navItem.setImages(images[i]);
            data.add(navItem);
        }
        return data;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // drawer labels
       // titles = getActivity().getResources().getStringArray(R.array.nav_drawer_labels);
        if(AppConstant.IS_USER_FROM_FACEBOOK_SIGNUP){
            //AppConstant.IS_USER_FROM_FACEBOOK_SIGNUP = false;
            titles = Arrays.asList(getActivity().getResources().getStringArray(R.array.nav_drawer_labels_for_set_password));
        }else {
            titles = Arrays.asList(getActivity().getResources().getStringArray(R.array.nav_drawer_labels));
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflating view layout
        View layout = inflater.inflate(R.layout.fragment_navigation_drawer, container, false);
        recyclerView = (RecyclerView) layout.findViewById(R.id.drawerList);
        mSessionManager = new SessionManager(getActivity());

        tv_username = (TextView) layout.findViewById(R.id.tv_username);
        img_profile_pic = (RoundedImageView) layout.findViewById(R.id.img_profile_pic);

        updateDrawerDetails();

        adapter = new NavigationDrawerAdapter(getActivity(), getData());
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
       // SeparatorDecoration decoration = new SeparatorDecoration(getActivity(), Color.parseColor("#dfaa35"), 1.5f);
        SeparatorDecoration decoration = new SeparatorDecoration(getActivity(), Color.LTGRAY, 1.5f);

        recyclerView.addItemDecoration(decoration);
        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getActivity(), recyclerView, new ClickListener() {
            @Override
            public void onClick(View view, int position) {
                drawerListener.onDrawerItemSelected(view, position);
                mDrawerLayout.closeDrawer(containerView);
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));

        return layout;
    }

   /* public  void showBackArrow(){
        getActivity().setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }*/

    public void updateDrawerDetails(){
        tv_username.setText(mSessionManager.getStringData(SessionManager.KEY_C_FIRST_NAME)+" "+mSessionManager.getStringData(SessionManager.KEY_C_LAST_NAME));
        Picasso.with(getActivity())
                .load(mSessionManager.getStringData(SessionManager.KEY_C_PROFILE_URL)) //extract as User instance method
                //.transform(new CropCircleTransformation())
                .resize(120, 120)
                .into(img_profile_pic);
    }


    public void setUp(int fragmentId, DrawerLayout drawerLayout, final Toolbar toolbar) {
        containerView = getActivity().findViewById(fragmentId);
        mDrawerLayout = drawerLayout;
        mDrawerToggle = new ActionBarDrawerToggle(getActivity(), drawerLayout, toolbar, R.string.drawer_open, R.string.drawer_close) {
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                getActivity().invalidateOptionsMenu();
                updateDrawerDetails();
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                getActivity().invalidateOptionsMenu();
            }

            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                super.onDrawerSlide(drawerView, slideOffset);
                toolbar.setAlpha(1 - slideOffset / 2);
            }
        };

        mDrawerLayout.setDrawerListener(mDrawerToggle);
        mDrawerLayout.post(new Runnable() {
            @Override
            public void run() {
                mDrawerToggle.syncState();
            }
        });

    }

    public static interface ClickListener {
        public void onClick(View view, int position);

        public void onLongClick(View view, int position);
    }

    static class RecyclerTouchListener implements RecyclerView.OnItemTouchListener {

        private GestureDetector gestureDetector;
        private ClickListener clickListener;

        public RecyclerTouchListener(Context context, final RecyclerView recyclerView, final ClickListener clickListener) {
            this.clickListener = clickListener;
            gestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
                @Override
                public boolean onSingleTapUp(MotionEvent e) {
                    return true;
                }

                @Override
                public void onLongPress(MotionEvent e) {
                    View child = recyclerView.findChildViewUnder(e.getX(), e.getY());
                    if (child != null && clickListener != null) {
                        clickListener.onLongClick(child, recyclerView.getChildPosition(child));
                    }
                }
            });
        }

        @Override
        public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {

            View child = rv.findChildViewUnder(e.getX(), e.getY());
            if (child != null && clickListener != null && gestureDetector.onTouchEvent(e)) {
                clickListener.onClick(child, rv.getChildPosition(child));
            }
            return false;
        }

        @Override
        public void onTouchEvent(RecyclerView rv, MotionEvent e) {
        }

        @Override
        public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

        }


    }

    public interface FragmentDrawerListener {
        public void onDrawerItemSelected(View view, int position);
    }
}
