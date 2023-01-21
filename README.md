# java-explore-with-me
Template repository for ExploreWithMe project.

https://github.com/az0vtsev/java-explore-with-me/pull/3 - pull request feature -> main

Ветка feature - реализация подписок:
    Предполагается, что подписки доступны только зарегистрированным пользователям.
    Запрос на подписку односторонний, требуется подтверждение.
    Эндпоинты:
    PrivateSubscriptionController:
    1. "GET /users/{userId}/subscribers?from=from&size=size" - пользователь userId получает список своих подписчиков.
    2. "GET /users/{userId}/subscriptions?from=from&size=size" - пользователь userId получает список своих подписок.
    3. "GET /users/{userId}/subscriptions/request" - пользователь userId получает список запросов на подписку
    в статусе WAITING.
    4. "POST users/{subscriberId}/subscribe/{publisherId}" - пользователь subscriberId подписывается 
    на пользователя publisherId. Допускается отправить заявку еще раз, после отклонения пользователем publisherId.
    5. "PATCH /users/{userId}/subscribers/request/{requestId}/confirm" - пользователь userId подтверждает
    запрос requestId на подписку. Подтвердить можно только заявку в статусе WAITING.
    6. "PATCH /users/{userId}/subscribers/request/{requestId}/confirm" - пользователь userId отклоняет
    запрос requestId на подписку. Отклонить можно только заявку в статусе WAITING.
    7. "DELETE users/{subscriberId}/subscribe/{publisherId}" - пользователь subscriberId отписывается
    от пользователя publisherId.
    PrivateEventController:
    8. "GET /users/{}/subscriptions/{}/events??rangeStart={rangeStart}&rangeEnd={rangeEnd}&from={from}&size={size}" - 
    пользователь userId получает список мероприятий, опубликованных пользователем publisherId в указанном диапазоне дат.