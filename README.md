# itmo-java

## ExpressionParser. Вычисление в различных типах с обработкой ошибок
 Вычисляет обычные строковые выражения с переменными в различных типах
Модификации
 * *Базовая*
    * Класс `GenericTabulator` должен строить трехмерную таблицу значений заданного выражения.
        * `mode` — режим вычислений:
           * `i` — вычисления в `int` с проверкой на переполнение;
           * `d` — вычисления в `double` без проверки на переполнение;
           * `bi` — вычисления в `BigInteger`.
        * `expression` — выражение, для которого надо построить таблицу;
        * `x1`, `x2` — минимальное и максимальное значения переменной `x` (включительно)
        * `y1`, `y2`, `z1`, `z2` — аналогично для `y` и `z`.
        * Результат: элемент `result[i][j][k]` должен содержать
          значение выражения для `x = x1 + i`, `y = y1 + j`, `z = z1 + k`.
          Если значение не определено (например, по причине переполнения),
          то соответствующий элемент должен быть равен `null`.
 * *AsmUfb*
    * Дополнительно реализовать унарные операции:
        * `abs` — модуль числа, `abs -5` равно 5;
        * `square` — возведение в квадрат, `square 5` равно 25.
    * Дополнительно реализовать бинарную операцию (максимальный приоритет):
        * `mod` — взятие по модулю, приоритет как у умножения (`1 + 5 mod 3` равно `1 + (5 mod 3)` равно `3`).
    * Дополнительно реализовать поддержку режимов:
        * `u` — вычисления в `int` без проверки на переполнение;
        * `f` — вычисления в `float` без проверки на переполнение;
        * `b` — вычисления в `byte` без проверки на переполнение.
   
## FastScanner. Быстрый сканнер на джава

## FunctionalExpressionParser(on js).Функциональные выражения на JavaScript

Модификации
 * *Базовая*
    * Код должен находиться в файле `functionalExpression.js`.
 * *PieAvgMed*. Дополнительно реализовать поддержку:
    * переменных: `y`, `z`;
    * констант:
        * `pi` — π;
        * `e` — основание натурального логарифма;
    * операций:
        * `avg5` — арифметическое среднее пяти аргументов, `1 2 3 4 5 avg5` равно 7.5;
        * `med3` — медиана трех аргументов, `1 2 -10 med3` равно 1.
 * *Variables*. Дополнительно реализовать поддержку:
    * переменных: `y`, `z`;
 * *OneIffAbs*. Дополнительно реализовать поддержку:
    * переменных: `y`, `z`;
    * констант:
        * `one` — 1;
        * `two` — 2;
    * операций:
        * `abs` — абсолютное значение, `-2 abs` равно 2;
        * `iff` — условный выбор:
            если первый аргумент неотрицательный,
            вернуть второй аргумент,
            иначе вернуть первый третий аргумент.
            * `iff one two 3` равно 2
            * `iff -1 -2 -3` равно -3
            * `iff 0 one two` равно 1;
 * *IffAbs*. Дополнительно реализовать поддержку:
    * переменных: `y`, `z`;
    * операций:
        * `abs` — абсолютное значение, `-2 abs` равно 2;
        * `iff` — условный выбор:
            если первый аргумент неотрицательный,
            вернуть второй аргумент,
            иначе вернуть первый третий аргумент:
            * `iff 1 2 3` равно 2
            * `iff -1 -2 -3` равно -3
            * `iff 0 1 2` равно 1;
 * *OneTwo*. Дополнительно реализовать поддержку:
    * переменных: `y`, `z`;
    * констант:
        * `one` — 1;
        * `two` — 2;
