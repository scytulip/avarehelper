/*
Copyright (c) 2012, Apps4Av Inc. (apps4av.com) 
All rights reserved.

Redistribution and use in source and binary forms, with or without modification, are permitted provided that the following conditions are met:

    * Redistributions of source code must retain the above copyright notice, this list of conditions and the following disclaimer.
    *     * Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following disclaimer in the documentation and/or other materials provided with the distribution.
    *
    *     THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
*/
package com.apps4av.avarehelper;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.apps4av.avarehelper.connections.Connection;
import com.apps4av.avarehelper.connections.ConnectionFactory;
import com.apps4av.avarehelper.storage.Preferences;
import com.apps4av.avarehelper.storage.SavedCheckbox;

import java.util.List;

/**
 * 
 * @author zkhan
 * 
 */
public class BlueToothOutFragment extends Fragment {

    private Connection mBt;
    private List<String> mList; 
    private Spinner mSpinner;
    private Context mContext;
    private Button mConnectButton;
    private SavedCheckbox mSecureCb;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        mContext = container.getContext();


        View view = inflater.inflate(R.layout.layout_ap, container, false);

        /*
         * List of BT devices is same
         */
        mBt = ConnectionFactory.getConnection("BlueToothConnectionOut", mContext);

        mList = mBt.getDevices();
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(mContext,
                android.R.layout.simple_spinner_item, mList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        mSpinner = (Spinner)view.findViewById(R.id.main_spinner_out);

        mSpinner.setAdapter(adapter);

        mSecureCb = (SavedCheckbox) view.findViewById(R.id.main_cb_btout);
        
        mConnectButton = (Button) view.findViewById(R.id.main_button_connect_out);
        mConnectButton.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                /*
                 * If connected, disconnect
                 */
                if (mBt.isConnected()) {
                    mBt.stop();
                    mBt.disconnect();
                    setStates();
                    return;
                }
                /*
                 * Connect to the given device in list
                 */
                String val = (String) mSpinner.getSelectedItem();
                if (null != val && (!mBt.isConnected())) {
                    mConnectButton.setText(getString(R.string.Connect));
                    mBt.connect(val, mSecureCb.isChecked());
                    if (mBt.isConnected()) {
                        mBt.start(new Preferences(getActivity()));
                    }
                    setStates();
                }
            }
        });

        setStates();
        return view;

    }
    
    /**
     * 
     */
    private void setStates() {
        if (mBt.isConnected()) {
            mConnectButton.setText(getString(R.string.Disconnect));
        } else {
            mConnectButton.setText(getString(R.string.Connect));
        }
        int loc = mList.indexOf(mBt.getConnDevice());
        if(loc >= 0) {
            mSpinner.setSelection(loc);            
        }
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

}