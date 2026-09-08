/**
 * LoRaWAN Payload Decoder for The Things Network (TTN) & ChirpStack.
 * Decodes incoming raw binary sensor payload from Arduino LoRa edge nodes.
 *
 * Payload structure (6 bytes total):
 *  - Bytes 0-1: Temperature in Celsius (* 100)
 *  - Bytes 2-3: Gas concentration in PPM / analog val (* 100)
 *  - Bytes 4-5: Relative Humidity percentage (* 100)
 */

function Decoder(bytes, port) {
  var temperature = (bytes[0] << 8) | bytes[1];
  var gas = (bytes[2] << 8) | bytes[3];
  var humidity = (bytes[4] << 8) | bytes[5];

  return {
    celsius: temperature / 100.0,
    gas: gas / 100.0,
    humidity: humidity / 100.0
  };
}

// ChirpStack v4 / TTN v3 codec wrapper
function decodeUplink(input) {
  return {
    data: Decoder(input.bytes, input.fPort),
    warnings: [],
    errors: []
  };
}

if (typeof module !== 'undefined' && module.exports) {
  module.exports = { Decoder, decodeUplink };
}
