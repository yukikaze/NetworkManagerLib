package com.qware.library.networkmanagerlib;

import android.content.Context;
import android.net.NetworkInfo;
import android.util.Log;

import com.github.pwittchen.reactivenetwork.library.Connectivity;
import com.github.pwittchen.reactivenetwork.library.ConnectivityPredicate;
import com.github.pwittchen.reactivenetwork.library.ReactiveNetwork;

import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * Created by Matt_Chi on 2017/5/15.
 */

public class NetworkManager {

    public static final String TAG = "NetworkManager";
    private static NetworkManager instance;

    private Context context;
    private boolean hasNetwork;

    private Subscription subscription;
    private Subscription networkDisconnectedListener;
    private Subscription networkConnectedListener;

    public NetworkManager(Context context) {
        this.context = context;
        this.hasNetwork = true;
    }

    private Action1<Connectivity> createAction1() {
        return new Action1<Connectivity>() {
            @Override
            public void call(Connectivity connectivity) {
                if (connectivity.getState() == NetworkInfo.State.DISCONNECTED) {
                    hasNetwork = false;
                    Log.e(TAG, "hasNetwork = " + hasNetwork);
                } else {
                    hasNetwork = true;
                    Log.e(TAG, "hasNetwork = " + hasNetwork);
                }
            }
        };
    }

    public static NetworkManager getInstance(Context context) {
        if (instance == null) {
            instance = new NetworkManager(context);
        }
        return instance;
    }

    public boolean isHasNetwork() {
        if (instance == null) {
            return false;
        } else {
            return hasNetwork;
        }
    }

    public void subscribe() {
        subscription = ReactiveNetwork.observeNetworkConnectivity(context)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(createAction1());

    }

    public void subscribeNetworkDisconnectedListener(Action1<Connectivity> networkDisconnectedAction) {
        networkDisconnectedListener = ReactiveNetwork.observeNetworkConnectivity(context)
                .subscribeOn(Schedulers.io())
                .filter(ConnectivityPredicate.hasState(NetworkInfo.State.DISCONNECTED))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(networkDisconnectedAction);
    }

    public void subscribeNetworkConnectedListener(Action1<Connectivity> networkConnectedAction) {
        networkConnectedListener = ReactiveNetwork.observeNetworkConnectivity(context)
                .subscribeOn(Schedulers.io())
                .filter(ConnectivityPredicate.hasState(NetworkInfo.State.CONNECTED))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(networkConnectedAction);
    }

    public void unsubscribe() {
        subscription.unsubscribe();
        networkDisconnectedListener.unsubscribe();
        networkConnectedListener.unsubscribe();
    }


}
