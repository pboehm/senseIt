# Projekt-Dokumentation

> Sensorik Vorlesung SS 2013 - Alternative Prüfungsleistung - Philipp Böhm

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

### Projekt-Daten

Alle projekt-spezifischen Daten (darunter die senseIt-App) sind unter folgender
URL bei Github erreichbar:

   [http://git.io/8AvXjg](http://git.io/8AvXjg)

### Einführung in die Android-Entwicklung

Die Entwicklung von Android-Apps geschieht in Java, in Verbindung mit einem
Android-spezifischen Framework, welches die Eigenheiten des Systems kapselt. Um
den Anforderungen von Smartphones zu entsprechen, kommt anstatt der
Java-Virtual-Machine eine Register-basierte Virtuelle Maschine zum Einsatz
(Dalvik). Diese VM benötigt ein besonderes Bytecode-Format, was dazu führt, dass
Android-Apps exklusiv auf Smartphones laufen, entgegen dem Java-Slogan "Write
once, run everywhere".

Entwickelt werden Android-Apps meistens mithilfe einer Entwicklungsumgebung
(IDE), die dem Entwickler alle wiederkehrenden Aufgaben abnimmt und die
Android-spezifischen Aufgaben vom Entwickler fern hält. Folgende IDEs können
verwendet werden:

  * [Eclipse mit ADT-Plugin](http://developer.android.com/sdk/index.html)
  * [Android Studio](http://developer.android.com/sdk/installing/studio.html)

Android Studio wurde im Rahmen der Google IO 2013 vorgestellt und wir
langfristig Eclipse als Standard-Plattform ersetzen. Die senseIt-App wurde mit
Android-Studio entwickelt und für neue Projekte wird der Einsatz von
Android-Studio empfohlen.

Android-Apps werden in Form einer `apk`-Datei verteilt, die vom Entwickler
signiert werden muss. Um eine App ohne den Umweg des Play Stores auf dem Gerät
installieren zu können, ist eine Einstellung auf dem Telefon zu aktivieren:

`Einstellungen` => `Sicherheit` => `Unbekannte Herkunft`

#### Softwaretechnische Grundlagen

Android-Apps bestehen aus sogenannten Activities, wobei es sich Instanzen der
Klasse `Activity` handelt. Jede "Bildschirmseite" wird dabei von einer
Java-Klasse implementiert, die ihrerseits von der Klasse `Activity` erbt.
Erbende Klassen erhalten dadurch Funktionalitäten ihrer Elternklasse und können
diese für Ihre eigenen Zwecke anpassen.

Activities haben einen bestimmten
[Lebenszyklus](http://developer.android.com/reference/android/app/Activity.html),
was bedeutet, dass Android bei jeder aktiven Activity, in wohl definierten
Fällen, bestimmte Methoden aufruft. In der erbenden Klasse können alle diese
Methoden überschrieben werden.

![Lebenszyklus einer Android-Activity mit entsprechenden Methoden](activity_lifecycle.png)

Eine der `Activity` sehr ähnliche Komponente ist der `Service`, der im Gegensatz
zur Activity im Hintergrund läuft und auch aktiv bleiben kann, wenn die
entsprechende App nicht mehr aktiv ist. In der senseIt-App wurde ein Service
implementiert, der die Umgebungsgeräusche durch das Mikrofon aufnimmt, auch wenn
die App gar nicht aktiv ist.

Ein weiteres Konzept, welches in Android häufig zum Einsatz kommt, ist der
`Listener`. Mittels eines `Listener` kann man sich auf bestimmte Ereignisse
registrieren, bei dessen Eintreten vom Betriebssystem eine entsprechende Methode
auf der eigenen Klasse ausgeführt wird. Für Sensor-Daten ist der
`SensorEventListener` zuständig, dessen Interface mann in seiner Activity
implementieren muss. Implementieren eines Interface erfolgt mit dem Hinzufügen
von `implements SensorEventListener` in die Klassen-Definition.  Außerdem müssen
die Methoden, die das Interface vorschreibt, implementiert werden. Für den
`SensorEventListener` wären das folgende:

* `onSensorChanged(SensorEvent event)` wird jeweils aufgerufen, wenn der Sensor
  neue Daten liefert. Im SensorEvent sind dann abhängig vom Sensor die
  verschiedenen Sensor-Daten enthalten. In dieser Methode wird also die meiste
  Logik implementiert werden, um die Sensordaten geeignet zu speichern bzw.
  schon auszuwerten.
* `onAccuracyChanged(Sensor sensor, int i)` wird aufgerufen, wenn sich die
  Genauigkeit eines Sensors verändert, was hier nicht näher betrachtet wird.

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
benötigt. Welche Daten der jeweilige Sensor liefert, ist der senseIt-App bzw.
der Android-API-Dokumentation zu entnehmen.

![Koordinatensystem, welches dem `SensorEvent` zugrunde liegt](axis_device.png)

#### Bewegungssensoren

- `TYPE_ACCELEROMETER`
- `TYPE_GRAVITY`
- `TYPE_GYROSCOPE`
- `TYPE_LINEAR_ACCELERATION`
- `TYPE_ROTATION_VECTOR`

[zur Dokumentation](http://developer.android.com/guide/topics/sensors/sensors_motion.html)

#### Umgebungssensoren

- `TYPE_LIGHT`
- `TYPE_AMBIENT_TEMPERATURE`
- `TYPE_PRESSURE`
- `TYPE_RELATIVE_HUMIDITY`
- `TYPE_TEMPERATURE`

[zur Dokumentation](http://developer.android.com/guide/topics/sensors/sensors_environment.html)

#### Lagesensoren

- `TYPE_MAGNETIC_FIELD`
- `TYPE_PROXIMITY`

[zur Dokumentation](http://developer.android.com/guide/topics/sensors/sensors_position.html)


### Möglichkeiten zur Speicherung von Sensordaten

Für die Speicherung der erfassten Sensor-Daten bieten sich mehrere Lösungen an:

- Speicherung in einer Datenbank auf dem Smartphone. Android stellt einer App
  `Sqlite`-Datenbanken zur Verfügung. Die Vorteile dieser Lösung sind
    - Daten können mittels `SQL` entsprechend abgefragt werden
    - Breite Tool-Unterstützung auf allen Betriebssystemen
    - Datenbank besteht aus einer einzoigen Datei, die einfach vom Smartphone
      ennommen werden kann.
- Speicherung in einem einfachen Textfile, z.B. im Format CSV. Vorteile:
    - Auf dem Telefon leichter zu implementieren
    - Abfragen auf den Daten nicht so leicht möglich, eventuell
      Programmieraufwand nötig

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
Beschleunigungssensors registriert und diese dann komponentenweise in das Log
schreibt. Dieser Code funktioniert für alle Sensoren und erfordert nur die
Anpassung der `onSensorChanged()`-Methode.

``` java
public class SensorExampleActivity extends Activity implements SensorEventListener{

    private final String TAG = "senseIt"; // für die Log-Ausgabe verwendet

    private SensorManager manager;
    private Sensor gravitySensor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        manager = (SensorManager) getSystemService(SENSOR_SERVICE);

        // wir wollen die Daten eines Beschleunigungssensors haben
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

### Möglichkeiten zur Laufzeitverlängerung

Um mit dem Smartphone über eine Dauer von 24 Stunden alle anfallenden
Sensor-Daten zu erfassen, sowie Geräusche über das Mikrofon aufzunehmen bedarf es
einiger Vorarbeiten, da alle aktiven Sensoren relativ viel Energie verbrauchen
und das Smartphone die 24 Stunden sonst nicht überstehen würde.

- Deaktivieren aller ungenutzten Funktionen, besonders aller
  Mobilfunk-Funktionalitäten (GSM, WLAN). Auf dem Smartphone erfüllt das, das
  Einstellen des Flugmodus.
- Erhöhen des Intervalls zwischen der Erfassung zweier Sensor-Daten, sodass der
  Sensor weniger aktiv ist und weniger Energie verbraucht.
- Benutzung eines [externen Battery-Packs](http://goo.gl/t7b0i) um das Smartphone
  während der Nutzung direkt wieder zu laden. Mit Einsatz eines solchen Geräts
  sollte eine Zeitspanne von 24 Stunden definitiv zu schaffen sein.
