# Курс OTUS Highload Architect

## Домашка №1
Приложение слушает на 6868 порту. 

Запуск - gradle таск `docker enable -> appUp` из Idea или `gradle appUp` из консоли

[Postman-коллекция для теста](https://www.postman.com/satellite-astronomer-74457762/workspace/otus-social/collection/24147546-06255d1b-628e-4227-96f5-959ddbefdbfc?action=share&creator=24147546)

## Домашка №2
Нагрузочный вариант поднимается по `docker enable -> loadTestUp`, если база не пуста, то скачивает дамп на 1 миллион юзеров и разворачивает его на тестовой базе. 
Тестовая аппка слушает на 16868, база на 13307. Нагрузочный вариант может выполняться параллельно с основным приложением.

[Генератор запросов](core/src/test/kotlin/info/hauu/highloadsocial/load/QueryAmountTest.kt)

[Детальный отчёт](core/src/test/resources/report/report.pdf)

## Домашка №3
Нагрузочный вариант поднимается по `docker enable -> loadTestUp`, если база не пуста, то скачивает дамп на 1 миллион юзеров и разворачивает его на тестовой базе.
Тестовая аппка слушает на 16868, база на 13307. Нагрузочный вариант может выполняться параллельно с основным приложением.

[Генератор запросов](core/src/test/kotlin/info/hauu/highloadsocial/load/QueryAmountTest.kt)

[Генератор запросов на запись](core/src/test/kotlin/info/hauu/highloadsocial/load/SyntheticLoadTest.kt)

[Детальный отчёт](core/src/test/resources/report/report_p2.pdf)

## Домашка №4
Нагрузочный вариант с разворачиванием предварительной базы пользователей/постов/подписок в этой итерации не подготовлен. 
Приложение слушает на 6868 порту.

Проверить функциональность можно при помощи [Postman-коллекции](https://www.postman.com/satellite-astronomer-74457762/workspace/otus-social/collection/24147546-06255d1b-628e-4227-96f5-959ddbefdbfc?action=share&creator=24147546).
В коллекции поддержаны запросы:
* на регистрацию двух пользователей (публикующего и подписавшегося)
* создание, получение и удаление постов
* подписку одного пользователя на другого 
* просмотр ленты со сдвигом/лимитом.

Убедиться что сброс кэша при обновлении записей работают можно в логе приложения, записи класса ``CacheConfig`` вида:
```
2023-02-16 21:17:47 2023-02-16 18:17:47.971  INFO 1 --- [ntContainer#1-1] i.h.highloadsocial.config.CacheConfig    : Invalidated PostCacheChunk(author='89f0fc20-3a7b-4a85-943f-f5bab721fcbe', 1 subscribers) in 0 second(s)
```

## Домашка #5
