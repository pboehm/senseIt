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
