#include <lmic.h>
#include <hal/hal.h>
#include <SPI.h>
#include <DHT.h>
#define DHTPIN 4
#define DHTTYPE DHT22
DHT dht(DHTPIN, DHTTYPE);

// LoRaWAN NwkSKey, network session key
// This is the default Semtech key, which is used by the early prototype TTN
// network.
static const PROGMEM u1_t NWKSKEY[16] = { 0x3D, 0x67, 0xB3, 0x27, 0xC7, 0x88, 0xCB, 0xB2, 0xDB, 0x81, 0xA0, 0x9D, 0x59, 0x68, 0xC7, 0x85 };

// LoRaWAN AppSKey, application session key
// This is the default Semtech key, which is used by the early prototype TTN
// network.
static const u1_t PROGMEM APPSKEY[16] = { 0x77, 0x41, 0xDC, 0xF5, 0x9E, 0xA3, 0x86, 0xCF, 0x4B, 0x97, 0x8B, 0x94, 0x2C, 0x20, 0x06, 0x90 };

// LoRaWAN end-device address (DevAddr)
static const u4_t DEVADDR = 0x2602143A ; // <-- Change this address for every node!

// These callbacks are only used in over-the-air activation, so they are
// left empty here (we cannot leave them out completely unless
// DISABLE_JOIN is set in config.h, otherwise the linker will complain).
void os_getArtEui (u1_t* buf) { }
void os_getDevEui (u1_t* buf) { }
void os_getDevKey (u1_t* buf) { }

static osjob_t sendjob;

// Schedule TX every this many seconds (might become longer due to duty
// cycle limitations).
unsigned TX_INTERVAL = 30;
boolean FIRE = false;

// Pin mapping
const lmic_pinmap lmic_pins = {
    .nss = 10,
    .rxtx = LMIC_UNUSED_PIN,
    .rst = 9,
    .dio = {2, 6, 7},
};

void onEvent (ev_t ev) {
    Serial.print(os_getTime());
    Serial.print(": ");
    switch(ev) {
        case EV_SCAN_TIMEOUT:
            Serial.println(F("EV_SCAN_TIMEOUT"));
            break;
        case EV_BEACON_FOUND:
            Serial.println(F("EV_BEACON_FOUND"));
            break;
        case EV_BEACON_MISSED:
            Serial.println(F("EV_BEACON_MISSED"));
            break;
        case EV_BEACON_TRACKED:
            Serial.println(F("EV_BEACON_TRACKED"));
            break;
        case EV_JOINING:
            Serial.println(F("EV_JOINING"));
            break;
        case EV_JOINED:
            Serial.println(F("EV_JOINED"));
            break;
        case EV_RFU1:
            Serial.println(F("EV_RFU1"));
            break;
        case EV_JOIN_FAILED:
            Serial.println(F("EV_JOIN_FAILED"));
            break;
        case EV_REJOIN_FAILED:
            Serial.println(F("EV_REJOIN_FAILED"));
            break;
        case EV_TXCOMPLETE:
            Serial.println(F("EV_TXCOMPLETE (includes waiting for RX windows)"));
            if (LMIC.txrxFlags & TXRX_ACK)
              Serial.println(F("Received ack"));
            if (LMIC.dataLen) {
              Serial.println(F("Received "));
              Serial.println(LMIC.dataLen);
              Serial.println(F(" bytes of payload"));
            }
            // Schedule next transmission
            os_setTimedCallback(&sendjob, os_getTime()+sec2osticks(TX_INTERVAL), do_send);
            break;
        case EV_LOST_TSYNC:
            Serial.println(F("EV_LOST_TSYNC"));
            break;
        case EV_RESET:
            Serial.println(F("EV_RESET"));
            break;
        case EV_RXCOMPLETE:
            // data received in ping slot
            Serial.println(F("EV_RXCOMPLETE"));
            break;
        case EV_LINK_DEAD:
            Serial.println(F("EV_LINK_DEAD"));
            break;
        case EV_LINK_ALIVE:
            Serial.println(F("EV_LINK_ALIVE"));
            break;
         default:
            Serial.println(F("Unknown event"));
            break;
    }
}

void do_send(osjob_t* j){
    // Check if there is not a current TX/RX job running
    if (LMIC.opmode & OP_TXRXPEND) {
        Serial.println(F("OP_TXRXPEND, not sending"));
    } else {
        // Prepare upstream data transmission at the next possible time.
        byte data[6];
        float celsius =  getTemperature();
        float gas = getGas();
        float humaidity = getHumidity();
        
        //translate float value to int 16 bit *100 get rid of the .
        int16_t temperature = (int16_t)(celsius * 100);
        int16_t barometer = (int16_t) (gas*100) ;
        //serialMonitor.println(barometer);
        int16_t humidity = (int16_t)(humaidity * 100);

        if (celsius > 75 || gas > 512 || humaidity < 2){
          FIRE = true;
        }

        //shift bits and store the value in bytes
        data[0] = temperature >> 8;
        data[1] = temperature & 0xFF;
        data[2] = barometer >>8;
        data[3] = barometer & 0xFF;
        data[4] = humidity >> 8;
        data[5] = humidity & 0xFF;
        
        LMIC_setTxData2(1, data, sizeof(data), 0);
        Serial.println(F("Packet queued"));
    }
    // Next TX is scheduled after TX_COMPLETE event.
}

void setup() {
    Serial.begin(115200);
    Serial.println(F("Starting"));
    const int DO = 3; 
    dht.begin();
    #ifdef VCC_ENABLE
    // For Pinoccio Scout boards
    pinMode(VCC_ENABLE, OUTPUT);
    digitalWrite(VCC_ENABLE, HIGH);
    delay(1000);
    #endif

    // LMIC init
    os_init();
    // Reset the MAC state. Session and pending data transfers will be discarded.
    LMIC_reset();
    // relaxed the timing
    LMIC_setClockError(MAX_CLOCK_ERROR * 1 / 100);
    LMIC_selectSubBand(1);

    //Disable FSB1, channels 0-7
    for (int i = 0; i <= 7; i++) 
    {
      LMIC_disableChannel(i); 
    } 

    //Disable channels 16-64
    for (int i = 16; i <= 64; i++) 
    {
      LMIC_disableChannel(i);
    }

    //Disable channels 66-72
    for (int i = 66; i <= 72; i++) 
    {
      LMIC_disableChannel(i);
    }
    // Set static session parameters. Instead of dynamically establishing a session
    // by joining the network, precomputed session parameters are be provided.
    #ifdef PROGMEM
    // On AVR, these values are stored in flash and only copied to RAM
    // once. Copy them to a temporary buffer here, LMIC_setSession will
    // copy them into a buffer of its own again.
    uint8_t appskey[sizeof(APPSKEY)];
    uint8_t nwkskey[sizeof(NWKSKEY)];
    memcpy_P(appskey, APPSKEY, sizeof(APPSKEY));
    memcpy_P(nwkskey, NWKSKEY, sizeof(NWKSKEY));
    LMIC_setSession (0x1, DEVADDR, nwkskey, appskey);
    LMIC.dn2Dr = DR_SF9;
    #else
    // If not running an AVR with PROGMEM, just use the arrays directly
    LMIC_setSession (0x1, DEVADDR, NWKSKEY, APPSKEY);
    #endif

    // Disable link check validation
    LMIC_setLinkCheckMode(0);

    // TTN uses SF9 for its RX2 window.
    LMIC.dn2Dr = DR_SF9;

    // Set data rate and transmit power for uplink (note: txpow seems to be ignored by the library)
    LMIC_setDrTxpow(DR_SF7,14);

    // Start job
    do_send(&sendjob);
}

void loop() {
    os_runloop_once();
}

void checkFire(){
  if(FIRE == true){
    TX_INTERVAL = 10;
  }
}

float getTemperature(){
  float temp = dht.readTemperature();
  Serial.println(temp);
  return temp;
}

float getGas(){
   float sensorValue = analogRead(A0);
   return sensorValue;
}

float getHumidity(){
  float humidity = dht.readHumidity();
  Serial.println(humidity);
  return humidity;
}
