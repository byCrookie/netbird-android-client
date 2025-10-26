package io.netbird.client.ui.home;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import io.netbird.client.R;

import java.text.DecimalFormat;

public class PeerDetailsDialogFragment extends DialogFragment {

    private static final String ARG_PEER = "peer";
    private Peer peer;

    public static PeerDetailsDialogFragment newInstance(Peer peer) {
        PeerDetailsDialogFragment fragment = new PeerDetailsDialogFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_PEER, peer);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, R.style.Theme_NetBird);
        if (getArguments() != null) {
            peer = (Peer) getArguments().getSerializable(ARG_PEER);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.dialog_peer_details, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (peer == null) {
            dismiss();
            return;
        }

        populateView(view);
    }

    public void setPeer(Peer peer) {
        this.peer = peer;
    }

    private void populateView(View view) {
        // FQDN
        TextView fqdnView = view.findViewById(R.id.peer_fqdn);
        fqdnView.setText(peer.getFqdn());

        // IP Address
        TextView ipView = view.findViewById(R.id.peer_ip);
        ipView.setText(peer.getIp());

        // Public Key
        TextView pubKeyView = view.findViewById(R.id.peer_public_key);
        String pubKey = peer.getPubKey();
        if (pubKey != null && !pubKey.isEmpty()) {
            pubKeyView.setText(pubKey);
        } else {
            pubKeyView.setText(R.string.peer_not_available);
        }

        // Status
        TextView statusView = view.findViewById(R.id.peer_status);
        statusView.setText(peer.getStatus().toString());

        // Connection Type
        TextView connTypeView = view.findViewById(R.id.peer_connection_type);
        connTypeView.setText(peer.getConnectionType());

        // Latency
        TextView latencyView = view.findViewById(R.id.peer_latency);
        View latencySection = view.findViewById(R.id.latency_section);
        if (peer.getLatency() > 0 && peer.getStatus() == Status.CONNECTED) {
            latencyView.setText(getString(R.string.peer_latency_ms, peer.getLatency()));
            latencySection.setVisibility(View.VISIBLE);
        } else {
            latencySection.setVisibility(View.GONE);
        }

        // Data Transfer
        TextView bytesRxView = view.findViewById(R.id.peer_bytes_rx);
        TextView bytesTxView = view.findViewById(R.id.peer_bytes_tx);
        View dataTransferSection = view.findViewById(R.id.data_transfer_section);
        
        if (peer.getStatus() == Status.CONNECTED && (peer.getBytesRx() > 0 || peer.getBytesTx() > 0)) {
            bytesRxView.setText(getString(R.string.peer_bytes_received, formatBytes(peer.getBytesRx())));
            bytesTxView.setText(getString(R.string.peer_bytes_sent, formatBytes(peer.getBytesTx())));
            dataTransferSection.setVisibility(View.VISIBLE);
        } else {
            dataTransferSection.setVisibility(View.GONE);
        }

        // ICE Candidates
        TextView localIceView = view.findViewById(R.id.peer_local_ice);
        TextView remoteIceView = view.findViewById(R.id.peer_remote_ice);
        View iceSection = view.findViewById(R.id.ice_section);
        
        String localType = peer.getLocalIceCandidateType();
        String localEndpoint = peer.getLocalIceCandidateEndpoint();
        String remoteType = peer.getRemoteIceCandidateType();
        String remoteEndpoint = peer.getRemoteIceCandidateEndpoint();
        
        if (peer.getStatus() == Status.CONNECTED && 
            ((localType != null && !localType.isEmpty()) || 
             (remoteType != null && !remoteType.isEmpty()))) {
            
            String localText = localType != null && !localType.isEmpty() ? localType : getString(R.string.peer_not_available);
            String localEndpointText = localEndpoint != null && !localEndpoint.isEmpty() ? localEndpoint : getString(R.string.peer_not_available);
            localIceView.setText(getString(R.string.peer_local_ice, localText, localEndpointText));
            
            String remoteText = remoteType != null && !remoteType.isEmpty() ? remoteType : getString(R.string.peer_not_available);
            String remoteEndpointText = remoteEndpoint != null && !remoteEndpoint.isEmpty() ? remoteEndpoint : getString(R.string.peer_not_available);
            remoteIceView.setText(getString(R.string.peer_remote_ice, remoteText, remoteEndpointText));
            
            iceSection.setVisibility(View.VISIBLE);
        } else {
            iceSection.setVisibility(View.GONE);
        }

        // Last WireGuard Handshake
        TextView handshakeView = view.findViewById(R.id.peer_last_handshake);
        View handshakeSection = view.findViewById(R.id.handshake_section);
        String lastHandshake = peer.getLastWireguardHandshake();
        
        if (peer.getStatus() == Status.CONNECTED && lastHandshake != null && !lastHandshake.isEmpty()) {
            handshakeView.setText(lastHandshake);
            handshakeSection.setVisibility(View.VISIBLE);
        } else {
            handshakeSection.setVisibility(View.GONE);
        }

        // Connection Status Update
        TextView statusUpdateView = view.findViewById(R.id.peer_status_update);
        View statusUpdateSection = view.findViewById(R.id.status_update_section);
        String statusUpdate = peer.getConnStatusUpdate();
        
        if (statusUpdate != null && !statusUpdate.isEmpty()) {
            statusUpdateView.setText(statusUpdate);
            statusUpdateSection.setVisibility(View.VISIBLE);
        } else {
            statusUpdateSection.setVisibility(View.GONE);
        }

        // Quantum Resistance (Rosenpass)
        TextView rosenpassView = view.findViewById(R.id.peer_rosenpass);
        View rosenpassSection = view.findViewById(R.id.rosenpass_section);
        
        if (peer.getStatus() == Status.CONNECTED) {
            rosenpassView.setText(peer.isRosenpassEnabled() ? 
                R.string.peer_rosenpass_enabled : R.string.peer_rosenpass_disabled);
            rosenpassSection.setVisibility(View.VISIBLE);
        } else {
            rosenpassSection.setVisibility(View.GONE);
        }

        // Close Button
        view.findViewById(R.id.btn_close).setOnClickListener(v -> dismiss());
    }

    private String formatBytes(long bytes) {
        if (bytes < 1024) {
            return bytes + " B";
        } else if (bytes < 1024 * 1024) {
            DecimalFormat df = new DecimalFormat("#.#");
            return df.format(bytes / 1024.0) + " KB";
        } else if (bytes < 1024 * 1024 * 1024) {
            DecimalFormat df = new DecimalFormat("#.##");
            return df.format(bytes / (1024.0 * 1024.0)) + " MB";
        } else {
            DecimalFormat df = new DecimalFormat("#.##");
            return df.format(bytes / (1024.0 * 1024.0 * 1024.0)) + " GB";
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        // Make the dialog use full width
        if (dialog.getWindow() != null) {
            dialog.getWindow().setLayout(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            );
        }
        return dialog;
    }

    @Override
    public void onStart() {
        super.onStart();
        if (getDialog() != null && getDialog().getWindow() != null) {
            // Set width to 90% of screen width
            int width = (int) (getResources().getDisplayMetrics().widthPixels * 0.9);
            getDialog().getWindow().setLayout(width, ViewGroup.LayoutParams.WRAP_CONTENT);
        }
    }
}
