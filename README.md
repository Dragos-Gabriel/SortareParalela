# SortareParalela
Comparare intre sortare paralela si secventiala

      Proiectul meu se numește „Sortare în paralel” și are ca scop compararea performanței dintre sortarea secvențială și sortarea paralelă în Java.
     Aplicația este realizată în Java 17 și este împărțită în mai multe clase, fiecare având o responsabilitate bine definită.
      În primul rând, aplicația generează vectori cu 100.000, 500.000, 1.000.000 și 5.000.000 de numere.
       Pentru fiecare vector folosesc două metode de sortare. Prima este Arrays.sort(), care reprezintă abordarea secvențială, iar a doua este Arrays.parallelSort(), care reprezintă abordarea paralelă și folosește mecanismele Fork/Join ale Java.
       Pentru ca experimentul să fie corect, generez un vector inițial și creez două copii identice.     O copie este sortată cu Arrays.sort(), iar cealaltă cu Arrays.parallelSort().
      Timpul de execuție este măsurat folosind System.nanoTime(). Rezultatele sunt memorate și afișate într-un tabel în consolă.
       Aplicația permite alegerea numărului de elemente și a numărului de teste. Pot să testez o singură dimensiune sau toate cele patru dimensiuni.
        Rezultatele pot fi salvate într-un fișier CSV, pe care îl pot deschide ulterior în Excel. Aplicația va genera și un grafic pentru compararea timpilor de execuție.
       Un aspect important este că după fiecare sortare verific dacă vectorul este sortat corect și verific și dacă rezultatele celor două metode sunt identice.( După fiecare sortare verific dacă vectorul este ordonat crescător folosind metoda isSorted(). În plus, compar rezultatele obținute cu Arrays.sort() și Arrays.parallelSort() folosind Arrays.equals(), pentru a confirma că ambele metode produc exact același rezultat.)
        Prin acest experiment pot observa cum se modifică timpul de execuție atunci când crește numărul de elemente și pot compara experimental cele două abordări: secvențială și paralelă.
        Scopul proiectului este astfel să evidențieze impactul dimensiunii datelor asupra performanței și avantajele, respectiv limitele, sortării paralele.

