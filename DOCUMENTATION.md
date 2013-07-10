# Projekt-Dokumentation

> Sensorik Vorlesung SS 2013 - Philipp Böhm

## Projekt 1: NI USB-6008 + Android Smartphone

Inhalt der Projektaufgabe ist folgendes:

> Entwickeln einer Ansteuerung für ein USB-Datenerfassungsmodul von National
> Instruments durch ein Android Smartphone zur Erfassung von Sensordaten.

### Durchführung

Das USB-Datenerfassungsmodul
[USB-6008](http://sine.ni.com/nips/cds/view/p/lang/de/nid/201986) (nachfolgend
DAQ genannt) ist für die Nutzung am PC, in Verbindung mit der Software
[LabView](http://www.ni.com/labview/d/) von National Instruments
konzipiert.  Die Kommunikation zwischen DAQ und LabView erfolgt über USB und
erfordert auf Seiten des PCs einen Treiber. Solche Treiber (darunter der
[DAQmx](http://www.ni.com/dataacquisition/d/nidaqmx.htm)) gibt es für die
Betriebssysteme Windows, Mac OS X und Linux, wobei unter Linux nur ein Bruchteil
der USB-Produkte, aufgrund von Lizenzproblemen, unterstützt werden.

Android unterstützt seit der Version 3.1 den sogenannten
[USB Host-Modus](http://developer.android.com/guide/topics/connectivity/usb/host.html),
was eine Vorraussetzung für den Betrieb des DAQ an einem Smartphone erfüllen
würde. NI stellt selber keine Treiber für Android bereit und wird dies in
Zukunft wohl [auch nicht tun wird](https://decibel.ni.com/content/thread/10827). Somit
würde das eine Eigenentwicklung erfordern, was durch das von NI bereitgestellte
[Driver Development Kit
(DDK)](http://sine.ni.com/nips/cds/view/p/lang/de/nid/11737) mit viel Aufwand
möglich sein sollte. Jedoch würde das immer noch eine LabView-Android-App
erfordern, was bezüglich der Komplexität von LabView eher unwahrscheinlich
ist.

Die Unterstützung von NI für Android besteht in der Bereitstellung einer App, um
Messdaten aus LabView auf mobilen Geräten (iOS, Android) darzustellen [zu
können](http://www.ni.com/white-paper/14033/de) Damit muss das
entsprechende Gerät aber weiterhin an einem PC betrieben werden. Für die meisten
seiner USB-Geräte bietet NI nicht einmal Unterstützung für Linux, was schon
einmal eine Vorraussetzung für Android gewesen wäre.

Einige neuere Produkte wie das NI cDAQ 9191 besitzen eine Wifi-Schnittstelle und
für die gibt es dann auch Unterstützung in Form von offiziellen Apps, die dann
die Rolle von LabView übernehmen.

### Fazit

Die Entwicklung eines eigenen Treibers hätte den Rahmen des Projektes gesprengt,
wenn es wegen der nicht-offenen Treiber-Sourcen überhaupt möglich gewesen wäre.

## Projekt 2: Sensoren in der Android-Plattform

Inhalt der Projektaufgabe ist folgendes:

> Untersuchen der Sensor-Unterstützung der Android-Plattform, sowie das
> Erstellen von Beispielcode für die Ansteuerung der einzelnen Sensoren.

Alle projekt-spezifischen Daten sind unter folgender URL einsehbar:

   [http://git.io/8AvXjg](http://git.io/8AvXjg)

### Sensoren in Android

Android bietet seit der ersten Version Unterstützung für verbaute Sensoren,
sowie der Verarbeitung der gelieferten Sensordaten. Den Zugriff auf vorhandene
Sensoren ermöglicht die Klasse
[`SensorManager`](http://developer.android.com/reference/android/hardware/SensorManager.html).
Sie enthält zwei wichtige Methoden (`registerListener()` und
`unregisterListener()`),
welche es einer App ermöglichen, sich für die Daten eines bestimmten Sensors zu
registrieren.

Sensoren werden in Android durch eine Instanz der Klasse
[`Sensor`](http://developer.android.com/reference/android/hardware/Sensor.html)
repräsentiert. Sie enthalten einige Methoden, um Daten über den Sensor
bereitzustellen:

- `getMaximumRange()` liefert die maximale Spanne der Werte in der jeweiligen
  Einheit des Sensors
- `getMinDelay()` liefert die minimale Verzögerung zwischen zwei Events
- `getName()` liefert die Bezeichnung eines Sensors
   (z.B. `K330 3-axis Accelerometer`)
- `getPower()` liefert den Strom in mA, den der Sensor benötigt, wenn er aktiv
  ist
- `getResolution()` liefert die Auflösung des Sensors in der jeweiligen Einheit
- `getType()` liefert den Typ des Sensors, in Form eine `int`-Konstante
- `getVendor()` liefert den Hersteller-Namen

### Unterstützte Sensor-Typen

Nachfolgend sind alle unterstützten Sensor-Typen aufgelistet, wobei immer eine
Konstante `TYPE_XXXXX` angegeben ist. Diese Konstanten sind in der Klasse
`Sensor` definiert und werden für den Zugriff auf den jeweiligen Sensor
benötigt. Außerdem werden die gelieferten Daten, in Form eines `SensorEvent`,
erklärt.

- `TYPE_ACCELEROMETER`
- `TYPE_GYROSCOPE`
- `TYPE_LINEAR_ACCELERATION`
- `TYPE_GRAVITY`
- `TYPE_ROTATION_VECTOR`
- `TYPE_AMBIENT_TEMPERATURE`
- `TYPE_LIGHT`
- `TYPE_MAGNETIC_FIELD`
- `TYPE_PRESSURE`
- `TYPE_PROXIMITY`
- `TYPE_RELATIVE_HUMIDITY`

### Sensordaten

![Koordinatensystem, welches dem `SensorEvent` zugrunde liegt](axis_device.png)

Error: Argument 0 for @NotNull parameter of com/intellij/openapi/util/io/FileUtil.toSystemDependentName must not be null
### Beispiel für Ansteuerung eines Sensors

Die Vorgehensweise für die Ansteuerung eines Sensors unter Android ist wie
folgt:

1. Implementieren einer Java-Klasse, welche das Interface `SensorEventListener`
   sowie die dazugehörigen Methode `onSensorChanged()` implementiert.
2. Instanziieren von `SensorManager` und des entsprechenden Sensors
3. Für Events mit dem entsprechenden Sensor beim `SensorManager`
registrieren und beim Beenden der App entregistrieren.
4. Dabei kann man sich auf eine beliebige Anzahl an Sensoren registrieren. Das
   an `onSensorChanged()` übergebene `SensorEvent` enthält einen Verweis auf den
   erzeugenden Sensor
5. Das Betriebssystem ruft nun bei jeder Veränderung des Sensors die Methode
   `onSensorChanged()` auf und übergibt die jeweiligen Sensordaten als Parameter
   (`SensorEvent`).

Folgender Beispielcode enthält eine Activity, welche sich für Daten des
Gravitationssensors registriert und diese dann komponentenweise in das Log
schreibt. Dieser Code funktioniert für alle Sensoren und erfordert nur die
Anpassung der `onSensorChanged()`-Methode

``` java
public class SensorExampleActivity extends Activity implements SensorEventListener{

    private final String TAG = "senseIt"; // für die Log-Ausgabe verwendet

    private SensorManager manager;
    private Sensor gravitySensor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        manager = (SensorManager) getSystemService(SENSOR_SERVICE);

        // wir wollen die Daten eines Gravitationssensors haben
        gravitySensor = manager.getDefaultSensor(Sensor.TYPE_GRAVITY);
    }

    /**
     * Wird jeweils dann aufgerufen, wenn die Activity sichtbar wird
     */
    @Override
    protected void onResume() {
        super.onResume();
        manager.registerListener(this, gravitySensor,
            SensorManager.SENSOR_DELAY_NORMAL);
    }

    /**
     * Wird jeweils dann aufgerufen, wenn die Activity nicht mehr sichtbar
     * ist, also z.B. die App beendet wird oder durch eine andere
     * Activity überdeckt wird
     */
    @Override
    protected void onPause() {
        super.onPause();
        manager.unregisterListener(this);
    }

    /**
     * Die Methode wird jeweils aufgerufen, wenn der Sensor neue Daten liefert
     *
     * Die Daten stecken jeweils in sensorEvent.values, einem float-Array. Die
     * Anzahl der Werte ist abhängig vom Sensor.
     */
    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        Log.d(TAG, "x = " + sensorEvent.values[0]);
        Log.d(TAG, "y = " + sensorEvent.values[1]);
        Log.d(TAG, "z = " + sensorEvent.values[2]);
    }

    /**
     * Wird aufgerufen, wenn sich die Genauigkeit des entsprechenden
     * Sensors ändert. Wird hier nicht weiter betrachtet.
     */
    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {
    }
}
```

## Möglichkeiten zur Laufzeitverlängerung

- Bentzung eines externen Battery-Packs
- Deaktivieren aller ungenutzter Funktionen (GSM, WLAN)
- Erhöhen des 
