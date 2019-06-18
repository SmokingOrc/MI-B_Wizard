# Wizard

Ziel des Spiels ist es, in jeder Runde korrekt vorherzusagen, wie oft man die höchste Karte (Stich) hat.

<h2>Spielbeschreibung:</h2>
In der ersten Runde bekommt jeder Spieler eine Karte und kann somit entweder 0 oder 1 Stich vorhersagen. Mit jeder Runde kommt eine Karte hinzu, wodurch die Anzahl der möglichen Stiche steigt.
Pro Runde wird eine Karte offen ausgelegt, welche die Trumpf-Farbe bestimmt, also die stärkste Farbe der Runde
Es gibt 13 Zahlen-Karten in 4 Farben sowie 4 Zauberer und 4 Narren.
Die Wertung der Karten ist wie folgt (absteigend):

- Zauberer
- 13-1 in Trumpf-Farbe
- 13-1 in der Farbe der ersten gelegten Karte
- alle anderen Zahlenkarten
- Narr



Zuerst muss jeder Spieler vorhersagen, wie viele Stiche er glaubt diese Runde zu machen. Die Vorhersage kann mittels Spracheingabe durchgeführt und einfach in das Smartdevice gesprochen oder auch manuell eingegeben werden.
Jetzt beginnt das eigentliche Spiel, indem ein vom System ausgewählter Spieler seine erste Karte spielt.
Jetzt werfen alle Spieler nacheinander in einer Reihenfolge, welche automatisch bestimmt wurde eine Karte ab.
Ab der zweiten Runde geht es damit weiter, dass der Gewinner des ersten Stichs die nächste Karte abwirft. Der ausgelegte Trumpf in der Mitte bleibt bis zum Ende der Runde bestehen.
Am Ende der Runde werden die Punkte automatisch berechnet und zum Ergebnis der Vorrunden addiert.




<h2>Punkteberechnung: </h2>
<p>
    <ul>
    <li>Für die korrekte Ansage der realisierten Stiche gibt es 20 Punkte.</li>
    <li>Für jeden Stich (bei korrekter Ansage) gibt es weitere 10 Punkte.</li>
    <li>Falls die Vorhersage nicht richtig war, werden pro falschen Stich 10 Minuspunkte notiert.</li>
    <li>Beispiel: 2 Stiche angesagt, 2 Stiche bekommen: 20 Punkte (richtige Vorhersage) + 2 * 10 Punkte = 40 Punkte</li>
    <li>Beispiel: 3 Stiche angesagt, 4 Stiche bekommen: 0 Punkte (falsche Ansage) - 10 Punkte = -10 Punkte.</li>


</ul>
</p>

<h2>Besonderheiten</h2>
<p>
  <ul>
    <li>Wenn mehrere Zauberer gespielt werden bekommt der erst gespielte Zauberer den Stich.</li>
    <li>Werden nur Narren abgeworfen, bekommt der erste gespielte Narr den Stich.</li>
    <li>Liegt ein Narr/ Zauberer als Trumpf in der Mitte, ist die Farbe des Narrs/Zauberers die Trumpffarbe.</li>
    <li>Liegt keine Karte als Trumpf in der Mitte (letzte Runde), entscheidet jeweils die erste Karte die Trumpffarbe.</li>
    <li>Durch Schütteln kann man eine Schummelfunktion aktivieren und die gesamten Karten der derzeitigen Runde anschauen. Wird man beim Schummeln erwischt bekommt man 50 abgezogen. Erwischt man selbst jemanden beim Schummeln bekommt man 50 Punkte dazu. 
 </ul>          
</p>


<h3>Startbildschirm:</h3>
<img src ="https://user-images.githubusercontent.com/46019040/59675742-33325700-91c6-11e9-80b9-f3a2a646b806.jpeg" width ="300px" >

<h3>Spielansicht bei laufendem Spiel:</h3>
<img src ="https://user-images.githubusercontent.com/46019040/59674833-5825ca80-91c4-11e9-8452-9b0122609d45.jpeg" width ="300px" >



<h2>Teammitglieder:</h2>
<p>
  <ul> 
<li>Martin Angermann</li>
<li>Erik Breznik</li>
<li>Christina Freundl</li>
<li>Julia Hummel</li>
<li>Martin Kristof</li>
  </ul> 
</p>
