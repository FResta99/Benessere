#include <Arduino.h>

#include <ESP8266WiFi.h>
#include <Servo.h>

#include "configMain.h"
#include "../configWiFi.h"
#include "../configServer.h"

void checkTimeoutTurnstile();

WiFiServer server(portServer);

Servo turnstile;
unsigned long timeoutTurnstile = 0;

void setup() {
  Serial.begin(115200);

  Serial.print("\nConnection to WiFi..");
  WiFi.begin(wifiSSID, wifiPassword);
  WiFi.hostname(wifiHostname);
  while (WiFi.status() != WL_CONNECTED) {
    delay(500);
    Serial.print(".");
  }
  Serial.print("\n\tIP address: ");
  Serial.println(WiFi.localIP());
  Serial.print("\tHostname: ");
  Serial.println(WiFi.hostname());

  server.begin();

  pinMode(pinLed, OUTPUT);
  digitalWrite(pinLed, HIGH);

  turnstile.attach(pinServo);
  turnstile.write(valueClose);
}

void loop() {
  WiFiClient client = server.available();
  if (client) {
    while (client.connected()) {
      int action = 0;

      while (client.available() != 0) {
        action = client.read();
        break;
      }
      delay(10);

      switch (action) {
          case '1':
            turnstile.write(valueOpen);
            timeoutTurnstile = millis() + timeOpenedTurnstile;
            action = 0;
            
            break;
      }

      checkTimeoutTurnstile();
    }
    client.stop();

    checkTimeoutTurnstile();
  }
}

void checkTimeoutTurnstile() {
  if ((millis() > timeoutTurnstile) && (timeoutTurnstile != 0)) {
    timeoutTurnstile = 0;
    turnstile.write(valueClose);
  }
}