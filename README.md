# Курс OTUS Highload Architect

## Домашка №1
Приложение слушает на 6868 порту. 

Запуск - gradle таск `docker enable -> appUp` из Idea или `gradle appUp` из консоли

[Postman-коллекция для теста](https://www.postman.com/satellite-astronomer-74457762/workspace/otus-social/collection/24147546-06255d1b-628e-4227-96f5-959ddbefdbfc?action=share&creator=24147546)

## Домашка №2
Нагрузочный вариант поднимается по `docker enable -> loadTestUp`, если база не пуста, то скачивает дамп на 1 миллион юзеров и разворачивает его на тестовой базе. 
Тестовая аппка слушает на 16868, база на 13307. Нагрузочный вариант может выполняться параллельно с основным приложением.

[Генератор запросов](src/test/kotlin/info/hauu/highloadsocial/load/QueryAmountTest.kt)

[Детальный отчёт](src/test/resources/report/report.pdf)

## Домашка №3
Нагрузочный вариант поднимается по `docker enable -> loadTestUp`, если база не пуста, то скачивает дамп на 1 миллион юзеров и разворачивает его на тестовой базе.
Тестовая аппка слушает на 16868, база на 13307. Нагрузочный вариант может выполняться параллельно с основным приложением.

[Генератор запросов](src/test/kotlin/info/hauu/highloadsocial/load/QueryAmountTest.kt)

[Генератор запросов на запись](src/test/kotlin/info/hauu/highloadsocial/load/SyntheticLoadTest.kt)

[Детальный отчёт](src/test/resources/report/report_p2.pdf)

## Домашка №4

