package io.netbird.client.ui.home;

import java.io.Serializable;
import java.time.Instant;

public class Peer implements Serializable {
   private static final long serialVersionUID = 1L;
   
   private final Status status;
   private final String ip;
   private final String fqdn;
   private final String pubKey;
   private final String localIceCandidateType;
   private final String remoteIceCandidateType;
   private final String localIceCandidateEndpoint;
   private final String remoteIceCandidateEndpoint;
   private final long bytesRx;
   private final long bytesTx;
   private final long latency; // in milliseconds
   private final boolean relayed;
   private final boolean direct;
   private final String connStatusUpdate;
   private final String lastWireguardHandshake;
   private final boolean rosenpassEnabled;

   public Peer(Status status, String ip, String fqdn, String pubKey,
               String localIceCandidateType, String remoteIceCandidateType,
               String localIceCandidateEndpoint, String remoteIceCandidateEndpoint,
               long bytesRx, long bytesTx, long latency, boolean relayed,
               boolean direct, String connStatusUpdate, String lastWireguardHandshake,
               boolean rosenpassEnabled) {
      this.status = status;
      this.ip = ip;
      this.fqdn = fqdn;
      this.pubKey = pubKey;
      this.localIceCandidateType = localIceCandidateType;
      this.remoteIceCandidateType = remoteIceCandidateType;
      this.localIceCandidateEndpoint = localIceCandidateEndpoint;
      this.remoteIceCandidateEndpoint = remoteIceCandidateEndpoint;
      this.bytesRx = bytesRx;
      this.bytesTx = bytesTx;
      this.latency = latency;
      this.relayed = relayed;
      this.direct = direct;
      this.connStatusUpdate = connStatusUpdate;
      this.lastWireguardHandshake = lastWireguardHandshake;
      this.rosenpassEnabled = rosenpassEnabled;
   }

   public Status getStatus() {
      return status;
   }

   public String getIp() {
      return ip;
   }

   public String getFqdn() {
      return fqdn;
   }

   public String getPubKey() {
      return pubKey;
   }

   public String getLocalIceCandidateType() {
      return localIceCandidateType;
   }

   public String getRemoteIceCandidateType() {
      return remoteIceCandidateType;
   }

   public String getLocalIceCandidateEndpoint() {
      return localIceCandidateEndpoint;
   }

   public String getRemoteIceCandidateEndpoint() {
      return remoteIceCandidateEndpoint;
   }

   public long getBytesRx() {
      return bytesRx;
   }

   public long getBytesTx() {
      return bytesTx;
   }

   public long getLatency() {
      return latency;
   }

   public boolean isRelayed() {
      return relayed;
   }

   public boolean isDirect() {
      return direct;
   }

   public String getConnStatusUpdate() {
      return connStatusUpdate;
   }

   public String getLastWireguardHandshake() {
      return lastWireguardHandshake;
   }

   public boolean isRosenpassEnabled() {
      return rosenpassEnabled;
   }

   public String getConnectionType() {
      if (relayed) {
         return "Relayed";
      } else if (direct) {
         return "Direct (P2P)";
      } else {
         return "Unknown";
      }
   }
}